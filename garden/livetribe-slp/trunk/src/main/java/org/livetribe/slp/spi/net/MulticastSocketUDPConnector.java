/*
 * Copyright 2007-2008 the original author or authors
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
package org.livetribe.slp.spi.net;

import org.livetribe.slp.settings.Defaults;
import static org.livetribe.slp.settings.Keys.*;
import org.livetribe.slp.settings.Settings;

/**
 * @version $Revision$ $Date$
 */
public class MulticastSocketUDPConnector extends SocketUDPConnector
{
    private String multicastAddress = Defaults.get(MULTICAST_ADDRESS_KEY);

    public MulticastSocketUDPConnector(Settings settings)
    {
        super(settings);
        if (settings != null) setSettings(settings);
    }

    private void setSettings(Settings settings)
    {
        if (settings.containsKey(MULTICAST_ADDRESS_KEY)) setMulticastAddress(settings.get(MULTICAST_ADDRESS_KEY));
    }

    public void setMulticastAddress(String multicastAddress)
    {
        this.multicastAddress = multicastAddress;
    }

    protected String getManycastAddress()
    {
        return multicastAddress;
    }
}
