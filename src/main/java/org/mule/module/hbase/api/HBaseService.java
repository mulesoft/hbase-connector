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
import org.apache.hadoop.hbase.client.ResultScanner;

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
        String columnQualifier, Long timestamp, String value, Boolean writeToWAL) throws HBaseServiceException;
    
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
    
    /**
     * Scan across all rows in a table. 
     * 
     * @param tableName limits the scan to a specific table. This is the only required argument.
     * @param columnFamilyName limits the scan to a specific column family or <code>null</code>
     * @param columnQualifier limits the scan to a specific column or <code>null</code>. 
     *        Requires a columnFamilyName to be defined.
     * @param timestamp limits the scan to a specific timestamp
     * @param maxTimestamp get versions of columns 
     *        only within the specified timestamp range: [timestamp, maxTimestamp)
     * @param caching the number of rows for caching
     * @param batch the maximum number of values to return for each call to next() in the {@link ResultScanner}
     * @param cacheBlocks the number of rows for caching that will be passed to scanners
     * @param maxVersions limits the number of versions on each column
     * @param allVersions get all available versions on each column
     * @param startRow limits the beginning of the scan to the specified row inclusive
     * @param stopRow limits the end of the scan to the specified row exclusive
     */
    public ResultScanner scan(String tableName, String columnFamilyName, String columnQualifier, 
            Long timestamp, Long maxTimestamp, Integer caching, Integer batch, Boolean cacheBlocks, 
            Integer maxVersions, Boolean allVersions, String startRow, String stopRow) 
            throws HBaseServiceException;
    
    /**
     * Atomically increments a column value. 
     * If the column value does not yet exist it is initialized to <code>amount</code> 
     * and written to the specified column.
     * 
     * @param writeToWAL set it to <code>false</code> means that in a fail scenario, 
     *        you will lose any increments that have not been flushed.
     * 
     * @return the new value, post increment
     */
    long increment(String tableName, String row, String columnFamilyName, String columnQualifier, 
            long amount, boolean writeToWAL) throws HBaseServiceException;
    /**
     * Atomically checks if a row/family/qualifier value matches the expected value. 
     * If it does, it adds the put.
     * 
     * @param writeToWAL set it to <code>false</code> means that in a fail scenario, 
     *        you will lose any increments that have not been flushed.
     * 
     * @return true if the new put was executed, false otherwise
     */
    boolean checkAndPut(String tableName, String row, 
            String checkColumnFamilyName, String checkColumnQualifier, String checkValue,
            String putColumnFamilyName, String putColumnQualifier, 
            Long putTimestamp, String putValue, Boolean putWriteToWAL) throws HBaseServiceException;
    
    /**
     * Atomically checks if a row/family/qualifier value matches the expected value. 
     * If it does, it adds the delete.
     * 
     * @return true if the new delete was executed, false otherwise
     */
    boolean checkAndDelete(String tableName, String row, 
            String checkColumnFamilyName, String checkColumnQualifier, String checkValue,
            String deleteColumnFamilyName, String deleteColumnQualifier, 
            Long deleteTimestamp, Boolean deleteAllVersions) throws HBaseServiceException;
    
    //------------ Configuration
    /**
     * Add the properties to the main configuration. 
     * It overrides old properties if they where already added.
     */
    void addProperties(Map<String, String> properties);

}
