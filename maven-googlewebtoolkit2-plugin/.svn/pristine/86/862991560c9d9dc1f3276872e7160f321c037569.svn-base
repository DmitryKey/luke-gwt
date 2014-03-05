/*
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA
 */
package com.totsp.mavenplugin.gwt.util;

import com.totsp.mavenplugin.gwt.AbstractGWTMojo;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.artifact.ActiveProjectArtifact;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Util to consolidate classpath manipulation stuff in one place.
 * 
 * @author ccollins
 */
public class BuildClasspathUtil {


    public static Set<File> getSystemPaths(final AbstractGWTMojo mojo){
        MavenProject project = mojo.getProject();
        Set<File> items = new LinkedHashSet<File>();
        // add system
        for (Artifact a : (Collection<Artifact>) project.getSystemArtifacts()) {
            items.add(a.getFile());
        }
        return items;
    }

    // TODO all over this class we have methods that manipulate input parameters, remove that
    // we should return values and/or let the object keep state, not manipulate params    
    
    /**
     * Build classpath list using either gwtHome (if present) or using *project* dependencies. Note
     * that this is ONLY used for the script/cmd writers (so the scopes are not for the compiler, or
     * war plugins, etc).
     * 
     * This is required so that the script writers can get the dependencies they need regardless of
     * the Maven scopes (still want to use the Maven scopes for everything else Maven, but for
     * GWT-Maven we need to access deps differently - directly at times).
     * 
     * 
     * @param mojo
     * @param scope
     * @return file collection for classpath
     * @throws DependencyResolutionRequiredException
     */
    public static Collection<File> buildClasspathList(final AbstractGWTMojo mojo, final DependencyScope scope)
        throws DependencyResolutionRequiredException, MojoExecutionException {
        mojo.getLog().info("establishing classpath list (buildClaspathList - scope = " + scope + ")");

        File gwtHome = mojo.getGwtHome();
        MavenProject project = mojo.getProject();

        Set<File> items = new LinkedHashSet<File>();

        // inject GWT jars and relative native libs for all scopes
        // (gwt-user and gwt-dev should be scoped provided to keep them out of
        // other maven stuff - not end up in war, etc - this util is only used for GWT-Maven
        // scripts)
        // TODO filter the rest of the stuff so we don't double add these
        if (gwtHome != null) {
            mojo.getLog().info(
                "google.webtoolkit.home (gwtHome) set, using it for GWT dependencies - " + gwtHome.getAbsolutePath());
            items.addAll(BuildClasspathUtil.injectGwtDepsFromGwtHome(gwtHome, mojo));
        } else {
            mojo.getLog().info("google.webtoolkit.home (gwtHome) *not* set, using project POM for GWT dependencies");
            items.addAll(BuildClasspathUtil.injectGwtDepsFromRepo(mojo));
        }

        // add sources
        if (mojo.getSourcesOnPath()) {
            BuildClasspathUtil.addSourcesWithActiveProjects(project, items, DependencyScope.COMPILE);
        }

        // add resources
        if (mojo.getResourcesOnPath()) {
            BuildClasspathUtil.addResourcesWithActiveProjects(project, items, DependencyScope.COMPILE);
        }

        // add classes dir
        items.add(new File(project.getBuild().getDirectory() + File.separator + "classes"));

        // if runtime add runtime
        if (scope == DependencyScope.RUNTIME) {
            // use Collection<Artifact> because sometimes it's LinkedHashSet, sometimes it's List, and NO TIMES is it documented
            for (Artifact a : (Collection<Artifact>) project.getRuntimeArtifacts()) {
                items.add(a.getFile());
            }
        }

        // if test add test
        if (scope == DependencyScope.TEST) {
            for (Artifact a : (Collection<Artifact>) project.getTestArtifacts()) {
                items.add(a.getFile());
            }
            
            // add test classes dir
            items.add(new File(project.getBuild().getDirectory() + File.separator + "test-classes"));

            // add test sources and resources
            BuildClasspathUtil.addSourcesWithActiveProjects(project, items, scope);
            BuildClasspathUtil.addResourcesWithActiveProjects(project, items, scope);
        }

        // add compile (even when scope is other than)
        for (Artifact a : (Collection<Artifact>) project.getCompileArtifacts()) {
            items.add(a.getFile());
        }

        // add all source artifacts
        for (Artifact a : (Collection<Artifact>) project.getArtifacts()) {
            if ("sources".equals(a.getClassifier())) {
                items.add(a.getFile());
            }
        }

        mojo.getLog().debug("SCRIPT INJECTION CLASSPATH LIST");

        for (File f : items) {
            mojo.getLog().debug("   " + f.getAbsolutePath());
        }

        return items;
    }

