package br.ufal.ic.grow.grinv.test.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owls.process.variable.Input;

import br.ufal.ic.grow.grinv.configuration.SystemConfiguration;
import br.ufal.ic.grow.grinv.exception.ServiceInvocationException;
import br.ufal.ic.grow.grinv.repository.AbstractRepositoryManager;
import br.ufal.ic.grow.grinv.service.Service;
import br.ufal.ic.grow.grinv.service.OWLSService;
import br.ufal.ic.grow.grinv.service.parameters.Parameter;
import junit.framework.TestCase;


public class TestServices extends TestCase {

	public void testInvocation(){
		
//		AbstractRepositoryManager repo = SystemConfiguration.getRepository();
//		
//		List<Service> services = repo.loadAllServices();
//		
//		SimpleService s = (SimpleService) services.get(2);
//		
//		
//		
//		OWLKnowledgeBase kb = OWLFactory.createKB();
//		
//		URI inputURI = URI.create("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Estudante");
//		Map<Parameter, Object> map = new HashMap<Parameter, Object>();
//
//		Input estudante = kb.createInput(URI.create("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Estudante_Djalma"));
//		
//		Parameter p = new Parameter(s, s.getInputs().get(0).getId());
//		map.put(p, estudante);
//		
//		Map<URI, Object> oMap = s.invoke(map);
//		
//		for (Object o : oMap.keySet()) {
//			
//			System.out.println(((OWLIndividual)oMap.get(o)).toRDF(true, true));
//		}
		
	}
	
public void testInvocation2(){
		
		AbstractRepositoryManager repo = SystemConfiguration.getRepository();
		
		List<Service> services = repo.loadAllServices();
		
		OWLSService s = (OWLSService) services.get(1);
		
		
		
		OWLKnowledgeBase kb = OWLFactory.createKB();
		
		URI inputURI = URI.create("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Problem");
		Map<Parameter, OWLIndividual> map = new HashMap<Parameter, OWLIndividual>();

		Input estudante = kb.createInput(URI.create("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Problem_2"));
		
		Parameter p = new Parameter(s, s.getInputs().get(0).getId());
		map.put(p, estudante);
		
		Map<Parameter, OWLIndividual> oMap;
		try {
			oMap = s.invoke(-1,map);
			
			for (Object o : oMap.keySet()) {
				
				System.out.println(((OWLIndividual)oMap.get(o)).toRDF(true, true));
			}
		} catch (ServiceInvocationException e) {
			fail();
		}
		
		
		
	}
	
	
	
	
}
