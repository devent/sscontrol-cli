/*
 * Copyright 2013-2014 Erwin Müller <erwin.mueller@deventm.org>
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
package com.anrisoftware.sscontrol.filesystem

import static com.anrisoftware.globalpom.utils.TestUtils.*
import static com.anrisoftware.sscontrol.resources.Resources.*
import static org.apache.commons.io.FileUtils.*
import groovy.util.logging.Slf4j

import java.util.regex.Pattern

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.sscontrol.core.modules.CoreResourcesModule
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see FileSystem
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Slf4j
class FileSystemTest {

	@Test
	void "list directory"() {
		copyURLToFile hostnameUbuntuProfile, new File(tmpdir, "Ubuntu_10_04Profile.groovy")

		FileSystem system = injector.getInstance FileSystem
		system.addLocation tmpdir.toURI().toURL()
		def files = system.findFiles PATTERN
		assert files.size() == 1
		log.info "Found profile in directory: {}", files
	}

	@Test
	void "list targz"() {
		copyURLToFile profilesTarGz, profilesTarGzTmp

		FileSystem system = injector.getInstance FileSystem
		system.addLocation profilesTarGzTmp.toURI().toURL()
		def files = system.findFiles PATTERN
		assert files.size() == 1
		log.info "Found profile in tar gz: {}", files
	}

	@Test
	void "list zip"() {
		copyURLToFile profilesZip, profilesZipTmp

		FileSystem system = injector.getInstance FileSystem
		system.addLocation profilesZipTmp.toURI().toURL()
		def files = system.findFiles PATTERN
		assert files.size() == 1
		log.info "Found profile in zip: {}", files
	}

	@Test
	void "list jar"() {
		copyURLToFile profilesJar, profilesJarTmp

		FileSystem system = injector.getInstance FileSystem
		system.addLocation profilesJarTmp.toURI().toURL()
		def files = system.findFiles PATTERN
		assert files.size() == 1
		log.info "Found profile in jar: {}", files
	}

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder()

	static Injector injector

	static Pattern PATTERN = Pattern.compile(/.+Profile\.\w+$/)

	File tmpdir

	File profilesZipTmp

	File profilesTarGzTmp

	File profilesJarTmp

	@BeforeClass
	static void createInjector() {
		injector = Guice.createInjector(new FileSystemModule(), new CoreResourcesModule())
	}

	@Before
	void createTemp() {
		tmpdir = tmp.newFolder()
		profilesTarGzTmp = new File(tmpdir, "profiles.tar.gz")
		profilesZipTmp = new File(tmpdir, "profiles.zip")
		profilesJarTmp = new File(tmpdir, "profiles.jar")
	}
}
