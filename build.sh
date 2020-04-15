#!/bin/bash

sbt build
# sbt unpackJars
# sbt stage
# sbt assembly
sbt docker

# docker run com.planr/planr:latest
docker-compose up

# sbt gatling:test