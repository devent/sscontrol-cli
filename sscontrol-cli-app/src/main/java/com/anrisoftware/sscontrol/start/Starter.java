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
package com.anrisoftware.sscontrol.start;

import static org.apache.commons.lang3.builder.ToStringBuilder.setDefaultStyle;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import com.anrisoftware.sscontrol.app.App;
import com.anrisoftware.sscontrol.app.AppException;
import com.anrisoftware.sscontrol.app.AppModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Starts the application.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class Starter {

	private static StarterLogger log;

	/**
	 * Starts the application with the supplied command line arguments.
	 * 
	 * @param args
	 *            the command line arguments.
	 */
	public static void main(String[] args) {
		log = new StarterLogger();
		setDefaultStyle(SHORT_PREFIX_STYLE);
		Injector injector = Guice.createInjector(new AppModule());
		try {
			injector.getInstance(App.class).start(args);
		} catch (AppException e) {
			System.out.println(e.toString());
			log.appException(e);
		}
	}
}
