#!/bin/bash

sbt build
sbt assembly
# sbt stage
sbt unpackJars
sbt docker

docker-compose up # docker run com.planr/planr:latest