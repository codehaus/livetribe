/**
 *  Copyright 2006 Simula Labs Inc., 4676 Admiralty Way, Suite 520 Marina del Rey, CA 90292 U.S.A. All rights reserved.
 */
package org.livetribe.arm.ra.hibernate;

import javax.resource.spi.ActivationSpec;
import javax.resource.spi.endpoint.MessageEndpointFactory;


/**
 * @version $Revision$ $Date$
 */
class EndpointActivationKey
{
    final private MessageEndpointFactory messageEndpointFactory;
    final private ActivationSpec activationSpec;

    EndpointActivationKey(MessageEndpointFactory messageEndpointFactory, ActivationSpec activationSpec)
    {
        assert messageEndpointFactory != null;
        assert activationSpec != null;

        this.messageEndpointFactory = messageEndpointFactory;
        this.activationSpec = activationSpec;
    }

    MessageEndpointFactory getMessageEndpointFactory()
    {
        return messageEndpointFactory;
    }

    ActivationSpec getActivationSpec()
    {
        return activationSpec;
    }

    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        final EndpointActivationKey that = (EndpointActivationKey) obj;

        /**
         * Per the 12.4.9 spec:
         *
         * MessageEndpointFactory does not implement equals()
         * ActivationSpec does not implement equals()
         */
        return activationSpec == that.activationSpec && messageEndpointFactory == that.messageEndpointFactory;
    }

    public int hashCode()
    {
        return messageEndpointFactory.hashCode() ^ activationSpec.hashCode();
    }
}
