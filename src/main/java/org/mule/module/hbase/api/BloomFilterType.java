/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */


package org.mule.module.hbase.api;

import org.apache.hadoop.hbase.regionserver.StoreFile.BloomType;

/**
 * Bloom Filter Types used by hbase
 * @author flbulgarelli
 */
public enum BloomFilterType
{
    NONE(BloomType.NONE), ROW(BloomType.ROW), ROWCOL(BloomType.ROWCOL);

    private final BloomType bloomType;

    private BloomFilterType(BloomType bloomType)
    {
        this.bloomType = bloomType;
    }

    public BloomType getBloomType()
    {
        return bloomType;
    }
}
