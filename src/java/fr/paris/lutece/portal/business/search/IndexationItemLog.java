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
    
/**
     * Class Indexation Item Log 
     * the class provide manage method to get Indexers's Information in order to have Log
     */
    public class IndexationItemLog{

        private String  actionItem;
        private String  errorItem;
        private String  uidItem;

        /**
         * Constructor
         * @param actionItem
         * @param errorItem
         * @param uidItem
         */
        public IndexationItemLog(String actionItem,String  errorItem, String uidItem)
        {

            this.actionItem = actionItem;
            this.errorItem = errorItem;
            this.uidItem = uidItem;
        }

        /**
         * Default Constructor
         */
        public IndexationItemLog()
        {

            this.actionItem = null;
            this.errorItem = null;
            this.uidItem = null;
        }

        /**
         * Reset all Param
         */
        public void resetIndexationItemLog()
        {

            this.actionItem = null;
            this.errorItem = null;
            this.uidItem = null;
        }

        /** 
         * Get the Action Item
         * @return String
         */
        public String getActionItem()
        {
            return actionItem;
        }

        /**
         * Get Error Item
         * @return String
         */
        public String  getErrorItem()
        {
            return errorItem;
        }

        /**
         * Get the Uid Item
         * @return
         */
        public String getUidItem()
        {
            return uidItem;
        }



        /**
         * Set Action Item
         * @param actionItem
         */
        public void setActionItem(String actionItem)
        {
            this.actionItem = actionItem;
        }

        /**
         * Set Error Item
         * @param errorItem
         */
        public void setErrorItem(String  errorItem)
        {
            this.errorItem = errorItem;
        }

        /**
         * Set Uid Item
         * @param uidItem
         */
        public void setUidItem(String uidItem)
        {
            this.uidItem = uidItem;
        }


    }