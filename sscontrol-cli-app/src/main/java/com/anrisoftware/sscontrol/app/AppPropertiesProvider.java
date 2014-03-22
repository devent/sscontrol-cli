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
package com.anrisoftware.sscontrol.app;

import java.net.URL;

import javax.inject.Singleton;

import com.anrisoftware.propertiesutils.AbstractContextPropertiesProvider;

/**
 * Provides the application properties.
 * <p>
 * The application properties are loaded from {@code app.properties}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@SuppressWarnings("serial")
@Singleton
public class AppPropertiesProvider extends AbstractContextPropertiesProvider {

    /**
     * Default application port.
     */
    public static final String APPLICATION_PORT_PROPERTY = "application_port";

    /**
     * Command line arguments separator.
     */
    public static final String COMMAND_LINE_SEPARATOR = "command_line_separator";

    private static final URL RESOURCE = AppPropertiesProvider.class
            .getResource("/app.properties");

    AppPropertiesProvider() {
        super(App.class, RESOURCE);
    }

    public int getApplicationPortProperty() {
        return get().getNumberProperty(APPLICATION_PORT_PROPERTY).intValue();
    }

    public String getCommandLineSeparator() {
        return get().getProperty(COMMAND_LINE_SEPARATOR);
    }
}
