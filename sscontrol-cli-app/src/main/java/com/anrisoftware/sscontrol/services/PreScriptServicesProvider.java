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

import static java.util.ServiceLoader.load;

import javax.inject.Inject;

import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.ServicePreScriptFactory;
import com.anrisoftware.sscontrol.core.api.ServiceScriptInfo;

/**
 * Provides the pre-script services.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class PreScriptServicesProvider {

    @Inject
    private PreScriptServicesProviderLogger log;

    /**
     * Finds the pre-script for the specified service.
     * 
     * @param name
     *            the service {@link String} name.
     * 
     * @param profile
     *            the service {@link ProfileService} profile.
     * 
     * @return the {@link ServicePreScriptFactory} or {@code null}.
     */
    public ServicePreScriptFactory findServiceFactory(String name,
            ProfileService profile) {
        ServicePreScriptFactory found = null;
        for (ServicePreScriptFactory service : load(ServicePreScriptFactory.class)) {
            ServiceScriptInfo info = service.getInfo();
            if (info.getServiceName().equals(name)) {
                if (info.getProfileName() != null) {
                    if (info.getProfileName().equals(profile.getProfileName())) {
                        found = service;
                        break;
                    }
                } else {
                    found = service;
                    break;
                }
            }
        }
        log.foundService(found, name, profile);
        return found;
    }
}
