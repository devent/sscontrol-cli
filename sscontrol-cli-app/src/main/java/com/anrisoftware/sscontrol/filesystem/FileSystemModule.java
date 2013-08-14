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
package com.anrisoftware.sscontrol.filesystem;

import java.io.IOException;
import java.net.URL;

import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;

import com.anrisoftware.propertiesutils.ContextProperties;
import com.anrisoftware.propertiesutils.ContextPropertiesFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import eu.medsea.mimeutil.MimeUtil2;

public class FileSystemModule extends AbstractModule {

	private static final URL FILE_SYSTEMS_RESOURCE = FileSystemModule.class
			.getResource("/filesystems.properties");

	@Override
	protected void configure() {
	}

	@Provides
	@Singleton
	MimeUtil2 getMimeUtil() {
		MimeUtil2 util = new MimeUtil2();
		util.registerMimeDetector("eu.medsea.mimeutil.detector.ExtensionMimeDetector");
		return util;
	}

	@Provides
	@Singleton
	FileSystemManager getFileSystemManager() throws FileSystemException {
		return VFS.getManager();
	}

	@Provides
	@Named("filesystems-properties")
	@Singleton
	ContextProperties getFileSystemsProperties() throws IOException {
		return new ContextPropertiesFactory(FileSystem.class)
				.fromResource(FILE_SYSTEMS_RESOURCE);
	}
}
