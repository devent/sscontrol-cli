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

import static org.apache.commons.lang3.StringUtils.split;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import com.anrisoftware.globalpom.format.inetsocketaddress.InetSocketAddressFormat;
import com.anrisoftware.globalpom.format.inetsocketaddress.InetSocketAddressFormatFactory;
import com.anrisoftware.sscontrol.app.AppPropertiesProvider;
import com.anrisoftware.sscontrol.appmodel.AppModel;
import com.anrisoftware.sscontrol.appmodel.ModelException;

/**
 * Parses the command line.
 * 
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
public class AppParser implements AppModel {

    @Inject
    private AppParserLogger log;

    @Inject
    private AppArgs arguments;

    @Inject
    private InetSocketAddressFormatFactory addressFormatFactory;

    @Inject
    private AppPropertiesProvider properties;

    private List<InetSocketAddress> servers;

    private List<URI> scripts;

    private Map<String, Object> variables;

    private List<String> services;

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
        CmdLineParser parser = new CmdLineParser(arguments);
        try {
            parser.parseArgument(args);
            parseArgs(arguments);
            return this;
        } catch (CmdLineException e) {
            throw log.errorParseArgs(args, e);
        }
    }

    private void parseArgs(AppArgs args) throws ModelException {
        this.scripts = parseScripts(args);
        this.servers = parseAddresses(args);
        this.variables = parseVariables(args);
        this.services = parseServices(args);
    }

    private List<String> parseServices(AppArgs args) {
        String[] strings = split(args.getServices(),
                properties.getCommandLineSeparator());
        List<String> services = new ArrayList<String>();
        if (strings != null) {
            services.addAll(Arrays.asList(strings));
        }
        return services;
    }

    private List<URI> parseScripts(AppArgs args) {
        List<URI> scripts = new ArrayList<URI>();
        String[] strings = split(args.getScripts(),
                properties.getCommandLineSeparator());
        for (String s : strings) {
            scripts.add(parseURI(s));
        }
        return scripts;
    }

    private URI parseURI(String s) {
        URI url;
        try {
            url = new URI(s);
        } catch (URISyntaxException e) {
            url = new File(s).toURI();
        }
        return url;
    }

    private List<InetSocketAddress> parseAddresses(AppArgs args)
            throws ModelException {
        String[] strings = split(args.getServers(),
                properties.getCommandLineSeparator());
        List<InetSocketAddress> addresses = new ArrayList<InetSocketAddress>();
        InetSocketAddressFormat format = addressFormatFactory.create();
        int defaultPort = properties.getApplicationPortProperty();
        for (String s : strings) {
            try {
                parseAddress(addresses, format, defaultPort, s);
            } catch (ParseException e) {
                throw log.errorParseAddresses(e, strings);
            }
        }
        return addresses;
    }

    private void parseAddress(List<InetSocketAddress> addresses,
            InetSocketAddressFormat format, int defaultPort, String s)
            throws ParseException {
        InetSocketAddress address = format.parse(s);
        if (address.getPort() == 0) {
            InetAddress host = address.getAddress();
            addresses.add(new InetSocketAddress(host, defaultPort));
        } else {
            addresses.add(address);
        }
    }

    private Map<String, Object> parseVariables(AppArgs args) {
        String[] strings = split(args.getVariables(),
                properties.getCommandLineSeparator());
        Map<String, Object> variables = new HashMap<String, Object>();
        if (strings == null) {
            return variables;
        }
        for (String s : strings) {
            String[] split = split(s, "=");
            log.checkVariable(split);
            variables.put(split[0], split[1]);
        }
        return variables;
    }

    @Override
    public List<URI> getScriptsLocations() {
        return scripts;
    }

    @Override
    public String getProfile() {
        return arguments.getProfile();
    }

    @Override
    public List<InetSocketAddress> getServers() {
        return servers;
    }

    @Override
    public List<String> getServices() {
        return services;
    }

    @Override
    public boolean containsService(String name) {
        List<String> services = getServices();
        if (services.size() < 1) {
            return true;
        } else {
            return services.contains(name);
        }
    }

    @Override
    public Map<String, Object> getScriptVariables() {
        return variables;
    }

}
