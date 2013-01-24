package br.ufal.ic.grow.grinv.repository;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import br.ufal.ic.grow.grinv.requests.Request;
import br.ufal.ic.grow.grinv.service.Service;
import br.ufal.ic.grow.grinv.service.OWLSService;


/**
 * Class that extends the AbstractRepositoryManager, it implements a simple Repository Manager
 * @author heitor
 *
 */
public class DefaultRepositoryManager extends AbstractRepositoryManager {

	public DefaultRepositoryManager() {
		super();
	}
	private static final String USER_PATH = "user.home";
	private static final String REPOSITORY_PATH = "/Grinv/repository/services";
	
	/**
	 * Method thats loads the file that contains the service list
	 * @return
	 * @throws IOException
	 */
	private List<Service> loadServicesFile() throws IOException{
		List<Service> services = new ArrayList<Service>();
		String home = System.getProperty(USER_PATH);
		String path = home+REPOSITORY_PATH;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		String s = "";
		while ((s=reader.readLine())!=null) {
			if ((s.isEmpty())||(s.startsWith("#"))) {
				break;
			}else{
				services.add(new OWLSService(URI.create(s)));
			}
			
		}
		return services;
		
	}
	
	@Override
	public void
	addService(URI service) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Service> loadAllServices() {
		try {
			return this.loadServicesFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public URI loadService(URI service) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Service> loadServices(Request requisition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(URI service) {
		// TODO Auto-generated method stub
		
	}
}