/*
 * Copyright (c) 2002-2019, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.portal.service.search;

import fr.paris.lutece.portal.business.indexeraction.IndexerAction;
import fr.paris.lutece.portal.business.indexeraction.IndexerActionFilter;
import fr.paris.lutece.portal.business.indexeraction.IndexerActionHome;
import fr.paris.lutece.portal.business.search.IndexationMode;
import fr.paris.lutece.portal.business.search.IndexationItemLog;
import fr.paris.lutece.portal.business.search.IndexationInformation;
import fr.paris.lutece.portal.business.search.GeneralIndexLog;
import fr.paris.lutece.portal.business.search.AllIndexationInformations;
import fr.paris.lutece.portal.service.init.LuteceInitException;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.util.IndexationLogService;

import org.apache.commons.lang.StringUtils;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;

import java.nio.file.Paths;
import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * This class provides management methods for indexing
 */
public final class IndexationService
{
    // Constants corresponding to the variables defined in the lutece.properties file
    // file
    public static final String PATH_INDEX = "search.lucene.indexPath";
    public static final String PATH_INDEX_IN_WEBAPP = "search.lucene.indexInWebapp";
    public static final String PARAM_FORCING = "forcing";
    public static final int ALL_DOCUMENT = -1;
    public static final Version LUCENE_INDEX_VERSION = Version.LATEST;
    private static final String PARAM_TYPE_PAGE = "Page";
    private static final String PROPERTY_WRITER_MERGE_FACTOR = "search.lucene.writer.mergeFactor";
    private static final String PROPERTY_WRITER_MAX_FIELD_LENGTH = "search.lucene.writer.maxFieldLength";
    private static final String PROPERTY_ANALYSER_CLASS_NAME = "search.lucene.analyser.className";
    private static final int DEFAULT_WRITER_MERGE_FACTOR = 20;
    private static final int DEFAULT_WRITER_MAX_FIELD_LENGTH = 1000000;
    
    // Buffer for Logs
    private static IndexationInformation _indexationInformation = new IndexationInformation( );
    private static GeneralIndexLog _generalIndexLog = new GeneralIndexLog( );
    private static Map<String, IndexationInformation> _mapIndexationInformation = new HashMap<>( );
    private static IndexationItemLog _indexationItemLog = new IndexationItemLog( );
    private static Map<String, List<IndexationItemLog>> _mapListIndexationItemsLog = new HashMap<>( );
    private static List<IndexationItemLog> _listIndexationItemsLog = new ArrayList<>( );
    private static String _strIndexerName = new String( );
    private static String _strIndex;
    private static int _nWriterMergeFactor;
    private static int _nWriterMaxFieldLength;

    // General Infos logs
    private static final int NUMBER_MAX_ITEMS_BY_BULK = 50000;    
    private static final int NUMBER_OF_ERRORS_PRINT = 5000;
    private static int _numberOfItemsToProcessTotal = 0;
    private static int _numberOfLoop = 1;
    private static boolean isIndexing;
    private static Analyzer _analyzer;
    private static Map<String, SearchIndexer> _mapIndexers = new ConcurrentHashMap<String, SearchIndexer>( );
    private static IndexWriter _writer;
    //private static StringBuffer _sbLogs;
    private static SearchIndexerComparator _comparator = new SearchIndexerComparator( );

    /**
     * The private constructor
     */
    private IndexationService( )
    {
    }

