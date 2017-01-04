# smarthome2016
A java port for the OpenHPI 2016 embedded Smarthome course.
The course took place from Monday, 06. June 2016, 08:00 (UTC) until Sunday, 10. Juli 2016, 12:00 (UTC) as an openHPI MOOC. 
You can recap the course in self-study at https://open.hpi.de/courses/smarthome2016.
The python sources for this course can be found at https://github.com/openHPI/Embedded-Smart-Home-2016

The programming examples in the course are all based on python. This project shows how to access the hardware using Java.
You need the Board used during the course connected to an Raspberry PI, like shown in the course videos.
Easiest way to get all running is to check out the sources, build and run directly on your Raspberry PI.
Java 8 and maven is needed to build all sources.

Assumed <a href="https://maven.apache.org/">maven</a> is installed call "mvn site" in the root directory of the project
to generate the documentation for each module. You can build a complete local documentation site with "mvn site:stage".
I recommend to have a look at generated Java Doc in the projects report section.

Download <a href="https://open.hpi.de/files/61a191f3-7eaa-409b-8d35-f71cb493fc6c">circuit diagram</a>
Buy <a href="https://supr.com/embedded-smart-home/">the board</a>