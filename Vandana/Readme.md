# Apache Jena TDB Installation

(https://jena.apache.org/download/index.cgi)
You will need to download the Apache Jena TDB API for this project by clicking on the link given above. 

Requirements : Java8 version

1. Open the folder apache-jena-3.10.x (version number) using Eclipse. 
2. Create a new folder in the first level of subdirectories with a name of your choice (eg : TDB)
3. Locate file name pom.xml and edit the file to include the pom.xml in this repository. 

Basic change made 

```
<dependency>
    	<groupId>org.slf4j</groupId>
    	<artifactId>slf4j-log4j12</artifactId>
    	<version>1.7.5</version>
    	<scope>test</scope>
</dependency>

``` 
4. Create new files for loading and querying data in the folder created in step 2. 

# SP2B Triple Generation

1. Download SP2B Benchmark from (http://dbis.informatik.uni-freiburg.de/forschung/projekte/SP2B/)
2. After downloading SP2B Benchmark, move from command line and change working directory to sp2b -> bin
3. On command line execute command ./sp2b_gen [BREAK_CONDITION] [OUTFILE] which is specified in README File given in bin folder. 
4. This will generate an output file in the bin folder by name of 'sp2b.n3'. These are your generated triples. 

# Loading these triples

1. After creating triples and the loading file in Jena API, create a configuration file within the same folder. 
2. In the configuration file, specify the fLocation to the location where the generated .n3 file is saved. 
3. Specify the dName to the location where the database has to be created. 
4. Execute the loading file. The output time for loading will be displayed on the console. 

# Querying triples

1. For querying triples, open the sp2b folder. sp2b -> queries
2. Open the queries.txt file. This file contains sparql queries in text format. 
3. Create a new folder on your computer called qFolder. Create separate text files for each of the queries. 
4. Next, update the configuration file with the qFolder location
5. Create a folder called Results. Keep it empty. 
6. Now execute the querying.java. 
7. Different output files will be created in the results folder each recording the time in ms taken by the query for its execution.
