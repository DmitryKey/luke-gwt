/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.totsp.mavenplugin.gwt.scripting;

import com.totsp.mavenplugin.gwt.AbstractGWTMojo;
import java.io.File;
import org.apache.maven.plugin.MojoExecutionException;

/**
 *
 * @author kebernet
 */
public interface ScriptWriter16 extends ScriptWriter {

    public File writeValidationScript(AbstractGWTMojo mojo) throws MojoExecutionException;
}
