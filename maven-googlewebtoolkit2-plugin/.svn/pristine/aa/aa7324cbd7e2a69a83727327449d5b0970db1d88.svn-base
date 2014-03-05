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

import com.totsp.mavenplugin.gwt.AbstractGWTMojo;

public final class ScriptWriterFactory {
    
    private ScriptWriterFactory() {
    }
    
    public static ScriptWriter getInstance(AbstractGWTMojo mojo) {
        ScriptWriter sw = null;
        if (AbstractGWTMojo.OS_NAME.startsWith(AbstractGWTMojo.WINDOWS)) {            
            if(mojo.getGwtVersion().startsWith("2.") ){
                sw = new ScriptWriterWindows20();
            } else if( mojo.getGwtVersion().startsWith("1.6") ){
                sw = new ScriptWriterWindows16();
            } else {
                sw = new ScriptWriterWindows();
            }
            System.out.println("windows");
        } else {
            if(mojo.getGwtVersion().startsWith("2.") ){
                sw = new ScriptWriterUnix20();
            } else if( mojo.getGwtVersion().startsWith("1.6") ){
                sw = new ScriptWriterUnix16();
            } else {
                sw = new ScriptWriterUnix();
            }
            System.out.println("unix");
        }        
        return sw;        
    }    
}