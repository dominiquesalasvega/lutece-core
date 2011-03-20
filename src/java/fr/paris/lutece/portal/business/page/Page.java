/*
 * Copyright (c) 2002-2010, Mairie de Paris
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
package fr.paris.lutece.portal.business.page;

import fr.paris.lutece.portal.business.portlet.Portlet;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.rbac.RBACResource;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupResource;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


/**
 * This class reprsents business objects Page
 */
public class Page implements RBACResource, AdminWorkgroupResource
{
    /////////////////////////////////////////////////////////////////////////////////
    // Constants
    public static final String RESOURCE_TYPE = "PAGE";
    public static final String ROLE_NONE = "none";
    private static final String THEME_DEFAULT = "default";
    private static PageRoleRemovalListener _listenerRole;

    // Variables declarations     
    private int _nId;
    private int _nParentPageId;
    private int _nOrder;
    private int _nStatus;
    private int _nPageTemplateId;
    private int _nNodeStatus;
    private String _strMimeType;
    private String _strRole; /* @since v1.1 */
    private String _strName;
    private String _strDescription;
    private String _strTemplate;
    private String _strCodeTheme;
    private byte[] _strImageContent;
    private Timestamp _dateUpdate;
    private List<Portlet> _listPortlets = new ArrayList<Portlet>(  );
    private String _strWorkgroup;
    private String _strMetaKeywords;
    private String _strMetaDescription;

    /**
     * Initialize the Page
     */

    /* FIXME PageRoleRemovalListener should not be registered here
        public static void init(  )
        {
            // Create removal listeners and register them
            if ( _listenerRole == null )
            {
                _listenerRole = new PageRoleRemovalListener(  );
                RoleRemovalListenerService.getService(  ).registerListener( _listenerRole );
            }
        }
    */

    /**
     * Sets the identifier of the page
     *
     * @param nId the page identifier
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the identifier of the page
     *
     * @return page identifier
     */
    public int getId(  )
    {
        return _nId;
    }

    /**
     * Sets the identifier of the parent current page
     *
     * @param nParentPageId the parent page identifier
     */
    public void setParentPageId( int nParentPageId )
    {
        _nParentPageId = nParentPageId;
    }

    /**
     * Returns the identifier of the parent current page
     *
     * @return the parent page identifier
     */
    public int getParentPageId(  )
    {
        return _nParentPageId;
    }

    /**
     * Returns the ImageContent
     *
     * @return The ImageContent
     */
    public byte[] getImageContent(  )
    {
        return _strImageContent;
    }

    /**
     * Sets the ImageContent
     *
     * @param strImageContent The ImageContent
     */
    public void setImageContent( byte[] strImageContent )
    {
        _strImageContent = strImageContent;
    }

    /**
     * Returns the MimeType
     *
     * @return The MimeType
     */
    public String getMimeType(  )
    {
        return _strMimeType;
    }

    /**
     * Sets the MimeType
     *
     * @param strMimeType The MimeType
     */
    public void setMimeType( String strMimeType )
    {
        _strMimeType = strMimeType;
    }

    /**
     * Sets the name of the page
     *
     * @param strName The page name
     */
    public void setName( String strName )
    {
        _strName = strName;
    }

    /**
     * Returns the name of the page
     *
     * @return the page name
     */
    public String getName(  )
    {
        return _strName;
    }

    /**
     * Sets the identifier of the template for the page-setting
     *
     * @param nPageTemplateId the template identifier
     */
    public void setPageTemplateId( int nPageTemplateId )
    {
        _nPageTemplateId = nPageTemplateId;
    }

    /**
     * Returns the identifier of the template for the page-setting
     *
     * @return the template identifier
     */
    public int getPageTemplateId(  )
    {
        return _nPageTemplateId;
    }

    /**
     * Sets the name of the template file for page-setting
     *
     * @param strTemplate the template filename
     */
    public void setTemplate( String strTemplate )
    {
        _strTemplate = strTemplate;
    }

    /**
     * Returns the name of the template file for page-setting
     *
     * @return the template filename
     */
    public String getTemplate(  )
    {
        return _strTemplate;
    }

    /**
     * Sets the position of the current page into a portlet child pages
     *
     * @param nOrder the current page position into a portlet child pages
     */
    public void setOrder( int nOrder )
    {
        _nOrder = nOrder;
    }

    /**
     * Returns the position of the page into a portlet child pages
     *
     * @return the current page position
     */
    public int getOrder(  )
    {
        return _nOrder;
    }

    /**
     * Sets the status of the current page (active or not active)
     *
     * @param nStatus the page status
     */
    public void setStatus( int nStatus )
    {
        _nStatus = nStatus;
    }

    /**
     * Returns the status of the current page
     *
     * @return the current page status
     */
    public int getStatus(  )
    {
        return _nStatus;
    }

