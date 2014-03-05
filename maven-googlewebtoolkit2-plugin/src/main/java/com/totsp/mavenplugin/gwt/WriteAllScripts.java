package com.totsp.mavenplugin.gwt;

import com.totsp.mavenplugin.gwt.scripting.ScriptWriter;
import com.totsp.mavenplugin.gwt.scripting.ScriptWriter16;
import com.totsp.mavenplugin.gwt.scripting.ScriptWriterFactory;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Simply writes out all the scripts to the target folder.
 * 
 * @goal writeAllScripts
 * @description Simply writes out all the scripts to the target folder
 * @author kebernet
 */
public class WriteAllScripts extends AbstractGWTMojo {

    public void execute() throws MojoExecutionException, MojoFailureException {
        // build it for the correct platform
        ScriptWriter writer = ScriptWriterFactory.getInstance(this);
        writer.writeCompileScript(this);
        writer.writeRunScript(this);
        writer.writeI18nScript(this);
        writer.writeTestScripts(this);
        if (writer instanceof ScriptWriter16) {
            ((ScriptWriter16) writer).writeValidationScript(this);
        }
    }

}
