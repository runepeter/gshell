/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.gshell.cli2.setter;

import org.apache.gshell.i18n.MessageSource;
import org.apache.gshell.i18n.ResourceBundleMessageSource;

/**
 * Messages for the {@link org.apache.maven.shell.cli2.setter} package.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
enum Messages
{
    ///CLOVER:OFF

    ILLEGAL_METHOD_SIGNATURE,
    ILLEGAL_FIELD_SIGNATURE,
    UNSUPPORTED_COLLECTION_TYPE,
    FIELD_NOT_COLLECTION;
    
    private static final MessageSource messages = new ResourceBundleMessageSource(Messages.class);

    String format(final Object... args) {
        return messages.format(name(), args);
    }
}