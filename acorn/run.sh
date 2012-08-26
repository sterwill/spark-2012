#!/bin/sh

OLD_STTY=`stty --save`

# unset ^D
stty eof ""
# unset ^Z
stty susp ""
# unset ^C
stty intr ""
# unset ^\
stty quit ""

java -cp bin org.tailfeather.acorn.Acorn

stty $OLD_STTY
