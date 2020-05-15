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
 * Class General Indexation information the class provide manage method to get
 * Indexers's Information in order to have Logs
 */
    public class GeneralIndexLog{

        private int             numberOfItemsFailed;
        private long            treatmentDurationMs;
        private int             numberOfItemsToProcess;
        private int             numberOfItemsProcessed;
        private boolean         isIndexing;


        /**
         * Default Constructor 
         */
        public GeneralIndexLog()
        {
            this.numberOfItemsFailed = 0;
            this.treatmentDurationMs = 0;
            this.numberOfItemsProcessed = 0;
            this.numberOfItemsToProcess = 0;
            this.isIndexing = true;
        }

        /**
         * Constructor
         * @param numberOfItemsFailed
         * @param treatmentDurationMs
         * @param numberOfItemsToProcess
         * @param numberOfItemsProcessed
         * @param isIndexing
         */
        public GeneralIndexLog(int numberOfItemsFailed,long  treatmentDurationMs , int  numberOfItemsToProcess, int numberOfItemsProcessed, boolean isIndexing)
        {
            this.numberOfItemsFailed = numberOfItemsFailed;
            this.treatmentDurationMs = treatmentDurationMs;
            this.numberOfItemsToProcess = numberOfItemsToProcess;
            this.numberOfItemsProcessed = numberOfItemsProcessed;
            this.isIndexing = isIndexing;
        }

        
        /**
         * reset All Params from General Indexation information
         */
        public void resetGeneralIndexLog()
        {
            this.numberOfItemsFailed = 0;
            this.treatmentDurationMs = 0;
            this.numberOfItemsProcessed = 0;
            this.numberOfItemsToProcess = 0;
            this.isIndexing = true;
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
         * Get the Treatment Duration in Ms
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
         * Get boolean true -> Indexation is running
         *                  -> Indexation is not running
         * @return boolean
         */
        public boolean getIsIndexing()
        {
            return isIndexing;
        }


        /**
         * set the Number Of Items Failed
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
         * Set the Number Of Items To Process
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
         * Set boolean true -> Indexation is running
         *                  -> Indexation is not running
         * @param isIndexing
         */
        public void setIsIndexing(boolean isIndexing)
        {
            this.isIndexing = isIndexing;
        }


    }
