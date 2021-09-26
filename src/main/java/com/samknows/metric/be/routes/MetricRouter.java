package com.samknows.metric.be.routes;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.ListJacksonDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.samknows.metric.be.config.CamelConfig;
import com.samknows.metric.be.model.Metric;
import com.samknows.metric.be.services.MetricService;

/**
 * This route is responsible for loading the files under input folder and storage
 * the results of the analisis of data
 * in output folder
 */
@Component
public class MetricRouter extends RouteBuilder{
	
	public static final String ROUTE_NAME = "METRIC_LOAD_ROUTE";

	@Autowired
	private final CamelConfig camelConfig; 
	
	@Autowired
	private MetricService metricService;
	
	public MetricRouter() {
		this.camelConfig = new CamelConfig();
		
	}
	
	@Override
	public void configure() throws Exception {
		
		ListJacksonDataFormat listJacksonDataFormat = new ListJacksonDataFormat(Metric.class);

		onException(Exception.class).continued(false);

		from(camelConfig.getMetricLoadEndpoint()).routeId(ROUTE_NAME)
		.choice()
		.when(simple("${file:ext} == 'json'"))
			.choice()
			.when(simple("${bodyAs(String)} contains 'dtime' && ${bodyAs(String)} contains 'metricValue'"))
				.log(LoggingLevel.INFO, "Fetch and convert in Metric List .. ${file:name} ")
				.unmarshal(listJacksonDataFormat)
				.log(LoggingLevel.INFO, "Processing file ... ${file:name} ")
				.bean(metricService) //The body that metric Service received is a List of Metrics
				.to(camelConfig.getMetricStorageEndpoint()+"/?fileName=${file:onlyname.noext}.output")
			.endChoice()
		.otherwise()
			.log(LoggingLevel.ERROR, "File ${file:name} with incorrect extension")
		.end();
		
	}

}
