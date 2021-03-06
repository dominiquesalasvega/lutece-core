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
package fr.paris.lutece.portal.web.search;

import fr.paris.lutece.portal.business.search.IndexationMode;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.search.IndexationService;
import fr.paris.lutece.portal.business.search.AllIndexationInformations;
import fr.paris.lutece.portal.service.search.SearchIndexer;
import fr.paris.lutece.portal.service.security.SecurityTokenService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.web.admin.AdminFeaturesPageJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the user interface to manage the launching of the indexing of the site pages
 */
public class SearchIndexationJspBean extends AdminFeaturesPageJspBean
{
    // //////////////////////////////////////////////////////////////////////////
    // Constantes
    /**
     * Right to manage indexation
     */
    public static final String RIGHT_INDEXER = "CORE_SEARCH_INDEXATION";
    private static final long serialVersionUID = 2585709013740037568L;
    private static final String TEMPLATE_MANAGE_INDEXER = "admin/search/manage_search_indexation.html";
    private static final String TEMPLATE_INDEXER_LOGS = "admin/search/search_indexation_logs.html";
    private static final String MARK_LOGS = "logs";
    private static final String MARK_INDEXERS_LIST = "indexers_list";
    private static final String INDEXATION_MODE = "indexation_mode";
    private static final String INDEXER_NAME = "indexer_name";
    private static final String INDEXATION_MESSAGE_ISINDEXING = "portal.search.search_indexation.currentIndexation";
    private static final String INDEXATION_MESSAGE_MODE = "portal.search.search_indexation.indexationMode";

    /**
     * Displays the indexing parameters
     *
     * @param request
     *            the http request
     * @return the html code which displays the parameters page
     */
    public String getIndexingProperties( HttpServletRequest request )
    {
        HashMap<String, Object> model = new HashMap<String, Object>( );
        Collection<SearchIndexer> listIndexers = IndexationService.getIndexers( );
        model.put( MARK_INDEXERS_LIST, listIndexers );
        model.put( SecurityTokenService.MARK_TOKEN, SecurityTokenService.getInstance( ).getToken( request, TEMPLATE_MANAGE_INDEXER ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_INDEXER, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Calls the indexing process
     *
     * @param request
     *            the http request
     * @return the result of the indexing process
     * @throws AccessDeniedException
     *             if the security token is invalid
     */
    public String doIndexing( HttpServletRequest request ) throws AccessDeniedException
    {
        /*if ( !SecurityTokenService.getInstance( ).validate( request, TEMPLATE_MANAGE_INDEXER ) )
        {
            throw new AccessDeniedException( "Invalid security token" );
        }*/
        String strLogs;
        HashMap<String, Object> model = new HashMap<String, Object>();
        if (IndexationService.getIsIndexing() == false)
        {
            IndexationService.setIsIndexing(true);
            String strModeIndex = request.getParameter(INDEXATION_MODE);
            String strIndexerTreated = request.getParameter(INDEXER_NAME);
            IndexationMode modeIndexation = IndexationMode.getIndexationMode(strModeIndex);
            if (modeIndexation != null) {
                strLogs = IndexationService.processIndexing(modeIndexation,strIndexerTreated);
            } else {
                strLogs = I18nService.getLocalizedString( INDEXATION_MESSAGE_MODE, request.getLocale() );
            }
        }
        else
        {
            strLogs = I18nService.getLocalizedString( INDEXATION_MESSAGE_ISINDEXING, request.getLocale() );
        }
        model.put(MARK_LOGS, strLogs);

        HtmlTemplate template = AppTemplateService.getTemplate(TEMPLATE_INDEXER_LOGS, null, model);

        return getAdminPage(template.getHtml());
    }

    /**
     * Get Indexation logs
     * @param request
     * @return String
     */
    public String getIndexingLogs(HttpServletRequest request) {

        AllIndexationInformations allIndexationInformations = IndexationService.getAllIndexationInformations();
        return IndexationService.getJsonString(allIndexationInformations);

    }


    /**
     * STOP Indexation
     * @param request
     * @return String
     */
    public String stopIndexation(HttpServletRequest request) {

        IndexationService.setStop(true);
        return "Indexation Stopped";
    }
}
