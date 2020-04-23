#!/bin/bash

sbt build
# sbt unpackJars
# sbt stage
# sbt assembly
sbt docker

docker-compose up -d --scale planr=4
# java -jar -Xmx512M -Xms512M -Xss1M planr-assembly-1.0.jar
# docker-compose up -d --scale planr=1
# java -jar -Xmx2048M -Xms2048M -Xss4M planr-assembly-4.0.jar

# sbt -> project planr-gatling -> gatling:test