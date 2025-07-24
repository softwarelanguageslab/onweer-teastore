#!/bin/bash
set -euxo pipefail

registry='descartesresearch/'
docker buildx build --tag "${registry}teastore-db" ../utilities/tools.descartes.teastore.database/ --load
docker buildx build --tag "${registry}teastore-kieker-rabbitmq" ../utilities/tools.descartes.teastore.kieker.rabbitmq/ --load
docker buildx build --tag "${registry}teastore-base" ../utilities/tools.descartes.teastore.dockerbase/ --load
docker buildx build --tag "${registry}teastore-registry" ../services/tools.descartes.teastore.registry/ --load
docker buildx build --tag "${registry}teastore-persistence" ../services/tools.descartes.teastore.persistence/ --load
docker buildx build --tag "${registry}teastore-image" ../services/tools.descartes.teastore.image/ --load
docker buildx build --tag "${registry}teastore-webui" ../services/tools.descartes.teastore.webui/ --load
docker buildx build --tag "${registry}teastore-auth" ../services/tools.descartes.teastore.auth/ --load
docker buildx build --tag "${registry}teastore-recommender" ../services/tools.descartes.teastore.recommender/ --load
