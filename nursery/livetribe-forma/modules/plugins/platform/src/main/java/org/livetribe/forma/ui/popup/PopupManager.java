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
package org.livetribe.forma.ui.popup;

import java.awt.Component;

import org.livetribe.forma.ui.action.ActionContext;
import org.livetribe.forma.ui.popup.spi.PopupInfo;

/**
 * @version $Rev$ $Date$
 */
public interface PopupManager
{
    public static final String ID = PopupManager.class.getName();

    public void spiAddPopupInfo(PopupInfo popupInfo);

    public void displayPopupMenu(String popupId, ActionContext context, Component component, int x, int y);
}
