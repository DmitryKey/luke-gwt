luke
====

This is a GWT version of  Luke tool. Version 0.1.

In order to compile and run you will need at least gwt 2.5.2 (tested) + gwt plugin. There two options as to how to acquire the plugin.

The first option (tried):

$ svn co http://gwt-maven.googlecode.com/svn/trunk/maven-googlewebtoolkit2-plugin/ maven-googlewebtoolkit2-plugin
$ cd maven-googlewebtoolkit2-plugin
$ mvn install

History:

* The original author is Andrzej Bialecki (https://code.google.com/p/luke)
* The project has been mavenized by Neil Ireson (see google group discussion here: http://bit.ly/16Y8utO)
* The project has been ported to Lucene trunk (marked as 5.0 at the time) by Dmitry Kan
* The project has been back-ported to Lucene 4.3 by sonarname, who later decided not to continue supporting the project
* There are updates to the (non-mavenized) project done by tarzanek (https://github.com/tarzanek/luke)

This fork's goal is:

0. Keep the project mavenized (compatible with Apache Lucene and Solr style)
1. Implement UI to an ASL compliant license framework so that it can be contributed back to Apache Lucene.
   Current work is done with GWT 2.5.1.
2. To revive the project and establish a single point of trust for the development and updates of the tool. This said,
   everyone is welcome to join.

One technical difficulty with older versions of Luke (based on thinlet UI) tackled here is:

Be able to load and study Lucene index from the server remotely, rather than require the index to always be local to the tool.
This has been implemented using the custom servlet for listing remote directory. The rest of mechanics is taken from Luke.

