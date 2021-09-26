package com.samknows.metric.be.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Entity that map the element in the file json
 */
public class Metric {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date dtime;

	private double metricValue;

	public Metric() {

	}

	public Date getDtime() {
		return dtime;
	}

	public void setDtime(Date dtime) {
		this.dtime = dtime;
	}

	public double getMetricValue() {
		return metricValue;
	}

	public void setMetricValue(double metricValue) {
		this.metricValue = metricValue;
	}

	public Metric(Date dtime, double metricValue) {
		this.dtime = dtime;
		this.metricValue = metricValue;
	}


}
