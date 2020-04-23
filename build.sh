#!/bin/bash

sbt build
# sbt unpackJars
# sbt stage
# sbt assembly
sbt docker

docker-compose up -d --scale planr=4
# docker-compose up -d --scale planr=1

# sbt -> project planr-gatling -> gatling:test