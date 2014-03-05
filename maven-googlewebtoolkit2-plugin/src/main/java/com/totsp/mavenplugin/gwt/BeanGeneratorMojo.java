/*
 * BeanGeneratorMojo.java
 *
 * Created on February 17, 2007, 8:53 PM
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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Generates client beans for the project (currently unsupported).
 * 
 * @goal generateClientBeans
 * @requiresDependencyResolution compile
 * @description Generates client beans for the project (currently unsupported).
 * 
 * @author cooper
 */
public class BeanGeneratorMojo extends AbstractGWTMojo {

    /** Creates a new instance of BeanGeneratorMojo */
    public BeanGeneratorMojo() {
    }

    public void execute() throws MojoExecutionException, MojoFailureException {

        throw new MojoExecutionException(
                "not currently supported (may return in near future, but not included at present because of lack of project support/maintainers for this feature set)");
    }
}
