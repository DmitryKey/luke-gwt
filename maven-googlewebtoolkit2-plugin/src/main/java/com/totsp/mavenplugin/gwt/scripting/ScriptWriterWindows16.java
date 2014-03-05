package com.totsp.mavenplugin.gwt.scripting;

import com.totsp.mavenplugin.gwt.AbstractGWTMojo;
import com.totsp.mavenplugin.gwt.DebugMojo;
import com.totsp.mavenplugin.gwt.util.BuildClasspathUtil;
import com.totsp.mavenplugin.gwt.util.DependencyScope;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.MojoExecutionException;

import org.codehaus.plexus.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.Collection;
import java.util.List;


/**
 *
 * @author kebernet
 */
public class ScriptWriterWindows16 implements ScriptWriter16 {
    public ScriptWriterWindows16() {
        super();
    }

    /**
    * Write run script.
     * @param mojo The Mojo calling this (contianing config parametersd)
     * @return the script file that was written.
     * @throws org.apache.maven.plugin.MojoExecutionException general purpose exception.
    */
    public File writeRunScript(AbstractGWTMojo mojo)
        throws MojoExecutionException {
        String filename = (mojo instanceof DebugMojo) ? "debug.cmd" : "run.cmd";
        File file = new File(mojo.getBuildDir(), filename);
        PrintWriter writer = this.getPrintWriterWithClasspath(mojo, file,
                DependencyScope.RUNTIME);

        String extra = (mojo.getExtraJvmArgs() != null)
            ? mojo.getExtraJvmArgs() : "";
        writer.print("\"" + mojo.getJavaCommand() + "\" " + extra + " -cp %CP% ");

        if (mojo instanceof DebugMojo) {
            writer.print(" -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,address=");
            writer.print(mojo.getDebugPort());
            writer.print(mojo.isDebugSuspend() ? ",suspend=y " : ",suspend=n ");
        }

        writer.print(" com.google.gwt.dev.HostedMode");
        writer.print(" -gen ");
        writer.print(mojo.getGen().getAbsolutePath());
        writer.print(" -logLevel ");
        writer.print(mojo.getLogLevel());
        writer.print(" -style ");
        writer.print(mojo.getStyle());

        if (mojo.isEnableAssertions()) {
            writer.print(" -ea ");
        }

        if ((mojo.isShowTreeLogger())) {
            writer.print(" -treeLogger ");
        }

//        writer.print(" -workDir ");
//        mojo.getWorkDir().mkdirs();
//        writer.print(mojo.getWorkDir().getAbsolutePath());
//        writer.print(" -extra ");
//        mojo.getExtraDir().mkdirs();
//        writer.print(mojo.getExtraDir());
        writer.print(" -war ");
        writer.print("\"" + mojo.getOutput().getAbsolutePath() + "\" ");
        writer.print("-localWorkers ");
        writer.print(mojo.getLocalWorkers());
        writer.print(" -port ");
        writer.print(Integer.toString(mojo.getPort()));

        if (mojo.isNoServer()) {
            writer.print(" -noserver ");
        }

        if ((mojo.getWhitelist() != null) && (mojo.getWhitelist().length() > 0)) {
            writer.print(" -whitelist \"");
            writer.print(mojo.getWhitelist());
            writer.print("\" ");
        }

        if ((mojo.getBlacklist() != null) && (mojo.getBlacklist().length() > 0)) {
            writer.print(" -blacklist \"");
            writer.print(mojo.getBlacklist());
            writer.print("\" ");
        }

        writer.print(" -startupUrl " + mojo.getRunTarget());
        writer.print(" ");
        writer.print(mojo.getCompileTarget()[0]);
        writer.println();

        writer.flush();
        writer.close();

        return file;
    }

     /**
     * Write compile script.
      * @param mojo The Mojo calling this (contianing config parametersd)
     * @return the script file that was written.
     * @throws org.apache.maven.plugin.MojoExecutionException general purpose exception.
     */
    public File writeCompileScript(AbstractGWTMojo mojo) throws MojoExecutionException {
        return this.writeCompilerInvocationScript(mojo, false);
    }

