#!/bin/bash
DIR=$(dirname $0)

if [ "$#" -lt 1 ]; then
    echo "usage startProject.sh <projectDir> [options]"
    exit 1
fi

for FILE in `ls $1/*.jar`
do
    CP=$CP:$FILE
done

shift

start() {
java $JAVA_OPTIONS -cp "`dirname $0`/../lib/boot/*:`dirname $0`/../lib/*":$JAVA_HOME/lib/tools.jar tekgenesis.app.SuiGeneris -a `dirname $0`/../webapp -m $CP start  "$@"
}
E_CODE=124
while [ $E_CODE -eq 124 ]
do
$DIR/pre-start.sh
start "$@"
E_CODE=$?
done
