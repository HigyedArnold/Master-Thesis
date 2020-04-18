#!/bin/bash

sbt build
# sbt unpackJars
# sbt stage
# sbt assembly
sbt docker

# docker run com.planr/planr:latest
# docker-compose up -d
docker-compose up -d --scale planr=1

# sbt -> project planr-gatling -> gatling:test