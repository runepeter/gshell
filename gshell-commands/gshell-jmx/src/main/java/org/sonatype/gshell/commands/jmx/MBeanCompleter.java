package org.sonatype.gshell.commands.jmx;

import jline.console.completer.StringsCompleter;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MBeanCompleter extends StringsCompleter
{
    private static final ObjectName ALL = allQuery();
    private final MBeanServer mbeanServer;

    public MBeanCompleter()
    {
        super(Collections.<String>emptyList());
        this.mbeanServer = ManagementFactory.getPlatformMBeanServer();
    }

    @Override
    public int complete(String buffer, int cursor, List<CharSequence> candidates)
    {
        if (buffer == null)
        {
            getStrings();
        }

        return super.complete(buffer, cursor, candidates);
    }

    @Override
    public Collection<String> getStrings()
    {
        Collection<String> current = super.getStrings();

        Set<ObjectName> set = mbeanServer.queryNames(ALL, null);
        for (ObjectName objectName : set)
        {
            String s = objectName.getCanonicalName();
            current.add(s);
        }

        return current;
    }

    private static ObjectName allQuery()
    {
        try
        {
            return new ObjectName("*:*");
        } catch (MalformedObjectNameException e)
        {
            throw new RuntimeException(e);
        }
    }

}