    /**
     * Initalizes the service
     *
     * @throws LuteceInitException
     *             If an error occured
     */
    public static void init( ) throws LuteceInitException
    {
        // Read configuration properties
        boolean indexInWebapp = AppPropertiesService.getPropertyBoolean( PATH_INDEX_IN_WEBAPP, true );

        if ( indexInWebapp )
        {
            _strIndex = AppPathService.getPath( PATH_INDEX );
        }
        else
        {
            _strIndex = AppPropertiesService.getProperty( PATH_INDEX );
        }

        if ( ( _strIndex == null ) || ( _strIndex.equals( "" ) ) )
        {
            throw new LuteceInitException( "Lucene index path not found in lucene.properties", null );
        }

        _nWriterMergeFactor = AppPropertiesService.getPropertyInt( PROPERTY_WRITER_MERGE_FACTOR, DEFAULT_WRITER_MERGE_FACTOR );
        _nWriterMaxFieldLength = AppPropertiesService.getPropertyInt( PROPERTY_WRITER_MAX_FIELD_LENGTH, DEFAULT_WRITER_MAX_FIELD_LENGTH );

        String strAnalyserClassName = AppPropertiesService.getProperty( PROPERTY_ANALYSER_CLASS_NAME );

        if ( ( _strIndex == null ) || ( _strIndex.equals( "" ) ) )
        {
            throw new LuteceInitException( "Analyser class name not found in lucene.properties", null );
        }

        try
        {
            _analyzer = (Analyzer) Class.forName( strAnalyserClassName ).newInstance( );
        }
        catch( Exception e )
        {
            throw new LuteceInitException( "Failed to load Lucene Analyzer class", e );
        }
    }

    /**
     * Register an indexer
     *
     * @param indexer
     *            The indexer to add to the registry
     */
    public static void registerIndexer( SearchIndexer indexer )
    {
        if ( indexer != null )
        {
            _mapIndexers.put( indexer.getName( ), indexer );
            AppLogService.info( "New search indexer registered : " + indexer.getName( ) );
        }
    }

    /**
     * Unregister an indexer. The indexer is only removed if its name has not changed
     * 
     * @param indexer
     *            the indexer to remove from the registry
     */
    public static void unregisterIndexer( SearchIndexer indexer )
    {
        if ( indexer != null )
        {
            if ( _mapIndexers.remove( indexer.getName( ), indexer ) )
            {
                AppLogService.info( "Search indexer unregistered : " + indexer.getName( ) );
            }
            else
            {
                AppLogService.error( "Search indexer " + indexer.getName( ) + " could not be be unregistered" );
            }
        }
    }

    /**
     * Process the indexing
     *
     * @param bCreate
     *            Force creating the index
     * @return the result log of the indexing
     */
    public static synchronized String processIndexing( IndexationMode modeProcessIndex, String strIndexerTreated ) {
        // String buffer for building the response page;
        _writer = null;
        Directory dir = null;
        if ( strIndexerTreated.equals( "All" ) )
        {
            strIndexerTreated = null;
        }
        
        try 
        {
            dir = IndexationService.getDirectoryIndex( );
            if ( !DirectoryReader.indexExists( dir ) )  // verify if init file indexer is correct
            {
                modeProcessIndex = IndexationMode.FULL;
            }
            Date start = new Date();
            IndexWriterConfig conf = new IndexWriterConfig( _analyzer );
            // run the Mode Choosen by users
            switch ( modeProcessIndex ) 
            {

                /*case INCREMENTAL_DIRECTLY:
                    // not implemented yet
                    _writer = new IndexWriter(dir, conf);
                    break;*/
                case FULL:
                    conf.setOpenMode( OpenMode.CREATE );
                    _writer = new IndexWriter( dir, conf );
                    processFullIndexing( strIndexerTreated );
                    break;

                case INCREMENTAL_BY_BULK:
                    conf.setOpenMode( OpenMode.APPEND );
                    _writer = new IndexWriter( dir, conf );
                    processIncrementalIndexing( strIndexerTreated );
                    break;
                default:
            }

            Date end = new Date();
            _generalIndexLog.setTreatmentDurationMs( end.getTime() - start.getTime() );
        } 
        catch ( Exception e ) 
        {
            error( "Indexing ", e, "" );
        } 
        finally 
        {
            try 
            {
                if ( _writer != null ) 
                {
                    _writer.close();
                }
            } 
            catch ( IOException e ) 
            {
                error( "Close Writer ", e, "" );
            }

            try 
            {
                if ( dir != null ) 
                {
                    dir.close();
                }
            } 
            catch ( IOException e ) 
            {
                error( "Close dir",e,"" );
            }
            setIsIndexing( false );
            _generalIndexLog.setIsIndexing( isIndexing );
        }
        return getJsonString( getAllIndexationInformations( ) );
    }

