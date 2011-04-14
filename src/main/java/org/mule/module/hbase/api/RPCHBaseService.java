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
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableFactory;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTableInterfaceFactory;
import org.apache.hadoop.hbase.client.Result;

/**
 * {@link HBaseService} that uses the official RPC client to connect with the database.
 * <br><br>
 * <strong>Important</strong>
 * It requires HBase >= 0.90.3-SNAPSHOT because of this two issues:
 * <ul>
 *  <li><a href="https://issues.apache.org/jira/browse/HBASE-3712">
 *      https://issues.apache.org/jira/browse/HBASE-3712</a></li>
 *  <li><a href="https://issues.apache.org/jira/browse/HBASE-3734">
 *      https://issues.apache.org/jira/browse/HBASE-3734</a></li>
 * </ul>
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
        HBaseAdmin hBaseAdmin = null;
        try {
            hBaseAdmin = createHBaseAdmin();
            boolean ret = hBaseAdmin.isMasterRunning();
            return ret;
        } catch (HBaseServiceException e) {
            return false;
        } catch (MasterNotRunningException e) {
            return false;
        } catch (ZooKeeperConnectionException e) {
            return false;
        } finally {
            destroyHBaseAdmin(hBaseAdmin);
        }
    }

    /** @see org.mule.module.hbase.api.HBaseService#createTable(java.lang.String) */
    public void createTable(String name) {
        final HBaseAdmin hBaseAdmin = createHBaseAdmin();
        try {
            hBaseAdmin.createTable(new HTableDescriptor(name));
            doFlush(hBaseAdmin, name);
        } catch (IOException e) {
            throw new HBaseServiceException(e);
        } finally {
            destroyHBaseAdmin(hBaseAdmin);
        }
    }

    /** @see org.mule.module.hbase.api.HBaseService#existsTable(java.lang.String) */
    public boolean existsTable(String name) {
        HBaseAdmin hBaseAdmin = null;
        try {
            hBaseAdmin = createHBaseAdmin();
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
        } finally {
            destroyHBaseAdmin(hBaseAdmin);
        }
    }

    /** @see org.mule.module.hbase.api.HBaseService#deleteTable(java.lang.String) */
    public void deleteTable(String name) {
        HBaseAdmin hBaseAdmin = null;
        try {
            hBaseAdmin = createHBaseAdmin();
            hBaseAdmin.disableTable(name);
            hBaseAdmin.deleteTable(name);
            doFlush(hBaseAdmin, name);
        } catch (IOException e) {
            throw new HBaseServiceException(e);
        } finally {
            destroyHBaseAdmin(hBaseAdmin);
        }
    }
    
    /** @see org.mule.module.hbase.api.HBaseService#isDisabledTable(java.lang.String) */
    public boolean isDisabledTable(String name) {
        HBaseAdmin hBaseAdmin = null;
        try {
            hBaseAdmin = createHBaseAdmin();
            return hBaseAdmin.isTableDisabled(name);
        } catch (IOException e) {
            throw new HBaseServiceException(e);
        } finally {
            destroyHBaseAdmin(hBaseAdmin);
        }
    }
    
    /** @see org.mule.module.hbase.api.HBaseService#enableTable(java.lang.String) */
    public void enableTable(String name) {
        HBaseAdmin hBaseAdmin = null;
        try {
            hBaseAdmin = createHBaseAdmin();
            hBaseAdmin.enableTable(name);
        } catch (IOException e) {
            throw new HBaseServiceException(e);
        } finally {
            destroyHBaseAdmin(hBaseAdmin);
        }
    }
    
    /** @see org.mule.module.hbase.api.HBaseService#disabeTable(java.lang.String) */
    public void disabeTable(String name) {
        HBaseAdmin hBaseAdmin = null;
        try {
            hBaseAdmin = createHBaseAdmin();
            hBaseAdmin.disableTable(name);
        } catch (IOException e) {
            throw new HBaseServiceException(e);
        } finally {
            destroyHBaseAdmin(hBaseAdmin);
        }
    }
    
    /** @see org.mule.module.hbase.api.HBaseService#addColumn(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Boolean, java.lang.Integer) */
    public void addColumn(String name, String someColumnFamilyName, Integer maxVersions, Boolean inMemory, Integer scope) {
        final HColumnDescriptor descriptor = new HColumnDescriptor(someColumnFamilyName);
        if (maxVersions != null) {
            descriptor.setMaxVersions(maxVersions);
        }
        if (inMemory != null) {
            descriptor.setInMemory(inMemory);
        }
        if (scope != null) {
            descriptor.setScope(scope);
        }
        HBaseAdmin hBaseAdmin = null;
        try {
            hBaseAdmin = createHBaseAdmin();
            hBaseAdmin.disableTable(name);
            hBaseAdmin.addColumn(name, descriptor);
            hBaseAdmin.enableTable(name);
            doFlush(hBaseAdmin, name);
        } catch (IOException e) {
            throw new HBaseServiceException(e);
        } finally {
            destroyHBaseAdmin(hBaseAdmin);
        }
    }
    
    /** @see org.mule.module.hbase.api.HBaseService#existsColumn(java.lang.String, java.lang.String) */
    public boolean existsColumn(String tableName, String columnFamilyName) {
        HTableInterface hTable = null;
        try {
            hTable = createHTable(tableName);
            final boolean ret = hTable.getTableDescriptor().getFamily(columnFamilyName.getBytes(UTF8)) != null;
            return ret;
        } catch (IOException e) {
            throw new HBaseServiceException(e);
        } finally {
            if (hTable != null) {
                try {
                    hTable.close();
                } catch (IOException e) {
                    throw new HBaseServiceException(e);
                }
            }
        }
    }
    
    /** @see org.mule.module.hbase.api.HBaseService#deleteColumn(java.lang.String, java.lang.String) */
    public void deleteColumn(String tableName, String columnFamilyName) {
        HBaseAdmin hBaseAdmin = null;
        try {
            hBaseAdmin = createHBaseAdmin();
            hBaseAdmin.disableTable(tableName);
            hBaseAdmin.deleteColumn(tableName, columnFamilyName);
            hBaseAdmin.enableTable(tableName);
            doFlush(hBaseAdmin, tableName);
        } catch (IOException e) {
            throw new HBaseServiceException(e);
        } finally {
            destroyHBaseAdmin(hBaseAdmin);
        }
    }
    
    //------------ Row Operations
    /** @see org.mule.module.hbase.api.HBaseService#get(java.lang.String, java.lang.String) */
    public Result get(String tableName, String row) {
        HTableInterface hTable = null;
        Result result = null;
        try {
            hTable = createHTable(tableName);
            result = doGet(hTable, row);
        } finally {
            if (hTable != null) {
                try {
                    hTable.close();
                } catch (IOException e) {
                    throw new HBaseServiceException(e);
                }
            }
        }
        return result;
    }
    
    //------------ Configuration
    /** @see org.mule.module.hbase.api.HBaseService#addProperties(java.util.Map) */
    public void addProperties(Map<String, String> properties) {
        for (Entry<String, String> entry : properties.entrySet()) {
            configuration.set(entry.getKey(), entry.getValue());
        }
    }
    
    //------------ Private
    
    private void doFlush(HBaseAdmin hBaseAdmin, String name) {
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

    private HTableInterface createHTable(String tableName) {
        return hTableInterfaceFactory.createHTableInterface(configuration, tableName.getBytes(UTF8));
    }

    /**
     * Returns a new instance of {@link HBaseAdmin}. 
     * Clients should call {@link RPCHBaseService#destroyHBaseAdmin(HBaseAdmin)}. 
     */
    private HBaseAdmin createHBaseAdmin() {
        try {
            return new HBaseAdmin(configuration);
        } catch (MasterNotRunningException e) {
            throw new HBaseServiceException(e);
        } catch (ZooKeeperConnectionException e) {
            throw new HBaseServiceException(e);
        }
    }

    /** Release any resources allocated by {@link HBaseAdmin} */
    private void destroyHBaseAdmin(final HBaseAdmin hBaseAdmin) {
        if (hBaseAdmin != null) {
            HConnectionManager.deleteConnection(hBaseAdmin.getConfiguration(), true);
        }
    }

}
