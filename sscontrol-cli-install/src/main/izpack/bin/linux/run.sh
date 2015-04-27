#!/bin/bash
#
# Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
#
# This file is part of sscontrol-cli-install.
#
# sscontrol-cli-install is free software: you can redistribute it and/or modify it
# under the terms of the GNU Affero General Public License as published by the
# Free Software Foundation, either version 3 of the License, or (at your
# option) any later version.
#
# sscontrol-cli-install is distributed in the hope that it will be useful, but
# WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
# FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
# for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with sscontrol-cli-install. If not, see <http://www.gnu.org/licenses/>.
#

## change the directory to the start directory of the application.
function changeBinDirectory() {
    symlink=`find "$0" -printf "%l"`
    cd "`dirname "${symlink:-$0}"`"
}

## Returns the no-java-runtime text based on the current locale.
## It is expected to find the text in a file under:
##     etc/[LANG]/no_java_runtime.txt
function noJavaRuntime() {
    file="../../etc/${lang[0]}/no_java_runtime.txt"
    if [ ! -f $file ]; then
        file="../../etc/en_US/no_java_runtime.txt"
    fi
    if [ -f $file ]; then
        noJavaRuntimeText="`cat $file`"
    fi
}

## Checks that the Java runtime is installed and shows an error dialog if
## it is not the case.
function checkJavaRuntime() {
    if [ -z "$javaCommand" ]; then
        noJavaRuntime
        type zenity >/dev/null 2>&1
        if [ $? = 0 ]; then
            zenity --error --text="$noJavaRuntimeText"
        else
            echo "$noJavaRuntimeText"
        fi
        exit 1
    fi
}

currentDir=$(pwd)
changeBinDirectory
javaCommand=`type -P java`
mainClass="${project.custom.app.mainclass}"
lib="../../lib/*"
IFS='.' read -a lang <<< "$LANG"
log="-Dlogback.configurationFile=file:///$PWD/../../etc/logback.xml"
logArgs="-Dproject.custom.log.prefix=$currentDir"
args=""
noJavaRuntimeText="No Java Runtime found."
checkJavaRuntime
export _JAVA_OPTIONS="-Dawt.useSystemAAFontSettings=on"
"$javaCommand" "$logArgs" "$log" -cp "$lib" "$mainClass" $args $*
