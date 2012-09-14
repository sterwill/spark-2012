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

mvn -e compile exec:java

stty $OLD_STTY
