/**
 * Mule HBase Cloud Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mulesoft.demo.hbase;

import org.mule.api.MuleEvent;
import org.mule.api.transport.PropertyScope;
import org.mule.construct.MessageProcessor;
import org.mule.tck.FunctionalTestCase;

import java.util.Collections;

public class HBaseFunctionalTestDriver extends FunctionalTestCase
{

    @Override
    protected String getConfigResources()
    {
        return "mule-config.xml";
    }

    /**
     * Creates the resources necessary for the rest of the tests
     */
    public void testScanWeatherObservation() throws Exception
    {
      System.out.println("Return is " + lookupFlowConstruct("ScanWeatherObservations").process(getTestEvent("")));
    }

    private MessageProcessor lookupFlowConstruct(final String name)
    {
        return (MessageProcessor) muleContext.getRegistry().lookupFlowConstruct(name);
    }
}
