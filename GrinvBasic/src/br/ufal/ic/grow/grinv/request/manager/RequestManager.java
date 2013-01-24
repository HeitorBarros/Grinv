package br.ufal.ic.grow.grinv.request.manager;




import br.ufal.ic.grow.grinv.discovery.DiscoveryController;
import br.ufal.ic.grow.grinv.exception.ServiceInvocationException;
import br.ufal.ic.grow.grinv.repository.GrinvOntManager;
import br.ufal.ic.grow.grinv.requests.DaIRequest;
import br.ufal.ic.grow.grinv.requests.DiscoveryRequest;
import br.ufal.ic.grow.grinv.requests.DiscoveryResponse;
import br.ufal.ic.grow.grinv.requests.InvocationRequest;
import br.ufal.ic.grow.grinv.requests.InvocationResponse;
import br.ufal.ic.grow.grinv.requests.Request;
import br.ufal.ic.grow.grinv.requests.Response;
import br.ufal.ic.grow.grinv.service.Service;
import br.ufal.ic.grow.grinv.service.OWLSService;
import br.ufal.ic.grow.grinv.service.parameters.Input;
import br.ufal.ic.grow.grinv.service.parameters.Parameter;
import br.ufal.ic.grow.grinv.utils.logger.GrinvLogger;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLOntology;

/**
 * Class that manages the requests execution
 * 
 * @author Heitor Barros
 * @version 0.1
 *
 */
public class RequestManager {

	private GrinvLogger logger;
	
	private int id;
	/**
	 * Discovery Controller
	 */
	protected DiscoveryController discoveryController;
	
	/**
	 * Constructor
	 */
	public RequestManager() {
		super();
		this.discoveryController = new DiscoveryController();
	}


	public DiscoveryResponse manageRequest(DiscoveryRequest req){
		if (!(req.getId()>0)) {
			req.setId(this.generateID());
		}
		
		
		GrinvLogger.info(req.getId(),"Attending Request: \n\n" +req.getCompleteDescription());
		
		Service s = discoveryController.discover(req.getId(), req.getServiceDescriptions());

		return new DiscoveryResponse(s);
		
	}
	
	public InvocationResponse manageRequest(DaIRequest req){
		if (!(req.getId()>0)) {
			req.setId(this.generateID());
		}
		
		
		GrinvLogger.info(req.getId(),"Attending Request: \n\n" +req.getCompleteDescription());
		
		
		
		
		Service s = discoveryController.discover(req.getId(), req.getServiceDescriptions());

		Map<Parameter, OWLIndividual> parameters = null;
		try {
			parameters = this.createParameters(s, req.getInputs());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		List<OWLIndividual> result = new ArrayList<OWLIndividual>();
		try {
			result.addAll(s.invoke(req.getId(), parameters).values());
		} catch (ServiceInvocationException e) {
			FaultToleranceMechanism ft = new FaultToleranceMechanism();
			return ft.recoverFromFault(id, s, req);
		}
		
		
		return new InvocationResponse(result);
	}
	
	public InvocationResponse manageRequest(InvocationRequest req){
		if (!(req.getId()>0)) {
			req.setId(this.generateID());
		}
		
		GrinvLogger.info(req.getId(),"Attending Request: \n\n" +req.getCompleteDescription());
		
		
		Map<Parameter, OWLIndividual> parameters = null;
		try {
			parameters = this.createParameters(req.getService(), req.getInputs());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		List<OWLIndividual> result = new ArrayList<OWLIndividual>();
		try {
			result.addAll(req.getService().invoke(req.getId(),parameters).values());
		} catch (ServiceInvocationException e) {
			FaultToleranceMechanism ft = new FaultToleranceMechanism();
			
			ft.recoverFromFault(req.getId(), req);
		}
		
		
		return new InvocationResponse(result);
		
		
	}
	
	
	private Map<Parameter, OWLIndividual> createParameters(Service s, List<URI> inputs) throws IOException {
		Map<Parameter, OWLIndividual> mapOfParameters = new HashMap<Parameter, OWLIndividual>();
		Parameter p = new Parameter();
		
		for (URI uri : inputs) {
			GrinvOntManager om = new GrinvOntManager();
			
			OWLKnowledgeBase kb = om.getOntology(uri);
			OWLOntology owl = kb.getOntology(uri);
			if ( owl == null) {
				owl = kb.read(uri);
			}
			
			OWLIndividual individual = owl.getIndividual(uri);
			for (Input in : s.getInputs()) {
				if (in.getType().toString().equals(individual.getType().toString())) {
					p = new Parameter(s, in.getId());
					
					mapOfParameters.put(p, individual);
				}
			}
		}
		
		return mapOfParameters;
	}
	
	private int generateID(){
		//generating a ID for requisition
		Long id = System.currentTimeMillis();
		return id.intValue();
		
		
	}
	

	public static void main(String[] args) throws URISyntaxException {
		RequestManager rm = new RequestManager();
		DiscoveryRequest req = new DiscoveryRequest();
		Map<String, List<URI>> desc = new HashMap<String, List<URI>>();
		
		List<URI> l = new ArrayList<URI>();
		List<URI> l2 = new ArrayList<URI>();
		l.add(new URI("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Problem"));
		desc.put("inputs", l);
		l2.add(new URI("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Explanation"));
		desc.put("outputs", l2);
		
		List<URI> InParents = new ArrayList<URI>();
		InParents.addAll(l);
		InParents.add(new URI("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Resource"));
		//InParents.add(new URI("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Education"));
		List<URI> OutParents = new ArrayList<URI>();
		OutParents.addAll(l);
		OutParents.add(new URI("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Resource"));
		//OutParents.add(new URI("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Education"));
		
		desc.put("inputParents", InParents);
		desc.put("outputParents", OutParents);
		
		//req.setId(14);
		req.setServiceDescriptions(desc);
		Service s = rm.manageRequest(req).getService();
		
		System.out.println(s.getURI());
		
		DaIRequest dai = new DaIRequest();
		
		dai.setServiceDescriptions(desc);
		List<URI> inputs = new ArrayList<URI>();
		inputs.add(new URI("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Problem_2"));
		dai.setInputs(inputs);
		//dai.setId(15);
		
		InvocationResponse inv = rm.manageRequest(dai);
	
		System.out.println(((OWLIndividual)inv.getResults().get(0)).toRDF(true, true));
//		
//		
//		InvocationRequest ir = new InvocationRequest();
//		
//		ir.setInputs(inputs);
//		ir.setService(s);
//		
//		inv = rm.manageRequest(ir);
//		System.out.println(((OWLIndividual)inv.getResults().get(0)).toRDF(true, true));
		
			
	}	
	
}