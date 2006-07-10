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
package org.livetribe.forma.maven;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.archiver.manager.NoSuchArchiverException;

/**
 * Assembles a Forma plugin into a jar
 *
 * @goal assemble-plugin
 * @phase package
 * @requiresDependencyResolution compile
 * @version $Rev$ $Date$
 */
public class PluginAssemblerMojo extends AbstractMojo
{
    static final String PLUGIN_CLASSIFIER = "plugin";

    /**
     * Directory in which to create the plugin-jar.
     *
     * @parameter default-value="${project.build.directory}"
     * @required
     */
    private File outputDirectory;

    /**
     * @parameter default-value="${project.build.outputDirectory}"
     * @required
     * @readonly
     */
    private File classesDirectory;

    /**
     * The output filename for the plugin assembly, minus the extension.
     *
     * @parameter default-value="${project.build.finalName}"
     * @required
     */
    private String finalName;

    /**
     * @parameter default-value="${project.runtimeClasspathElements}"
     * @required
     * @readonly
     */
    private List dependencies;

    /**
     * @component
     */
    private ArchiverManager archiverManager;

    /**
     * @component
     */
    private MavenProjectHelper projectHelper;

    /**
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * @parameter default-value="${project.packaging}"
     * @required
     * @readonly
     */
    private String projectPackaging;

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        if ("pom".equals(projectPackaging))
        {
            getLog().info("Module with packaging 'pom' will not be processed.");
            return;
        }

        outputDirectory.mkdirs();

        // Change the extension, to avoid that this file overwrites the default maven output
        File destFile = new File(outputDirectory, finalName + ".pjar");

        Archiver archiver;

        try
        {
            archiver = archiverManager.getArchiver("jar");
        }
        catch (NoSuchArchiverException e)
        {
            throw new MojoExecutionException("Cannot lookup 'jar' archiver. Reason: " + e.getMessage(), e);
        }

        archiver.setDestFile(destFile);

        buildArchiveContents(archiver);

        try
        {
            archiver.createArchive();
        }
        catch (ArchiverException e)
        {
            throw new MojoExecutionException("Failed to create archive. Reason: " + e.getMessage(), e);
        }
        catch (IOException e)
        {
            throw new MojoExecutionException("Failed to create archive. Reason: " + e.getMessage(), e);
        }

        projectHelper.attachArtifact(project, destFile, PLUGIN_CLASSIFIER);
    }

    private void buildArchiveContents(Archiver archiver) throws MojoExecutionException
    {
        try
        {
            archiver.addDirectory(classesDirectory, "classes/");
        }
        catch (ArchiverException e)
        {
            throw new MojoExecutionException("Failed to add classes to archive. Reason: " + e.getMessage(), e);
        }

        try
        {
            for (Object dependency : dependencies)
            {
                String artifact = (String) dependency;
                File artifactFile = new File(artifact);
                if (artifactFile.isFile()) archiver.addFile(artifactFile, "lib/" + artifactFile.getName());
            }
        }
        catch (ArchiverException e)
        {
            throw new MojoExecutionException("Failed to add dependency libraries to archive. Reason: " + e.getMessage(), e);
        }
    }
}
