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
package org.livetribe.web.command.servlet.spi;

import java.util.Map;
import java.util.Enumeration;
import javax.servlet.ServletContext;

import org.livetribe.web.command.spi.AbstractDependecySetter;
import org.livetribe.web.command.spi.AttributeContainer;
import org.livetribe.web.command.spi.CommandContext;

/**
 * $Rev: 51 $
 */
public class CommandDependencySetter extends AbstractDependecySetter
{
    public CommandDependencySetter(Map configuration)
    {
        super(configuration);
    }

    public void perform(CommandContext context)
    {
        AttributeContainer servletContext = convert(((ServletCommandContext)context).getServletConfig().getServletContext());
        setDependencies(context, servletContext);
    }

    private AttributeContainer convert(ServletContext servletContext)
    {
        return new ServletAttributeContainer(servletContext);
    }

    private static class ServletAttributeContainer implements AttributeContainer
    {
        private final ServletContext servletContext;

        public ServletAttributeContainer(ServletContext servletContext)
        {
            this.servletContext = servletContext;
        }

        public Object getAttribute(String key)
        {
            return servletContext.getAttribute(key);
        }

        public Object[] getAttributeValues(String key)
        {
            return new Object[]{getAttribute(key)};
        }

        public Enumeration enumeration()
        {
            return servletContext.getAttributeNames();
        }
    }
}
