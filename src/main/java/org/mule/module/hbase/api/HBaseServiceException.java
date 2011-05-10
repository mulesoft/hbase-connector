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

/**
 * Wrapper for every checked exception throw by the HBase RPC client
 * 
 * @author Pablo Martin Grigolatto
 * @since Apr 13, 2011
 */
public class HBaseServiceException extends RuntimeException
{

    private static final long serialVersionUID = -5576324163271302491L;

    /** Creates the HBaseServiceException. */
    public HBaseServiceException(Throwable e)
    {
        super(e);
    }

}
