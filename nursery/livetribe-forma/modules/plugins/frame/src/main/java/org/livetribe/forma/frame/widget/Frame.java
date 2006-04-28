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
package org.livetribe.forma.frame.widget;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.livetribe.forma.frame.FrameService;
import org.livetribe.forma.frame.MenuBarService;
import org.livetribe.forma.frame.action.CloseFrameAction;
import org.livetribe.forma.frame.action.ExitApplicationAction;
import org.livetribe.forma.frame.action.NewFrameAction;
import org.livetribe.forma.frame.i18n.I18N;
import org.livetribe.forma.frame.i18n.I18NService;
import org.livetribe.ioc.IOCService;
import org.livetribe.ioc.Inject;
import org.livetribe.ioc.PostConstruct;

/**
 * @version $Rev$ $Date$
 */
public class Frame extends JFrame
{
    private FrameService frameService;
    private I18N bundle;
    private MenuBarService menuBarService;
    private IOCService iocService;
    private DashBoard dashBoard;
    private StatusBar statusBar;

    public Frame(String title) throws HeadlessException
    {
        super(title);
    }

    @Inject
    public void setFrameService(FrameService frameService)
    {
        this.frameService = frameService;
    }

    @Inject
    public void setI18NService(I18NService i18nService)
    {
        this.bundle = i18nService.getI18N(getClass());
    }

    @Inject
    public void setMenuBarService(MenuBarService menuBarService)
    {
        this.menuBarService = menuBarService;
    }

    @Inject
    public void setIOCService(IOCService iocService)
    {
        this.iocService = iocService;
    }

    @PostConstruct
    private void initComponents()
    {
        // We want to control window closing
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowEventListener());

        setupMenuBar();
        setupDashBoard();
        setupStatusBar();

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(dashBoard, BorderLayout.CENTER);
        contentPane.add(statusBar, BorderLayout.SOUTH);

        pack();

        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public void display()
    {
        setVisible(true);
    }

    public void undisplay()
    {
        setVisible(false);
    }

    public DashBoard getDashBoard()
    {
        return dashBoard;
    }

    public boolean confirmClose()
    {
        int result = JOptionPane.showConfirmDialog(this,
                bundle.get("application.confirm.close.message"),
                bundle.get("application.confirm.close.title"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        return JOptionPane.YES_OPTION == result;
    }

    private void setupMenuBar()
    {
        JMenu file = menuBarService.getMenu(this, "menubar.menu.file");
        if (file == null) file = menuBarService.addMenu(null, "menubar.menu.file", bundle);

        JMenuItem newFrame = menuBarService.getMenuItem(this, "menubar.menu.file.menuitem.newframe");
        if (newFrame == null)
        {
            NewFrameAction action = new NewFrameAction();
            iocService.resolve(action);
            menuBarService.addMenuItem(file, "menubar.menu.file.menuitem.newframe", bundle, action);
        }

        menuBarService.addMenuItemSeparator(file);

        JMenuItem close = menuBarService.getMenuItem(this, "menubar.menu.file.menuitem.close");
        if (close == null)
        {
            CloseFrameAction action = new CloseFrameAction();
            iocService.resolve(action);
            menuBarService.addMenuItem(file, "menubar.menu.file.menuitem.close", bundle, action);
        }

        menuBarService.addMenuItemSeparator(file);

        JMenuItem exit = menuBarService.getMenuItem(this, "menubar.menu.file.menuitem.exit");
        if (exit == null)
        {
            ExitApplicationAction action = new ExitApplicationAction();
            iocService.resolve(action);
            menuBarService.addMenuItem(file, "menubar.menu.file.menuitem.exit", bundle, action);
        }
    }

    private void setupDashBoard()
    {
        dashBoard = new DashBoard();
        iocService.resolve(dashBoard);
    }

    private void setupStatusBar()
    {
        statusBar = new StatusBar();
        iocService.resolve(statusBar);

        GarbageCollectorButton gcButton = new GarbageCollectorButton();
        iocService.resolve(gcButton);
        statusBar.append(gcButton);

        statusBar.appendSeparator();

        MemoryBar memoryBar = new MemoryBar();
        iocService.resolve(memoryBar);
        statusBar.append(memoryBar);
        gcButton.addActionListener(memoryBar);

        statusBar.appendSeparator();

        // TODO: append swing errors icon
    }

    public void setStatusText(String text)
    {
        statusBar.setText(text);
    }

    private class WindowEventListener extends WindowAdapter
    {
        @Override
        public void windowClosing(WindowEvent e)
        {
            frameService.closeFrame(Frame.this);
        }

        @Override
        public void windowActivated(WindowEvent e)
        {
            frameService.setCurrentFrame(Frame.this);
        }
    }
}
