FROM java:8
WORKDIR /cpslab
ADD ./hu/bme/mit/cps/k8slab/*.java /cpslab/hu/bme/mit/cps/k8slab/
ADD ./hu/bme/mit/cps/k8slab/currentvalidator/*.java /cpslab/hu/bme/mit/cps/k8slab/currentvalidator/
ADD rti_license.dat /cpslab/rti_license.dat
ADD libs/* /cpslab/libs/
ADD *.txt /cpslab/
ADD ./hu/bme/mit/cps/k8slab/currentvalidator/MANIFEST.MF /cpslab/META-INF/MANIFEST.MF
ENV LD_LIBRARY_PATH="/cpslab/libs:${LD_LIBRARY_PATH}"
RUN javac -classpath /cpslab/libs/nddsjava.jar ./hu/bme/mit/cps/k8slab/Current.java ./hu/bme/mit/cps/k8slab/CurrentSeq.java ./hu/bme/mit/cps/k8slab/CurrentTypeCode.java ./hu/bme/mit/cps/k8slab/CurrentTypeSupport.java ./hu/bme/mit/cps/k8slab/CurrentDataReader.java ./hu/bme/mit/cps/k8slab/CurrentDataWriter.java ./hu/bme/mit/cps/k8slab/currentvalidator/CurrentValidator.java
RUN jar cmvf ./META-INF/MANIFEST.MF cps-k8slab-currentvalidator.jar hu/bme/mit/cps/k8slab/currentvalidator/*.class hu/bme/mit/cps/k8slab/*.class
CMD java -jar cps-k8slab-currentvalidator.jar
