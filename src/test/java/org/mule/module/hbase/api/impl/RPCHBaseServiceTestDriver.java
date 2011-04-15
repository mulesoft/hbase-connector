/*
 * Copyright (c) 2011 Zauber S.A.  -- All rights reserved
 */
package org.mule.module.hbase.api.impl;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.hbase.TableExistsException;
import org.junit.Before;
import org.junit.Test;
import org.mule.module.hbase.api.HBaseServiceException;

/**
 * <p>Testing the {@link RPCHBaseService} implementation.</p>
 * <em>It requires an HBase 0.90.x server running on localhost with the default ports.</em>
 * 
 * @author Pablo Martin Grigolatto
 * @since Apr 12, 2011
 */
public class RPCHBaseServiceTestDriver {

    private static final String SOME_TABLE_NAME = "some-table-name";
    private static final String SOME_COLUMN_FAMILY_NAME = "some-column-family-name";

    private static RPCHBaseService rpchBaseService = new RPCHBaseService(); //shared between all tests
    private static Map<String, String> properties;

    @Before
    public void before() {
        properties = new HashMap<String, String>();
        properties.put("hbase.zookeeper.quorum", "127.0.0.1");
        properties.put("hbase.zookeeper.peerport", "2888");
        properties.put("hbase.zookeeper.property.clientPort", "2181");
        properties.put("hbase.zookeeper.leaderport", "3888");
        properties.put("hbase.master.port", "60000");
        properties.put("hbase.master.info.port", "60010");
        properties.put("hbase.regionserver.port", "60020");
        properties.put("hbase.regionserver.info.port", "60030");
        properties.put("hbase.rest.port", "8080");
        properties.put("zookeeper.znode.parent", "/hbase");
        
        properties.put("ipc.client.connect.max.retries", "2");
        properties.put("hbase.client.retries.number", "2");
        properties.put("hbase.client.rpc.maxattempts", "2");
        properties.put("hbase.rpc.timeout", "7000");
        properties.put("hbase.client.prefetch.limit", "3");

        properties.put("ipc.client.connection.maxidletime", "10000");
        properties.put("zookeeper.session.timeout", "10000");
        properties.put("hbase.zookeeper.property.maxClientCnxns", "3");
        
        //reset properties for each test
        rpchBaseService.addProperties(properties);
        
        //reset the database for each test
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
    public void testNotAlive() {
        properties.put("hbase.zookeeper.peerport", "2889");
        properties.put("hbase.zookeeper.property.clientPort", "2182");
        properties.put("hbase.zookeeper.leaderport", "3889");
        properties.put("hbase.master.port", "60001");
        properties.put("hbase.master.info.port", "60011");
        properties.put("hbase.regionserver.port", "60021");
        properties.put("hbase.regionserver.info.port", "60031");
        properties.put("hbase.rest.port", "8081");
        properties.put("zookeeper.znode.parent", "/anotherpath");
        
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
        assertFalse(rpchBaseService.isDisabledTable("another-table-name"));
    }
    
    /** table column management */
    @Test
    public void testColumnAdmin() {
        rpchBaseService.createTable(SOME_TABLE_NAME);
        assertFalse(rpchBaseService.existsColumn(SOME_TABLE_NAME, SOME_COLUMN_FAMILY_NAME));
        
        rpchBaseService.addColumn(SOME_TABLE_NAME, SOME_COLUMN_FAMILY_NAME, 5, false, null);
        assertTrue(rpchBaseService.existsColumn(SOME_TABLE_NAME, SOME_COLUMN_FAMILY_NAME));
        
        rpchBaseService.deleteColumn(SOME_TABLE_NAME, SOME_COLUMN_FAMILY_NAME);
        assertFalse(rpchBaseService.existsColumn(SOME_TABLE_NAME, SOME_COLUMN_FAMILY_NAME));
    }
    
    //------------ Row Operations

    
}
