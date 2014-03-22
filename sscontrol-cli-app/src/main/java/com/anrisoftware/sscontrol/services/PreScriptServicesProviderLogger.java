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

import static com.anrisoftware.sscontrol.services.PreScriptServicesProviderLogger._.found_service;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.sscontrol.core.api.ProfileService;
import com.anrisoftware.sscontrol.core.api.ServicePreScriptFactory;

/**
 * Logging for {@link PreScriptServicesProvider}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class PreScriptServicesProviderLogger extends AbstractLogger {

    enum _ {

        found_service("Pre-script service found for '{}'-{}: {}.");

        private String name;

        private _(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Sets the context of the logger to {@link PreScriptServicesProvider}.
     */
    public PreScriptServicesProviderLogger() {
        super(PreScriptServicesProvider.class);
    }

    void foundService(ServicePreScriptFactory service, String name,
            ProfileService profile) {
        debug(found_service, name, profile, service);
    }
}
