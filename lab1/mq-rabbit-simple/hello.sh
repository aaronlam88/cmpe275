#!/bin/bash
#
#

# export HOME="$( cd "$( dirname "${BASH_SOURCE[0]}" )/../.." && pwd )"

JAVA_MAIN='app.HelloDemo'

# echo java -cp .:${HOME}/lib/'*':${HOME}/classes ${JAVA_MAIN}
# java -cp .:${HOME}/lib/'*':${HOME}/classes ${JAVA_MAIN}
echo java -cp .:$PWD/lib/*:$PWD/classes ${JAVA_MAIN}
java -cp .:$PWD/lib/*:$PWD/classes ${JAVA_MAIN}
