# Introduction

The openHPI smarthome server application was developed to show how to provide simple access to the smarthome board
using a webserver that produces JSON



# Running The Application

To test the  application run the following commands.

* To package the example run.

        mvn package

* To run the server run.

        java -jar target/smarthome2016-server-1.0.0-SNAPSHOT.jar server smarthome.yml

* To read the Board values IndoorTemp, OutdoorTemp and Humidity.

	http://localhost:8080/board

