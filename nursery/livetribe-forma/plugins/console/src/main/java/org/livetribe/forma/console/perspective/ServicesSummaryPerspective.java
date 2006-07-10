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
package org.livetribe.forma.console.perspective;

import java.util.List;
import java.util.prefs.Preferences;

import org.livetribe.forma.console.action.ScanNetworkWithSLPAction;
import org.livetribe.forma.ui.Context;
import org.livetribe.forma.ui.Part;
import org.livetribe.forma.ui.perspective.AbstractPerspective;
import org.livetribe.ioc.PostConstruct;

/**
 * @version $Rev$ $Date$
 */
public class ServicesSummaryPerspective extends AbstractPerspective
{
    public static final String ID = ServicesSummaryPerspective.class.getName();
    private Preferences preferences;

    public String getPerspectiveId()
    {
        return ID;
    }

    @PostConstruct
    private void initComponents()
    {
        // TODO: initialize the layout, borders, etc; load of the UI is done in spiLoad
    }

    public void spiDisplay(Part part, Object constraint)
    {
        // TODO:
    }

    @Override
    public void spiLoad(Context context)
    {
        preferences = Preferences.userNodeForPackage(getClass()).node(getClass().getSimpleName());
        // TODO: load perspective configuration for views and editors, and eventually for other things
    }

    @Override
    public void spiOpen(Context context)
    {
        List nodes = (List)context.get(ScanNetworkWithSLPAction.RESULT);

    }

    @Override
    public void spiClose()
    {
        super.spiClose();
    }
}
