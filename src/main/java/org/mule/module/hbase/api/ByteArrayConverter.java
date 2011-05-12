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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;

import org.apache.commons.lang.UnhandledException;

/**
 * Object to byte array converter.
 * 
 * @author flbulgarellli
 */
public final class ByteArrayConverter
{
    /**
     * The Charset used for converting strings to byte array
     */
    private final Charset conversionCharset;

    public ByteArrayConverter(Charset convertionCharset)
    {
        this.conversionCharset = convertionCharset;
    }

    /**
     * Converts the given object into a byte array. If the object is a byte array, is
     * is returned as is. If the object is an string, it is written in the provided
     * conversionCharset given by constructor. Any other serializable object is
     * converted into a byte array using serialization.
     * 
     * @param o
     * @return a byte array representation of the given object
     */
    public byte[] toByteArray(Object o)
    {
        if (o instanceof byte[])
        {
            return (byte[]) o;
        }
        if (o instanceof String)
        {
            return ((String) o).getBytes(conversionCharset);
        }
        if (o instanceof Serializable)
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try
            {
                new ObjectOutputStream(out).writeObject(o);
            }
            catch (IOException e)
            {
                throw new UnhandledException(e);
            }
            return out.toByteArray();
        }
        throw new IllegalArgumentException("Object " + o + " can not be converted to byte array");
    }

}
