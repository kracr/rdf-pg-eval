package com.Blazegraph;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openrdf.model.Statement;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.rio.RDFFormat;

import com.bigdata.rdf.sail.webapp.SD;
import com.bigdata.rdf.sail.webapp.client.ConnectOptions;
import com.bigdata.rdf.sail.webapp.client.JettyResponseListener;
import com.bigdata.rdf.sail.webapp.client.RemoteRepository;
import com.bigdata.rdf.sail.webapp.client.RemoteRepositoryManager;

public class DataLoading {

	
	
	

	protected static final Logger log = Logger.getLogger(DataLoading.class);
	private  String sparqlEndPoint;
	private String repositoryName;
	private String tripleFilePath ;
	//private String tripleFileName;
	private String  tripleFileExtension;
	RemoteRepositoryManager repo;
	

	public static void main(String[] args) throws Exception {
		
		DataLoading d= new DataLoading();
		d.readConfigFile(); //read config file
			//for(int i=0;i<1;i++)
			//{
				d.startLoad();
		//	}
	}

	void startLoad() throws Exception
	{
		
		
		repo = new RemoteRepositoryManager(sparqlEndPoint, false /* useLBS */);
		log.info(repo.getBaseServiceURL());
		
		
		//System.out.println("deleted");
		File folder;
		//DataLoading_LUBM d= new DataLoading_LUBM();			
				
   

		//System.out.println(repo.getBaseServiceURL());

		try {

			JettyResponseListener response = getStatus(repo);
			log.info(response.getResponseBody());
			//System.out.println("Done1");

			// create a new namespace if not exists
			final String namespace = repositoryName;
			final Properties properties = new Properties();
			//final Properties properties = loadProperties("/home/panouti/Desktop/ExperimentTestFolder/fastload.properties");
			
//			if (properties.getProperty(com.bigdata.journal.Options.FILE) == null) {
//	            /*
//	             * Create a backing temporary file iff none was specified in the
//	             * properties file.
//	             */
//	            final File journal = File.createTempFile("bigdata", ".jnl");
//	            journal.deleteOnExit();
//	            properties.setProperty(BigdataSail.Options.FILE, journal
//	                    .getAbsolutePath());
//	}
			
			properties.setProperty("com.bigdata.rdf.sail.namespace", namespace);
			//properties.setProperty("com.bigdata.rdf.relation.namespace", namespace);
			//properties.setProperty("com.bigdata.rdf.sail.truthMaintenance","true");
			//properties.setProperty("com.bigdata.rdf.store.AbstractTripleStore.justify", "true");
			//properties.setProperty("com.bigdata.rdf.store.AbstractTripleStore.statementIdentifiers","false");
			//properties.setProperty("com.bigdata.rdf.store.AbstractTripleStore.axiomsClass", "com.bigdata.rdf.axioms.OwlAxioms");
			//properties.setProperty("com.bigdata.rules.InferenceEngine", "com.bigdata.rdf.axioms.OwlAxioms");
			//properties.setProperty("com.bigdata.rdf.store.AbstractTripleStore.closureClass", "com.bigdata.rdf.rules.FastClosure");
	
			if(namespaceExists(repo, namespace))
			{
				log.info(String.format("delete namespace %s...", namespace));
				repo.deleteRepository(repositoryName);
				System.out.println("deleted");
				log.info(String.format("Create namespace %s done", namespace));
			} 
			
			repo.createRepository(namespace, properties);
			
		/*	if (!namespaceExists(repo, namespace)) {
				log.info(String.format("Create namespace %s...", namespace));
				repo.createRepository(namespace, properties);
				log.info(String.format("Create namespace %s done", namespace));
			} else {
				log.info(String.format("Namespace %s already exists", namespace));
			} */

			//get properties for namespace
			log.info(String.format("Property list for namespace %s", namespace));
			response = getNamespaceProperties(repo, namespace);
			//System.out.println(response.getResponseBody());
			log.info(response.getResponseBody());

			/*
			 * Load data from file located in the resource folder
			 * src/main/resources/data.n3
			 */


			folder = new File(tripleFilePath);
			File[] fileNames = folder.listFiles();
			String fname;
			long startTime=0,stopTime=0;
			long loadingTime = 0;
			for(int i=0;i<fileNames.length;i++)
			{
				fname= fileNames[i].getName();
				//System.out.println(fname);
				if(fname.contains(tripleFileExtension))
				{
					startTime=System.currentTimeMillis();
					loadDataFromResource(repo, namespace, tripleFilePath+"/"+fname,fname);
					stopTime=System.currentTimeMillis();
					loadingTime+=stopTime-startTime;

				}

			}
			System.out.println(" Loading Time :"+loadingTime+" ms");



		} finally {
			repo.close();
		}
	}
	/*
	 * Status request.
	 */
	private  JettyResponseListener getStatus(RemoteRepositoryManager repo)
			throws Exception {

		ConnectOptions opts = new ConnectOptions(sparqlEndPoint + "/status");
		opts.method = "GET";
		//System.out.println(opts);
		return repo.doConnect(opts);

	}

