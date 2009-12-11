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

package org.sonatype.gshell.commands.scm;

import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmResult;
import org.apache.maven.scm.ScmRevision;
import org.apache.maven.scm.ScmTag;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.scm.provider.ScmProviderRepository;
import org.apache.maven.scm.provider.ScmProviderRepositoryWithHost;
import org.apache.maven.scm.repository.ScmRepository;
import org.apache.maven.scm.repository.ScmRepositoryException;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.util.StringUtils;
import org.sonatype.gshell.command.CommandActionSupport;
import org.sonatype.gshell.plexus.PlexusRuntime;
import org.sonatype.gshell.util.cli.Option;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Resolve repository artifacts.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 3.0
 */
public abstract class ScmCommandSupport
    extends CommandActionSupport
{
    protected final PlexusRuntime plexus;

    @Option(name="-c", aliases={"--connection-url"})
    protected String connectionUrl;

    @Option(name="-d", aliases={"--dir"})
    protected File workingDirectory;

    @Option(name="-u", aliases={"--username"})
    protected String username;

    @Option(name="-p", aliases={"--password"})
    protected String password;

    @Option(name="-k", aliases={"--private-key"})
    protected String privateKey;

    @Option(name="-a", aliases={"--passphrase"})
    protected String passphrase;

    @Option(name="-i", aliases={"--includes"})
    protected String includes;

    @Option(name="-x", aliases={"--excludes"})
    protected String excludes;

    protected ScmCommandSupport(final PlexusRuntime plexus) {
        assert plexus != null;
        this.plexus = plexus;
    }

    protected ScmManager getScmManager() {
        try {
            return plexus.lookup(ScmManager.class);
        }
        catch (ComponentLookupException e) {
            throw new RuntimeException(e);
        }
    }

    protected ScmFileSet getFileSet() throws IOException {
        if (includes != null || excludes != null) {
            return new ScmFileSet(workingDirectory, includes, excludes);
        }
        else {
            return new ScmFileSet(workingDirectory);
        }
    }

    protected ScmRepository getScmRepository() throws ScmException {
        ScmRepository repository;

        try {
            repository = getScmManager().makeScmRepository(connectionUrl);

            ScmProviderRepository providerRepo = repository.getProviderRepository();

            if (!StringUtils.isEmpty(username)) {
                providerRepo.setUser(username);
            }

            if (!StringUtils.isEmpty(password)) {
                providerRepo.setPassword(password);
            }

            if (repository.getProviderRepository() instanceof ScmProviderRepositoryWithHost) {
                ScmProviderRepositoryWithHost repo = (ScmProviderRepositoryWithHost) repository.getProviderRepository();

                if (!StringUtils.isEmpty(username)) {
                    repo.setUser(username);
                }

                if (!StringUtils.isEmpty(password)) {
                    repo.setPassword(password);
                }

                if (!StringUtils.isEmpty(privateKey)) {
                    repo.setPrivateKey(privateKey);
                }

                if (!StringUtils.isEmpty(passphrase)) {
                    repo.setPassphrase(passphrase);
                }
            }
        }
        catch (ScmRepositoryException e) {
            if (!e.getValidationMessages().isEmpty()) {
                for (Iterator i = e.getValidationMessages().iterator(); i.hasNext();) {
                    String message = (String) i.next();
                    log.error(message);
                }
            }

            throw new ScmException("Can't load the scm provider.", e);
        }
        catch (Exception e) {
            throw new ScmException("Can't load the scm provider.", e);
        }

        return repository;
    }

    protected void checkResult(ScmResult result) throws Exception {
        if (!result.isSuccess()) {
            log.error("Provider message:");
            log.error(result.getProviderMessage() == null ? "" : result.getProviderMessage());

            log.error("Command output:");
            log.error(result.getCommandOutput() == null ? "" : result.getCommandOutput());

            throw new Exception("Command failed." + StringUtils.defaultString(result.getProviderMessage()));
        }
    }

    protected ScmVersion getScmVersion(String versionType, String version) throws Exception {
        if (StringUtils.isEmpty(versionType) && StringUtils.isNotEmpty(version)) {
            throw new Exception("You must specify the version type.");
        }

        if (StringUtils.isEmpty(version)) {
            return null;
        }

        if ("branch".equals(versionType)) {
            return new ScmBranch(version);
        }

        if ("tag".equals(versionType)) {
            return new ScmTag(version);
        }

        if ("revision".equals(versionType)) {
            return new ScmRevision(version);
        }

        throw new Exception("Unknown '" + versionType + "' version type.");
    }
}