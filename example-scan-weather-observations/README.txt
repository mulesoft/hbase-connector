HBase Scan Weather Observations demo
==================================

INTRODUCTION
  This is minimalistic demo that shows how an HBase table can be scanned. This demo must be run after the HBase Add Weather Observation demo, which creates and 
  adds data to the table.  

HOW TO DEMO:
  1. Although under normal circumstances an HBase client needs many properties to be set, in order to keep things simple, 
  this demo uses default HBase client configuration, which assumes an HBase server locally started using default ports, as described in the HBase
  quick tutorial: http://hbase.apache.org/book/quickstart.html.
  2. Run the "ScanWeatherObservation" flow from HBaseFunctionalTestDriver, or deploy the example in a mule Container and hit  
  		http://localhost:9091/hbase-demo-weather-scan-weather-data
  3. Check that the values were added from the HBase Shell. For example: 		
	You should get an org.apache.hadoop.hbase.client.Result for each value stored. This object lets you access the row information.  

HOW IT WORKS:
   - If checks if WeatherObservations table exists. If not, it fails. 
   - It scans up to 40 versions for each row of the WeatherObservations table.
   - It converts each of the answered Result objects into string using a collection splitter
