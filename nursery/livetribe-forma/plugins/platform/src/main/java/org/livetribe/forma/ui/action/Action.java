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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Icon;

import org.livetribe.forma.ui.Context;
import org.livetribe.forma.ui.ContextAware;

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

    public void setActionContext(Context context)
    {
        if (actionListener instanceof ContextAware) ((ContextAware)actionListener).spiSetContext(context);
    }

    public void actionPerformed(final ActionEvent e)
    {
        actionListener.actionPerformed(e);
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
