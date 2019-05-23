
### [Blazegraph Installation]  
 (https://wiki.blazegraph.com/wiki/index.php/Quick_Start)  
 You will need to download Blazegraph.jar from the above mentioned link following for this project:

### Linux/Unix Intallation
 1. Download blazegraph.jar from https://sourceforge.net/projects/bigdata/files/latest/download .
 2. Go to the folder containing the Jar using Terminal and run the jar using 
 ```
 java -server -Xmx4g -jar blazegraph.jar .
 ```
 3. you will get following as output

```

INFO: com.bigdata.util.config.LogUtil: Configure: jar:file:/home/Akanksha/Desktop/blazegraph.jar!/log4j.properties

BlazeGraph(TM) Graph Engine

                   Flexible
                   Reliable
                  Affordable
      Web-Scale Computing for the Enterprise

Copyright SYSTAP, LLC DBA Blazegraph 2006-2016.  All rights reserved.

Akanksha-VirtualBox
Thu May 23 15:34:11 IST 2019
Linux/4.18.0-20-generic amd64
Intel(R) Core(TM) i7-6700HQ CPU @ 2.60GHz Family 6 Model 94 Stepping 3, GenuineIntel #CPU=4
Oracle Corporation 1.8.0_212
freeMemory=80433888
buildVersion=2.1.5
gitCommit=cb08991909034b5fba53c16f5c495e72ab011933

Dependency         License                                                                 
ICU                http://source.icu-project.org/repos/icu/icu/trunk/license.html          
bigdata-ganglia    http://www.apache.org/licenses/LICENSE-2.0.html                         
blueprints-core    https://github.com/tinkerpop/blueprints/blob/master/LICENSE.txt         
colt               http://acs.lbl.gov/software/colt/license.html                           
commons-codec      http://www.apache.org/licenses/LICENSE-2.0.html                         
commons-fileupload http://www.apache.org/licenses/LICENSE-2.0.html                         
commons-io         http://www.apache.org/licenses/LICENSE-2.0.html                         
commons-logging    http://www.apache.org/licenses/LICENSE-2.0.html                         
dsiutils           http://www.gnu.org/licenses/lgpl-2.1.html                               
fastutil           http://www.apache.org/licenses/LICENSE-2.0.html                         
flot               http://www.opensource.org/licenses/mit-license.php                      
high-scale-lib     http://creativecommons.org/licenses/publicdomain                        
httpclient         http://www.apache.org/licenses/LICENSE-2.0.html                         
httpclient-cache   http://www.apache.org/licenses/LICENSE-2.0.html                         
httpcore           http://www.apache.org/licenses/LICENSE-2.0.html                         
httpmime           http://www.apache.org/licenses/LICENSE-2.0.html                         
jackson-core       http://www.apache.org/licenses/LICENSE-2.0.html                         
jetty              http://www.apache.org/licenses/LICENSE-2.0.html                         
jquery             https://github.com/jquery/jquery/blob/master/MIT-LICENSE.txt            
jsonld             https://raw.githubusercontent.com/jsonld-java/jsonld-java/master/LICENCE
log4j              http://www.apache.org/licenses/LICENSE-2.0.html                         
lucene             http://www.apache.org/licenses/LICENSE-2.0.html                         
nanohttp           http://elonen.iki.fi/code/nanohttpd/#license                            
rexster-core       https://github.com/tinkerpop/rexster/blob/master/LICENSE.txt            
river              http://www.apache.org/licenses/LICENSE-2.0.html                         
semargl            https://github.com/levkhomich/semargl/blob/master/LICENSE               
servlet-api        http://www.apache.org/licenses/LICENSE-2.0.html                         
sesame             http://www.openrdf.org/download.jsp                                     
slf4j              http://www.slf4j.org/license.html                                       
zookeeper          http://www.apache.org/licenses/LICENSE-2.0.html                         

WARN : NanoSparqlServer.java:517: Starting NSS
WARN : ServiceProviderHook.java:171: Running.
serviceURL: http://172.17.0.1:9999


Welcome to the Blazegraph(tm) Database.

Go to http://172.17.0.1:9999/blazegraph/ to get started.

```
This message means that Blazegraph has been set up.



### LUBM  Triple Generation:

-- The official site of LUBM can be found at: http://swat.cse.lehigh.edu/projects/lubm/ . LUBM provides Ontology data about college departments, classes and professors. There are 14 queries provided in SPARQL to test response times of the system.
Steps followed to generate triples for Experiment.  
1.  Download the following
         1. Univ-Ben benchmark ontology: http://swat.cse.lehigh.edu/onto/univ-bench.owl
         2. The data generator (UBA) v1.7: http://swat.cse.lehigh.edu/projects/lubm/uba1.7.zip  
      Note: In case  of linux, you need to download a fix for UBA data generator: http://swat.cse.lehigh.edu/projects/lubm/GeneratorLinuxFix.zip . This fixes the paths for the linux based working environment because UBA generates path with a "\", but Linux uses "/" in the path. 
          1. You unzip this fix patch, it's called "Generator.java" as well. 
          2.  You unzip the UBA v1.7, and go to src/edu/lehigh/swat/bench/uba directory, to replace the original Generator.java with this fix Generator.java  
2. Compile the java files.
       To do that follow:
	 1. cd /home/Akanksha/Desktop/UBA1.7/src/edu/lehigh/swat/bench/uba
	2. javac -d /home/Akanksha/Desktop *.java  // this compiles all the java files and creates  folder called edu on Desktop.  
3. Run the data generater by usin command.
	java edu.lehigh.swat.bench.uba.Generator -univ <value> -index <value> -seed <value> -  daml -onto <value>
       options:
      -univ number of universities to generate; 1 by default
      -index starting index of the universities; 0 by default
      -seed seed used for random data generation; 0 by default
      -daml generate DAML+OIL data; OWL data by default
      -onto url of the univ-bench ontology ( http://swat.cse.lehigh.edu/onto/univ-bench.owl )  
option example:

java edu.lehigh.swat.bench.uba.Generator -univ 2 -index 0 -seed 0 -onto http://swat.cse.lehigh.edu/onto/univ-bench.owl .      
This will generate 2 universities data, with the default data in OWL file. If you want to generate daml, then add " -daml" before "-onto".

### Usage

Import the project in your IDE or navigate through console.  
2. Locate the config.properties `Akanksha/blazegraph/Configuration.properties`  
3. Set the parameters in config.properties file as follows.  
```
sparqlEndPoint=http://localhost:9999/blazegraph
repositoryName=watdiv_500k
tripleFilePath=/home/Akanksha/Desktop/ExperimentTestFolder/Triples
queryInputFilePath=/home/Akanksha/Desktop/ExperimentTestFolder/Query_Input
queryOutputFilePath=/home/Akanksha/Desktop/ExperimentTestFolder/Query_Output
InputFilePathForConversion=/home/Akanksha/Desktop/ExperimentTestFolder/InputFileFolder
TTLFolderPath=/home/Akanksha/Desktop/ExperimentTestFolder/TTLFolder
TripleInputforConversion=Nt
```
4. Run ConversionToTTL.java to convert Triples from any format to Turtle Format
5. Run DataLoading.java to load data into Blazegraph.
6. Run DataQuerying.java to run all the queries on the data provided in config file.

### Run Bulk Load

To do Bulk Load , run Bulk Load script present in `Akanksha/Bulk Load ' folder.

 To run the script use command :
 ```
 sh loadRestAPI.sh  (dir)*
 ```
 example:sh loadRestAPI.sh /home/Akanksha/Desktop/ExperimentTestFolder/Triples   
 
 The script uses fastload.properties that can be found in `Akanksha/Bulk Load` .