    /**
     * Process all contents
     */
    private static void processFullIndexing( ) 
    {
        processFullIndexing( null );
    } 

    /**
     * Process all contents depending Indexer
     */
    private static void processFullIndexing(String strIndexerTreated) 
    {
        initializationIndexerParam("Full",strIndexerTreated);
        IndexationLogService.debug("Starting Full Indexing");
        AppLogService.info("Starting Full Indexing");
        for (SearchIndexer indexer : getIndexerListSortedByName()) 
        {
            // catch any exception coming from an indexer to prevent global indexation to
            // fail
            
            try 
            {
                if (indexer.isEnable()) 
                {
                    // it allows only specefied indexer to run if not null
                    if (strIndexerTreated != (null)  && !indexer.getName().equals(strIndexerTreated))
                    {
                        continue;
                    }

                    Date start = new Date();
                    _strIndexerName = indexer.getName();
                    _numberOfLoop = getNumberOfLoop(indexer.getNumberOfElementsToProcess());

                    for (int index = 0 ; index < _numberOfLoop; index++)
                    {
                        // the indexer will call write(doc)
                        indexer.indexDocuments();
                    }
                    Date end = new Date();
                    _mapIndexationInformation.get(_strIndexerName).setTreatmentDurationMs(end.getTime() - start.getTime());
                    
                }

            } 
            catch (Exception e) 
            {
                error(indexer.getName() , e , StringUtils.EMPTY );
            }
            IndexationLogService.debug("Full Indexing for Indexer : " + indexer.getName() + " Done");
            AppLogService.info("Full Indexing for Indexer : " + indexer.getName() + " Done");
        }
        removeAllIndexerAction();
        IndexationLogService.debug("End Full Indexing");
        AppLogService.info("End Full Indexing");
        setIsIndexing(false);
    }


    /**
     * Process incremental indexing
     *
     * @throws CorruptIndexException
     *             if an error occurs
     * @throws IOException
     *             if an error occurs
     * @throws InterruptedException
     *             if an error occurs
     * @throws SiteMessageException
     *             if an error occurs
     */
    private static void processIncrementalIndexing() 
            throws CorruptIndexException, IOException, InterruptedException, SiteMessageException 
    {
        processIncrementalIndexing( null );
    }

