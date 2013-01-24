package br.ufal.ic.grow.grinv.repository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLOntology;

import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Output;
//import org.mindswap.owls.service.Service;

import br.ufal.ic.grow.grinv.requests.Request;
import br.ufal.ic.grow.grinv.service.Service;
import br.ufal.ic.grow.grinv.service.ServiceList;
import br.ufal.ic.grow.grinv.service.OWLSService;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;




/**
 * Class that implements a repository manager
 * 
 * @author Heitor Barros
 * @version 0.1
 *
 */
public class GrinvRepository extends AbstractRepositoryManager {

	private static final String USER_PATH = "user.home";
	private static final String REPOSITORY_PATH = "/Grinv/repository/"; 
	private static final String DEFAULT_REPOSITORY = "servicesList";
	private static final String ONTOLOGIES_REPOSITORY_PATH = "/GrInv/repository/onto/";
	/**
	 * Method that adds a service in the repository, extracting the relevant informations
	 */
	@Override
	public void addService(URI service) {
		//creating a grinv service
	    OWLSService aService = new OWLSService(); 
		//service inputs list
		List<br.ufal.ic.grow.grinv.service.parameters.Input> inputs = new ArrayList<br.ufal.ic.grow.grinv.service.parameters.Input>();
		//List of parents of the services inputs
		List<URI> parentEntitiesOfInputs = new ArrayList<URI>();
		//service outputs list
		List<br.ufal.ic.grow.grinv.service.parameters.Output> outputs = new ArrayList<br.ufal.ic.grow.grinv.service.parameters.Output>();
		//List of parents of the services outputs
		List<URI> parentEntitiesOfOutputs = new ArrayList<URI>();		
		//creating kb
		GrinvOntManager ontManager = new GrinvOntManager();
		OWLKnowledgeBase kb;
		try {
			kb = ontManager.getKB();
		} catch (IOException e1) {
			kb = OWLFactory.createKB();
		}
		
		//reading the service
		org.mindswap.owls.service.Service owlsService;
		try {
			owlsService = kb.readService(service);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		//reading inputs
		for (Input input : owlsService.getProfile().getInputs()) {
			URI uri = input.getParamType().getURI();
			//adding URI into inputs vector and parent entities vector
			String id = input.getURI().toString();
			
			inputs.add(new br.ufal.ic.grow.grinv.service.parameters.Input(id, uri));
			parentEntitiesOfInputs.add(uri);
			try {
				kb.read(uri);
				OWLOntology onto = kb.getOntology(URI.create(uri.toString().split("#")[0]));
				
				
				kb.setReasoner("Pellet");
				if (!kb.isConsistent()) {
					System.out.println("ONTOLOGIA INCONSISTENTE!");
					return;
				}
				//adding entities at entities vector
				for (OWLClass clazz : kb.getSuperClasses(onto.getClass(uri), true)) {
					parentEntitiesOfInputs.add(clazz.getURI());
				}
				
				
			} catch (IOException e) {
				System.out.println(e.getMessage());
				return;
			}
		}
		
		//reading outputs
		for (Output output : owlsService.getProfile().getOutputs()) {
			URI uri = output.getParamType().getURI();
			//adding URI into outputs vector and parent entities vector
			String id = output.getURI().toString();
			
			URI type = output.getParamType().getURI();
			outputs.add(new br.ufal.ic.grow.grinv.service.parameters.Output(id,uri));
			parentEntitiesOfOutputs.add(uri);
			
			try {
				kb.read(uri);
				OWLOntology onto = kb.getOntology(URI.create(uri.toString().split("#")[0]));
				if (!kb.isConsistent()) {
					System.out.println("ONTOLOGIA INCONSISTENTE!");
					return;
				}
				kb.setReasoner("Pellet");
				//adding entities at entities vector
				for (OWLClass clazz : kb.getSuperClasses(onto.getClass(uri), true)) {
					parentEntitiesOfOutputs.add(clazz.getURI());
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}		
		
		//adding vector to Grinv Service
		aService.setInputs(inputs);
		aService.setOutputs(outputs);
		aService.setURI(service);
		aService.setInputsClasses(parentEntitiesOfInputs);
		aService.setOutputsClasses(parentEntitiesOfOutputs);
		
		//adding new service to service list
		this.addInFile(aService);
		
		//adding the ontologies to the repository
//		try {
//			ontManager.addOntology(service);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		

	}
	
	private void addOntology(OWLKnowledgeBase kb) {
		Set<OWLOntology> setOnto = kb.getOntologies(true);
		System.out.println(setOnto.size());
		String file_path = System.getProperty(USER_PATH)+ONTOLOGIES_REPOSITORY_PATH;
		for (OWLOntology onto : setOnto) {
			System.out.println(onto.getURI());
			String tk = onto.getURI().getPath();
			if (onto.getURI()!=null && tk!=null) {
				String[] t  = tk.split("/");
				tk = t[t.length-1];
			}
			Writer wt = null;
			try {
				wt = new FileWriter(file_path+tk);
				onto.getWriter().write(wt, onto.getURI());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
	}

	/**
	 * Method that adds a service in repository file
	 * @param aService
	 */
	private void addInFile(OWLSService aService){
		//String file_path = System.getProperty(USER_PATH)+REPOSITORY_PATH;
		String file_path = super.getFilePath();
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(ServiceList.class);
		
		ServiceList servicesList = null;
		try {
			servicesList = (ServiceList)xstream.fromXML(new FileInputStream(file_path));
		} catch (Exception e1) {
			servicesList = new ServiceList();
			servicesList.setServices(new ArrayList<Service>());
		}
		if (servicesList.getServices()==null) {
			servicesList.setServices(new ArrayList<Service>());
		}
		if (servicesList.getServices().contains(aService)) {
			System.out.println("Serviço já está no repositório");
			return;
		}
		List<Service> lsAux = servicesList.getServices();
		lsAux.add(aService);
		
		if (lsAux.isEmpty()) {
			servicesList = new ServiceList();
		}else{
			servicesList.setServices(lsAux);
		}
		
		
		try {
			xstream.toXML(servicesList, new FileWriter(file_path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



	@Override
	public List<Service> loadAllServices() {
		System.out.println("Loading Services");
		String file_path = super.getFilePath();
		
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(ServiceList.class);
		
		ServiceList sl = null;
		try {
			sl = (ServiceList)xstream.fromXML(new FileInputStream(file_path));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return sl.getServices();
	}

	@Override
	public Object loadService(URI service) {
		List<Service> sl = this.loadAllServices();
		for (Service service2 : sl) {
			if (service2.getURI().equals(service)) {
				return service2;
			}
		}
		
		return null;
	}

	@Override
	public List<Service> loadServices(Request requisition) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void remove(URI service){
		//String file_path = System.getProperty(USER_PATH)+REPOSITORY_PATH;
		String file_path = super.getFilePath();
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(ServiceList.class);
		
		ServiceList servicesList = null;
		try {
			servicesList = (ServiceList)xstream.fromXML(new FileInputStream(file_path));
		} catch (Exception e1) {
			servicesList = new ServiceList();
			servicesList.setServices(new ArrayList<Service>());
		}
		
		List<Service> lsAux = servicesList.getServices();
		Service toBeRemoved = null;
		for (Service s : lsAux) {
			if (s.getURI().equals(service)) {
				toBeRemoved = s;
			}
		}
		if (toBeRemoved!=null) {
			lsAux.remove(toBeRemoved);
		}
		
		
		servicesList.setServices(lsAux);
		
		try {
			xstream.toXML(servicesList, new FileWriter(file_path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String[] args) {
		GrinvRepository r = new GrinvRepository();
		
//		r.addService(URI.create("http://localhost:8080/LearningServices/owls/consultarRelatorio.owl"));
		r.addService(URI.create("http://localhost:8080/LearningServices/owls/getConcept.owl"));
		r.addService(URI.create("http://localhost:8080/LearningServices/owls/getExplanation.owl"));
		
		for (Service s : r.loadAllServices()) {
			System.out.println("\nService: "+ s.getURI());
			for (br.ufal.ic.grow.grinv.service.parameters.Input ins : s.getInputs()) {
				System.out.println("Input: "+ins.getType());
				System.out.println("id: "+ins.getId());
			}
			for (br.ufal.ic.grow.grinv.service.parameters.Output outs : s.getOutputs()) {
				System.out.println("Output: "+outs.getType());
				System.out.println("id: "+outs.getId());
			}
			for (URI uri : ((OWLSService)s).getOutputsClasses()) {
				System.out.println("outputs classes: "+uri);
				
			}
		}
	}
	

}
