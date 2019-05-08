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
import org.apache.jena.tdb.TDBFactory ;
import org.apache.jena.tdb.TDBLoader;
import org.apache.jena.util.FileManager;
import org.apache.jena.rdf.model.Model ;
import org.apache.jena.rdf.model.ModelFactory ;
import org.apache.jena.rdf.model.Property ;
import org.apache.jena.rdf.model.Resource ;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

public class finalLoading
{
    public static void main(String... argv)
    {
    	
    	long totalLoadingTime = 0 ;    // records total loading time for all files present in the folder 
    	
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
  
        File location = new File(config.getProperty("fLocation"));  // fLocation is the location of the folder which has the triple files
        
        System.out.println(location.getAbsolutePath());
        
        String directory = config.getProperty("dName") ;
        
        File[] fList = location.listFiles();
        
        for (File file : fList){
        
        	if (file.isFile()){
            
        		String f = file.getName();
                
        		if (f.endsWith(".nt"))   // this helps in loading all files present in the folder with .nt extension for Watdiv, .n3 for SP2B, .owl for LUBM
                {
                	
                	System.out.println("Opening File " + f);
                	
                	System.out.println();
                	
                	String source = file.getAbsolutePath();
                	
                	Dataset dataset = TDBFactory.createDataset(directory) ;  // Location of directory is the location where the created database is saved in memory
                	
                	dataset.begin(ReadWrite.WRITE);   // It is important to begin the transaction and end the transaction when the dataset work has concluded. This prevents corruption of node table
                	
                    Model model = dataset.getDefaultModel();
                    
                	long t0, t1;
                	
                	t0 = System.currentTimeMillis();    // Start timer prior to loading
                	
                	FileManager.get().readModel(model, source, "RDF");    // Read the dataset into the default model
                	 
                	t1 = System.currentTimeMillis();   // Stop the timer after loading 
                    
                	System.out.println("Loading TIME=" + (t1 - t0));   // Record the difference
                    
                    totalLoadingTime += (t1-t0);       // Add the difference to the total loading time for files. 
                    
                    dataset.commit();                  // commit the changes made in the dataset
                	
                    dataset.end();                     // Close the transaction 
                    
                	
                }
            }
            
            
            
        }
        
        System.out.println("Total Loading time for all files = " + totalLoadingTime);   // This is the total loading time
        
    }
    
}

