package org.sonatype.gshell.util.cli2;

import java.util.Map;

public interface DynamicCommand
{
    Map<String, Class<?>> getArgumentMap();

    void setValue(String argumentName, Object value);

    <T> T getValue(String argumentName);

    Class<?> getType(String argumentName);
}
