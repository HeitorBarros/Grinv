package br.ufal.ic.grow.grinv.test.repository;

import java.net.URI;

import junit.framework.TestCase;

import br.ufal.ic.grow.grinv.repository.GrinvRepository;
import br.ufal.ic.grow.grinv.service.OWLSService;


public class GrinvRepositoryTestCase extends TestCase{

	public void testAddition(){
		GrinvRepository r = new GrinvRepository();
		r.setLocation("SRTest");
		
		OWLSService s1 = new OWLSService(URI.create("http://localhost:8080/LearningServices/owls/consultarRelatorio.owl"));
		
		r.addService(URI.create("http://localhost:8080/LearningServices/owls/consultarRelatorio.owl"));
		//r.addService(URI.create("http://localhost:8080/LearningServices/owls/getExplanation.owl"));
	}
	
	public void testRemove(){
		GrinvRepository r = new GrinvRepository();
		r.setLocation("SRTest");
		
		r.remove(URI.create("http://localhost:8080/LearningServices/owls/consultarRelatorio.owl"));
	}
	
	
}
