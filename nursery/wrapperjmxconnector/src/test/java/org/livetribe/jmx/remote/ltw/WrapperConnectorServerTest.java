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
package org.livetribe.jmx.remote.ltw;

import java.util.HashMap;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.Map;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerFactory;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import junit.framework.TestCase;
import org.livetribe.jmx.util.I18NJMX;

/**
 * @version $Revision$ $Date$
 */
public class WrapperConnectorServerTest extends TestCase
{
    public void test() throws Exception
    {
        String config = "<?xml version=\"1.0\"?>";

        Map serverEnv = new HashMap();
        serverEnv.put(WrapperConnectorServer.CONFIG_RESOURCE_ATTRIBUTE, config);
        serverEnv.put(JMXConnectorServerFactory.PROTOCOL_PROVIDER_PACKAGES, "org.livetribe.jmx.remote.provider");

        MBeanServer mbeanServer = MBeanServerFactory.createMBeanServer();

        ObjectName mbeanName = ObjectName.getInstance(":Type=I18N");
        mbeanServer.registerMBean(new I18N(), mbeanName);

        JMXServiceURL jmxServiceURL = new JMXServiceURL("service:jmx:ltw:////rmi:///");
        JMXConnectorServer connectorServer = JMXConnectorServerFactory.newJMXConnectorServer(jmxServiceURL, serverEnv, mbeanServer);
        connectorServer.start();

        JMXConnector connector = null;
        try
        {
            Map clientEnv = new HashMap();
            clientEnv.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES, "org.livetribe.jmx.remote.provider");
            connector = JMXConnectorFactory.connect(connectorServer.getAddress(), clientEnv);
            MBeanServerConnection connection = connector.getMBeanServerConnection();
            I18NMBean mbean = (I18NMBean)MBeanServerInvocationHandler.newProxyInstance(connection, mbeanName, I18NMBean.class, false);

            // No Locale from the client
            String result = mbean.getTranslated();
            assertEquals(I18N.WELCOME_EN, result);

            // Set Locale from the client
            I18NJMX.setLocale(Locale.ITALIAN);
            result = mbean.getTranslated();
            assertEquals(I18N.WELCOME_IT, result);

            // Set unsupported locale from the client
            I18NJMX.setLocale(Locale.GERMAN);
            result = mbean.getTranslated();
            assertEquals(I18N.WELCOME_EN, result);

            // Reset Locale
            I18NJMX.setLocale(null);
            result = mbean.getTranslated();
            assertEquals(I18N.WELCOME_EN, result);

            // Translate on client side
            I18NJMX.setLocale(I18NJMX.CLIENT);
            result = mbean.getTranslated();
            assertEquals(I18N.WELCOME_KEY, result);
        }
        finally
        {
            if (connector != null) connector.close();
            connectorServer.stop();
        }
    }

    public static interface I18NMBean
    {
        public String getTranslated();
    }

    public static class I18N implements I18NMBean
    {
        private static final String WELCOME_KEY = "welcome.key";
        private static final String WELCOME_EN = "Welcome!";
        private static final String WELCOME_IT = "Benvenuto!";

        public String getTranslated()
        {
            return I18NJMX.translate(WELCOME_KEY, WELCOME_EN, "org.livetribe.jmx.remote.ltw.WrapperConnectorServerTest$Bundle");
        }
    }

    public static class Bundle_it extends ListResourceBundle
    {
        protected Object[][] getContents()
        {
            return new Object[][]{{I18N.WELCOME_KEY, I18N.WELCOME_IT}};
        }
    }
}
