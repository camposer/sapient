#!/bin/bash

buildBackend() {
	cd backend && ./gradlew build
}

run() {
	docker-compose up
}

buildBackend
run
