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
package com.anrisoftware.sscontrol.app;

import static org.joda.time.Duration.standardSeconds;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.inject.Inject;

import com.anrisoftware.globalpom.threads.api.Threads;
import com.anrisoftware.globalpom.threads.api.ThreadsException;
import com.anrisoftware.globalpom.threads.properties.PropertiesThreads;
import com.anrisoftware.globalpom.threads.properties.PropertiesThreadsFactory;
import com.anrisoftware.sscontrol.appmodel.AppModel;
import com.anrisoftware.sscontrol.appmodel.ModelException;
import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServicesRegistry;
import com.anrisoftware.sscontrol.core.service.ThreadsPropertiesProvider;
import com.anrisoftware.sscontrol.filesystem.FileSystem;
import com.anrisoftware.sscontrol.filesystem.FileSystemException;
import com.anrisoftware.sscontrol.parser.AppParser;
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
    private AppParser parser;

    @Inject
    private ServicesRegistry registry;

    @Inject
    private FileSystem fileSystem;

    @Inject
    private ProfileSearch profileSearch;

    @Inject
    private ServiceLoad serviceLoad;

    @Inject
    private ThreadsPropertiesProvider threadsPropertiesProvider;

    @Inject
    private PropertiesThreadsFactory threadsFactory;

    private Threads threads;

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
        threads = createThreads();
        loadServices();
        startServices();
        stopThreads();
    }

    private Threads createThreads() throws AppException {
        String threadsName = "script";
        try {
            PropertiesThreads threads = threadsFactory.create();
            threads.setProperties(threadsPropertiesProvider.get());
            threads.setName(threadsName);
            return threads;
        } catch (ThreadsException e) {
            throw log.errorCreateThreads(e, threadsName);
        }
    }

    private void stopThreads() throws AppException {
        try {
            List<Future<?>> tasks = threads.waitForTasks(standardSeconds(1));
            for (Future<?> future : tasks) {
                log.owerdueTasks(future);
                future.cancel(true);
            }
        } catch (InterruptedException e) {
            throw log.errorWaitForTasks(e);
        }
        threads.shutdown();
        log.stoppedThreads();
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
            log.startService(service);
            String name = service.getName();
            service.call();
            log.finishService(name);
        } catch (Exception e) {
            throw log.errorStartService(e, service);
        }
    }

    private void loadServices() throws AppException {
        serviceLoad.setFileSystem(fileSystem);
        serviceLoad.setRegistry(registry);
        serviceLoad.setThreads(threads);
        for (String name : profile.getEntryNames()) {
            if ("system".equals(name)) {
                continue;
            }
            if (!model.containsService(name)) {
                continue;
            }
            try {
                Map<String, Object> variables = model.getScriptVariables();
                serviceLoad.loadService(name, profile, variables);
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
        ProfileService profile = null;
        profileSearch.setFileSystem(fileSystem);
        profileSearch.setRegistry(registry);
        profileSearch.setThreads(threads);
        try {
            profile = profileSearch.searchProfile(name, variables);
        } catch (FileSystemException e) {
            throw log.errorSearchProfile(name, e);
        } catch (ServiceException e) {
            log.errorLoadProfile(name, e);
        }
        if (profile == null) {
            throw log.noProfileFound(model);
        }
        return profile;
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
