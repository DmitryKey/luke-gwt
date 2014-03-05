/*
 * MergeWebXmlMojo.java
 *
 * Created on January 13, 2007, 7:45 PM
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

import java.io.File;
import java.util.Iterator;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.totsp.mavenplugin.gwt.support.ExitException;
import com.totsp.mavenplugin.gwt.support.GwtWebInfProcessor;
import com.totsp.mavenplugin.gwt.util.FileIOUtils;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Merges GWT servlet elements into deployment descriptor (and non GWT servlets into shell).
 * 
 * @goal mergewebxml
 * @phase process-resources
 * @requiresDependencyResolution compile
 * @description Merges GWT servlet elements into deployment descriptor (and non GWT servlets into shell).
 * 
 * @author cooper
 */
public class MergeWebXmlMojo extends AbstractGWTMojo {

    /** Creates a new instance of MergeWebXmlMojo */
    public MergeWebXmlMojo() {
        super();
    }

    public void execute() throws MojoExecutionException, MojoFailureException {

        try {
            this.getLog().info("copy source web.xml - " + this.getWebXml() + " to build dir (source web.xml required if mergewebxml execution is enabled)");
            File destination = new File(this.getBuildDir(), "web.xml");
            if (!destination.exists()) {
                destination.getParentFile().mkdirs();
                destination.createNewFile();
            }
            
            FileIOUtils.copyFile(this.getWebXml(), destination);

            for (int i = 0; i < this.getCompileTarget().length; i++) {
                File moduleFile = null;
                for (Iterator it = this.getProject().getCompileSourceRoots().iterator(); it.hasNext()
                        && moduleFile == null;) {
                    File check = new File(it.next().toString() + "/" + this.getCompileTarget()[i].replace('.', '/')
                            + ".gwt.xml");
                    getLog().debug("Looking for file: " + check.getAbsolutePath());
                    if (check.exists()) {
                        moduleFile = check;
                    }
                }
                for (Iterator it = this.getProject().getResources().iterator(); it.hasNext();) {
                    Resource r = (Resource) it.next();
                    File check = new File(r.getDirectory() + "/" + this.getCompileTarget()[i].replace('.', '/')
                            + ".gwt.xml");
                    getLog().debug("Looking for file: " + check.getAbsolutePath());
                    if (check.exists()) {
                        moduleFile = check;
                    }
                }



                ClassLoader loader = this.fixThreadClasspath();

                if(moduleFile == null){
                    try{
                        String classpath = "/"+this.getCompileTarget()[i].replace('.', '/')+".gwt.xml";
                        InputStream is = loader.getResourceAsStream( classpath);
                        System.out.println("Looking for classpath: "+classpath+ "("+(is!=null)+")");
                        if( is != null ){
                            File temp = new File( this.getBuildDir() , this.getCompileTarget()[i].concat(".gwt.xml"));
                            FileOutputStream fos = new FileOutputStream(temp);
                            FileIOUtils.copyStream(is, fos);
                            moduleFile = temp;
                        }
                    } catch(IOException e){
                        this.getLog().info(e);
                    }
                }

                GwtWebInfProcessor processor = null;
                try {
                    if (moduleFile != null) {
                        getLog().info("Module file: " + moduleFile.getAbsolutePath());
                        processor = new GwtWebInfProcessor(this.getCompileTarget()[i], moduleFile, destination
                                .getAbsolutePath(), destination.getAbsolutePath(), this.isWebXmlServletPathAsIs());
                        processor.process();
                    } else {
                        throw new MojoExecutionException("module file null");
                    }
                } catch (ExitException e) {
                    this.getLog().info(e.getMessage());
                   // return;
                }
                
            }
        } catch (Exception e) {
            throw new MojoExecutionException("Unable to merge web.xml", e);
        }
    }

}
