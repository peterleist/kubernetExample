FROM java:8
WORKDIR /smartuniversity
ARG timetable_value=0
ARG timetable_lab_id=0
ENV timetable=${timetable_value}
ENV lab_id=${timetable_lab_id}
ADD ./hu/bme/mit/cps/smartuniversity/*.java /smartuniversity/hu/bme/mit/cps/smartuniversity/
ADD ./hu/bme/mit/cps/smartuniversity/calendar/*.java /smartuniversity/hu/bme/mit/cps/smartuniversity/calendar/
ADD rti_license.dat /smartuniversity/rti_license.dat
ADD libs/* /smartuniversity/libs/
ADD *.txt /smartuniversity/
ADD ./hu/bme/mit/cps/smartuniversity/calendar/MANIFEST.MF /smartuniversity/META-INF/MANIFEST.MF
ENV LD_LIBRARY_PATH="/smartuniversity/libs:${LD_LIBRARY_PATH}"
RUN javac -classpath /smartuniversity/libs/nddsjava.jar ./hu/bme/mit/cps/smartuniversity/Entry.java ./hu/bme/mit/cps/smartuniversity/EntrySeq.java ./hu/bme/mit/cps/smartuniversity/EntryTypeCode.java ./hu/bme/mit/cps/smartuniversity/EntryTypeSupport.java ./hu/bme/mit/cps/smartuniversity/EntryDataReader.java ./hu/bme/mit/cps/smartuniversity/EntryDataWriter.java ./hu/bme/mit/cps/smartuniversity/calendar/Timetable.java
RUN jar cmvf ./META-INF/MANIFEST.MF cps-smartuniversity-timetable.jar hu/bme/mit/cps/smartuniversity/calendar/*.class hu/bme/mit/cps/smartuniversity/*.class
CMD java -jar cps-smartuniversity-timetable.jar ${timetable} ${lab_id}
