package org.sonatype.gshell.util.cli2;

import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DynamicTest extends CliProcessorTestSupport
{
    private static class Dynamic implements DynamicCommand
    {
        private final Map<String, Object> map = new HashMap<String, Object>();

        public Map<String, Class<?>> getArgumentMap()
        {
            Map<String, Class<?>> map = new LinkedHashMap<String, Class<?>>();
            map.put("arg1", int.class);
            map.put("arg2", String.class);
            return map;
        }

        public void setValue(String argumentName, Object value)
        {
            map.put(argumentName, value);
        }

        public <T> T getValue(String argumentName)
        {
            return (T) map.get(argumentName);
        }

        public Class<?> getType(String argumentName)
        {
            return getArgumentMap().get(argumentName);
        }
    }

    private Dynamic dynamic;

    @Override
    protected Object createBean()
    {
        this.dynamic = new Dynamic();
        return dynamic;
    }

    @Test
    public void testSingleArg() throws Exception
    {
        clp.process("1", "foo");
        assertEquals(dynamic.<String>getValue("arg1"), 1);
        assertEquals(dynamic.<String>getValue("arg2"), "foo");
    }

}
