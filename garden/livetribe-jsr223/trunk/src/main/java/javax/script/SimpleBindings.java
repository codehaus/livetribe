/**
 *
 * Copyright 2006 - 2007 (C) The original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package javax.script;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * @version $Revision$ $Date$
 */
public class SimpleBindings implements Bindings
{
    private final Map<String, Object> map;

    public SimpleBindings()
    {
        this(new HashMap<String, Object>());
    }

    public SimpleBindings(Map<String, Object> map)
    {
        this.map = map;
    }

    public Object put(String name, Object value)
    {
        if (name == null) throw new NullPointerException("Name is null");

        return map.put((String) name, value);
    }

    public void putAll(Map<? extends String, ? extends Object> toMerge)
    {
        for (String key : toMerge.keySet())
        {
            if (key == null) throw new NullPointerException("Key is null");
        }

        map.putAll(toMerge);
    }

    public int size()
    {
        return map.size();
    }

    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    public boolean containsKey(Object key)
    {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value)
    {
        return map.containsValue(value);
    }

    public Object get(Object key)
    {
        return map.get(key);
    }

    public Object remove(Object key)
    {
        return map.remove(key);
    }

    public void clear()
    {
        map.clear();
    }

    public Set<String> keySet()
    {
        return map.keySet();
    }

    public Collection<Object> values()
    {
        return map.values();
    }

    public Set<Map.Entry<String,Object>> entrySet()
    {
        return map.entrySet();
    }

    public boolean equals(Object o)
    {
        return map.equals(o);
    }

    public int hashCode()
    {
        return map.hashCode();
    }
}
