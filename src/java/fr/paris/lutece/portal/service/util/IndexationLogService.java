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
package fr.paris.lutece.portal.service.util;

import org.apache.log4j.Logger;

/**
 * This class provides writing services in the application logs files
 */
public final class IndexationLogService
{
    // Constants
    private static final String LOGGER_INDEXER = "lutece.indexer";

    /** alternate log4j property file */
    private static Logger _loggerIndexer = Logger.getLogger( LOGGER_INDEXER );

    /**
     * Creates a new IndexLogService object.
     */
    private IndexationLogService( )
    {
    }

    // //////////////////////////////////////////////////////////////////////////
    // Log4j wrappers

    /**
     * Tells if the logger accepts debug messages. If not it prevents to build consuming messages that will be ignored.
     * 
     * @return True if the logger accepts debug messages, otherwise false.
     */
    public static boolean isDebugEnabled( )
    {
        return _loggerIndexer.isDebugEnabled( );
    }

    /**
     * Log a message object with the DEBUG level. It is logged in application.log
     *
     * @param objToLog
     *            the message object to log
     */
    public static void debug( Object objToLog )
    {
        if ( _loggerIndexer.isDebugEnabled( ) )
        {
            _loggerIndexer.info( objToLog );
        }
    }




    /**
     * Log a message object with the ERROR level including the stack trace of the Throwable t passed as parameter. It is logged in error.log
     *
     * @param message
     *            the message object to log
     * @param t
     *            the exception to log, including its stack trace
     */
    public static void error( Object message, Throwable t )
    {
        if ( _loggerIndexer != null )
        {
            _loggerIndexer.error( message, t );
        }
    }

    /**
     * Log a message object with the ERROR Level. It is logged in error.log
     *
     * @param objToLog
     *            the message object to log
     */
    public static void error( Object objToLog )
    {
        if ( _loggerIndexer != null )
        {
            _loggerIndexer.error( objToLog );
        }
    }
    
   
}
