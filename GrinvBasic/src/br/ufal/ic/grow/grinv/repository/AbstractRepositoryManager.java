package br.ufal.ic.grow.grinv.repository;



import java.net.URI;
import java.util.List;

import br.ufal.ic.grow.grinv.requests.Request;
import br.ufal.ic.grow.grinv.service.Service;


/**
 * Class that represents an abstract Repository Manager, repository extensions should extends this class 
 * 
 * @author Heitor Barros
 * @version 0.1
 * 
 */
public abstract class AbstractRepositoryManager {

	private static final String USER_PATH = "user.home";
	protected String path = System.getProperty(USER_PATH)+"/Grinv/repository/";
	private String location = "SR";
	

	public AbstractRepositoryManager() {
		super();
	}

	public abstract List<Service> loadAllServices();

	public abstract Object loadService(URI service);

	public abstract void addService(URI service);

	public abstract List<Service> loadServices(Request requisition);
	
	public abstract void remove(URI service);

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		if (location.startsWith("./")) {
			this.location = location.substring(2);
		}else{
			this.location = location;
		}
	}
	
	public String getFilePath(){
		return this.path+this.location;
	}
	
	
}