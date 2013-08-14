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

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Map;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.appmodel.AppModel;
import com.anrisoftware.sscontrol.appmodel.ModelException;
import com.anrisoftware.sscontrol.cli.CliParser;
import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServicesRegistry;
import com.anrisoftware.sscontrol.filesystem.FileSystem;
import com.anrisoftware.sscontrol.filesystem.FileSystemException;
import com.anrisoftware.sscontrol.services.ProfileSearch;
import com.anrisoftware.sscontrol.services.ServiceLoad;

/**
 * Parse command line arguments, search for specified profile and deploy the
 * service scripts.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class App {

	@Inject
	private AppLogger log;

	@Inject
	private CliParser parser;

	@Inject
	private ServicesRegistry registry;

	@Inject
	private FileSystem fileSystem;

	@Inject
	private ProfileSearch profileSearch;

	@Inject
	private ServiceLoad serviceLoad;

	private AppModel model;

	private ProfileService profile;

	/**
	 * Parses the command line arguments and starts the services.
	 * 
	 * @param args
	 *            the command line arguments.
	 * 
	 * @throws AppException
	 *             if an error occurred that causes the application to
	 *             terminate.
	 */
	public void start(String[] args) throws AppException {
		model = parseArguments(args);
		setupLocations();
		profile = searchProfile();
		if (profile == null) {
			throw log.noProfileFound(model);
		}
		loadServices();
		startServices();
	}

	private void startServices() throws AppException {
		for (String name : profile.getEntryNames()) {
			if (!registry.haveService(name)) {
				continue;
			}
			for (Service service : registry.getService(name)) {
				startService(service);
			}
		}
	}

	private void startService(Service service) throws AppException {
		try {
			String name = service.getName();
			service.call();
			log.finishService(name);
		} catch (Exception e) {
			throw log.errorStartService(e, service);
		}
	}

	private void loadServices() throws AppException {
		for (String name : profile.getEntryNames()) {
			if ("system".equals(name)) {
				continue;
			}
			try {
				Map<String, Object> variables = model.getScriptVariables();
				serviceLoad.loadService(name, fileSystem, registry, profile,
						variables);
			} catch (FileSystemException e) {
				throw log.errorSearchService(name, e);
			} catch (ServiceException e) {
				throw log.errorLoadService(name, e);
			}
		}
	}

	private ProfileService searchProfile() throws AppException {
		String name = model.getProfile();
		Map<String, Object> variables = model.getScriptVariables();
		try {
			return profileSearch.searchProfile(name, fileSystem, registry,
					variables);
		} catch (FileSystemException e) {
			throw log.errorSearchProfile(name, e);
		} catch (ServiceException e) {
			log.errorLoadProfile(name, e);
			return null;
		}
	}

	private void setupLocations() throws AppException {
		for (URI location : model.getScriptsLocations()) {
			try {
				fileSystem.addLocation(location.toURL());
			} catch (FileSystemException e) {
				throw log.errorAddLocation(location, e);
			} catch (MalformedURLException e) {
				throw log.malformedLocation(location, e);
			}
		}
	}

	private AppModel parseArguments(String[] args) throws AppException {
		try {
			return parser.parse(args);
		} catch (ModelException e) {
			throw log.errorParseArguments(args, e);
		}
	}

}
