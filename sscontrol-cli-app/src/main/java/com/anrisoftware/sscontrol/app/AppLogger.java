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
package com.anrisoftware.sscontrol.app;

import static com.anrisoftware.sscontrol.app.AppLogger._.start_service;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.resources.texts.api.Texts;
import com.anrisoftware.sscontrol.appmodel.AppModel;
import com.anrisoftware.sscontrol.appmodel.ModelException;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.filesystem.FileSystemException;

/**
 * Logging messages for {@link App}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class AppLogger extends AbstractLogger {

    enum _ {

		ERROR_LOAD_SERVICE2("error_load_service_message"),

		ERROR_LOAD_SERVICE("error_load_service"),

		ERROR_SEARCH_SERVICE2("error_search_service_message"),

		ERROR_SEARCH_SERVICE("error_search_service"),

		SERVICE_NAME("service_name"),

		NO_PROFILE2("no_profile_message"),

		LOCATIONS("locations"),

		NO_PROFILE("no_profile"),

		ARGUMENTS("arguments"),

		ERROR_PARSE_ARGUMENTS2("error_parse_arguments_message"),

		ERROR_PARSE_ARGUMENTS("error_parse_arguments"),

		ERROR_LOAD_PROFILE2("error_load_profile_message"),

		ERROR_LOAD_PROFILE("error_load_profile"),

		PROFILE_NAME("profile_name"),

		ERROR_SEARCH_PROFILE2("error_search_profile_message"),

		ERROR_SEARCH_PROFILE("error_search_profile"),

		LOCATION("location"),

		LOCATION_URL2("location_url_message"),

		LOCATION_URL("location_url"),

		ERROR_START_SERVICE("error_start_service"),

		ERROR_START_SERVICE2("error_start_service_message"),

		ERROR_ADD_LOCATION("error_add_location"),

		ERROR_ADD_LOCATION2("error_add_location_message"),

		FINSISH_SERVICE("finsish_service"),

        SERVICE("service"),

        start_service("start_service");

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
	 * Create logger for {@link App}.
	 */
	public AppLogger() {
		super(App.class);
	}

	@Inject
	void setTexts(TextsProvider provider) {
		_.retrieveResources(provider.get());
	}

	AppException errorSearchProfile(String name, FileSystemException e) {
		return logException(new AppException(_.ERROR_SEARCH_PROFILE, e).add(
				_.PROFILE_NAME, name), _.ERROR_SEARCH_PROFILE2, name);
	}

	ServiceException errorLoadProfile(String name, ServiceException e) {
		return logException(e, _.ERROR_LOAD_PROFILE2, name);
	}

	AppException errorParseArguments(String[] args, ModelException e) {
		String argsstr = Arrays.toString(args);
		return logException(new AppException(_.ERROR_PARSE_ARGUMENTS, e).add(
				_.ARGUMENTS, argsstr), _.ERROR_PARSE_ARGUMENTS2, argsstr);
	}

	AppException noProfileFound(AppModel model) {
		String profile = model.getProfile();
		return logException(
				new AppException(_.NO_PROFILE).add(_.PROFILE_NAME, profile)
						.add(_.LOCATIONS, model.getScriptsLocations()),
				_.NO_PROFILE2, profile);
	}

	AppException errorSearchService(String name, FileSystemException e) {
		return logException(new AppException(_.ERROR_SEARCH_SERVICE, e).add(
				_.SERVICE_NAME, name), _.ERROR_SEARCH_SERVICE2, name);
	}

	AppException errorLoadService(String name, ServiceException e) {
		return logException(new AppException(_.ERROR_LOAD_SERVICE, e).add(
				_.SERVICE_NAME, name), _.ERROR_LOAD_SERVICE2, name);
	}

	AppException errorStartService(Exception e, Service service) {
		return logException(new AppException(_.ERROR_START_SERVICE, e).add(
				_.SERVICE, service), _.ERROR_START_SERVICE2, service.getName());
	}

	AppException errorAddLocation(URI location, FileSystemException e) {
		return logException(new AppException(_.ERROR_ADD_LOCATION, e).add(
				_.LOCATION, location), _.ERROR_ADD_LOCATION2, location);
	}

	AppException malformedLocation(URI location, MalformedURLException e) {
		return logException(
				new AppException(_.LOCATION_URL, e).add(_.LOCATION, location),
				_.LOCATION_URL2, location);
	}

	void finishService(String name) {
		log.info(_.FINSISH_SERVICE.text, name);
	}

    void startService(Service service) {
        trace(start_service, service);
    }
}
