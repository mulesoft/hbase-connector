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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.commons.lang.math.LongRange;
import org.junit.Test;

/**
 * Test for {@link ByteArrayConverter}
 * 
 * @author flbulgarelli
 */
public class ByteArrayConverterUnitTest
{
    private ByteArrayConverter c = new ByteArrayConverter(Charset.forName("utf-8"));

    @Test
    public void testToByteArrayByteArray()
    {
        byte[] ba = new byte[]{10, 20, 30};
        assertSame(ba, c.toByteArray(ba));
    }

    @Test
    public void testToByteArrayString() throws UnsupportedEncodingException
    {
        assertArrayEquals("hello".getBytes("utf-8"), c.toByteArray("hello"));
    }

    @Test
    public void testToByteArraySerializable()
    {
        assertNotNull(c.toByteArray(new LongRange(10, 20)));
    }

}
