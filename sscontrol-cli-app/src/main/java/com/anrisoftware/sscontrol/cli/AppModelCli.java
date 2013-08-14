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

import static com.anrisoftware.sscontrol.app.AppProperties.APPLICATION_PORT_PROPERTY;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;
import static org.apache.commons.lang3.StringUtils.split;
import static org.apache.commons.lang3.Validate.isTrue;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.kohsuke.args4j.Option;

import com.anrisoftware.sscontrol.app.AppProperties;
import com.anrisoftware.sscontrol.appmodel.AppModel;

/**
 * Args4j annotated application model.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AppModelCli implements AppModel {

	private static final String INVALID_VARIABLE = "Variable must have a key=value pair, invalid variable '%s'.";

	@Option(name = "-scripts", multiValued = true, required = true)
	private final List<URI> scriptsLocations;

	@Option(name = "-profile", required = true)
	private String profile;

	@Option(name = "-server", multiValued = true, required = true, handler = InetSocketAddrOptionHandler.class)
	private final List<InetSocketAddress> servers;

	@Option(name = "-variables", multiValued = true, required = false)
	private final List<String> scriptVariables;

	private final int appPort;

	@Inject
	AppModelCli(AppProperties p) {
		this.appPort = p.get().getNumberProperty(APPLICATION_PORT_PROPERTY)
				.intValue();
		this.scriptsLocations = new ArrayList<URI>();
		this.servers = new ArrayList<InetSocketAddress>();
		this.scriptVariables = new ArrayList<String>();
	}

	@Override
	public List<URI> getScriptsLocations() {
		return scriptsLocations;
	}

	@Override
	public String getProfile() {
		return profile;
	}

	@Override
	public List<InetSocketAddress> getServers() {
		List<InetSocketAddress> addr;
		addr = new ArrayList<InetSocketAddress>(servers.size());
		for (InetSocketAddress address : servers) {
			if (address.getPort() == 0) {
				addr.add(new InetSocketAddress(address.getHostString(), appPort));
			} else {
				addr.add(address);
			}
		}
		return unmodifiableList(addr);
	}

	@Override
	public Map<String, Object> getScriptVariables() {
		Map<String, Object> variables = new HashMap<String, Object>();
		for (String string : scriptVariables) {
			String[] split = split(string, "=");
			isTrue(split.length == 2, INVALID_VARIABLE, string);
			variables.put(split[0], split[1]);
		}
		return unmodifiableMap(variables);
	}
}
