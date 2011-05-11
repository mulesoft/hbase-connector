/**
 * Mule HBase Cloud Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.module.hbase;

import org.mule.api.lifecycle.Initialisable;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.module.hbase.api.CompressionType;
import org.mule.module.hbase.api.HBaseService;
import org.mule.module.hbase.api.impl.RPCHBaseService;
import org.mule.tools.cloudconnect.annotations.Connector;
import org.mule.tools.cloudconnect.annotations.Operation;
import org.mule.tools.cloudconnect.annotations.Parameter;
import org.mule.tools.cloudconnect.annotations.Property;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.RowLock;

/**
 * <p>
 * HBase connector
 * </p>
 * <p>
 * It delegates each operation on a {@link HBaseService} and it accepts custom
 * configuration in a Key-Value fashion
 * </p>
 * 
 * @author Pablo Martin Grigolatto
 * @since Apr 18, 2011
 */
@Connector(namespacePrefix = "hbase")
public class HbaseCloudConnector implements Initialisable
{
    @Property(name = "facade-ref", optional = true)
    private HBaseService facade;

    /**
     * HBase internal configuration properties. Consult HBase documentation.
     */
    @Property(name = "properties-ref", optional = true)
    private Map<String, String> properties;

    public HbaseCloudConnector()
    {
        properties = Collections.emptyMap();
    }

    // ------------ Admin Operations

    /**
     * Answers if the HBase server is reachable
     * 
     * @return true if the server can be reached and the master node is alive, false
     *         otherwise.
     */
    @Operation
    public boolean isAliveServer()
    {
        return facade.alive();
    }

    /**
     * Creates a new table given its name. The descriptor must be unique and not
     * reserved.
     * 
     * @param name the descriptor for the new table.
     */
    @Operation
    public void createTable(@Parameter(optional = false) final String name)
    {
        facade.createTable(name);
    }

    /**
     * Answers if a given table exists, regardless it is enabled or not
     * 
     * @param name the table name
     * @return true only if the table exists, false otherwise
     */
    @Operation
    public boolean existsTable(@Parameter(optional = false) final String name)
    {
        return facade.existsTable(name);
    }

    /**
     * Disables and deletes an existent table TODO if exists?
     * 
     * @param name name of table to delete
     */
    @Operation
    public void deleteTable(@Parameter(optional = false) final String name)
    {
        facade.deleteTable(name);
    }

    /**
     * Answers if the given table is enable You should check if the table exists
     * before calling this method. TODO why? Throws an exception?
     * 
     * @param name
     * @return true only if the table was disabled. False otherwise
     */
    @Operation
    public boolean isEnabledTable(@Parameter(optional = false) final String name)
    {
        return facade.isDisabledTable(name);
    }

    /**
     * Enables a table given its name
     * 
     * @param name name of the table
     */
    @Operation
    public void enableTable(@Parameter(optional = false) final String name)
    {
        facade.enableTable(name);
    }

    /**
     * Disables a table given its name
     * 
     * @param name the table name
     */
    @Operation
    public void disableTable(@Parameter(optional = false) final String name)
    {
        facade.disabeTable(name);
    }

    /**
     * Adds a column family to a table given a table and column name. This operation
     * gracefully handles necessary table disabling and enabled.
     * 
     * @param tableName the name of the target table
     * @param columnFamilyName the name of the column
     * @param maxVersions the optional maximum number of versions the column family
     *            supports
     * @param inMemory if all the column values will be stored in the region's cache
     * @param scope
     */
    @Operation
    public void addColumn(@Parameter(optional = false) final String tableName,
                          @Parameter(optional = false) final String columnFamilyName,
                          @Parameter(optional = true) final Integer maxVersions,
                          @Parameter(optional = true, defaultValue = "false") final Boolean inMemory,
                          @Parameter(optional = true) final Integer scope)
    {
        facade.addColumn(tableName, columnFamilyName, maxVersions, inMemory, scope);
    }

    /**
     * Answers if column family exists.
     * 
     * @param tableName the target table name
     * @param columnFamilyName the target column family name
     * @return true if the column exists, false otherwise
     */
    @Operation
    public boolean existsColumn(@Parameter(optional = false) final String tableName,
                                @Parameter(optional = false) final String columnFamilyName)
    {
        return facade.existsColumn(tableName, columnFamilyName);
    }

