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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import org.livetribe.forma.ui.action.Action;
import org.livetribe.forma.ui.action.ActionCommand;
import org.livetribe.forma.ui.action.ActionException;
import org.livetribe.forma.ui.action.ActionManager;
import org.livetribe.ioc.Container;
import org.livetribe.ioc.Inject;

/**
 * @version $Rev$ $Date$
 */
public class DefaultActionManager implements ActionManager
{
    private final Logger logger = Logger.getLogger(getClass().getName());
    @Inject
    private Container containerManager;
    private final Map<String, ActionInfo> actionInfos = new HashMap<String, ActionInfo>();
    private final Map<String, ActionCommand> actionCommands = new HashMap<String, ActionCommand>();

    public void spiAddActionInfo(ActionInfo actionInfo)
    {
        String actionId = actionInfo.getActionId();
        if (actionInfos.containsKey(actionId)) throw new ActionException("Duplicate action id " + actionId);
        actionInfos.put(actionId, actionInfo);
    }

    public ActionCommand getActionCommand(String actionId)
    {
        ActionCommand actionCommand = actionCommands.get(actionId);
        if (actionCommand == null)
        {
            ActionInfo actionInfo = actionInfos.get(actionId);
            if (actionInfo == null)
            {
                if (logger.isLoggable(Level.INFO)) logger.info("Could not find action with id: " + actionId);
                return null;
            }

            Action action = newAction(actionInfo.getActionClassName());
            actionCommand = new ActionCommand(action);
            actionCommands.put(actionId, actionCommand);

            setProperties(action, actionInfo.getProperties());

            actionCommand.spiSetCommand(actionId);
            actionCommand.spiSetText(actionInfo.getActionText());
            actionCommand.spiSetTooltip(actionInfo.getActionTooltip());

            String iconPath = actionInfo.getActionIconPath();
            if (iconPath != null)
            {
                URL iconURL = getClass().getClassLoader().getResource(iconPath);
                if (iconURL != null)
                {
                    ImageIcon icon = new ImageIcon(iconURL);
                    actionCommand.spiSetIcon(icon);
                }
            }

            String mnemonic = actionInfo.getActionMnemonic();
            if (mnemonic != null && mnemonic.length() > 0)
                actionCommand.spiSetMnemonic(mnemonic.charAt(0));

            String accelerator = actionInfo.getActionAccelerator();
            if (accelerator != null) actionCommand.spiSetAccelerator(KeyStroke.getKeyStroke(accelerator));
        }
        return actionCommand;
    }

    private Action newAction(String actionClassName)
    {
        try
        {
            Action action = (Action)Thread.currentThread().getContextClassLoader().loadClass(actionClassName).newInstance();
            containerManager.resolve(action);
            return action;
        }
        catch (Exception x)
        {
            throw new ActionException(x);
        }
    }

    private void setProperties(Object target, Map<String, Object> properties)
    {
        BeanInfo beanInfo = getBeanInfo(target.getClass());
        if (beanInfo == null) return;

        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        for (Map.Entry<String, Object> entry : properties.entrySet())
        {
            String propertyName = entry.getKey();
            for (PropertyDescriptor descriptor : descriptors)
            {
                if (descriptor.getName().equals(propertyName))
                {
                    Method setter = descriptor.getWriteMethod();
                    if (setter != null)
                    {
                        setProperty(target, setter, entry.getValue());
                    }
                }
            }
        }
    }

    private BeanInfo getBeanInfo(Class<?> cls)
    {
        try
        {
            return Introspector.getBeanInfo(cls);
        }
        catch (IntrospectionException x)
        {
            if (logger.isLoggable(Level.FINE)) logger.log(Level.FINE, "Could not retrieve BeanInfo information for " + cls, x);
            return null;
        }
    }

    private void setProperty(Object target, Method setter, Object value)
    {
        try
        {
            setter.invoke(target, value);
        }
        catch (Exception x)
        {
            if (logger.isLoggable(Level.FINE)) logger.log(Level.FINE, "Could not invoke setter " + setter + " on " + target + ", ignoring", x);
        }
    }
}
