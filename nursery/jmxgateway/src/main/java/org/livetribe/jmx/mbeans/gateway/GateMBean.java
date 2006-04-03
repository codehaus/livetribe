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
package org.livetribe.jmx.mbeans.gateway;

import java.io.IOException;
import java.util.Set;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.AttributeNotFoundException;
import javax.management.QueryExp;

/**
 * @version $Rev$ $Date$
 */
public interface GateMBean
{
    public void close() throws IOException;

    public Object getAttribute(ObjectName objectName, String attributeName) throws IOException, ReflectionException, InstanceNotFoundException, MBeanException, AttributeNotFoundException;

    public Object invoke(ObjectName objectName, String operationName, Object[] arguments, String[] signature) throws IOException, ReflectionException, InstanceNotFoundException, MBeanException;

    public boolean isRegistered(ObjectName objectName) throws IOException;

    public Set queryNames(ObjectName pattern, QueryExp expression) throws IOException;
}
