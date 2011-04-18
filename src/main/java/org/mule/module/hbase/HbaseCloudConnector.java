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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.hbase.client.Result;
import org.mule.api.lifecycle.Initialisable;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.module.hbase.api.HBaseService;
import org.mule.module.hbase.api.impl.RPCHBaseService;
import org.mule.tools.cloudconnect.annotations.Connector;
import org.mule.tools.cloudconnect.annotations.Operation;
import org.mule.tools.cloudconnect.annotations.Parameter;
import org.mule.tools.cloudconnect.annotations.Property;

/**
 * <p>HBase connector</p>
 * <p>It delegates each operation on a {@link HBaseService} 
 *    and it accepts custom configuration in a Key-Value fashion</p>
 * 
 * @author Pablo Martin Grigolatto
 * @since Apr 18, 2011
 */
@Connector(namespacePrefix="hbase")
public class HbaseCloudConnector implements Initialisable
{
    @Property(name = "facade-ref", optional = true) 
    private HBaseService facade;
    
    @Property(name = "properties-ref", optional = true) 
    private Map<String, String> properties;

    public HbaseCloudConnector() {
        properties = Collections.emptyMap();
    }
    
    //------------ Admin Operations

    @Operation
    public boolean alive() {
        return facade.alive(); 
    }
    
    @Operation
    public void createTable(@Parameter(optional = false) final String name) {
        facade.createTable(name);
    }
    
    @Operation
    public boolean existsTable(@Parameter(optional = false) final String name) {
        return facade.existsTable(name);
    }

    @Operation
    public void deleteTable(@Parameter(optional = false) final String name) {
        facade.deleteTable(name);
    }
    
    @Operation
    public boolean isEnabledTable(@Parameter(optional = false) final String name) {
        return facade.isDisabledTable(name);
    }
    
    @Operation
    public void enableTable(@Parameter(optional = false) final String name) {
        facade.enableTable(name);
    }
    
    @Operation
    public void disableTable(@Parameter(optional = false) final String name) {
        facade.disabeTable(name);
    }
    
    @Operation
    public void addColumn(
            @Parameter(optional = false) final String tableName, 
            @Parameter(optional = false) final String columnFamilyName, 
            @Parameter(optional = true) final Integer maxVersions, 
            @Parameter(optional = true) final Boolean inMemory, 
            @Parameter(optional = true) final Integer scope) {
        facade.addColumn(tableName, columnFamilyName, maxVersions, inMemory, scope);
    }
    
    @Operation
    public boolean existsColumn(
            @Parameter(optional = false) final String tableName, 
            @Parameter(optional = false) final String columnFamilyName) {
        return facade.existsColumn(tableName, columnFamilyName);
    }
    
    @Operation
    public void modifyColumn(
            @Parameter(optional = false) final String tableName, 
            @Parameter(optional = false) final String columnFamilyName,
            @Parameter(optional = true) final Integer maxVersions, 
            @Parameter(optional = true) final Integer blocksize,
            @Parameter(optional = true) final String compressionType, 
            @Parameter(optional = true) final String compactionCompressionType, 
            @Parameter(optional = true) final Boolean inMemory, 
            @Parameter(optional = true) final Integer timeToLive,
            @Parameter(optional = true) final Boolean blockCacheEnabled, 
            @Parameter(optional = true) final String bloomFilterType, 
            @Parameter(optional = true) final Integer replicationScope,
            @Parameter(optional = true) final Map<String, String> values) {
        facade.modifyColumn(
            tableName, columnFamilyName, maxVersions, blocksize, compressionType,              
            compactionCompressionType, inMemory, timeToLive, 
            blockCacheEnabled, bloomFilterType, replicationScope, values);
    }
    
    @Operation
    public void deleteColumn(
            @Parameter(optional = false) final String tableName, 
            @Parameter(optional = false) final String columnFamilyName) {
        facade.deleteColumn(tableName, columnFamilyName);
    }
    

    //------------ Row Operations
    
    @Operation
    public Result get(
            @Parameter(optional = false) final String tableName, 
            @Parameter(optional = false) final String row, 
            @Parameter(optional = true) final Integer maxVersions, 
            @Parameter(optional = true) final Long timestamp) {
        return facade.get(tableName, row, maxVersions, timestamp);
    }
    
    @Operation
    public void put(
            @Parameter(optional = false) final String tableName, 
            @Parameter(optional = false) final String row,
            @Parameter(optional = false) final String columnFamilyName,
            @Parameter(optional = false) final String columnQualifier, 
            @Parameter(optional = true) final Long timestamp,
            @Parameter(optional = false) final String value) {
        facade.put(tableName, row, columnFamilyName, columnQualifier, timestamp, value);
    }
    
    @Operation
    public void delete(
            @Parameter(optional = false) final String tableName, 
            @Parameter(optional = false) final String row,
            @Parameter(optional = true) final String columnFamilyName, 
            @Parameter(optional = true) final String columnQualifier, 
            @Parameter(optional = true) final Long timestamp, 
            @Parameter(optional = true) final Boolean deleteAllVersions) {
        facade.delete(tableName, row, columnFamilyName, columnQualifier, timestamp, deleteAllVersions);
    }
    
    @Operation
    public void scan(
            @Parameter(optional = false) final String tableName, 
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
            @Parameter(optional = true) final String stopRow) {
        facade.scan(tableName, columnFamilyName, columnQualifier, timestamp, maxTimestamp, caching, 
                    batch, cacheBlocks, maxVersions, allVersions, startRow, stopRow);
    }

    
    //------------ Configuration
    
    public void setFacade(HBaseService facade) {
        this.facade = facade;
    }
    public HBaseService getFacade() {
        return facade;
    }
    public Map<String, String> getProperties() {
        return Collections.unmodifiableMap(properties);
    }
    public void setProperties(Map<String, String> properties) {
        this.properties = new HashMap<String, String>(properties);
    }

    /** @see org.mule.api.lifecycle.Initialisable#initialise() */
    public void initialise() throws InitialisationException {
        if (facade == null) {
            facade = new RPCHBaseService();
            facade.addProperties(properties);
        }
    }

}
