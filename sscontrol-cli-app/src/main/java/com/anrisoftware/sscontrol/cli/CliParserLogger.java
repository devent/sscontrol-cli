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

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.kohsuke.args4j.CmdLineException;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.resources.texts.api.Texts;
import com.anrisoftware.sscontrol.app.TextsProvider;
import com.anrisoftware.sscontrol.appmodel.ModelException;

/**
 * Logging messages for {@link CliParser}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class CliParserLogger extends AbstractLogger {

	private enum _ {

		ARGUMENTS("arguments"),

		ERROR_PARSE1("error_parse_message"),

		ERROR_PARSE("error_parse");

		public static void retrieveResources(Texts texts) {
			for (_ value : values()) {
				value.setText(texts);
			}
		}

		private String name;

		private String text;

		private _(String name) {
			this.name = name;
		}

		public void setText(Texts texts) {
			this.text = texts.getResource(name).getText();
		}

		@Override
		public String toString() {
			return text;
		}
	}

	/**
	 * Create logger for {@link CliParser}.
	 */
	CliParserLogger() {
		super(CliParser.class);
	}

	@Inject
	void setTexts(TextsProvider provider) {
		_.retrieveResources(provider.get());
	}

	ModelException errorParseArgs(String[] args, CmdLineException e) {
		String argsstring = Arrays.toString(args);
		return logException(new ModelException(_.ERROR_PARSE, e).add(
				_.ARGUMENTS, argsstring), _.ERROR_PARSE1, argsstring);
	}
}
