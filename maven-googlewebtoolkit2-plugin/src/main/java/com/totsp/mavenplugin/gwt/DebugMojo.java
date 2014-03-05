/*
 * DebugMojo.java
 *
 * Created on January 12, 2007, 9:45 PM
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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Extends the gwt goal and runs the project in the GWTShell with a debugger port hook (optionally suspended).
 * 
 * @goal debug
 * @execute phase=install
 * @description Runs the project with a debugger port hook (optionally suspended). 
 * 
 * @author cooper
 */
public class DebugMojo extends GWTMojo {
    public DebugMojo() {
        super();
    }

    public void execute() throws MojoExecutionException, MojoFailureException {
        if (isDebugSuspend())
            getLog().info("starting debugger on port " + getDebugPort() + " in suspend mode");
        else
            getLog().info("starting debugger on port " + getDebugPort());
        super.execute();
    }
}
