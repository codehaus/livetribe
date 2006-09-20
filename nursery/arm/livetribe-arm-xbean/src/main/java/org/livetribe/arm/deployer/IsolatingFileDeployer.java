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
package org.livetribe.arm.deployer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.xbean.server.deployer.FileDeployer;

import org.livetribe.arm.classloader.EmptyClassLoader;
import org.livetribe.arm.classloader.IsolatingClassLoader;


/**
 * A service which auto-deploys services within a recursive file system.
 *
 * @version $Revision: $ $Date: $
 * @org.apache.xbean.XBean namespace="http://livetribe.org/schemas/server/1_0" element="isolating-file-deployer" description="Deploys services in a file system"
 */
public class IsolatingFileDeployer extends FileDeployer
{
    protected final Logger logger = Logger.getLogger(getClass().getName());
    private ClassLoader ancestor;

    public void afterPropertiesSet() throws Exception
    {
        ancestor = collectClassLoader();
        setClassLoader(new EmptyClassLoader("Empty"));

        super.afterPropertiesSet();
    }

    protected ClassLoader createChildClassLoader(String name, File directory, ClassLoader parentClassLoader) throws MalformedURLException
    {
        return new IsolatingClassLoader(name + ".ClassLoader", collectURLs(directory, name), parentClassLoader, ancestor);
    }

    protected ClassLoader collectClassLoader()
    {
        ClassLoader result = getClassLoader();

        if (result == null) result = Thread.currentThread().getContextClassLoader();
        if (result == null) result = getClass().getClassLoader();

        return result;
    }

    protected URL[] collectURLs(File directory, String loaderName) throws MalformedURLException
    {
        List urls = new ArrayList();
        File[] files = directory.listFiles();

        if (files != null)
        {
            for (int i = 0; i < files.length; i++)
            {
                File file = files[i];

                if (!file.isDirectory() && (file.getName().endsWith(".zip") || file.getName().endsWith(".jar")))
                {
                    if (isVerbose())
                    {
                        try
                        {
                            logger.info("Adding to classpath: " + loaderName + " jar: " + file.getCanonicalPath());
                        }
                        catch (Exception ignore)
                        {
                        }
                    }
                    urls.add(file.toURL());
                }
            }
        }

        return (URL[]) urls.toArray(new URL[urls.size()]);
    }
}
