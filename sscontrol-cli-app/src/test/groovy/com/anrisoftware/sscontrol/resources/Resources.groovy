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
package com.anrisoftware.sscontrol.resources

/**
 * Loads test resources.
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class Resources {

    public static final URL hostnameUbuntuProfile = Resources.class.getResource("HostnameUbuntuProfile.groovy")

    public static final URL hostsHostnameUbuntuProfile = Resources.class.getResource("HostsHostnameUbuntuProfile.groovy")

    public static final URL profilesTarGz = Resources.class.getResource("profiles.tar.gz")

    public static final URL profilesZip = Resources.class.getResource("profiles.zip")

    public static final URL profilesJar = Resources.class.getResource("profiles.jar")

    public static final URL hostnameSecondZip = Resources.class.getResource("HostnameSecond.zip")

    public static final URL hostnameScript = Resources.class.getResource("Hostname.groovy")

    public static final URL hostsScript = Resources.class.getResource("Hosts.groovy")

    public static final URL installCommand = Resources.class.getResource("echo_command.txt")

    public static final URL restartCommand = Resources.class.getResource("echo_command.txt")

    public static final URL hostnameRestartCommand = Resources.class.getResource("echo_command.txt")

    public static final URL defaultHosts = Resources.class.getResource("hosts.txt")

    public static final URL hostsExpected = Resources.class.getResource("hosts_expected.txt")

    public static final URL aptitudeOutExpected = Resources.class.getResource("aptitude_out_expected.txt")

    public static final URL aptitudeOutSecondExpected = Resources.class.getResource("aptitude_out_second_expected.txt")

    public static final URL hostnameRestartOutExpected = Resources.class.getResource("hostname_restart_out_expected.txt")
}
