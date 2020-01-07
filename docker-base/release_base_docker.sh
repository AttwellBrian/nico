#!/usr/bin/env bash
cd  ../docker-base
docker build . --file=Dockerfile-jre8 --tag=gcr.io/nico-264204/nico_jre8_base:latest
gcr.io/nico-264204/nico_jre8_base
