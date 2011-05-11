package org.sonatype.gshell.commands.jmx;

import com.google.inject.name.Named;
import jline.console.completer.Completer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.gshell.command.Command;
import org.sonatype.gshell.command.CommandContext;
import org.sonatype.gshell.command.registry.CommandRegistry;
import org.sonatype.gshell.command.registry.NoSuchCommandException;
import org.sonatype.gshell.command.support.CommandActionSupport;
import org.sonatype.gshell.util.cli2.Argument;

import javax.inject.Inject;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.Set;

@Command(name = "mbean")
public class MBeanCommand extends CommandActionSupport
{
    private static final String CURRENT_MBEAN = "current.mbean";

    private final Logger logger = LoggerFactory.getLogger(MBeanCommand.class);

    private final MBeanServer server;
    private final CommandRegistry registry;
    private final Set<String> operationSet;

    @Argument(required = true, index = 0)
    private String mbeanObjectName;

    @Inject
    public MBeanCommand(final CommandRegistry registry)
    {
        this.registry = registry;
        this.server = ManagementFactory.getPlatformMBeanServer();
        this.operationSet = new HashSet<String>();
    }

    @Inject
    public MBeanCommand installCompleters(final @Named("mbean-name") Completer c1) {
        assert c1 != null;
        setCompleters(c1, null);
        return this;
    }

    public Object execute(CommandContext context) throws Exception
    {
        ObjectName objectName = new ObjectName(mbeanObjectName);
        MBeanInfo beanInfo = server.getMBeanInfo(objectName);

        clearRegisteredMBeanOperations();

        for (MBeanOperationInfo operationInfo : beanInfo.getOperations())
        {
            registry.registerCommand(operationInfo.getName(), new OperationDynamicCommandAction(operationInfo, objectName, server));
            operationSet.add(operationInfo.getName());
        }

        context.getVariables().set(CURRENT_MBEAN, mbeanObjectName);

        return Result.SUCCESS;
    }

    private void clearRegisteredMBeanOperations()
    {
        for (String operation : operationSet)
        {
            try
            {
                registry.removeCommand(operation);
            } catch (NoSuchCommandException e)
            {
                logger.trace("Unable to remove command '" + operation + "'.", e);
            }
        }
        operationSet.clear();
    }

}
