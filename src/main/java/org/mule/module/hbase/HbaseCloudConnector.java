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
import org.mule.api.lifecycle.InitialisationException;
import org.mule.module.hbase.api.HBaseService;
import org.mule.module.hbase.api.RPCHBaseService;
import org.mule.tools.cloudconnect.annotations.Connector;
import org.mule.tools.cloudconnect.annotations.Operation;
import org.mule.tools.cloudconnect.annotations.Parameter;
import org.mule.tools.cloudconnect.annotations.Property;

@Connector(namespacePrefix="hbase")
public class HbaseCloudConnector
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
    public void deleteTable(@Parameter(optional = false) String name) {
        facade.deleteTable(name);
    }
    
    //------------ Row Operations
    
    @Operation
    public Result get(
            @Parameter(optional = false) final String tableName, 
            @Parameter(optional = false) final String row) {
        return facade.get(tableName, row);
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
