#!/bin/sh
echo "Running pre -commit checks..."

JAVA_HOME="/usr/lib/jvm/java-17-openjdk-17.0.9.0.9-3.fc39.x86_64"
export JAVA_HOME

OUTPUT="/tmp/res"
./gradlew lint ktlintFormat ktlintCheck spotlessApply --daemon > ${OUTPUT}
EXIT_CODE=$?
if [ ${EXIT_CODE} -ne 0 ]; then
    cat ${OUTPUT}
    rm ${OUTPUT}
    echo "Pre Commit Checks Failed. Please fix the above issues before committing"
    exit ${EXIT_CODE}
else
    rm ${OUTPUT}
    echo "Pre Commit Checks Passed -- no problems found"
fi