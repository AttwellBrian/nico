#!/usr/bin/env bash
cd  ../docker-base
docker build . --file=Dockerfile-jre8 --tag=nico_jre8_base:latest
