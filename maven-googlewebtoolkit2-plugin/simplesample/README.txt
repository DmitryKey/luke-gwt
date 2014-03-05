GWT-Maven simplesample
-----------------------

Simple sample that demonstrates what a recommended POM looks like for GWT-Maven usage. 

Run with: 
"mvn gwt-maven:gwt" (run the shell) 

or 
"mvn install" (build a war)

or (subsequent runs, skip compiling again and skip tests)
"mvn -Dgoogle.webtoolkit-compileSkip=true -Dgoogle.webtoolkit.testSkip=true gwt-maven:gwt"

or 
"mvn gwt-maven:debug" (run with debugger hook, connect IDE to debug)


Note that the default POM uses the recommended "automatic" setup. This means GWT dependencies are declared in the
POM and GWT is downloaded and extracted from Maven repositories (the Maven way).  There is also an alternative
POM provided that demonstrates the "manual" way - pom-with-gwthome.xml.  This POM expects you to have 
GWT already installed on the local machine, and requires google.webtoolkit.home (gwtHome) to be specified, and GWT 
dependencies still must be present with system scope and correct systemPath.  You can run this alternative POM with 
"mvn -f pom-with-gwthome.xml gwt-maven:gwt" (assuming you edit the gwtHome and systemPath for your environment). 