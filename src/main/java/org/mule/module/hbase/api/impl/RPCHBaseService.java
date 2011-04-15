/**
 * Mule HBase Cloud Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.module.hbase.api.impl;

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
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableFactory;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTableInterfaceFactory;
import org.apache.hadoop.hbase.client.Result;
import org.mule.module.hbase.api.HBaseService;
import org.mule.module.hbase.api.HBaseServiceException;

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
        try {
            return (Boolean) doWithHBaseAdmin(new AdminCallback<Boolean>() {
                public Boolean doWithHBaseAdmin(HBaseAdmin hBaseAdmin) {
                    try {
                        return hBaseAdmin.isMasterRunning();
                    } catch (MasterNotRunningException e) {
                        return false;
                    } catch (ZooKeeperConnectionException e) {
                        return false;
                    } catch (HBaseServiceException e) {
                        return false;
                    }
                }
            });
        } catch (Exception e) {
            return false;
        }
    }

    /** @see org.mule.module.hbase.api.HBaseService#createTable(java.lang.String) */
    public void createTable(final String name) {
        doWithHBaseAdmin(new AdminCallback<Void>() {
            public Void doWithHBaseAdmin(HBaseAdmin hBaseAdmin) {
                try {
                    hBaseAdmin.createTable(new HTableDescriptor(name));
                    doFlush(hBaseAdmin, name);
                    return null;
                } catch (IOException e) {
                    throw new HBaseServiceException(e);
                }
            }
        });
    }

    /** @see org.mule.module.hbase.api.HBaseService#existsTable(java.lang.String) */
    public boolean existsTable(final String name) {
        return (Boolean) doWithHBaseAdmin(new AdminCallback<Boolean>() {
            public Boolean doWithHBaseAdmin(HBaseAdmin hBaseAdmin) {
                try {
                    return hBaseAdmin.getTableDescriptor(name.getBytes(UTF8)) != null;
                } catch (TableNotFoundException e) {
                    return false;
                } catch (IOException e) {
                    throw new HBaseServiceException(e);
                }
            }
        });
    }

    /** @see org.mule.module.hbase.api.HBaseService#deleteTable(java.lang.String) */
    public void deleteTable(final String name) {
        doWithHBaseAdmin(new AdminCallback<Void>() {
            public Void doWithHBaseAdmin(HBaseAdmin hBaseAdmin) {
                try {
                    hBaseAdmin.disableTable(name);
                    hBaseAdmin.deleteTable(name);
                    doFlush(hBaseAdmin, name);
                    return null;
                } catch (IOException e) {
                    throw new HBaseServiceException(e);
                }
            }
        });
    }
    
    /** @see org.mule.module.hbase.api.HBaseService#isDisabledTable(java.lang.String) */
    public boolean isDisabledTable(final String name) {
        return (Boolean) doWithHBaseAdmin(new AdminCallback<Boolean>() {
            public Boolean doWithHBaseAdmin(HBaseAdmin hBaseAdmin) {
                try {
                    return hBaseAdmin.isTableDisabled(name);
                } catch (IOException e) {
                    throw new HBaseServiceException(e);
                }
            }
        });
    }
    
    /** @see org.mule.module.hbase.api.HBaseService#enableTable(java.lang.String) */
    public void enableTable(final String name) {
        doWithHBaseAdmin(new AdminCallback<Void>() {
            public Void doWithHBaseAdmin(HBaseAdmin hBaseAdmin) {
                try {
                    hBaseAdmin.enableTable(name);
                    return null;
                } catch (IOException e) {
                    throw new HBaseServiceException(e);
                }
            }
        });
    }
    
    /** @see org.mule.module.hbase.api.HBaseService#disabeTable(java.lang.String) */
    public void disabeTable(final String name) {
        doWithHBaseAdmin(new AdminCallback<Void>() {
            public Void doWithHBaseAdmin(HBaseAdmin hBaseAdmin) {
                try {
                    hBaseAdmin.disableTable(name);
                    return null;
                } catch (IOException e) {
                    throw new HBaseServiceException(e);
                }
            }
        });
    }
    
    /** @see org.mule.module.hbase.api.HBaseService#addColumn(
     *  java.lang.String, java.lang.String, java.lang.Integer, java.lang.Boolean, java.lang.Integer) */
    public void addColumn(final String name, final String someColumnFamilyName, 
            final Integer maxVersions, final Boolean inMemory, final Integer scope) {
        doWithHBaseAdmin(new AdminCallback<Void>() {
            public Void doWithHBaseAdmin(HBaseAdmin hBaseAdmin) {
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
                try {
                    hBaseAdmin.disableTable(name);
                    hBaseAdmin.addColumn(name, descriptor);
                    hBaseAdmin.enableTable(name);
                    doFlush(hBaseAdmin, name);
                    return null;
                } catch (IOException e) {
                    throw new HBaseServiceException(e);
                }
            }
        });
    }
    
    /** @see org.mule.module.hbase.api.HBaseService#existsColumn(java.lang.String, java.lang.String) */
    public boolean existsColumn(String tableName, final String columnFamilyName) {
        return (Boolean) doWithHTable(tableName, new TableCallback<Boolean>() {
            public Boolean doWithHBaseAdmin(HTableInterface hTable) {
                try {
                    return hTable.getTableDescriptor().getFamily(columnFamilyName.getBytes(UTF8)) != null;
                } catch (IOException e) {
                    throw new HBaseServiceException(e);
                }
            }
        });
    }
    
    /** @see org.mule.module.hbase.api.HBaseService#deleteColumn(java.lang.String, java.lang.String) */
    public void deleteColumn(final String tableName, final String columnFamilyName) {
        doWithHBaseAdmin(new AdminCallback<Void>() {
            public Void doWithHBaseAdmin(HBaseAdmin hBaseAdmin) {
                try {
                    hBaseAdmin.disableTable(tableName);
                    hBaseAdmin.deleteColumn(tableName, columnFamilyName);
                    hBaseAdmin.enableTable(tableName);
                    doFlush(hBaseAdmin, tableName);
                    return null;
                } catch (IOException e) {
                    throw new HBaseServiceException(e);
                }
            }
        });
    }
    
    //------------ Row Operations
    /** @see org.mule.module.hbase.api.HBaseService#get(java.lang.String, java.lang.String) */
    public Result get(String tableName, final String row) {
        return (Result) doWithHTable(tableName, new TableCallback<Result>() {
            public Result doWithHBaseAdmin(HTableInterface hTable) {
                return doGet(hTable, row);
            }
        });
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

    /** Retain and release the {@link HBaseAdmin} */
    private Object doWithHBaseAdmin(AdminCallback<?> callback) {
        HBaseAdmin hBaseAdmin = null;
        try {
            hBaseAdmin = createHBaseAdmin();
            return callback.doWithHBaseAdmin(hBaseAdmin);
        } finally {
            destroyHBaseAdmin(hBaseAdmin);
        }
    }
    
    /** Retain and release the {@link HTable} */
    private Object doWithHTable(final String tableName, final TableCallback<?> callback) {
        HTableInterface hTable = null;
        try {
            hTable = createHTable(tableName);
            return callback.doWithHBaseAdmin(hTable);
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

    /** Callback for using the {@link HBaseAdmin} without worry about releasing it */
    interface AdminCallback<T> {
        T doWithHBaseAdmin(final HBaseAdmin hBaseAdmin);
    }
    
    /** Callback for using the {@link HTableInterface} without worry about releasing it */
    interface TableCallback<T> {
        T doWithHBaseAdmin(final HTableInterface hTable);
    }
}
