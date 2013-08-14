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
package com.anrisoftware.sscontrol.services;

import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.resources.texts.api.Texts;
import com.anrisoftware.sscontrol.app.TextsProvider;
import com.anrisoftware.sscontrol.core.api.ServiceException;

/**
 * Logging messages for {@link ServiceLoad}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class ServiceLoadLogger extends AbstractLogger {

	private enum _ {

		SERVICE_FILES_NOT_FOUND2("service_files_not_found_message"),

		SERVICE_FILES_NOT_FOUND("service_files_not_found"),

		SERVICE_FILES_SERVICE2("service_files_service_message"),

		SERVICE_FILES_PATTERN("service_files_pattern"),

		SERVICE_NAME("service_name"),

		SERVICE_FILES_SERVICE("service_files_service");

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
	 * Create logger for {@link ServiceLoad}.
	 */
	@Inject
	ServiceLoadLogger() {
		super(ServiceLoad.class);
	}

	@Inject
	void setTexts(TextsProvider provider) {
		_.retrieveResources(provider.get());
	}

	ServiceException serviceFileNotContainService(String name, Pattern pattern) {
		return logException(
				new ServiceException(_.SERVICE_FILES_SERVICE).add(
						_.SERVICE_NAME, name).add(_.SERVICE_FILES_PATTERN,
						pattern), _.SERVICE_FILES_SERVICE2, name);
	}

	ServiceException noServiceFilesFound(String name, Pattern pattern) {
		return logException(new ServiceException(_.SERVICE_FILES_NOT_FOUND)
				.add(_.SERVICE_NAME, name)
				.add(_.SERVICE_FILES_PATTERN, pattern),
				_.SERVICE_FILES_NOT_FOUND2, name);
	}
}
