###JanusGraph
JanusGraph can be downloaded from https://github.com/JanusGraph/janusgraph/releases/tag/v0.2.3 
Download janusgraph-0.2.3-hadoop2.zip 

Run Cassandra: 
	Open terminal and go to 'janusgraph-0.2.2-hadoop2/bin'.
	Run sudo ./cassandra &
	Now to see if cassandra is working properly, we need to run: sudo ./nodetool statusthrift
	
Run ElasticSearch:
	Now to run Elasticsearch we need to run: ./../elasticsearch/bin/elasticsearch &
	
**Also make sure that you clean the JanusGraph before starting it.
Run:  sudo ./janusgraph.sh clean
For stopping the JanusGraph:  sudo ./janusgraph.sh stop

###Usage
The project JanusGraphBench is used to test different benchmarks on JanusGraph. 
This project takes a file config_benchmark.properties as a input file. The structure of input file is as follows:

config_benchmark.properties
-----------------------------------------------------------------------------------
no_of_inputFile=n  
inputFile1=input/lubm/univ-bench.owl [path of the input files]
.
.
.
inputFileN=input/lubm/University0_0.owl
outputFile=output/lubmResult.txt
mappingFile=input/lubm/lubmMapping.txt
queryFile=input/lubm/lubmQueries.txt
logFile=output/lubm_log.txt
cassandra=conf/jgex-cassandra.properties
------------------------------------------------------------------------------------

The program will create a JanusGraph using the N number of files containing triples and a mappingFile which contains the mapping of prefixes used and their URLs(seperated by =). 
It then runs the queries given in the queryFile on the JanusGraph and stores the result in outputFile. The outputFile contains the loading time of the triples and the results of the execution of queries and their execution time. 
The queryFile contains query in the SPARQL language and JanusGraph uses Gremlin language for queries so our software is using gremlinator for converting SPARQL queries to Gremlin traversals. 
Since the JanusGraph is stored in cassandra, cassandra contains the path of cassandra property file.    

Since gremlinator takes specific form of sparql queries therefore each query in the queryFile should be of the form:
It should start with '#mapping' and end with '#end'.

#mapping
SELECT ?yr WHERE 
{
  ?journal rdf:type bench:Journal .
  ?journal dc:title "Journal 1 (1940)" .
  ?journal dcterms:issued ?yr .
}
#end