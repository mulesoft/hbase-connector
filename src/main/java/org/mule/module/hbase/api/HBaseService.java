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
 * TODO: Description of the class, Comments in english by default  
 * 
 * 
 * @author Pablo Martin Grigolatto
 * @since Apr 11, 2011
 */
public interface HBaseService {

    void createTable(String name);
    Result get(String tableName, String row);
    void addProperties(Map<String, String> properties);

}
