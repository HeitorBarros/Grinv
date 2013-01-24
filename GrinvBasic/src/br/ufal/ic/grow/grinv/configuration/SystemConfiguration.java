package br.ufal.ic.grow.grinv.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufal.ic.grow.grinv.configuration.model.Configuration;
import br.ufal.ic.grow.grinv.configuration.model.DiscoveryAlgorithm;
import br.ufal.ic.grow.grinv.discovery.AbstractMatchmaker;
import br.ufal.ic.grow.grinv.discovery.GrinvMatchmaker;
import br.ufal.ic.grow.grinv.invocation.InvocationEngine;
import br.ufal.ic.grow.grinv.invocation.OWLSInvocationEngine;
import br.ufal.ic.grow.grinv.repository.AbstractRepositoryManager;
import br.ufal.ic.grow.grinv.repository.DefaultRepositoryManager;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @author Heitor Barros
 * @version 0.1
 * 
 * Class that loads the System Configuration 
 * 
 */
public class SystemConfiguration {

	/**
	 * User Path key
	 */
	private static final String USER_PATH = "user.home";
	/**
	 * Path to configuration file folder
	 */
	private static final String CONFIGURATION_PATH = "/Grinv/";
	/**
	 * File name
	 */
	private static final String FILE_NAME = "Configuration.xml";

//	/**
//	 * Method that loads the configuration file and creates a map with the relationship <hotspot, configuration>
//	 * @param filename
//	 * @return
//	 * @throws IOException
//	 */
//	public static Map<String, String> getFileConfiguration(String filename) throws IOException{
//		
//		//Configurations Map
//		Map<String, String> configuration = new HashMap<String, String>();
//		//loading user home path
//		String home = System.getProperty(USER_PATH);
//		//creating file name
//		String path = home+CONFIGURATION_PATH+filename;
//		
//		BufferedReader reader = new BufferedReader(new FileReader(path));
//
//		String s = "";
//		String key, value;
//		
//		//reading configuration file
//		while ((s=reader.readLine())!=null) {
//			//verify if its a null line or a comment
//			if ((s.isEmpty())||(s.startsWith("#"))) {
//				break;
//			}else{
//				//break string in key hotspot and configuration
//				StringTokenizer tokens = new StringTokenizer(s,"=");
//				key = tokens.nextToken();
//				value = tokens.nextToken();
//				//put configuration in map
//				configuration.put(key, value);
//			}
//			
//		}
//
//		//returning map with configurations
//		return configuration;
//	}
	
