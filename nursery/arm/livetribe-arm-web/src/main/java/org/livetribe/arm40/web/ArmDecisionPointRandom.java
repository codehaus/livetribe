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
import java.util.Random;


/**
 * Fire an ARM transaction a certain percentage of the time.
 *
 * @version $Revision: $ $Date$
 */
public class ArmDecisionPointRandom extends ArmDecisionPoint
{
    private Random random = new Random(System.currentTimeMillis());
    private float percent;


    public float getPercent()
    {
        return percent;
    }

    public void setPercent(float percent)
    {
        this.percent = percent;
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
        if (random.nextFloat() < percent) ArmTransactionFilter.fireTransaction(true);

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
