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

httpd {
	domain "ubuntutest.com", address: "192.168.0.100", {
		redirect to_www
		redirect http_to_https
	}
	ssl_domain "ubuntutest.com", address: "192.168.0.100", {
		certification_file certCrt.resource
		certification_key_file certKey.resource
		redirect to_www
		setup_auth provider: file, name: "private", {
			location "private"
			require valid_user
			require group: "admin"
			group "admin", {
				user "adminfoo", password: "adminfoopassword"
				user "adminbar", password: "adminbarpassword"
				user "adminbaz", password: "adminbazpassword"
			}
			user "foo", password: "foopassword"
			user "bar", password: "barpassword"
			user "baz", password: "bazpassword"
		}
	}
}
