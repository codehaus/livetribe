/*
 * Copyright 2006 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.livetribe.web.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * $Rev: 51 $
 */
public abstract class AbstractCommand implements Command
{
    public static final String OOPS_KEY = "oops";

    protected Log logger = LogFactory.getLog(getClass());
    private Throwable oops;

    protected void setOops(Throwable oops)
    {
        this.oops = oops;
    }

    public Throwable getOops()
    {
        return oops;
    }
}
