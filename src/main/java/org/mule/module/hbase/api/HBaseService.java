/**
 * Mule HBase Cloud Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.module.hbase.api;

import java.util.Map;

import org.apache.hadoop.hbase.client.Result;

/**
 * The service exposes several actions to manage and use an HBase database
 * 
 * @author Pablo Martin Grigolatto
 * @since Apr 11, 2011
 */
public interface HBaseService {

    //------------ Admin Operations
    /** @return true only if the server can be reached and the master node is alive, false otherwise. */
    boolean alive() throws HBaseServiceException;
    
    void createTable(String name) throws HBaseServiceException;

    /** @return true only if the table exists, regardless it is enabled or not */
    boolean existsTable(String name) throws HBaseServiceException;
    
    void deleteTable(String name) throws HBaseServiceException;
    
    /** @return true only if the table was disabled. 
     * <em>You should check if the table exists before calling this method.</em> */
    boolean isDisabledTable(String name) throws HBaseServiceException;
    
    void enableTable(String name) throws HBaseServiceException;
    void disabeTable(String name) throws HBaseServiceException;
    
    //------------ Row Operations
    Result get(String tableName, String row) throws HBaseServiceException;
    
    
    //------------ Configuration
    /**
     * Add the properties to the main configuration. 
     * It overrides old properties if they where already added.
     */
    void addProperties(Map<String, String> properties);


}