    /**
     * Changes one or more properties of a column family in a table. This operation
     * gracefully handles necessary table disabling and enabled.
     * 
     * @param tableName required the target table
     * @param columnFamilyName required the target column family
     * @param maxVersions the new max amount of versions
     * @param blocksize the the new block size
     * @param compressionType the new compression type
     * @param compactionCompressionType the new compaction compression type
     * @param inMemory new value for if values are stored in Region's cache
     * @param timeToLive new ttl
     * @param blockCacheEnabled new value of enabling block cache
     * @param bloomFilterType new value of bloom filter type
     * @param replicationScope
     * @param values
     */
    @Operation
    public void modifyColumn(@Parameter(optional = false) final String tableName,
                             @Parameter(optional = false) final String columnFamilyName,
                             @Parameter(optional = true) final Integer maxVersions,
                             @Parameter(optional = true) final Integer blocksize,
                             @Parameter(optional = true) final CompressionType compressionType,
                             @Parameter(optional = true) final CompressionType compactionCompressionType,
                             @Parameter(optional = true) final Boolean inMemory,
                             @Parameter(optional = true) final Integer timeToLive,
                             @Parameter(optional = true) final Boolean blockCacheEnabled,
                             @Parameter(optional = true) final BloomFilterType bloomFilterType,
                             @Parameter(optional = true) final Integer replicationScope,
                             @Parameter(optional = true) final Map<String, String> values)
    {
        facade.modifyColumn(tableName, columnFamilyName, maxVersions, blocksize, compressionType,
            compactionCompressionType, inMemory, timeToLive, blockCacheEnabled, bloomFilterType,
            replicationScope, values);
    }

    @Operation
    public void deleteColumn(@Parameter(optional = false) final String tableName,
                             @Parameter(optional = false) final String columnFamilyName)
    {
        facade.deleteColumn(tableName, columnFamilyName);
    }

    // ------------ Row Operations
    // TODO non string values

    /**
     * Answers the values at the given row - (table, row) combination
     * 
     * @param tableName
     * @param row
     * @param maxVersions
     * @param timestamp
     * @return the result
     */
    @Operation
    public Result getValues(@Parameter(optional = false) final String tableName,
                            @Parameter(optional = false) final String row,
                            @Parameter(optional = true) final Integer maxVersions,
                            @Parameter(optional = true) final Long timestamp)
    {
        return facade.get(tableName, row, maxVersions, timestamp);
    }

    /**
     * Saves a value at the specified (table, row, familyName, familyQualifier,
     * timestamp) combination
     * 
     * @param tableName
     * @param row
     * @param columnFamilyName the column family dimension
     * @param columnQualifier the column qualifier dimension
     * @param timestamp the version dimension
     * @param value
     * @param writeToWAL
     * @param lock
     */
    @Operation
    public void putValue(@Parameter(optional = false) final String tableName,
                         @Parameter(optional = false) final String row,
                         @Parameter(optional = false) final String columnFamilyName,
                         @Parameter(optional = false) final String columnQualifier,
                         @Parameter(optional = true) final Long timestamp,
                         @Parameter(optional = false) final String value,
                         @Parameter(optional = true) final Boolean writeToWAL,
                         @Parameter(optional = true) final RowLock lock)
    {
        facade.put(tableName, row, columnFamilyName, columnQualifier, timestamp, value, writeToWAL, lock);
    }

    /**
     * Deletes the values at a given row
     * 
     * @param tableName
     * @param row
     * @param columnFamilyName set null to delete all column families in the
     *            specified row
     * @param columnQualifier set null to delete all columns in the specified column
     *            family
     * @param timestamp set it to delete all versions of the specified column or
     *            column family with a timestamp less than or equal to the specified
     *            timestamp
     * @param deleteAllVersions set false to delete only the latest version of the
     *            specified column
     * @param lock
     */
    @Operation
    public void deleteValues(@Parameter(optional = false) final String tableName,
                             @Parameter(optional = false) final String row,
                             @Parameter(optional = true) final String columnFamilyName,
                             @Parameter(optional = true) final String columnQualifier,
                             @Parameter(optional = true) final Long timestamp,
                             @Parameter(optional = true) final Boolean deleteAllVersions,
                             @Parameter(optional = true) final RowLock lock)
    {
        facade.delete(tableName, row, columnFamilyName, columnQualifier, timestamp, deleteAllVersions, lock);
    }

    /**
     * Scans across all rows in a table, returning a scanner over it
     * 
     * @param tableName limits the scan to a specific table. This is the only
     *            required argument.
     * @param columnFamilyName limits the scan to a specific column family or null
     * @param columnQualifier limits the scan to a specific column or null. Requires
     *            a columnFamilyName to be defined.
     * @param timestamp limits the scan to a specific timestamp
     * @param maxTimestamp get versions of columns only within the specified
     *            timestamp range: [timestamp, maxTimestamp)
     * @param caching the number of rows for caching
     * @param batch the maximum number of values to return for each call to next() in
     *            the ResultScanner
     * @param cacheBlocks the number of rows for caching that will be passed to
     *            scanners
     * @param maxVersions limits the number of versions on each column
     * @param allVersions get all available versions on each column
     * @param startRow limits the beginning of the scan to the specified row
     *            inclusive
     * @param stopRow limits the end of the scan to the specified row exclusive
     * @return the scanner over the table
     */
    @Operation
    public ResultScanner scanTable(@Parameter(optional = false) final String tableName,
                                   @Parameter(optional = true) final String columnFamilyName,
                                   @Parameter(optional = true) final String columnQualifier,
                                   @Parameter(optional = true) final Long timestamp,
                                   @Parameter(optional = true) final Long maxTimestamp,
                                   @Parameter(optional = true) final Integer caching,
                                   @Parameter(optional = true) final Integer batch,
                                   @Parameter(optional = true) final Boolean cacheBlocks,
                                   @Parameter(optional = true) final Integer maxVersions,
                                   @Parameter(optional = true) final Boolean allVersions,
                                   @Parameter(optional = true) final String startRow,
                                   @Parameter(optional = true) final String stopRow)
    {
        return facade.scan(tableName, columnFamilyName, columnQualifier, timestamp, maxTimestamp, caching,
            batch, cacheBlocks, maxVersions, allVersions, startRow, stopRow);
    }

