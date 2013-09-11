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
package apache_authldap.ubuntu_10_04

httpd {
	domain "ubuntutest.com", address: "192.168.0.100", {
		redirect to_www
		redirect http_to_https
	}
	ssl_domain "admit.ubuntutest.com", address: "192.168.0.100", {
		certification_file "/home/devent/certs/admin.ubuntutest.com.crt"
		certification_key_file "/home/devent/certs/admin.ubuntutest.com.key.insecure"
		auth "Admin Access", location: "/admin", provider: ldap, {
			host "ldap://127.0.0.1:389", url: "o=deventorg,dc=ubuntutest,dc=com?cn"
			credentials "cn=admin,dc=ubuntutest,dc=com", password: "adminpass"
			require group: "cn=ldapadminGroup,o=deventorg,dc=ubuntutest,dc=com", { //.
				attribute "uniqueMember" //.
			}
		}
	}
}
