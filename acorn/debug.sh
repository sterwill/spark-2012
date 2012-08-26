#!/bin/sh
export COLUMNS
export LINES
java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=6543 -cp bin org.tailfeather.acorn.Main acorn.xml
