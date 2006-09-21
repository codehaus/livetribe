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
package org.livetribe.console.ui.jmx;

import java.util.ArrayList;
import java.util.List;

/**
 * @version $Rev$ $Date$
 */
public abstract class JMXNode
{
    private JMXNode parent;
    private final List<JMXNode> children = new ArrayList<JMXNode>();
    
    public abstract String getName();
    
    public abstract JMXNode find(String key);
    
    public void add(JMXNode node)
    {
        children.add(node);
        node.setParent(this);
    }
    
    private void setParent(JMXNode parent)
    {
        this.parent = parent;
    }
    
    public JMXNode getParent()
    {
        return parent;
    }
    
    public JMXNode getRoot()
    {
        JMXNode node = this;
        while (node.getParent() != null) node = node.getParent();
        return node;
    }
    
    public List<JMXNode> getChildren()
    {
        return children;
    }
    
    public boolean hasChildren()
    {
        return !children.isEmpty();
    }
}
