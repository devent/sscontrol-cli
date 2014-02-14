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
