package finalTDB;

import javax.swing.Timer;
import org.apache.jena.query.Dataset ;
import org.apache.jena.query.Query ;
import org.apache.jena.query.QueryExecution ;
import org.apache.jena.query.QueryExecutionFactory ;
import org.apache.jena.query.QueryFactory ;
import org.apache.jena.query.QuerySolution ;
import org.apache.jena.query.ReadWrite ;
import org.apache.jena.query.ResultSet ;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.tdb.TDBFactory ;
import org.apache.jena.tdb.TDBLoader;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.ReasonerVocabulary;
import org.junit.runners.model.Statement;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model ;
import org.apache.jena.rdf.model.ModelFactory ;
import org.apache.jena.rdf.model.Property ;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource ;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.rulesys.RDFSRuleReasonerFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

public class q
{
	public static void main(String ...argv) {
		
		InputStream in = finalLoading.class.getResourceAsStream("configFile.properties"); 
		
    	Properties config = new Properties(); 
    	
    	try 
    	{ 
    		config.load(in); 
    	}
    	
    	catch (IOException e1) 
    	{ 
    		e1.printStackTrace(); 
    	}    	
		
		String directory = config.getProperty("dName");   // dName is where the dataset was created in memory
    	
		Dataset dataset = TDBFactory.createDataset(directory);    // dataset is loaded here 
		
		
		Model tdb = dataset.getDefaultModel();     // Default model is initialised with the dataset
		
		Reasoner reasoner = RDFSRuleReasonerFactory.theInstance().create(null);      // Reasoner is enabled
		reasoner.setParameter(ReasonerVocabulary.PROPsetRDFSLevel,
		                      ReasonerVocabulary.RDFS_SIMPLE);
		
		// ALTERNATE WAY for enabling the reasoner
		
		//Resource config1 = tdb.createResource()
	     //           .addProperty(ReasonerVocabulary.PROPsetRDFSLevel, "full");
			
		//Reasoner reasoner = RDFSRuleReasonerFactory.theInstance().create(config1);		
			
		InfModel inf = ModelFactory.createInfModel(reasoner, tdb);    // Creating the inference model
			
		inf.prepare();
					
						
		String queryFolder = config.getProperty("qFolder") ;    // Location of folder where all query txt files are saved individually
		
		File location = new File(queryFolder);
		
		File[] fList = location.listFiles();
        
		int count = 1;
		
		for (File file : fList)
        {
            if (file.isFile())
            {
                String x=file.getName();
                
                String absPath=file.getAbsolutePath();
                
                if (x.endsWith(".txt"))
                {
                	BufferedReader reader ;
                	
                	String sparqlQueryString = null;
                	
                	try
                	{
                		reader = new BufferedReader(new FileReader(absPath));
                		String line ;
            			sparqlQueryString="";
            			
            			while ((line = reader.readLine())!=null) {
                			String str=line;
                			sparqlQueryString = sparqlQueryString + " " + str ;
                			
                		}
                		
                	}
                	
                	catch (FileNotFoundException e)
                	{
                		e.printStackTrace();
                	}
                	
                	catch (IOException e)
                	{
                		e.printStackTrace();
                	}
                	
                	System.out.println(sparqlQueryString);      // sparqlQueryString represents the string that is read from the file after basic string manipulation
                	
                	Query query = QueryFactory.create(sparqlQueryString);
                	
                	QueryExecution qexec = QueryExecutionFactory.create(query,tdb);
                	
                	try
                	{
                		PrintStream out = new PrintStream(new FileOutputStream(config.getProperty("logFile") + "/" + Integer.toString(count) + ".txt"));
                    	
                    	System.setOut(out);
                    	
                    	long t0, t1;
                    	
                    	t0 = System.currentTimeMillis();    // Timer is started
                    	
                    	ResultSet results = qexec.execSelect();
                    	
                    	t1 = System.currentTimeMillis();     // Timer is stopped
                    	
                    	System.out.println("Time taken for execution = " + (t1 - t0));   // Difference is recorded as time for execution
                    	
                    	int c = 0;                           // This helps in keeping count of the number of results generated by the query
                    	
                    	for ( ; results.hasNext() ; )
                        {
                            QuerySolution soln = results.nextSolution() ;
                            c=c+1;
                        }
                    	
                    	System.out.println(c);
                    	
                    	ResultSetFormatter.out(out,results,query);       // Outputs the formatted results to the txt file in the Results folder obtained from the configuration file
                    
                    	count = count + 1;
                	}
                	
                	catch (FileNotFoundException e)
                	{
                		e.printStackTrace();
                	}
                	
                }
                
            }
            
        }
        		
	}

}