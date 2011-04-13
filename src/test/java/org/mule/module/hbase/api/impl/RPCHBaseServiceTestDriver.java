/*
 * Copyright (c) 2011 Zauber S.A.  -- All rights reserved
 */
package org.mule.module.hbase.api.impl;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.hbase.TableExistsException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mule.module.hbase.api.HBaseServiceException;
import org.mule.module.hbase.api.RPCHBaseService;

/**
 * <p>Testing the {@link RPCHBaseService} implementation.</p>
 * <em>It requires an HBase 0.90.x server running on localhost with the default ports.</em>
 * 
 * @author Pablo Martin Grigolatto
 * @since Apr 12, 2011
 */
public class RPCHBaseServiceTestDriver {

    private static final String SOME_TABLE_NAME = "some-table-name";

    private RPCHBaseService rpchBaseService;
    private Map<String, String> properties;

    @Before
    public void before() {
        rpchBaseService = new RPCHBaseService();
        properties = new HashMap<String, String>();
        properties.put("hbase.zookeeper.quorum", "127.0.0.1");
        
        properties.put("ipc.client.connect.max.retries", "2");
        properties.put("hbase.client.retries.number", "2");
        properties.put("hbase.client.rpc.maxattempts", "2");
        properties.put("hbase.rpc.timeout", "7000");
        properties.put("hbase.client.prefetch.limit", "3");
        
        rpchBaseService.addProperties(properties);
        
        if (rpchBaseService.existsTable(SOME_TABLE_NAME)) {
            rpchBaseService.deleteTable(SOME_TABLE_NAME);
        }
    }

    //------------ Admin Operations
    
    @Test
    public void testAlive() {
        assertTrue(rpchBaseService.alive());
    }

    /** should fail because the server is running at 2181 by default */
    @Test
    @Ignore
    public void testNotAlive() {
        properties.put("hbase.zookeeper.property.clientPort", "5000");
        rpchBaseService.addProperties(properties);
        assertFalse(rpchBaseService.alive());
    }

    /** table management */
    @Test
    public void testTableAdmin() {
        //creates a new table
        assertFalse(rpchBaseService.existsTable(SOME_TABLE_NAME));
        rpchBaseService.createTable(SOME_TABLE_NAME);
        assertTrue(rpchBaseService.existsTable(SOME_TABLE_NAME));
        
        //table already exists
        try {
            rpchBaseService.createTable(SOME_TABLE_NAME);
            fail("table should exist");
        } catch (HBaseServiceException e) {
            if (e.getCause() instanceof TableExistsException) {
                //ok
            } else {
                fail("unexpected exception: " + e.getCause());
            }
        }
        
        //delete the table
        rpchBaseService.deleteTable(SOME_TABLE_NAME);
        assertFalse(rpchBaseService.existsTable(SOME_TABLE_NAME));
    }
    
    /** table enable/disable */
    @Test
    public void testTableEnable() {
        rpchBaseService.createTable(SOME_TABLE_NAME);
        assertFalse(rpchBaseService.isDisabledTable(SOME_TABLE_NAME));

        rpchBaseService.disabeTable(SOME_TABLE_NAME);
        assertTrue(rpchBaseService.isDisabledTable(SOME_TABLE_NAME));
        
        rpchBaseService.enableTable(SOME_TABLE_NAME);
        assertFalse(rpchBaseService.isDisabledTable(SOME_TABLE_NAME));
    }
        
    /** a table is not disabled even if it does not exists */
    @Test
    public void testTableNotDisabled() {
        properties.put("hbase.zookeeper.property.clientPort", "5000");
        rpchBaseService.addProperties(properties);
        assertFalse(rpchBaseService.isDisabledTable(SOME_TABLE_NAME));
    }
    
    //------------ Row Operations

    
}