    /**
     * Sets the description of the page
     *
     * @param strDescription the page description
     */
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }

    /**
     * Returns the description of the page
     *
     * @return the description page
     */
    public String getDescription(  )
    {
        return _strDescription;
    }

    /**
     * Sets the node_status of the page
     *
     * @param nNodeStatus the node status
     */
    public void setNodeStatus( int nNodeStatus )
    {
        _nNodeStatus = nNodeStatus;
    }

    /**
     * Returns the NodeStatus of the page
     *
     * @return the NodeStatus page
     */
    public int getNodeStatus(  )
    {
        return _nNodeStatus;
    }

    /**
     * Returns the portlets list contained into the page
     *
     * @return the portlets list
     */
    public List<Portlet> getPortlets(  )
    {
        return _listPortlets;
    }

    /**
     * Sets the date to which the portlets list has been modified
     *
     * @param listPortlets the portlet list
     */
    public void setPortlets( List<Portlet> listPortlets )
    {
        _listPortlets = listPortlets;
    }

    /**
     * Sets the date to which the content page has been modified
     *
     * @param dateUpdate the date of modification
     */
    public void setDateUpdate( Timestamp dateUpdate )
    {
        _dateUpdate = dateUpdate;
    }

    /**
     * Returns the date to which the content page has been modified
     *
     * @return the date of modification
     */
    public Timestamp getDateUpdate(  )
    {
        return _dateUpdate;
    }

    /**
     * Gets the page role
     * @return page's role as a String
     * @since v1.1
     */
    public String getRole(  )
    {
        return _strRole;
    }

    /**
     * Sets the page's role
     * @param strRole The role
     * @since v1.1
     */
    public void setRole( String strRole )
    {
        _strRole = ( ( strRole == null ) || ( strRole.equals( "" ) ) ) ? ROLE_NONE : strRole;
    }

    /**
     * Returns the theme of the page
     *
     * @return The theme of the page as a string.
     */
    public String getCodeTheme(  )
    {
        return _strCodeTheme;
    }

    /**
     * Sets the Theme of the page to the specified string.
     *
     * @param strCodeTheme The new Theme of the page.
     */
    public void setCodeTheme( String strCodeTheme )
    {
        _strCodeTheme = ( ( strCodeTheme == null ) || ( strCodeTheme.equals( "" ) ) ) ? THEME_DEFAULT : strCodeTheme;
    }

    /**
     * Checks if the page is visible for the current user
     * @param request The HTTP request
     * @return true if the page could be shown to the user
     * @since v1.3.1
     */
    public boolean isVisible( HttpServletRequest request )
    {
        if ( SecurityService.isAuthenticationEnable(  ) )
        {
            if ( !getRole(  ).equals( ROLE_NONE ) )
            {
                return SecurityService.getInstance(  ).isUserInRole( request, getRole(  ) );
            }
        }

        return true;
    }

    /**
     * Return the page id of the node authorization page
     *
     * @param nPageId the page identifier
     * @return strPageId the page identifier with node_status to 0
     */
    public static int getAuthorizationNode( int nPageId )
    {
        Page page = PageHome.findByPrimaryKey( nPageId );

        if ( ( nPageId != PortalService.getRootPageId(  ) ) && ( page.getNodeStatus(  ) != 0 ) )
        {
            return getAuthorizationNode( page.getParentPageId(  ) );
        }

        return nPageId;
    }

    ////////////////////////////////////////////////////////////////////////////
    // RBAC Resource implementation

    /**
     * Returns the Resource Type Code that identify the resource type
     * @return The Resource Type Code
     */
    public String getResourceTypeCode(  )
    {
        return RESOURCE_TYPE;
    }

    /**
     * Returns the resource Id of the current object
     * @return The resource Id of the current object
     */
    public String getResourceId(  )
    {
        return "" + getId(  );
    }

    /**
    *
    * @return the work group associate to the page
    */
    public String getWorkgroup(  )
    {
        return _strWorkgroup;
    }

    /**
     * set  the work group associate to the page
     * @param workGroup  the work group associate to the page
     */
    public void setWorkgroup( String workGroup )
    {
        _strWorkgroup = workGroup;
    }

    /**
    *
    * @return the META Name associate to the page
    */
    public String getMetaKeywords(  )
    {
        return _strMetaKeywords;
    }

    /**
     * set the META name
     * @param strMetaKeywords the META name
     */
    public void setMetaKeywords( String strMetaKeywords )
    {
        _strMetaKeywords = strMetaKeywords;
    }

    /**
    *
    * @return the META description associate to the page
    */
    public String getMetaDescription(  )
    {
        return _strMetaDescription;
    }

    /**
     * set the META description
     * @param strMetaDescription  the META description
     */
    public void setMetaDescription( String strMetaDescription )
    {
        _strMetaDescription = strMetaDescription;
    }
}
