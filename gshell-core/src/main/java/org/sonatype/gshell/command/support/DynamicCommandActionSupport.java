package org.sonatype.gshell.command.support;

import org.sonatype.gshell.command.CommandContext;
import org.sonatype.gshell.util.cli2.DynamicCommand;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class DynamicCommandActionSupport extends CommandActionSupport implements DynamicCommand
{
    private final Map<String, Class<?>> argumentMap;
    private final Map<String, Object> valueMap;

    protected DynamicCommandActionSupport(LinkedHashMap<String, Class<?>> argumentDefinitionMap)
    {
        this.argumentMap = argumentDefinitionMap;
        this.valueMap = new HashMap<String, Object>(argumentDefinitionMap.size());

        for (String key : argumentDefinitionMap.keySet())
        {
            valueMap.put(key, null);
        }
    }

    public Map<String, Class<?>> getArgumentMap()
    {
        return argumentMap;
    }

    public void setValue(String argumentName, Object value)
    {
        valueMap.put(argumentName, value);
    }

    public <T> T getValue(String argumentName)
    {
        return (T) valueMap.get(argumentName);
    }

    public Class<?> getType(String argumentName)
    {
        return argumentMap.get(argumentName);
    }
}
