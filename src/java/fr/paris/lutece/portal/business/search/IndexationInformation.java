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

import java.util.List;

/**
 * Class Indexation information the class provide manage method to get
 * Indexers's Information in order to have Logs
 */
    public class IndexationInformation{

        private String          indexerName;
        private String          indexerDescription;
        private String          indexationMode;
        private int             numberOfItemsFailed;
        private String          errorLogs;
        private long            treatmentDurationMs;
        private int             numberOfItemsToProcess;
        private int             numberOfItemsProcessed;
        private List<IndexationLogs>  listIndexerLogs;


        /**
         * Default Constructor 
         */
        public IndexationInformation()
        {
            this.indexerName = null;
            this.indexationMode = null;
            this.indexerDescription = null;
            this.numberOfItemsFailed = -1;
            this.errorLogs = "None";
            this.treatmentDurationMs = 0;
            this.numberOfItemsToProcess = 0;
            this.numberOfItemsProcessed = 0;
            this.listIndexerLogs = null;
        }

        /**
         * Constructor 
         */
        public IndexationInformation(String name,String indexerDescription,String  indexationMode,int numberOfItemsFailed,String errorLogs ,long  treatmentDurationMs , int numberOfItemsToProcess,int numberOfItemsProcessed,List<IndexationLogs> listIndexerLogs)
        {
            this.indexerName = name;
            this.indexationMode = indexationMode;
            this.indexerDescription = indexerDescription;
            this.numberOfItemsFailed = numberOfItemsFailed;
            this.errorLogs = errorLogs;
            this.treatmentDurationMs = treatmentDurationMs;
            this.numberOfItemsToProcess = numberOfItemsToProcess;
            this.numberOfItemsProcessed = numberOfItemsProcessed;
            this.listIndexerLogs = listIndexerLogs;
        }
        
        
        /**
         * getter
         */
        
        public String getIndexerName()
        {
            return indexerName;
        }
        public String getIndexerDescription()
        {
            return indexerDescription;
        }
        public String getIndexationMode()
        {
            return indexationMode;
        }
        public int getNumberOfItemsFailed()
        {
            return numberOfItemsFailed;
        }
        public String getErrorLogs()
        {
            return errorLogs;
        }
        public long getTreatmentDurationMs()
        {
            return treatmentDurationMs;
        }
        public int getNumberOfItemsToProcess()
        {
            return numberOfItemsToProcess;
        }
        public int getNumberOfItemsProcessed()
        {
            return numberOfItemsProcessed;
        }
        public List<IndexationLogs> getListIndexerLogs()
        {
            return listIndexerLogs;
        }

        /**
         * setter
         */
        public void setIndexerName(String indexerName)
        {
            this.indexerName = indexerName;
        }
        public void setIndexerDescription(String indexerDescription)
        {
            this.indexerDescription = indexerDescription;
        }
        public void setIndexationMode(String indexationMode)
        {
            this.indexationMode = indexationMode;
        }
        public void setNumberOfItemsFailed(int numberOfItemsFailed)
        {
            this.numberOfItemsFailed = numberOfItemsFailed;
        }
        public void setErrorLogs(String errorLogs)
        {
            this.errorLogs = errorLogs;
        }
        public void setTreatmentDurationMs(long treatmentDurationMs)
        {
            this.treatmentDurationMs = treatmentDurationMs;
        }
        public void setNumberOfItemsToProcess(int numberOfItemsToProcess)
        {
            this.numberOfItemsToProcess = numberOfItemsToProcess;
        }
        public void setNumberOfItemsProcessed(int numberOfItemsProcessed)
        {
            this.numberOfItemsProcessed = numberOfItemsProcessed;
        }
        public void setListIndexerLogs(List<IndexationLogs> listIndexerLogs)
        {
            this.listIndexerLogs = listIndexerLogs;
        }

        public void addToListIndexerLogs(IndexationLogs indexLogs)
        {
            this.listIndexerLogs.add(indexLogs);
        }
        public void addListIndexerLogs(List<IndexationLogs> listIndexLogs)
        {
            this.listIndexerLogs.addAll(listIndexLogs);
        }


        public void setAllParam(int numberOfItemsFailed, String errorLogs ,long  treatmentDurationMs , int numberOfItemsToProcess,int numberOfItemsProcessed)
        {
            this.numberOfItemsFailed = numberOfItemsFailed;
            this.errorLogs = errorLogs;
            this.treatmentDurationMs = treatmentDurationMs;
            this.numberOfItemsToProcess = numberOfItemsToProcess;
            this.numberOfItemsProcessed = numberOfItemsProcessed;
        }

    }
