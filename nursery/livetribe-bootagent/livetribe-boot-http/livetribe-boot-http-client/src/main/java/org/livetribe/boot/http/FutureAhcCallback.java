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
package org.livetribe.boot.http;

import org.apache.ahc.AsyncHttpClientCallback;
import org.apache.ahc.codec.HttpResponseMessage;


/**
 * @version $Revision$ $Date$
 */
public class FutureAhcCallback<V> implements AsyncHttpClientCallback
{
    private FutureResponse<V> owner;

    final void setOwner(FutureResponse<V> owner)
    {
        this.owner = owner;
    }

    final protected void setValue(V value)
    {
        owner.setValue(value);
    }

    final protected void setThrowable(Throwable throwable)
    {
        owner.setThrowable(throwable);
    }

    public void onResponse(HttpResponseMessage message)
    {
        throw new UnsupportedOperationException("Response not supported");
    }

    public void onException(Throwable cause)
    {
        throw new UnsupportedOperationException("Exception not supported");
    }

    public void onClosed()
    {
        throw new UnsupportedOperationException("Close not supported");
    }

    public void onTimeout()
    {
        throw new UnsupportedOperationException("Timeout not supported");
    }
}
