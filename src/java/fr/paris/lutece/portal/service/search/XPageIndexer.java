/*
 * Copyright (c) 2002-2017, Mairie de Paris
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

import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.page.IPageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Indexer service for projet
 */
public class XPageIndexer implements SearchIndexer {
    public static final String INDEX_TYPE_PROJET = "xpage";
    public static final String INDEXER_NAME = "xPageIndexer";
    protected static final String PROPERTY_PROJET_BASE_URL = "search.xPageIndexer.baseUrl";
    protected static final String PROPERTY_SEARCH_PROJET_URL = "search.xPageSearch.baseUrl";
    protected static final String PROPERTY_INDEXER_ENABLE = "search.xPageIndexer.enable";
    protected static final String PARAMETER_PROJET_ID = "xPage_id";
    protected static IPageService _pageService = (IPageService) SpringContextService.getBean("pageService");
    private static final String INDEXER_DESCRIPTION = "XPage Indexer Service";
    private static final String INDEXER_VERSION = "2.8.1";
    private static String INDEXER_PATH_INDEX;
    private static final int NUMBER_OF_DOC_GENERATED = 12000; 
    private static final int SIZE_OF_TITLE = 2;
    private static final int SIZE_OF_DESCRIPTION = 5;
    
    
    private int numberOfItemProcessed = -1;
    private int numberOfItemFailed = -1;
    private boolean _initialization = false; 
    private int _lastIdIndexed = -1;
    List<Projet> _listProjet = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void indexDocuments() throws IOException, InterruptedException, SiteMessageException {
        String strProjetBaseUrl = AppPropertiesService.getProperty(PROPERTY_PROJET_BASE_URL);
        
        
        initIndexer();
        int mathnum = Math.min(getNumberOfElementsToProcess()-_lastIdIndexed,IndexationService.getNumberMaxItemsByBulk());

        for (int index = 0 ; index < mathnum; index++) {
            
            Projet projet = _listProjet.get(_lastIdIndexed);
            _lastIdIndexed++;
            UrlItem url = new UrlItem(strProjetBaseUrl);
            url.addParameter(PARAMETER_PROJET_ID, projet.getId());
            Document doc = null;
            try {
                doc = getDocument(projet, url.getUrl());
                
            } catch (Exception e) {
                numberOfItemFailed++;
                IndexationService.error(this.getName(), e, String.valueOf(projet.getId()));
            }
            
            if (doc != null) {
                //Thread.sleep(100);
                IndexationService.write(doc);
                numberOfItemProcessed++;
            }
            
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Document> getDocuments(String nIdDocument)
            throws IOException, InterruptedException, SiteMessageException {

        ArrayList<Document> listDocuments = new ArrayList<Document>();

        
        numberOfItemProcessed = 0;
        numberOfItemFailed = 0;
        String strProjetBaseUrl = AppPropertiesService.getProperty(PROPERTY_PROJET_BASE_URL);

        List<Projet> _listProjet = getAllProjet();
        Projet projet = getProjet(Integer.parseInt(nIdDocument), _listProjet);

        if ((projet != null) && (projet.getId() != 0)) {
            UrlItem url = new UrlItem(strProjetBaseUrl);
            url.addParameter(PARAMETER_PROJET_ID, projet.getId());
            Document doc = null;
            
            try {
                doc = getDocument(projet, url.getUrl());
                
            } catch (Exception e) {
                numberOfItemFailed++;
                IndexationService.error(this.getName(), e, String.valueOf(projet.getId()));
            }
            if (doc != null)
            {
                listDocuments.add(doc);
            }
        }

        return listDocuments;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return INDEXER_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion() {
        return INDEXER_VERSION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return INDEXER_DESCRIPTION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPathIndex() {
        INDEXER_PATH_INDEX = IndexationService.getIndexPath();
        return INDEXER_PATH_INDEX;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEnable() {
        String strEnable = AppPropertiesService.getProperty(PROPERTY_INDEXER_ENABLE, Boolean.TRUE.toString());

        return (strEnable.equalsIgnoreCase(Boolean.TRUE.toString()));
    }

    /**
     * Builds a document which will be used by Lucene during the indexing of the
     * projet of the site with the following fields : summary, uid, url, contents,
     * title and description.
     * 
     * @return the built Document
     * @param strUrl The base URL for documents
     * @param page   the page to index
     * @throws IOException          The IO Exception
     * @throws InterruptedException The InterruptedException
     * @throws SiteMessageException occurs when a site message need to be displayed
     */
    protected Document getDocument(Projet projet, String strUrl)
            throws IOException, InterruptedException, SiteMessageException {
        FieldType ft = new FieldType(StringField.TYPE_STORED);
        ft.setOmitNorms(false);

        FieldType ftNotStored = new FieldType(StringField.TYPE_NOT_STORED);
        ftNotStored.setOmitNorms(false);
        ftNotStored.setTokenized(false);

        // make a new, empty document
        Document doc = new Document();

        // Add the url as a field named "url". Use an UnIndexed field, so
        // that the url is just stored with the document, but is not searchable.
        doc.add(new Field(SearchItem.FIELD_URL, strUrl, ft));

        String strIdPage = String.valueOf(projet.getId());
        doc.add(new Field(SearchItem.FIELD_UID, strIdPage, ft));
        doc.add(new Field(SearchItem.FIELD_TITLE, projet.getStrTitle(), ft));

        doc.add( new Field( SearchItem.FIELD_DATE, "2014-06-06", ft ) );
        String randomContent = generateRandomStringContent(1);
        doc.add( new Field( SearchItem.FIELD_CONTENTS, randomContent, TextField.TYPE_NOT_STORED ) );
        doc.add( new StringField( SearchItem.FIELD_METADATA, "lutecetag", Field.Store.NO ) );

        doc.add( new Field( SearchItem.FIELD_TYPE, "lutecetype", ft ) );
        doc.add( new Field( SearchItem.FIELD_ROLE, "role1", ft ) );

        // return the document
        return doc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getListType() {
        List<String> listType = new ArrayList<String>();
        listType.add(INDEX_TYPE_PROJET);

        return listType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSpecificSearchAppUrl() {
        return AppPropertiesService.getProperty(PROPERTY_SEARCH_PROJET_URL);
    }

    private class Projet {
        private int nId;
        private String strTitle;
        private String strDescription;

        public int getId() {
            return nId;
        }

        public String getStrTitle() {
            return strTitle;
        }

        public String getStrDescription() {
            return strDescription;
        }

        public Projet(int nId) {
            this.nId = nId;
            this.strTitle = generateRandomString(SIZE_OF_TITLE);
            this.strDescription = generateRandomString(SIZE_OF_DESCRIPTION);
        }

        public String generateRandomString(int length) {
            char[] values = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                    's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

            String out = "";
            Random random = new Random();
            for (int i = 0; i < length; i++) {
                int idx = random.nextInt(values.length);
                out += values[idx];
            }
            return out;
        }
    }

    public List<Projet> getAllProjet() {
        List<Projet> _listProjet = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_DOC_GENERATED; i++) {
            Projet projet = new Projet(i);
            _listProjet.add(projet);
        }
        return _listProjet;
    }

    public Projet getProjet(int nId, List<Projet> _listProjet) {
        for (Projet projet : _listProjet) {
            if (projet.getId() == nId) {
                return projet;
            }
        }
        return null;
    }

    public String generateRandomStringContent(int length) {
        char[] values = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

        String out = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int idx = random.nextInt(values.length);
            out += values[idx];
        }
        return out;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfElementsToProcess( )
    {
        return NUMBER_OF_DOC_GENERATED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfElementsProcessed( )
    {
        return numberOfItemProcessed;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfElementsFailed()
    {
        return numberOfItemFailed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInitializationIndexer()
    {
        _initialization = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initIndexer()
    {
        if (_initialization == false )
        {
            _listProjet = getAllProjet();
            _lastIdIndexed = 0;
            numberOfItemProcessed = 0;
            numberOfItemFailed = 0;
            _initialization = true;
        }
    }

}
