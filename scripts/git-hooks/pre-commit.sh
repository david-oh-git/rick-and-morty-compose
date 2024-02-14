#!/bin/sh
echo "Running pre -commit checks..."

osname=$(uname -s)
#check if mac os
if [[ "$osname" == *"Darwin"* ]];
  then
  # use android studio's embedded java
  export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" >> ~/.zshenv
  #  source ~/.zshrc
  source ~/.zshenv
  echo $JAVA_HOME
else
  JAVA_HOME="/usr/lib/jvm/java-17-openjdk-17.0.9.0.9-3.fc39.x86_64"
  export JAVA_HOME
fi

# add all staged files to an array
staged_files=$(git diff --staged --name-only)

OUTPUT="/tmp/res"
./gradlew lint ktlintFormat spotlessApply --daemon > ${OUTPUT}
EXIT_CODE=$?
if [ ${EXIT_CODE} -ne 0 ]; then
    cat ${OUTPUT}
    rm ${OUTPUT}
    echo "Pre Commit Checks Failed. Please fix the above issues before committing"
    exit ${EXIT_CODE}
else
    rm ${OUTPUT}
    for file in $staged_files
      do
      if test -f "$file" ;
        then
        git add $file
      fi
    done
    echo "Pre Commit Checks Passed -- no problems found"
fi