    /**
     * Helper to inject gwt-user and gwt-dev into classpath from gwtHome, ONLY for compile and run
     * scripts.
     * 
     * @param mojo
     * @return
     */
    public static Collection<File> injectGwtDepsFromGwtHome(final File gwtHome, final AbstractGWTMojo mojo) {
        mojo
            .getLog()
            .debug(
                "injecting gwt-user and gwt-dev for script classpath from google.webtoolkit.home (and expecting relative native libs)");

        Collection<File> items = new LinkedHashSet<File>();
        if(mojo.isUseOophm()){
            File oophmJar = new File(gwtHome, "gwt-dev-oophm.jar");
            items.add(oophmJar);
        }
        File userJar = new File(gwtHome, "gwt-user.jar");
        File devJar = mojo.getGwtVersion().startsWith("2.") ?
             new File(gwtHome, "gwt-dev.jar")
             :
             new File(gwtHome, ArtifactNameUtil.guessDevJarName());
        items.add(userJar);
        items.add(devJar);

        return items;
    }

    /**
     * Helper to inject gwt-user and gwt-dev into classpath from repo, ONLY for compile and run
     * scripts.
     * 
     * @param mojo
     * @return
     */
    public static Collection<File> injectGwtDepsFromRepo(final AbstractGWTMojo mojo) throws MojoExecutionException {
        mojo
            .getLog()
            .debug(
                "injecting gwt-user and gwt-dev for script classpath from local repository (and expecting relative native libs)");

        Collection<File> items = new LinkedHashSet<File>();

        Artifact gwtUser = mojo.getArtifactFactory().createArtifactWithClassifier("com.google.gwt", "gwt-user",
            mojo.getGwtVersion(), "jar", null);
        Artifact gwtDev = mojo.getGwtVersion().startsWith("2.") ?
                mojo.getArtifactFactory().createArtifactWithClassifier("com.google.gwt", "gwt-dev",
            mojo.getGwtVersion(), "jar", null)
                :
                mojo.getArtifactFactory().createArtifactWithClassifier("com.google.gwt", "gwt-dev",
            mojo.getGwtVersion(), "jar", ArtifactNameUtil.getPlatformName());
        Artifact oophm = null;
        if(mojo.isUseOophm() ){
            oophm = mojo.getArtifactFactory().createArtifactWithClassifier("com.google.gwt", "gwt-dev-oophm",
            mojo.getGwtVersion(), "jar", ArtifactNameUtil.getPlatformName());
        }
        List<ArtifactRepository> remoteRepositories = mojo.getRemoteRepositories();

        try {
            mojo.getResolver().resolve(gwtUser, remoteRepositories, mojo.getLocalRepository());
            mojo.getResolver().resolve(gwtDev, remoteRepositories, mojo.getLocalRepository());
            if(oophm !=null ){
                 mojo.getResolver().resolve(oophm, remoteRepositories, mojo.getLocalRepository());
                 items.add(oophm.getFile());
            }
            items.add(gwtUser.getFile());
            items.add(gwtDev.getFile());
        } catch (ArtifactNotFoundException e) {
            throw new MojoExecutionException("artifact not found - " + e.getMessage(), e);
        } catch (ArtifactResolutionException e) {
            throw new MojoExecutionException("artifact resolver problem - " + e.getMessage(), e);
        }

        return items;
    }

    /**
     * Cut from MavenProject.java
     * 
     * @param groupId
     * @param artifactId
     * @param version
     * @return
     */
    private static String getProjectReferenceId(final String groupId, final String artifactId, final String version) {
        return groupId + ":" + artifactId + ":" + version;
    }

