package com.Blazegraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;

public class ConversionToTTL {

	private String InputFilePath="";
	private String TTLFolderPath="";
	private String TripleInputforConversion="";
	public static void main(String[] args) throws MalformedURLException {
		// TODO Auto-generated method stub
		new ConversionToTTL().convert();
	}
	
	void convert() throws MalformedURLException
	{
		readConfigFile();
		// open our input document
		//java.net.URL documentUrl = new URL("http://swat.cse.lehigh.edu/onto/univ-bench.owl");
		//InputStream inputStream = documentUrl.openStream();
		
		InputStream inputStream;
		try {
			
			File folder = new File(InputFilePath);
			File[] fileNames = folder.listFiles();
			String fname;
			//System.out.println(InputFilePath);
			for(int i=0;i<fileNames.length;i++)
			{
				fname= fileNames[i].getName();
				 System.out.println("converting:"+  fname);
				inputStream = new FileInputStream(InputFilePath+"/"+fname);
				// create a parser for Turtle and a writer for RDF/XML 
				
				 RDFFormat f = null;
				 if(TripleInputforConversion.toUpperCase().contains("n3".toUpperCase()))
				  {
					  f= RDFFormat.N3;
				  }
				  else if (TripleInputforConversion.toUpperCase().equalsIgnoreCase("owl".toUpperCase()))
				  {
					  f= RDFFormat.RDFXML;
				  }
				  else if(TripleInputforConversion.toUpperCase().equalsIgnoreCase("nt".toUpperCase()))
				  {
					  f= RDFFormat.NTRIPLES;
				  }
				RDFParser rdfParser = Rio.createParser(f);
				fname=fname.split("\\.")[0];
				RDFWriter rdfWriter = Rio.createWriter(RDFFormat.TURTLE,   new FileOutputStream(TTLFolderPath+"/"+fname+".ttl"));

				// link our parser to our writer...
				rdfParser.setRDFHandler(rdfWriter);

				// ...and start the conversion!
				try {
				   rdfParser.parse(inputStream, "");
				  
				}
				catch (IOException e) {
				  // handle IO problems (e.g. the file could not be read)
				}
				catch (RDFParseException e) {
				  // handle unrecoverable parse error
				}
				catch (RDFHandlerException e) {
				  // handle a problem encountered by the RDFHandler
				}

			}
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
	        
	        InputFilePath=prop.getProperty("InputFilePath");
	       // tripleFileName=prop.getProperty("tripleFileName");
	        TTLFolderPath=prop.getProperty("TTLFolderPath").trim();
	        TripleInputforConversion=prop.getProperty("TripleInputforConversion").trim();
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

       
	}
	

}
