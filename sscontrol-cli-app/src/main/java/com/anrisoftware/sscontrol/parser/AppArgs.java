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

import javax.inject.Inject;

import org.kohsuke.args4j.Option;

/**
 * Command line arguments.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AppArgs {

    @Inject
    private AppArgsLogger log;

    private String scripts;

	private String profile;

    private String servers;

    private String variables;

    private String services;

    @Option(name = "-scripts", required = true)
    public void setScripts(String scripts) {
        this.scripts = scripts;
        log.scriptsSet(scripts);
    }

    public String getScripts() {
        return scripts;
    }

    @Option(name = "-profile", required = true)
    public void setProfile(String profile) {
        this.profile = profile;
        log.profileSet(profile);
    }

    public String getProfile() {
        return profile;
    }

    @Option(name = "-server", required = true)
    public void setServers(String servers) {
        this.servers = servers;
        log.serversSet(servers);
    }

    public String getServers() {
        return servers;
    }

    @Option(name = "-services", required = false)
    public void setServices(String services) {
        this.services = services;
        log.servicesSet(services);
    }

    public String getServices() {
        return services;
    }

    @Option(name = "-variables", required = false)
    public void setVariables(String variables) {
        this.variables = variables;
        log.variablesSet(variables);
    }

    public String getVariables() {
        return variables;
    }

}
