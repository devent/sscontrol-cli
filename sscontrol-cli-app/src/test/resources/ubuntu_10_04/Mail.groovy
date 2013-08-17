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
package ubuntu_10_04

mail {
	bind_addresses all

	name "mail.ubuntutest.com"
	origin "ubuntutest.com"
	database "maildb" user "mail" password "mailpassword"

	masquerade {
		domains "mail.ubuntutest.com"
		users "root"
	}

	domain "localhost.localdomain", { catchall destination: "@localhost" }

	domain "localhost", {
		alias "postmaster", destination: "root"
		alias "sysadmin", destination: "root"
		alias "webmaster", destination: "root"
		alias "abuse", destination: "root"
		alias "root", destination: "root"
		catchall destination: "root"
		user "root", password: "rootpasswd"
	}

	domain "ubuntutest.com", {
		user "devent", password: "devent12"
		user "userfoo", password: "foo12"
	}
}
