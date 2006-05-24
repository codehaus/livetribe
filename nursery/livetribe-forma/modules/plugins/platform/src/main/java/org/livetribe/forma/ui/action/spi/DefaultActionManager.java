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
package org.livetribe.forma.ui.action.spi;

import java.awt.event.ActionListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import org.livetribe.forma.ui.action.Action;
import org.livetribe.forma.ui.action.ActionContext;
import org.livetribe.forma.ui.action.ActionException;
import org.livetribe.forma.ui.action.ActionManager;
import org.livetribe.ioc.Container;
import org.livetribe.ioc.Inject;

/**
 * @version $Rev$ $Date$
 */
public class DefaultActionManager implements ActionManager
{
    @Inject
    private Container containerManager;
    private final Map<String, ActionInfo> actionInfos = new HashMap<String, ActionInfo>();
    private final Map<String, Action> actions = new HashMap<String, Action>();

    public void spiAddActionInfo(ActionInfo actionInfo)
    {
        String actionId = actionInfo.getActionId();
        if (actionInfos.containsKey(actionId)) throw new ActionException("Duplicate action id " + actionId);
        actionInfos.put(actionId, actionInfo);
    }

    public Action getAction(String actionId, ActionContext context)
    {
        Action action = actions.get(actionId);
        if (action == null)
        {
            ActionInfo actionInfo = actionInfos.get(actionId);
            if (actionInfo == null) return null;

            ActionListener actionListener = newActionListener(actionInfo.getActionClassName());
            action = new Action(actionListener);
            actions.put(actionId, action);

            action.putValue(Action.ACTION_COMMAND_KEY, actionId);
            action.putValue(Action.NAME, actionInfo.getActionText());
            action.putValue(Action.SHORT_DESCRIPTION, actionInfo.getActionTooltip());

            String iconPath = actionInfo.getActionIconPath();
            if (iconPath != null)
            {
                URL iconURL = getClass().getClassLoader().getResource(iconPath);
                if (iconURL != null)
                {
                    ImageIcon icon = new ImageIcon(iconURL);
                    action.putValue(Action.SMALL_ICON, icon);
                }
            }

            String mnemonic = actionInfo.getActionMnemonic();
            if (mnemonic != null && mnemonic.length() > 0)
                action.putValue(Action.MNEMONIC_KEY, Integer.valueOf(mnemonic.charAt(0)));

            String accelerator = actionInfo.getActionAccelerator();
            if (accelerator != null) action.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accelerator));
        }
        action.setActionContext(context);
        return action;
    }

    private ActionListener newActionListener(String actionClassName)
    {
        try
        {
            ActionListener action = (ActionListener)Thread.currentThread().getContextClassLoader().loadClass(actionClassName).newInstance();
            containerManager.resolve(action);
            return action;
        }
        catch (Exception x)
        {
            throw new ActionException(x);
        }
    }
}
