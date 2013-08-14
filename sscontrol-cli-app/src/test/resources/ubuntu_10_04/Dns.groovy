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
dns {
	serial 1
	bind_address "127.0.0.1"

	// recursive servers
	alias "localhost" address "127.0.0.1"
	roots { servers "icann" }
	recursive { servers "localhost" }

	// first zone
	zone "ubuntutest.com", "ns1.%", "hostmaster@%", "127.0.0.1", {
		mx_record "mx1.%", "127.0.0.1"
		cname_record "www.%", "%"
	}
}
