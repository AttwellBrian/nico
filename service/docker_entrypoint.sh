#!/usr/bin/env bash

# Run command that exposes the ability to pass in JAVA_OPTS for case of interactive debugging.
# This should not be invoked directly. This should only be used within docker files.
exec java $JAVA_OPTS -cp /app/resources:/app/classes:/app/libs/* "$MAIN_CLASS_NAME"
