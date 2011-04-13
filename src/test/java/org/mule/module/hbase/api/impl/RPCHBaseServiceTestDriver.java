/*
 * Copyright (c) 2011 Zauber S.A.  -- All rights reserved
 */
package org.mule.module.hbase.api.impl;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mule.module.hbase.api.RPCHBaseService;

/**
 * Testing the {@link RPCHBaseService} implementation 
 * 
 * @author Pablo Martin Grigolatto
 * @since Apr 12, 2011
 */
public class RPCHBaseServiceTestDriver {

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
    }
    
    @Test
    public void testAlive() {
        assertTrue(rpchBaseService.alive());
    }

    @Test
    public void testNotAlive() {
        properties.put("hbase.zookeeper.property.clientPort", "5000");
        rpchBaseService.addProperties(properties);
        assertFalse(rpchBaseService.alive());
    }
    
}
