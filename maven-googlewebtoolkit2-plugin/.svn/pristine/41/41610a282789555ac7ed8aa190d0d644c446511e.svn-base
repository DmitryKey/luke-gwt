/*
 * 
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

package com.totsp.mavenplugin.gwt;

import com.totsp.mavenplugin.gwt.scripting.ScriptUtil;
import com.totsp.mavenplugin.gwt.scripting.ScriptWriter;
import com.totsp.mavenplugin.gwt.scripting.ScriptWriter16;
import com.totsp.mavenplugin.gwt.scripting.ScriptWriterFactory;
import java.io.File;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Validates the GWT source but does not compile.
 * 
 * @goal validate
 * @requiresDependencyResolution compile
 * @description Validates the GWT source but does not compile.
 * @phase validate
 * 
 * @author kebernet
 */
public class ValidateMojo extends AbstractGWTMojo {

    public void execute() throws MojoExecutionException, MojoFailureException {
        // build it for the correct platform
        ScriptWriter writer = ScriptWriterFactory.getInstance(this);
        if (writer instanceof ScriptWriter16) {
            ScriptWriter16 w16 = (ScriptWriter16) writer;
            File exec = w16.writeValidationScript(this);
            // run it
            ScriptUtil.runScript(exec);
        } else {
            throw new MojoExecutionException("Validation is only supported with GWT 1.6+");
        }

    }

}
