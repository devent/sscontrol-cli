#!/bin/sh
#
# Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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


symlink=`find "$0" -printf "%l"`
cd "`dirname "${symlink:-$0}"`"

export _JAVA_OPTIONS="-Dawt.useSystemAAFontSettings=on"
mainjar="../../lib/${project.custom.jarfile}"
log="-Dlogback.configurationFile=file:///$PWD/../../etc/logback-debug.xml"

java "$log" -jar "$mainjar" $*
