/*
 * Copyright (c) 2011 Zauber S.A.  -- All rights reserved
 */
package org.mule.module.hbase.api.impl;

import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.hadoop.hbase.TableExistsException;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.RowLock;
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

    private static final Charset UTF8= Charset.forName("UTF-8");
    private static final String SOME_TABLE_NAME = "some-table-name";
    private static final String SOME_COLUMN_FAMILY_NAME = "some-column-family-name";
    private static final String SOME_ROW_NAME = "some-row-name";
    private static final String SOME_COLUMN_QUALIFIER = "some-qualifier";

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
        
        properties.put("hbase.hstore.blockingWaitTime", "30000");
        
        
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
        
        rpchBaseService.put(SOME_TABLE_NAME, SOME_ROW_NAME, SOME_COLUMN_FAMILY_NAME, "q1", null, "value1", false, null);
        Result ret1 = rpchBaseService.get(SOME_TABLE_NAME, SOME_ROW_NAME, null, null);
        assertEquals(1, ret1.list().size());
        assertTrue(rpchBaseService.exists(SOME_TABLE_NAME, SOME_ROW_NAME, null, null));
        long ret1timestamp = ret1.list().get(0).getTimestamp();
        assertTrue(rpchBaseService.exists(SOME_TABLE_NAME, SOME_ROW_NAME, null, ret1timestamp));
        
        rpchBaseService.put(SOME_TABLE_NAME, SOME_ROW_NAME, SOME_COLUMN_FAMILY_NAME, "q2", null, "value2", false, null);
        rpchBaseService.put(SOME_TABLE_NAME, SOME_ROW_NAME, SOME_COLUMN_FAMILY_NAME, "q3", null, "value3", false, null);
        rpchBaseService.put(SOME_TABLE_NAME, SOME_ROW_NAME, "family2", "q4", null, "value4", false, null);
        rpchBaseService.put(SOME_TABLE_NAME, SOME_ROW_NAME, "family2", "q5", null, "value5", false, null);
        rpchBaseService.put(SOME_TABLE_NAME, SOME_ROW_NAME, "family3", "q6", null, "value6", false, null);
        Result ret2 = rpchBaseService.get(SOME_TABLE_NAME, SOME_ROW_NAME, null, null);
        assertEquals(6, ret2.list().size()); //every family
        
        rpchBaseService.put(SOME_TABLE_NAME, SOME_ROW_NAME, SOME_COLUMN_FAMILY_NAME, "q2", null, "value2-2", false, null);
        Result ret3 = rpchBaseService.get(SOME_TABLE_NAME, SOME_ROW_NAME, 10, null);
        assertEquals(7, ret3.list().size()); //6 + 1 old version
        
        Result ret4 = rpchBaseService.get(SOME_TABLE_NAME, SOME_ROW_NAME, 10, ret1timestamp);
        assertEquals(1, ret4.list().size()); //the first version
        
        rpchBaseService.delete(SOME_TABLE_NAME, SOME_ROW_NAME, SOME_COLUMN_FAMILY_NAME, "q2", null, true, null);
        Result ret5 = rpchBaseService.get(SOME_TABLE_NAME, SOME_ROW_NAME, 10, null);
        assertEquals(5, ret5.list().size()); //q1 + q3 + q4 + q5 + q6
        
        rpchBaseService.delete(SOME_TABLE_NAME, SOME_ROW_NAME, SOME_COLUMN_FAMILY_NAME, null, null, true, null);
        Result ret6 = rpchBaseService.get(SOME_TABLE_NAME, SOME_ROW_NAME, 10, null);
        assertEquals(3, ret6.list().size()); //q4 + q5 + q6
        
        rpchBaseService.delete(SOME_TABLE_NAME, SOME_ROW_NAME, null, null, null, true, null);
        Result ret7 = rpchBaseService.get(SOME_TABLE_NAME, SOME_ROW_NAME, 10, null);
        assertTrue(ret7.isEmpty());
    }
    
    @Test
    public void testScanRow() {
        rpchBaseService.createTable(SOME_TABLE_NAME);
        rpchBaseService.addColumn(SOME_TABLE_NAME, "family1", null, null, null);
        rpchBaseService.addColumn(SOME_TABLE_NAME, "family2", null, null, null);
        rpchBaseService.addColumn(SOME_TABLE_NAME, "family3", null, null, null);
        rpchBaseService.addColumn(SOME_TABLE_NAME, "family4", null, null, null);
             
        ResultScanner ret1 = rpchBaseService.scan(SOME_TABLE_NAME, null, null, null, null, null, null, null, null, null, null, null);
        assertFalse(ret1.iterator().hasNext());
        
        rpchBaseService.put(SOME_TABLE_NAME, SOME_ROW_NAME, "family1", "q1", null, "value1", false, null);
        Result row1 = rpchBaseService.get(SOME_TABLE_NAME, SOME_ROW_NAME, null, null);
        final long r1Timestamp = row1.list().get(0).getTimestamp();
        rpchBaseService.put(SOME_TABLE_NAME, SOME_ROW_NAME, "family1", "q2", null, "value2", false, null);
        rpchBaseService.put(SOME_TABLE_NAME, SOME_ROW_NAME, "family1", "q3", null, "value3", false, null);
        rpchBaseService.put(SOME_TABLE_NAME, SOME_ROW_NAME, "family2", "q4", null, "value4", false, null);
        rpchBaseService.put(SOME_TABLE_NAME, SOME_ROW_NAME, "family2", "q5", null, "value5", false, null);
        rpchBaseService.put(SOME_TABLE_NAME, SOME_ROW_NAME, "family3", "q6", null, "value6", false, null);
        
        rpchBaseService.put(SOME_TABLE_NAME, "r2", "family2", "q1", null, "r2f2q1value", false, null);
        rpchBaseService.put(SOME_TABLE_NAME, "r2", "family2", "q2", null, "r2f2q2value", false, null);
        Result row2 = rpchBaseService.get(SOME_TABLE_NAME, "r2", null, null);
        final long r2Timestamp = row2.list().get(0).getTimestamp();
        
        rpchBaseService.put(SOME_TABLE_NAME, "r3", "family2", "q1", null, "r3f2q1value", false, null);
        rpchBaseService.put(SOME_TABLE_NAME, "r3", "family3", "q2", null, "r3f3q2value", false, null);
        Result row3 = rpchBaseService.get(SOME_TABLE_NAME, "r3", null, null);
        final long r3Timestamp = row3.list().get(0).getTimestamp();
        
        rpchBaseService.put(SOME_TABLE_NAME, "r4", "family3", "q1", null, "r4f3q1value", false, null);
        rpchBaseService.put(SOME_TABLE_NAME, "r4", "family4", "q2", null, "r4f4q2value", false, null);

        try {
            rpchBaseService.scan(null, null, null, null, null, null, null, null, null, null, null, null);
            fail("table name is required");
        } catch (IllegalArgumentException e) {
            // ok
        }
        
        //no filters
        final ResultScanner ret2 = rpchBaseService.scan(
            SOME_TABLE_NAME, null, null, null, null, null, null, null, null, null, null, null);
        assertEquals(4, count(ret2));
        
        //column family
        final ResultScanner ret3 = rpchBaseService.scan(
            SOME_TABLE_NAME, "family1", null, null, null, null, null, null, null, null, null, null);
        Iterator<Result> it3 = ret3.iterator();
        assertTrue(it3.hasNext());
        assertEquals("value1", 
            new String(it3.next().getValue("family1".getBytes(UTF8), "q1".getBytes(UTF8)), UTF8));
        assertFalse(it3.hasNext());
        
        //column qualifier
        final ResultScanner ret4 = rpchBaseService.scan(
            SOME_TABLE_NAME, "family2", "q1", null, null, null, null, null, null, null, null, null);
        assertEquals(2, count(ret4));
        
        //exclusive stop
        final ResultScanner ret5 = rpchBaseService.scan(
                SOME_TABLE_NAME, "family2", "q1", null, null, null, null, null, null, null, null, "r3");
        assertEquals(1, count(ret5));
        
        //specific timestamp
        final ResultScanner ret6 = rpchBaseService.scan(
                SOME_TABLE_NAME, null, null, r1Timestamp, null, null, null, null, null, null, null, null);
        assertEquals(1, count(ret6));
        
        //max timestamp is exclusive
        final ResultScanner ret7 = rpchBaseService.scan(
                SOME_TABLE_NAME, null, null, r1Timestamp, r2Timestamp, null, null, null, null, null, null, null);
        assertEquals(1, count(ret7));
        
        //max timestamp is exclusive
        final ResultScanner ret8 = rpchBaseService.scan(
                SOME_TABLE_NAME, null, null, r1Timestamp, r3Timestamp, null, null, null, null, null, null, null);
        assertEquals(2, count(ret8));
    
        final ResultScanner ret9 = rpchBaseService.scan(
                SOME_TABLE_NAME, null, null, null, null, 5, null, null, null, null, null, null);
        assertEquals(4, count(ret9));
        
        final ResultScanner ret10 = rpchBaseService.scan(
                SOME_TABLE_NAME, null, null, null, null, null, 10, null, null, null, null, null);
        assertEquals(4, count(ret10));
        

        final ResultScanner ret11 = rpchBaseService.scan(
                SOME_TABLE_NAME, null, null, null, null, null, null, true, null, null, null, null);
        assertEquals(4, count(ret11));
        
        //more than one version
        assertEquals(1, rpchBaseService.scan(
            SOME_TABLE_NAME, "family4", "q2", null, null, null, null, null, null, null, null, null)
            .iterator().next().getColumn("family4".getBytes(UTF8), "q2".getBytes(UTF8)).size());
        rpchBaseService.put(SOME_TABLE_NAME, "r4", "family4", "q2", null, "r4f4q2value-v2", false, null);
        rpchBaseService.put(SOME_TABLE_NAME, "r4", "family4", "q2", null, "r4f4q2value-v3", false, null);
        rpchBaseService.put(SOME_TABLE_NAME, "r4", "family4", "q2", null, "r4f4q2value-v4", false, null);
        assertEquals(2, rpchBaseService.scan(
            SOME_TABLE_NAME, "family4", "q2", null, null, null, null, null, 2, null, null, null)
            .iterator().next().getColumn("family4".getBytes(UTF8), "q2".getBytes(UTF8)).size());
        
        //all versions
        assertEquals(3, rpchBaseService.scan(
            SOME_TABLE_NAME, "family4", "q2", null, null, null, null, null, null, true, null, null)
            .iterator().next().getColumn("family4".getBytes(UTF8), "q2".getBytes(UTF8)).size());
            
        //exclusive stop row
        assertEquals(1, count(rpchBaseService.scan(
            SOME_TABLE_NAME, null, null, null, null, null, null, null, null, null, "r2", "r3")));
        assertEquals(2, count(rpchBaseService.scan(
            SOME_TABLE_NAME, null, null, null, null, null, null, null, null, null, "r2", "r4")));
        assertEquals(1, count(rpchBaseService.scan(
            SOME_TABLE_NAME, null, null, null, null, null, null, null, null, null, null, "r3")));
    }
    
    @Test
    public void testIncrementValue() {
        rpchBaseService.createTable(SOME_TABLE_NAME);
        rpchBaseService.addColumn(SOME_TABLE_NAME, SOME_COLUMN_FAMILY_NAME, null, null, null);
        
        assertEquals(5, rpchBaseService.increment(
            SOME_TABLE_NAME, SOME_ROW_NAME, SOME_COLUMN_FAMILY_NAME, SOME_COLUMN_QUALIFIER, 5, false));
        assertEquals(3, rpchBaseService.increment(
            SOME_TABLE_NAME, SOME_ROW_NAME, SOME_COLUMN_FAMILY_NAME, SOME_COLUMN_QUALIFIER, -2, false));
        assertEquals(4, rpchBaseService.increment(
            SOME_TABLE_NAME, SOME_ROW_NAME, SOME_COLUMN_FAMILY_NAME, SOME_COLUMN_QUALIFIER, 1, false));
    }
    
    @Test
    public void testCheckOperations() {
        rpchBaseService.createTable(SOME_TABLE_NAME);
        rpchBaseService.addColumn(SOME_TABLE_NAME, "f1", null, null, null);
        rpchBaseService.addColumn(SOME_TABLE_NAME, "f2", null, null, null);
        
        assertFalse(rpchBaseService.checkAndDelete(
            SOME_TABLE_NAME, "r1", "f1", "q1", "v1", "f2", "q2", null, true, null));
        assertFalse(rpchBaseService.checkAndPut(
            SOME_TABLE_NAME, "r1", "f1", "q1", "v1", "f2", "q2", null, "v2", false, null));
        
        rpchBaseService.put(SOME_TABLE_NAME, "r1", "f1", "q1", null, "v1", false, null);
        assertTrue(rpchBaseService.checkAndPut(
            SOME_TABLE_NAME, "r1", "f1", "q1", "v1", "f2", "q2", null, "v2", false, null));
        
        Result r2 = rpchBaseService.get(SOME_TABLE_NAME, "r1", null, null);
        assertEquals("v2", new String(
                r2.getColumnLatest("f2".getBytes(UTF8), "q2".getBytes(UTF8)).getValue(), UTF8));
        
        assertTrue(rpchBaseService.get(
                SOME_TABLE_NAME, "r1", null, null).containsColumn("f2".getBytes(UTF8), "q2".getBytes(UTF8)));
        assertFalse(rpchBaseService.checkAndDelete(
                SOME_TABLE_NAME, "r1", "f2", "q1", "v1", "f2", "q2", null, true, null));
        assertTrue(rpchBaseService.get(
                SOME_TABLE_NAME, "r1", null, null).containsColumn("f2".getBytes(UTF8), "q2".getBytes(UTF8)));
        assertFalse(rpchBaseService.checkAndDelete(
                SOME_TABLE_NAME, "r1", "f1", "q2", "v1", "f2", "q2", null, true, null));
        assertTrue(rpchBaseService.get(
                SOME_TABLE_NAME, "r1", null, null).containsColumn("f2".getBytes(UTF8), "q2".getBytes(UTF8)));
        assertFalse(rpchBaseService.checkAndDelete(
                SOME_TABLE_NAME, "r1", "f1", "q1", "v2", "f2", "q2", null, true, null));
        assertTrue(rpchBaseService.get(
                SOME_TABLE_NAME, "r1", null, null).containsColumn("f2".getBytes(UTF8), "q2".getBytes(UTF8)));
        assertTrue(rpchBaseService.checkAndDelete(
                SOME_TABLE_NAME, "r1", "f1", "q1", "v1", "f2", "q2", null, true, null));
        assertFalse(rpchBaseService.get(
                SOME_TABLE_NAME, "r1", null, null).containsColumn("f2".getBytes(UTF8), "q2".getBytes(UTF8)));
    }
    
    @Test
    public void testLock() {
        rpchBaseService.createTable(SOME_TABLE_NAME);
        rpchBaseService.addColumn(SOME_TABLE_NAME, "f1", null, null, null);
        rpchBaseService.put(SOME_TABLE_NAME, "r1", "f1", "q1", null, "v1", false, null);
        rpchBaseService.put(SOME_TABLE_NAME, "r1", "f1", "q2", null, "v2", false, null);
        
        //lock
        RowLock lock = rpchBaseService.lock(SOME_TABLE_NAME, "r1");
        assertNotNull(lock);
        
        //locked for 30s
        try {
            rpchBaseService.put(SOME_TABLE_NAME, "r1", "f1", "q1", null, "v2", false, null);
            fail();
        } catch (HBaseServiceException e) {
            assertEquals("v1",
                new String(rpchBaseService.get(SOME_TABLE_NAME, "r1", null, null)
                    .getColumnLatest("f1".getBytes(UTF8), "q1".getBytes(UTF8)).getValue(), UTF8));
        }
        
        //put with lock
        rpchBaseService.put(SOME_TABLE_NAME, "r1", "f1", "q1", null, "v3", false, lock);
        assertEquals("v3",
            new String(rpchBaseService.get(SOME_TABLE_NAME, "r1", null, null)
                .getColumnLatest("f1".getBytes(UTF8), "q1".getBytes(UTF8)).getValue(), UTF8));
        
        //delete locked row
        try {
            rpchBaseService.delete(SOME_TABLE_NAME, "r1", "f1", "q2", null, true, null);
            fail();
        } catch (HBaseServiceException e) {
            assertEquals("v2",
                new String(rpchBaseService.get(SOME_TABLE_NAME, "r1", null, null)
                    .getColumnLatest("f1".getBytes(UTF8), "q2".getBytes(UTF8)).getValue(), UTF8));
        }
        
        //delete with lock
        rpchBaseService.delete(SOME_TABLE_NAME, "r1", "f1", "q2", null, true, lock);
        assertFalse(rpchBaseService.get(SOME_TABLE_NAME, "r1", null, null)
            .containsColumn("f1".getBytes(UTF8), "q2".getBytes(UTF8)));
        
        //unlock
        rpchBaseService.unlock(SOME_TABLE_NAME, lock);
        
        //unlocked put
        rpchBaseService.put(SOME_TABLE_NAME, "r1", "f1", "q1", null, "v2", false, null);
        assertEquals("v2",
            new String(rpchBaseService.get(SOME_TABLE_NAME, "r1", null, null)
                .getColumnLatest("f1".getBytes(UTF8), "q1".getBytes(UTF8)).getValue(), UTF8));
    }

    private <T>int count(Iterable<T> iterator) {
        int aux = 0;
        for (T t: iterator) {
            aux++;
        }
        return aux;
    }
}