	/**
	 * Method that reads the configuration file and creates a Configuration Class to be returned.
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Configuration getConfiguration() throws FileNotFoundException{
		//creates a xstream object
		XStream xstream = new XStream(new DomDriver());
		//reading a configuration Class
		xstream.processAnnotations(Configuration.class);
		
		Configuration conf =null;
		
		//reading the configuration file and parsing it to a configuration object
		conf = (Configuration)xstream.fromXML(new FileInputStream(System.getProperty(USER_PATH)+CONFIGURATION_PATH+FILE_NAME));

		return conf;
	}

	/**
	 * Method that recover the parameters useds by Discovery Algorithm
	 * @return
	 */
	public static List<String> getAlgorithmParameters(){
		
		List<String> param = new ArrayList<String>();
		
		
		Configuration conf;
		try {
			conf = SystemConfiguration.getConfiguration();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
		for (String parameter : conf.getDiscovery().getAlgorithms().get(0).getParameters()) {
			param.add(parameter);
		}
		
		return param;
	}
	
	
	/**
	 * Method that recover the discovery algorithm in the configuration file
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<Integer, AbstractMatchmaker> getMatchmaker(){
		
		Configuration conf;
		try {
			conf = SystemConfiguration.getConfiguration();
		} catch (FileNotFoundException e1) {
			//if Configuration file is not found
			// returns a default matchmaker
			Map m = new HashMap();
			m.put(0,new GrinvMatchmaker());
			return m;
		}
		
		List<DiscoveryAlgorithm> algorithms = conf.getDiscovery().getAlgorithms();
		
		
		Map<Integer, AbstractMatchmaker> m = new HashMap();
		if (algorithms == null || algorithms.size()==0) {
			//if discovery is not configured, returns a default matchmaker
			m.put(0, new GrinvMatchmaker());
			return m;
		}
		
		for (DiscoveryAlgorithm discoveryAlgorithm : algorithms) {
			try {
				Class c = Class.forName(discoveryAlgorithm.getDiscovery_class());
				m.put(discoveryAlgorithm.getPriority(), (AbstractMatchmaker) c.newInstance());
			} catch (Exception e) {
				//if class could not be instantiated, returns a default matchmaker
				System.err.println(e.getMessage());
				if (m.size()==0) {
					m.put(0, new GrinvMatchmaker());
					return m;
				}else{
					return m;
				}
				
			} 
		}
		return m;
	}
	
	/**
	 * Method that recover the analysis strategy in the configuration file
	 * @return
	 */
//	@SuppressWarnings("unchecked")
//	public static AbstractAnalysisStrategy getAnalysisStrategy() {
//		Configuration conf;
//		try {
//			conf = SystemConfiguration.getConfiguration();
//		} catch (FileNotFoundException e1) {
//			//if Configuration file is not found
//			// returns a default analysis strategy
//			return new DefaultAnalysisStrategy();
//		}
//		
//		String classPath = conf.getAnalysis().getName();
//		if (classPath == null) {
//			//if analysis is not configured, returns a default analysis strategy
//			return new DefaultAnalysisStrategy();
//		}
//		
//		
//		try {
//			Class c = Class.forName(classPath);
//			return (AbstractAnalysisStrategy) c.newInstance();
//		} catch (Exception e) {
//			//if class could not be instantiated, returns a default analysis strategy
//			System.err.println(e.getMessage());
//			return new DefaultAnalysisStrategy();
//		} 
//		
//	}
	
	/**
	 * Method that recover the invocation engine in the configuration file
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static InvocationEngine getInvocationEngine(){
		Configuration conf;
		try {
			conf = SystemConfiguration.getConfiguration();
		} catch (FileNotFoundException e1) {
			//if Configuration file is not found
			// returns a default invocation engine
			return new OWLSInvocationEngine();
		}
		
		String classPath = conf.getInvocation().getName();
		if (classPath == null) {
			//if invocation is not configured, returns a default invocation engine
			return new OWLSInvocationEngine();
		}
		
		try {
			Class c = Class.forName(classPath);
			return (InvocationEngine) c.newInstance();
		} catch (Exception e) {
			//if class could not be instantiated, returns a default invocation engine
			System.err.println(e.getMessage());
			return new OWLSInvocationEngine();
		}
	}
	
	/**
	 * Method that recover the services repository in the configuration file
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static AbstractRepositoryManager getRepository(){
		Configuration conf;

		try {
			conf = SystemConfiguration.getConfiguration();
		} catch (FileNotFoundException e1) {
			//if Configuration file is not found
			// returns a default repository manager
			return new DefaultRepositoryManager();
		}
		
		String classPath = conf.getRepository().getName();
		if (classPath == null) {
			//if repository is not configured, returns a default repository manager
			return new DefaultRepositoryManager();
		}
		
		try {
			Class c = Class.forName(classPath);
			AbstractRepositoryManager rm = (AbstractRepositoryManager) c.newInstance();
			rm.setLocation(conf.getRepository().getLocation());
			return rm;
		} catch (Exception e) {
			//if class could not be instantiated, returns a default repository manager
			System.err.println(e.getMessage());
			return new DefaultRepositoryManager();
		}
	}
	
	/**
	 * Method that recover the services repository in the configuration file
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static AbstractRepositoryManager getSUORepository(){
		Configuration conf;

		try {
			conf = SystemConfiguration.getConfiguration();
		} catch (FileNotFoundException e1) {
			//if Configuration file is not found
			// returns a default repository manager
			return new DefaultRepositoryManager();
		}
		
		String classPath = conf.getSuoRepository().getName();
		if (classPath == null) {
			//if repository is not configured, returns a default repository manager
			return new DefaultRepositoryManager();
		}
		
		try {
			Class c = Class.forName(classPath);
			AbstractRepositoryManager rm = (AbstractRepositoryManager) c.newInstance();
			rm.setLocation(conf.getSuoRepository().getLocation());
			return rm;
		} catch (Exception e) {
			//if class could not be instantiated, returns a default repository manager
			System.err.println(e.getMessage());
			return new DefaultRepositoryManager();
		}
	}
	
	
}
