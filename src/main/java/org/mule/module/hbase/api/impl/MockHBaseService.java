/*
 * Copyright (c) 2011 Zauber S.A.  -- All rights reserved
 */
package org.mule.module.hbase.api.impl;

import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.apache.hadoop.hbase.client.Result;
import org.mule.module.hbase.api.HBaseService;
import org.mule.module.hbase.api.HBaseServiceException;

/**
 * {@link HBaseService} implementation that always throws exceptions
 * 
 * @author Pablo Martin Grigolatto
 * @since Apr 11, 2011
 */
public class MockHBaseService implements HBaseService {

    /** @see org.mule.module.hbase.api.HBaseService#createTable(String) */
    public void createTable(String name) {
        throw new NotImplementedException();
    }

    /** @see org.mule.module.hbase.api.HBaseService#addProperties(java.util.Map) */
    public void addProperties(Map<String, String> properties) {
        throw new NotImplementedException();
    }

    /** @see org.mule.module.hbase.api.HBaseService#alive() */
    public boolean alive() {
        throw new NotImplementedException();
    }

    /** @see org.mule.module.hbase.api.HBaseService#existsTable(String) */
    public boolean existsTable(String name) {
        throw new NotImplementedException();
    }

    /** @see org.mule.module.hbase.api.HBaseService#deleteTable(String) */
    public void deleteTable(String name) {
        throw new NotImplementedException();
    }

    /** @see org.mule.module.hbase.api.HBaseService#isDisabledTable(String) */
    public boolean isDisabledTable(String name) throws HBaseServiceException {
        throw new NotImplementedException();
    }

    /** @see org.mule.module.hbase.api.HBaseService#enableTable(String) */
    public void enableTable(String name) throws HBaseServiceException {
        throw new NotImplementedException();
    }

    /** @see org.mule.module.hbase.api.HBaseService#disabeTable(String) */
    public void disabeTable(String name) throws HBaseServiceException {
        throw new NotImplementedException();
    }

    /** @see org.mule.module.hbase.api.HBaseService#addColumn(String, String, Integer, Boolean, Integer) */
    public void addColumn(String tableName, String columnFamilyName,
            Integer maxVersions, Boolean inMemory, Integer scope) {
        throw new NotImplementedException();
    }

    /** @see org.mule.module.hbase.api.HBaseService#existsColumn(String, String) */
    public boolean existsColumn(String tableName, String columnFamilyName) {
        throw new NotImplementedException();
    }
    
    /** @see org.mule.module.hbase.api.HBaseService#modifyColumn(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Boolean, java.lang.Integer, java.lang.Boolean, java.lang.String, java.lang.Integer, java.util.Map) */
    public void modifyColumn(String tableName, String columnFamilyName,
            Integer maxVersions, Integer blocksize, String compressionType,
            String compactionCompressionType, Boolean inMemory,
            Integer timeToLive, Boolean blockCacheEnabled,
            String bloomFilterType, Integer replicationScope,
            Map<String, String> values) {
        throw new NotImplementedException();
    }

    /** @see org.mule.module.hbase.api.HBaseService#deleteColumn(String, String) */
    public void deleteColumn(String tableName, String columnFamilyName) {
        throw new NotImplementedException();
    }

    /** @see org.mule.module.hbase.api.HBaseService#get(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Long) */
    public Result get(String tableName, String row, Integer maxVersions, Long timestamp) throws HBaseServiceException {
            throw new NotImplementedException();
    }

    /** @see org.mule.module.hbase.api.HBaseService#put(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Long, java.lang.String) */
    public void put(String tableName, String row, String columnFamilyName,
            String columnQualifier, Long timestamp, String value)
            throws HBaseServiceException {
        throw new NotImplementedException();
    }

}
