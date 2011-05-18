HBase Add Weather Observation demo
==================================

INTRODUCTION
  This is minimalistic demo that shows how a data can be added to an HBase Table. It populates hbase rows with current weather information for airports.

HOW TO DEMO:
  1. Set the following system properties:
    

  2. Run the "AddWeatherObservation" flow from HBaseFunctionalTestDriver, or deploy the example in a mule Container and hit  
  		http://localhost:9090/hbase-demo-weather-add-weather-data?cityIcao=<AN ICAO Code>
    This will process a USD 50 payment, capture the transaction and send a confirmation email.
    To simplify usage of the demo, the only parameter expected is the payment ammount. The rest of the payment information has been hard-coded into the payload.

HOW IT WORKS:
   - "doDirectPayment" API method is called with specified amount and fake payer and credit card information
   - "capturePayment" API method is called, with the transaction ID and amount received from the payment operation
   - Summary email is sent to the specified address.

   

hbase(main):022:0> scan 'WeatherObservations'
ROW                                                COLUMN+CELL                                                                                                                                        
 KBCT                                              column=Weather:Clouds, timestamp=1305654707566, value=scattered clouds                                                                             
 KBCT                                              column=Weather:Temperature, timestamp=1305654707563, value=31                                                                                      
 KMCO                                              column=Weather:Clouds, timestamp=1305654575007, value=scattered clouds                                                                             
 KMCO                                              column=Weather:Temperature, timestamp=1305654575003, value=25                                                                                      
 SABE                                              column=Weather:Clouds, timestamp=1305654803070, value=few clouds                                                                                   
 SABE                                              column=Weather:Temperature, timestamp=1305654803067, value=18                                                                                      
3 row(s) in 0.0330 seconds

hbase(main):023:0> 
   