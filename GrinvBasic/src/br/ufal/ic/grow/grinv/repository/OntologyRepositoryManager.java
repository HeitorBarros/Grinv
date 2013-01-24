package br.ufal.ic.grow.grinv.repository;

import java.io.IOException;
import java.net.URI;

import org.mindswap.owl.OWLKnowledgeBase;

public interface OntologyRepositoryManager {

	public void addOntology(URI uri) throws IOException;
	
	public OWLKnowledgeBase getKB() throws IOException;
	
	
}
