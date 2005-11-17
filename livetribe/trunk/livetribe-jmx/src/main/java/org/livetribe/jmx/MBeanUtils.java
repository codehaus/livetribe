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

import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanOperationInfo;


/**
 * @version $Revision: $ $Date: $
 */
public class MBeanUtils
{
    private final static Map beanInfoCache = Collections.synchronizedMap( new WeakHashMap() );

    public static MBeanAttributeInfo[] getMBeanAttributeInfo( Object bean )
    {
        return getMBeanAttributeInfo( bean.getClass() );
    }

    public static MBeanConstructorInfo[] getMBeanConstructorInfo( Object bean )
    {
        return getMBeanConstructorInfo( bean.getClass() );
    }

    public static MBeanOperationInfo[] getMBeanOperationInfo( Object bean )
    {
        return getMBeanOperationInfo( bean.getClass() );
    }

    public static MBeanAttributeInfo[] getMBeanAttributeInfo( Class beanClass )
    {
        MBeanAttributeInfo[] result = new MBeanAttributeInfo[0];

        try
        {
            result = getBeanInfo( beanClass ).attributes;
        }
        catch ( IntrospectionException doNothing )
        {
        }

        return result;
    }

    public static MBeanConstructorInfo[] getMBeanConstructorInfo( Class beanClass )
    {
        MBeanConstructorInfo[] result = new MBeanConstructorInfo[0];

        try
        {
            result = getBeanInfo( beanClass ).constructors;
        }
        catch ( IntrospectionException doNothing )
        {
        }

        return result;
    }

    public static MBeanOperationInfo[] getMBeanOperationInfo( Class beanClass )
    {
        MBeanOperationInfo[] result = new MBeanOperationInfo[0];

        try
        {
            result = getBeanInfo( beanClass ).operations;
        }
        catch ( IntrospectionException doNothing )
        {
        }

        return result;
    }

    private static CacheItem getBeanInfo( Class beanClass ) throws IntrospectionException
    {
        CacheItem ci = (CacheItem) beanInfoCache.get( beanClass );
        if ( ci == null )
        {
            BeanInfo bi = Introspector.getBeanInfo( beanClass, Object.class );

            ci = new CacheItem();

            ci.attributes = getAttributes( bi );
            ci.constructors = getConstructors( beanClass );
            ci.operations = getOperations( bi );

            beanInfoCache.put( beanClass, ci );
        }
        return ci;
    }

    private static MBeanAttributeInfo[] getAttributes( BeanInfo bi )
    {
        PropertyDescriptor[] descriptors = bi.getPropertyDescriptors();
        MBeanAttributeInfo[] result = new MBeanAttributeInfo[descriptors.length];

        for ( int i = 0; i < descriptors.length; i++ )
        {
            PropertyDescriptor descriptor = descriptors[i];
            boolean hasReadMethod = descriptor.getReadMethod() != null;
            result[i] = new MBeanAttributeInfo( descriptor.getName(),
                                                descriptor.getPropertyType().toString(),
                                                descriptor.getPropertyType() + " " + descriptor.getName(),
                                                hasReadMethod,
                                                descriptor.getWriteMethod() != null,
                                                hasReadMethod && descriptor.getReadMethod().getName().startsWith( "is" ) );
        }

        return result;
    }

    private static MBeanConstructorInfo[] getConstructors( Class beanClass )
    {
        Constructor[] constructors = beanClass.getConstructors();
        MBeanConstructorInfo[] result = new MBeanConstructorInfo[constructors.length];

        for ( int i = 0; i < constructors.length; i++ )
        {
            result[i] = new MBeanConstructorInfo( constructors[i].toString(), constructors[i] );
        }

        return result;
    }

    private static MBeanOperationInfo[] getOperations( BeanInfo bi )
    {
        Set methods = new HashSet();

        MethodDescriptor[] methodDesc = bi.getMethodDescriptors();
        for ( int i = 0; i < methodDesc.length; i++ )
        {
            methods.add( methodDesc[i].getMethod() );
        }

        EventSetDescriptor[] eventDesc = bi.getEventSetDescriptors();
        for ( int i = 0; i < eventDesc.length; i++ )
        {
            methods.remove( eventDesc[i].getAddListenerMethod() );
            methods.remove( eventDesc[i].getRemoveListenerMethod() );
            methods.remove( eventDesc[i].getGetListenerMethod() );
        }

        PropertyDescriptor[] propDesc = bi.getPropertyDescriptors();
        for ( int i = 0; i < propDesc.length; i++ )
        {
            methods.remove( propDesc[i].getReadMethod() );
            methods.remove( propDesc[i].getWriteMethod() );
        }

        MBeanOperationInfo[] result = new MBeanOperationInfo[methods.size()];
        Iterator iter = methods.iterator();
        for ( int i = 0; i < result.length; i++ )
        {
            Method method = (Method) iter.next();
            result[i] = new MBeanOperationInfo( method.toString(), method );
        }

        return result;
    }

    public static String capitalize(String name) {
	if (name == null || name.length() == 0) {
	    return name;
	}
	char chars[] = name.toCharArray();
	chars[0] = Character.toUpperCase(chars[0]);
	return new String(chars);
    }

    public static String decapitalize(String name) {
	if (name == null || name.length() == 0) {
	    return name;
	}
	if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) &&
			Character.isUpperCase(name.charAt(0))){
	    return name;
	}
	char chars[] = name.toCharArray();
	chars[0] = Character.toLowerCase(chars[0]);
	return new String(chars);
    }
    private static class CacheItem
    {
        public MBeanAttributeInfo[] attributes;
        public MBeanConstructorInfo[] constructors;
        public MBeanOperationInfo[] operations;
    }
}
