/**
 *
 * Copyright 2005 (C) The original author or authors.
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
package org.livetribe.jmx;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.JMException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationEmitter;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ReflectionException;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanOperationInfo;


/**
 * @version $Revision: $ $Date: $
 */
public class DynamicMBeanAdapter implements DynamicMBean, NotificationEmitter
{
    private final NotificationBroadcasterSupport broadcaster = new NotificationBroadcasterSupport();
    private final Object resource;
    private final MBeanInfo mbeanInfo;

    public DynamicMBeanAdapter( Object resource, MBeanInfo mbeanInfo )
    {
        this.resource = resource;
        this.mbeanInfo = mbeanInfo;
    }

    public Object getAttribute( String attributeName ) throws AttributeNotFoundException, MBeanException, ReflectionException
    {
        MBeanAttributeInfo[] attributes = mbeanInfo.getAttributes();
        for ( int i = 0; i < attributes.length; i++ )
        {
            MBeanAttributeInfo attributeInfo = attributes[i];
            if ( attributeInfo.isReadable() && attributeInfo.getName().equals( attributeName ) )
            {
                String prefix = attributeInfo.isIs() ? "is" : "get";
                String methodName = prefix + MBeanUtils.capitalize( attributeName );
                return invokeMethod( methodName, null, null );
            }
        }
        throw new AttributeNotFoundException( attributeName );
    }

    public void setAttribute( Attribute attribute ) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException
    {
        MBeanAttributeInfo[] attributes = mbeanInfo.getAttributes();
        String attributeName = attribute.getName();
        for ( int i = 0; i < attributes.length; i++ )
        {
            MBeanAttributeInfo attributeInfo = attributes[i];
            if ( attributeInfo.isWritable() && attributeInfo.getName().equals( attributeName ) )
            {
                String methodName = "set" + MBeanUtils.capitalize( attributeName );
                invokeMethod( methodName, new String[]{attributeInfo.getType()}, new Object[]{attribute.getValue()} );
                return;
            }
        }
        throw new AttributeNotFoundException( attributeName );
    }

    public AttributeList getAttributes( String[] attributes )
    {
        AttributeList result = new AttributeList();
        for ( int i = 0; i < attributes.length; i++ )
        {
            String attributeName = attributes[i];
            try
            {
                Object value = getAttribute( attributeName );
                result.add( new Attribute( attributeName, value ) );
            }
            catch ( JMException ignored )
            {
                // JMX Specification requires to ignore the exception
            }
        }
        return result;
    }

    public AttributeList setAttributes( AttributeList attributes )
    {
        AttributeList result = new AttributeList();
        for ( Iterator it = attributes.iterator(); it.hasNext(); )
        {
            Attribute attribute = (Attribute) it.next();
            try
            {
                setAttribute( attribute );
                result.add( attribute );
            }
            catch ( JMException ignored )
            {
                // JMX Specification requires to ignore the exception
            }
        }
        return result;
    }

    public Object invoke( String operationName, Object params[], String signature[] ) throws MBeanException, ReflectionException
    {
        MBeanOperationInfo[] operations = mbeanInfo.getOperations();
        for ( int i = 0; i < operations.length; i++ )
        {
            MBeanOperationInfo operation = operations[i];
            if ( operation.getName().equals( operationName ) )
            {
                boolean found = false;
                MBeanParameterInfo[] parameters = operation.getSignature();
                if ( parameters.length == 0 )
                {
                    found = true;
                }
                else
                {
                    if ( parameters.length == signature.length )
                    {
                        boolean result = true;
                        for ( int j = 0; j < parameters.length; ++j )
                        {
                            MBeanParameterInfo parameter = parameters[j];
                            result &= parameter.getType().equals( signature[j] );
                        }
                        found = result;
                    }
                }
                if ( found ) return invokeMethod( operationName, signature, params );
            }
        }

        if ( isObjectMethod( operationName, signature ) )
        {
            return invokeMethod( operationName, signature, params );
        }

        throw new ReflectionException( new NoSuchMethodException( operationName ) );
    }

    public MBeanInfo getMBeanInfo()
    {
        return mbeanInfo;
    }

    protected boolean isObjectMethod( String methodName, String[] signature )
    {
        Method[] methods = Object.class.getMethods();
        for ( int i = 0; i < methods.length; i++ )
        {
            Method method = methods[i];
            if ( isSameMethod( method, methodName, signature ) ) return true;
        }
        return false;
    }

    protected boolean isSameMethod( Method method, String methodName, String[] signature )
    {
        if ( method.getName().equals( methodName ) )
        {
            Class[] parameters = method.getParameterTypes();

            if ( parameters.length == 0 )
            {
                if ( signature == null || signature.length == 0 ) return true;
            }
            else
            {
                if ( signature == null ) return false;
                if ( parameters.length != signature.length ) return false;

                boolean result = true;
                for ( int j = 0; j < parameters.length; ++j )
                {
                    Class parameter = parameters[j];
                    String[] tokens = signature[j].split(" ");
                    result &= parameter.getName().equals( tokens[tokens.length-1] );
                }
                return result;
            }
        }
        return false;
    }

    protected Object invokeMethod( String methodName, String[] signature, Object[] arguments ) throws ReflectionException, MBeanException
    {
        Method[] methods = resource.getClass().getMethods();
        for ( int i = 0; i < methods.length; i++ )
        {
            Method method = methods[i];
            if ( isSameMethod( method, methodName, signature ) )
            {
                try
                {
                    return method.invoke( resource, arguments );
                }
                catch ( IllegalAccessException x )
                {
                    throw new ReflectionException( x );
                }
                catch ( InvocationTargetException x )
                {
                    Throwable cause = x.getCause();
                    if ( cause instanceof Exception )
                    {
                        throw new MBeanException( (Exception) cause );
                    }
                    else
                    {
                        throw new MBeanException( new Exception( cause ) );
                    }
                }
            }
        }
        throw new ReflectionException( new NoSuchMethodException( methodName ) );
    }

    public void removeNotificationListener( NotificationListener listener, NotificationFilter filter, Object handback ) throws ListenerNotFoundException
    {
        broadcaster.removeNotificationListener( listener, filter, handback );
    }

    public void addNotificationListener( NotificationListener listener, NotificationFilter filter, Object handback ) throws IllegalArgumentException
    {
        broadcaster.addNotificationListener( listener, filter, handback );
    }

    public void removeNotificationListener( NotificationListener listener ) throws ListenerNotFoundException
    {
        broadcaster.removeNotificationListener( listener );
    }

    public void sendNotification( Notification notification )
    {
        broadcaster.sendNotification( notification );
    }

    public MBeanNotificationInfo[] getNotificationInfo()
    {
        return getMBeanInfo().getNotifications();
    }
}

