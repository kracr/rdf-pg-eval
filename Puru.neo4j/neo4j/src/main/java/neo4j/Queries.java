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

public class Queries implements AutoCloseable {
	static ArrayList<String> property = new ArrayList<String>();
	static ArrayList<String> vertex = new ArrayList<String>();
	static ArrayList<String> edge = new ArrayList<String>();
	public static long startTime = 0;
	public static long endTime = 0;

	private final Driver driver;

	public Queries(String uri, String user, String password) {
		driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
	}

	@Override
	public void close() throws Exception {
		driver.close();
		/*
		 * { commitSize: 5000 }
		 */
	}

	static ArrayList<String> listOfFiles = new ArrayList<>();

	public static void listFilesForFolder(final File folder) {
		String folderName = folder.toString();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {

				listOfFiles.add(folderName + '/' + fileEntry.getName());
			}
		}
		for (int i = 0; i < listOfFiles.size(); i++) {
			if (!listOfFiles.get(i).contains(".txt")) {
				listOfFiles.remove(i);
			}
		}

	}

	public static String returnQuery(String pathOfFile) throws IOException {
		File file = new File(pathOfFile);

		BufferedReader br = new BufferedReader(new FileReader(file));
		String retVal = "";
		String st;
		while ((st = br.readLine()) != null) {
			retVal += st;
		}
		return retVal;
	}

	public void runQuery(String queryToBeExecuted) throws Exception{
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
					System.out.println("Time for this query: (millisec) " + duration);
					System.out.println("Time for this query: (seconds) " + (double) (duration / 1000));
					System.out.println("---------------------------------------");
					System.out.println();
					System.out.println();
					try {
						Log.createlog("Time taken to run the below mentioned query in milliseconds: "+duration);
						Log.createlog(queryToBeExecuted);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
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
		System.out.println("#############  QueryExecution ##############");
		String queryFolderPath = PropertyValues.getProperty("queries_folder_path");
		System.out.println("Queries are contained in this folder:  ");
		System.out.println(queryFolderPath);
		
		final File folder = new File(queryFolderPath);
		listFilesForFolder(folder);
		System.out.println("Files in the folder are listed below as: ");
		System.out.println("------------------------------------------------");
		System.out.println(listOfFiles);
		System.out.println("------------------------------------------------");

		System.out.println("Executing file by file: (Query file should be in a txt format )");
		/*
		 * data_path=file:///Users/purukathuria/Desktop/sp2b.n3
	queries_folder_path = /Users/purukathuria/Desktop/SP2_Queries
neo4jConnection = bolt://localhost:7687
neo4jUserName = neo4j
neo4jPassword = password
dataFileFormat = Turtle
		 */
		String username = PropertyValues.getProperty("neo4jUserName");
		String password = PropertyValues.getProperty("neo4jPassword");
		String connection = PropertyValues.getProperty("neo4jConnection");
		Queries execute = new Queries(connection, username, password);

		for (int i = 0; i < listOfFiles.size(); i++) {
			
			String query = returnQuery(listOfFiles.get(i));
			execute.runQuery(query);
		}
		
		System.out.println("closing connection");
		System.out.println("xxxxxxxxxxxxxxxxxxxxx");
		execute.close();

	}
}