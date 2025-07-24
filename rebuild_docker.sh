#!/bin/bash

set -euxo pipefail
export DOCKER_BUILDKIT=1

mvn clean install -DskipTests

pushd utilities/tools.descartes.teastore.dockerbase;

docker build --tag descartesresearch/teastore-base .;

popd;
pushd tools;

registry='descartesresearch/'
docker build --tag "${registry}teastore-db" ../utilities/tools.descartes.teastore.database/
docker build --tag "${registry}teastore-kieker-rabbitmq" ../utilities/tools.descartes.teastore.kieker.rabbitmq/
docker build --tag "${registry}teastore-base" ../utilities/tools.descartes.teastore.dockerbase/
docker build --tag "${registry}teastore-registry" ../services/tools.descartes.teastore.registry/
docker build --tag "${registry}teastore-persistence" ../services/tools.descartes.teastore.persistence/
docker build --tag "${registry}teastore-image" ../services/tools.descartes.teastore.image/
docker build --tag "${registry}teastore-webui" ../services/tools.descartes.teastore.webui/
docker build --tag "${registry}teastore-auth" ../services/tools.descartes.teastore.auth/
docker build --tag "${registry}teastore-recommender" ../services/tools.descartes.teastore.recommender/

popd;

docker-compose -f examples/docker/docker-compose_default.yaml build;

docker ps -a | tail -n +2 | awk '{ print $1 }' | xargs -L 1000 -P 100 docker rm -f
