Latest source code on Github
The latest source code for Virtuoso can be obtained from Github using :

$ git clone git://github.com/openlink/virtuoso-opensource.git

Using Pre-built binaries: 

These periodically produced pre-built binaries, typically from stable milestones, will let you get up-and-running quickly with VOS, without building from code yourself.
You can manually browse to locate pre-built binaries of this and other versions, or download via the links below.

GNU/Linux:
Built against glibc 2.5, this should work on all more recent glibc versions: https://sourceforge.net/projects/virtuoso/files/virtuoso/7.2.5/virtuoso-opensource.x86_64-generic_glibc25-linux-gnu.tar.gz/download

macOS a/k/a Mac OS X:
A drag-and-drop installer for macOS (Mavericks [10.9] and later): https://sourceforge.net/projects/virtuoso/files/virtuoso/7.2.5/virtuoso-opensource-7.2.5-macosx-app.dmg/download

Microsoft Windows:
A standard double-click installer for Windows: https://sourceforge.net/projects/virtuoso/files/

Obtaining  GPL license : http://vos.openlinksw.com/owiki/wiki/VOS/VOSLicense



What is Jena?
Jena is an open source Semantic Web framework for Java. It provides an API to extract data from and write to RDF graphs. The graphs are represented as an abstract "model". A model can be sourced with data from files, databases, URIs or a combination of these. A Model can also be queried through SPARQL and updated through SPARUL.

What is the Virtuoso Jena Provider?
The Virtuoso Jena RDF Data Provider is a fully operational Native Graph Model Storage Provider for the Jena Framework, which enables Semantic Web applications written using the Jena RDF Frameworks to directly query the Virtuoso RDF Quad Store. Providers are available for the latest Jena 2.6.x - 2.13.x and 3.0.x versions. 

Setting up the JDBC connection strings therein to point to a valid Virtuoso Server instance, using the form: 

"jdbc:virtuoso://<virtuoso-hostname-or-IP-address>[:<data port>]/charset=UTF-8/log_enable=2", "<username>", "<password>"   
Default data port : 1111
Default username : dba
Default password : dba


Prerequisites:

Download the latest Virtuoso Jena Provider for your Jena framework version, Virtuoso JDBC Driver, Jena Framework, and associated classes and sample programs.

Note: You must use a matching set of Jena Provider and JDBC Driver.
The Jena Provider for Jena 2.6 requires the Virtuoso JDBC 3 Driver.
The Jena Provider for Jena 2.1x.x and 3.0.x requires the Virtuoso JDBC 4 Driver. 

This project uses Jena 3.0.x.

Virtuoso Jena 3.0.x Provider JAR file : virt_jena3.jar 
Virtuoso JDBC 4 Driver JAR file	: virtjdbc4.jar
Link : http://vos.openlinksw.com/owiki/wiki/VOS/VOSDownload#Jena%20Provider


Jena 3.0.x dependencies : 

junit-4.5.jar
commons-lang3-3.3.2.jar
jena-arq-3.0.0.jar
jena-iri-3.0.0.jar
jena-base-3.0.0.jar
jena-core-3.0.0.jar
jena-core-3.0.0-tests.jar
jcl-over-slf4j-1.7.12.jar
log4j-1.2.17.jar
slf4j-api-1.7.12.jar
slf4j-log4j12-1.7.12.jar
xercesImpl-2.11.0.jar
xml-apis-1.4.01.jar
jena-shaded-guava-3.0.0.jar
virtjdbc4.jar
virt_jena3.jar



Please note:

For LUBM : inferencing has been tried using Open Coded Inference where all combinations of subclass and subproperty relations are expressed as unions. 
Link : http://vos.openlinksw.com/owiki/wiki/VOS/VOSArticleLUBMBenchmark
