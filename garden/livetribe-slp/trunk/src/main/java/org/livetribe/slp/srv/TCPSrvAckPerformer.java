/*
 * Copyright 2005 the original author or authors
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
package org.livetribe.slp.srv;

import java.net.Socket;

import org.livetribe.slp.settings.Settings;
import org.livetribe.slp.srv.msg.Message;
import org.livetribe.slp.srv.msg.SrvAck;
import org.livetribe.slp.srv.net.TCPConnector;

/**
 * @version $Revision$ $Date$
 */
public class TCPSrvAckPerformer
{
    private final TCPConnector tcpConnector;

    public TCPSrvAckPerformer(TCPConnector tcpConnector, Settings settings)
    {
        this.tcpConnector = tcpConnector;
    }

    public void perform(Socket socket, Message message, int errorCode)
    {
        SrvAck srvAck = newSrvAck(message, errorCode);
        byte[] bytes = srvAck.serialize();
        tcpConnector.write(socket, bytes);
    }

    private SrvAck newSrvAck(Message message, int errorCode)
    {
        SrvAck srvAck = new SrvAck();
        srvAck.setXID(message.getXID());
        srvAck.setLanguage(message.getLanguage());
        srvAck.setErrorCode(errorCode);
        return srvAck;
    }
}
