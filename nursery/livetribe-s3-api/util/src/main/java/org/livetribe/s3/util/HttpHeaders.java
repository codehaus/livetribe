/**
 *
 * Copyright 2008 (C) The original author or authors
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
package org.livetribe.s3.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @version $Revision$ $Date$
 */
public class HttpHeaders implements Map<String, List<String>>
{

    private final Map<String, List<String>> map = new HashMap<String, List<String>>();

    public int size() { return map.size(); }

    public boolean isEmpty() { return map.isEmpty(); }

    public boolean containsKey(Object key) { return map.containsKey(key); }

    public boolean containsValue(Object value) { return map.containsValue(value); }

    public List<String> get(Object key) { return map.get(key); }

    public List<String> put(String key, List<String> value) { return map.put(key, value); }

    public List<String> put(String key, String value)
    {
        List<String> values = map.get(key);

        if (values == null) map.put(key, values = new ArrayList<String>());

        List<String> old = new ArrayList<String>(values);

        values.add(value);

        return old;
    }

    public List<String> remove(Object key) { return map.remove(key); }

    public void putAll(Map<? extends String, ? extends List<String>> t) { map.putAll(t); }

    public void clear() { map.clear(); }

    public Set<String> keySet() { return map.keySet(); }

    public Collection<List<String>> values() { return map.values(); }

    public Set<Entry<String, List<String>>> entrySet() { return map.entrySet(); }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HttpHeaders that = (HttpHeaders) o;

        if (!map.equals(that.map)) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        return map.hashCode();
    }
}
