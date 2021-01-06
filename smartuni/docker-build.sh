
#set minikube as docker environment
#eval $(minikube -p minikube docker-env)

#building images
#docker build -t cps-smartuni-tempsensor --build-arg sensor_value=0 --build-arg lab_id=0 -f ./hu/bme/mit/cps/smartuniversity/tempsensor/Dockerfile .; docker build -t cps-k8slab-currentsensor1 --build-arg sensor_value=1 -f ./hu/bme/mit/cps/k8slab/currentsensor/Dockerfile .; docker build -t cps-k8slab-currentsensor2 --build-arg sensor_value=2 -f ./hu/bme/mit/cps/k8slab/currentsensor/Dockerfile .; docker build -t cps-k8slab-voltagesensor0 --build-arg sensor_value=0 -f ./hu/bme/mit/cps/k8slab/voltagesensor/Dockerfile .; docker build -t cps-k8slab-voltagesensor1 --build-arg sensor_value=1 -f ./hu/bme/mit/cps/k8slab/voltagesensor/Dockerfile .; docker build -t cps-k8slab-voltagesensor2 --build-arg sensor_value=2 -f ./hu/bme/mit/cps/k8slab/voltagesensor/Dockerfile .; docker build -t cps-k8slab-currentvalidator -f ./hu/bme/mit/cps/k8slab/currentvalidator/Dockerfile .; docker build -t cps-k8slab-voltagevalidator -f ./hu/bme/mit/cps/k8slab/voltagevalidator/Dockerfile .; docker build -t cps-k8slab-powercalculator -f ./hu/bme/mit/cps/k8slab/powercalculator/Dockerfile .
#docker build -t cps-k8slab-monitor -f ./monitor/Dockerfile .


docker build -t cps-smartuni-tempsensor0-0 --build-arg sensor_value=0 --build-arg sensor_lab_id=0 -f ./hu/bme/mit/cps/smartuniversity/tempsensor/Dockerfile .
docker build -t cps-smartuni-tempsensor0-1 --build-arg sensor_value=1 --build-arg sensor_lab_id=0 -f ./hu/bme/mit/cps/smartuniversity/tempsensor/Dockerfile .
docker build -t cps-smartuni-tempsensor0-2 --build-arg sensor_value=2 --build-arg sensor_lab_id=0 -f ./hu/bme/mit/cps/smartuniversity/tempsensor/Dockerfile .

docker build -t cps-smartuni-tempvalidator0 --build-arg sensor_lab_id=0 -f ./hu/bme/mit/cps/smartuniversity/tempvalidator/Dockerfile .

docker build -t cps-smartuni-weather -f ./hu/bme/mit/cps/smartuniversity/weather/Dockerfile .

docker build -t cps-smartuni-calendar0 --build-arg timetable_lab_id=0 --build-arg timetable_value=0 -f ./hu/bme/mit/cps/smartuniversity/calendar/Dockerfile .
docker build -t cps-smartuni-calendarvalidator0 --build-arg timetable_lab_id=0 -f ./hu/bme/mit/cps/smartuniversity/calendarvalidator/Dockerfile .

docker build -t cps-smartuni-adjustment0 --build-arg adj_lab_id=0 -f ./hu/bme/mit/cps/smartuniversity/adjustmentcalculator/Dockerfile .



docker build -t cps-smartuni-tempsensor1-0 --build-arg sensor_value=0 --build-arg sensor_lab_id=1 -f ./hu/bme/mit/cps/smartuniversity/tempsensor/Dockerfile .; docker build -t cps-smartuni-tempsensor1-1 --build-arg sensor_value=1 --build-arg sensor_lab_id=1 -f ./hu/bme/mit/cps/smartuniversity/tempsensor/Dockerfile .; docker build -t cps-smartuni-tempsensor1-2 --build-arg sensor_value=2 --build-arg sensor_lab_id=1 -f ./hu/bme/mit/cps/smartuniversity/tempsensor/Dockerfile .; docker build -t cps-smartuni-tempvalidator1 --build-arg sensor_lab_id=1 -f ./hu/bme/mit/cps/smartuniversity/tempvalidator/Dockerfile .; docker build -t cps-smartuni-calendar1 --build-arg timetable_lab_id=1 --build-arg timetable_value=0 -f ./hu/bme/mit/cps/smartuniversity/calendar/Dockerfile .; docker build -t cps-smartuni-calendarvalidator1 --build-arg timetable_lab_id=1 -f ./hu/bme/mit/cps/smartuniversity/calendarvalidator/Dockerfile .; docker build -t cps-smartuni-adjustment1 --build-arg adj_lab_id=1 -f ./hu/bme/mit/cps/smartuniversity/adjustmentcalculator/Dockerfile .; docker build -t cps-smartuni-tempsensor0-0 --build-arg sensor_value=0 --build-arg sensor_lab_id=0 -f ./hu/bme/mit/cps/smartuniversity/tempsensor/Dockerfile .; docker build -t cps-smartuni-tempsensor0-1 --build-arg sensor_value=1 --build-arg sensor_lab_id=0 -f ./hu/bme/mit/cps/smartuniversity/tempsensor/Dockerfile .; docker build -t cps-smartuni-tempsensor0-2 --build-arg sensor_value=2 --build-arg sensor_lab_id=0 -f ./hu/bme/mit/cps/smartuniversity/tempsensor/Dockerfile .; docker build -t cps-smartuni-tempvalidator0 --build-arg sensor_lab_id=0 -f ./hu/bme/mit/cps/smartuniversity/tempvalidator/Dockerfile .; docker build -t cps-smartuni-weather -f ./hu/bme/mit/cps/smartuniversity/weather/Dockerfile .; docker build -t cps-smartuni-calendar0 --build-arg timetable_lab_id=0 --build-arg timetable_value=0 -f ./hu/bme/mit/cps/smartuniversity/calendar/Dockerfile .; docker build -t cps-smartuni-calendarvalidator0 --build-arg timetable_lab_id=0 -f ./hu/bme/mit/cps/smartuniversity/calendarvalidator/Dockerfile .; docker build -t cps-smartuni-adjustment0 --build-arg adj_lab_id=0 -f ./hu/bme/mit/cps/smartuniversity/adjustmentcalculator/Dockerfile .; docker build -t cps-smartuni-monitor -f ./hu/bme/mit/cps/smartuniversity/monitor/Dockerfile .
