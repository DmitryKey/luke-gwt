/*
 * AbstractGWTMojo.java
 *
 * Created on January 11, 2007, 6:17 PM
 *
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
package com.totsp.mavenplugin.gwt;

import com.totsp.mavenplugin.gwt.support.MakeCatalinaBase;
import com.totsp.mavenplugin.gwt.util.BuildClasspathUtil;
import com.totsp.mavenplugin.gwt.util.DependencyScope;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;

import org.codehaus.classworlds.ClassRealm;
import org.codehaus.classworlds.ClassWorld;

import org.codehaus.plexus.util.FileUtils;

import java.io.File;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;


/**
 * Abstract Mojo for GWT-Maven.
 *
 * @author ccollins
 * @author cooper
 * @author willpugh
 */
public abstract class AbstractGWTMojo extends AbstractMojo {
    public static final String OS_NAME = System.getProperty("os.name")
                                               .toLowerCase(Locale.US);
    public static final String GWT_GROUP_ID = "com.google.gwt";
    public static final String WINDOWS = "windows";
    public static final String LINUX = "linux";
    public static final String MAC = "mac";
    public static final String LEOPARD = "leopard";
    public static final String GOOGLE_WEBTOOLKIT_HOME = "google.webtoolkit.home";
    private static final String JAVA_COMMAND = (System.getProperty("java.home") != null)
        ? FileUtils.normalize(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java") : "java";

    /**
     * @component
     */
    private org.apache.maven.artifact.factory.ArtifactFactory artifactFactory;

    /**
     * @parameter expression="${localRepository}"
     */
    private org.apache.maven.artifact.repository.ArtifactRepository localRepository;

    /**
     * @parameter expression="${project.remoteArtifactRepositories}"
     */
    private java.util.List remoteRepositories;

    /**
     * @component
     */
    private org.apache.maven.artifact.resolver.ArtifactResolver resolver;

    // GWT-Maven properties

    /**
     * Location on filesystem where project should be built.
     *
     * @parameter expression="${project.build.directory}"
     */
    private File buildDir;

    /**
     * Source Tomcat context.xml for GWT shell - copied to
     * /gwt/localhost/ROOT.xml (used as the context.xml for the SHELL - requires
     * Tomcat 5.0.x format - hence no default).
     *
     * @parameter
     */
    private File contextXml;

    /**
     * Non-deployable files directory (1.6+)
     * @parameter default-value="${project.build.directory}/gwtExtra"
     */
    private File extraDir;

    /**
     * Location on filesystem where GWT will write generated content for review
     * (-gen option to GWTCompiler).
     *
     * @parameter expression="${project.build.directory}/.generated"
     */
    private File gen;

    /**
     * Location on filesystem where GWT is installed - for manual mode (existing
     * GWT on machine - not needed for automatic mode).
     *
     * @parameter expression="${google.webtoolkit.home}"
     */
    private File gwtHome;

    /**
     * Location on filesystem to output generated i18n Constants and Messages
     * interfaces.
     *
     * @parameter expression="${basedir}/src/main/java/"
     */
    private File i18nOutputDir;

    /**
     * Location on filesystem where GWT will write output files (-out option to
     * GWTCompiler).
     *
     * @parameter
     *            expression="${project.build.directory}/${project.build.finalName}"
     */
    private File output;

    /**
     * Specify the location on the filesystem for the generated embedded Tomcat
     * directory.
     *
     * @parameter expression="${project.build.directory}/tomcat"
     */
    private File tomcat;

    /**
     * Source web.xml deployment descriptor that is used for GWT shell and for
     * deployment WAR to "merge" servlet entries.
     *
     * @parameter expression="${basedir}/src/main/webapp/WEB-INF/web.xml"
     */
    private File webXml;

    /**
     * Compiler work directory (1.6+)
     * @parameter default-value="${project.build.directory}/gwtWork"
     */
    private File workDir;

    /**
     * <i>Maven Internal</i>: List of artifacts for the plugin.
     *
     * @parameter expression="${plugin.artifacts}"
     * @required
     * @readonly
     */
    private List pluginClasspathList;

    // Maven properties

    /**
     * Project instance, used to add new source directory to the build.
     *
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * Whitelist URL pattern for GWTShell and JUnitShell
     *
     * @parameter
     */
    private String blacklist;

    /**
     * Extra JVM arguments that are passed to the GWT-Maven generated scripts
     * (for compiler, shell, etc - typically use -Xmx512m here, or
     * -XstartOnFirstThread, etc).
     *
     * @parameter expression="${google.webtoolkit.extrajvmargs}"
     */
    private String extraJvmArgs;

    /**
     * Extra JVM arguments that are passed only to the GWT-Maven generated test
     * scripts (in addition to std extraJvmArgs).
     *
     * @parameter default-value=""
     */
    private String extraTestArgs;

    /**
     * Destination package for generated classes.
     *
     * @parameter
     */
    private String generatorDestinationPackage;

    /**
     * Set the GWT version number - used to build dependency paths, should match
     * the "version" in the Maven repo.
     *
     * @parameter default-value="1.5.3"
     */
    private String gwtVersion;

    /**
     * Option to specify the jvm (or path to the java executable) to use with the test cases.
     * For the default, the jvm will be the same as JAVA_HOME.
     *
     * @parameter expression="${google.webtoolkit.jvm}"
     */
    private String jvm;

    /**
     * GWT logging level (-logLevel ERROR, WARN, INFO, TRACE, DEBUG, SPAM, or
     * ALL).
     *
     * @parameter default-value="INFO"
     */
    private String logLevel;

    /**
     * URL that should be automatically opened by default in the GWT shell.
     *
     * @parameter
     * @required
     */
    private String runTarget;

    /**
     * @parameter
     */
    private String server;

    /**
     * Specifies the mapping URL to be used with the shell servlet.
     *
     * @parameter default-value="/*"
     */
    private String shellServletMappingURL;

    /**
     * GWT JavaScript compiler output style (-style OBF[USCATED], PRETTY, or
     * DETAILED).
     *
     * @parameter default-value="OBF"
     */
    private String style;

    /**
     * Simple string filter for classes that should be treated as GWTTestCase
     * type (and therefore invoked during gwtTest goal).
     *
     * @parameter default-value="GwtTest*"
     */
    private String testFilter;

    /**
     * Whitelist URL pattern for GWTShell and JUnitShell
     *
     * @parameter
     */
    private String whitelist;

    /**
     * List of GWT module names that should be compiled with the GWT compiler.
     *
     * @parameter property="compileTargets"
     * @required
     */
    private String[] compileTarget;
    
    /**
     * List of GWT module names that should be compiled with the GWT compiler.
     *
     * @parameter property="codeserverTargets"
     * @required
     */
    private String[] codeserverTargets;

    /**
     * Top level (root) of classes to begin generation from.
     *
     * @parameter property="generatorRootClasses"
     */
    private String[] generatorRootClasses;

    /**
     * Browser configs to use in HTML unit
     * @parameter
     */
    private String[] htmlUnitBrowsers;

    /**
     * List of names of properties files that should be used to generate i18n
     * Constants interfaces.
     *
     * @parameter
     */
    private String[] i18nConstantsNames;

    /**
     * List of names of properties files that should be used to generate i18n
     * Messages interfaces.
     *
     * @parameter
     */
    private String[] i18nMessagesNames;

    /**
     * Whether or not to skip GWT compilation.
     *
     * @parameter expression="${google.webtoolkit.compileSkip}" default-value="false"
     */
    private boolean compileSkip;

    /**
     * Whether or not to suspend execution until a debugger connects.
     *
     * @parameter default-value="true"
     */
    private boolean debugSuspend;

    /**
     * Whether or not to enable assertions in generated scripts (-ea).
     *
     * @parameter default-value="false"
     */
    private boolean enableAssertions;

    /**
     * Whether or not to generate getter/setter methods for generated classes.
     *
     * @parameter
     */
    private boolean generateGettersAndSetters;

    /**
     * Whether or not to generate PropertyChangeSupport handling for generated
     * classes.
     *
     * @parameter
     */
    private boolean generatePropertyChangeSupport;

    /**
     * Whether or not to generate i18n Constants interfaces with the added
     * ability to look up values at runtime with a string key.
     *
     * @parameter default-value="false"
     */
    private boolean i18nConstantsWithLookup;

    /**
     * Prevents the embedded GWT Tomcat server from running (even if a port is
     * specified).
     *
     * @parameter default-value="false"
     */
    private boolean noServer;

    /**
     * Whether or not to overwrite generated classes if they exist.
     *
     * @parameter
     */
    private boolean overwriteGeneratedClasses;

    /**
     * Whether or not to add resources root to classpath.
     *
     * @parameter default-value="true"
     */
    private boolean resourcesOnPath;

    /**
     * Toggles the graphical logger (1.6+)
     *
     * @parameter default-value="false"
     *
     */
    private boolean showTreeLogger;

    /**
     * Whether or not to add compile source root to classpath.
     *
     * @parameter default-value="true"
     */
    private boolean sourcesOnPath;

    /**
     * Whether or not to skip GWT testing.
     *
     * @parameter expression="${google.webtoolkit.testSkip}" default-value="false"
     */
    private boolean testSkip;

    /**
     * Toggles HTMLUnit testing (GWT 2.0+)
     * @parameter
     */
    private boolean useHtmlUnit;

    /**
     * Project instance, used to add new source directory to the build.
     *
     * @parameter default-value="false"
     */
    private boolean useOophm;

    /**
     * Specifies whether or not to add the module name as a prefix to the
     * servlet path when merging web.xml.  If you set this to false the exact
     * path from the GWT module will be used, nothing else will be prepended.
     *
     * @parameter default-value="false"
     */
    private boolean webXmlServletPathAsIs;

    /**
     * Port to listen for debugger connection on.
     *
     * @parameter default-value="8000"
     */
    private int debugPort;

    /**
     * Specifies the number of worker threads to use to compile (1.6+)
     * @parameter default-value="2"
     */
    private int localWorkers;

    /**
     * Runs the embedded GWT Tomcat server on the specified port.
     *
     * @parameter default-value="8888"
     */
    private int port;
    
    /**
     * Runs the embedded GWT Code server on the specified port.
     *
     * @parameter default-value="9876"
     */
    private int codeserverPort;

    // ctor

    /** Creates a new instance of AbstractGWTMojo */
    public AbstractGWTMojo() {
    }

    public void setArtifactFactory(org.apache.maven.artifact.factory.ArtifactFactory artifactFactory) {
        this.artifactFactory = artifactFactory;
    }

    public org.apache.maven.artifact.factory.ArtifactFactory getArtifactFactory() {
        return this.artifactFactory;
    }

    /**
     * @return the blacklist
     */
    public String getBlacklist() {
        return this.blacklist;
    }

    //
    // accessors/mutators
    //
    public void setBuildDir(File buildDir) {
        this.buildDir = buildDir;
    }

    public File getBuildDir() {
        return this.buildDir;
    }

    public boolean isCompileSkip() {
        return this.compileSkip;
    }

    public void setCompileTarget(String[] compileTarget) {
        this.compileTarget = compileTarget;
    }

    public String[] getCompileTarget() {
        return this.compileTarget;
    }
    
    public String[] getCodeserverTargets(){
        return this.codeserverTargets;
    }
    
    public void setCodeserverTargets(String[] targets){
        this.codeserverTargets = targets;
    }
    
    public int getCodeserverPort(){
        return this.codeserverPort;
    }
    
    public void setCodeserverPort(int port){
        this.codeserverPort = port;
    }

    public void setCompileTargets(String[] targets) {
        this.compileTarget = targets;
    }

    public void setContextXml(File contextXml) {
        this.contextXml = contextXml;
    }

    public File getContextXml() {
        return this.contextXml;
    }

    public void setDebugPort(int debugPort) {
        this.debugPort = debugPort;
    }

    public int getDebugPort() {
        return this.debugPort;
    }

    public void setDebugSuspend(boolean debugSuspend) {
        this.debugSuspend = debugSuspend;
    }

    public boolean isDebugSuspend() {
        return this.debugSuspend;
    }

    public void setEnableAssertions(boolean enableAssertions) {
        this.enableAssertions = enableAssertions;
    }

    public boolean isEnableAssertions() {
        return this.enableAssertions;
    }

    /**
     * @param extraDir the extraDir to set
     */
    public void setExtraDir(File extraDir) {
        this.extraDir = extraDir;
    }

    /**
     * @return the extraDir
     */
    public File getExtraDir() {
        return this.extraDir;
    }

    public void setExtraJvmArgs(String extraJvmArgs) {
        this.extraJvmArgs = extraJvmArgs;
    }

    public String getExtraJvmArgs() {
        return this.extraJvmArgs;
    }

    public void setExtraTestArgs(String extraTestArgs) {
        this.extraTestArgs = extraTestArgs;
    }

    public String getExtraTestArgs() {
        return this.extraTestArgs;
    }

    public void setGen(File gen) {
        this.gen = gen;
    }

    public File getGen() {
        return this.gen;
    }

    public void setGenerateGettersAndSetters(boolean generateGettersAndSetters) {
        this.generateGettersAndSetters = generateGettersAndSetters;
    }

    public boolean isGenerateGettersAndSetters() {
        return this.generateGettersAndSetters;
    }

    public void setGeneratePropertyChangeSupport(boolean generatePropertyChangeSupport) {
        this.generatePropertyChangeSupport = generatePropertyChangeSupport;
    }

    public boolean isGeneratePropertyChangeSupport() {
        return this.generatePropertyChangeSupport;
    }

    public void setGeneratorDestinationPackage(String generatorDestinationPackage) {
        this.generatorDestinationPackage = generatorDestinationPackage;
    }

    public String getGeneratorDestinationPackage() {
        return this.generatorDestinationPackage;
    }

    public void setGeneratorRootClasses(String[] generatorRootClasses) {
        this.generatorRootClasses = generatorRootClasses;
    }

    public String[] getGeneratorRootClasses() {
        return this.generatorRootClasses;
    }

    public void setGwtHome(File gwtHome) {
        this.gwtHome = gwtHome;
    }

    public File getGwtHome() {
        return this.gwtHome;
    }

    public void setGwtVersion(String gwtVersion) {
        this.gwtVersion = gwtVersion;
    }

    public String getGwtVersion() {
        return this.gwtVersion;
    }

    /**
     * @param htmlUnitBrowsers the htmlUnitBrowsers to set
     */
    public void setHtmlUnitBrowsers(String[] htmlUnitBrowsers) {
        this.htmlUnitBrowsers = htmlUnitBrowsers;
    }

    /**
     * @return the htmlUnitBrowsers
     */
    public String[] getHtmlUnitBrowsers() {
        return htmlUnitBrowsers;
    }

    public void setI18nConstantsNames(String[] constantsNames) {
        this.i18nConstantsNames = constantsNames;
    }

    public String[] getI18nConstantsNames() {
        return this.i18nConstantsNames;
    }

    public void setI18nConstantsWithLookup(boolean i18nConstantsWithLookup) {
        this.i18nConstantsWithLookup = i18nConstantsWithLookup;
    }

    public boolean isI18nConstantsWithLookup() {
        return this.i18nConstantsWithLookup;
    }

    public void setI18nMessagesNames(String[] messagesNames) {
        this.i18nMessagesNames = messagesNames;
    }

    public String[] getI18nMessagesNames() {
        return this.i18nMessagesNames;
    }

    public void setI18nOutputDir(File outputDir) {
        this.i18nOutputDir = outputDir;
    }

    public File getI18nOutputDir() {
        return this.i18nOutputDir;
    }

    public String getJavaCommand() {
        return ((getJvm() != null) ? getJvm() : JAVA_COMMAND);
    }

    public void setJvm(String jvm) {
        this.jvm = jvm;
    }

    public String getJvm() {
        return this.jvm;
    }

    public void setLocalRepository(org.apache.maven.artifact.repository.ArtifactRepository localRepository) {
        this.localRepository = localRepository;
    }

    public org.apache.maven.artifact.repository.ArtifactRepository getLocalRepository() {
        return this.localRepository;
    }

    /**
     * @param localWorkers the localWorkers to set
     */
    public void setLocalWorkers(int localWorkers) {
        this.localWorkers = localWorkers;
    }

    /**
     * @return the localWorkers
     */
    public int getLocalWorkers() {
        return this.localWorkers;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getLogLevel() {
        return this.logLevel;
    }

    public void setNoServer(boolean noServer) {
        this.noServer = noServer;
    }

    public boolean isNoServer() {
        return this.noServer;
    }

    public void setOutput(File output) {
        this.output = output;
    }

    public File getOutput() {
        return this.output;
    }

    public void setOverwriteGeneratedClasses(boolean overwriteGeneratedClasses) {
        this.overwriteGeneratedClasses = overwriteGeneratedClasses;
    }

    public boolean isOverwriteGeneratedClasses() {
        return this.overwriteGeneratedClasses;
    }

    public void setPluginClasspathList(List pluginClasspathList) {
        this.pluginClasspathList = pluginClasspathList;
    }

    public List getPluginClasspathList() {
        return this.pluginClasspathList;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return this.port;
    }

    public void setProject(MavenProject project) {
        this.project = project;
    }

    public MavenProject getProject() {
        return this.project;
    }

    public void setRemoteRepositories(java.util.List remoteRepositories) {
        this.remoteRepositories = remoteRepositories;
    }

    public java.util.List getRemoteRepositories() {
        return this.remoteRepositories;
    }

    public void setResolver(org.apache.maven.artifact.resolver.ArtifactResolver resolver) {
        this.resolver = resolver;
    }

    public org.apache.maven.artifact.resolver.ArtifactResolver getResolver() {
        return this.resolver;
    }

    public void setResourcesOnPath(boolean resourcesOnPath) {
        this.resourcesOnPath = resourcesOnPath;
    }

    public boolean getResourcesOnPath() {
        return this.resourcesOnPath;
    }

    public void setRunTarget(String runTarget) {
        this.runTarget = runTarget;
    }

    public String getRunTarget() {
        return this.runTarget;
    }

    /**
     * @param server the server to set
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * @return the server
     */
    public String getServer() {
        return server;
    }

    public void setShellServletMappingURL(String shellServletMappingURL) {
        this.shellServletMappingURL = shellServletMappingURL;
    }

    public String getShellServletMappingURL() {
        return this.shellServletMappingURL;
    }

    /**
     * @param showTreeLogger the showTreeLogger to set
     */
    public void setShowTreeLogger(boolean showTreeLogger) {
        this.showTreeLogger = showTreeLogger;
    }

    /**
     * @return the showTreeLogger
     */
    public boolean isShowTreeLogger() {
        return this.showTreeLogger;
    }

    public void setSourcesOnPath(boolean value) {
        this.sourcesOnPath = value;
    }

    public boolean getSourcesOnPath() {
        return this.sourcesOnPath;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyle() {
        return this.style;
    }

    public void setTestFilter(String testFilter) {
        this.testFilter = testFilter;
    }

    public String getTestFilter() {
        return this.testFilter;
    }

    public void setTestSkip(boolean skip) {
        this.testSkip = skip;
    }

    public boolean isTestSkip() {
        return this.testSkip;
    }

    public void setTomcat(File tomcat) {
        this.tomcat = tomcat;
    }

    public File getTomcat() {
        return this.tomcat;
    }

    /**
     * @param useHtmlUnit the useHtmlUnit to set
     */
    public void setUseHtmlUnit(boolean useHtmlUnit) {
        this.useHtmlUnit = useHtmlUnit;
    }

    /**
     * @return the useHtmlUnit
     */
    public boolean isUseHtmlUnit() {
        return useHtmlUnit;
    }

    /**
     * @param useOophm the useOophm to set
     */
    public void setUseOophm(boolean useOophm) {
        this.useOophm = useOophm;
    }

    /**
     * @return the useOophm
     */
    public boolean isUseOophm() {
        return useOophm;
    }

    public void setWebXml(File webXml) {
        this.webXml = webXml;
    }

    public File getWebXml() {
        return this.webXml;
    }

    public void setWebXmlServletPathAsIs(boolean webXmlServletPathAsIs) {
        this.webXmlServletPathAsIs = webXmlServletPathAsIs;
    }

    public boolean isWebXmlServletPathAsIs() {
        return this.webXmlServletPathAsIs;
    }

    /**
     * @return the whitelist
     */
    public String getWhitelist() {
        return this.whitelist;
    }

    /**
     * @param workDir the workDir to set
     */
    public void setWorkDir(File workDir) {
        this.workDir = workDir;
    }

    /**
     * @return the workDir
     */
    public File getWorkDir() {
        return this.workDir;
    }

    /**
     * Create embedded GWT tomcat base dir based on properties.
     *
     * @throws Exception
     */
    public void makeCatalinaBase() throws Exception {
        getLog()
            .debug("make catalina base for embedded Tomcat");

        if ((this.getWebXml() != null) && this.getWebXml()
                                                  .exists()) {
            this.getLog()
                .info("source web.xml present - " + this.getWebXml() + " - using it with embedded Tomcat");
        } else {
            this.getLog()
                .info("source web.xml NOT present, using default empty web.xml for shell");
        }

        // note that MakeCatalinaBase (support jar) will use emptyWeb.xml if webXml does not exist 
        String[] args = {
                this.getTomcat()
                    .getAbsolutePath(), this.getWebXml()
                                            .getAbsolutePath(), this.getShellServletMappingURL()
            };
        MakeCatalinaBase.main(args);

        if ((this.getContextXml() != null) && this.getContextXml()
                                                      .exists()) {
            this.getLog()
                .info("contextXml parameter present - " + this.getContextXml() +
                " - using it for embedded Tomcat ROOT.xml");
            FileUtils.copyFile(this.getContextXml(), new File(this.getTomcat(), "conf/gwt/localhost/ROOT.xml"));
        }
    }

    // methods

    /**
     * Helper hack for classpath problems, used as a fallback.
     *
     * @return
     */
    protected ClassLoader fixThreadClasspath() {
        try {
            ClassWorld world = new ClassWorld();

            //use the existing ContextClassLoader in a realm of the classloading space
            ClassRealm root = world.newRealm("gwt-plugin", Thread.currentThread().getContextClassLoader());
            ClassRealm realm = root.createChildRealm("gwt-project");

            for (Iterator it = BuildClasspathUtil.buildClasspathList(this, DependencyScope.COMPILE)
                                                 .iterator(); it.hasNext();) {
                realm.addConstituent(((File) it
                                      .next()).toURI().toURL());
            }

            Thread.currentThread()
                  .setContextClassLoader(realm.getClassLoader());

            ///System.out.println("AbstractGwtMojo realm classloader = " + realm.getClassLoader().toString());
            return realm.getClassLoader();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
