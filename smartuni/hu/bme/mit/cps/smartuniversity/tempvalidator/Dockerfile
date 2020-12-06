FROM java:8
WORKDIR /smartuniversity
ARG sensor_lab_id=0
ENV lab_id=${sensor_lab_id}
ADD ./hu/bme/mit/cps/smartuniversity/*.java /smartuniversity/hu/bme/mit/cps/smartuniversity/
ADD ./hu/bme/mit/cps/smartuniversity/tempvalidator/*.java /smartuniversity/hu/bme/mit/cps/smartuniversity/tempvalidator/
ADD rti_license.dat /smartuniversity/rti_license.dat
ADD libs/* /smartuniversity/libs/
ADD *.txt /smartuniversity/
ADD ./hu/bme/mit/cps/smartuniversity/tempvalidator/MANIFEST.MF /smartuniversity/META-INF/MANIFEST.MF
ENV LD_LIBRARY_PATH="/smartuniversity/libs:${LD_LIBRARY_PATH}"
RUN javac -classpath /smartuniversity/libs/nddsjava.jar ./hu/bme/mit/cps/smartuniversity/Temperature.java ./hu/bme/mit/cps/smartuniversity/TemperatureSeq.java ./hu/bme/mit/cps/smartuniversity/TemperatureTypeCode.java ./hu/bme/mit/cps/smartuniversity/TemperatureTypeSupport.java ./hu/bme/mit/cps/smartuniversity/TemperatureDataReader.java ./hu/bme/mit/cps/smartuniversity/TemperatureDataWriter.java ./hu/bme/mit/cps/smartuniversity/tempvalidator/TempValidator.java
RUN jar cmvf ./META-INF/MANIFEST.MF cps-smartuniversity-tempvalidator.jar hu/bme/mit/cps/smartuniversity/tempvalidator/*.class hu/bme/mit/cps/smartuniversity/*.class
CMD java -jar cps-smartuniversity-tempvalidator.jar ${lab_id}
