package org.sonatype.gshell.util.cli2;

import java.util.Map;

/**
 * Dynamic command with arguments specified in a Map. Can be used as a basis for providing a JMX shell.
 */
public interface DynamicCommand
{
    Map<String, Class<?>> getArgumentMap();

    void setValue(String argumentName, Object value);

    <T> T getValue(String argumentName);

    Class<?> getType(String argumentName);
}
