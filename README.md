### Metric Analyser

The project was developed in camel.

The application processes a file json that contains the information to analyse: 
- dtime: represents the date of the measurements
- metricValue: represents the measurement in byte per second

## What we want to obtain

The final result is a file that will be stored under a folder called outputs.  It contains the following value of the metric:
- Minimun 
- Maximun 
- Average or mean 
- Median
- If there are lower performances it will be shown the period where this performance happened.

## Assumptions

- We will assume that when a metric is below 33% of the maximum value it will be underperforming.
- The possibility that it is underperforming all month is not evaluated.
- We will assume that the underperformance periods are continuous.
 
## Output

A pre-format output will be written in a file which name is the same as the file json and the extension will be change from json to ouput.
The numeric value are in Megabits per second.  Therefore, a conversion to Megabytes per second to Megabits per second will be there.

## Formula

Average =

Median =

## Thoughts

- What happen if the under performing is only one day or in alternate days ?
- 

