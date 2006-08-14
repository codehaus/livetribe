/**
 *
 * Copyright 2006 (C) The original author or authors.
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
package org.livetribe.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;


/**
 * Holds weak references to its members.
 *
 * @version $Revision: $ $Date: $
 */
public class WeakHashSet implements Set
{
    private final static Object VALUE = new Object();
    private final WeakHashMap map;

    public WeakHashSet()
    {
        map = new WeakHashMap();
    }

    public WeakHashSet(int initialCapacity)
    {
        map = new WeakHashMap(initialCapacity);
    }

    public WeakHashSet(int initialCapacity, float loadFactor)
    {
        map = new WeakHashMap(initialCapacity, loadFactor);
    }

    public WeakHashSet(Collection c)
    {
        map = new WeakHashMap();
        addAll(c);
    }

    public int size()
    {
        return map.size();
    }

    public void clear()
    {
        map.clear();
    }

    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    public Object[] toArray()
    {
        return map.keySet().toArray();
    }

    public boolean add(Object o)
    {
        return map.put(o, VALUE) != VALUE;
    }

    public boolean contains(Object o)
    {
        return map.containsKey(o);
    }

    public boolean remove(Object o)
    {
        return map.remove(o) == VALUE;
    }

    public boolean addAll(Collection c)
    {
        int original = map.size();

        Iterator iter = c.iterator();
        while (iter.hasNext()) map.put(iter.next(), VALUE);

        return size() != original;
    }

    public boolean containsAll(Collection c)
    {
        return map.keySet().containsAll(c);
    }

    public boolean removeAll(Collection c)
    {
        int original = map.size();

        Iterator iter = c.iterator();
        while (iter.hasNext()) map.remove(iter.next());

        return size() != original;
    }

    public boolean retainAll(Collection c)
    {
        Iterator iter = c.iterator();
        Set original = map.keySet();

        map.clear();

        while (iter.hasNext())
        {
            Object key = iter.next();
            if (original.contains(key)) map.put(key, VALUE);
        }

        return size() != original.size();
    }

    public Iterator iterator()
    {
        return map.keySet().iterator();
    }

    public Object[] toArray(Object a[])
    {
        return map.keySet().toArray(a);
    }

    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return map.equals(((WeakHashSet) o).map);
    }

    public int hashCode()
    {
        return map.hashCode();
    }
}
