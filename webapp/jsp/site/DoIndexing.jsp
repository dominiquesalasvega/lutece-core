<%@ page isErrorPage="true" %>

<%@ page import="fr.paris.lutece.portal.service.search.IndexationService" %>
<%@ page import="fr.paris.lutece.portal.service.search.IndexationMode" %>
<%
	IndexationMode modeIndexation = IndexationMode.INCREMENTAL_BY_BULK;
	IndexationService.processIndexing( modeIndexation , "All" );
%>
