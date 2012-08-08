/**
 * Copyright (c) MuleSoft, Inc. All rights reserved. http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.md file.
 */

package org.mule.module.hbase.api;

import org.apache.hadoop.hbase.io.hfile.Compression.Algorithm;

/**
 * Compression types used by HBase
 * 
 * @author flbulgarelli
 */
public enum CompressionType
{
    LZO(Algorithm.LZO), GZ(Algorithm.GZ), NONE(Algorithm.NONE);

    private final Algorithm algorithm;

    private CompressionType(Algorithm algorithm)
    {
        this.algorithm = algorithm;
    }

    public Algorithm getAlgorithm()
    {
        return algorithm;
    }

}
