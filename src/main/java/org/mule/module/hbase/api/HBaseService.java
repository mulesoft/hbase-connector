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
    
    
    void addColumn(String tableName, String columnFamilyName, Integer maxVersions, Boolean inMemory, Integer scope);
    boolean existsColumn(String tableName, String columnFamilyName);
    /**
     * Changes a column family in a table, all <code>null</code> parameters will be ignored. 
     *   
     * @param tableName required
     * @param columnFamilyName required
     * @param values (optional) an extension point for arbitrary data
     */
    void modifyColumn(String tableName, String columnFamilyName, Integer maxVersions, Integer blocksize,
            String compressionType, String compactionCompressionType, Boolean inMemory, Integer timeToLive,
            Boolean blockCacheEnabled, String bloomFilterType, Integer replicationScope, 
            Map<String, String> values);
    void deleteColumn(String tableName, String columnFamilyName);
    
    //------------ Row Operations
    Result get(String tableName, String row, Integer maxVersions, Long timestamp) throws HBaseServiceException;
    
    /**
     * Saves the value at the specified cell (row + family:qualifier + timestamp)
     * 
     * @param timestamp (optional) a specific version
     */
    void put(String tableName, String row, String columnFamilyName, 
        String columnQualifier, Long timestamp, String value) throws HBaseServiceException;
    
    /** @return true only if the row exists and is not null */
    boolean exists(String tableName, String row, Integer maxVersions, Long timestamp) throws HBaseServiceException;
    
    /**
     * This method can delete a row in several levels depending on the parameters combination. 
     * 
     * @param columnFamilyName set <code>null</code> to delete all column families in the specified row
     * @param columnQualifier set <code>null</code> to delete all columns in the specified column family
     * @param timestamp set it to delete all versions of the specified column or column family 
     *        with a timestamp less than or equal to the specified timestamp
     * @param deleteAllVersions set <code>false</code> to delete only the latest version of the specified column
     */
    public void delete(String tableName, String row, String columnFamilyName, 
            String columnQualifier, Long timestamp, boolean deleteAllVersions) throws HBaseServiceException;
    
    //------------ Configuration
    /**
     * Add the properties to the main configuration. 
     * It overrides old properties if they where already added.
     */
    void addProperties(Map<String, String> properties);

}
