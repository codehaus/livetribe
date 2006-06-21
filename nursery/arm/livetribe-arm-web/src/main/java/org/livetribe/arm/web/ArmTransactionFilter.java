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
package org.livetribe.arm.web;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

import org.opengroup.arm40.transaction.ArmConstants;
import org.opengroup.arm40.transaction.ArmCorrelator;
import org.opengroup.arm40.transaction.ArmTransaction;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;

import org.livetribe.arm.CorrelationManager;


/**
 * @version $Revision: $ $Date$
 */
public class ArmTransactionFilter extends GenericFilterBean
{
    private static final ThreadLocal fireTransaction = new ThreadLocal()
    {
        protected Object initialValue()
        {
            return Boolean.FALSE;
        }
    };
    private String transactionName;
    private ArmTransaction transaction;

    public String getTransactionName()
    {
        return transactionName;
    }

    public void setTransactionName(String transactionName)
    {
        this.transactionName = transactionName;
    }

    public ArmTransaction getTransactionFactory()
    {
        if (transaction == null)
        {
            ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
            transaction = (ArmTransaction) context.getBean(transactionName);
        }
        return transaction;
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        ArmCorrelator parent = CorrelationManager.get();

        if (transactionFired())
        {
            if (parent != null) getTransactionFactory().start(parent);
            else getTransactionFactory().start();

            CorrelationManager.put(getTransactionFactory().getCorrelator());

            try
            {
                filterChain.doFilter(servletRequest, servletResponse);

                transaction.stop(ArmConstants.STATUS_GOOD);
            }
            catch (IOException ioe)
            {
                transaction.stop(ArmConstants.STATUS_ABORT, ioe.toString());
                throw ioe;
            }
            catch (ServletException se)
            {
                transaction.stop(ArmConstants.STATUS_ABORT, se.toString());
                throw se;
            }
            finally
            {
                CorrelationManager.put(parent);
            }
        }
        else
        {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    public static void fireTransaction(boolean fire)
    {
        fireTransaction.set((fire ? Boolean.TRUE : Boolean.FALSE));
    }

    protected static boolean transactionFired()
    {
        return ((Boolean) fireTransaction.get()).booleanValue();
    }
}
