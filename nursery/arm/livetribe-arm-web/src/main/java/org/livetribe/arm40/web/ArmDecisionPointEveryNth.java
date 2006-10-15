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
package org.livetribe.arm40.web;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;


/**
 * Fire an ARM transaction every nth call.
 *
 * @version $Revision: $ $Date$
 */
public class ArmDecisionPointEveryNth extends ArmDecisionPoint
{
    private int count = 0;
    private int nth;

    public int getNth()
    {
        return nth;
    }

    public void setNth(int nth)
    {
        this.nth = nth;
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        if (count++ % nth == 0) ArmTransactionFilter.fireTransaction(true);

        filterChain.doFilter(servletRequest, servletResponse);
    }
}