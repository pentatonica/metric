package com.samknows.metric.be.services;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.samknows.metric.be.model.Metric;


/**
 * This service has the logic to generate the report files that finishes in the folder output.
 */
@Component
public class MetricService {
	
	private static final int BITS = 8;
	private static final double MEGA = 1E6;
	private static final double THRESHOLD_PERFORMANCE = 0.33;
	private static final String DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * In this method it is processed the payload of the json file 
	 * found in input folder
	 * Whatever file that can be put there is process
	 * Assumption the file is json and we don't format it
	 * @param metricList
	 * @return
	 */
	public StringBuilder metricFileProcessor(List<Metric>  metricList) {

		Map<String, String> statistic = analyseData(metricList);

		return this.getMetricReport(statistic, getAnomaliesReport(metricList));
		
	}

    public Map<String,String> analyseData(List<Metric> metricList) {

	    Map<String,String> stat = new HashMap<>();

        List<Metric> metricSorted = metricList.stream()
                .sorted(Comparator.comparing(Metric::getMetricValue))
                .collect(Collectors.toList());

        Double average;

        average = metricSorted.stream().mapToDouble(Metric::getMetricValue).average().getAsDouble();
        int numOfMeasures = metricList.size();

        double median;
        if(metricSorted.size()%2 == 1)
            median = metricSorted.get(numOfMeasures/2).getMetricValue();
        else {
            median = (metricSorted.get(numOfMeasures/2-1).getMetricValue() +
                    metricSorted.get(numOfMeasures/2).getMetricValue())/2;
        }

        stat.put("from", getFormatDate(metricList.get(0).getDtime()));
        stat.put("to", getFormatDate(metricList.get(numOfMeasures-1).getDtime()));
        stat.put("min", fromBytesToMegabits(metricSorted.get(0).getMetricValue()));
        stat.put("max",  fromBytesToMegabits(metricSorted.get(numOfMeasures-1).getMetricValue()));
        stat.put("median", fromBytesToMegabits(median) );
        stat.put("average", fromBytesToMegabits(average) );

	    return stat;
    }


    /**
     * This method change the payload in a pre-format report and return it
     * to the route to be sent to the output folder
     * @param statistic
     * @param anomalies
     * @return
     */
	private StringBuilder getMetricReport(Map<String, String> statistic, StringBuilder anomalies) {
		

		StringBuilder output = new StringBuilder();
		
		output.append("SamKnows Metric Analyser v1.0.0 \n");
		output.append("=============================== \n\n ");
		
		output.append("Period checked:\n\n ");
		
		output.append("\tFrom: "+ statistic.get("from")  + "\n");
		output.append("\tTo:   "+ statistic.get("to") +"\n\n");
		
		output.append("Statistics: \n\n");
		
		output.append("\tUnit: Megabits per second \n\n");
		
		output.append("\tAverage: "+ statistic.get("average")+"\n");
		output.append("\tMin: "+ statistic.get("min")+"\n");
		output.append("\tMax: "+ statistic.get("max")+"\n");
		output.append("\tMedian: "+ statistic.get("median")+"\n");
		
		output.append(anomalies);
		
		return output;
		
	}
	
	/**
	 * getAnomaliesReport method sends the new information
	 * that need to be added in case it is found some under performing period
	 * the assumption is that the periods are continued
	 * @param metricList
	 * @return
	 */
	
	private StringBuilder getAnomaliesReport(List<Metric> metricList) {
		
		List<Metric> anomalies = getAnomalies(metricList);
		
		StringBuilder anomaliesStr = new StringBuilder();
		
		if(anomalies.size()>0) {
			anomaliesStr.append("\nUnder-performing periods:\n\n");
			anomaliesStr.append("\t* The period between " + getFormatDate(anomalies.get(0).getDtime()) + " and "+ getFormatDate(anomalies.get(anomalies.size()-1).getDtime()));
			anomaliesStr.append("\n\t was under-performing.");
			
		}
		else
			return anomaliesStr.append("");
		return anomaliesStr;
	}
	

	/**
	 * The assumption is that every metric value under 0.33*maxValue of the measurments are under performance.
	 * It is supposed that all the measurements under performing are continuos.
	 * @param metricList
	 * @return a list of Metric objects that its metric value are under performance
	 */
	private List<Metric> getAnomalies(List<Metric> metricList) {

		return metricList.stream()
				.filter(metric -> metric.getMetricValue()<THRESHOLD_PERFORMANCE*metricList.get(metricList.size()-1).getMetricValue())
				.collect(Collectors.toList());
	}
	
	/**
	 * Convert bytes in Megabits
	 * @param num
	 * @return
	 */

	public String fromBytesToMegabits(double num) {

        DecimalFormat df = new DecimalFormat("0.00");

		return df.format(num*BITS/MEGA);
	}
	
	public String getFormatDate(Date date) {
		
		SimpleDateFormat  fm = new SimpleDateFormat(DATE_FORMAT);
		
		return fm.format(date).toString();		
	}
}