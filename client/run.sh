#!/bin/sh

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH/opt/opencv-2.4.2/lib
export COLUMNS
export LINES
OLD_STTY=`stty --save`

# unset ^D
stty eof ""
# unset ^Z
stty susp ""
# unset ^C
stty intr ""
# unset ^\
stty quit ""

CLASSPATH="bin:./lib/commons-codec-1.6.jar:./lib/fluent-hc-4.2.1.jar:./lib/httpclient-cache-4.2.1.jar:./lib/httpmime-4.2.1.jar:./lib/commons-logging-1.1.1.jar:./lib/httpclient-4.2.1.jar:./lib/httpcore-4.2.1.jar:./lib/javacv-0.2-bin/javacv.jar:./lib/javacv-0.2-bin/javacv-linux-x86_64.jar"

java -cp $CLASSPATH org.tailfeather.acorn.Main acorn.xml

stty $OLD_STTY