    /**
     * Process incremental indexing
     *
     * @throws CorruptIndexException
     *             if an error occurs
     * @throws IOException
     *             if an error occurs
     * @throws InterruptedException
     *             if an error occurs
     * @throws SiteMessageException
     *             if an error occurs
     */
    private static void processIncrementalIndexing( String strIndexerTreated ) throws CorruptIndexException, IOException, InterruptedException, SiteMessageException
    {
        initializationIndexerParam("Incremental",strIndexerTreated);
        // incremental indexing
        IndexationLogService.debug("Starting Incremental Indexing");
        AppLogService.info("Starting Incremental Indexing");
        Collection<IndexerAction> actions = IndexerActionHome.getList();
        int iteration = 0;
        for (IndexerAction action : actions) 
        {
            // catch any exception coming from an indexer to prevent global indexation to
            // fail
            
            _strIndexerName = action.getIndexerName();
            // it allows only specefied indexer to run if not null
            if (strIndexerTreated != (null)  && !_strIndexerName.equals(strIndexerTreated))
            {
                continue;
            }
            
            SearchIndexer indexer = _mapIndexers.get(action.getIndexerName());
            try 
            {
                Date start = new Date();
                IndexationInformation indexInfo = _mapIndexationInformation.get( indexer.getName( ) );


                if (action.getIdTask() == IndexerAction.TASK_DELETE) 
                {
                    deleteDocument(action);
                    _indexationItemLog = new IndexationItemLog( "Deleting",null, action.getIdDocument( ) );
                    iteration++;
                    indexInfo.setNumberOfItemsProcessed(iteration); 
                    _mapListIndexationItemsLog.get(indexer.getName()).add(_indexationItemLog);
                } 
                else 
                {
                    List<org.apache.lucene.document.Document> luceneDocuments = indexer
                            .getDocuments(action.getIdDocument());

                    if ((luceneDocuments != null) && (luceneDocuments.size() > 0)) 
                    {
                        for (org.apache.lucene.document.Document doc : luceneDocuments) 
                        {
                            if ((action.getIdPortlet() == ALL_DOCUMENT)
                                    || ((doc.get(SearchItem.FIELD_DOCUMENT_PORTLET_ID) != null)
                                            && (doc.get(SearchItem.FIELD_DOCUMENT_PORTLET_ID).equals(
                                                    doc.get(SearchItem.FIELD_UID) + "&" + action.getIdPortlet())))) 
                            {
                                // Indexing 1 item
                                processDocument(action, doc);
                                iteration++;
                                indexInfo.setNumberOfItemsProcessed(iteration); 
                                indexInfo.setNumberOfItemsFailed(indexer.getNumberOfElementsFailed());
                                // Get items by Items and put it on a mapper                 
                            }
                        }
                    }
                }
                Date end = new Date();
                indexInfo.setTreatmentDurationMs(end.getTime() - start.getTime());
                removeIndexerAction(action.getIdAction());

            } 
            catch (Exception e) 
            {
                error( indexer.getName( ),
                      e,
                      "Action from indexer : " + action.getIndexerName() 
                    + " Action ID : " + action.getIdAction()
                    + " - Document ID : " + action.getIdDocument() 
                    + StringUtils.EMPTY);      
            }
        }
        
        // reindexing all pages.
        _writer.deleteDocuments(new Term(SearchItem.FIELD_TYPE, PARAM_TYPE_PAGE));
        for (SearchIndexer indexer : getIndexerListSortedByName()) 
        {
            _mapIndexers.get(indexer.getName()).indexDocuments();
        }
        IndexationLogService.debug("End Incremental Indexing");
        AppLogService.info("End Incremental Indexing");
        setIsIndexing(false);
    }

    /**
     * Delete a document from the index
     *
     * @param action
     *            The current action
     * @throws CorruptIndexException
     *             if an error occurs
     * @throws IOException
     *             if an error occurs
     */
    private static void deleteDocument( IndexerAction action ) throws CorruptIndexException, IOException
    {
        if ( action.getIdPortlet( ) != ALL_DOCUMENT )
        {
            // delete only the index linked to this portlet
            _writer.deleteDocuments( new Term( SearchItem.FIELD_DOCUMENT_PORTLET_ID, action.getIdDocument( ) + "&" + Integer.toString( action.getIdPortlet( ) ) ) );
        }
        else
        {
            // delete all index linked to uid
            _writer.deleteDocuments( new Term( SearchItem.FIELD_UID, action.getIdDocument( ) ) );
        }

        IndexationLogService.debug("Deleting #"+action.getIdDocument()+"\r\n");
    }

    /**
     * Create or update the index for a given document
     *
     * @param action
     *            The current action
     * @param doc
     *            The document
     * @throws CorruptIndexException
     *             if an error occurs
     * @throws IOException
     *             if an error occurs
     */
    private static void processDocument( IndexerAction action, Document doc ) throws CorruptIndexException, IOException
    {
        if ( action.getIdTask( ) == IndexerAction.TASK_CREATE )
        {
            _writer.addDocument( doc );
            logDoc( "Adding ", doc );
        }
        else
            if ( action.getIdTask( ) == IndexerAction.TASK_MODIFY )
            {
                if ( action.getIdPortlet( ) != ALL_DOCUMENT )
                {
                    // delete only the index linked to this portlet
                    _writer.updateDocument( new Term( SearchItem.FIELD_DOCUMENT_PORTLET_ID, doc.get( SearchItem.FIELD_DOCUMENT_PORTLET_ID ) ), doc );
                }
                else
                {
                    _writer.updateDocument( new Term( SearchItem.FIELD_UID, doc.getField( SearchItem.FIELD_UID ).stringValue( ) ), doc );
                }

                logDoc( "Updating ", doc );
            }
    }

