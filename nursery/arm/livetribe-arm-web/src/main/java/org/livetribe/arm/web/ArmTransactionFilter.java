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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

import org.opengroup.arm40.transaction.ArmConstants;
import org.opengroup.arm40.transaction.ArmCorrelator;
import org.opengroup.arm40.transaction.ArmTransaction;

import org.livetribe.arm.CorrelationManager;


/**
 * @version $Revision: $ $Date$
 */
public class ArmTransactionFilter implements Filter
{
    public static final String TRANSACTION = "org.livetribe.arm.web.ArmTransactionFilter.transaction";
    private static final ThreadLocal fireTransaction = new ThreadLocal()
    {
        protected Object initialValue()
        {
            return Boolean.FALSE;
        }
    };
    private ArmTransaction transaction;

    public void init(FilterConfig filterConfig) throws ServletException
    {
        String key = filterConfig.getInitParameter(TRANSACTION);
        transaction = (ArmTransaction) KnitPoint.getContext().getBean(key);
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        ArmCorrelator parent = CorrelationManager.get();

        if (transactionFired())
        {
            if (parent != null) transaction.start(parent);
            else transaction.start();

            CorrelationManager.put(transaction.getCorrelator());

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

    public void destroy()
    {
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
