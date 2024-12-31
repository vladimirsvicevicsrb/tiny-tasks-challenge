#!/bin/sh

docker run -ti \
  --entrypoint /bin/bash \
  --privileged \
  -v ${PWD}:/tiny-tasks \
  -w /tiny-tasks \
  -e GRADLE_USER_HOME=/tiny-tasks/.gradle \
  adoptopenjdk/openjdk11:jdk-11.0.11_9
