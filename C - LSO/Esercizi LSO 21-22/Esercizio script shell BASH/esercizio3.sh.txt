#!/bin/bash
for arg in $@
do
cat  $arg|tr -d '\n'|sed 's:\/\*:\n\/\*:g'|sed 's:\*\/:\*\/\n:g'|sed 's:\/\*.*\*\/::g'>$(echo $arg|sed 's:\.c$:-nocomment.c:g')
done
