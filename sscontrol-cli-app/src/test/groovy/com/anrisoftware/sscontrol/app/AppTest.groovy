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
package com.anrisoftware.sscontrol.app

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.resources.Resources.*
import static org.apache.commons.io.FileUtils.*

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see App
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AppTest {

    @Test
    void "invalid arguments"() {
        String[] args = []
        App app = injector.getInstance App
        shouldFailWith AppException, { app.start(args) }
    }

    @Test
    void "profile not found"() {
        copyURLToFile hostnameUbuntuProfile, new File(tmpdir, "Ubuntu_10_04Profile.groovy")
        String profile = "notdefined"
        String[] args = [
            "-scripts",
            "file://${tmpdir.absolutePath}",
            "-profile",
            profile,
            "-server",
            localServer,
        ]
        App app = injector.getInstance App
        shouldFailWith AppException, { app.start(args) }
    }

    @Test
    void "no service file"() {
        copyURLToFile hostnameUbuntuProfile, new File(tmpdir, "Ubuntu_10_04Profile.groovy")
        String profile = "ubuntu_10_04"
        String[] args = [
            "-scripts",
            "file://${tmpdir.absolutePath}",
            "-profile",
            profile,
            "-server",
            localServer,
        ]
        App app = injector.getInstance App
        shouldFailWith(AppException) { app.start args }
    }

    @Test
    void "hostname, hosts service"() {
        copyURLToFile hostsHostnameUbuntuProfile, new File(tmpdir, "Ubuntu_10_04Profile.groovy")
        copyURLToFile hostnameScript, new File(tmpdir, "Hostname.groovy")
        copyURLToFile hostsScript, new File(tmpdir, "Hosts.groovy")
        copyResourceToCommand installCommand, new File(tmpdir, "/usr/bin/aptitude")
        copyResourceToCommand hostnameRestartCommand, new File(tmpdir, "/etc/init.d/hostname")

        String profile = "ubuntu_10_04"
        String[] args = [
            "-scripts",
            "file://${tmpdir.absolutePath}",
            "-profile",
            profile,
            "-server",
            localServer,
            "-variables",
            "prefix=$variables.prefix"
        ]
        App app = injector.getInstance App
        app.start args
        assertFileContent hostsFile, hostsExpected
        assertFileContent aptitudeOut, aptitudeOutExpected
        assertFileContent hostnameRestartOut, hostnameRestartOutExpected
    }

    @Test
    void "hostname, hosts service from archive zip"() {
        copyURLToFile profilesZip, profilesZipTmp
        copyResourceToCommand installCommand, new File(tmpdir, "/usr/bin/aptitude")
        copyResourceToCommand hostnameRestartCommand, new File(tmpdir, "/etc/init.d/hostname")

        String profile = "ubuntu_10_04"
        String[] args = [
            "-scripts",
            "file://${profilesZipTmp.absolutePath}",
            "-profile",
            profile,
            "-server",
            localServer,
            "-variables",
            "prefix=$variables.prefix"
        ]
        App app = injector.getInstance App
        app.start args
        assertFileContent hostsFile, hostsExpected
        assertFileContent aptitudeOut, aptitudeOutExpected
        assertFileContent hostnameRestartOut, hostnameRestartOut
    }

    @Test
    void "hostname, not hosts service from archive zip"() {
        copyURLToFile profilesZip, profilesZipTmp
        copyResourceToCommand installCommand, new File(tmpdir, "/usr/bin/aptitude")
        copyResourceToCommand hostnameRestartCommand, new File(tmpdir, "/etc/init.d/hostname")

        String profile = "ubuntu_10_04"
        String[] args = [
            "-scripts",
            "file://${profilesZipTmp.absolutePath}",
            "-profile",
            profile,
            "-server",
            localServer,
            "-variables",
            "prefix=$variables.prefix",
            "-services",
            "hostname"
        ]
        App app = injector.getInstance App
        app.start args
        assert !hostsFile.exists()
        assertFileContent aptitudeOut, aptitudeOutExpected
        assertFileContent hostnameRestartOut, hostnameRestartOut
    }

    @Test
    void "not hostname, hosts service from archive zip"() {
        copyURLToFile profilesZip, profilesZipTmp
        copyResourceToCommand installCommand, new File(tmpdir, "/usr/bin/aptitude")
        copyResourceToCommand hostnameRestartCommand, new File(tmpdir, "/etc/init.d/hostname")

        String profile = "ubuntu_10_04"
        String[] args = [
            "-scripts",
            "file://${profilesZipTmp.absolutePath}",
            "-profile",
            profile,
            "-server",
            localServer,
            "-variables",
            "prefix=$variables.prefix",
            "-services",
            "!hostname"
        ]
        App app = injector.getInstance App
        app.start args
        assertFileContent hostsFile, hostsExpected
        assert !hostnameRestartOut.exists()
    }

    static Injector injector

    static String localServer = "127.0.0.1"

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder()

    File tmpdir

    File hostsFile

    File aptitudeOut

    File hostnameRestartOut

    File profilesZipTmp

    Map variables = [:]

    @BeforeClass
    static void createInjector() {
        injector = Guice.createInjector(new AppModule())
    }

    @Before
    void createTemp() {
        tmpdir = tmp.newFolder()
        hostsFile = new File(tmpdir, "etc/hosts")
        aptitudeOut = new File(tmpdir, "usr/bin/aptitude.out")
        hostnameRestartOut = new File(tmpdir, "etc/init.d/hostname.out")
        profilesZipTmp = new File(tmpdir, "profiles.zip")
        variables.prefix = tmpdir.absolutePath
    }

    @BeforeClass
    static void setupToStringStyle() {
        toStringStyle
    }
}
