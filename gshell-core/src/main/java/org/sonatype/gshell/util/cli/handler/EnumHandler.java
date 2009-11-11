/*
 * Copyright (C) 2009 the original author(s).
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

package org.sonatype.gshell.util.cli.handler;

import org.sonatype.gshell.util.cli.Descriptor;
import org.sonatype.gshell.util.setter.Setter;

/**
 * Handler for enum types.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 2.0
 */
public class EnumHandler
    extends ConvertingHandler
{
    public EnumHandler(final Descriptor desc, final Setter setter) {
        super(desc, setter);
    }

    @Override
    public String getDefaultToken() {
        StringBuilder buff = new StringBuilder();
        buff.append('[');

        Enum[] constants = (Enum[]) getSetter().getType().getEnumConstants();

        for (int i = 0; i < constants.length; i++) {
            buff.append(constants[i].name().toLowerCase());
            if (i + 1 < constants.length) {
                buff.append('|');
            }
        }

        buff.append(']');

        return buff.toString();
    }
}