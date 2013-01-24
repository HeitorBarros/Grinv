package br.ufal.ic.grow.grinv.repository;

import java.util.List;

import br.ufal.ic.grow.grinv.service.Service;




/**
 * Facade that receives request to services repository content
 * @author Heitor Barros
 * @version 0.1
 *
 */
public class RepositoryFacade {

	private AbstractRepositoryManager repositoryManager;

	public RepositoryFacade() {
		super();
		this.repositoryManager = RepositoryManagerFactory.createRepositoryManager();
	}
	
	public List<Service> loadServices(){
		return repositoryManager.loadAllServices();
	}
	
	
	
	
	
}
