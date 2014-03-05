/*
 * GWTMojo.java
 *
 * Created on January 11, 2007, 6:42 PM
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

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.totsp.mavenplugin.gwt.scripting.ScriptUtil;
import com.totsp.mavenplugin.gwt.scripting.ScriptWriter;
import com.totsp.mavenplugin.gwt.scripting.ScriptWriterFactory;

/**
 * Runs the the project in the GWTShell for development.
 * 
 * Note that this goal is intended to be explicitly run from the command line 
 * (execute phase=), whereas other GWT-Maven goals are not (others happen as 
 * part of the standard Maven life-cycle phases: "compile" "test" "install").
 * 
 * @goal gwt
 * @execute phase=install
 * @requiresDependencyResolution compile
 * @description Runs the the project in the GWTShell for development.
 * 
 * @author ccollins
 * @author cooper
 */
public class GWTMojo extends AbstractGWTMojo {

    /** Creates a new instance of GWTMojo */
    public GWTMojo() {
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
