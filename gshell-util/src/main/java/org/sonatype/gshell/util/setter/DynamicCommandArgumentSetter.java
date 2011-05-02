package org.sonatype.gshell.util.setter;

import org.sonatype.gshell.util.cli2.DynamicCommand;

public class DynamicCommandArgumentSetter implements Setter
{
    private final DynamicCommand command;
    private final String argumentName;

    public DynamicCommandArgumentSetter(final DynamicCommand command, final String argumentName)
    {
        this.command = command;
        this.argumentName = argumentName;
    }

    public void set(Object value) throws Exception
    {
        command.setValue(argumentName, value);
    }

    public Object getBean()
    {
        return command;
    }

    public String getName()
    {
        return argumentName;
    }

    public Class<?> getType()
    {
        return command.getType(argumentName);
    }

    public boolean isMultiValued()
    {
        return false;
    }

}
