#!/bin/bash
#
# Copyright 2011-2013 Erwin Müller <erwin.mueller@deventm.org>
#
# This file is part of globalpom-izpack.
#
# globalpom-izpack is free software: you can redistribute it and/or modify it
# under the terms of the GNU Lesser General Public License as published by the
# Free Software Foundation, either version 3 of the License, or (at your
# option) any later version.
#
# globalpom-izpack is distributed in the hope that it will be useful, but
# WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
# FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
# details.
#
# You should have received a copy of the GNU Lesser General Public License
# along with globalpom-izpack. If not, see <http://www.gnu.org/licenses/>.
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
        zenity --error --text="$noJavaRuntimeText"
        exit 1
    fi
}

changeBinDirectory
javaCommand=`type -P java`
mainClass="${project.custom.mainclass}"
lib="../../lib/*"
IFS='.' read -a lang <<< "$LANG"
log="-Dlogback.configurationFile=file:///$PWD/../../etc/logback-test.xml"
noJavaRuntimeText="No Java Runtime found."
checkJavaRuntime
export _JAVA_OPTIONS="-Dawt.useSystemAAFontSettings=on"
"$javaCommand" "$log" -cp "$lib" "$mainClass" $*
