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
package org.livetribe.ec2.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.SignatureException;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @version $Revision$ $Date$
 */
public final class Util
{
    private final static Logger LOGGER = LoggerFactory.getLogger(Util.class);
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    /**
     * Computes RFC 2104-compliant HMAC signature for Amazon EC2 API calls.
     *
     * @param data The data to be signed.
     * @param key  The signing key.
     * @return The base64-encoded RFC 2104-compliant HMAC signature.
     * @throws SignatureException when signature generation fails
     */
    public static String sign(Map<String, String> data, String key) throws SignatureException
    {
       if (LOGGER.isDebugEnabled()) LOGGER.debug("Data {} key {}", data, key);

        StringBuilder sortedData = new StringBuilder();
        Set<String> sorted = new TreeSet<String>(new Comparator<String>()
        {
            public int compare(String o1, String o2)
            {
                return o1.compareToIgnoreCase(o2);
            }
        });

        sorted.addAll(data.keySet());

        for (String k : sorted)
        {
            try
            {
                sortedData.append(k);
                sortedData.append(String.valueOf(URLDecoder.decode(data.get(k), "UTF-8")));
            }
            catch (UnsupportedEncodingException uee)
            {
                LOGGER.warn("Error URL decoding " + data.get(k), uee);
            }
        }

        String result;
        try
        {
            // get an hmac_sha1 key from the raw key bytes
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);

            // get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);

            // compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(sortedData.toString().getBytes());

            // base64-encode the hmac
            result = Base64Encoder.convert(rawHmac);

            if (LOGGER.isDebugEnabled()) LOGGER.debug("result: {}", result);
        }
        catch (Exception e)
        {
            LOGGER.error("Failed to generate HMAC : " + e.getMessage(), e);
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }

    private Util() { }
}
