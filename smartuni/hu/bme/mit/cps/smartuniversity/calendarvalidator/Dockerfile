FROM java:8
WORKDIR /smartuniversity
ARG timetable_lab_id=0
ENV lab_id=${timetable_lab_id}
ADD ./hu/bme/mit/cps/smartuniversity/*.java /smartuniversity/hu/bme/mit/cps/smartuniversity/
ADD ./hu/bme/mit/cps/smartuniversity/calendarvalidator/*.java /smartuniversity/hu/bme/mit/cps/smartuniversity/calendarvalidator/
ADD rti_license.dat /smartuniversity/rti_license.dat
ADD libs/* /smartuniversity/libs/
ADD jar/* /smartuniversity/jars/
ADD *.txt /smartuniversity/
ADD ./hu/bme/mit/cps/smartuniversity/calendarvalidator/MANIFEST.MF /smartuniversity/META-INF/MANIFEST.MF
ENV LD_LIBRARY_PATH="/smartuniversity/libs:${LD_LIBRARY_PATH}"
RUN javac -classpath /smartuniversity/libs/nddsjava.jar:/smartuniversity/jars/influxdb-java-2.10.jar:/smartuniversity/jars/okhttp-3.10.0.jar:/smartuniversity/jars/okio-1.14.0.jar:/smartuniversity/jars/retrofit-2.4.0.jar:/smartuniversity/jars/moshi-1.5.0.jar:/smartuniversity/jars/logging-interceptor-3.10.0.jar:/smartuniversity/jars/converter-moshi-2.4.0.jar ./hu/bme/mit/cps/smartuniversity/*.java ./hu/bme/mit/cps/smartuniversity/calendarvalidator/TimetableValidator.java
RUN jar cmvf ./META-INF/MANIFEST.MF cps-smartuniversity-timetablevalidator.jar hu/bme/mit/cps/smartuniversity/calendarvalidator/*.class hu/bme/mit/cps/smartuniversity/*.class
CMD java -jar cps-smartuniversity-timetablevalidator.jar ${lab_id}
