GWT-Maven simplesdataample
--------------------------

Simple sample that demonstrates what a recommended POM looks like for GWT-Maven usage,
and includes source web.xml and source context.xml for use with the GWT shell through
GWT-RPC (uses and HSQL database and creates a JNDI accessible Tomcat managed pooled DataSource). 

Run with: 
"mvn gwt-maven:gwt" (run the shell) 

or 
"mvn install" (build a war)

or (subsequent runs, skip compiling again and skip tests)
"mvn -Dgoogle.webtoolkit-compileSkip=true -Dgoogle.webtoolkit.testSkip=true gwt-maven:gwt"

or 
"mvn gwt-maven:debug" (run with debugger hook, connect IDE to debug)

