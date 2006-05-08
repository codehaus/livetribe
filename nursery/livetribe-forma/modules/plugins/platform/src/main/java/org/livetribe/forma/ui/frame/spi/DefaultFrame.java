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
package org.livetribe.forma.ui.frame.spi;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.livetribe.forma.ui.menubar.MenubarContainer;
import org.livetribe.forma.ui.menubar.MenubarManager;
import org.livetribe.forma.ui.Part;
import org.livetribe.forma.ui.statusbar.StatusbarManager;
import org.livetribe.forma.ui.statusbar.StatusbarContainer;
import org.livetribe.forma.ui.perspective.PerspectiveManager;
import org.livetribe.forma.ui.perspective.PerspectiveContainer;
import org.livetribe.forma.ui.perspective.Perspective;
import org.livetribe.forma.ui.frame.FrameManager;
import org.livetribe.forma.ui.frame.Frame;
import org.livetribe.forma.i18n.InternationalizationManager;
import org.livetribe.forma.i18n.Bundle;
import org.livetribe.ioc.Inject;
import org.livetribe.ioc.PostConstruct;

/**
 * @version $Rev$ $Date$
 */
public class DefaultFrame extends JFrame implements Frame, MenubarContainer, StatusbarContainer, PerspectiveContainer
{
    @Inject private MenubarManager menubarManager;
    @Inject private FrameManager frameManager;
    @Inject private PerspectiveManager perspectiveManager;
    @Inject private StatusbarManager statusbarManager;
    private Bundle bundle;
    private Part perspective;
    private DefaultPerspective defaultPerspective = new DefaultPerspective();

    @Inject
    public void setTranslator(InternationalizationManager i18nManager)
    {
        this.bundle = i18nManager.getBundle("Frame", null);
    }

    @PostConstruct
    public void initComponents()
    {
        // We want to control window closing
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowEventListener());

        setTitle(bundle.get("application.title"));

        menubarManager.installMenuBar(this, MenubarManager.MENUBAR_ID);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(defaultPerspective, BorderLayout.CENTER);

        perspectiveManager.displayNewPerspective(this, perspectiveManager.getDefaultPerspectiveId());

        statusbarManager.installStatusBar(this, StatusbarManager.STATUSBAR_ID);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        Dimension size = new Dimension(screenWidth - (screenWidth >> 2), screenHeight - (screenHeight >> 2));
        setSize(size);
        setLocationRelativeTo(null);
    }

    public void spiDisplay()
    {
        setVisible(true);
    }

    public void spiUndisplay()
    {
        setVisible(false);
    }

    public void spiDisplay(Part part)
    {
        Container contentPane = getContentPane();
        if (part != null)
        {
            contentPane.add(part.spiGetComponent(), BorderLayout.CENTER);
            perspective = part;
        }
        else
        {
            if (perspective != null)
            {
                contentPane.remove(perspective.spiGetComponent());
                contentPane.add(defaultPerspective);
            }
            perspective = null;
        }
    }

    public boolean spiConfirmClose()
    {
        int result = JOptionPane.showConfirmDialog(this,
                bundle.get("application.confirm.close.message"),
                bundle.get("application.confirm.close.title"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        return JOptionPane.YES_OPTION == result;
    }

    public Perspective spiGetPerspective()
    {
        if (perspective instanceof Perspective) return (Perspective)perspective;
        return null;
    }

    private class WindowEventListener extends WindowAdapter
    {
        @Override
        public void windowClosing(WindowEvent e)
        {
            frameManager.closeFrame(DefaultFrame.this);
        }

        @Override
        public void windowActivated(WindowEvent e)
        {
            frameManager.setCurrentFrame(DefaultFrame.this);
        }
    }

    private class DefaultPerspective extends JPanel
    {
    }
}