    /**
     * Index one document, called by plugin indexers
     *
     * @param doc
     *            the document to index
     * @throws CorruptIndexException
     *             corruptIndexException
     * @throws IOException
     *             i/o exception
     */
    public static void write( Document doc ) throws CorruptIndexException, IOException
    {
        _writer.addDocument( doc );
        logDoc( "Indexing ", doc );
    }

    /**
     * Log an action made on a document
     * 
     * @param strAction
     *            The action
     * @param doc
     *            The document
     */
    private static void logDoc( String strAction, Document doc )
    {
        // Add Indexation infos on DEBUG
        IndexationLogService.debug( "IndexerName : " + _strIndexerName 
                                  + " Action : " + strAction 
                                  + " Type : " + doc.get(SearchItem.FIELD_TYPE) 
                                  + " Uid : " + doc.get(SearchItem.FIELD_UID)
                                  + " Title :" + doc.get(SearchItem.FIELD_TITLE));
        
    }

    /**
     * Log the error for the search indexer.
     *
     * @param indexer
     *            the {@link SearchIndexer}
     * @param e
     *            the exception
     * @param strMessage
     *            the str message
     */
    public static void error( SearchIndexer indexer, Exception e, String strMessage )
    {
        
        // Put an Indexation Errors item Infos on a map
        _indexationItemLog = new IndexationItemLog("Indexing",e.getMessage( ),strMessage);
        _mapListIndexationItemsLog.get( indexer.getName( ) ).add( _indexationItemLog );
        String strTitle = "Indexer : " + indexer.getName( );
        error( strTitle, e, strMessage );
    }

    /**
     * Log an exception
     * 
     * @param strTitle
     *            The title of the error
     * @param e
     *            The exception to log
     * @param strMessage
     *            The message
     */
    private static void error( String strTitle, Exception e, String strMessage )
    {
        // Add Indexation infos on Buffer for template
        StringBuffer logs = new StringBuffer( );
        logs.append(strTitle
              + " Uid : " + strMessage 
              + " Errors : " + e.getMessage( ) );
        if ( e.getCause( ) != null )
        {
            logs.append( " : " );
            logs.append( e.getCause( ).getMessage( ) );
        }
      
        if ( StringUtils.isNotBlank( strMessage ) )
        {
            logs.append( " - " ).append( strMessage );
        }

        // Add Indexation infos on DEBUG
        IndexationLogService.error( logs, e );
    }

    /**
     * Gets the current index
     *
     * @return The index
     * @deprecated use getDirectoryIndex( ) instead
     */
    @Deprecated
    public static String getIndex( )
    {
        return _strIndex;
    }

    /**
     * Gets the current IndexSearcher.
     *
     * @return IndexSearcher
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static Directory getDirectoryIndex( ) throws IOException
    {
        return NIOFSDirectory.open( Paths.get( _strIndex ) );
    }

    /**
     * Gets the current analyser
     *
     * @return The analyser
     */
    public static Analyzer getAnalyser( )
    {
        return _analyzer;
    }

    /**
     * Returns all search indexers
     *
     * @return A collection of indexers
     */
    public static Collection<SearchIndexer> getIndexers( )
    {
        return _mapIndexers.values( );
    }

    /**
     * return a list of IndexerAction by task key
     *
     * @param nIdTask
     *            the task kety
     * @return a list of IndexerAction
     */
    public static List<IndexerAction> getAllIndexerActionByTask( int nIdTask )
    {
        IndexerActionFilter filter = new IndexerActionFilter( );
        filter.setIdTask( nIdTask );

        return IndexerActionHome.getList( filter );
    }

