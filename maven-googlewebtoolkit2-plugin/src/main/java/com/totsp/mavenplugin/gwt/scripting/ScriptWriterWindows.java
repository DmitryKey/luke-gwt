/*
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
package com.totsp.mavenplugin.gwt.scripting;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.StringUtils;

import com.totsp.mavenplugin.gwt.AbstractGWTMojo;
import com.totsp.mavenplugin.gwt.DebugMojo;
import com.totsp.mavenplugin.gwt.util.BuildClasspathUtil;
import com.totsp.mavenplugin.gwt.util.DependencyScope;

/**
 * Handler for writing cmd scripts for the windows platform.
 * 
 * @author ccollins
 * @author rcooper
 */
public class ScriptWriterWindows implements ScriptWriter {

    public ScriptWriterWindows() {
    }

    /**
     * Write run script.
     */
    public File writeRunScript(AbstractGWTMojo mojo) throws MojoExecutionException {
        String filename = (mojo instanceof DebugMojo) ? "debug.cmd" : "run.cmd";
        File file = new File(mojo.getBuildDir(), filename);
        PrintWriter writer = this.getPrintWriterWithClasspath(mojo, file, DependencyScope.RUNTIME);        
        
        String extra = (mojo.getExtraJvmArgs() != null) ? mojo.getExtraJvmArgs() : "";
        writer.print("\"" + mojo.getJavaCommand() + "\" " + extra + " -cp %CLASSPATH% ");

        if (mojo instanceof DebugMojo) {
            writer.print(" -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,address=");
            writer.print(mojo.getDebugPort());
            writer.print(",suspend=y ");
        }

        writer.print("-Dcatalina.base=\"" + mojo.getTomcat().getAbsolutePath() + "\" ");
        writer.print(" com.google.gwt.dev.GWTShell");
        writer.print(" -gen \"");
        writer.print(mojo.getGen().getAbsolutePath());
        writer.print("\" -logLevel ");
        writer.print(mojo.getLogLevel());
        writer.print(" -style ");
        writer.print(mojo.getStyle());
        writer.print(" -out ");
        writer.print("\"" + mojo.getOutput().getAbsolutePath() + "\"");
        writer.print(" -port ");
        writer.print(Integer.toString(mojo.getPort()));

        if (mojo.isNoServer()) {
            writer.print(" -noserver ");
        }
        if (mojo.getWhitelist() != null && mojo.getWhitelist().length() > 0){
            writer.print(" -whitelist \"");
            writer.print(mojo.getWhitelist() );
            writer.print("\" ");
        }
        if (mojo.getBlacklist() != null && mojo.getBlacklist().length() > 0){
            writer.print(" -blacklist \"");
            writer.print(mojo.getBlacklist() );
            writer.print("\" ");
        }

        writer.print(" " + mojo.getRunTarget());
        writer.println();
        writer.flush();
        writer.close();

        return file;
    }

    /**
     * Write compile script.
     */
    public File writeCompileScript(AbstractGWTMojo mojo) throws MojoExecutionException {
        File file = new File(mojo.getBuildDir(), "compile.cmd");
        PrintWriter writer = this.getPrintWriterWithClasspath(mojo, file, DependencyScope.COMPILE);

        for (String target : mojo.getCompileTarget()) {
            String extra = (mojo.getExtraJvmArgs() != null) ? mojo.getExtraJvmArgs() : "";
            writer.print("\"" + mojo.getJavaCommand() + "\" " + extra + " -cp %CLASSPATH% ");
            writer.print(" com.google.gwt.dev.GWTCompiler ");
            writer.print(" -gen \"");
            writer.print(mojo.getGen().getAbsolutePath());
            writer.print("\" -logLevel ");
            writer.print(mojo.getLogLevel());
            writer.print(" -style ");
            writer.print(mojo.getStyle());

            writer.print(" -out ");
            writer.print("\"" + mojo.getOutput().getAbsolutePath() + "\"");
            writer.print(" ");

            if (mojo.isEnableAssertions()) {
                writer.print(" -ea ");
            }

            writer.print(target);
            writer.println();
        }

        writer.flush();
        writer.close();

        return file;
    }    

