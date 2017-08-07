Template Processor component for Alfresco 5.x
===============================================================
[![Build Status](https://www?branch=master)](https://www)

This project contains a Template Processor for filling PDF-documents
 with values using meta-data.
 
Usage
--------
Add the dependencies to the Alfresco repository and share POM files of your WAR projects.

<dependencies>
.... 
TODO: add dependencies



Installation TODO:rewrite
------------

The component has been developed to install on top of an existing Alfresco
5.0 and higher installation. The `*.amp` or
`*.amp` needs to be installed into the Alfresco
Repository / Share webapp using the Alfresco Module Management Tool:

    java -jar alfresco-mmt.jar install plugin-repo-<version>.amp /path/to/alfresco.war
    java -jar alfresco-mmt.jar install plugin-share-<version>.amp /path/to/share.war

You can also use the Alfresco Maven SDK to install or overlay the AMP during the build of a
Repository / Share WAR project. See https://artifacts.alfresco.com/nexus/content/repositories/alfresco-docs/alfresco-lifecycle-aggregator/latest/plugins/alfresco-maven-plugin/advanced-usage.html
for details.

Building TODO:rewrite
--------

To build the module and its AMP / JAR files, run the following command from the base
project directory:

    mvn install

The command builds JAR file named `plugin-repo-<version>.jar`.

Using the component TODO:rewrite
-------------------

- Description How-To