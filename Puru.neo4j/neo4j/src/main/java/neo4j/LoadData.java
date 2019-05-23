package neo4j;

/*
	Importing necessary libraries
	Make sure Neo4j java driver is installed. If it is not already present,
	install the driver using maven. (Add dependency in pom.xml)
*/
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.Properties;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;
import org.neo4j.driver.v1.Value;

/*
Class LoadData takes input as RDF data in turtle/.n3/RdfXml format and outputs
a graph in neo4j. For more file formats, check github Readme
*/

public class LoadData implements AutoCloseable {
	public static long startTime = 0;
	public static long endTime = 0;
	// public static long loadTime = 0;

	private final Driver driver;

	// Constructor for initialisation.
	/*
	 * parameters: uri: bolt://localhost:7687 (uri will remain the same
	 * throughout as neo4j uses bolt protocol to establish connection) user:
	 * username for the neo4j account password: password for the neo4j accpunt
	 * 
	 */
	public LoadData(String uri, String user, String password) {
		driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
	}

	@Override
	public void close() throws Exception {
		driver.close();
		/*
		 * { commitSize: 5000 }
		 */
	}

	/*
	 * loadRDFtriples: helps in actual loading parameters: message: general
	 * message you want to print after loading ends.
	 * 
	 */
	public Long loadRDFtriples(final String message) throws IOException {
		System.out.println("Loading starts now");
		Timer.init();
		try (Session session = driver.session()) {
			String greeting = session.writeTransaction(new TransactionWork<String>() {

				@Override

				public String execute(Transaction tx) {
					// String dataPath = getProperty("data_path");
					String initialQuery = "CREATE INDEX ON :Resource(uri)";
					//tx.run(initialQuery);
					String dataPath1 = PropertyValues.getProperty("data_path");
					String format1 = PropertyValues.getProperty("dataFileFormat");
					String loadQuery1 = "CALL semantics.importRDF(\"" + dataPath1 + "\",\"" + format1
							+ "\",{commitSize:5000})";
					try {
						Log.createlog("Loading: " + dataPath1);
					} catch (IOException e) {
						System.out.println(e.getMessage());

					}
					// for reference //String query = "CALL
					// semantics.importRDF(\"file:///Users/purukathuria/Desktop/IP/benchmark/lubm8.ttl\",\"Turtle\",
					// { commitSize: 5000 })";

					StatementResult result = tx.run(loadQuery1);
					return result.single().get(0).asString();
				}
			});
			Long time = Timer.duration();
			System.out.println("load time in milliseconds : " + time);
			System.out.println("load time in seconds : " + (double) (time / 1000.0));

			Log.createlog("Time taken to load in milliseconds: " + time);

		}
		return Timer.duration();
	}

	// ####################################################################//
	// ###################### Utility Classes ########################//
	// ####################################################################//
	// Timer, PropertyValues, Log

	// ####################################################################//
	// ###################### Main Driver ########################//
	// ####################################################################//

	public static void main(String... args) throws Exception {
		String username = PropertyValues.getProperty("neo4jUserName");
		String password = PropertyValues.getProperty("neo4jPassword");
		String connection = PropertyValues.getProperty("neo4jConnection");

		LoadData rdf = new LoadData(connection, username, password);
		rdf.loadRDFtriples("loading");
		rdf.close();

	}
}