package neo4j;


import java.io.*;
import java.util.ArrayList;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;

public class DeleteAll implements AutoCloseable {

	private final Driver driver;

	public DeleteAll(String uri, String user, String password) {
		driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
	}

	@Override
	public void close() throws Exception {
		driver.close();
		/*
		 * { commitSize: 5000 }
		 */
	}



	public void deleteAllNodes(String queryToBeExecuted) {
		System.out.println("Executing this query:  ");
		System.out.println("---------------------------------------");
		System.out.println(queryToBeExecuted);
		System.out.println("---------------------------------------");
		try (Session session = driver.session()) {
			String greeting = session.writeTransaction(new TransactionWork<String>() {
				@Override
				public String execute(Transaction tx) {
					Timer.init();

					StatementResult result = tx.run(queryToBeExecuted);
					long duration = Timer.duration();
					System.out.println("---------------------------------------");
					System.out.println("Time for deletion query: (millisec) " + duration);
					System.out.println("Time for deletion query: (seconds) " + (double) (duration / 1000));
					System.out.println("---------------------------------------");
					System.out.println();
					System.out.println();
					
					return "";// result.single().get(0).asString();
				}
			});

		}
	}

	public static void main(String... args) throws Exception {

		/*
		 * listOfFiles contains a list of all the query files.
		 */

		// use config to access its pathname
		System.out.println("#############  Deleting All nodes from Neo4j ##############");
		String username = PropertyValues.getProperty("neo4jUserName");
		String password = PropertyValues.getProperty("neo4jPassword");
		String connection = PropertyValues.getProperty("neo4jConnection");
		DeleteAll deleteNodes = new DeleteAll(connection, username, password);
		deleteNodes.deleteAllNodes("MATCH (n) DETACH DELETE n");
		deleteNodes.deleteAllNodes("MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r");
		
		System.out.println("closing connection");
		System.out.println("xxxxxxxxxxxxxxxxxxxxx");
		deleteNodes.close();

	}
}