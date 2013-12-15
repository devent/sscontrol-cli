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
package com.anrisoftware.sscontrol.parser;

import static com.anrisoftware.sscontrol.parser.AppArgsLogger._.profile_set;
import static com.anrisoftware.sscontrol.parser.AppArgsLogger._.scripts_set;
import static com.anrisoftware.sscontrol.parser.AppArgsLogger._.servers_set;
import static com.anrisoftware.sscontrol.parser.AppArgsLogger._.services_set;
import static com.anrisoftware.sscontrol.parser.AppArgsLogger._.variables_set;

import com.anrisoftware.globalpom.log.AbstractLogger;

/**
 * Logging for {@link AppArgs}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AppArgsLogger extends AbstractLogger {

    enum _ {

        scripts_set("Service scripts locations set: '{}'."),

        profile_set("Profile set: '{}'."),

        servers_set("Servers set: '{}'."),

        variables_set("Variables set: '{}'."),

        services_set("Services set: '{}'.");

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
     * Sets the context of the logger to {@link AppArgs}.
     */
    public AppArgsLogger() {
        super(AppArgs.class);
    }

    void scriptsSet(String scripts) {
        debug(scripts_set, scripts);
    }

    void profileSet(String profile) {
        debug(profile_set, profile);
    }

    void serversSet(String servers) {
        debug(servers_set, servers);
    }

    void variablesSet(String variables) {
        debug(variables_set, variables);
    }

    void servicesSet(String services) {
        debug(services_set, services);
    }
}
