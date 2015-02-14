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
package com.anrisoftware.sscontrol.app;

import static com.anrisoftware.sscontrol.app.AppLogger._.arguments;
import static com.anrisoftware.sscontrol.app.AppLogger._.error_add_location;
import static com.anrisoftware.sscontrol.app.AppLogger._.error_add_location_message;
import static com.anrisoftware.sscontrol.app.AppLogger._.error_create_threads;
import static com.anrisoftware.sscontrol.app.AppLogger._.error_create_threads_message;
import static com.anrisoftware.sscontrol.app.AppLogger._.error_load_profile_message;
import static com.anrisoftware.sscontrol.app.AppLogger._.error_load_service;
import static com.anrisoftware.sscontrol.app.AppLogger._.error_load_service_message;
import static com.anrisoftware.sscontrol.app.AppLogger._.error_parse_arguments;
import static com.anrisoftware.sscontrol.app.AppLogger._.error_parse_arguments_message;
import static com.anrisoftware.sscontrol.app.AppLogger._.error_search_profile;
import static com.anrisoftware.sscontrol.app.AppLogger._.error_search_profile_message;
import static com.anrisoftware.sscontrol.app.AppLogger._.error_search_service;
import static com.anrisoftware.sscontrol.app.AppLogger._.error_search_service_message;
import static com.anrisoftware.sscontrol.app.AppLogger._.error_start_service;
import static com.anrisoftware.sscontrol.app.AppLogger._.error_start_service_message;
import static com.anrisoftware.sscontrol.app.AppLogger._.error_wait;
import static com.anrisoftware.sscontrol.app.AppLogger._.error_wait_message;
import static com.anrisoftware.sscontrol.app.AppLogger._.finsish_service;
import static com.anrisoftware.sscontrol.app.AppLogger._.location_url;
import static com.anrisoftware.sscontrol.app.AppLogger._.location_url_message;
import static com.anrisoftware.sscontrol.app.AppLogger._.locations;
import static com.anrisoftware.sscontrol.app.AppLogger._.no_profile;
import static com.anrisoftware.sscontrol.app.AppLogger._.no_profile_message;
import static com.anrisoftware.sscontrol.app.AppLogger._.owerdue_task;
import static com.anrisoftware.sscontrol.app.AppLogger._.profile_name;
import static com.anrisoftware.sscontrol.app.AppLogger._.service_name;
import static com.anrisoftware.sscontrol.app.AppLogger._.start_service;
import static com.anrisoftware.sscontrol.app.AppLogger._.threads_name;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.Future;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.globalpom.threads.api.ThreadsException;
import com.anrisoftware.resources.texts.api.TextResource;
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

        error_load_service_message,

        error_load_service,

        error_search_service_message,

        error_search_service,

        service_name,

        no_profile_message,

        locations,

        no_profile,

        arguments,

        error_parse_arguments_message,

        error_parse_arguments,

        error_load_profile_message,

        error_load_profile,

        profile_name,

        error_search_profile_message,

        error_search_profile,

        location,

        location_url_message,

        location_url,

        error_start_service,

        error_start_service_message,

        error_add_location,

        error_add_location_message,

        finsish_service,

        service,

        start_service,

        error_create_threads,

        threads_name,

        error_create_threads_message,

        error_wait,

        error_wait_message,

        owerdue_task,

        stopped_threads;

        public static void retrieveResources(Texts texts) {
            for (_ value : values()) {
                value.setResource(texts.getResource(value.name()));
            }
        }

        private TextResource resource;

        public void setResource(TextResource resource) {
            this.resource = resource;
        }

        public TextResource getResource() {
            return resource;
        }

        @Override
        public String toString() {
            return resource.getText();
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
        return logException(new AppException(error_search_profile, e).add(
                profile_name, name), error_search_profile_message, name);
    }

    ServiceException errorLoadProfile(String name, ServiceException e) {
        return logException(e, error_load_profile_message, name);
    }

    AppException errorParseArguments(String[] args, ModelException e) {
        String argsstr = Arrays.toString(args);
        return logException(new AppException(error_parse_arguments, e).add(
                arguments, argsstr), error_parse_arguments_message, argsstr);
    }

    AppException noProfileFound(AppModel model) {
        String profile = model.getProfile();
        return logException(
                new AppException(no_profile).add(profile_name, profile).add(
                        locations, model.getScriptsLocations()),
                no_profile_message, profile);
    }

    AppException errorSearchService(String name, FileSystemException e) {
        return logException(new AppException(error_search_service, e).add(
                service_name, name), error_search_service_message, name);
    }

    AppException errorLoadService(String name, ServiceException e) {
        return logException(
                new AppException(error_load_service, e).add(service_name, name),
                error_load_service_message, name);
    }

    AppException errorStartService(Exception e, Service service) {
        return logException(
                new AppException(error_start_service, e).add(service, service),
                error_start_service_message, service.getName());
    }

    AppException errorAddLocation(URI location, FileSystemException e) {
        return logException(
                new AppException(error_add_location, e).add(location, location),
                error_add_location_message, location);
    }

    AppException malformedLocation(URI location, MalformedURLException e) {
        return logException(
                new AppException(location_url, e).add(location, location),
                location_url_message, location);
    }

    AppException errorCreateThreads(ThreadsException e, String name) {
        return logException(new AppException(error_create_threads, e).add(
                threads_name, name), error_create_threads_message, name);
    }

    AppException errorWaitForTasks(InterruptedException e) {
        return logException(new AppException(error_wait, e), error_wait_message);
    }

    void finishService(String name) {
        info(finsish_service, name);
    }

    void startService(Service service) {
        trace(start_service, service);
    }

    void owerdueTasks(Future<?> future) {
        debug(owerdue_task, future);
    }

    void stoppedThreads() {
        debug(_.stopped_threads);
    }

}
