/**
 *
 * Copyright 2006 (C) The original author or authors
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
package org.livetribe.arm40.connection.activemq;

import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.opengroup.arm40.metric.ArmMetric;
import org.opengroup.arm40.metric.ArmMetricCounter32;
import org.opengroup.arm40.metric.ArmMetricCounter32Definition;
import org.opengroup.arm40.metric.ArmMetricDefinition;
import org.opengroup.arm40.metric.ArmMetricFactory;
import org.opengroup.arm40.metric.ArmMetricGauge32;
import org.opengroup.arm40.metric.ArmMetricGauge32Definition;
import org.opengroup.arm40.metric.ArmMetricGroup;
import org.opengroup.arm40.metric.ArmMetricGroupDefinition;
import org.opengroup.arm40.metric.ArmMetricNumericId32;
import org.opengroup.arm40.metric.ArmMetricNumericId32Definition;
import org.opengroup.arm40.metric.ArmTransactionWithMetrics;
import org.opengroup.arm40.metric.ArmTransactionWithMetricsDefinition;
import org.opengroup.arm40.transaction.ArmApplication;
import org.opengroup.arm40.transaction.ArmApplicationDefinition;
import org.opengroup.arm40.transaction.ArmConstants;
import org.opengroup.arm40.transaction.ArmCorrelator;
import org.opengroup.arm40.transaction.ArmErrorCallback;
import org.opengroup.arm40.transaction.ArmIdentityPropertiesTransaction;
import org.opengroup.arm40.transaction.ArmInterface;
import org.opengroup.arm40.transaction.ArmTransaction;
import org.opengroup.arm40.transaction.ArmTransactionDefinition;
import org.opengroup.arm40.transaction.ArmTransactionFactory;
import org.opengroup.arm40.transaction.ArmUser;
import org.springframework.context.ConfigurableApplicationContext;

import org.livetribe.arm40.bean.KnitPointBean;
import org.livetribe.arm40.connection.Connection;
import org.livetribe.arm40.connection.ConnectionMonitor;
import org.livetribe.arm40.connection.StaticConnectionMonitor;

/**
 * @version $Revision: $ $Date: $
 */
public class ActiveMQConnectionTest extends TestCase implements ArmErrorCallback, ConnectionMonitor
{
    Throwable error;

    ConfigurableApplicationContext context;
    ConsumerBean consumer;

    AtmCustomer[] customers = new AtmCustomer[]{
            new AtmCustomer(1, "Mueller", 1234, 150.0, "EUR"),
            new AtmCustomer(2, "Miller", 5678, 100.0, "GBP"),
            new AtmCustomer(3, "Meyer", 4711, 200.0, "USD")
    };
    static final int ATM_ERR_NONE = 0;
    static final int ATM_ERR_INVALID_NO = 1;
    static final int ATM_ERR_INVALID_PIN = 2;
    static final int ATM_ERR_OVERDRAWN = 3;
    static final int ATM_ERR_MAX = 4;
    static final String[] errors = new String[]{
            "no error",
            "invalid customer number",
            "invalid pin number",
            "account overdrawn"
    };

    /**
     * --------------------------------------------------------------
     * ---------------------- ARM objects ---------------------------
     * --------------------------------------------------------------
     */

    // factories
    ArmTransactionFactory tranFactory;
    ArmMetricFactory metricFactory;

    // application definitions
    ArmApplicationDefinition appDef;

    // transaction definitions

    ArmTransactionDefinition atmTranDef;
    ArmTransactionWithMetricsDefinition checkCustomerTranDef;
    ArmTransactionWithMetricsDefinition withdrawTranDef;

    // identity properties
    ArmIdentityPropertiesTransaction withdrawIdProperties;

    // metric definitions
    ArmMetricNumericId32Definition customerNoMetDef;
    ArmMetricNumericId32Definition customerPinMetDef;
    ArmMetricGroupDefinition checkCustomerMetGroupDef;

    ArmMetricCounter32Definition amountMetDef;
    ArmMetricGauge32Definition oldBalanceMetDef;
    ArmMetricGauge32Definition newBalanceMetDef;
    ArmMetricGroupDefinition withdrawMetGroupDef;

    // application instance
    ArmApplication app;

    // transaction instances
    ArmTransaction atmTran;
    ArmTransactionWithMetrics checkCustomerTran;
    ArmTransactionWithMetrics withdrawTran;

    // metric objects
    ArmMetricNumericId32 customerNoMet;
    ArmMetricNumericId32 customerPinMet;
    ArmMetricGroup checkCustomerMetGroup;

    ArmMetricCounter32 amountMet;
    ArmMetricGauge32 oldBalanceMet;
    ArmMetricGauge32 newBalanceMet;
    ArmMetricGroup withdrawMetGroup;

    // correlators for parent transactions
    ArmCorrelator atmCorr;
    ArmCorrelator checkCustomerCorr;

    public void testNothing() throws Throwable
    {
    }

    public void XtestFactories() throws Throwable
    {
        assertNotNull(tranFactory);
        assertNotNull(metricFactory);

        if (error != null) throw error;
    }


