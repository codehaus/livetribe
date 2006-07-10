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
package org.livetribe.forma.ui.feedback.spi;

import java.awt.Component;
import java.awt.Cursor;

import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

import org.livetribe.forma.ui.feedback.Feedback;

/**
 * @version $Rev$ $Date$
 */
public class WaitCursorFeedback implements Feedback
{
    private final Component glassPane;

    public WaitCursorFeedback(Component component)
    {
        JRootPane rootPane = SwingUtilities.getRootPane(component);
        glassPane = rootPane.getGlassPane();
        glassPane.setVisible(true);
        glassPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

    public void stop()
    {
        glassPane.setCursor(Cursor.getDefaultCursor());
        glassPane.setVisible(false);
    }
}
