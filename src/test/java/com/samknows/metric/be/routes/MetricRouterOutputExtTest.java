package com.samknows.metric.be.routes;

import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;

import java.io.File;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MetricRouterOutputExtTest extends CamelTestSupport {

    private final static String FAKE_BODY = "[{\"metricValue\":12693166.98,\"dtime\":\"2018-01-29\"},{\"metricValue\":12668239.57,\"dtime\":\"2018-01-30\"}]";

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

    @Before
    public void before() throws Exception {
        File mock_trash = new File("mock-files/");

        if (mock_trash.isDirectory()) {
            File[] files = mock_trash.listFiles();
            if (files.length > 0) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith("000000000"))
                        file.delete();
                }
            }
        }
    }

    @Test
    @DisplayName(" Test file in output generate a correct ext output")
    public void testTheOuputFileNameExtension() throws InterruptedException{

        template.sendBody("file:mock-files", FAKE_BODY);
        Thread.sleep(3000);

        File file=new File("mock-files");
        assertTrue(file.isDirectory());

        Exchange exchange = consumer.receive("file:mock-files/outputs");

        System.out.println("Output file's name is :" + exchange.getIn().getHeader("CamelFileName"));
        assertEquals("1.output", exchange.getIn().getHeader("CamelFileName"));
    }

    @Test
    @DisplayName(" Test file in output generate a wrong extension")
    public void testTheOuputFileNameExtensionFalse() throws InterruptedException{

        template.sendBody("file:mock-files", FAKE_BODY);
        Thread.sleep(3000);

        File file=new File("mock-files");
        assertTrue(file.isDirectory());

        Exchange exchange = consumer.receive("file:mock-files/outputs");

        System.out.println("Output file's name is :" + exchange.getIn().getHeader("CamelFileName"));
        assertNotEquals("1.outpu", exchange.getIn().getHeader("CamelFileName"));
    }
}
