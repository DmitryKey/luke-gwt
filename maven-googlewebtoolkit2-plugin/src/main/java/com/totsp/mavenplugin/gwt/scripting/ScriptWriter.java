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

import org.apache.maven.plugin.MojoExecutionException;

import com.totsp.mavenplugin.gwt.AbstractGWTMojo;

/**
 * 
 * @author ccollins
 * 
 */
public interface ScriptWriter {

    public File writeRunScript(AbstractGWTMojo mojo) throws MojoExecutionException;

    public File writeCompileScript(AbstractGWTMojo mojo) throws MojoExecutionException;

    public File writeI18nScript(AbstractGWTMojo mojo) throws MojoExecutionException;
    
    public void writeTestScripts(AbstractGWTMojo mojo) throws MojoExecutionException;


}
