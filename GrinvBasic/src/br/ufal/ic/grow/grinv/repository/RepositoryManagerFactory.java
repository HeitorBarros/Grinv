package br.ufal.ic.grow.grinv.repository;

import br.ufal.ic.grow.grinv.configuration.SystemConfiguration;

/**
* @author Heitor Barros
* @version 0.1
* 
* Factory responsible to instantiate the Repository Manager found in the configuration file
* created by the Grinv Manager
* 
*/
public class RepositoryManagerFactory {

	public static AbstractRepositoryManager createRepositoryManager(){
		return getInstance();
	}

	private static AbstractRepositoryManager repository;
	
	private static AbstractRepositoryManager getInstance(){
		if (repository==null) {
			repository = SystemConfiguration.getRepository();
		}
		return repository;
	
	}
	
	////////////////////////////////
	
	private static AbstractRepositoryManager suoRepository;
	
	public static AbstractRepositoryManager createSUORepositoryManager(){
		return getSUOInstance();
	}
	
	private static AbstractRepositoryManager getSUOInstance() {
		if (suoRepository==null) {
			suoRepository = SystemConfiguration.getSUORepository();
		}
		return suoRepository;
	}
	
}
