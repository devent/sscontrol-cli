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

import static com.anrisoftware.globalpom.format.inetsocketaddress.InetSocketAddressFormat.createInetSocketAddressFormat;
import static java.lang.String.format;

import java.net.InetSocketAddress;
import java.text.ParseException;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;

import com.anrisoftware.globalpom.format.inetsocketaddress.InetSocketAddressFormat;

/**
 * Command line argument handler for Internet socket address.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class InetSocketAddrOptionHandler extends
		OptionHandler<InetSocketAddress> {

	private final InetSocketAddressFormat format;

	public InetSocketAddrOptionHandler(CmdLineParser parser, OptionDef option,
			Setter<? super InetSocketAddress> setter) {
		super(parser, option, setter);
		this.format = createInetSocketAddressFormat();
	}

	@Override
	public int parseArguments(Parameters params) throws CmdLineException {
		String param = params.getParameter(0);
		try {
			parseAddress(param);
		} catch (ParseException e) {
			throw notValidAddress(param);
		}
		return 1;
	}

	private void parseAddress(String param) throws CmdLineException,
			ParseException {
		InetSocketAddress address = format.parse(param);
		setter.addValue(address);
	}

	private CmdLineException notValidAddress(String param) {
		return new CmdLineException(owner, format(
				"Not valid server address '%s'", param));
	}

	@Override
	public String getDefaultMetaVariable() {
		return "HOST[:PORT]";
	}
}
