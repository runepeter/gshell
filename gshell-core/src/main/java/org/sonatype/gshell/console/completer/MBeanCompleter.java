package org.sonatype.gshell.console.completer;

import jline.console.completer.StringsCompleter;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Singleton
@Named("mbean-name")
public class MBeanCompleter extends StringsCompleter
{
    private static final ObjectName ALL = allQuery();
    private MBeanServer mbeanServer;

    public MBeanCompleter()
    {
        super(Collections.<String>emptyList());
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

        MBeanServer server = getMBeanServer();
        if (server != null)
        {
            Set<ObjectName> set = server.queryNames(ALL, null);
            for (ObjectName objectName : set)
            {
                String s = objectName.getCanonicalName();
                current.add(s);
            }
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

    private MBeanServer getMBeanServer()
    {
        if (mbeanServer == null)
        {
            this.mbeanServer = ManagementFactory.getPlatformMBeanServer();
        }
        return mbeanServer;
    }

}
