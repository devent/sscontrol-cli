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

import static java.lang.String.format;
import static java.util.regex.Pattern.compile;

import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.ServiceException;
import com.anrisoftware.sscontrol.core.api.ServiceLoader;
import com.anrisoftware.sscontrol.core.api.ServiceLoaderFactory;
import com.anrisoftware.sscontrol.core.api.ServicePreScript;
import com.anrisoftware.sscontrol.core.api.ServicePreScriptFactory;
import com.anrisoftware.sscontrol.core.api.ServicesRegistry;
import com.anrisoftware.sscontrol.filesystem.FileSystem;
import com.anrisoftware.sscontrol.filesystem.FileSystemException;
import com.google.inject.Injector;

/**
 * Search the file system for service files and loads them.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class ServiceLoad {

    private final static String SERVICE_FILE_PATTERN = "%s.*?\\.\\w+$";

    private final ServiceLoadLogger log;

    private final ServiceLoaderFactory serviceFactory;

    @Inject
    private Injector injector;

    @Inject
    private PreScriptServicesProvider preScriptServices;

    private Pattern filePattern;

    @Inject
    ServiceLoad(ServiceLoadLogger logger, ServiceLoaderFactory serviceFactory) {
        this.log = logger;
        this.serviceFactory = serviceFactory;
    }

    /**
     * Search the file system for the service script file and loads the service.
     * 
     * @param name
     *            the name of the service.
     * 
     * @param fileSystem
     *            the {@link FileSystem} where to search.
     * 
     * @param registry
     *            the {@link ServicesRegistry} to register the profile script.
     * 
     * @param profile
     *            the {@link ProfileService} or {@code null} if no profile is
     *            set.
     * 
     * @param variables
     *            a {@link Map} of variables that should be injected in the
     *            script. The map should contain entries
     *            {@code [<variable name>=<value>, ...]}.
     * 
     * @return {@link ServicesRegistry} registry that will contain the loaded
     *         service.
     * 
     * @throws FileSystemException
     *             if there was error searching the profile in the file system.
     * 
     * @throws ServiceException
     *             if there was error loading the service script.
     */
    public ServicesRegistry loadService(String name, FileSystem fileSystem,
            ServicesRegistry registry, ProfileService profile,
            Map<String, Object> variables) throws FileSystemException,
            ServiceException {
        Pattern pattern = filePattern == null ? filePattern(name) : filePattern;
        Set<URL> files = fileSystem.findFiles(pattern);
        if (files.isEmpty()) {
            throw log.noServiceFilesFound(name, pattern);
        }
        for (URL url : files) {
            ServicePreScript prescript = loadPreScript(name, profile);
            ServiceLoader loader = serviceFactory.create(registry, variables);
            loader.setParent(injector);
            loader.loadService(url, profile, prescript);
        }
        if (!registry.getServiceNames().contains(name)) {
            throw log.serviceFileNotContainService(name, pattern);
        }
        return registry;
    }

    private ServicePreScript loadPreScript(String name, ProfileService profile)
            throws ServiceException {
        ServicePreScriptFactory factory = preScriptServices.findServiceFactory(
                name, profile);
        return factory == null ? null : factory.getPreScript();
    }

    private Pattern filePattern(String name) {
        String fileName = StringUtils.capitalize(name);
        String regex = format(SERVICE_FILE_PATTERN, fileName);
        return compile(regex);
    }

    /**
     * Sets the file pattern for the service.
     * 
     * @param pattern
     *            the service file pattern.
     */
    public void setFilePattern(Pattern pattern) {
        this.filePattern = pattern;
    }

}
