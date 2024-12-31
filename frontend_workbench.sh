#!/bin/sh

docker run -ti \
  --entrypoint /bin/bash \
  -v ${PWD}:/tiny-tasks \
  -w /tiny-tasks \
  node:erbium
