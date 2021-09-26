package com.samknows.metric.be.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;


import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MetricServiceTest {

    @Autowired
    private MetricService metricService;

    @Before
    public void setUp(){
        this.metricService = new MetricService();
    }

    @Test
    @DisplayName(" Verifying conversion Bytes to Megabits per sec. ")
    public void testMetricFileCheckConversionBytesInMegabits(){

        assertEquals("101.55", metricService.fromBytesToMegabits(12693166.98));

    }

}
