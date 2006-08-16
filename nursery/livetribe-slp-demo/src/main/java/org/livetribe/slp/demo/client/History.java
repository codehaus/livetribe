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
package org.livetribe.slp.demo.client;

import java.util.LinkedList;
import java.util.List;

/**
 * @version $Revision$ $Date$
 */
public class History
{
    private List elements = new LinkedList();
    private int current = -1;

    public void add(Object element)
    {
        elements.subList(current + 1, elements.size()).clear();
        elements.add(current + 1, element);
        if (current == -1) ++current;
    }

    public Object remove()
    {
        if (current == -1) throw new IllegalStateException();
        Object result = elements.remove(current);
        if (current == elements.size()) --current;
        return result;
    }

    public Object getCurrent()
    {
        return elements.get(current);
    }

    public void moveForward()
    {
        if (!canMoveForward()) throw new IllegalStateException();
        ++current;
    }

    public void moveBackward()
    {
        if (!canMoveBackward()) throw new IllegalStateException();
        --current;
    }

    public void moveToTop()
    {
        moveTo(elements.size() - 1);
    }

    public void moveToBottom()
    {
        moveTo(0);
    }

    public void moveTo(int position)
    {
        if (current < 0 || current > elements.size() - 1) throw new IllegalStateException();
        current = position;
    }

    public boolean canMoveBackward()
    {
        return current > 0;
    }

    public boolean canMoveForward()
    {
        return current < elements.size() - 1;
    }

    public boolean contains(Object element)
    {
        return elements.contains(element);
    }

    public int indexOf(Object element)
    {
        return elements.indexOf(element);
    }

    public String toString()
    {
        return "[" + elements + " | " + current + "]";
    }
}
