/**
 *
 * Copyright 2008 (C) The original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.livetribe.s3.rest.v20060301;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.net.URL;


/**
 * A restful implementation of the <code>EC2API</code> interface.
 *
 * @version $Revision$ $Date: 2008-06-29 20:37:33 -0700 (Sun, 29 Jun 2008) $
 */
public final class RestEC2API
{
    private final static String VERSION = "2006-03-01";
    private final JAXBContext context20060301;
    private final JAXBContext context;
    private final URL url;
    private final String AWSAccessKeyId;
    private final String secretAccessKey;
    private int timeout = 60;

    /**
     * A restful implementation of the <code>EC2API</code> interface
     *
     * @param url             the URL of the Amazon EC2 restful API
     * @param AWSAccessKeyId  the Access Key ID for the request sender. This identifies the account which will be charged for usage of the service. The account with which the Access Key ID is associated must be signed up for Amazon EC2, or requests will not be accepted.
     * @param secretAccessKey the Secret Access Key is used to calculate a signature the requests
     * @throws JAXBException if unable to create JAXB Contexts for org.livetribe.ec2.jaxb or org.livetribe.ec2.jaxb.v20080201
     */
    public RestEC2API(URL url, String AWSAccessKeyId, String secretAccessKey) throws JAXBException
    {
        if (url == null) throw new IllegalArgumentException("url cannot be null");
        if (AWSAccessKeyId == null) throw new IllegalArgumentException("AWSAccessKeyId cannot be null");
        if (secretAccessKey == null) throw new IllegalArgumentException("secretAccessKey cannot be null");

        this.url = url;
        this.AWSAccessKeyId = AWSAccessKeyId;
        this.secretAccessKey = secretAccessKey;
        this.context = JAXBContext.newInstance("org.livetribe.s3.jaxb");
        this.context20060301 = JAXBContext.newInstance("org.livetribe.s3.jaxb.v20060301");
    }

    /**
     * Get how long to wait for a reply in seconds
     *
     * @return how long to wait for a reply in seconds
     */
    public int getTimeout()
    {
        return timeout;
    }

    /**
     * Set how long to wait for a reply in seconds
     *
     * @param timeout how long to wait for a reply in seconds
     */
    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }
}
