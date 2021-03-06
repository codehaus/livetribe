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
package org.livetribe.forma.ui.perspective;

import java.awt.Component;
import javax.swing.JPanel;

import org.livetribe.forma.ui.Context;
import org.livetribe.forma.ui.PartContainer;

/**
 * @version $Rev$ $Date$
 */
public abstract class AbstractPerspective extends JPanel implements Perspective
{
    private PartContainer container;

    public Component spiGetComponent()
    {
        return this;
    }

    public void spiDisplayIn(PartContainer partContainer, Object constraint)
    {
        this.container = partContainer;
        partContainer.spiDisplay(this, constraint);
    }

    public void spiLoad(Context context)
    {
    }

    public void spiOpen(Context context)
    {
    }

    public void spiSave()
    {
    }

    public void spiClose()
    {
    }

    public void spiUndisplay()
    {
        if (container == null) return;
        container.spiDisplay(null, null);
        container = null;
    }
}
