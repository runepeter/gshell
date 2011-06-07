package org.sonatype.gshell.commands.jmx;

import jline.console.completer.ArgumentCompleter;
import jline.console.completer.Completer;
import org.sonatype.gshell.command.CommandContext;
import org.sonatype.gshell.command.IO;
import org.sonatype.gshell.command.support.DynamicCommandActionSupport;

import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.util.*;

public class OperationDynamicCommand extends DynamicCommandActionSupport
{
    private final MBeanOperationInfo operationInfo;
    private final ObjectName mbeanName;
    private final MBeanServer mbeanServer;

    private static final Map<String, Class<?>> primitiveMap = new HashMap<String, Class<?>>()
    {
        {
            put("int", int.class);
            put("long", long.class);
            put("boolean", boolean.class);
            put("double", double.class);
            put("float", float.class);
            put("byte", byte.class);
            put("char", char.class);
        }
    };

    public OperationDynamicCommand(final MBeanOperationInfo operationInfo, final ObjectName mbeanName, final MBeanServer mbeanServer)
    {
        super(toArgumentDefinitionMap(operationInfo));

        this.operationInfo = operationInfo;
        this.mbeanName = mbeanName;
        this.mbeanServer = mbeanServer;

        MBeanParameterInfo[] signature = operationInfo.getSignature();
        List<Completer> list = new ArrayList<Completer>(signature.length);
        for (MBeanParameterInfo parameterInfo : signature)
        {
            list.add(new ArgumentCompleter());
        }

        if (!list.isEmpty())
        {
            list.add(null);
        }

        setCompleters(list);
    }

    private static LinkedHashMap<String, Class<?>> toArgumentDefinitionMap(final MBeanOperationInfo operationInfo)
    {
        if (operationInfo == null) {
            throw new IllegalArgumentException("MBeanOperationInfo argument cannot be 'null'.");
        }

        LinkedHashMap<String, Class<?>> map = new LinkedHashMap<String, Class<?>>();

        MBeanParameterInfo[] signature = operationInfo.getSignature();
        for (MBeanParameterInfo parameterInfo : signature)
        {
            String typeName = parameterInfo.getType();
            Class<?> typeClass = primitiveMap.get(typeName);

            try
            {
                map.put(parameterInfo.getName(), typeClass != null ? typeClass : Class.forName(typeName));
            } catch (ClassNotFoundException e)
            {
                throw new RuntimeException("Unable to resolve argument type '" + parameterInfo.getType() + "'.");
            }
        }

        return map;
    }

    public Object execute(CommandContext commandContext) throws Exception
    {
        IO io = commandContext.getIo();

        List<String> list = new ArrayList<String>();
        for (MBeanParameterInfo parameterInfo : operationInfo.getSignature())
        {
            list.add(parameterInfo.getType());
        }

        String[] signature = new String[list.size()];
        signature = list.toArray(signature);

        List<Object> l = new ArrayList<Object>(getArgumentMap().size());
        for (String key : getArgumentMap().keySet())
        {
            l.add(getValue(key));
        }

        Object[] array = new Object[l.size()];
        array = l.toArray(array);

        try
        {
            Object result = mbeanServer.invoke(mbeanName, operationInfo.getName(), array, signature);
            io.println(result);
        } catch (Exception e)
        {
            io.println("Unable to invoke operation: '" + e.getMessage() + "'.");
        }

        return Result.SUCCESS;
    }
}
