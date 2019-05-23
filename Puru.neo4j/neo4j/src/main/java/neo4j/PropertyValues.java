package neo4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyValues {
	static String configFilePath = "src/main/java/neo4j/config.properties";
	public static String getProperty(String key) {
		
		Properties prop = new Properties();
		String valueFromConfig = "";
		try {
			FileInputStream ip = new FileInputStream(configFilePath);
			prop.load(ip);
//			System.out.println((prop.getProperty(key)));
			valueFromConfig = prop.getProperty(key);
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found, please check the path");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Input output exception");
			e.printStackTrace();
		}

		return valueFromConfig;
	}


}
