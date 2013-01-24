package br.ufal.ic.grow.grinv.discovery;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufal.ic.grow.grinv.repository.GrinvRepository;
import br.ufal.ic.grow.grinv.service.Service;
import br.ufal.ic.grow.grinv.service.parameters.Input;
import br.ufal.ic.grow.grinv.service.parameters.Output;
import br.ufal.ic.grow.grinv.utils.logger.GrinvLogger;

/**
 * Class that extends AbstractMatchmaker and implents a simple matching algorithm
 * 
 * @author Heitor Barros
 * @version 0.1
 *
 */
public class GrinvMatchmaker extends AbstractMatchmaker {

	@Override
	public Service discover(int id, Map<String, List<URI>> selectDesc,
			List<Service> services) {
		String log = "\n- - - - - -\nStarting Discovery Process\n";
		
		List<URI> inputs = selectDesc.get("inputs");
		List<URI> outputs = selectDesc.get("outputs");

		int count = 0;
		//searching service that best matches the user request
		for (Service service : services) {
			
			log += "Comparing with Service: "+(++count)+service.getURI()+"\n";
			//comparing inputs and outputs of the services and the descriptions sent by user
			if (this.compareInputs(service.getInputs(),inputs)&&this.compareOutputs(service.getOutputs(),outputs)) {
				log+="\nService Found: "+ service.getURI()+"\n";
				log+="Inputs:\n";
				for (Input i :service.getInputs()) {
					log+= "- "+i.getType()+"\n";
				}
				
				log+="Outputs:\n";
				for (Output o :service.getOutputs()) {
					log+= "-"+o.getType()+"\n";
				}
				
				GrinvLogger.info(id, log);

				return service;
			}
			
			
		}	
		GrinvLogger.info(id, log);
		return null;
	}
	private boolean compareOutputs(List<Output> outputs, List<URI> p2) {
		List<URI> a, b;
		a= new ArrayList<URI>();
		for (Output i : outputs) {
			a.add(i.getType());
		}
		
		b= new ArrayList<URI>();
		b.addAll(p2);
		
		//comparing sizes
		if (a.size()!= b.size()) {
			return false;
		}
		
		//matching uris
		for (URI uri : b) {
			if (!a.contains(uri)) {
				return false;
			}
			a.remove(uri);
		}
		
		
		return true;
		
	}
	/**
	 * Method that compares two list of URIs
	 * @param list list 1
	 * @param p2 list 2
	 * @return
	 */
	public boolean compareInputs(List<Input> list, List<URI> p2){
		
		List<URI> a, b;
		a= new ArrayList<URI>();
		for (Input i : list) {
			a.add(i.getType());
		}
		
		b= new ArrayList<URI>();
		b.addAll(p2);
		
		//comparing sizes
		if (a.size()!= b.size()) {
			return false;
		}
		
		//matching uris
		for (URI uri : b) {
			if (!a.contains(uri)) {
				return false;
			}
			a.remove(uri);
		}
		
		
		return true;
		
	}
	
	public static void main(String[] args) {
		GrinvMatchmaker disc = new GrinvMatchmaker();
		GrinvRepository r = new GrinvRepository();
		List<URI> outputs = new ArrayList<URI>();
		List<URI> inputs = new ArrayList<URI>();
		Map<String, List<URI>> map = new HashMap<String, List<URI>>();
		
		inputs.add(URI.create("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Problem"));
		outputs.add(URI.create("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Concept"));
		//outputs.add(URI.create("http://127.0.0.1/ontology/books.owl#Book"));
		//outputs.add(URI.create("http://127.0.0.1/ontology/concept.owl#Price"));
		
		map.put("outputs", outputs);
		map.put("inputs", inputs);
		
		System.out.println(disc.discover(14, map, r.loadAllServices()).getURI());
		
	}

}
