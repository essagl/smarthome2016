# smarthome2016
A java port for the OpenHPI 2016 embedded Smarthome course.
The course took place from Monday, 06. June 2016, 08:00 (UTC) until Sunday, 10. Juli 2016, 12:00 (UTC) as an openHPI MOOC. 
You can recap the course in self-study at https://open.hpi.de/courses/smarthome2016.
The python sources for this course can be found at https://github.com/openHPI/Embedded-Smart-Home-2016

The programming examples in the course are all based on python.
This project shows how to access the hardware using Java and how to control the board using the server component.
The Board is controlled using the <a href="http://pi4j.com/">PI4J</a> API. The server is based on the
<a href="http://www.dropwizard.io/1.0.5/docs/">dropwizard</a> framework. The implementation includes a
<a href="http://swagger.io/">swagger</a> based generated client to access the server functionality.
You will have most fun with the Board used during the course connected to an Raspberry PI, like shown in the course videos,
but it is not required. If you are interested in the server component, which is indeed the focus of this project, a mock
implementation of the board interface is included. See also
<a href="https://github.com/essagl/smarthome2016/wiki/Configuration-described">the wiki page</a>.

Installation:

Build from source:
Java 8 and maven is required to build all sources.
Assumed <a href="https://maven.apache.org/">maven</a> is installed call "mvn clean install" in the root directory
of the project to build the project and  "mvn site" to generate the documentation for each module.
You can build a complete local documentation site with "mvn site:stage".
I recommend to have a look at generated Java Doc in the projects report section.

To start the server change to the "server" directory and call:
<b>java -jar target/server-1.0-SNAPSHOT.jar db migrate smarthome.yml</b> to prepare the user database
and create an "admin" user with password "secret".
<b>java -jar target/server-1.0-SNAPSHOT.jar server smarthome.yml</b> starts the server.
You can now access the client with your browser on your Raspberry Pi with http://localhost:8080/swagger.

Download <a href="https://open.hpi.de/files/61a191f3-7eaa-409b-8d35-f71cb493fc6c">circuit diagram</a>
Buy <a href="https://supr.com/embedded-smart-home/">the board</a>