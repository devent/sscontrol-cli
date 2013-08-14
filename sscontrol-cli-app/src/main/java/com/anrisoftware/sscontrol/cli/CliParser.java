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
package com.anrisoftware.sscontrol.cli;

import javax.inject.Inject;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.anrisoftware.sscontrol.appmodel.AppModel;
import com.anrisoftware.sscontrol.appmodel.ModelException;

/**
 * Parses the command line.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class CliParser {

	private final CliParserLogger log;

	private final AppModel model;

	@Inject
	CliParser(CliParserLogger logger, AppModel model) {
		this.log = logger;
		this.model = model;
	}

	/**
	 * Parses the specified command line arguments.
	 * 
	 * @param args
	 *            the command line arguments.
	 * 
	 * @return the {@link AppModel}.
	 * 
	 * @throws ModelException
	 *             if there was an error parsing the command line arguments.
	 */
	public AppModel parse(String[] args) throws ModelException {
		CmdLineParser parser = new CmdLineParser(model);
		try {
			parser.parseArgument(args);
			return model;
		} catch (CmdLineException e) {
			throw log.errorParseArgs(args, e);
		}
	}
}
