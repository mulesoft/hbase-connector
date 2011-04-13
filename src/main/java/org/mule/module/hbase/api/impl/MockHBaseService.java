/*
 * Copyright (c) 2011 Zauber S.A.  -- All rights reserved
 */
package org.mule.module.hbase.api.impl;

import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.apache.hadoop.hbase.client.Result;
import org.mule.module.hbase.api.HBaseService;

/**
 * TODO: Description of the class, Comments in english by default  
 * 
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

}
