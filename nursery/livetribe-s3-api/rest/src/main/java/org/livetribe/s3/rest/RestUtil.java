/**
 *
 * Copyright 2008-2011 (C) The original author or authors
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
package org.livetribe.s3.rest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.livetribe.s3.api.S3Exception;
import org.livetribe.s3.util.Base64Encoder;

import org.apache.ahc.api.HttpVerb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @version $Revision$ $Date$
 */
public class RestUtil
{
    private final static Logger LOGGER = LoggerFactory.getLogger(RestUtil.class);
    private final static String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    /**
     * Computes RFC 2104-compliant HMAC signature for Amazon S3 API calls.
     *
     * @param headers HTTP headers
     * @param key     The signing key.
     * @param verb    The HTTP verb.
     * @param path    The path to be signed.
     * @return The base64-encoded RFC 2104-compliant HMAC signature.
     * @throws S3Exception when signature generation fails
     */
    public static String sign(Map<String, List<String>> headers, String key,
                              HttpVerb verb,
                              String path) throws S3Exception
    {
        if (verb == null) throw new IllegalArgumentException("verb is null");
        if (path == null) throw new IllegalArgumentException("date is null");

        LOGGER.debug("Data {} key {}", headers, key);

        StringBuilder sortedData = new StringBuilder();
        Set<String> sorted = new TreeSet<String>(new Comparator<String>()
        {
            public int compare(String o1, String o2)
            {
                return o1.compareToIgnoreCase(o2);
            }
        });

        sorted.addAll(headers.keySet());

        String date = null;
        String md5Hash = null;
        String contentType = null;
        boolean hasXAmzDate = false;
        Map<String, List<String>> intermediate = new HashMap<String, List<String>>();
        for (String k : sorted)
        {
            String lowercasKey = k.toLowerCase();
            if (lowercasKey.startsWith("x-amz-"))
            {
                List<String> values = intermediate.get(lowercasKey);

                if (values == null) intermediate.put(lowercasKey, values = new ArrayList<String>());

                for (String value : headers.get(k)) values.add(unfold(value));

                if ("x-amz-date".equals(lowercasKey)) hasXAmzDate = true;
            }
            else if (lowercasKey.equals("content-type"))
            {
                contentType = headers.get(k).get(0);
            }
            else if (lowercasKey.equals("content-md5"))
            {
                md5Hash = headers.get(k).get(0);
            }
            else if (lowercasKey.equals("date"))
            {
                date = headers.get(k).get(0);
            }
        }

        if (!hasXAmzDate && date == null) throw new IllegalArgumentException("date is null and x-amz-date has not been set");

        StringBuilder stringToSign = new StringBuilder();

        stringToSign.append(verb).append("\n");
        stringToSign.append(md5Hash == null ? "" : md5Hash).append("\n");
        stringToSign.append(contentType == null ? "" : contentType).append("\n");
        stringToSign.append(hasXAmzDate ? "" : date).append("\n");

        for (String k : sorted)
        {
            String lowercasKey = k.toLowerCase();
            if (lowercasKey.startsWith("x-amz-"))
            {
                List<String> values = intermediate.get(lowercasKey);

                stringToSign.append(lowercasKey).append(":");

                stringToSign.append(values.get(0));

                if (values.size() > 1)
                {
                    for (int i = 1; i < values.size(); i++) stringToSign.append(",").append(values.get(i));
                }

                stringToSign.append("\n");
            }
        }

        stringToSign.append(path);

        String result;
        try
        {
            // get an hmac_sha1 key from the raw key bytes
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);

            // get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);

            // compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(stringToSign.toString().getBytes());

            // base64-encode the hmac
            result = Base64Encoder.convert(rawHmac);

            if (LOGGER.isDebugEnabled()) LOGGER.debug("result: {}", result);
        }
        catch (Exception e)
        {
            throw new S3Exception("Failed to generate HMAC", e);
        }

        return result;
    }

    /**
     * "Un-fold" long headers that span multiple lines (as allowed by RFC 2616,
     * section 4.2) by replacing the folding white-space (including new-line)
     * by a single space.
     *
     * @param string the header value to be unfolded
     * @return an unfolded header value
     */
    public static String unfold(String string)
    {
        StringBuilder buffer = new StringBuilder();

        boolean inWhitespace = false;
        for (char c : string.toCharArray())
        {
            switch (c)
            {
                case '\n':
                case ' ':
                    inWhitespace = true;
                    break;
                default:
                    if (inWhitespace)
                    {
                        buffer.append(' ');
                        inWhitespace = false;
                    }
                    buffer.append(c);
            }
        }

        return buffer.toString();
    }

    private RestUtil() { }
}
