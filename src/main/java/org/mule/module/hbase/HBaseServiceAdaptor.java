/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.module.hbase;

import org.mule.module.hbase.api.HBaseService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Proxy factory for creating {@link HBaseService}s that log operations
 * 
 * @author flbulgarelli
 */
public final class HBaseServiceAdaptor
{
    private static final Logger logger = LoggerFactory.getLogger(HbaseCloudConnector.class);

    private HBaseServiceAdaptor()
    {
    }

    public static HBaseService adapt(final HBaseService service)
    {
        return (HBaseService) Proxy.newProxyInstance(HBaseServiceAdaptor.class.getClassLoader(),
            new Class[]{HBaseService.class}, new InvocationHandler()
            {
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
                {
                    try
                    {
                        if (logger.isDebugEnabled())
                        {
                            logger.debug("Entering into {} with args {}", method.getName(), args);
                        }
                        Object result = method.invoke(service, args);
                        if (logger.isDebugEnabled())
                        {
                            logger.debug("Returning from {} with value {}", method.getName(), result);
                        }
                        return result;
                    }
                    catch (InvocationTargetException e)
                    {
                        logger.warn("Throwing {} at {}", e.getCause(), method.getName());
                        throw e.getCause();
                    }
                }
            });
    }
}
