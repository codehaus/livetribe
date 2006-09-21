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
package org.livetribe.console.ui.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * @version $Revision$ $Date$
 */
public class OutlookBar extends Composite
{
    public OutlookBar(Composite parent)
    {
        super(parent, SWT.NONE);
        RowLayout layout = new RowLayout(SWT.VERTICAL);
        layout.marginTop = layout.marginRight = layout.marginBottom = layout.marginLeft = 10;
        layout.spacing = 10;
        layout.pack = true;
        layout.fill = true;
        layout.wrap = false;
        setLayout(layout);
    }
    
    public Item newItem()
    {
        return new Item(this);
    }
    
    public static class Item extends Composite 
    {
        private Label imageLabel;
        private Label textLabel;
        
        private Item(Composite parent)
        {
            super(parent, SWT.BORDER);
            RowLayout layout = new RowLayout(SWT.VERTICAL);
//            layout.marginTop = layout.marginRight = layout.marginBottom = layout.marginLeft = 5;
            layout.spacing = 5;
            layout.pack = true;
            layout.fill = true;
            layout.wrap = false;
            setLayout(layout);
            setBackground(parent.getBackground());
            
            imageLabel = new Label(this, SWT.NONE);
            imageLabel.setBackground(parent.getBackground());
            imageLabel.setAlignment(SWT.CENTER);
            textLabel = new Label(this, SWT.NONE);
            textLabel.setBackground(parent.getBackground());
            textLabel.setAlignment(SWT.CENTER);
        }
        
        public void setImage(Image image)
        {
            imageLabel.setImage(image);
        }
        
        public void setText(String text)
        {
            textLabel.setText(text);
        }
    }
}
