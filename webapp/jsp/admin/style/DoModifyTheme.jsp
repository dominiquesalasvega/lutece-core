<%@ page errorPage="../ErrorPage.jsp" %>

<jsp:useBean id="theme" scope="session" class="fr.paris.lutece.portal.web.style.ThemesJspBean" />

<%
	theme.init( request , fr.paris.lutece.portal.web.style.ThemesJspBean.RIGHT_MANAGE_THEMES );
    response.sendRedirect( theme.doModifyTheme( request ));
%>

