/**
 * Copyright (c) 2009-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sonatype.gshell.alias;

import java.util.EventObject;

/**
 * Event fired once an alias has been registered.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 2.5
 */
public class AliasRegisteredEvent
    extends EventObject
{
    ///CLOVER:OFF

    private final String name;

    private final String alias;

    public AliasRegisteredEvent(final String name, final String alias) {
        super(name);

        assert name != null;
        assert alias != null;

        this.name = name;
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }
}