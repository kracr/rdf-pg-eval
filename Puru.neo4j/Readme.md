# [Neo4j Installation](https://neo4j.com/download-center/#releases)
You will need the following for this project:
1. Neo4j server
2. Neo4j Java Driver
3. neosemantics 
4. Benchmark to run the experiments. (Load, Query and Reasoning)



### Linux/Unix Installation


Download Neo4j community server/Enterprise server/Desktop version from [here](https://neo4j.com/download-center/#releases)

1. Open up your terminal/shell/console.

2. Extract the contents using unzip/tar.

```bash
tar -xf neo4j-edition-version-unix.tar.gz
```

3. Run Neo4j using the below command. (NEO4J_HOME is the top level directory). Insetad of 'console', you can use 'neo4j start' to start the server process in background. 
```bash
$NEO4J_HOME/bin/neo4j console
```
4. Visit  [http://localhost:7474](http://localhost:7474) in your browser, change the password here for the neo4j account. 

### Windows (exe) Installation
1. The installer includes the Java version needed for running Neo4j.

2. Launch the installer you just downloaded. You might have to give the installer permission to make changes to your computer.

3. Follow the prompts, and choose the option to Run Neo4j.

4. Click on the Start button to start the Neo4j server.

5. Open the provided URL in your local web browser.
Change the password for the neo4j account.

### Windows (zip) Installation
1. If java is not already installed, get [OpenJDK 8](http://openjdk.java.net/) or [Oracle Java 8](https://www.oracle.com/technetwork/java/javase/downloads/index.html), recommended for Neo4j. Version 8 is recommended for releases prior to 3.5.5. (Check for the latest Neo4j release and download the suitable JDK). 

2. Find the zip file you just downloaded and right-click, extract all.

3. Place the extracted files in a permanent home on your server. The top level directory is referred to as NEO4J_HOME, for example D:\neo4j\.

4. Start and manage Neo4j using the Windows PowerShell module included in the zip file.

5. Visit [http://localhost:7474](http://localhost:7474) in your web browser.

6. Change the password for the neo4j account. 


# [Neo4j Java Driver](https://github.com/neo4j/neo4j-java-driver)
[This](https://github.com/neo4j/neo4j-java-driver) repository holds the official Java driver for Neo4j.

#### The pom.xml file

The driver is distributed exclusively via [Maven](https://maven.apache.org/). To use the driver in your Maven project, include the following within your pom.xml file:  (This project uses version 1.7.4)
```maven
<dependency>
    <groupId>org.neo4j.driver</groupId>
    <artifactId>neo4j-java-driver</artifactId>
    <version>x.y.z</version>
</dependency>
```
#### Building
``` bash
mvn clean install
```


# [neosemantics](https://github.com/jbarrasa/neosemantics#installation)

You can either download a prebuilt jar from the [releases area](https://github.com/jbarrasa/neosemantics/releases) or build it from the source. If you prefer to build, check the note below.
1. Copy the the jar(s) in the <NEO_HOME>/plugins directory of your Neo4j instance. (note: If you're going to use the JSON-LD serialisation format for RDF, you'll need to include also APOC)
2. Add the following line to your <NEO_HOME>/conf/neo4j.conf
```bash
dbms.unmanaged_extension_classes=semantics.extension=/rdf
```
3. Restart the server.
4. Check that the installation went well by running ```bash call dbms.procedures()```. The list of procedures should include the ones implemented in the library.

#### Note on build

When you run

``` bash 
mvn clean package
```
This will produce two jars :

1. A neosemantics-[...].jar This jar bundles all the dependencies.
2. An original-neosemantics-[...].jar This jar is just the neosemantics bit. So go this way if you want to keep the third party jars separate. In this case you will have to add all third party dependencies (look at the pom.xml).

### Usage of neosemantics in our project

semantics.importRDF

|Procedures | Parameters  | Description  |   
|---|---|---|
| `semantics.importRDF`  |   URL of the dataset and serialisation format | Imports into Neo4j all the triples in the data set according to the mapping defined in this post. Note that before running the import procedure an index needs to be created on property uri of Resource nodes. Just run CREATE INDEX ON :Resource(uri) on your Neo4j DB. Examples:` CALL semantics.importRDF("file:///.../myfile.ttl","Turtle", { shortenUrls: false, typesToLabels: true, commitSize: 9000 })` |


  

*Note*: for more information on neosemantics and its procedures, visit [this](https://github.com/jbarrasa/neosemantics#installation).


# Usage
1. Import the project in your IDE or navigate through console.
2. Locate the config.properties 
`neo4j/src/main/java/neo4j/config.properties` 
3. Set the parameters in config.properties file as follows. 

```
data_path=<path of triple file>
queries_folder_path = <path of folder containing all the queries in txt>
neo4jConnection = bolt://localhost:7687
neo4jUserName = neo4j
neo4jPassword = password
dataFileFormat = <supported triple file format>

```
4. Run CreateIndex.java when loading for the first time to create an index. 
5. Run LoadData.java to load data into Neo4j.
6. Run Queries.java to run all the queries on the data provided in config file.
7. (Optional) DeleteAll.java deletes all nodes and relationships. 

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.