    /**
     * Atomically increments the value of at a (table, row, familyName,
     * familyQualifier) combination. If the cell value does not yet exist it is
     * initialized to amount.
     * 
     * @param tableName
     * @param row
     * @param columnFamilyName
     * @param columnQualifier
     * @param amount
     * @param writeToWAL set it to false means that in a fail scenario, you will lose
     *            any increments that have not been flushed.
     * @param writeToWAL
     * @return the new value, post increment
     */
    @Operation
    public long incrementValue(@Parameter(optional = false) final String tableName,
                               @Parameter(optional = false) final String row,
                               @Parameter(optional = false) final String columnFamilyName,
                               @Parameter(optional = false) final String columnQualifier,
                               @Parameter(optional = false) final long amount,
                               @Parameter(optional = false) final boolean writeToWAL)
    {
        return facade.increment(tableName, row, columnFamilyName, columnQualifier, amount, writeToWAL);
    }

    /**
     * Atomically checks if a value at a (table, row,family,qualifier) matches the
     * given one. If it does, it performs the put.
     * 
     * @param tableName
     * @param row
     * @param checkColumnFamilyName
     * @param checkColumnQualifier
     * @param checkValue
     * @param putColumnFamilyName
     * @param putColumnQualifier
     * @param putTimestamp
     * @param putValue
     * @param writeToWAL set it to false means that in a fail scenario, you will lose
     *            any increments that have not been flushed.
     * @param lock
     * @return true if the new put was executed, false otherwise
     */
    @Operation
    public boolean checkAndPutValue(@Parameter(optional = false) final String tableName,
                                    @Parameter(optional = false) final String row,
                                    @Parameter(optional = false) final String checkColumnFamilyName,
                                    @Parameter(optional = false) final String checkColumnQualifier,
                                    @Parameter(optional = false) final String checkValue,
                                    @Parameter(optional = false) final String putColumnFamilyName,
                                    @Parameter(optional = false) final String putColumnQualifier,
                                    @Parameter(optional = true) final Long putTimestamp,
                                    @Parameter(optional = false) final String putValue,
                                    @Parameter(optional = true) final Boolean putWriteToWAL,
                                    @Parameter(optional = true) final RowLock lock)
    {
        return facade.checkAndPut(tableName, row, checkColumnFamilyName, checkColumnQualifier, checkValue,
            putColumnFamilyName, putColumnQualifier, putTimestamp, putValue, putWriteToWAL, lock);
    }

    /**
     * Atomically checks if a value at a (table, row,family,qualifier) matches the
     * given one. If it does, it performs the delete.
     * 
     * @param tableName
     * @param row
     * @param checkColumnFamilyName
     * @param checkColumnQualifier
     * @param checkValue
     * @param deleteColumnFamilyName
     * @param deleteColumnQualifier
     * @param deleteTimestamp
     * @param deleteAllVersions
     * @param lock
     * @return true if the new delete was executed, false otherwise
     */
    @Operation
    public boolean checkAndDeleteValue(@Parameter(optional = false) final String tableName,
                                       @Parameter(optional = false) final String row,
                                       @Parameter(optional = false) final String checkColumnFamilyName,
                                       @Parameter(optional = false) final String checkColumnQualifier,
                                       @Parameter(optional = false) final String checkValue,
                                       @Parameter(optional = false) final String deleteColumnFamilyName,
                                       @Parameter(optional = false) final String deleteColumnQualifier,
                                       @Parameter(optional = true) final Long deleteTimestamp,
                                       @Parameter(optional = true) final Boolean deleteAllVersions,
                                       @Parameter(optional = true) final RowLock lock)
    {
        return facade.checkAndDelete(tableName, row, checkColumnFamilyName, checkColumnQualifier, checkValue,
            deleteColumnFamilyName, deleteColumnQualifier, deleteTimestamp, deleteAllVersions, lock);
    }

    // ------------ Configuration

    public void setFacade(HBaseService facade)
    {
        this.facade = HBaseServiceAdaptor.adapt(facade);
    }

    public HBaseService getFacade()
    {
        return facade;
    }

    public Map<String, String> getProperties()
    {
        return Collections.unmodifiableMap(properties);
    }

    public void setProperties(Map<String, String> properties)
    {
        this.properties = new HashMap<String, String>(properties);
    }

    /** @see org.mule.api.lifecycle.Initialisable#initialise() */
    public void initialise() throws InitialisationException
    {
        if (facade == null)
        {
            setFacade(new RPCHBaseService());
            facade.addProperties(properties);
        }
    }

}
