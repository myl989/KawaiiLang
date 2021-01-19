#!/bin/bash

#Todo: update jar file to correct jar file (note that KawaiiLang class doesn't exist yet)
echo -n "Please enter a file location: "
read fileLocation
java -Xmx512M&nbsp; -jar KawaiiLang.jar $fileLocation