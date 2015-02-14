/*
 * Copyright 2013-2015 Erwin Müller <erwin.mueller@deventm.org>
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

import java.net.URL;

import javax.inject.Inject;

import org.apache.commons.vfs2.FileObject;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.resources.texts.api.Texts;
import com.anrisoftware.sscontrol.app.TextsProvider;

import eu.medsea.mimeutil.MimeException;

/**
 * Logging messages for {@link FileSystem}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class FileSystemLogger extends AbstractLogger {

	private enum _ {

		ERROR_GET_URL("error_get_url"),

		ERROR_GET_URL2("error_get_url_message"),

		ERROR_LIST_FILES("error_list_files"),

		ERROR_LIST_FILES1("error_list_files_message"),

		NO_FILES_FOUND2("no_files_found_message"),

		NO_FILES_FOUND("no_files_found"),

		ERROR_MIME_TYPE_MESSAGE("error_mime_type_message"),

		ERROR_MIME_TYPE("error_mime_type"),

		ERROR_RESOLVE_MESSAGE("error_resolve_message"),

		ERROR_RESOLVE("error_resolve"),

		LOCATION("location"),

		NO_MIME_TYPE_MESSAGE("no_mime_type_message"),

		NO_MIME_TYPE_RETURNED("no_mime_type_returned"),

		FILE("file");

		public static void retrieveResources(Texts texts) {
			for (_ value : values()) {
				value.setText(texts);
			}
		}

		private String name;

		private String text;

		private _(String name) {
			this.name = name;
		}

		public void setText(Texts texts) {
			this.text = texts.getResource(name).getText();
		}

		@Override
		public String toString() {
			return text;
		}
	}

	/**
	 * Create logger for {@link FileSystem}.
	 */
	FileSystemLogger() {
		super(FileSystem.class);
	}

	@Inject
	void setTexts(TextsProvider provider) {
		_.retrieveResources(provider.get());
	}

	FileSystemException resolveFileError(URL url,
			org.apache.commons.vfs2.FileSystemException e) {
		return logException(
				new FileSystemException(_.ERROR_RESOLVE).add(_.LOCATION, url),
				_.ERROR_RESOLVE_MESSAGE, url);
	}

	FileSystemException getMimeError(URL url, MimeException e) {
		return logException(
				new FileSystemException(_.ERROR_MIME_TYPE).add(_.LOCATION, url),
				_.ERROR_MIME_TYPE_MESSAGE, url);
	}

	FileSystemException noMimeTypeReturned(URL url) {
		return logException(
				new FileSystemException(_.NO_MIME_TYPE_RETURNED).add(
						_.LOCATION, url), _.NO_MIME_TYPE_MESSAGE, url);
	}

	FileSystemException errorListFiles(FileObject location,
			org.apache.commons.vfs2.FileSystemException e) {
		return logException(new FileSystemException(_.ERROR_LIST_FILES, e).add(
				_.LOCATION, location), _.ERROR_LIST_FILES1, location);
	}

	FileSystemException getURLError(FileObject file,
			org.apache.commons.vfs2.FileSystemException e) {
		return logException(
				new FileSystemException(_.ERROR_GET_URL, e).add(_.FILE, file),
				_.ERROR_GET_URL2, file);
	}

	void checkFiles(FileObject[] files, FileObject location)
			throws FileSystemException {
		if (files == null) {
			throw logException(new FileSystemException(_.NO_FILES_FOUND).add(
					_.LOCATION, location), _.NO_FILES_FOUND2, location);
		}
	}
}
