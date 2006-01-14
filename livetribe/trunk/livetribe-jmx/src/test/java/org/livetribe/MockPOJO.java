/**
 *
 * Copyright 2005 (C) The LiveTribe Group. All Rights Reserved.
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
package org.livetribe;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;


/**
 * @version $Revision: $ $Date: $
 */
public class MockPOJO
{
    private List tstListeners = new ArrayList();
    private List propListeners = new ArrayList();
    private int attr1;
    private Integer attr2;
    private boolean dog;

    public MockPOJO()
    {
    }

    public MockPOJO(int attr1, Integer attr2, boolean dog)
    {
        this.attr1 = attr1;
        this.attr2 = attr2;
        this.dog = dog;
    }

    public int getAttr1()
    {
        return attr1;
    }

    public void setAttr1(int attr1)
    {
        this.attr1 = attr1;
    }

    public Integer getAttr2()
    {
        return attr2;
    }

    public void setAttr2(Integer attr2)
    {
        this.attr2 = attr2;
    }

    public boolean isDog()
    {
        return dog;
    }

    public void setDog(boolean dog)
    {
        this.dog = dog;
    }

    public String getFoo()
    {
        return "BAR";
    }

    public void setBar(String bar)
    {
        if (!"FOO".equals(bar)) throw new IllegalArgumentException("Only FOO is accepted");
    }

    public void addMockListener(MockListener listener)
    {
        tstListeners.add(listener);
    }

    public void removeMockListener(MockListener listener)
    {
        tstListeners.remove(listener);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propListeners.add(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propListeners.remove(listener);
    }


    public void firePropertyChange()
    {
        Long oldValue = new Long(System.currentTimeMillis());
        Long newValue = new Long(oldValue.longValue() - 1L);
        PropertyChangeEvent event = new PropertyChangeEvent(this, "property", oldValue, newValue);
        for (int i = 0; i < tstListeners.size(); i++)
        {
            MockListener listener = (MockListener) tstListeners.get(i);
            listener.propertyChange(event);
        }
    }

    public void fireInternalChange()
    {
        for (int i = 0; i < tstListeners.size(); i++)
        {
            MockListener listener = (MockListener) tstListeners.get(i);
            listener.testChange(new MockEvent(this));
        }
    }

    public void fireBigChange()
    {
        for (int i = 0; i < tstListeners.size(); i++)
        {
            MockListener listener = (MockListener) tstListeners.get(i);
            listener.bigChange(123, "This is a big change");
        }
    }

    public List getTestListeners()
    {
        return tstListeners;
    }
}
