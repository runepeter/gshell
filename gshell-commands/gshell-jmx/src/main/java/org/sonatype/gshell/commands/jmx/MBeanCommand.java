package org.sonatype.gshell.commands.jmx;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import jline.console.completer.Completer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.gshell.command.Command;
import org.sonatype.gshell.command.CommandContext;
import org.sonatype.gshell.command.registry.CommandRegistry;
import org.sonatype.gshell.command.registry.DuplicateCommandException;
import org.sonatype.gshell.command.registry.NoSuchCommandException;
import org.sonatype.gshell.command.support.CommandActionSupport;
import org.sonatype.gshell.shell.Shell;
import org.sonatype.gshell.util.cli2.Argument;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.Set;

@Command(name = "mbean")
public class MBeanCommand extends CommandActionSupport
{
    public static final String JMX_MBEAN = "jmx.mbean";
    public static final String NO_MBEAN = "<NO MBEAN>";

    private final Logger logger = LoggerFactory.getLogger(MBeanCommand.class);

    private final MBeanServer server;
    private final Shell shell;
    private final CommandRegistry registry;
    private final Set<String> operationSet;

    @Argument(required = true, index = 0)
    private String mbeanObjectName;

    @Inject
    public MBeanCommand(final CommandRegistry registry, final Shell shell) throws Exception
    {
        this.shell = shell;
        this.registry = registry;
        this.server = ManagementFactory.getPlatformMBeanServer();
        this.operationSet = new HashSet<String>();

        Object o = shell.getVariables().get(JMX_MBEAN);
        if (o != null && !NO_MBEAN.equals(o)) {

            ObjectName objectName = new ObjectName("" + o);
            MBeanInfo beanInfo = server.getMBeanInfo(objectName);
            if (beanInfo != null) {
                registerOperations(objectName);
            } else {
                logger.info("Unable to register commands for MBean {}.", o);
            }

        }
    }

    @Inject
    public MBeanCommand installCompleters(final @Named("mbean-name") Completer c1)
    {
        assert c1 != null;
        setCompleters(c1, null);
        return this;
    }

    public Object execute(CommandContext context) throws Exception
    {
        ObjectName objectName = new ObjectName(mbeanObjectName);

        clearRegisteredMBeanOperations();

        registerOperations(objectName);

        context.getVariables().set(JMX_MBEAN, mbeanObjectName);

        return Result.SUCCESS;
    }

    private void registerOperations(ObjectName objectName)
            throws InstanceNotFoundException, IntrospectionException, ReflectionException, DuplicateCommandException
    {
        MBeanInfo beanInfo = server.getMBeanInfo(objectName);
        for (MBeanOperationInfo operationInfo : beanInfo.getOperations())
        {
            registry.registerCommand(operationInfo.getName(), new OperationDynamicCommand(operationInfo, objectName, server));
            operationSet.add(operationInfo.getName());
        }
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