    /**
     * Get resources for specific scope.
     * 
     * @param project
     * @param scope
     * @return
     */
    @SuppressWarnings("unchecked")
    private static List<Resource> getResources(final MavenProject project, final DependencyScope scope) {
        if (DependencyScope.COMPILE.equals(scope)) {
            return project.getResources();
        } else if (DependencyScope.TEST.equals(scope)) {
            return project.getTestResources();
        } else {
            throw new RuntimeException("Not allowed scope " + scope);
        }
    }

    /**
     * Get artifacts for specific scope.
     * 
     * @param project
     * @param scope
     * @return
     */
    @SuppressWarnings("unchecked")
    private static List<Artifact> getScopeArtifacts(final MavenProject project, final DependencyScope scope) {
        if (DependencyScope.COMPILE.equals(scope)) {
            return project.getCompileArtifacts();
        } else if (DependencyScope.TEST.equals(scope)) {
            return project.getTestArtifacts();
        } else {
            throw new RuntimeException("Not allowed scope " + scope);
        }
    }

    /**
     * Get source roots for specific scope.
     * 
     * @param project
     * @param scope
     * @return
     */
    private static List<String> getSourceRoots(final MavenProject project, final DependencyScope scope) {
        if (DependencyScope.COMPILE.equals(scope)) {
            return project.getCompileSourceRoots();
        } else if (DependencyScope.TEST.equals(scope)) {
            return project.getTestCompileSourceRoots();
        } else {
            throw new RuntimeException("Not allowed scope " + scope);
        }
    }

    /**
     * Add source path and resource paths of the project to the list of classpath items.
     * 
     * @param items Classpath items.
     * @param resources
     */
    private static void addResources(final Set<File> items, final List<Resource> resources) {
        for (Resource r : resources) {
            items.add(new File(r.getDirectory()));
        }
    }

    /**
     * Add all sources and resources also with active (maven reactor active) referenced project
     * sources and resources. Addresses issue no. 147.
     * 
     * @param project
     * @param items
     * @param scope
     */
    private static void addResourcesWithActiveProjects(final MavenProject project, final Set<File> items,
        final DependencyScope scope) {
        final List<Artifact> scopeArtifacts = BuildClasspathUtil.getScopeArtifacts(project, scope);

        List<Resource> resources = BuildClasspathUtil.getResources(project, scope);
        BuildClasspathUtil.addResources(items, resources);

        for (Artifact artifact : scopeArtifacts) {
            if (artifact instanceof ActiveProjectArtifact) {
                MavenProject refProject = (MavenProject) project.getProjectReferences().get(
                    BuildClasspathUtil.getProjectReferenceId(artifact.getGroupId(), artifact.getArtifactId(), artifact
                        .getVersion()));

                if (refProject != null) {
                    List<Resource> resourcesRef = BuildClasspathUtil.getResources(refProject, scope);
                    BuildClasspathUtil.addResources(items, resourcesRef);
                }
            }
        }
    }

    /**
     * Add source path and resource paths of the project to the list of classpath items.
     * 
     * @param items Classpath items.
     * @param sourceRoots
     */
    private static void addSources(final Set<File> items, final List<String> sourceRoots) {
        for (String s : sourceRoots) {
            items.add(new File(s));
        }
    }

    /**
     * Add all sources and resources also with active (maven reactor active) referenced project
     * sources and resources. Addresses issue no. 147.
     * 
     * @param project
     * @param items
     * @param scope
     */
    private static void addSourcesWithActiveProjects(final MavenProject project, final Set<File> items,
        final DependencyScope scope) {
        
        List<Artifact> scopeArtifacts = BuildClasspathUtil.getScopeArtifacts(project, scope);
        List<String> sourceRoots = BuildClasspathUtil.getSourceRoots(project, scope);
        BuildClasspathUtil.addSources(items, sourceRoots);

        for (Artifact artifact : scopeArtifacts) {
            if (artifact instanceof ActiveProjectArtifact) {
                MavenProject refProject = (MavenProject) project.getProjectReferences().get(
                    BuildClasspathUtil.getProjectReferenceId(artifact.getGroupId(), artifact.getArtifactId(), artifact
                        .getVersion()));

                if (refProject != null) {
                    List<String> sourceRootsRef = BuildClasspathUtil.getSourceRoots(refProject, scope);
                    BuildClasspathUtil.addSources(items, sourceRootsRef);
                }
            }
        }
    }
}
