#!/usr/bin/env bash

# Called by the build process to generate the static resources, which are served by this service.
cd ../frontend
npm run build
