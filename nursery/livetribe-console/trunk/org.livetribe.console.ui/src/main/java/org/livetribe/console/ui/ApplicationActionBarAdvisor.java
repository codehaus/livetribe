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
package org.livetribe.console.ui;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of the actions added to a workbench window.
 * Each window will be populated with new actions.
 */
class ApplicationActionBarAdvisor extends ActionBarAdvisor
{
    private IWorkbenchAction exitAction;
    private IContributionItem perspectivesAction;
	private IContributionItem viewsAction;

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer)
    {
        super(configurer);
    }

    protected void makeActions(final IWorkbenchWindow window)
    {
        exitAction = ActionFactory.QUIT.create(window);
        exitAction.setText(Messages.MENUITEM_EXIT_TEXT);
        exitAction.setImageDescriptor(Activator.getImageDescriptor(Messages.MENUITEM_EXIT_IMAGE));
        register(exitAction);
        
        perspectivesAction = ContributionItemFactory.PERSPECTIVES_SHORTLIST.create(window);

        viewsAction = ContributionItemFactory.VIEWS_SHORTLIST.create(window);
    }

    @Override
    protected void fillMenuBar(IMenuManager menuBar)
    {
        MenuManager fileMenu = new MenuManager(Messages.MENU_FILE_TEXT, IWorkbenchActionConstants.M_FILE);
        menuBar.add(fileMenu);

        fileMenu.add(new GroupMarker("new"));
        fileMenu.add(new Separator());
        
        fileMenu.add(exitAction);

        menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
        
        MenuManager windowMenu = new MenuManager(Messages.MENU_WINDOW_TEXT, IWorkbenchActionConstants.M_WINDOW);
        menuBar.add(windowMenu);
        
        MenuManager perspectivesMenu = new MenuManager(Messages.MENU_PERSPECTIVES_TEXT, "perspectives");
        windowMenu.add(perspectivesMenu);
        perspectivesMenu.add(perspectivesAction);
        
        MenuManager viewsMenu = new MenuManager(Messages.MENU_VIEWS_TEXT, "views");
        windowMenu.add(viewsMenu);
        viewsMenu.add(viewsAction);
    }

    @Override
    protected void fillCoolBar(ICoolBarManager coolBar)
    {
    }
}
