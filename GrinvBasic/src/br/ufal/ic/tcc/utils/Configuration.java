package br.ufal.ic.tcc.utils;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * 
 * @author <a href="mailto:marlos.tacio@gmail.com">marlos</a>
 * 
 */

public class Configuration {

	// Attributes ----------------------------------------------------

	private static Configuration conf;

	private Properties properties;
	
	// Static --------------------------------------------------------
	
	private static Logger logger;

	static {
		logger = Logger.getLogger(Configuration.class.getName());
	}

	// Constructor ---------------------------------------------------

	/**
	 * 
	 */
	private Configuration() {
		this.properties = new Properties();

		try {
			this.properties.load(new FileInputStream("services.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Public --------------------------------------------------------

	
	/**
	 * 
	 * @return
	 */
	public static Configuration getInstance() {
		return conf == null ? conf = new Configuration() : conf;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getProperty(final String key) {
		return this.properties.containsKey(key) ? this.properties
				.getProperty(key) : "";
	}

	/**
	 * 
	 * @return
	 */
	public Vector<String> getProperties() {
		Vector<String> vector = new Vector<String>();

		for (Object key : this.properties.keySet())
			vector.add(this.properties.getProperty(key.toString()));
		return vector;
	}
}
