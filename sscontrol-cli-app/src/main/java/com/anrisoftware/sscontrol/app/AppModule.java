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
package com.anrisoftware.sscontrol.app;

import com.anrisoftware.sscontrol.cli.CliModelModule;
import com.anrisoftware.sscontrol.core.modules.CoreModule;
import com.anrisoftware.sscontrol.core.modules.CoreResourcesModule;
import com.anrisoftware.sscontrol.core.service.ServiceModule;
import com.anrisoftware.sscontrol.filesystem.FileSystemModule;
import com.google.inject.AbstractModule;

/**
 * Installs the dependency modules for the application.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AppModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FileSystemModule());
		install(new CliModelModule());
		install(new CoreModule());
		install(new ServiceModule());
		install(new CoreResourcesModule());
	}
}