	/*
	 * Check namespace already exists.
	 */
	private  boolean namespaceExists(RemoteRepositoryManager repo,
			String namespace) throws Exception {
		//System.out.println(re);
		GraphQueryResult res = repo.getRepositoryDescriptions();
		try {
			while (res.hasNext()) {
				Statement stmt = res.next();
				//System.out.println(stmt);
				if (stmt.getPredicate()
						.toString()
						.equals(SD.KB_NAMESPACE.stringValue())) {
					if (namespace.equals(stmt.getObject().stringValue())) {
						return true;
					}
				}
			}
		} finally {
			res.close();
		}
		return false;
	}

	/*
	 * Get namespace properties.
	 */
	private  JettyResponseListener getNamespaceProperties(
			RemoteRepositoryManager repo, String namespace) throws Exception {

		ConnectOptions opts = new ConnectOptions(sparqlEndPoint + "/namespace/"
				+ namespace + "/properties");
		opts.method = "GET";
		return repo.doConnect(opts);

	}

	/*
	 * Load data into a namespace.
	 */
	private  void loadDataFromResource(RemoteRepositoryManager repo,
			String namespace, String resource,String TripleIn) throws Exception {
		//InputStream is = DataLoading.class.getResourceAsStream(resource);
		  InputStream is =new FileInputStream(resource);
		  RDFFormat f = null;
		/*if (is == null) {
			throw new IOException("Could not locate resource: " + resource);
		}*/
		  if(TripleIn.contains("n3"))
		  {
			  f= RDFFormat.N3;
		  }
		  else if (TripleIn.contains("owl"))
		  {
			  f= RDFFormat.RDFXML;
		  }
		  else if(TripleIn.contains(".nt"))
		  {
			  f= RDFFormat.NTRIPLES;
		  }
		  else if(TripleIn.contains("ttl"))
		  {
			  f= RDFFormat.TURTLE;
		  }
		 
		       
		try {
			repo.getRepositoryForNamespace(namespace).add(new RemoteRepository.AddOp(is, f));
			//BigdataSailRemoteRepository b=	repo.getRepositoryForNamespace(namespace).getBigdataSailRemoteRepository();
		
			
			
		} finally {
			is.close();
		}
	}

	public void readConfigFile()
	{
		Properties prop = new Properties();
	    InputStream input = null;
	    
	    try {
			input = new FileInputStream("Configuration.properties");
			
			 // load a properties file
	        prop.load(input);
	        
	        sparqlEndPoint=prop.getProperty("sparqlEndPoint").trim();
	        repositoryName= prop.getProperty("repositoryName").trim();
	        tripleFilePath=prop.getProperty("tripleFilePath");
	       // tripleFileName=prop.getProperty("tripleFileName");
	        tripleFileExtension=prop.getProperty("tripleFileExtension").trim();
	      //  tripleFileExtension="ttl";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

       
	}
	
//	 public Properties loadProperties(String resource) throws Exception { // when using fastload
//	        Properties p = new Properties();
//	        InputStream is =new FileInputStream(resource);
//	       // InputStream is1 = getClass().getResourceAsStream(resource);
//	        p.load(new InputStreamReader(new BufferedInputStream(is)));
//	        is.close();
//	        return p;
//	        
//	       
//	}
	/*public void callQueryData(RemoteRepositoryManager repo,String namespace)
	{
		DataQuerying d=new DataQuerying();
		d.queryData(repo,namespace);
	}*/

}
