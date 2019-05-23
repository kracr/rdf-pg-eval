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

public class CreateIndex implements AutoCloseable {

	private final Driver driver;

	public CreateIndex(String uri, String user, String password) {
		driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
	}

	@Override
	public void close() throws Exception {
		driver.close();
	}

	public Long createIdx() throws IOException {
		System.out.println("Loading starts now");
		Timer.init();
		try (Session session = driver.session()) {
			String greeting = session.writeTransaction(new TransactionWork<String>() {

				@Override

				public String execute(Transaction tx) {
					String initialQuery = "CREATE INDEX ON :Resource(uri)";

					tx.run(initialQuery);
					return "";
				}
			});
			Long time = Timer.duration();

			Log.createlog("creating index before loading(only done once)");

		}
		return Timer.duration();
	}

	public static void main(String... args) throws Exception {
		String username = PropertyValues.getProperty("neo4jUserName");
		String password = PropertyValues.getProperty("neo4jPassword");
		String connection = PropertyValues.getProperty("neo4jConnection");
		CreateIndex index = new CreateIndex(connection, username, password);
		index.createIdx();
		System.out.println("IndexCreated: Ready for loading now");
		index.close();
	}
}