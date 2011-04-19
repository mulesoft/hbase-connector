/**
 * This file was automatically generated by the Mule Cloud Connector Development Kit
 */
package org.mule.module.hbase.config;

import org.apache.hadoop.hbase.client.Result;
import org.mule.api.MuleEvent;
import org.mule.construct.SimpleFlowConstruct;
import org.mule.tck.FunctionalTestCase;

public class HbaseNamespaceHandlerTestCase extends FunctionalTestCase
{
    @Override
    protected String getConfigResources()
    {
        return "hbase-namespace-config.xml";
    }

    public void testSendMessageToFlow()throws Exception
    {
//        String payload = "some-row";
//        SimpleFlowConstruct flow = lookupFlowConstruct("flowGetByKey");
//        MuleEvent event = getTestEvent(payload);
//        MuleEvent responseEvent = flow.process(event);
//        
//        Result response = responseEvent.getMessage().getPayload(Result.class);
//        assertTrue(response.isEmpty());
    }

    private SimpleFlowConstruct lookupFlowConstruct(String name)
    {
        return(SimpleFlowConstruct)muleContext.getRegistry().lookupFlowConstruct(name);
    }
}