    /**
     * Write i18n script.
     */
    public File writeI18nScript(AbstractGWTMojo mojo) throws MojoExecutionException {
        File file = new File(mojo.getBuildDir(), "i18n.cmd");
        if (!file.exists()) {
        	if (mojo.getLog().isDebugEnabled()) mojo.getLog().debug("File '" + file.getAbsolutePath() + "' does not exsists, trying to create.");
        	try {
        		file.getParentFile().mkdirs();
        		file.createNewFile();
        		if (mojo.getLog().isDebugEnabled()) mojo.getLog().debug("New file '" + file.getAbsolutePath() + "' created.");
        	} catch (Exception exe) {
        		mojo.getLog().error("Couldn't create file '" + file.getAbsolutePath() + "'. Reason: " + exe.getMessage(), exe);
        	}
        }
        PrintWriter writer = this.getPrintWriterWithClasspath(mojo, file, DependencyScope.COMPILE);

        // constants
        if (mojo.getI18nConstantsNames() != null) {
            for (String target : mojo.getI18nConstantsNames()) {
                String extra = (mojo.getExtraJvmArgs() != null) ? mojo.getExtraJvmArgs() : "";

                writer.print("\"" + mojo.getJavaCommand() + "\" " + extra + " -cp %CLASSPATH%");
                writer.print(" com.google.gwt.i18n.tools.I18NSync");
                writer.print(" -out ");
                writer.print("\"" + mojo.getI18nOutputDir() + "\"");
                writer.print(mojo.isI18nConstantsWithLookup() ? " -createConstantsWithLookup " : " ");
                writer.print(target);
                writer.println();
            }
        }

        // messages
        if (mojo.getI18nMessagesNames() != null) {
            for (String target : mojo.getI18nMessagesNames()) {
                String extra = (mojo.getExtraJvmArgs() != null) ? mojo.getExtraJvmArgs() : "";

                writer.print("\"" + mojo.getJavaCommand() + "\" " + extra + " -cp %CLASSPATH%");
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
    public void writeTestScripts(AbstractGWTMojo mojo) throws MojoExecutionException {
        
        // get extras
        String extra = (mojo.getExtraJvmArgs() != null) ? mojo.getExtraJvmArgs() : "";        
        String testExtra = mojo.getExtraTestArgs() != null ? mojo.getExtraTestArgs() : ""; 
        
        // make sure output dir is present
        File outputDir = new File(mojo.getBuildDir(), "gwtTest");
        outputDir.mkdirs();
        outputDir.mkdir();

        // for each test compile source root, build a test script
        List<String> testCompileRoots = mojo.getProject().getTestCompileSourceRoots();
        for (String currRoot : testCompileRoots) {

            Collection<File> coll = FileUtils.listFiles(new File(currRoot),
                    new WildcardFileFilter(mojo.getTestFilter()), HiddenFileFilter.VISIBLE);

            for (File currFile : coll) {
                
                String testName = currFile.toString();                
                mojo.getLog().debug(("gwtTest test match found (after filter applied) - " + testName));

                // parse off the extension
                if (testName.lastIndexOf('.') > testName.lastIndexOf(File.separatorChar)) {
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
                File file = new File(mojo.getBuildDir() + File.separator + "gwtTest", "gwtTest-" + testName + ".cmd");
                PrintWriter writer = this.getPrintWriterWithClasspath(mojo, file, DependencyScope.TEST);

                // build Java command
                writer.print("\"" + mojo.getJavaCommand() + "\" ");
                if (extra.length() > 0) {
                    writer.print(" " + extra + " ");
                }
                if (testExtra.length() > 0) {
                    writer.print(" " + testExtra + " ");
                }               
                
                writer.print("-cp %CLASSPATH% ");
                
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
    private PrintWriter getPrintWriterWithClasspath(final AbstractGWTMojo mojo, File file, final DependencyScope scope)
            throws MojoExecutionException {

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(file));
            writer.println("@echo off");
        } catch (IOException e) {
            throw new MojoExecutionException("Error creating script - " + file, e);
        }

        try {
            Collection<File> classpath = BuildClasspathUtil.buildClasspathList(mojo, scope);
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
            throw new MojoExecutionException("Error creating script - " + file, e);
        }

        writer.println();
        return writer;
    }
}
