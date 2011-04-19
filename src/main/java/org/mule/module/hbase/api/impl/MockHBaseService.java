/*
 * Copyright (c) 2011 Zauber S.A.  -- All rights reserved
 */
package org.mule.module.hbase.api.impl;

import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.mule.module.hbase.api.HBaseService;
import org.mule.module.hbase.api.HBaseServiceException;

/**
 * {@link HBaseService} implementation that always throws exceptions
 * 
 * @author Pablo Martin Grigolatto
 * @since Apr 11, 2011
 */
public class MockHBaseService implements HBaseService {

    /** @see HBaseService#createTable(String) */
    public void createTable(String name) {
        throw new NotImplementedException();
    }

    /** @see HBaseService#addProperties(Map) */
    public void addProperties(Map<String, String> properties) {
        throw new NotImplementedException();
    }

    /** @see HBaseService#alive() */
    public boolean alive() {
        throw new NotImplementedException();
    }

    /** @see HBaseService#existsTable(String) */
    public boolean existsTable(String name) {
        throw new NotImplementedException();
    }

    /** @see HBaseService#deleteTable(String) */
    public void deleteTable(String name) {
        throw new NotImplementedException();
    }

    /** @see HBaseService#isDisabledTable(String) */
    public boolean isDisabledTable(String name) throws HBaseServiceException {
        throw new NotImplementedException();
    }

    /** @see HBaseService#enableTable(String) */
    public void enableTable(String name) throws HBaseServiceException {
        throw new NotImplementedException();
    }

    /** @see HBaseService#disabeTable(String) */
    public void disabeTable(String name) throws HBaseServiceException {
        throw new NotImplementedException();
    }

    /** @see HBaseService#addColumn(String, String, Integer, Boolean, Integer) */
    public void addColumn(String tableName, String columnFamilyName,
            Integer maxVersions, Boolean inMemory, Integer scope) {
        throw new NotImplementedException();
    }

    /** @see HBaseService#existsColumn(String, String) */
    public boolean existsColumn(String tableName, String columnFamilyName) {
        throw new NotImplementedException();
    }
    
    /** @see HBaseService#modifyColumn(String, String, Integer, Integer, String, 
     *  String, Boolean, Integer, Boolean, String, Integer, Map) */
    public void modifyColumn(String tableName, String columnFamilyName,
            Integer maxVersions, Integer blocksize, String compressionType,
            String compactionCompressionType, Boolean inMemory,
            Integer timeToLive, Boolean blockCacheEnabled,
            String bloomFilterType, Integer replicationScope,
            Map<String, String> values) {
        throw new NotImplementedException();
    }

    /** @see HBaseService#deleteColumn(String, String) */
    public void deleteColumn(String tableName, String columnFamilyName) {
        throw new NotImplementedException();
    }

    /** @see HBaseService#get(String, String, Integer, Long) */
    public Result get(String tableName, String row, Integer maxVersions, Long timestamp) 
            throws HBaseServiceException {
        throw new NotImplementedException();
    }

    /** @see HBaseService#put(String, String, String, String, Long, String, Boolean) */
    public void put(String tableName, String row, String columnFamilyName,
            String columnQualifier, Long timestamp, String value, Boolean writeToWAL)
            throws HBaseServiceException {
        throw new NotImplementedException();
    }

    /** @see HBaseService#exists(String, String, Integer, Long) */
    public boolean exists(String tableName, String row, Integer maxVersions,
            Long timestamp) throws HBaseServiceException {
        throw new NotImplementedException();
    }

    /** @see HBaseService#delete(String, String, String, String, Long, boolean) */
    public void delete(String tableName, String row, String columnFamilyName,
            String columnQualifier, Long timestamp, boolean deleteAllVersions)
            throws HBaseServiceException {
        throw new NotImplementedException();
    }

    /** @see HBaseService#scan(String, String, String, Long, Long, Integer, Integer, 
     *                         Boolean, Integer, Boolean, String, String) */
    public ResultScanner scan(String tableName, String columnFamilyName,
            String columnQualifier, Long timestamp, Long maxTimestamp,
            Integer caching, Integer batch, Boolean cacheBlocks,
            Integer maxVersions, Boolean allVersions, String startRow,
            String stopRow) throws HBaseServiceException {
        throw new NotImplementedException();
    }

    /** @see HBaseService#increment(String, String, String, String, long, boolean) */
    public long increment(String tableName, String row,
            String columnFamilyName, String columnQualifier, long amount,
            boolean writeToWAL) {
        throw new NotImplementedException();
    }

    /** @see HBaseService#checkAndPut(
     *       String, String, String, String, String, String, String, Long, String, Boolean) */
    public boolean checkAndPut(String tableName, String row,
            String checkColumnFamilyName, String checkColumnQualifier,
            String checkValue, String putColumnFamilyName,
            String putColumnQualifier, Long putTimestamp, String putValue,
            Boolean putWriteToWAL) {
        throw new NotImplementedException();
    }

    /** @see HBaseService#checkAndDelete(
     *      String, String, String, String, String, String, String, Long, Boolean) */
    public boolean checkAndDelete(String tableName, String row,
            String checkColumnFamilyName, String checkColumnQualifier,
            String checkValue, String deleteColumnFamilyName,
            String deleteColumnQualifier, Long deleteTimestamp,
            Boolean deleteAllVersions) throws HBaseServiceException {
        throw new NotImplementedException();
    }

}
