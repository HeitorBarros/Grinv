package br.ufal.ic.grow.grinv.test.request.manager;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mindswap.owl.OWLIndividual;

import br.ufal.ic.grow.grinv.request.manager.RequestManager;
import br.ufal.ic.grow.grinv.requests.DaIRequest;
import br.ufal.ic.grow.grinv.requests.DiscoveryRequest;
import br.ufal.ic.grow.grinv.requests.InvocationRequest;
import br.ufal.ic.grow.grinv.requests.InvocationResponse;
import br.ufal.ic.grow.grinv.service.Service;

public class RequestManagerTestCase {

	@Test
	public void testManageRequestDiscoveryRequest() {
		RequestManager rm = new RequestManager();
		DiscoveryRequest req = new DiscoveryRequest();
		Map<String, List<URI>> desc = new HashMap<String, List<URI>>();
		
		List<URI> l = new ArrayList<URI>();
		List<URI> l2 = new ArrayList<URI>();
		l.add(URI.create("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Problem"));
		desc.put("inputs", l);
		l2.add(URI.create("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Explanation"));
		desc.put("outputs", l2);
		
		List<URI> InParents = new ArrayList<URI>();
		InParents.addAll(l);
		InParents.add(URI.create("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Resource"));
		//InParents.add(new URI("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Education"));
		List<URI> OutParents = new ArrayList<URI>();
		OutParents.addAll(l);
		OutParents.add(URI.create("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Resource"));
		//OutParents.add(new URI("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Education"));
		
		desc.put("inputParents", InParents);
		desc.put("outputParents", OutParents);
		
		//req.setId(14);
		req.setServiceDescriptions(desc);
		Service s = rm.manageRequest(req).getService();
		
		System.out.println(s.getURI());
		assertEquals("localhost:8080/Services/compositeService.owl", s.getURI().toString());
	}

	@Test
	public void testManageRequestDaIRequest() {
		RequestManager rm = new RequestManager();
		DiscoveryRequest req = new DiscoveryRequest();
		Map<String, List<URI>> desc = new HashMap<String, List<URI>>();
		
		List<URI> l = new ArrayList<URI>();
		List<URI> l2 = new ArrayList<URI>();
		l.add(URI.create("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Problem"));
		desc.put("inputs", l);
		l2.add(URI.create("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Explanation"));
		desc.put("outputs", l2);
		
		List<URI> InParents = new ArrayList<URI>();
		InParents.addAll(l);
		InParents.add(URI.create("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Resource"));
		//InParents.add(new URI("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Education"));
		List<URI> OutParents = new ArrayList<URI>();
		OutParents.addAll(l);
		OutParents.add(URI.create("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Resource"));
		//OutParents.add(new URI("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Education"));
		
		desc.put("inputParents", InParents);
		desc.put("outputParents", OutParents);
		
		DaIRequest dai = new DaIRequest();
		
		dai.setServiceDescriptions(desc);
		List<URI> inputs = new ArrayList<URI>();
		inputs.add(URI.create("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Problem_2"));
		dai.setInputs(inputs);
		//dai.setId(15);
		
		InvocationResponse inv = rm.manageRequest(dai);
	
		System.out.println(((OWLIndividual)inv.getResults().get(0)).toRDF(true, true));
	}

	@Test
	public void testManageRequestInvocationRequest() {
		RequestManager rm = new RequestManager();
		DiscoveryRequest req = new DiscoveryRequest();
		Map<String, List<URI>> desc = new HashMap<String, List<URI>>();
		
		List<URI> l = new ArrayList<URI>();
		List<URI> l2 = new ArrayList<URI>();
		l.add(URI.create("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Problem"));
		desc.put("inputs", l);
		l2.add(URI.create("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Explanation"));
		desc.put("outputs", l2);
		
		List<URI> InParents = new ArrayList<URI>();
		InParents.addAll(l);
		InParents.add(URI.create("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Resource"));
		//InParents.add(new URI("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Education"));
		List<URI> OutParents = new ArrayList<URI>();
		OutParents.addAll(l);
		OutParents.add(URI.create("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Resource"));
		//OutParents.add(new URI("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Education"));
		
		desc.put("inputParents", InParents);
		desc.put("outputParents", OutParents);
		
		//req.setId(14);
		req.setServiceDescriptions(desc);
		Service s = rm.manageRequest(req).getService();
		
		List<URI> inputs = new ArrayList<URI>();
		inputs.add(URI.create("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Problem_2"));
		
		InvocationRequest ir = new InvocationRequest();
		
		ir.setInputs(inputs);
		ir.setService(s);
		
		InvocationResponse inv = rm.manageRequest(ir);
		System.out.println(((OWLIndividual)inv.getResults().get(0)).toRDF(true, true));
	}

}
