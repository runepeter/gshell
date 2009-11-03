/*
 * Copyright (C) 2009 the original author(s).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.geronimo.gshell.commands.network;

import org.sonatype.gshell.core.command.CommandActionSupport;
import org.sonatype.gshell.command.CommandContext;
import org.sonatype.gshell.command.IO;

import java.net.InetAddress;

/**
 * Displays the name of the current host.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 2.0
 */
public class HostnameCommand
    extends CommandActionSupport
{
    public Object execute(final CommandContext context) throws Exception {
        assert context != null;
        IO io = context.getIo();

        InetAddress localhost = InetAddress.getLocalHost();
        io.info(localhost.getHostName());
        io.verbose(localhost.getHostAddress());

        return Result.SUCCESS;
    }
}