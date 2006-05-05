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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.Archiver;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.UnArchiver;
import org.codehaus.plexus.archiver.manager.ArchiverManager;
import org.codehaus.plexus.archiver.manager.NoSuchArchiverException;
import org.codehaus.plexus.util.FileUtils;

/**
 * Assembles a Forma application.
 *
 * @version $Rev$ $Date$
 * @goal assemble-application
 * @phase package
 * @requiresDependencyResolution compile
 */
public class ApplicationAssemblerMojo extends AbstractMojo
{
    /**
     * @component
     */
    private ArchiverManager archiverManager;

    /**
     * @component
     */
    private ArtifactResolver artifactResolver;

    /**
     * @component
     */
    private ArtifactFactory artifactFactory;

    /**
     * @parameter default-value="${localRepository}"
     * @required
     * @readonly
     */
    private ArtifactRepository localRepository;

    /**
     * Maven output directory.
     *
     * @parameter default-value="${project.build.directory}"
     * @required
     */
    private File outputDirectory;

    /**
     * Directory where the application is assembled, ready to be run.
     *
     * @parameter default-value="${project.build.directory}/work"
     * @required
     */
    private File workDirectory;

    /**
     * The output filename for the plugin assembly, minus the extension.
     *
     * @parameter default-value="${project.build.finalName}"
     * @required
     */
    private String finalName;

    /**
     * The list of modules to be included as is.
     *
     * @parameter
     */
    private List modules = new ArrayList();

    /**
     * The list of plugins to be included.
     *
     * @parameter
     */
    private List plugins = new ArrayList();

    /**
     * @parameter default-value="${reactorProjects}"
     * @required
     * @readonly
     */
    private List reactorProjects;

    /**
     * @parameter default-value="${project.packaging}"
     * @required
     * @readonly
     */
    private String projectPackaging;

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        if (!"pom".equals(projectPackaging))
        {
            getLog().info("Module with packaging not 'pom' will not be processed.");
            return;
        }

        outputDirectory.mkdirs();
        workDirectory.mkdirs();

        UnArchiver unarchiver;
        try
        {
            unarchiver = archiverManager.getUnArchiver("zip");
        }
        catch (NoSuchArchiverException e)
        {
            throw new MojoExecutionException("Cannot lookup unarchiver. Reason: " + e.getMessage(), e);
        }

        Archiver archiver;
        try
        {
            archiver = archiverManager.getArchiver("zip");
        }
        catch (NoSuchArchiverException e)
        {
            throw new MojoExecutionException("Cannot lookup archiver. Reason: " + e.getMessage(), e);
        }

        File archiveFile = new File(outputDirectory, finalName + ".zip");
        archiver.setDestFile(archiveFile);

        addModules(archiver);
        addPlugins(unarchiver, archiver);

        try
        {
            archiver.createArchive();
        }
        catch (ArchiverException e)
        {
            throw new MojoExecutionException("Failed to create application assembly. Reason: " + e.getMessage(), e);
        }
        catch (IOException e)
        {
            throw new MojoExecutionException("Failed to create application assembly. Reason: " + e.getMessage(), e);
        }
    }

    private void addModules(Archiver archiver) throws MojoExecutionException
    {
        for (Iterator projects = reactorProjects.iterator(); projects.hasNext();)
        {
            MavenProject project = (MavenProject)projects.next();
            Artifact artifact = project.getArtifact();
            String artifactKey = ArtifactUtils.versionlessKey(artifact);
            if (modules.contains(artifactKey))
            {
                try
                {
                    FileUtils.copyFileToDirectory(artifact.getFile(), workDirectory);
                    List dependencies = project.getRuntimeClasspathElements();
                    for (int i = 0; i < dependencies.size(); ++i)
                    {
                        String dependency = (String)dependencies.get(i);
                        File dependencyFile = new File(dependency);
                        if (dependencyFile.isFile()) FileUtils.copyFileToDirectory(dependencyFile, workDirectory);
                    }
                }
                catch (IOException e)
                {
                    throw new MojoExecutionException("Failed to copy module " + artifactKey + " to assemble directory. Reason: " + e.getMessage(), e);
                }
                catch (DependencyResolutionRequiredException e)
                {
                    throw new MojoExecutionException("Failed to copy module " + artifactKey + " to assemble directory. Reason: " + e.getMessage(), e);
                }
            }
        }
    }

    private void addPlugins(UnArchiver unarchiver, Archiver archiver) throws MojoExecutionException
    {
        File pluginsDirectory = new File(workDirectory, "plugins");
        pluginsDirectory.mkdirs();

        for (Iterator projects = reactorProjects.iterator(); projects.hasNext();)
        {
            MavenProject project = (MavenProject)projects.next();
            Artifact artifact = project.getArtifact();
            String artifactKey = ArtifactUtils.versionlessKey(artifact);
            if (plugins.contains(artifactKey))
            {
                Artifact plugin = null;
                List attachedArtifacts = project.getAttachedArtifacts();
                for (Iterator attachments = attachedArtifacts.iterator(); attachments.hasNext();)
                {
                    Artifact attachment = (Artifact)attachments.next();
                    if (PluginAssemblerMojo.PLUGIN_CLASSIFIER.equals(attachment.getClassifier()))
                    {
                        plugin = attachment;
                        break;
                    }
                }

                if (plugin == null)
                {
                    plugin = artifactFactory.createArtifactWithClassifier(project.getGroupId(), project.getArtifactId(), project.getVersion(), project.getPackaging(), PluginAssemblerMojo.PLUGIN_CLASSIFIER);

                    try
                    {
                        artifactResolver.resolve(artifact, project.getRemoteArtifactRepositories(), localRepository);
                    }
                    catch (ArtifactResolutionException e)
                    {
                        throw new MojoExecutionException("Cannot find plugin assembly artifact for project: " + project.getId() + ". Resolution failed with: " + e.getMessage(), e);
                    }
                    catch (ArtifactNotFoundException e)
                    {
                        throw new MojoExecutionException("Cannot find plugin assembly artifact for project: " + project.getId() + ". Resolution failed with: " + e.getMessage(), e);
                    }
                }

                File pluginDirectory = new File(pluginsDirectory, plugin.getArtifactId());
                pluginDirectory.mkdirs();

                unarchiver.setDestDirectory(pluginDirectory);
                unarchiver.setSourceFile(plugin.getFile());

                try
                {
                    unarchiver.extract();
                }
                catch (ArchiverException e)
                {
                    throw new MojoExecutionException("Failed to extract plugin artifact: " + artifactKey + " to assemble directory. Reason: " + e.getMessage(), e);
                }
                catch (IOException e)
                {
                    throw new MojoExecutionException("Failed to extract plugin artifact: " + artifactKey + " to assemble directory. Reason: " + e.getMessage(), e);
                }
            }
        }
    }
}
