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
