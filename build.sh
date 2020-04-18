#!/bin/bash

sbt build
# sbt unpackJars
# sbt stage
# sbt assembly
sbt docker

# docker run com.planr/planr:latest
# docker-compose up
docker-compose -d up --scale planr=5

# sbt -> project planr-gatling -> gatling:test