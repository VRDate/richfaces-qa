/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2016, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.richfaces.tests.qa.plugin.ensurer.taskkill.builder;

import java.io.IOException;
import java.text.MessageFormat;

import org.richfaces.tests.qa.plugin.properties.PropertiesProvider;

import com.google.inject.Inject;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class UnixKillCommandBuilder implements KillCommandBuilder {

    private final String SEARCH_TEMPLATE_START = "kill `ps -ef | ";
    private final String SEARCH_TEMPLATE_END = " grep -v grep | awk '{ print $2 }'`";
    private String arg = "";

    private final PropertiesProvider pp;

    @Inject
    public UnixKillCommandBuilder(PropertiesProvider pp) {
        this.pp = pp;
    }

    private void addArgument(String argument) {
        arg += "grep " + argument + " | ";
    }

    @Override
    public KillCommandBuilder addSearchArgument(String arg) {
        addArgument(arg);
        return this;
    }

    @Override
    public void searchAndKill() throws IOException {
        String[] cmd = { "/bin/sh", "-c", SEARCH_TEMPLATE_START + arg.trim() + SEARCH_TEMPLATE_END };
        pp.getLog().info(MessageFormat.format("Executing command: <{0}>", cmd[2]));
        Runtime.getRuntime().exec(cmd);
        arg = "";
    }

}