    public void XtestSomething() throws Throwable
    {
        List messages = consumer.flushMessages();

        if (error != null) throw error;
    }

    public void XtestRun() throws Throwable
    {
        // Now start the transaction.
        atmTran.start();
        atmCorr = atmTran.getCorrelator();

        System.out.println("atm:");
        System.out.println("----");

        checkCustomer(1, 1234, 5.0);

        System.out.println("bye.");

        // Stop the measurement for the atm transaction and commit
        // it to ARM.
        atmTran.stop(ArmConstants.STATUS_GOOD);

        List messages = consumer.flushMessages();

        if (error != null) throw error;
    }

    public void XsetUp() throws Exception
    {
        error = null;

        System.setProperty(ArmTransactionFactory.propertyKey, "org.livetribe.arm40.impl.LTTransactionFactory");
        System.setProperty(ArmMetricFactory.propertyKey, "org.livetribe.arm40.impl.LTMetricFactory");

        context = new ClassPathXmlApplicationContext("/org/livetribe/arm40/connection/activemq/ActiveMQConnectionTest.xml");

        consumer = (ConsumerBean) context.getBean("consumer");
        consumer.start();

        Thread.sleep(100);

        consumer.flushMessages();

        initFactories();
        armInit();
    }

    public void XtearDown() throws Exception
    {
        app.end();
        appDef.destroy();

        consumer.flushMessages();
        consumer.stop();

        KnitPointBean factory = (KnitPointBean) context.getBean("armKnitPoint");
        factory.setConnection(null);

        context.close();
    }

    public ActiveMQConnectionTest()
    {
        StaticConnectionMonitor.addConnectionMonitor(this);
    }

    ArmTransactionFactory getTranFactory() throws Exception
    {
        Properties p = System.getProperties();
        String tranFactoryName = p.getProperty(ArmTransactionFactory.propertyKey);
        if (tranFactoryName == null)
        {
            System.err.println("Could't getProperty " + ArmTransactionFactory.propertyKey);
            return null;
        }
        Class tranFactoryClass;
        ArmTransactionFactory tranFactory;

        tranFactoryClass = Class.forName(tranFactoryName);

        tranFactory = (ArmTransactionFactory) tranFactoryClass.newInstance();

        tranFactory.setErrorCallback(this);

        return tranFactory;
    }

    ArmMetricFactory getMetricFactory() throws Exception
    {
        Properties p = System.getProperties();
        String metFactoryName = p.getProperty(ArmMetricFactory.propertyKey);
        if (metFactoryName == null)
        {
            System.err.println("Could't getProperty " + ArmMetricFactory.propertyKey);
            return null;
        }
        Class metFactoryClass;
        ArmMetricFactory metFactory;

        metFactoryClass = Class.forName(metFactoryName);
        metFactory = (ArmMetricFactory) metFactoryClass.newInstance();

        metFactory.setErrorCallback(this);

        return metFactory;
    }

    void initFactories() throws Exception
    {
        tranFactory = getTranFactory();
        metricFactory = getMetricFactory();
    }

    void armInit()
    {
        // create application definition
        appDef = tranFactory.newArmApplicationDefinition("ATM - Automated Teller Machine", null, null);

        // create metric definitions
        customerNoMetDef = metricFactory.newArmMetricNumericId32Definition(appDef, "Customer-No.", "ID", ArmMetricDefinition.METRIC_USE_GENERAL, null);
        customerPinMetDef = metricFactory.newArmMetricNumericId32Definition(appDef, "Customer-Pin", "ID", ArmMetricDefinition.METRIC_USE_GENERAL, null);
        checkCustomerMetGroupDef = metricFactory.newArmMetricGroupDefinition(new ArmMetricDefinition[]{customerNoMetDef, customerPinMetDef});

        amountMetDef = metricFactory.newArmMetricCounter32Definition(appDef, "Amount", "money", ArmMetricDefinition.METRIC_USE_GENERAL, null);
        oldBalanceMetDef = metricFactory.newArmMetricGauge32Definition(appDef, "Old Balance", "money", ArmMetricDefinition.METRIC_USE_GENERAL, null);
        newBalanceMetDef = metricFactory.newArmMetricGauge32Definition(appDef, "New Balance", "money", ArmMetricDefinition.METRIC_USE_GENERAL, null);
        withdrawMetGroupDef = metricFactory.newArmMetricGroupDefinition(new ArmMetricDefinition[]{amountMetDef, oldBalanceMetDef, newBalanceMetDef});

        // create transaction definitions
        atmTranDef = tranFactory.newArmTransactionDefinition(appDef, "ATMTran", null, null);
        checkCustomerTranDef = metricFactory. newArmTransactionWithMetricsDefinition(appDef, "Check Customer", null, checkCustomerMetGroupDef, null);

        // The withdraw transaction uses a "Currency" context value, so we
        // need to set up a properties object.
        withdrawIdProperties = tranFactory.newArmIdentityPropertiesTransaction(null, null, new String[]{"Currency"}, null);
        withdrawTranDef = metricFactory.newArmTransactionWithMetricsDefinition(appDef, "Withdraw", withdrawIdProperties, withdrawMetGroupDef, null);

        // Create the application instance object.
        app = tranFactory.newArmApplication(appDef, "Examples", null, null);

        // Create transaction instance objects.
        atmTran = tranFactory.newArmTransaction(app, atmTranDef);

        // For transaction instance objects with metrics, metric instances
        // and groups must be created in advance.
        customerNoMet = metricFactory.newArmMetricNumericId32(customerNoMetDef);
        customerPinMet = metricFactory.newArmMetricNumericId32(customerPinMetDef);
        checkCustomerMetGroup = metricFactory.newArmMetricGroup(checkCustomerMetGroupDef, new ArmMetric[]{customerNoMet, customerPinMet});
        checkCustomerTran = metricFactory.newArmTransactionWithMetrics(app, checkCustomerTranDef, checkCustomerMetGroup);

        amountMet = metricFactory.newArmMetricCounter32(amountMetDef);
        oldBalanceMet = metricFactory.newArmMetricGauge32(oldBalanceMetDef);
        newBalanceMet = metricFactory.newArmMetricGauge32(newBalanceMetDef);
        withdrawMetGroup = metricFactory.newArmMetricGroup(withdrawMetGroupDef, new ArmMetric[]{amountMet, oldBalanceMet, newBalanceMet});
        withdrawTran = metricFactory.newArmTransactionWithMetrics(app, withdrawTranDef, withdrawMetGroup);
    }

