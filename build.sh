#!/bin/bash
sbt build
sbt assembly
sbt unpackJars
sbt docker
docker run com.planr/planr-main:latest