    /**
     * Write compile script.
     * @param mojo The Mojo calling this (contianing config parametersd)
     * @param validateOnly Indicates whether this should write a validation or compile script
     * @return the script file that was written.
     * @throws org.apache.maven.plugin.MojoExecutionException general purpose exception.
     */
    protected File writeCompilerInvocationScript(AbstractGWTMojo mojo, boolean validateOnly)
        throws MojoExecutionException {
        File file = new File(mojo.getBuildDir(), validateOnly? "validate.cmd" :"compile.cmd");
        PrintWriter writer = this.getPrintWriterWithClasspath(mojo, file,
                DependencyScope.COMPILE);

        
            String extra = (mojo.getExtraJvmArgs() != null)
                ? mojo.getExtraJvmArgs() : "";
            writer.print("\"" + mojo.getJavaCommand() + "\" " + extra +
                " -cp %CLASSPATH% ");
            writer.print(" com.google.gwt.dev.Compiler ");
        if(validateOnly){
            writer.print( " -validateOnly ");
        }
        writer.print(" -gen ");
        writer.print(mojo.getGen().getAbsolutePath());
        writer.print(" -logLevel ");
        writer.print(mojo.getLogLevel());
        writer.print(" -style ");
        writer.print(mojo.getStyle());

        if (mojo.isEnableAssertions()) {
            writer.print(" -ea ");
        }

        if ((mojo.isShowTreeLogger())) {
            writer.print(" -treeLogger ");
        }

        writer.print(" -workDir ");
        writer.print(mojo.getWorkDir().getAbsolutePath());
        writer.print(" -extra ");
        writer.print(mojo.getExtraDir());
        writer.print(" -war ");
        writer.print("\"" + mojo.getOutput().getAbsolutePath() + "\"");
        writer.print(" -localWorkers ");
        writer.print(mojo.getLocalWorkers());
        writer.print(" ");
        for (String target : mojo.getCompileTarget()) {
            writer.print(target);
            writer.print(" ");
        }
        writer.println();

        writer.flush();
        writer.close();
        return file;
    }

    /**
     * Write i18n script.
     * @param mojo The Mojo calling this (contianing config parametersd)
     * @return the script file that was written.
     * @throws org.apache.maven.plugin.MojoExecutionException general purpose exception.
     */
    public File writeI18nScript(AbstractGWTMojo mojo)
        throws MojoExecutionException {
        File file = new File(mojo.getBuildDir(), "i18n.cmd");

        if (!file.exists()) {
            if (mojo.getLog().isDebugEnabled()) {
                mojo.getLog()
                    .debug("File '" + file.getAbsolutePath() +
                    "' does not exsists, trying to create.");
            }

            try {
                file.getParentFile().mkdirs();
                file.createNewFile();

                if (mojo.getLog().isDebugEnabled()) {
                    mojo.getLog()
                        .debug("New file '" + file.getAbsolutePath() +
                        "' created.");
                }
            } catch (Exception exe) {
                mojo.getLog()
                    .error("Couldn't create file '" + file.getAbsolutePath() +
                    "'. Reason: " + exe.getMessage(), exe);
            }
        }

        PrintWriter writer = this.getPrintWriterWithClasspath(mojo, file,
                DependencyScope.COMPILE);

        // constants
        if (mojo.getI18nConstantsNames() != null) {
            for (String target : mojo.getI18nConstantsNames()) {
                String extra = (mojo.getExtraJvmArgs() != null)
                    ? mojo.getExtraJvmArgs() : "";

                writer.print("\"" + mojo.getJavaCommand() + "\" " +
                    extra + " -cp %CLASSPATH%");
                writer.print(" com.google.gwt.i18n.tools.I18NSync");
                writer.print(" -out ");
                writer.print("\"" + mojo.getI18nOutputDir() + "\"");
                writer.print(" ");
                writer.print(target);
                writer.println();
            }
        }

        // messages
        if (mojo.getI18nMessagesNames() != null) {
            for (String target : mojo.getI18nMessagesNames()) {
                String extra = (mojo.getExtraJvmArgs() != null)
                    ? mojo.getExtraJvmArgs() : "";

                writer.print("\"" + mojo.getJavaCommand() + "\" " +
                    extra + " -cp %CLASSPATH%");
                writer.print(" com.google.gwt.i18n.tools.I18NSync");
                writer.print(" -createMessages ");
                writer.print(" -out ");
                writer.print("\"" + mojo.getI18nOutputDir() + "\"");
                writer.print(" ");
                writer.print(target);
                writer.println();
            }
        }

        writer.flush();
        writer.close();

        return file;
    }

