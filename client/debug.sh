#!/bin/sh

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH/opt/opencv-2.4.2/lib
export COLUMNS
export LINES

#CLASSPATH="./bin:../tailfeather/target/classes"
#for lib in lib/*.jar lib/*/*.jar ; do
  #CLASSPATH="$CLASSPATH:$lib"
#done

#java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=6543 -cp $CLASSPATH org.tailfeather.acorn.Main acorn.xml

#DEBUG="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=6543"
#java $DEBUG -cp target/client-1.0-jar-with-dependencies.jar org.tailfeather.client.Main acorn.xml
export MAVEN_OPTS="-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000"
mvn compile exec:java
