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

import java.util.Map;

/**
 * Class All Indexation information the class provide manage method to get
 * Indexers's Information in order to have Logs
 */
    public class AllIndexationInformations{

        private GeneralIndexLog                     generalIndexLog;
        private Map<String,IndexationInformation>   mapCurrentIndexersInformation;


        /**
         * Default Constructor 
         */
        public AllIndexationInformations()
        {
            this.generalIndexLog = null;
            this.mapCurrentIndexersInformation = null;
        }

        /**
         * Construtor
         * @param generalIndexLog
         * @param mapCurrentIndexersInformation
         */
        public AllIndexationInformations(GeneralIndexLog generalIndexLog, Map<String,IndexationInformation>   mapCurrentIndexersInformation)
        {   
            this.generalIndexLog = generalIndexLog;
            this.mapCurrentIndexersInformation = mapCurrentIndexersInformation;
        }
        
        
        
        /**
         * Get General Information of Indexation logs
         * @return GeneralIndexLog
         */
        public GeneralIndexLog getGeneralIndexLog()
        {
            return generalIndexLog;
        }

        /**
         * Get Current map Information of Indexation logs
         * @return Map<String,IndexationInformation>
         */
        public Map<String,IndexationInformation>   getMapCurrentIndexersInformation()
        {
            return mapCurrentIndexersInformation;
        }


        /**
         * Set General Information of Indexation logs
         * @param generalIndexLog
         */
        public void setGeneralIndexLog(GeneralIndexLog generalIndexLog)
        {
            this.generalIndexLog = generalIndexLog;
        }

        /**
         * Set Current map Information of Indexation logs
         * @param mapCurrentIndexersInformation
         */
        public void setMapCurrentIndexersInformation(Map<String,IndexationInformation> mapCurrentIndexersInformation)
        {
            this.mapCurrentIndexersInformation = mapCurrentIndexersInformation;
        }


    }
