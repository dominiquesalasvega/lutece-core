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

        private String   indexerName;
        private String   indexerDescription;
        private String   indexationMode;
        private int      numberOfItemsFailed;
        private long     treatmentDurationMs;
        private int      numberOfItemsToProcess;
        private int      numberOfItemsProcessed;
        private List<IndexationItemLog>  listIndexationItemsLog;



        /**
         * Default Constructor 
         */
        public IndexationInformation()
        {
            this.indexerName = null;
            this.indexationMode = null;
            this.indexerDescription = null;
            this.numberOfItemsFailed = 0;
            this.treatmentDurationMs = 0;
            this.numberOfItemsToProcess = 0;
            this.numberOfItemsProcessed = 0;
            this.listIndexationItemsLog = null;
        }

        
        /**
         * reset All Params from Indexation information
         */
        public void resetIndexationInformation()
        {
            this.indexerName = null;
            this.indexationMode = null;
            this.indexerDescription = null;
            this.numberOfItemsFailed = 0;
            this.treatmentDurationMs = 0;
            this.numberOfItemsToProcess = 0;
            this.numberOfItemsProcessed = 0;
            this.listIndexationItemsLog = null;
        }

        /**
         * Constructor
         * @param name
         * @param indexerDescription
         * @param indexationMode
         * @param numberOfItemsFailed
         * @param treatmentDurationMs
         * @param numberOfItemsToProcess
         * @param numberOfItemsProcessed
         * @param listIndexationItemsLog
         * @param updatedIndexation
         */
        public IndexationInformation(String name,String indexerDescription,String  indexationMode,int numberOfItemsFailed,long  treatmentDurationMs , int numberOfItemsToProcess,int numberOfItemsProcessed,List<IndexationItemLog> listIndexationItemsLog)
        {
            this.indexerName = name;
            this.indexationMode = indexationMode;
            this.indexerDescription = indexerDescription;
            this.numberOfItemsFailed = numberOfItemsFailed;
            this.treatmentDurationMs = treatmentDurationMs;
            this.numberOfItemsToProcess = numberOfItemsToProcess;
            this.numberOfItemsProcessed = numberOfItemsProcessed;
            this.listIndexationItemsLog = listIndexationItemsLog;
        }
        
        
        /**
         * Get the Indexer Name
         * @return String
         */
        public String getIndexerName()
        {
            return indexerName;
        }

        /**
         * Get the Indexer Description
         * @return String
         */
        public String getIndexerDescription()
        {
            return indexerDescription;
        }

        /**
         * Get the Indexation Mode
         * @return String
         */
        public String getIndexationMode()
        {
            return indexationMode;
        }

        /**
         * Get the Number Of Items Failed
         * @return int
         */
        public int getNumberOfItemsFailed()
        {
            return numberOfItemsFailed;
        }

        /**
         * Get the Treatment Duration in MS
         * @return long
         */
        public long getTreatmentDurationMs()
        {
            return treatmentDurationMs;
        }

        /**
         * Get the Number Of Items To Process
         * @return int
         */
        public int getNumberOfItemsToProcess()
        {
            return numberOfItemsToProcess;
        }

        /**
         * Get the Number Of Items Processed
         * @return int
         */
        public int getNumberOfItemsProcessed()
        {
            return numberOfItemsProcessed;
        }

        /**
         * Get the List Of Indexation Items Logs
         * @return List<IndexationItemLog> 
         */
        public List<IndexationItemLog> getListIndexationItemsLog()
        {
            return listIndexationItemsLog;
        }

        /**
         * Get the Number Of Elements from a List
         * @return int
         */
        public int getNumberOfElementFromList()
        {
            return listIndexationItemsLog.size();
        }



        /**
         * Set the Indexer Name
         * @param indexerName
         */
        public void setIndexerName(String indexerName)
        {
            this.indexerName = indexerName;
        }

        /**
         * Set the Indexer Description
         * @param indexerDescription
         */
        public void setIndexerDescription(String indexerDescription)
        {
            this.indexerDescription = indexerDescription;
        }

        /**
         * Set the Indexation Mode
         * @param indexationMode
         */
        public void setIndexationMode(String indexationMode)
        {
            this.indexationMode = indexationMode;
        }

        /**
         * Set the Number Of Items Failed
         * @param numberOfItemsFailed
         */
        public void setNumberOfItemsFailed(int numberOfItemsFailed)
        {
            this.numberOfItemsFailed = numberOfItemsFailed;
        }

        /**
         * Set the Treatment Duration in Ms
         * @param treatmentDurationMs
         */
        public void setTreatmentDurationMs(long treatmentDurationMs)
        {
            this.treatmentDurationMs = treatmentDurationMs;
        }

        /**
         * Set Number Of Items to Process
         * @param numberOfItemsToProcess
         */
        public void setNumberOfItemsToProcess(int numberOfItemsToProcess)
        {
            this.numberOfItemsToProcess = numberOfItemsToProcess;
        }

        /**
         * Set the Number Of Items Processed 
         * @param numberOfItemsProcessed
         */
        public void setNumberOfItemsProcessed(int numberOfItemsProcessed)
        {
            this.numberOfItemsProcessed = numberOfItemsProcessed;
        }

        /**
         * Set the List of Indexation Items Logs
         * @param listIndexationItemsLog
         */
        public void setListIndexationItemsLog(List<IndexationItemLog> listIndexationItemsLog)
        {
            this.listIndexationItemsLog = listIndexationItemsLog;
        }
        
        /**
         * Add to the List of Indexation Items Logs
         * @param indexationItemLog
         */
        public void addToListIndexationItemsLog(IndexationItemLog indexationItemLog)
        {
            this.listIndexationItemsLog.add(indexationItemLog);
        }

        /**
         * Add a List of Indexation Items Logs
         * @param listIndexationItemsLog
         */
        public void addListIndexationItemsLog(List<IndexationItemLog> listIndexationItemsLog)
        {
            this.listIndexationItemsLog.addAll(listIndexationItemsLog);
        }

        /**
         * Set Treatment Parameter of Indexation Information
         * @param numberOfItemsFailed
         * @param treatmentDurationMs
         * @param numberOfItemsToProcess
         * @param numberOfItemsProcessed
         */
        public void setAllParam(int numberOfItemsFailed,long  treatmentDurationMs , int numberOfItemsToProcess,int numberOfItemsProcessed)
        {
            this.numberOfItemsFailed = numberOfItemsFailed;
            this.treatmentDurationMs = treatmentDurationMs;
            this.numberOfItemsToProcess = numberOfItemsToProcess;
            this.numberOfItemsProcessed = numberOfItemsProcessed;
        }

    }
