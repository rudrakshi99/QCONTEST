#!/bin/bash

# ./gradlew clean build -x test --no-daemon
# java -jar build/libs/example.jar INPUT_FILE=sample_input/input1.txt

# or
./gradlew run -q --args="INPUT_FILE=sample_input/input1.txt"