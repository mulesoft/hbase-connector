/**
 * This file was automatically generated by the Mule Cloud Connector Development Kit
 */
package org.mule.module.hbase;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Map;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.RowLock;
import org.junit.Before;
import org.junit.Test;
import org.mule.module.hbase.api.HBaseService;

public class HbaseTestCase
{
    private static final String SOME_ROW_KEY = "some-row-key";
    private static final String TABLE_NAME = "table-name";
    private static final String COLUMN_NAME = "column-name";
    private HbaseCloudConnector connector;
    private HBaseService facade;
    private RowLock lock;

    @Before
    public void before() {
        connector = new HbaseCloudConnector();
        facade = mock(HBaseService.class);
        connector.setFacade(facade);
        lock = mock(RowLock.class);
    }
    
    @Test
    public void testAlive()
    {
        when(facade.alive()).thenReturn(false);
        assertFalse(connector.alive());
        verify(facade).alive();
        
        reset(facade);
        when(facade.alive()).thenReturn(true);
        assertTrue(connector.alive());
        verify(facade).alive();
    }
    
    @Test
    public void testTableAdmin()
    {
        assertFalse(connector.existsTable(TABLE_NAME));
        verify(facade).existsTable(eq(TABLE_NAME));
        
        connector.createTable(TABLE_NAME);
        verify(facade).createTable(eq(TABLE_NAME));
        
        connector.deleteTable(TABLE_NAME);
        verify(facade).deleteTable(eq(TABLE_NAME));
        
        connector.isEnabledTable(TABLE_NAME);
        verify(facade).isDisabledTable(eq(TABLE_NAME));
        
        connector.enableTable(TABLE_NAME);
        verify(facade).enableTable(eq(TABLE_NAME));
        
        connector.disableTable(TABLE_NAME);
        verify(facade).disabeTable(eq(TABLE_NAME));
        
        connector.addColumn(TABLE_NAME, COLUMN_NAME, 3, true, 7);
        verify(facade).addColumn(eq(TABLE_NAME), eq(COLUMN_NAME), eq(3), eq(true), eq(7));
        
        connector.existsColumn(TABLE_NAME, COLUMN_NAME);
        verify(facade).existsColumn(eq(TABLE_NAME), eq(COLUMN_NAME));
        
        Map<String, String> map = mock(Map.class);
        connector.modifyColumn(TABLE_NAME, COLUMN_NAME, 5, 123, "LZO", "LZO", false, 0, true, "NONE", 12, map);
        verify(facade).modifyColumn(eq(TABLE_NAME), eq(COLUMN_NAME), 
            eq(5), eq(123), eq("LZO"), eq("LZO"), eq(false), eq(0), eq(true), eq("NONE"), eq(12), eq(map));
        
        connector.deleteColumn(TABLE_NAME, COLUMN_NAME);
        verify(facade).deleteColumn(eq(TABLE_NAME), eq(COLUMN_NAME));
        
        
        connector.delete(TABLE_NAME, SOME_ROW_KEY, "family", "qualifier", 123L, false, lock);
        verify(facade).delete(eq(TABLE_NAME), eq(SOME_ROW_KEY), eq("family"), eq("qualifier"), eq(123L), eq(false), eq(lock));
        
        connector.scan(TABLE_NAME, "family", "qualifier", 123L, 456L, 2, 3, true, 2, false, "row20", "row30");
        verify(facade).scan(eq(TABLE_NAME), eq("family"), eq("qualifier"), eq(123L), 
            eq(456L), eq(2), eq(3), eq(true), eq(2), eq(false), eq("row20"), eq("row30"));
        
        connector.increment(TABLE_NAME, SOME_ROW_KEY, "f1", "q", 3L, true);
        verify(facade).increment(eq(TABLE_NAME), eq(SOME_ROW_KEY), eq("f1"), eq("q"), eq(3L), eq(true));
        
        assertFalse(connector.checkAndPut(
                TABLE_NAME, SOME_ROW_KEY, "f1", "q1", "v1", "f2", "q2", 123L, "v2", true, lock));
        verify(facade).checkAndPut(
                eq(TABLE_NAME), eq(SOME_ROW_KEY), 
                eq("f1"), eq("q1"), eq("v1"), 
                eq("f2"), eq("q2"), 
                eq(123L), eq("v2"), eq(true), eq(lock));
        
        assertFalse(connector.checkAndDelete(
                TABLE_NAME, SOME_ROW_KEY, "f1", "q1", "v1", "f2", "q2", 123L, false, lock));
        verify(facade).checkAndDelete(
                eq(TABLE_NAME), eq(SOME_ROW_KEY), 
                eq("f1"), eq("q1"), eq("v1"), 
                eq("f2"), eq("q2"), eq(123L), eq(false), eq(lock));
    }
    
    @Test
    public void testGetByRow()
    {
        Result mockResult = mock(Result.class);
        when(mockResult.isEmpty()).thenReturn(false);
        when(facade.get(eq(TABLE_NAME), eq(SOME_ROW_KEY), anyInt(), anyLong())).thenReturn(mockResult);
        
        Result result = connector.get(TABLE_NAME, SOME_ROW_KEY, 3, 12345L);
        assertFalse(result.isEmpty());
        
        connector.put(TABLE_NAME, SOME_ROW_KEY, COLUMN_NAME, "q", 123L, "value", true, lock);
        verify(facade).put(
                eq(TABLE_NAME), eq(SOME_ROW_KEY), eq(COLUMN_NAME), eq("q"), 
                eq(123L), eq("value"), eq(true), eq(lock));
    }
}
