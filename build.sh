#!/bin/bash

sbt build
sbt assembly
# sbt stage
sbt unpackJars
sbt docker

docker-compose up -d # docker run com.planr/planr:latest