package virtuoso.jena.driver;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.util.FileManager;

public class loadingFile {
	public static void main(String[] args) {
		try {
			// For output file
			BufferedWriter writer = new BufferedWriter(
					new FileWriter("/Users/dakshyanisingh/Desktop/Output/Sehaj2WatdivLoading.txt"));
			Properties prop = new Properties();
			InputStream input = null;
			input = new FileInputStream("config.properties");
			prop.load(input);

			// Set connection with virtuoso
			// VirtModel.openDatabaseModel(java.lang.String graphName, java.lang.String url,
			// java.lang.String user, java.lang.String password)
			System.out.println(prop.getProperty("server"));
			Model model = VirtModel.openDatabaseModel("loadingSehaj2:watdiv", prop.getProperty("server"),
					prop.getProperty("database"), prop.getProperty("password"));
			// fetch location of triple file
			String nfile = prop.getProperty("f2SehajLocation");
			InputStream in = FileManager.get().open(nfile);
			System.out.println("1");
			if (in == null) {
				throw new IllegalArgumentException("File: not found");
			}
			System.out.println("2");

			long startTime = System.currentTimeMillis();
			// load triples
			model.read(in, null, "TURTLE");

			System.out.println("\nexecute: CLEAR GRAPH <http://LoadingWatDivSehaj2>");
			String str1 = "CLEAR GRAPH <http://LoadingWatDivSehaj2>";
			VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create(str1, model);
			vur.exec();
			long endTime = System.currentTimeMillis();
			// query to count the triples that were loaded
			Query sparql2 = QueryFactory
					.create("SELECT (count(*) AS ?count) FROM <http://LoadingWatDivSehaj2> WHERE {?s ?p ?o}");
			QueryExecution vqe = VirtuosoQueryExecutionFactory.create(sparql2, model);
			ResultSet result = vqe.execSelect();
			while (result.hasNext()) {
				QuerySolution soln = result.nextSolution();
				int count = soln.getLiteral("count").getInt();
				System.out.println("Count = " + count);
				writer.write("Count: " + count);
				writer.write("\n");
			}
			long totalTimeTaken = (endTime - startTime);
			// Write into output file (time taken)
			writer.write("Total time for loading in milliseconds: " + totalTimeTaken);
			System.out.println(totalTimeTaken);
			writer.close();

		} catch (Exception e) {
			System.out.println("Ex=" + e);
		}

	}

}
