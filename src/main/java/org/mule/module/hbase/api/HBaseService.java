/**
 * Mule HBase Cloud Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.module.hbase.api;

import java.util.Map;

import org.apache.hadoop.hbase.client.Result;

/**
 * The service exposes several actions to adminitrate and use an HBase database
 * 
 * @author Pablo Martin Grigolatto
 * @since Apr 11, 2011
 */
public interface HBaseService {

    //------------ Admin Operations
    /** @return true only if the server can be reached and the master node is alive, false otherwise. */
    boolean alive();
    
    void createTable(String name);

    
    //------------ Row Operations
    Result get(String tableName, String row);
    
    
    //------------ Configuration
    /**
     * Add the properties to the main configuration. 
     * It overrides old properties if they where already added.
     */
    void addProperties(Map<String, String> properties);

}
