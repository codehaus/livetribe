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

import javax.swing.AbstractAction;
import static javax.swing.Action.ACCELERATOR_KEY;
import static javax.swing.Action.ACTION_COMMAND_KEY;
import static javax.swing.Action.MNEMONIC_KEY;
import static javax.swing.Action.NAME;
import static javax.swing.Action.SHORT_DESCRIPTION;
import static javax.swing.Action.SMALL_ICON;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import org.livetribe.forma.ui.Context;

/**
 * @version $Rev$ $Date$
 */
public class ActionCommand
{
    private final org.livetribe.forma.ui.action.Action action;
    private final javax.swing.Action swingAction;
    private Context context;

    public ActionCommand(Action action)
    {
        this.action = action;
        this.swingAction = new SwingAction();
    }

    public void spiSetText(String text)
    {
        swingAction.putValue(NAME, text);
    }

    public void spiSetMnemonic(int mnemonic)
    {
        swingAction.putValue(MNEMONIC_KEY, mnemonic);
    }

    public String getTooltip()
    {
        return (String)swingAction.getValue(SHORT_DESCRIPTION);
    }

    public void spiSetTooltip(String tooltip)
    {
        swingAction.putValue(SHORT_DESCRIPTION, tooltip);
    }

    public Icon getIcon()
    {
        return (Icon)swingAction.getValue(SMALL_ICON);
    }

    public void spiSetIcon(Icon icon)
    {
        swingAction.putValue(SMALL_ICON, icon);
    }

    public String getCommand()
    {
        return (String)swingAction.getValue(ACTION_COMMAND_KEY);
    }

    public void spiSetCommand(String command)
    {
        swingAction.putValue(ACTION_COMMAND_KEY, command);
    }

    public void spiSetAccelerator(KeyStroke accelerator)
    {
        swingAction.putValue(ACCELERATOR_KEY, accelerator);
    }

    public javax.swing.Action getSwingAction()
    {
        return swingAction;
    }

    public void setContext(Context context)
    {
        this.context = context;
    }

    public void execute()
    {
        action.execute(context);
    }

    private class SwingAction extends AbstractAction
    {
        public void actionPerformed(ActionEvent e)
        {
            execute();
        }
    }
}
