FROM java:8
WORKDIR /smartuniversity
ADD ./hu/bme/mit/cps/smartuniversity/*.java /smartuniversity/hu/bme/mit/cps/smartuniversity/
ADD ./hu/bme/mit/cps/smartuniversity/weather/*.java /smartuniversity/hu/bme/mit/cps/smartuniversity/weather/
ADD rti_license.dat /smartuniversity/rti_license.dat
ADD libs/* /smartuniversity/libs/
ADD *.txt /smartuniversity/
ADD ./hu/bme/mit/cps/smartuniversity/weather/MANIFEST.MF /smartuniversity/META-INF/MANIFEST.MF
ENV LD_LIBRARY_PATH="/smartuniversity/libs:${LD_LIBRARY_PATH}"
RUN javac -classpath /smartuniversity/libs/nddsjava.jar ./hu/bme/mit/cps/smartuniversity/EnvironmentTemperature.java ./hu/bme/mit/cps/smartuniversity/EnvironmentTemperatureSeq.java ./hu/bme/mit/cps/smartuniversity/EnvironmentTemperatureTypeCode.java ./hu/bme/mit/cps/smartuniversity/EnvironmentTemperatureTypeSupport.java ./hu/bme/mit/cps/smartuniversity/EnvironmentTemperatureDataReader.java ./hu/bme/mit/cps/smartuniversity/EnvironmentTemperatureDataWriter.java ./hu/bme/mit/cps/smartuniversity/weather/WeatherForecast.java
RUN jar cmvf ./META-INF/MANIFEST.MF cps-smartuniversity-weather.jar hu/bme/mit/cps/smartuniversity/weather/*.class hu/bme/mit/cps/smartuniversity/*.class
CMD java -jar cps-smartuniversity-weather.jar
