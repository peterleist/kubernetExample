FROM java:8
WORKDIR /cpslab
ADD ./hu/bme/mit/cps/k8slab/*.java /cpslab/hu/bme/mit/cps/k8slab/
ADD ./hu/bme/mit/cps/k8slab/powercalculator/*.java /cpslab/hu/bme/mit/cps/k8slab/powercalculator/
ADD rti_license.dat /cpslab/rti_license.dat
ADD libs/* /cpslab/libs/
ADD jar/* /cpslab/jars/
ADD *.txt /cpslab/
ADD ./hu/bme/mit/cps/k8slab/powercalculator/MANIFEST.MF /cpslab/META-INF/MANIFEST.MF
ENV LD_LIBRARY_PATH="/cpslab/libs:${LD_LIBRARY_PATH}"
RUN javac -classpath /cpslab/libs/nddsjava.jar:/cpslab/jars/influxdb-java-2.10.jar:/cpslab/jars/okhttp-3.10.0.jar:/cpslab/jars/okio-1.14.0.jar:/cpslab/jars/retrofit-2.4.0.jar:/cpslab/jars/moshi-1.5.0.jar:/cpslab/jars/logging-interceptor-3.10.0.jar:/cpslab/jars/converter-moshi-2.4.0.jar ./hu/bme/mit/cps/k8slab/*.java ./hu/bme/mit/cps/k8slab/powercalculator/PowerCalculator.java
RUN jar cmvf ./META-INF/MANIFEST.MF cps-k8slab-powercalculator.jar hu/bme/mit/cps/k8slab/powercalculator/*.class hu/bme/mit/cps/k8slab/*.class
CMD java -jar cps-k8slab-powercalculator.jar