/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
 *
 * This file is part of sscontrol-cli-app.
 *
 * sscontrol-cli-app is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * sscontrol-cli-app is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with sscontrol-cli-app. If not, see <http://www.gnu.org/licenses/>.
 */
package com.anrisoftware.sscontrol.resources

def aptitude = "/usr/bin/aptitude"
def restart = "/sbin/restart"

profile "ubuntu_12_04", {
	hostname {
		install_command "$prefix$aptitude"
		restart_command "${prefix}/etc/init.d/hostname restart"
		configuration_directory "$prefix/etc"
	}
	hosts {
		install_command "$prefix$aptitude"
		configuration_directory "$prefix/etc"
	}
}
