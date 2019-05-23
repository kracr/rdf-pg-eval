package ankita.JanusGraphBench;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.util.GraphFactory;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphTransaction;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.datastax.sparql.gremlin.SparqlToGremlinCompiler;

public class IPJanusgraph {
	
   	private static final Logger LOGGER = LoggerFactory.getLogger(IPJanusgraph.class);
	JanusGraph g1;
	int no_of_inputFile;
	String inputFileName[];
	String outputFile;
	String queryFile;
	String mappingFile;
	String logFile;
	PropertiesConfiguration config;
	FileWriter outStream;
	// This function takes inputFile names containing triples and mapping file containing prefixes for the benchmark 
	// and creates a JanusGraph. It returns the total time taken to load a graph
	
	IPJanusgraph(String config_file) throws IOException
	{
		InputStream input = null;
    	try {
			input = new FileInputStream(config_file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
    	
		Properties prop = new Properties();
        try {
			prop.load(input);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	
        try {
			config=new PropertiesConfiguration(prop.getProperty("cassandra"));
		 	
		} catch (ConfigurationException e1) {
			e1.printStackTrace();
		}
        no_of_inputFile=Integer.parseInt(prop.getProperty("no_of_inputFile"));
        inputFileName=new String[no_of_inputFile];
        for (int i=0;i<no_of_inputFile;i++)
        	inputFileName[i]=prop.getProperty("inputFile"+(i+1));
        
        mappingFile =prop.getProperty("mappingFile");	
        queryFile =prop.getProperty("queryFile");
        outputFile = prop.getProperty("outputFile");
        logFile=prop.getProperty("logFile");
        outStream=new FileWriter(outputFile);
     }
	
     // load the graph using inputFileName and mapping file	
	 //long loadGraph(String inputFileName[], String mappingFile) throws IOException
	long loadGraph() throws IOException
	 {
    	 long startTime = System.currentTimeMillis();
		 InputStream mapping= new FileInputStream(mappingFile);
	     BufferedReader reader = new BufferedReader(new InputStreamReader(mapping));
		 String line;
		 ArrayList<String> url=new ArrayList<String>();
		 ArrayList<String> sname=new ArrayList<String>();
		      
		// reading mapping file and storing prefixes in sname and corresponding url into url 
		       
		while (null != (line = reader.readLine())) {
		   String temp[]=line.split("=");
		   sname.add(temp[0]);
		   url.add(temp[1]);
		}
	    reader.close();
	
		g1=(JanusGraph) GraphFactory.open(config);// create a empty JanusGraph
		Vertex v11=null, v12 = null;
		JanusGraphTransaction tx = g1.newTransaction();
		ArrayList<String> vertex_name=new ArrayList<String>();
		ArrayList<Vertex> vertex=new ArrayList<Vertex>();
		int total_records=0;
		//processing each input file one-by-one
		for (int l=0;l<inputFileName.length;l++)
		{
			Model m = ModelFactory.createDefaultModel() ;
			RDFDataMgr.read(m, inputFileName[l]);// create a model using input triple file
	    	
			StmtIterator iter = m.listStatements();// get a list of all triple from file
			int count=0;
			
			// Reading each statement 
			while (iter.hasNext()) {
			   count++;
			   Statement stmt      = iter.nextStatement();  // get next statement
			   Resource  subject   = stmt.getSubject();     // get the subject
			   RDFNode   object    = stmt.getObject();      // get the object
			   Property  predicate = stmt.getPredicate();   // get the predicate
							   
			   String v1=subject.toString();
			   String v2=object.toString();
			   //outStream.write(v1+"\t"+predicate.toString()+"\t"+v2+"\n");
	    	    
			   String temp1,temp2;
			   int i;
			   String ed=predicate.toString();
			   if (ed.indexOf('#')>0)
			   {   
				   temp1=ed.substring(0,ed.lastIndexOf("#")+1);
				   i=url.indexOf(temp1);
				   temp2=sname.get(i)+"_";
				   ed=temp2+ed.substring(ed.lastIndexOf("#")+1);
				   //System.out.println("Edge# "+ed);
			    }
				else
				{
				   temp1=ed.substring(0,ed.lastIndexOf("/")+1);
				   //System.out.println(temp1);
				   i=url.indexOf(temp1);
				   temp2=sname.get(i)+"_";
				   ed=temp2+ed.substring(ed.lastIndexOf("/")+1);
				   //System.out.println("Edge/ "+ed);
				}  
				if (object instanceof Resource) {
	    		    // predicate is an edge, add subject as vertex, object as vertex and create an edge b/w them
				   if (!vertex_name.contains(v1)) 
				   {
					   vertex_name.add(v1);
					   
					   v11=g1.addVertex(v1);
					   vertex.add(v11);
					   v11.property("name",v1 );
				       //outStream.write("Vertex added "+v11.label()+"\n");
					}
					else
					{	
					   int i1=vertex_name.indexOf(v1);
					   v11=vertex.get(i1);
					}
					   
					if (!vertex_name.contains(v2)) 
					{
					   vertex_name.add(v2);
					   v12=g1.addVertex(v2);
					   v12.property("name",v2);
					   vertex.add(v12);
					   //outStream.write("Vertex added "+v12.label()+"\n");
				    }
					else
					{	
					   int i1=vertex_name.indexOf(v2);
					   v12=vertex.get(i1);
				    }
					v11.addEdge(ed, v12); // addedge ed
					//outStream.write("Edge added "+ ed+"\n");
				} 
				else // if predicate is  property 
				{
	    			// predicate is a property, add subject as vertex , predicate as property and object as property value 
					if (!vertex_name.contains(v1)) 
					{
					   vertex_name.add(v1);
					   v11=g1.addVertex(v1);
					   vertex.add(v11);
					   v11.property("name",v1);
					   //outStream.write("Vertex added "+v11.label()+"\n");
					}
					else
					{	
					   int i1=vertex_name.indexOf(v1);
					   v11=vertex.get(i1);
					}
					int indx2=v2.indexOf("^^");
					if (indx2>=0) v2=v2.substring(0,indx2);
					v11.property(ed, v2);// set property
					//System.out.println("Vertex "+v11.label()+"  Property "+ed);
					//outStream.write("Property added "+v11.label()+"\t"+ed+"\n");
				}
				//System.out.println("count "+count);
			}
			total_records=total_records+count;
		}
						 
	    tx.commit();
	    long endTime = System.currentTimeMillis();
	 	long time=endTime-startTime;
	 	outStream.write("\nNumber of triples "+total_records+"\n");
	 	outStream.write("\nGraph loading time(ms) "+time+"\n");
	 	return time;
	 }
     // convert input sparql query file into the query file accepted by gremlinator
	 //void convertQuery(String inputFile,String outputFile, String mappingFile) throws IOException
	 void convertQuery(String inputFile,String outputFile) throws IOException
	 {
		   InputStream inputStream = new FileInputStream(inputFile);
		   BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		   InputStream mapping = new FileInputStream(mappingFile);
			  
		   BufferedReader map = new BufferedReader(new InputStreamReader(mapping));
	       String line;
	       ArrayList<String> url=new ArrayList<String>();
	       ArrayList<String> sname=new ArrayList<String>();
	       
	       // reading mapping file 
	       
	       while (null != (line = map.readLine())) {
	    	   String temp[]=line.split("=");
	    	   sname.add(temp[0]);
	    	   url.add(temp[1]);
	       }
		   map.close();	  
		   FileWriter outStream = new FileWriter(outputFile);
		   inputStream = new FileInputStream(inputFile);
		   reader = new BufferedReader(new InputStreamReader(inputStream));
			  	   
		   int c=100;
		   while (null != (line = reader.readLine())) 
		   {
		    	int index= line.indexOf('.');
		 		if (index!=-1)
		 		{
		 		       line = line.trim();
		 		       String temp[]=line.split("\\s");
		 		       String temp1=temp[1].replace(':', '_');
				       if (g1.containsEdgeLabel(temp1))
				       {
				    	   temp[1]="e:"+temp1;
				       }
				       else if (!temp[1].startsWith("?"))
				       {
				    	   temp[1]="v:"+temp1;
				       }
				       int idx1=temp[2].indexOf(':');
				       if (temp[2].charAt(0)=='"') 
				       {
				    	   int id=line.indexOf('"');
				    	   temp[2]=line.substring(id,line.indexOf('"',id+1)+1);
				           
				       }
				       if ((temp[2].charAt(0)!='"') && idx1>0)
			 	       {
				    	   String temp2=temp[2].substring(0,idx1);
				    	  // System.out.println("********"+temp2);
				    	   int i=sname.indexOf(temp2);
						   temp2=url.get(i)+temp[2].substring(idx1+1);
						   temp[2]="\""+temp2+"\"";
				    	}
		 		        if (temp[0].startsWith("\""))
				    	{
				    		   line="\t?v"+c+"\tv:name\t"+temp[0]+" .\n"+"\t?v"+c+"\t"+temp[1]+"\t"+temp[2]+" .";
				    		   c++;
				    	}   
				    	else if (temp[1].startsWith("e") && temp[2].startsWith("\""))
				    	{
				    	   line="\t?v"+c+"\tv:name\t"+temp[2]+" .\n"+"\t"+temp[0]+"\t"+temp[1]+"\t?v"+c+" .";
				    	   c++;
				    	}
				    	else
				    	   line="\t"+temp[0]+"\t"+temp[1]+"\t"+temp[2]+" .";
				      }  
				   	  line=line+"\n";
				      outStream.write(line);
				}
		 		    
		 	
		 	   reader.close();
		 	   outStream.close();
		 	   	
		       System.out.println("***************  DONE ");

			}
	 //void queryGraph(String filename,String outputFile) throws IOException 
	 void queryGraph() throws IOException 
	 {
		 String newQueryFile="conf/newQueryFile.txt";
		 convertQuery(queryFile,newQueryFile);
	   	InputStream inputStream = null;
	   	try 
	   	{
	   		inputStream = new FileInputStream(newQueryFile);
	   	} 
	   	catch (FileNotFoundException e1) 
	   	{
	    	   LOGGER.error("Query File not found");
	    	   System.exit(1);
	    }
	    //outStream = new FileWriter(outputFile);
	        
	    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder queryBuilder = new StringBuilder();
		String line;
		int count=1;
		queryBuilder.delete(0, queryBuilder.capacity());
		while (null!=(line=reader.readLine()))
		{
		   	if (line.startsWith("#mapping")) { queryBuilder.delete(0, queryBuilder.capacity()); continue;}
		    if (line.startsWith("#end"))
		    {
		   		final String queryString = queryBuilder.toString();
		       	outStream.write("Query: "+count+" "+queryString+"\n");
		       	//System.out.println("Query: "+count+" "+queryString+"\n");
		   	 	LOGGER.info("Queries Processed : "+count);
		   	 	long startTime = System.currentTimeMillis();
		   	 	try
		    	{
		    	 		Traversal<Vertex, ?> traversal = SparqlToGremlinCompiler.convertToGremlinTraversal(g1, queryString);
		       	 	    outStream.write(traversal.toString());
		       	 	    
	       	 	  	    try {
	       	 	          
	       	 	  	    	List<?> L1 = traversal.toList();
	       	 	  	    	outStream.write("\n Traversal started "+L1.size()+"\n");
	       	 	  	    	 int c=0;
	       	 	  	    	 Object v1=null;
	       	 	  	    	 Object v2=null;
	       	 	  	    	 for (int i=0;i<L1.size();i++)
	       	 	  	         {
	       	 	  	    		 v1=v2;
	       	 	  	    		 v2 = L1.get(i);
	       	 	  	    		 if (v1==null || !v1.equals(v2))
	       	 	  	    		 {
	       	 	  	    		   outStream.write(v2.toString()+"\n");
	       	 	  	    		   c++;
	       	 	  	    		 }
	       	 	  	         }
	       	 	  	    	 outStream.write("\n Records Selected : "+c+"\n");
	       	 	  	    	
		       	 	    }
		       	 	    catch(java.lang.IllegalStateException e)
		       	 	    {
		       	 	    	outStream.write("\n\n No records Selected : "+e.getMessage()+"\n");
		       	 	    	LOGGER.info("\n No records Selected \n");
		       	 	    }
		       	 	    long endTime = System.currentTimeMillis();
		       	 	    long time=endTime-startTime;
		       	 	    //outStream.write(traversal.toString());
		       	 	    outStream.write("\n\n Time taken(ms) "+time+"\n\n");
	    	       	 	
		       	 	}
		       	 	catch(Exception e)
		       	 	{
		       	 		outStream.write("\nQuery cannot be processed by gremlinator : "+e.getMessage()+"\n\n");
		       	 	}   
		   	 		count++;
		    	 }
		    	 else
		    	    	queryBuilder.append(System.lineSeparator()).append(line);
		       }
		       outStream.close();
		       reader.close();
		      // g1.close();
		       
		}
	    
	   void dropGraph() throws Exception {
	        if (g1 != null) {
	        	g1.close();
	            //JanusGraphFactory.drop(g1);
	        }
	    }

	    void printGraph() throws IOException
	    {
	    	FileWriter outStream = new FileWriter(logFile);
		    
	    	Iterator<Vertex> itr = g1.vertices();
	    	int count_v=1;
	    	while (itr.hasNext())
	    	{
	    		Vertex v=itr.next();
	    		//System.out.println(count_v+"  "+v.label());
	    		outStream.write(count_v+"  "+v.label()+"\t"+v.toString()+"\n");
	    		Set<String> key = v.keys();
	    		Iterator<String> key_it = key.iterator();
	    		while(key_it.hasNext())
	    		{
	    			String temp=key_it.next();
	    			String value=v.value(temp);
	    			//outStream.write("\t"+temp+"\t"+value+"\n");
	    		}
	    		
	    		count_v++;
	    	}
	    	System.out.println("Number of vertices "+count_v);
	    	Iterator<Edge> itr_ed = g1.edges();
	    	int count_e=1;
	    	while (itr_ed.hasNext())
	    	{
	    		Edge ed=itr_ed.next();
	    		//System.out.println(count_e+"  "+ed.inVertex().label()+" "+ed.label()+" "+ed.outVertex().label());
	    		outStream.write(count_e+"  "+ed.outVertex().label()+" "+ed.label()+" "+ed.inVertex().label()+"\n");
	    		count_e++;
	    	}
	    	//System.out.println("Number of vertices "+count_v);
	    	//System.out.println("Number of edges "+count_e);
	    	outStream.close();
	   }

	public static void main(String[] args) throws Exception {
		IPJanusgraph TestGraph=new IPJanusgraph("conf/config_lubm.properties");
		TestGraph.loadGraph();
		TestGraph.printGraph();
		TestGraph.queryGraph();
		TestGraph.dropGraph();
	}
}