    /**
     * Remove a Indexer Action
     *
     * @param nIdAction
     *            the key of the action to remove
     *
     */
    public static void removeIndexerAction( int nIdAction )
    {
        IndexerActionHome.remove( nIdAction );
    }

    /**
     * Remove all Indexer Action
     *
     */
    public static void removeAllIndexerAction( )
    {
        IndexerActionHome.removeAll( );
    }

    /**
     * Add Indexer Action to perform on a record
     *
     * @param strIdDocument
     *            the id of the document
     * @param indexerName
     *            the name of the indexer
     * @param nIdTask
     *            the key of the action to do
     * @param nIdPortlet
     *            id of the portlet
     */
    public static void addIndexerAction( String strIdDocument, String indexerName, int nIdTask, int nIdPortlet )
    {
        IndexerAction indexerAction = new IndexerAction( );
        indexerAction.setIdDocument( strIdDocument );
        indexerAction.setIdTask( nIdTask );
        indexerAction.setIndexerName( indexerName );
        indexerAction.setIdPortlet( nIdPortlet );
        IndexerActionHome.create( indexerAction );
    }

    /**
     * Add Indexer Action to perform on a record
     *
     * @param strIdDocument
     *            the id of the document
     * @param indexerName
     *            the name of the indexer
     * @param nIdTask
     *            the key of the action to do
     */
    public static void addIndexerAction( String strIdDocument, String indexerName, int nIdTask )
    {
        addIndexerAction( strIdDocument, indexerName, nIdTask, ALL_DOCUMENT );
    }

    /**
     * Gets a sorted list of registered indexers
     * 
     * @return The list
     */
    private static List<SearchIndexer> getIndexerListSortedByName( )
    {
        List<SearchIndexer> list = new ArrayList<SearchIndexer>( _mapIndexers.values( ) );
        Collections.sort( list, _comparator );

        return list;
    }

    /**
     * Comparator to sort indexer
     */
    private static class SearchIndexerComparator implements Comparator<SearchIndexer>, Serializable
    {
        private static final long serialVersionUID = -3800252801777838562L;

        /**
         * {@inheritDoc}
         */
        @Override
        public int compare( SearchIndexer si1, SearchIndexer si2 )
        {
            return si1.getName( ).compareToIgnoreCase( si2.getName( ) );
        }
    }

    /**
     * Gets the Path where Indexed Items are stored
     * 
     * @return String Path_Index
     */
    public static String getIndexPath() 
    {
        return AppPathService.getPath(PATH_INDEX);
    }

    
    /**
     * Get the Number of Items Max Treated by Bulk
     * @return int number of items by Bulk
     */
    public static int getNumberMaxItemsByBulk()
    {
        return NUMBER_MAX_ITEMS_BY_BULK;
    }

    /**
     * Get a Boolean , false -> there is no Current Indexation
     *                 true -> there is a Current Indexation
     * @return boolean
     */
    public static boolean getIsIndexing()
    {
        return isIndexing;
    }

    /**
     * Set a Boolean , false -> there is no Current Indexation
     *                 true -> there is a Current Indexation
     * @param boolean
     */
    public static void setIsIndexing(boolean isIndexation)
    {
        isIndexing = isIndexation;
    }


