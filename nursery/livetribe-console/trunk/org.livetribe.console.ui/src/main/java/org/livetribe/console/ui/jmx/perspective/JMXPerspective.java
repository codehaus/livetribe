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
package org.livetribe.console.ui.jmx.perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.livetribe.console.ui.jmx.view.MBeanView;
import org.livetribe.console.ui.jmx.view.ObjectNameView;
import org.livetribe.console.ui.jmx.view.JMXDiscoveryView;

/**
 * @version $Rev$ $Date$
 */
public class JMXPerspective implements IPerspectiveFactory
{
    public static final String ID = JMXPerspective.class.getName();

    public void createInitialLayout(IPageLayout layout)
    {
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);
        layout.addView(ObjectNameView.ID, IPageLayout.LEFT, 0.3F, editorArea);
        layout.addView(JMXDiscoveryView.ID, IPageLayout.BOTTOM, 0.7F, ObjectNameView.ID);
        layout.addView(MBeanView.ID, IPageLayout.RIGHT, 0.7F, editorArea);
        
        layout.addPerspectiveShortcut(ID);
        layout.addShowViewShortcut(MBeanView.ID);
    }
}
