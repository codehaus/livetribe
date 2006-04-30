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

import org.livetribe.forma.frame.IFrameServiceSpi;
import org.livetribe.forma.frame.IFrameSpi;
import org.livetribe.forma.frame.IMenuBarService;
import org.livetribe.forma.frame.action.CloseFrameAction;
import org.livetribe.forma.frame.action.ExitApplicationAction;
import org.livetribe.forma.frame.action.NewFrameAction;
import org.livetribe.forma.platform.i18n.Bundle;
import org.livetribe.forma.platform.i18n.InternationalizationService;
import org.livetribe.ioc.IOCService;
import org.livetribe.ioc.Inject;
import org.livetribe.ioc.PostConstruct;

/**
 * @version $Rev$ $Date$
 */
public class Frame extends JFrame implements IFrameSpi
{
    private IFrameServiceSpi frameService;
    private Bundle bundle;
    private IMenuBarService menuBarService;
    private IOCService iocService;
    private DashBoard dashBoard;
    private StatusBar statusBar;

    public Frame(String title) throws HeadlessException
    {
        super(title);
    }

    @Inject
    public void setFrameService(IFrameServiceSpi frameService)
    {
        this.frameService = frameService;
    }

    @Inject
    public void setI18NService(InternationalizationService i18nService)
    {
        this.bundle = i18nService.getBundle(getClass());
    }

    @Inject
    public void setMenuBarService(IMenuBarService menuBarService)
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

    public void revalidate()
    {
        validate();
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
        String fileKey = "menubar.menu.file";
        JMenu file = menuBarService.getMenu(this, fileKey);
        if (file == null) file = menuBarService.addMenu(null, fileKey, bundle);

        String newFrameKey = "menubar.menu.file.menuitem.newframe";
        JMenuItem newFrame = menuBarService.getMenuItem(this, newFrameKey);
        if (newFrame == null)
        {
            NewFrameAction action = new NewFrameAction();
            iocService.resolve(action);
            menuBarService.addMenuItem(file, newFrameKey, bundle, action);
        }

        file.addSeparator();

        String closeFrameKey = "menubar.menu.file.menuitem.close";
        JMenuItem close = menuBarService.getMenuItem(this, closeFrameKey);
        if (close == null)
        {
            CloseFrameAction action = new CloseFrameAction();
            iocService.resolve(action);
            menuBarService.addMenuItem(file, closeFrameKey, bundle, action);
        }

        file.addSeparator();

        String exitKey = "menubar.menu.file.menuitem.exit";
        JMenuItem exit = menuBarService.getMenuItem(this, exitKey);
        if (exit == null)
        {
            ExitApplicationAction action = new ExitApplicationAction();
            iocService.resolve(action);
            menuBarService.addMenuItem(file, exitKey, bundle, action);
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