    /**
    * Initialize All paramater for Indexer
    * @param modeProcessIndex Mode Of indexation chosen
    */
    private static void initializationIndexerParam(String modeProcessIndex,String indexerName){
        _indexationInformation.resetIndexationInformation();
        _generalIndexLog.resetGeneralIndexLog();
        _mapIndexationInformation.clear();
        _indexationItemLog.resetIndexationItemLog();
        _mapListIndexationItemsLog.clear();;
        _listIndexationItemsLog.clear();
        _strIndexerName = new String();
        _numberOfItemsToProcessTotal = 0;
        _numberOfLoop = 1;
        
        
        for (SearchIndexer indexer : getIndexerListSortedByName()) {
            
            if (indexer.isEnable())
            {
                // it allows only specefied indexer to run 
                // if null it is running all Indexers
                if (indexerName != null)
                {
                    if (!indexer.getName().equals(indexerName))
                    {
                        continue;
                    }
                }
                
                indexer.initIndexer();
                _listIndexationItemsLog = new ArrayList<>();
                IndexationLogService.debug("Mode : "+modeProcessIndex+" chosen");
                AppLogService.info("Mode : "+modeProcessIndex+" chosen");
                _mapListIndexationItemsLog.put(indexer.getName(), _listIndexationItemsLog);
                _indexationInformation = new IndexationInformation(indexer.getName(),
                                                                   indexer.getDescription(),
                                                                   modeProcessIndex,
                                                                   0,
                                                                   0,
                                                                   indexer.getNumberOfElementsToProcess(),
                                                                   0,
                                                                   _listIndexationItemsLog,
                                                                   true);

                if (indexer.getNumberOfElementsToProcess() == -1 || 
                    indexer.getNumberOfElementsProcessed() == -1 || 
                    indexer.getNumberOfElementsFailed() == -1)
                {
                    _indexationInformation.setNumberOfItemsToProcess(0); 
                    _indexationInformation.setUpdatedIndexation(false);
                }

                _numberOfItemsToProcessTotal += _indexationInformation.getNumberOfItemsToProcess();
                _mapIndexationInformation.put(indexer.getName(),_indexationInformation);
                IndexationLogService.debug("Initialization Indexer : " + indexer.getName() + " Done");
                AppLogService.info("Initialization Indexer : " + indexer.getName() + " Done");
            }
        }
    }
    
    /**
     * Get the Number of Loop to do
     * @param _numberOfItemsToProcessTotalByIndexer Total Number Of Items to Process
     * @return int Number Of loop
     */
    public static int getNumberOfLoop(int _numberOfItemsToProcessTotalByIndexer)
    {
        if (_numberOfItemsToProcessTotalByIndexer > NUMBER_MAX_ITEMS_BY_BULK)
        {
            if (_numberOfItemsToProcessTotalByIndexer % NUMBER_MAX_ITEMS_BY_BULK == 0)
            {
                return (_numberOfItemsToProcessTotalByIndexer / NUMBER_MAX_ITEMS_BY_BULK);
            }
            else{
                return (_numberOfItemsToProcessTotalByIndexer / NUMBER_MAX_ITEMS_BY_BULK) + 1 ;
            }
        }
        return 1;
    }

