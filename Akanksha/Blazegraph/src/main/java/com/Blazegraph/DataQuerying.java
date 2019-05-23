package com.Blazegraph;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQueryResult;

import com.bigdata.rdf.sail.webapp.client.ConnectOptions;
import com.bigdata.rdf.sail.webapp.client.JettyResponseListener;
import com.bigdata.rdf.sail.webapp.client.RemoteRepositoryManager;

public class DataQuerying {
	
	protected static final Logger log = Logger.getLogger(DataQuerying.class);
	
	private String queryInputFilePath;
	private ArrayList<String> Queries;
	private HashMap<String,String>QueriesHM;
	private  String sparqlEndPoint;
	private String repositoryName;
	private String queryOutputFilePath;
	private FileWriter fw = null;
	
	public  static void  main(String args[])
	{
		DataQuerying d= new DataQuerying();
		d.readConfigFile();
		d.readQueryFile();
		d.openConnection();
	}
	
	public void openConnection()
	{
		
		final RemoteRepositoryManager repo = new RemoteRepositoryManager(sparqlEndPoint, false /* useLBS */);
		//System.out.println(repo.getBaseServiceURL());
		try {

			JettyResponseListener response = getStatus(repo);
			//log.info(response.getResponseBody());
			//System.out.println("Done1");

			
			response = getNamespaceProperties(repo, repositoryName);
			log.info(response.getResponseBody());
			
			
			
			FileUtils.cleanDirectory(new File(queryOutputFilePath));
			queryData(repo);
	

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				repo.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void queryData(RemoteRepositoryManager repo)
	{
		ArrayList<String> queryResult= new ArrayList<String>();
		int line=0;
			// execute query
			TupleQueryResult result = null;
			boolean booleanResult;
			try {
				
				/*String q="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
						+"PREFIX ub: <http://swat.cse.lehigh.edu/onto/univ-bench.owl#> "
							+"SELECT ?X WHERE "
							+"{?X rdf:type ub:GraduateStudent ."
							 +" ?X ub:takesCourse <http://www.Department0.University0.edu/GraduateCourse5>}";
					System.out.println("q:"+q);*/
				long startTime=0,stopTime=0;
				//long loadingTime = 0;
				try {
				
					//System.out.println(Queries.size());
				if(QueriesHM.size()!=0)
				{
					for(Map.Entry<String, String> entry :QueriesHM.entrySet())
					{
						//System.out.println("-----------------------------------------------------------");
						queryResult.clear();
						line=0;
						//System.out.println("Query "+entry.getKey()+"\n"+entry.getValue());
						if(entry.getKey().contains("q12")) // for boolean query in SP2bench
						{
							
							startTime=System.currentTimeMillis();
							booleanResult=repo.getRepositoryForNamespace(repositoryName).prepareBooleanQuery(entry.getValue()).evaluate();
							stopTime=System.currentTimeMillis();
							queryResult.add(line++,String.valueOf(booleanResult));
						}
						else
						{
							//System.out.println(entry.getKey());
							startTime=System.currentTimeMillis();
							result= repo.getRepositoryForNamespace(repositoryName).prepareTupleQuery(entry.getValue()).evaluate();
							stopTime=System.currentTimeMillis();
							
							while (result.hasNext()) {
								BindingSet bs = result.next();
								queryResult.add(line++,bs.toString());
								
							} 
						}
						writeToFile(queryResult,entry.getKey());
						System.out.println("Time for "+entry.getKey()+": "+(stopTime-startTime)+" ms\n");
						
					}
				
				}
				} finally {
					if(result!=null)
					result.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
	public void readQueryFile()
	{
		
		String path= queryInputFilePath+File.separator;
		System.out.println(path);
		BufferedReader br = null;
		String st; 
		Queries= new ArrayList<String>();
		QueriesHM = new HashMap<String,String>();
		String s="";
		 try
		 {
			 ///input = new FileInputStream("queries.txt");
			 
			 File folder = new File(path); 
			// File file = new File("src/main/resources/queries.txt"); 
			 File[] fileNames = folder.listFiles();
			 String fname;
			
			 for(int k=0;k<fileNames.length;k++)
	         {
				 s=st="";
				 fname= fileNames[k].getName().trim();
				 
	        	//System.out.println(fname);
			    if(fname.contains(".txt")||fname.contains(".sparql") )
			    {
			    	br = new BufferedReader(new FileReader(new File(path+"/"+fname))); 
			    	//System.out.println(fname.split("\\.")[0]);
			    	fname=fname.split("\\.")[0];
			    	while ((st = br.readLine()) != null) 
					  {
			    		if(s.length()==0){
							  s=st;
						  }
						  else
						  {
							  s+=" "+st; 
						  }
					  }
			    	//System.out.println(s);
			    	Queries.add(k,s);
			    	QueriesHM.put(fname, s);
			    }
	         }
			  
			  
			  
			  
		
		 }catch(Exception e)
		 {
			 e.printStackTrace();
		 }finally
		 {
			 try
			 {
				 br.close();
			 }
			 catch(Exception e)
			 {
				 e.printStackTrace();
			 }
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
	        
	       
	       // testqueryFilePath=prop.getProperty("testqueryFilePath");
	        //queryFilePath=testqueryFilePath;
	        queryInputFilePath=prop.getProperty("queryInputFilePath");
	        repositoryName= prop.getProperty("repositoryName").trim() ;
	        sparqlEndPoint=prop.getProperty("sparqlEndPoint");
	        queryOutputFilePath =prop.getProperty("queryOutputFilePath");
	      //  queryFileExtension = prop.getProperty("queryFileExtension");
	        //queryFilePath=prop.getProperty("queryFilePath");
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

       
	}
	
	/*
	 * Status request.
	 */
	private  JettyResponseListener getStatus(RemoteRepositoryManager repo)
			throws Exception {

		ConnectOptions opts = new ConnectOptions(sparqlEndPoint + "/status");
		opts.method = "GET";
		//System.out.println("get status "+opts);
		return repo.doConnect(opts);

	}
	
	/*
	 * Get namespace properties.
	 */
	private  JettyResponseListener getNamespaceProperties(
			RemoteRepositoryManager repo, String namespace) throws Exception {

		ConnectOptions opts = new ConnectOptions(sparqlEndPoint + "/namespace/"+ namespace + "/properties");
		opts.method = "GET";
	//	System.out.println("get namespaceProperties");
		return repo.doConnect(opts);

	}
	
	private void writeToFile(ArrayList<String>queryResult ,String queryName)
	{
		try {
		
			
			String path= queryOutputFilePath+File.separator+"Output_"+queryName+".txt";
			//System.out.println("src/main/resources/q"+queryNum);
			fw = new FileWriter(path);
			for(int i=0;i<queryResult.size();i++)
			{
				
				fw.write(queryResult.get(i));
				fw.write("\n");
			}			
			//bw = new BufferedWriter(fw);
			//bw.write(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}finally
		{
			try {
				fw.flush();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}