    public void errorCodeSet(ArmInterface errorObject, String interfaceName, String methodName)
    {
        fail();
    }

    void checkCustomer(int no, int pin, double amount)
    {
        int idx = -1;
        int err = ATM_ERR_INVALID_NO;

        /**
         *  The real check_customer transaction starts here. But currently
         * we don't know the customer (arm40 user) so get a time stamp from
         * ARM and set it as actual arrival time that is used for the
         * call of the start() method later on.
         */
        checkCustomerTran.setArrivalTime();

        for (int i = 0; i < customers.length; ++i)
        {
            if (customers[i].no == no)
            {
                if (customers[i].pin == pin)
                {
                    idx = i;
                    err = ATM_ERR_NONE;
                }
                else
                {
                    err = ATM_ERR_INVALID_PIN;
                }
                break;
            }
        }

        if (idx >= 0)
        {
            ArmUser currentUser = tranFactory.newArmUser(customers[idx].name, null);
            checkCustomerTran.setUser(currentUser);
        }
        customerNoMet.set(no);
        customerPinMet.set(pin);

        // Now start the check_customer transaction.
        checkCustomerTran.start(atmCorr);
        checkCustomerCorr = checkCustomerTran.getCorrelator();
        if (idx >= 0)
        {
            withdraw(idx, amount);
        }

        if (err > ATM_ERR_NONE)
        {
            checkCustomerTran.stop(ArmConstants.STATUS_FAILED, errors[err]);
        }
        else
        {
            checkCustomerTran.stop(ArmConstants.STATUS_GOOD);
        }
    }

    void withdraw(int idx, double amount)
    {
        int err = ATM_ERR_NONE;
        double newBalance;
        double oldBalance;

        oldBalance = newBalance = customers[idx].balance;

        // We want the metric values to appear only at stop time,
        // so disable them for now.
        withdrawMetGroup.setMetricValid(0, false);
        withdrawMetGroup.setMetricValid(1, false);
        withdrawMetGroup.setMetricValid(2, false);
        withdrawTran.setContextValue(0, customers[idx].currency);
        withdrawTran.start(checkCustomerCorr);

        System.out.println(" Hello Mr./Mrs./Miss " + customers[idx].name);
        System.out.println(" your balance is " + customers[idx].balance + " " + customers[idx].currency);

        try
        {
            System.out.print(" enter amount to withdraw in " + customers[idx].currency + " :");
            if (amount > oldBalance)
            {
                err = ATM_ERR_OVERDRAWN;
            }
            else
            {
                newBalance = oldBalance - amount;
            }
            amountMet.set((int) (amount * 100.0));
            oldBalanceMet.set((int) (oldBalance * 100.0));
            newBalanceMet.set((int) (newBalance * 100.0));
        }
        catch (Exception e)
        {
            System.out.println("an error occured during entry: " + e.getMessage());
            amount = 0.0;
        }

        amountMet.set((int) (amount * 100.0));
        oldBalanceMet.set((int) (oldBalance * 100.0));
        newBalanceMet.set((int) (newBalance * 100.0));

        // Re-enable the recording of the metric values.
        withdrawMetGroup.setMetricValid(0, true);
        withdrawMetGroup.setMetricValid(1, true);
        withdrawMetGroup.setMetricValid(2, true);

        if (err > ATM_ERR_NONE)
        {
            withdrawTran.stop(ArmConstants.STATUS_FAILED, errors[err]);
        }
        else
        {
            System.out.println(" your new balance is " + newBalance);
            withdrawTran.stop(ArmConstants.STATUS_GOOD);
        }
    }

    public void error(Connection connection, Throwable t)
    {
        //todo: consider this autogenerated code
    }
}
