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
package com.anrisoftware.sscontrol.appmodel;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * The applications data to deploy server scripts.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public interface AppModel {

    /**
     * Returns the locations for the server scripts.
     * 
     * @return the {@link List} of the server scripts locations.
     */
    List<URI> getScriptsLocations();

    /**
     * Returns the profile to activate.
     * 
     * @return the profile name.
     */
    String getProfile();

    /**
     * Returns the servers to where the services should be deployed.
     * 
     * @return the {@link List} of the server address and port number.
     */
    List<InetSocketAddress> getServers();

    /**
     * Returns the name of the services to be deployed.
     * 
     * @return the {@link List} with the service names.
     */
    List<String> getServices();

    /**
     * Returns the name of the deactivated services.
     * 
     * @return the {@link List} with the service names.
     */
    List<String> getDeactivatedServices();

    /**
     * Tests if the service should be deployed.
     * 
     * @param name
     *            the {@link String} name of the service.
     * 
     * @return {@code true} if the service should be deployed.
     */
    boolean containsService(String name);

    /**
     * Returns a map with additional script variables. The variables are set in
     * each of the service scripts.
     * 
     * @return the {@link Map} of the script variables, with the key as the
     *         variable name and the value as the variable value.
     */
    Map<String, Object> getScriptVariables();

}
