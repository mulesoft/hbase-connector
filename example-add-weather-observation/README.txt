HBase Add Weather Observation demo
==================================

INTRODUCTION
  This is minimalistic demo that shows how a data can be added to an HBase Table. 
  It populates an HBase table rows with current weather information for a given airport ICAO code - http://en.wikipedia.org/wiki/List_of_airports_by_ICAO_code

HOW TO DEMO:
  1. Although under normal circumstances an HBase client needs many properties to be set, in order to keep things simple, 
  this demo uses default HBase client configuration, which assumes an HBase server locally started using default ports, as described in the HBase
  quick tutorial: http://hbase.apache.org/book/quickstart.html.
  2. Run the "AddWeatherObservation" flow from HBaseFunctionalTestDriver, or deploy the example in a mule Container and hit  
  		http://localhost:9090/hbase-demo-weather-add-weather-data?cityIcao=<AN ICAO Code> using your browser. You should get a Json with that information.
  3. Check that the values were added from the HBase Shell. For example: 		
	hbase(main):022:0> scan 'WeatherObservations', { VERSIONS => 40 }
	ROW                                                COLUMN+CELL                                                                                                                                        
 	KBCT                                              column=Weather:Clouds, timestamp=1305654707566, value=scattered clouds                                                                             
 	KBCT                                              column=Weather:Temperature, timestamp=1305654707563, value=31                                                                                      
 	KMCO                                              column=Weather:Clouds, timestamp=1305654575007, value=scattered clouds                                                                             
 	KMCO                                              column=Weather:Temperature, timestamp=1305654575003, value=25                                                                                      
	SABE                                              column=Weather:Clouds, timestamp=1305654803070, value=few clouds                                                                                   
 	SABE                                              column=Weather:Temperature, timestamp=1305654803067, value=18                                                                                      
	3 row(s) in 0.0330 seconds
	  		 
    You should add see an entry for each rowkey - ICAO code -, each Column family and qualifier - Weather:Clouds for sky status information and   Weather:Temperature for
    temperature information, and each version - repeated invocations of the same flow for the same ICAO. 

HOW IT WORKS:
   - If checks if WeatherObservations table exists. If not, it creates and configures it. 
   - It gets the weather status from ws.geonames.org using a dynamic http endpoint - http://www.mulesoft.org/documentation/display/MULE3USER/Configuring+Endpoints#ConfiguringEndpoints-DynamicEndpoints
   - It parses the response using the mule json support
   - It stores the data in the WeatherObservations table 
