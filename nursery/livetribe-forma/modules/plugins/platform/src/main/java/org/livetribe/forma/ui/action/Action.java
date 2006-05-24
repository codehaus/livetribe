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
package org.livetribe.forma.ui.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
 * @version $Rev$ $Date$
 */
public class Action extends AbstractAction
{
    private final ActionListener actionListener;

    public Action(ActionListener actionListener)
    {
        this.actionListener = actionListener;
    }

    public void setActionContext(ActionContext context)
    {
        if (actionListener instanceof ActionContextAware) ((ActionContextAware)actionListener).spiSetActionContext(context);
    }

    public void actionPerformed(final ActionEvent e)
    {
        // Using invokeLater here makes sure that the widget
        // (menu item, button, etc) is properly rendered
        // after the click (menu item popup is closed, button
        // is repainted non-pressed, etc)
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                actionListener.actionPerformed(e);
            }
        });
    }

    public String getTooltip()
    {
        return (String)getValue(SHORT_DESCRIPTION);
    }

    public Icon getIcon()
    {
        return (Icon)getValue(SMALL_ICON);
    }

    public String getCommand()
    {
        return (String)getValue(ACTION_COMMAND_KEY);
    }
}
