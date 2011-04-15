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
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterBase;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.io.hfile.Compression.Algorithm;
import org.apache.hadoop.hbase.regionserver.StoreFile.BloomType;
import org.apache.hadoop.hbase.util.Bytes;
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
    /** @see HBaseService#alive() */
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

    /** @see HBaseService#createTable(String) */
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

    /** @see HBaseService#existsTable(String) */
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

    /** @see HBaseService#deleteTable(String) */
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
    
    /** @see HBaseService#isDisabledTable(String) */
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
    
    /** @see HBaseService#enableTable(String) */
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
    
    /** @see HBaseService#disabeTable(String) */
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
    
    /** @see HBaseService#addColumn(
     *  String, String, Integer, Boolean, Integer) */
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
    
    /** @see HBaseService#existsColumn(String, String) */
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
    
    /** @see HBaseService#modifyColumn(String, String, Integer, Integer, String, String, Boolean, Integer, Boolean, String, Integer, Map) */
    public void modifyColumn(final String tableName, final String columnFamilyName,
            final Integer maxVersions, final Integer blocksize, final String compressionType,
            final String compactionCompressionType, final Boolean inMemory,
            final Integer timeToLive, final Boolean blockCacheEnabled,
            final String bloomFilterType, final Integer replicationScope,
            final Map<String, String> values) {
        
        doWithHBaseAdmin(new AdminCallback<Void>() {
            public Void doWithHBaseAdmin(HBaseAdmin hBaseAdmin) {
                try {
                    HTableDescriptor otd = hBaseAdmin.getTableDescriptor(tableName.getBytes(UTF8));
                    HColumnDescriptor ocd = otd.getFamily(columnFamilyName.getBytes(UTF8));
                    HColumnDescriptor descriptor = new HColumnDescriptor(ocd);
                    loadPropertiesInDescriptor(descriptor, maxVersions, blocksize, compressionType, 
                            compactionCompressionType, inMemory, timeToLive, blockCacheEnabled, 
                            bloomFilterType, replicationScope, values);
                    hBaseAdmin.disableTable(tableName);
                    hBaseAdmin.modifyColumn(tableName, descriptor);
                    hBaseAdmin.enableTable(tableName);
                    doFlush(hBaseAdmin, tableName);
                    return null;
                } catch (IOException e) {
                    throw new HBaseServiceException(e);
                }
            }

            private void loadPropertiesInDescriptor(
                    HColumnDescriptor descriptor, Integer maxVersions,
                    Integer blocksize, String compressionType,
                    String compactionCompressionType, Boolean inMemory,
                    Integer timeToLive, Boolean blockCacheEnabled,
                    String bloomFilterType, Integer replicationScope,
                    Map<String, String> values) {
                if (maxVersions != null) descriptor.setMaxVersions(maxVersions);
                if (blocksize != null) descriptor.setBlocksize(blocksize);
                if (compressionType != null) descriptor.setCompressionType(Algorithm.valueOf(compressionType));
                if (compactionCompressionType != null) descriptor.setCompactionCompressionType(Algorithm.valueOf(compactionCompressionType));
                if (inMemory != null) descriptor.setInMemory(inMemory);
                if (timeToLive != null) descriptor.setTimeToLive(timeToLive);
                if (blockCacheEnabled != null) descriptor.setBlockCacheEnabled(blockCacheEnabled);
                if (bloomFilterType != null) descriptor.setBloomFilterType(BloomType.valueOf(bloomFilterType));
                if (replicationScope != null) descriptor.setScope(replicationScope);
                if (values != null) {
                    for (Entry<String, String> entry : values.entrySet()) {
                        descriptor.setValue(entry.getKey(), entry.getValue());
                    }
                }
            }
        });
    }
    
    /** @see HBaseService#deleteColumn(String, String) */
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
    /** @see HBaseService#get(String, String, Integer, Long) */
    public Result get(String tableName, final String row, final Integer maxVersions, final Long timestap) {
        return (Result) doWithHTable(tableName, new TableCallback<Result>() {
            public Result doWithHBaseAdmin(HTableInterface hTable) {
                return doGet(hTable, row, maxVersions, timestap);
            }
        });
    }
    
    /** @see HBaseService#put(String, String, String, String, Long, String) */
    public void put(String tableName, final String row, final String columnFamilyName, 
            final String columnQualifier, final Long timestamp, final String value) {
        
        doWithHTable(tableName, new TableCallback<Void>() {
            public Void doWithHBaseAdmin(HTableInterface hTable) throws Exception {
                Put put = new Put(row.getBytes(UTF8));
                if (timestamp == null) {
                    put.add(columnFamilyName.getBytes(UTF8), columnQualifier.getBytes(UTF8), value.getBytes(UTF8));
                } else {
                    put.add(columnFamilyName.getBytes(UTF8), columnQualifier.getBytes(UTF8), timestamp, value.getBytes(UTF8));
                }
                hTable.put(put);
                return null;
            }
        });
    }
    
    //------------ Configuration
    /** @see HBaseService#addProperties(Map) */
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

    private Result doGet(final HTableInterface hTableInterface, final String rowKey, Integer maxVersions, Long timestamp) {
        try {
            return hTableInterface.get(createGet(rowKey, maxVersions, timestamp));
        } catch (IOException e) {
            throw new HBaseServiceException(e);
        }
    }

    private Get createGet(String rowKey, Integer maxVersions, Long timestamp) {
        Get get = new Get(rowKey.getBytes(UTF8));
        if (maxVersions != null) {
            try {
                get.setMaxVersions(maxVersions);
            } catch (IOException e) {
                new HBaseServiceException(e);
            }
        }
        if (timestamp != null) get.setTimeStamp(timestamp);
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
        } catch (Exception e) {
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

    /** Callback for using the {@link HBaseAdmin} without worry about releasing it */
    interface AdminCallback<T> {
        T doWithHBaseAdmin(final HBaseAdmin hBaseAdmin);
    }
    
    /** Callback for using the {@link HTableInterface} without worry about releasing it */
    interface TableCallback<T> {
        T doWithHBaseAdmin(final HTableInterface hTable) throws Exception;
    }

}
