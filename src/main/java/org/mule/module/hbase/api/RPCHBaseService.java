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

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableFactory;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTableInterfaceFactory;
import org.apache.hadoop.hbase.client.Result;

/**
 * {@link HBaseService} that uses the official RPC client to connect with the database
 * 
 * @author Pablo Martin Grigolatto
 * @since Apr 11, 2011
 */
public class RPCHBaseService implements HBaseService {

    private static final Charset UTF8 = Charset.forName("utf-8");
    private HTableInterfaceFactory hTableInterfaceFactory;
    private Configuration configuration;

    public RPCHBaseService() {
        hTableInterfaceFactory = new HTableFactory();
        configuration = HBaseConfiguration.create();
    }
    
    //------------ Admin Operations
    /** @see org.mule.module.hbase.api.HBaseService#alive() */
    public boolean alive() {
        try {
            final HBaseAdmin hBaseAdmin = createHBaseAdmin();
            return hBaseAdmin.isMasterRunning();
        } catch (HBaseServiceException e) {
            return false;
        } catch (MasterNotRunningException e) {
            return false;
        } catch (ZooKeeperConnectionException e) {
            return false;
        }
    }

    /** @see org.mule.module.hbase.api.HBaseService#createTable(java.lang.String) */
    public void createTable(String name) {
        final HBaseAdmin hBaseAdmin = createHBaseAdmin();
        final HTableDescriptor descriptor = new HTableDescriptor(name);
        try {
            hBaseAdmin.createTable(descriptor);
        } catch (IOException e) {
            throw new HBaseServiceException(e);
        } 
        doFlush(name, hBaseAdmin);
    }

    private HBaseAdmin createHBaseAdmin() {
        try {
            return new HBaseAdmin(configuration);
        } catch (MasterNotRunningException e) {
            throw new HBaseServiceException(e);
        } catch (ZooKeeperConnectionException e) {
            throw new HBaseServiceException(e);
        }
    }
    
    /** @see org.mule.module.hbase.api.HBaseService#existsTable(java.lang.String) */
    public boolean existsTable(String name) {
        try {
            HBaseAdmin hBaseAdmin = createHBaseAdmin();
            HTableDescriptor descriptor = hBaseAdmin.getTableDescriptor(name.getBytes(UTF8));
            return descriptor != null;
        } catch (MasterNotRunningException e) {
            throw new HBaseServiceException(e);
        } catch (ZooKeeperConnectionException e) {
            throw new HBaseServiceException(e);
        } catch (TableNotFoundException e) {
            return false;
        } catch (IOException e) {
            throw new HBaseServiceException(e);
        }
    }

    /** @see org.mule.module.hbase.api.HBaseService#deleteTable(java.lang.String) */
    public void deleteTable(String name) {
        final HBaseAdmin hBaseAdmin = createHBaseAdmin();
        try {
            hBaseAdmin.disableTable(name);
            hBaseAdmin.deleteTable(name);
        } catch (IOException e) {
            throw new HBaseServiceException(e);
        }
        doFlush(name, hBaseAdmin);
    }
    
    /** @see org.mule.module.hbase.api.HBaseService#isDisabledTable(java.lang.String) */
    public boolean isDisabledTable(String name) {
        try {
            return createHBaseAdmin().isTableDisabled(name);
            //return HTable.isTableEnabled(name);
        } catch (IOException e) {
            throw new HBaseServiceException(e);
        }
    }
    
    /** @see org.mule.module.hbase.api.HBaseService#enableTable(java.lang.String) */
    public void enableTable(String name) {
        try {
            createHBaseAdmin().enableTable(name);
        } catch (IOException e) {
            throw new HBaseServiceException(e);
        }        
    }
    
    /** @see org.mule.module.hbase.api.HBaseService#disabeTable(java.lang.String) */
    public void disabeTable(String name) {
        try {
            createHBaseAdmin().disableTable(name);
        } catch (IOException e) {
            throw new HBaseServiceException(e);
        }
    }
    
    //------------ Row Operations
    /** @see org.mule.module.hbase.api.HBaseService#get(java.lang.String, java.lang.String) */
    public Result get(String tableName, String row) {
        return doGet(getHTable(tableName), row);
    }
    
    //------------ Configuration
    /** @see org.mule.module.hbase.api.HBaseService#addProperties(java.util.Map) */
    public void addProperties(Map<String, String> properties) {
        for (Entry<String, String> entry : properties.entrySet()) {
            configuration.set(entry.getKey(), entry.getValue());
        }
    }
    
    //------------ Private
    
    private void doFlush(String name, HBaseAdmin hBaseAdmin) {
        try {
            hBaseAdmin.flush(name);
        } catch (IOException e) {
            throw new HBaseServiceException(e);
        } catch (InterruptedException e) {
            throw new HBaseServiceException(e);
        }
    }

    private Result doGet(final HTableInterface hTableInterface, final String rowKey) {
        try {
            return hTableInterface.get(createGet(rowKey));
        } catch (IOException e) {
            throw new HBaseServiceException(e);
        }
    }

    private Get createGet(String rowKey) {
        Get get = new Get(rowKey.getBytes(UTF8));
        return get;
    }

    private HTableInterface getHTable(String tableName) {
        return hTableInterfaceFactory.createHTableInterface(configuration, tableName.getBytes(UTF8));
    }

}
