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

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor
{
    private Rectangle screenSize;
    private Point windowSize;
    
    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer)
    {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer)
    {
        return new ApplicationActionBarAdvisor(configurer);
    }

    public void preWindowOpen()
    {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();

        screenSize = configurer.getWorkbenchConfigurer().getWorkbench().getDisplay().getClientArea();
        int width = screenSize.width - (screenSize.width >> 2);
        int height = screenSize.height - (screenSize.height >> 2);
        windowSize = new Point(width, height);
        configurer.setInitialSize(windowSize);

        configurer.setShowMenuBar(true);
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(false);
        configurer.setShowPerspectiveBar(true);
        configurer.setTitle(Messages.APPLICATION_TITLE_TEXT);
    }
    
    public void postWindowCreate()
    {
        // Workaround for bug #84938
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        Shell shell = configurer.getWindow().getShell();
        int x = (screenSize.width - windowSize.x) >> 1;
        int y = (screenSize.height - windowSize.y) >> 1;
        shell.setLocation(x, y);
    }
}
