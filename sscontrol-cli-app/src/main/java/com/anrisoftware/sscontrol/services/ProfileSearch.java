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
package com.anrisoftware.sscontrol.services;

import static java.util.regex.Pattern.compile;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.inject.Inject;

import com.anrisoftware.globalpom.threads.api.Threads;
import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.Service;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServicesRegistry;
import com.anrisoftware.sscontrol.filesystem.FileSystem;
import com.anrisoftware.sscontrol.filesystem.FileSystemException;

/**
 * Search a profile script in the file system.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class ProfileSearch {

    private static final String PROFILE_NAME = "profile";

    public final static Pattern PROFILE_FILE_PATTERN = compile(".+Profile\\.\\w+$");

    @Inject
    private ServiceLoad serviceLoad;

    private ServicesRegistry registry;

    public void setFileSystem(FileSystem fileSystem) {
        serviceLoad.setFileSystem(fileSystem);
    }

    public void setRegistry(ServicesRegistry registry) {
        this.registry = registry;
        serviceLoad.setRegistry(registry);
    }

    public void setThreads(Threads threads) {
        serviceLoad.setThreads(threads);
    }

    /**
     * Search the profile script in the file system.
     * 
     * @param profileName
     *            the name of the profile.
     * 
     * @return the found {@link ProfileService} profile or {@code null} if no
     *         such profile was found.
     * 
     * @throws FileSystemException
     *             if there was error searching the profile in the file system.
     * 
     * @throws ServiceException
     *             if there was error loading the profile script.
     */
    public ProfileService searchProfile(String profileName)
            throws FileSystemException, ServiceException {
        return searchProfile(profileName,
                Collections.<String, Object> emptyMap());
    }

    /**
     * Search the profile script in the file system.
     * 
     * @param name
     *            the name of the profile.
     * 
     * @param variables
     *            a {@link Map} of variables to pass to the profile script.
     * 
     * @return the found {@link ProfileService} profile or {@code null} if no
     *         such profile was found.
     * 
     * @throws FileSystemException
     *             if there was error searching the profile in the file system.
     * 
     * @throws ServiceException
     *             if there was error loading the profile script.
     */
    public ProfileService searchProfile(String name,
            Map<String, Object> variables) throws FileSystemException,
            ServiceException {
        serviceLoad.setFilePattern(PROFILE_FILE_PATTERN);
        serviceLoad.loadService(PROFILE_NAME, null, variables);
        List<Service> profiles = registry.getService(PROFILE_NAME);
        for (Service service : profiles) {
            ProfileService profile = (ProfileService) service;
            if (profile.getProfileName().equals(name)) {
                return profile;
            }
        }
        return null;
    }
}
