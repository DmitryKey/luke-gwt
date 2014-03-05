package com.totsp.mavenplugin.gwt;

import com.totsp.mavenplugin.gwt.scripting.ScriptUtil;
import com.totsp.mavenplugin.gwt.scripting.ScriptWriter;
import com.totsp.mavenplugin.gwt.scripting.ScriptWriterFactory;
import java.io.File;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Runs the the project in the GWTShell for development.
 * 
 * Note that this goal is intended to be explicitly run from the command line 
 * (execute phase=), whereas other GWT-Maven goals are not (others happen as 
 * part of the standard Maven life-cycle phases: "compile" "test" "install").
 * 
 * @goal codeserver
 * @execute phase=install
 * @requiresDependencyResolution compile
 * @description Runs the the project in the GWTShell for development.
 * 
 * @author kebernet
 */
public class SuperDevModeMojo extends AbstractGWTMojo {

    /** Creates a new instance of GWTMojo */
    public SuperDevModeMojo() {
        super();
    }

    public void execute() throws MojoExecutionException, MojoFailureException {

        try {
            this.makeCatalinaBase();
        } catch (Exception e) {
            throw new MojoExecutionException("Unable to build catalina.base", e);
        }
        if (!this.getOutput().exists()) {
            this.getOutput().mkdirs();
        }

        // build it for the correct platform
        ScriptWriter writer = ScriptWriterFactory.getInstance(this);
        File exec = writer.writeRunScript(this);        
        
        // run it
        ScriptUtil.runScript(exec);
    }    
}
