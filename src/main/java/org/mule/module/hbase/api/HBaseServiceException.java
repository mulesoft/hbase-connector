/*
 * Copyright (c) 2011 Zauber S.A.  -- All rights reserved
 */
package org.mule.module.hbase.api;


/**
 * Wrapper for every checked exception throw by the HBase RPC client 
 * 
 * @author Pablo Martin Grigolatto
 * @since Apr 13, 2011
 */
public class HBaseServiceException extends RuntimeException {
    
    private static final long serialVersionUID = -5576324163271302491L;
    
    /** Creates the HBaseServiceException. */
    public HBaseServiceException(Throwable e) {
        super(e);
    }
    
}
