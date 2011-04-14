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

    /** @see org.mule.module.hbase.api.HBaseService#createTable(java.lang.String) */
    public void createTable(String name) {
        throw new NotImplementedException();
    }

    /** @see org.mule.module.hbase.api.HBaseService#get(java.lang.String, java.lang.String) */
    public Result get(String tableName, String row) {
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

    /** @see org.mule.module.hbase.api.HBaseService#existsTable(java.lang.String) */
    public boolean existsTable(String name) {
        throw new NotImplementedException();
    }

    /** @see org.mule.module.hbase.api.HBaseService#deleteTable(java.lang.String) */
    public void deleteTable(String name) {
        throw new NotImplementedException();
    }

    /** @see org.mule.module.hbase.api.HBaseService#isDisabledTable(java.lang.String) */
    public boolean isDisabledTable(String name) throws HBaseServiceException {
        throw new NotImplementedException();
    }

    /** @see org.mule.module.hbase.api.HBaseService#enableTable(java.lang.String) */
    public void enableTable(String name) throws HBaseServiceException {
        throw new NotImplementedException();
    }

    /** @see org.mule.module.hbase.api.HBaseService#disabeTable(java.lang.String) */
    public void disabeTable(String name) throws HBaseServiceException {
        throw new NotImplementedException();
    }

    /** @see org.mule.module.hbase.api.HBaseService#addColumn(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Boolean, java.lang.Integer) */
    public void addColumn(String tableName, String columnFamilyName,
            Integer maxVersions, Boolean inMemory, Integer scope) {
        throw new NotImplementedException();
    }

    /** @see org.mule.module.hbase.api.HBaseService#existsColumn(java.lang.String, java.lang.String) */
    public boolean existsColumn(String tableName, String columnFamilyName) {
        throw new NotImplementedException();
    }

    /** @see org.mule.module.hbase.api.HBaseService#deleteColumn(java.lang.String, java.lang.String) */
    public void deleteColumn(String tableName, String columnFamilyName) {
        throw new NotImplementedException();
    }

}
