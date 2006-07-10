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
package org.livetribe.forma;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @version $Rev$ $Date$
 */
public abstract class AbstractInfo
{
    private final Map<String, Object> properties = new HashMap<String, Object>();

    protected Object put(String key, Object value)
    {
        return properties.put(key, value);
    }

    public Map<String, Object> getProperties()
    {
        return properties;
    }

    /**
     * Returns a sorted copy of the values contained in the given map.
     * The sort is a partial ordered sort, since values implements the
     * {@link PartiallyOrdered} interface.
     */
    public static <T extends PartiallyOrdered> List<T> sort(Map<String, T> elements)
    {
        Set<String> visited = new LinkedHashSet<String>();
        List<T> result = new LinkedList<T>();
        for (T value : elements.values()) visit(elements, value, visited, result);
        return result;
    }

    private static <T extends PartiallyOrdered> void visit(Map<String, T> elements, T value, Set<String> visited, List<T> result)
    {
        String elementId = value.getElementId();
        if (!visited.add(elementId))
        {
            if (result.contains(value)) return;

            StringBuilder builder = new StringBuilder();
            for (String visitedElementId : visited) builder.append(visitedElementId).append(" < ");
            builder.append(elementId);
            throw new ExtensionException("Cyclic dependency among elements ('<' means 'depends on'): " + builder);
        }

        for (String childId : value.getLesserElements())
        {
            T childElement = elements.get(childId);
            if (childElement == null)
                throw new ExtensionException("Broken dependency " + childId + " for element " + elementId);
            visit(elements, childElement, visited, result);
        }
        result.add(value);
    }
}