    /**
     * Write test scripts.
     */
    public void writeTestScripts(AbstractGWTMojo mojo)
        throws MojoExecutionException {
        // get extras
        String extra = (mojo.getExtraJvmArgs() != null)
            ? mojo.getExtraJvmArgs() : "";
        String testExtra = (mojo.getExtraTestArgs() != null)
            ? mojo.getExtraTestArgs() : "";

        // make sure output dir is present
        File outputDir = new File(mojo.getBuildDir(), "gwtTest");
        outputDir.mkdirs();
        outputDir.mkdir();

        // for each test compile source root, build a test script
        List<String> testCompileRoots = mojo.getProject()
                                            .getTestCompileSourceRoots();

        for (String currRoot : testCompileRoots) {
            Collection<File> coll = FileUtils.listFiles(new File(currRoot),
                    new WildcardFileFilter(mojo.getTestFilter()),
                    HiddenFileFilter.VISIBLE);

            for (File currFile : coll) {
                String testName = currFile.toString();
                mojo.getLog()
                    .debug(("gwtTest test match found (after filter applied) - " +
                    testName));

                // parse off the extension
                if (testName.lastIndexOf('.') > testName.lastIndexOf(
                            File.separatorChar)) {
                    testName = testName.substring(0, testName.lastIndexOf('.'));
                }

                if (testName.startsWith(currRoot)) {
                    testName = testName.substring(currRoot.length());
                }

                if (testName.startsWith(File.separator)) {
                    testName = testName.substring(1);
                }

                testName = StringUtils.replace(testName, File.separatorChar, '.');
                mojo.getLog().debug("testName after parsing - " + testName);

                // start script inside gwtTest output dir, and name it with test class name
                File file = new File(mojo.getBuildDir() + File.separator +
                        "gwtTest", "gwtTest-" + testName + ".cmd");
                PrintWriter writer = this.getPrintWriterWithClasspath(mojo,
                        file, DependencyScope.TEST);

                // build Java command
                writer.print("\"" + mojo.getJavaCommand() + "\" ");

                if (extra.length() > 0) {
                    writer.print(" " + extra + " ");
                }

                if (testExtra.length() > 0) {
                    writer.print(" " + testExtra + " ");
                }

                writer.print(" -cp %CLASSPATH% ");
                
                writer.print("-Dcatalina.base=\"" + mojo.getTomcat().getAbsolutePath() + "\" ");
                
                writer.print("junit.textui.TestRunner ");
                writer.print(testName);

                // write script out
                writer.flush();
                writer.close();
            }
        }
    }

    /**
     * Util to get a PrintWriter with Windows preamble.
     *
     * @param mojo
     * @param file
     * @return
     * @throws MojoExecutionException
     */
    private PrintWriter getPrintWriterWithClasspath(
        final AbstractGWTMojo mojo, File file, final DependencyScope scope)
        throws MojoExecutionException {
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new FileWriter(file));
            writer.println("@echo off");
        } catch (IOException e) {
            throw new MojoExecutionException("Error creating script - " + file,
                e);
        }

        try {
            Collection<File> classpath = BuildClasspathUtil.buildClasspathList(mojo,
                    scope);
            writer.print("set CLASSPATH=");

            StringBuffer cpString = new StringBuffer();

            for (File f : classpath) {
                cpString.append("\"" + f.getAbsolutePath() + "\";");

                // break the line at 4000 characters to try to avoid max size
                if (cpString.length() > 4000) {
                    writer.println(cpString);
                    cpString = new StringBuffer();
                    writer.print("set CLASSPATH=%CLASSPATH%;");
                }
            }

            writer.println(cpString);
            writer.println();
        } catch (DependencyResolutionRequiredException e) {
            throw new MojoExecutionException("Error creating script - " + file,
                e);
        }

        writer.println();

        return writer;
    }


    /**
     * Writes the validate.cmd script.
     * @param mojo The Mojo calling this (contianing config parametersd)
     * @return the script file that was written.
     * @throws org.apache.maven.plugin.MojoExecutionException general purpose exception.
     */
    public File writeValidationScript(AbstractGWTMojo mojo)
        throws MojoExecutionException {
        return this.writeCompilerInvocationScript(mojo, true);
    }
}
