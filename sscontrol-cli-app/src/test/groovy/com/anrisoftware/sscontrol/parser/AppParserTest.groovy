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
package com.anrisoftware.sscontrol.parser

import org.junit.Test

import com.anrisoftware.sscontrol.app.AppModule
import com.anrisoftware.sscontrol.appmodel.AppModel
import com.google.inject.Guice
import com.google.inject.Injector

/**
 * @see AppParser
 *
 * @author Erwin Mueller, erwin.mueller@deventm.org
 * @since 1.0
 */
class AppParserTest {

    @Test
    void "parse mandatory arguments"() {
        String[] args = [
            "-scripts",
            "$dirA",
            "-profile",
            profile,
            "-server",
            "$serverA"
        ]
        AppParser model = injector.getInstance(AppParser)
        model.parse(args)
        assert model.scriptsLocations.size() == 1
        assert model.scriptsLocations[0] == new URI(dirA)
        assert model.profile == profile
        assert model.servers.size() == 1
        assert model.servers[0].toString() == "/$serverA"
        assert model.scriptVariables.isEmpty()
    }

    @Test
    void "default server port"() {
        String[] args = [
            "-scripts",
            "$dirA",
            "-profile",
            profile,
            "-server",
            "127.0.0.1"
        ]
        AppModel model = injector.getInstance(AppParser).parse(args)
        assert model.servers.size() == 1
        assert model.servers[0].toString() == "/$serverA"
        assert model.scriptVariables.isEmpty()
    }

    @Test
    void "parse 2 scripts locations"() {
        String[] args = [
            "-scripts",
            "$dirA,$dirB",
            "-profile",
            profile,
            "-server",
            serverA
        ]
        AppModel model = injector.getInstance(AppParser).parse(args)
        assert model.scriptsLocations.size() == 2
        assert model.scriptsLocations.contains(new URI(dirA))
        assert model.scriptsLocations.contains(new URI(dirB))
    }

    @Test
    void "parse 2 servers"() {
        String[] args = [
            "-scripts",
            dirA,
            "-profile",
            profile,
            "-server",
            "$serverA,$serverB"
        ]
        AppModel model = injector.getInstance(AppParser).parse(args)
        assert model.servers.size() == 2
        assert model.servers[0].toString() == "/$serverA"
        assert model.servers[1].toString() == "/$serverB"
    }

    @Test
    void "parse script variables"() {
        def name = "foo"
        def value = "some"
        String[] args = [
            "-scripts",
            dirA,
            "-profile",
            profile,
            "-server",
            serverA,
            "-variables",
            "$name=$value"
        ]
        AppModel model = injector.getInstance(AppParser).parse(args)
        assert model.scriptVariables.size() == 1
        assert model.scriptVariables.keySet().contains(name)
        assert model.scriptVariables.values().contains(value)
    }

    @Test
    void "parse multiple script variables"() {
        def nameA = "foo"
        def valueA = "somefoo"
        def nameB = "bar"
        def valueB = "somebar"
        String[] args = [
            "-scripts",
            dirA,
            "-profile",
            profile,
            "-server",
            serverA,
            "-variables",
            "$nameA=$valueA,$nameB=$valueB"
        ]
        AppModel model = injector.getInstance(AppParser).parse(args)
        assert model.scriptVariables.size() == 2
        assert model.scriptVariables.keySet().contains(nameA)
        assert model.scriptVariables.values().contains(valueA)
        assert model.scriptVariables.keySet().contains(nameB)
        assert model.scriptVariables.values().contains(valueB)
    }

    static Injector injector = Guice.createInjector(new AppModule())

    static String dirA = "somedirA"

    static String dirB = "somedirB"

    static String profile = "ubuntu"

    static String serverA = "127.0.0.1:6666"

    static String serverB = "192.168.0.1:6666"
}
