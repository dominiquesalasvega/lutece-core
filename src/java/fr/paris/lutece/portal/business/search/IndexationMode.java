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

package fr.paris.lutece.portal.business.search;

import fr.paris.lutece.portal.service.util.AppLogService;

/* 
 * Enum the indexation Mode
 * @return the Mode if it does exist or null if it doesn't
 */
public enum IndexationMode {
    INCREMENTAL, FULL;

    /**
     * get the indexers Mode
     * 
     * @param strIndexationMode String
     * @return IndexationMode IndexationMode
     */
    public static IndexationMode getIndexationMode(String strIndexationMode) {
        switch (strIndexationMode) {
        case "full":
            return FULL;
        case "incremental":
            return INCREMENTAL;
        default:
            AppLogService.error("UnknowniIndexation mode : Provided " + strIndexationMode + " is not valid ");
            return null;

        }
    }

    /**
     * Get the string of indexation mode
     * 
     * @param _indexationMode
     * @return String String indexation mode
     */
    public static String getStrIndexingMode( IndexationMode _indexationMode )
    {
        switch( _indexationMode )
        {
            case FULL:
                return "full";
            case INCREMENTAL:
                return "incremental";
            default:
                AppLogService.error( "Unknown indexation mode " );
                return null;

        }
    }


}