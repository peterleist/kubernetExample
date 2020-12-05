
#set minikube as docker environment
eval $(minikube -p minikube docker-env)

#building images
docker build -t cps-k8slab-currentsensor0 --build-arg sensor_value=0 -f ./hu/bme/mit/cps/k8slab/currentsensor/Dockerfile .
docker build -t cps-k8slab-currentsensor1 --build-arg sensor_value=1 -f ./hu/bme/mit/cps/k8slab/currentsensor/Dockerfile .
docker build -t cps-k8slab-currentsensor2 --build-arg sensor_value=2 -f ./hu/bme/mit/cps/k8slab/currentsensor/Dockerfile .
docker build -t cps-k8slab-voltagesensor0 --build-arg sensor_value=0 -f ./hu/bme/mit/cps/k8slab/voltagesensor/Dockerfile .
docker build -t cps-k8slab-voltagesensor1 --build-arg sensor_value=1 -f ./hu/bme/mit/cps/k8slab/voltagesensor/Dockerfile .
docker build -t cps-k8slab-voltagesensor2 --build-arg sensor_value=2 -f ./hu/bme/mit/cps/k8slab/voltagesensor/Dockerfile .

docker build -t cps-k8slab-currentvalidator -f ./hu/bme/mit/cps/k8slab/currentvalidator/Dockerfile .
docker build -t cps-k8slab-voltagevalidator -f ./hu/bme/mit/cps/k8slab/voltagevalidator/Dockerfile .

docker build -t cps-k8slab-powercalculator -f ./hu/bme/mit/cps/k8slab/powercalculator/Dockerfile .
docker build -t cps-k8slab-monitor -f ./monitor/Dockerfile .