    /**
     * Gets an oject with All Indexing Logs 
     * 
     * @return AllIndexationInformations Of Indexer's Information
     */
    public static AllIndexationInformations getAllIndexationInformations(){
        // Set a New Map for template
        Map<String,IndexationInformation> mapCurrentIndexersInformation = new HashMap<>(_mapIndexationInformation);
        AllIndexationInformations allIndexationInformations = new AllIndexationInformations(_generalIndexLog,mapCurrentIndexersInformation);
        int _numberOfItemsProcessedTotal = 0;
        int _numberOfItemsFailed = 0;
        
        for (SearchIndexer indexer : getIndexerListSortedByName())
        {
            if (indexer.isEnable() && mapCurrentIndexersInformation.get(indexer.getName()) != null )
            {
                if(mapCurrentIndexersInformation.get(indexer.getName()).getUpdatedIndexation())
                {
                    mapCurrentIndexersInformation.get(indexer.getName()).setNumberOfItemsToProcess(indexer.getNumberOfElementsToProcess());
                    mapCurrentIndexersInformation.get(indexer.getName()).setNumberOfItemsProcessed(indexer.getNumberOfElementsProcessed());
                    mapCurrentIndexersInformation.get(indexer.getName()).setNumberOfItemsFailed(indexer.getNumberOfElementsFailed());
                }
                
                mapCurrentIndexersInformation.get(indexer.getName()).setTreatmentDurationMs(mapCurrentIndexersInformation.get(indexer.getName()).getTreatmentDurationMs());
                if( _mapListIndexationItemsLog.get(indexer.getName()) != null)
                {
                    mapCurrentIndexersInformation.get(indexer.getName()).setListIndexationItemsLog(_mapListIndexationItemsLog.get(indexer.getName()));
                }

                _numberOfItemsProcessedTotal += mapCurrentIndexersInformation.get(indexer.getName()).getNumberOfItemsProcessed();
                _numberOfItemsFailed += mapCurrentIndexersInformation.get(indexer.getName()).getNumberOfItemsFailed();
                
                IndexationLogService.debug( "IndexerName : " + indexer.getName() 
                                          + " Number Of Elements To Process : " + indexer.getNumberOfElementsToProcess() 
                                          + " Number Of Elements Processed : " + indexer.getNumberOfElementsProcessed() 
                                          + " Number Of Elements Failed : " + indexer.getNumberOfElementsFailed());
            }
        }

        _generalIndexLog.setNumberOfItemsToProcess(_numberOfItemsToProcessTotal);
        _generalIndexLog.setNumberOfItemsProcessed(_numberOfItemsProcessedTotal);
        _generalIndexLog.setNumberOfItemsFailed(_numberOfItemsFailed);
        
        IndexationLogService.debug( "General Index Logs - Number Of Elements To Process : " + _numberOfItemsToProcessTotal 
                                  + " Number Of Elements Processed : " + _numberOfItemsProcessedTotal 
                                  + " Number Of Elements Failed : " + _numberOfItemsFailed );
        return allIndexationInformations;
    }
    

    /**
     * Get the Logs in Json format
     * @param allIndexationInformations
     * @return String  JsonString of logs
     */
    public static String getJsonString(AllIndexationInformations allIndexationInformations )
    {
        
        ObjectMapper mapper = new ObjectMapper();
        Map<String,IndexationInformation> mapCurrentIndexersInformation = allIndexationInformations.getMapCurrentIndexersInformation();
        StringBuffer sbIndexerLogsRecover = new StringBuffer();
        String jsonString = new String();
        String jsonStringHead = new String();
        sbIndexerLogsRecover.append("[");
        GeneralIndexLog generalIndexLog = allIndexationInformations.getGeneralIndexLog();
        if (generalIndexLog == null){
            jsonStringHead = "{\"General logs\" : \"Not Enable\"}";
        }
        else{
            try {
                jsonStringHead = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(generalIndexLog);
            }
            catch (JsonGenerationException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            sbIndexerLogsRecover.append(jsonStringHead+",");
        }
        if (mapCurrentIndexersInformation != null)
        {

            Collection<SearchIndexer> listIndexers = getIndexers();
            int itemsCount = listIndexers.size();
            int i = 0;
            for (SearchIndexer indexer : listIndexers)
            {
                IndexationInformation indexationInfo = mapCurrentIndexersInformation.get(indexer.getName());
                if (indexationInfo == null){
                    jsonString = "{\"Indexer\" : \"Not Enable\"}";
                }
                else{
                    try {
                        int numberOfElements = indexationInfo.getNumberOfElementFromList();
                        if (numberOfElements>NUMBER_OF_ERRORS_PRINT)
                        {
                            indexationInfo.getListIndexationItemsLog().subList(NUMBER_OF_ERRORS_PRINT, numberOfElements).clear();
                        }
                        jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(indexationInfo);
                    }
                catch (JsonGenerationException e) {
                    e.printStackTrace();
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                }
                
                
                sbIndexerLogsRecover.append(jsonString);
                i++;
                if (i != itemsCount){
                    sbIndexerLogsRecover.append(",");
                }

                
            }
        }
        else{
            
            sbIndexerLogsRecover.append("{null}");
            
        }
        sbIndexerLogsRecover.append("]");
        return sbIndexerLogsRecover.toString();
    }

}

