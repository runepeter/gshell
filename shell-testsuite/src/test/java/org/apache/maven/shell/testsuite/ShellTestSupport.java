/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.maven.shell.testsuite;

import org.apache.maven.shell.Shell;
import org.apache.maven.shell.core.impl.registry.CommandRegistrationAgent;
import org.apache.maven.shell.io.IO;
import org.apache.maven.shell.io.IOHolder;
import org.codehaus.plexus.PlexusTestCase;

/**
 * Support for testing {@link Shell} instances.
 *
 * @version $Rev$ $Date$
 */
public abstract class ShellTestSupport
    extends PlexusTestCase
{
    private IO io;

    private Shell shell;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        io = new TestIO();

        IOHolder.set(io);

        CommandRegistrationAgent agent = lookup(CommandRegistrationAgent.class);
        agent.registerCommands();

        shell = lookup(Shell.class);
    }

    @Override
    protected void tearDown() throws Exception {
        io.flush();
        io = null;

        shell.close();
        shell = null;

        super.tearDown();
    }

    protected Shell getShell() {
        assertNotNull(shell);
        return shell;
    }

    protected Object execute(final String line) throws Exception {
        assertNotNull(line);
        return getShell().execute(line);
    }
}