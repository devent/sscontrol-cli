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

import static com.anrisoftware.sscontrol.parser.AppParserLogger._.addresses;
import static com.anrisoftware.sscontrol.parser.AppParserLogger._.arguments;
import static com.anrisoftware.sscontrol.parser.AppParserLogger._.error_parse;
import static com.anrisoftware.sscontrol.parser.AppParserLogger._.error_parse_addresses;
import static com.anrisoftware.sscontrol.parser.AppParserLogger._.error_parse_addresses_message;
import static com.anrisoftware.sscontrol.parser.AppParserLogger._.error_parse_message;
import static com.anrisoftware.sscontrol.parser.AppParserLogger._.invalid_variable;
import static com.anrisoftware.sscontrol.parser.AppParserLogger._.retrieveResources;
import static org.apache.commons.lang3.Validate.isTrue;

import java.text.ParseException;
import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.kohsuke.args4j.CmdLineException;

import com.anrisoftware.globalpom.log.AbstractLogger;
import com.anrisoftware.resources.texts.api.Texts;
import com.anrisoftware.sscontrol.app.TextsProvider;
import com.anrisoftware.sscontrol.appmodel.ModelException;

/**
 * Logging messages for {@link AppParser}.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
@Singleton
class AppParserLogger extends AbstractLogger {

    enum _ {

        arguments("arguments"),

        error_parse_message("error_parse_message"),

        error_parse("error_parse"),

        error_parse_addresses("error_parse_addresses"),

        addresses("addresses"),

        error_parse_addresses_message("error_parse_addresses_message"),

        invalid_variable("invalid_variable");

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
     * Create logger for {@link AppParser}.
     */
    AppParserLogger() {
        super(AppParser.class);
    }

    @Inject
    void setTexts(TextsProvider provider) {
        retrieveResources(provider.get());
    }

    ModelException errorParseArgs(String[] args, CmdLineException e) {
        String argsstring = Arrays.toString(args);
        return logException(
                new ModelException(error_parse, e).add(arguments, argsstring),
                error_parse_message, argsstring);
    }

    ModelException errorParseAddresses(ParseException e, String[] strings) {
        String s = Arrays.toString(strings);
        return logException(
                new ModelException(error_parse_addresses, e).add(addresses, s),
                error_parse_addresses_message, s);
    }

    void checkVariable(String[] split) {
        String s = Arrays.toString(split);
        isTrue(split.length == 2, invalid_variable.toString(), s);
    }
}
