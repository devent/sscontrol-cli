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
package openldap.ubuntu_10_04

ldap {
	organization "ubuntutest", domain: "ubuntutest.com", description: "My LDAP Root", {
		admin "admin", password: "adminpass", description: "LDAP Administrator"
	}
	script """# LDIF Export for o=deventorg,dc=ubuntutest,dc=com
# Server: My LDAP Server (127.0.0.1)
# Search Scope: sub
# Search Filter: (objectClass=*)
# Total Entries: 6
#
# Generated by phpLDAPadmin (http://phpldapadmin.sourceforge.net) on September 6, 2013 2:31 am
# Version: 1.2.3

version: 1

# Entry 1: o=deventorg,dc=ubuntutest,dc=com
dn: o=deventorg,dc=ubuntutest,dc=com
description: Devent Organization
o: deventorg
objectclass: organization
objectclass: top

# Entry 2: cn=ldapadmin,o=deventorg,dc=ubuntutest,dc=com
dn: cn=ldapadmin,o=deventorg,dc=ubuntutest,dc=com
cn: ldapadmin
description: LDAP Directory Administrator
objectclass: organizationalRole
objectclass: top

# Entry 3: cn=ldapadminGroup,o=deventorg,dc=ubuntutest,dc=com
dn: cn=ldapadminGroup,o=deventorg,dc=ubuntutest,dc=com
cn: ldapadminGroup
o: deventorg
objectclass: groupOfUniqueNames
objectclass: top
uniquemember: cn=foo,ou=TechnicalAdmin,o=deventorg,dc=ubuntutest,dc=com

# Entry 4: ou=phpldapadmin,o=deventorg,dc=ubuntutest,dc=com
dn: ou=phpldapadmin,o=deventorg,dc=ubuntutest,dc=com
associateddomain: phpldapadmin
objectclass: domainRelatedObject
objectclass: organizationalUnit
objectclass: top
ou: phpldapadmin

# Entry 5: ou=TechnicalAdmin,o=deventorg,dc=ubuntutest,dc=com
dn: ou=TechnicalAdmin,o=deventorg,dc=ubuntutest,dc=com
description: Technical Administrator.
objectclass: organizationalUnit
objectclass: top
ou: TechnicalAdmin

# Entry 6: cn=foo,ou=TechnicalAdmin,o=deventorg,dc=ubuntutest,dc=com
dn: cn=foo,ou=TechnicalAdmin,o=deventorg,dc=ubuntutest,dc=com
cn: foo
mail: foo@ubuntutest.com
o: deventorg
objectclass: inetOrgPerson
objectclass: top
ou: TechnicalAdmin
ou: phpldapadmin
sn: Foo
userpassword: {SSHA}v4Je0pxNDam0q9ZR6JpSaz0lhmaj8BnJ

"""
}
