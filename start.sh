#!/bin/bash

(cd backend && ./gradlew build)
docker-compose up
