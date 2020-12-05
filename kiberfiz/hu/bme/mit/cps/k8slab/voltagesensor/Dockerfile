FROM java:8
WORKDIR /cpslab
ARG sensor_value=0
ENV sensor=${sensor_value}
ADD ./hu/bme/mit/cps/k8slab/*.java /cpslab/hu/bme/mit/cps/k8slab/
ADD ./hu/bme/mit/cps/k8slab/voltagesensor/*.java /cpslab/hu/bme/mit/cps/k8slab/voltagesensor/
ADD rti_license.dat /cpslab/rti_license.dat
ADD libs/* /cpslab/libs/
ADD *.txt /cpslab/
ADD ./hu/bme/mit/cps/k8slab/voltagesensor/MANIFEST.MF /cpslab/META-INF/MANIFEST.MF
ENV LD_LIBRARY_PATH="/cpslab/libs:${LD_LIBRARY_PATH}"
RUN javac -classpath /cpslab/libs/nddsjava.jar ./hu/bme/mit/cps/k8slab/Voltage.java ./hu/bme/mit/cps/k8slab/VoltageSeq.java ./hu/bme/mit/cps/k8slab/VoltageTypeCode.java ./hu/bme/mit/cps/k8slab/VoltageTypeSupport.java ./hu/bme/mit/cps/k8slab/VoltageDataReader.java ./hu/bme/mit/cps/k8slab/VoltageDataWriter.java ./hu/bme/mit/cps/k8slab/voltagesensor/VoltageSensor.java
RUN jar cmvf ./META-INF/MANIFEST.MF cps-k8slab-voltagesensor.jar hu/bme/mit/cps/k8slab/voltagesensor/*.class hu/bme/mit/cps/k8slab/*.class
CMD java -jar cps-k8slab-voltagesensor.jar $sensor
