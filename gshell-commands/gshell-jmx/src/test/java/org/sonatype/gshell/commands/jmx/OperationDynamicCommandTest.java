package org.sonatype.gshell.commands.jmx;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sonatype.gshell.DummyShell;
import org.sonatype.gshell.command.CommandAction;
import org.sonatype.gshell.command.CommandContext;
import org.sonatype.gshell.command.IO;
import org.sonatype.gshell.command.support.TestIO;
import org.sonatype.gshell.shell.Shell;
import org.sonatype.gshell.variables.Variables;
import org.sonatype.gshell.variables.VariablesImpl;

import javax.management.*;
import java.lang.management.ManagementFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OperationDynamicCommandTest
{
    private static final ObjectName MBEAN_NAME = objectName("org.sonatype.gshell.jmx:type=Calculator");

    private MBeanServer mbeanServer;

    @Before
    public void initMBean() throws Exception {

        this.mbeanServer = ManagementFactory.getPlatformMBeanServer();
        assertNotNull(mbeanServer);

        if (!mbeanServer.isRegistered(MBEAN_NAME)) {
            mbeanServer.registerMBean(new Calculator(), MBEAN_NAME);
        }
    }

    @After
    public void unregisterMBean() throws Exception {
        mbeanServer.unregisterMBean(MBEAN_NAME);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullOperationArgument() throws Exception {
        new OperationDynamicCommand(null, MBEAN_NAME, mbeanServer);
    }

    @Test
    public void testAdd() throws Exception {

        OperationDynamicCommand addAction = new OperationDynamicCommand(getOperation("add"), MBEAN_NAME, mbeanServer);
        addAction.setValue("p1", 1);
        addAction.setValue("p2", 2);

        TestCommandContext context = new TestCommandContext();
        Object result = addAction.execute(context);
        assertNotNull(result);
        assertEquals(CommandAction.Result.SUCCESS, result);
        assertEquals("3", ((TestIO) context.getIo()).getOutputString().trim());
    }

    private MBeanOperationInfo getOperation(final String operationName) throws Exception {
        MBeanInfo info = mbeanServer.getMBeanInfo(MBEAN_NAME);
        MBeanOperationInfo[] operations = info.getOperations();
        for (MBeanOperationInfo operation : operations)
        {
            if (operationName.equals(operation.getName())) {
                return operation;
            }
        }
        throw new IllegalStateException("There's no operation with name '" + operationName + " for MBean '" + MBEAN_NAME + "'.");
    }

    private class TestCommandContext implements CommandContext {

        private final Shell shell = new DummyShell();
        private final IO io = new TestIO();
        private final Object [] arguments;

        private TestCommandContext(Object ... arguments)
        {
            this.arguments = arguments;
        }

        public Shell getShell()
        {
            return shell;
        }

        public Object[] getArguments()
        {
            return arguments;
        }

        public IO getIo()
        {
            return io;
        }

        public Variables getVariables()
        {
            return new VariablesImpl();
        }
    }

    public static interface CalculatorMBean {

        int add(int x, int y);

        int subtract(int x, int y);

        int multiply(int x, int y);

    }

    public static class Calculator implements CalculatorMBean {

        public int add(int x, int y)
        {
            return x + y;
        }

        public int subtract(int x, int y)
        {
            return x - y;
        }

        public int multiply(int x, int y)
        {
            return x * y;
        }

    }
    
    private static ObjectName objectName(final String objectName)
    {
        try
        {
            return new ObjectName(objectName);
        } catch (MalformedObjectNameException e)
        {
            throw new IllegalArgumentException("Argument '" + objectName + "' is not a valid JMX ObjectName.");
        }
    }

}
