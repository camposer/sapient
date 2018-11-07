#!/bin/bash

# Build process occurs locally and not inside of the docker container for performance sake

buildBackend() {
	echo "Building backend..."
	(cd backend && ./gradlew build)
}

buildFrontend() {
	echo "Building frontend..."
	(cd frontend && npm install && npm run build:prod)
}

run() {
	echo "Starting docker containers..."
	docker-compose up
}

buildBackend
buildFrontend
run
