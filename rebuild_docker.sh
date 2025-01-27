#!/bin/sh

set -euxo pipefail

mvn clean install -DskipTests

pushd utilities/tools.descartes.teastore.dockerbase;

docker build -t descartesresearch/teastore-base .;

popd;
pushd tools;

./build_docker.sh;

popd;

docker-compose -f examples/docker/docker-compose_default.yaml build;

docker ps -a | tail -n +2 | awk '{ print $1 }' | xargs -L 1000 -P 100 docker rm -f

