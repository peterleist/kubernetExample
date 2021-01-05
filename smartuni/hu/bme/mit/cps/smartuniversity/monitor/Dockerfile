FROM java:8
WORKDIR /smartuniversity
ADD ./hu/bme/mit/cps/smartuniversity/*.java /smartuniversity/hu/bme/mit/cps/smartuniversity/
ADD ./hu/bme/mit/cps/smartuniversity/monitor/*.java /smartuniversity/hu/bme/mit/cps/smartuniversity/monitor/
ADD rti_license.dat /smartuniversity/rti_license.dat
ADD libs/* /smartuniversity/libs/
ADD *.txt /smartuniversity/
ADD ./hu/bme/mit/cps/smartuniversity/tempsensor/MANIFEST.MF /smartuniversity/META-INF/MANIFEST.MF
ENV LD_LIBRARY_PATH="/smartuniversity/libs:${LD_LIBRARY_PATH}"
RUN javac -classpath /smartuniversity/libs/nddsjava.jar ./hu/bme/mit/cps/smartuniversity/SystemState.java ./hu/bme/mit/cps/smartuniversity/TSystemStateSeq.java ./hu/bme/mit/cps/smartuniversity/SystemStateTypeCode.java ./hu/bme/mit/cps/smartuniversity/SystemStateTypeSupport.java ./hu/bme/mit/cps/smartuniversity/SystemStateDataReader.java ./hu/bme/mit/cps/smartuniversity/SystemStateDataWriter.java ./hu/bme/mit/cps/smartuniversity/monitor/Monitor.java ./hu/bme/mit/cps/smartuniversity/monitor/IMonitor.java ./hu/bme/mit/cps/smartuniversity/monitor/Automaton.java ./hu/bme/mit/cps/smartuniversity/monitor/Specification.java ./hu/bme/mit/cps/smartuniversity/monitor/State.java ./hu/bme/mit/cps/smartuniversity/monitor/StateType.java ./hu/bme/mit/cps/smartuniversity/monitor/Transition.java
RUN jar cmvf ./META-INF/MANIFEST.MF cps-smartuniversity-tempsensor.jar hu/bme/mit/cps/smartuniversity/monitor/*.class hu/bme/mit/cps/smartuniversity/*.class
CMD java -jar cps-smartuniversity-monitor.jar
