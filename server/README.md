# Introduction

The openHPI smarthome server application was developed to show how to provide simple access to the smarthome board
using a webserver that produces JSON



# Running The Application

To test the  application run the following commands.

* To package the server application run.

        mvn package

* To run the server run from server module base directory:

        java -jar target/smarthome2016-server-1.0.0-SNAPSHOT.jar server smarthome.yml

* To read the Board values IndoorTemp, OutdoorTemp and Humidity start a Browser on your RaspberryPi and go to URL:

	http://localhost:8080/board

