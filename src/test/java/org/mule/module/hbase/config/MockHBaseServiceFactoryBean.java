/*
 * Copyright (c) 2011 Zauber S.A.  -- All rights reserved
 */
package org.mule.module.hbase.config;

import static org.mockito.Mockito.*;

import org.mule.module.hbase.api.HBaseService;

/**
 * Creates mock services for testing
 * 
 * @author Pablo Martin Grigolatto
 * @since Apr 20, 2011
 */
public class MockHBaseServiceFactoryBean {

    public static HBaseService getObject() throws Exception {
        return mock(HBaseService.class);
    }

}
