package com.samknows.metric.be.routes;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;


public class MetricRouterTest extends CamelTestSupport {

     @Test
    public void testMockBodyContent() throws Exception {

        getMockEndpoint("mock:result").expectedBodiesReceivedInAnyOrder("[\n" +
                "  {\n" +
                "    \"metricValue\": 12693166.98,\n" +
                "    \"dtime\": \"2018-01-29\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"metricValue\": 12668239.57,\n" +
                "    \"dtime\": \"2018-01-30\"\n" +
                "  }\n" +
                "]");

        template.sendBody("direct:start", "[\n" +
                "  {\n" +
                "    \"metricValue\": 12693166.98,\n" +
                "    \"dtime\": \"2018-01-29\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"metricValue\": 12668239.57,\n" +
                "    \"dtime\": \"2018-01-30\"\n" +
                "  }\n" +
                "]");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testMockBodyContentDifferent() throws Exception {

        getMockEndpoint("mock:result").expectedBodiesReceivedInAnyOrder("[\n" +
                "  {\n" +
                "    \"metricValue\": 12693166.98,\n" +
                "    \"dtime\": \"2018-01-29\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"metricValue\": 12668239.57,\n" +
                "    \"dtime\": \"2018-01-30\"\n" +
                "  }\n" +
                "]");

        template.sendBody("direct:start", "[\n" +
                "  {\n" +
                "    \"metricValue\": 12693166.98,\n" +
                "    \"dtime\": \"2018-01-29\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"metricValu\": 12668239.57,\n" +
                "    \"dtime\": \"2018-01-30\"\n" +
                "  }\n" +
                "]");

    }


    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                        .to("mock:result");
            }
        };
    }

}