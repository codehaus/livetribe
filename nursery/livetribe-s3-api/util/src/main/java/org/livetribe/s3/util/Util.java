/**
 *
 * Copyright 2008-2011(C) The original author or authors
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
package org.livetribe.s3.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @version $Revision$ $Date: 2008-06-29 20:37:33 -0700 (Sun, 29 Jun 2008) $
 */
public final class Util
{
    private final static Logger LOGGER = LoggerFactory.getLogger(Util.class);
    private final static SimpleDateFormat RFC822FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy H:mm:ss Z");
    private final static SimpleDateFormat ISO8601FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    /**
     * Format the calendar instance using ISO 8601 standard
     *
     * @param calendar the instance to be formatted
     * @return the formatted calendar instance using ISO 8601 standard
     */
    public static String iso8601Format(Calendar calendar)
    {
        return iso8601Format(calendar.getTime());
    }

    /**
     * Format the date instance using ISO 8601 standard
     *
     * @param date the instance to be formatted
     * @return the formatted date instance using ISO 8601 standard
     */
    public static String iso8601Format(Date date)
    {
        return ISO8601FORMAT.format(date);
    }

    /**
     * Format the calendar instance using RFC 822 standard
     *
     * @param calendar the instance to be formatted
     * @return the formatted calendar instance using RFC 822 standard
     */
    public static String rfc822Format(Calendar calendar)
    {
        return iso8601Format(calendar.getTime());
    }

    /**
     * Format the date instance using RFC 822 standard
     *
     * @param date the instance to be formatted
     * @return the formatted date instance using RFC 822 standard
     */
    public static String rfc822Format(Date date)
    {
        return RFC822FORMAT.format(date);
    }

    private Util() { }
}
