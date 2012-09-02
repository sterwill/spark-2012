#!/bin/sh

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH/opt/opencv-2.4.2/lib
export COLUMNS
export LINES

CLASSPATH="./bin:../tailfeather/target/classes"
for lib in lib/*.jar lib/*/*.jar ; do
  CLASSPATH="$CLASSPATH:$lib"
done

java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=6543 -cp $CLASSPATH org.tailfeather.acorn.Main acorn.xml
