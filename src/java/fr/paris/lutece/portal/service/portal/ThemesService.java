/*
 * Copyright (c) 2002-2009, Mairie de Paris
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
package fr.paris.lutece.portal.service.portal;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.paris.lutece.portal.business.style.ThemeHome;

/**
 * ThemesService
 */
public final class ThemesService
{
	public static final String DEFAULT_THEME = "default";
	private static final String COOKIE_NAME = "theme";
	private static String _strGlobalTheme = DEFAULT_THEME;

	/**
	 * Private constructor
	 */
	private ThemesService( )
	{
	}

	/**
	 * Gets the theme selected by the user
	 * 
	 * @param request The HTTP request
	 * @return The theme if available otherwise null
	 */
	public static String getUserTheme( HttpServletRequest request )
	{
		if( request != null )
		{
			Cookie[] cookies = request.getCookies( );

			if( cookies != null )
			{
				for( int i = 0; i < cookies.length; i++ )
				{
					Cookie cookie = cookies[i];

					if( cookie.getName( ).equalsIgnoreCase( COOKIE_NAME ) )
					{
						String strTheme = cookie.getValue( );

						if( ThemeHome.isValidTheme( strTheme ) )
						{
							return strTheme;
						}
					}
				}
			}
		}

		return null;
	}

	public static void setUserTheme( HttpServletRequest request, HttpServletResponse response, String strTheme )
	{
		Cookie cookie = new Cookie( COOKIE_NAME, strTheme );
		response.addCookie( cookie );
	}

	/**
	 * Returns the global theme
	 * 
	 * @return the global theme
	 */
	public static String getGlobalTheme( )
	{
		return _strGlobalTheme;
	}

	/**
	 * Sets the global theme
	 * 
	 * @param strTheme The global theme
	 */
	public static void setGlobalTheme( String strTheme )
	{
		_strGlobalTheme = strTheme;
	}
}
