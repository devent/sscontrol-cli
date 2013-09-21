/*
 * Copyright 2013 Erwin Müller <erwin.mueller@deventm.org>
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
package postfix_mysql.ubuntu_10_04

mail {
	debug logging: 2
	bind_addresses all
	name "mail.ubuntutest.com"
	origin "ubuntutest.com"
	database "maildb", user: "mail", password: "mailpassword"
	reset domains: yes

	masquerade {
		domains "mail.ubuntutest.com"
		users "root"
	}

	domain "localhost.localdomain", { catchall destination: "@localhost" }
	domain "localhost", {
		alias "postmaster", destination: "admin@ubuntutest.com"
		alias "sysadmin", destination: "admin@ubuntutest.com"
		alias "webmaster", destination: "admin@ubuntutest.com"
		alias "abuse", destination: "admin@ubuntutest.com"
		alias "root", destination: "admin@ubuntutest.com"
	}

	domain "admin.ubuntutest.com", { catchall destination: "@ubuntutest.com" }
	domain "ubuntutest.com", {
		user "admin", password: "admin12"
		user "devent", password: "devent12"
		user "userfoo", password: "foo12"
		alias "postmaster", destination: "admin"
		alias "sysadmin", destination: "admin"
		alias "webmaster", destination: "admin"
		alias "abuse", destination: "admin"
	}
}
