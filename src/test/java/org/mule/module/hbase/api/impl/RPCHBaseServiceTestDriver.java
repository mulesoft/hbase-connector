/*
 * Copyright (c) 2011 Zauber S.A.  -- All rights reserved
 */
package org.mule.module.hbase.api.impl;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.hbase.TableExistsException;
import org.apache.hadoop.hbase.client.Result;
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
    private static final String SOME_ROW_NAME = "some-row-name";
    private static final String SOME_COLUMN_QUALIFIER = "some-qualifier";
    private static final String SOME_VALUE = "some-value";

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
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("some-key", "some value");
        rpchBaseService.modifyColumn(SOME_TABLE_NAME, SOME_COLUMN_FAMILY_NAME, 
                7, 2048, null, "GZ", false, 123456, false, "ROW", 1, map);
        
        rpchBaseService.deleteColumn(SOME_TABLE_NAME, SOME_COLUMN_FAMILY_NAME);
        assertFalse(rpchBaseService.existsColumn(SOME_TABLE_NAME, SOME_COLUMN_FAMILY_NAME));
    }
    
    //------------ Row Operations
    
    @Test
    public void testRow() {
        rpchBaseService.createTable(SOME_TABLE_NAME);
        rpchBaseService.addColumn(SOME_TABLE_NAME, SOME_COLUMN_FAMILY_NAME, null, null, null);
        rpchBaseService.addColumn(SOME_TABLE_NAME, "family2", null, null, null);
        rpchBaseService.addColumn(SOME_TABLE_NAME, "family3", null, null, null);
        
        Result ret0 = rpchBaseService.get(SOME_TABLE_NAME, SOME_ROW_NAME, null, null);
        assertTrue(ret0.isEmpty());
        assertFalse(rpchBaseService.exists(SOME_TABLE_NAME, SOME_ROW_NAME, null, null));
        
        rpchBaseService.put(SOME_TABLE_NAME, SOME_ROW_NAME, SOME_COLUMN_FAMILY_NAME, "q1", null, "value1");
        Result ret1 = rpchBaseService.get(SOME_TABLE_NAME, SOME_ROW_NAME, null, null);
        assertEquals(1, ret1.list().size());
        assertTrue(rpchBaseService.exists(SOME_TABLE_NAME, SOME_ROW_NAME, null, null));
        long ret1timestamp = ret1.list().get(0).getTimestamp();
        assertTrue(rpchBaseService.exists(SOME_TABLE_NAME, SOME_ROW_NAME, null, ret1timestamp));
        
        rpchBaseService.put(SOME_TABLE_NAME, SOME_ROW_NAME, SOME_COLUMN_FAMILY_NAME, "q2", null, "value2");
        rpchBaseService.put(SOME_TABLE_NAME, SOME_ROW_NAME, SOME_COLUMN_FAMILY_NAME, "q3", null, "value3");
        rpchBaseService.put(SOME_TABLE_NAME, SOME_ROW_NAME, "family2", "q4", null, "value4");
        rpchBaseService.put(SOME_TABLE_NAME, SOME_ROW_NAME, "family2", "q5", null, "value5");
        rpchBaseService.put(SOME_TABLE_NAME, SOME_ROW_NAME, "family3", "q6", null, "value6");
        Result ret2 = rpchBaseService.get(SOME_TABLE_NAME, SOME_ROW_NAME, null, null);
        assertEquals(6, ret2.list().size()); //every family
        
        rpchBaseService.put(SOME_TABLE_NAME, SOME_ROW_NAME, SOME_COLUMN_FAMILY_NAME, "q2", null, "value2-2");
        Result ret3 = rpchBaseService.get(SOME_TABLE_NAME, SOME_ROW_NAME, 10, null);
        assertEquals(7, ret3.list().size()); //6 + 1 old version
        
        Result ret4 = rpchBaseService.get(SOME_TABLE_NAME, SOME_ROW_NAME, 10, ret1timestamp);
        assertEquals(1, ret4.list().size()); //the first version
        
        rpchBaseService.delete(SOME_TABLE_NAME, SOME_ROW_NAME, SOME_COLUMN_FAMILY_NAME, "q2", null, true);
        Result ret5 = rpchBaseService.get(SOME_TABLE_NAME, SOME_ROW_NAME, 10, null);
        assertEquals(5, ret5.list().size()); //q1 + q3 + q4 + q5 + q6
        
        rpchBaseService.delete(SOME_TABLE_NAME, SOME_ROW_NAME, SOME_COLUMN_FAMILY_NAME, null, null, true);
        Result ret6 = rpchBaseService.get(SOME_TABLE_NAME, SOME_ROW_NAME, 10, null);
        assertEquals(3, ret6.list().size()); //q4 + q5 + q6
        
        rpchBaseService.delete(SOME_TABLE_NAME, SOME_ROW_NAME, null, null, null, true);
        Result ret7 = rpchBaseService.get(SOME_TABLE_NAME, SOME_ROW_NAME, 10, null);
        assertTrue(ret7.isEmpty());
    }
    
}
