#!/bin/sh
export COLUMNS
export LINES

CLASSPATH="bin:./lib/commons-codec-1.6.jar:./lib/fluent-hc-4.2.1.jar:./lib/httpclient-cache-4.2.1.jar:./lib/httpmime-4.2.1.jar:./lib/commons-logging-1.1.1.jar:./lib/httpclient-4.2.1.jar:./lib/httpcore-4.2.1.jar"

java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=6543 -cp $CLASSPATH org.tailfeather.acorn.Main acorn.xml
