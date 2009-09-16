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

package org.apache.maven.shell.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Prefixes printed lines.
 *
 * @version $Rev$ $Date$
 */
public class PrefixingOutputStream
    extends FilterOutputStream
{
    private String prefix;

    private boolean outputHeader = true;

    public PrefixingOutputStream(final OutputStream out, final String prefix) {
        super(out);
        this.prefix = prefix;
    }

    public void write(final int b) throws IOException {
        if (outputHeader) {
            outputHeader = false;
            write(prefix.getBytes());
            out.write(b);
        }
        else if (b == '\n' && prefix != null) {
            out.write(b);
            write(prefix.getBytes());
        }
        else {
            out.write(b);
        }
    }
}