FROM java:8
FROM gcc:7.3
WORKDIR /cpslab
ADD ./monitor/rtiddsspy /cpslab/
ADD rti_license.dat /cpslab/rti_license.dat
ADD linuxlibs/* /cpslab/libs/
ENV LD_LIBRARY_PATH="/cpslab/libs:${LD_LIBRARY_PATH}"
CMD ping localhost
