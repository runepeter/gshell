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

package org.sonatype.gshell.builder.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.fusesource.jansi.AnsiRenderer;
import org.sonatype.gshell.command.CommandDocumenter;
import org.sonatype.gshell.command.CommandDocumenterImpl;
import org.sonatype.gshell.command.IO;
import org.sonatype.gshell.event.EventManager;
import org.sonatype.gshell.event.EventManagerImpl;
import org.sonatype.gshell.execute.CommandExecutor;
import org.sonatype.gshell.execute.CommandExecutorImpl;
import org.sonatype.gshell.io.PromptReader;
import org.sonatype.gshell.parser.CommandLineParser;
import org.sonatype.gshell.parser.CommandLineParserImpl;
import org.sonatype.gshell.registry.AliasRegistry;
import org.sonatype.gshell.registry.AliasRegistryImpl;
import org.sonatype.gshell.registry.CommandRegistrar;
import org.sonatype.gshell.registry.CommandRegistry;
import org.sonatype.gshell.registry.CommandRegistryImpl;
import org.sonatype.gshell.registry.CommandResolver;
import org.sonatype.gshell.registry.CommandResolverImpl;
import org.sonatype.gshell.shell.ShellHolder;

import java.io.IOException;

/**
 * GShell core module.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 2.0
 */
public class CoreModule
    extends AbstractModule
{
    @Override
    protected void configure() {
        bind(EventManager.class).to(EventManagerImpl.class);
        bind(AliasRegistry.class).to(AliasRegistryImpl.class);
        bind(CommandRegistry.class).to(CommandRegistryImpl.class);
        bind(CommandDocumenter.class).to(CommandDocumenterImpl.class);
        bind(CommandLineParser.class).to(CommandLineParserImpl.class);
        bind(CommandExecutor.class).to(CommandExecutorImpl.class);
        bind(CommandResolver.class).to(CommandResolverImpl.class);
        bind(CommandRegistrar.class).to(GuiceCommandRegistrar.class);
    }

    /**
     * Provides ANSI-aware prompt readers based on the current shell's IO context.
     */
    @Provides
    private PromptReader providePromptReader() throws IOException {
        IO io = ShellHolder.get().getIo();

        return new PromptReader(io.streams, io.getTerminal())
        {
            @Override
            public String readLine(String prompt, Validator validator) throws IOException {
                return super.readLine(AnsiRenderer.render(prompt), validator);
            }

            @Override
            public String readLine(String prompt, char mask, Validator validator) throws IOException {
                return super.readLine(AnsiRenderer.render(prompt), mask, validator);
            }

            @Override
            public String readPassword(String prompt, Validator validator) throws IOException {
                return super.readPassword(AnsiRenderer.render(prompt), validator);
            }
        };
    }
}