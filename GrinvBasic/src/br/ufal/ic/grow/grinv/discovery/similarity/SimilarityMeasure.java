package br.ufal.ic.grow.grinv.discovery.similarity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.mindswap.pellet.utils.SetUtils;

import br.ufal.ic.grow.grinv.discovery.GBMatchmaker;

/**
 * 
 * @author <a href="mailto:marlos.tacio@gmail.com">marlos</a>
 * 
 */

public abstract class SimilarityMeasure {

	// Attributes ----------------------------------------------------

	protected Vector<Double> service1;

	protected Vector<Double> service2;

	// Static --------------------------------------------------------

	private final static String USER_PATH = "user.home";

	static {

	}

	// Constructor ---------------------------------------------------

	public SimilarityMeasure(final List<URI> p1, final List<URI> p2)
			throws Exception {
		
		this.service1 = new Vector<Double>();
		this.service2 = new Vector<Double>();

		calculeFrequencies(p1, p2);
	}
	
	/**
	 * 
	 * @param s1
	 * @param s2
	 * @throws Exception
	 */
//	public SimilarityMeasure(final Vector<OWLIndividual> s1,
//			final Vector<OWLIndividual> s2) throws Exception {
//		this.service1 = new Vector<Double>();
//		this.service2 = new Vector<Double>();
//
//		Vector<String> ancestor1 = new Vector<String>();
//		Vector<String> ancestor2 = new Vector<String>();
//		for (OWLIndividual param : s1) {
//			OWLModel model = this.getOntology(param);
//			String individual = param.toString().split("#")[1];
//			ancestor1.addAll(this.getAncestor(individual, model));
//		}
//
//		for (OWLIndividual param : s2) {
//			OWLModel model = this.getOntology(param);
//			String individual = param.toString().split("#")[1];
//			ancestor2.addAll(this.getAncestor(individual, model));
//		}
//
//		calculeFrequencies(ancestor1, ancestor2);
//	}

	// Public ---------------------------------------------------------

	/**
	 * 
	 * @return
	 */
	public abstract double calculateSimilarity();

	// Private---------------------------------------------------------

	/**
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
//	private OWLModel getOntology(OWLIndividual param) throws Exception {
//		String uri = param.getURI().toString().split("#")[0];
//
//		return ProtegeOWL.createJenaOWLModelFromURI(uri);
//	}

	/**
	 * 
	 * @param individual
	 * @param model
	 * @return
	 * @throws Exception
	 */
//	private Vector<String> getAncestor(final String individual, OWLModel model)
//			throws Exception {
//		Vector<String> vector = new Vector<String>();
//		vector.add(individual);
//
//		String query = "SELECT ?object WHERE {:" + individual
//				+ " rdfs:subClassOf ?object}";
//
//		QueryResults results = model.executeSPARQLQuery(query);
//
//		while (results.hasNext()) {
//			Map<?, ?> map = results.next();
//
//			try {
//				for (Object key : map.keySet()) {
//					String name = ((DefaultOWLNamedClass) map.get(key))
//							.getLocalName();
//					vector.addAll(getAncestor(name, model));
//				}
//			} catch (Exception e) {
//				System.out.println("");
//			}
//		}
//		vector.remove("Thing");
//		return vector;
//	}

	/**
	 * 
	 * @param s1
	 * @param s2
	 */
	private void calculeFrequencies(final List<URI> s1,
			final List<URI> s2) {
		
		List<URI> union = new ArrayList<URI>();
		union.addAll(s1);
		for (URI uri : s2) {
			if (!union.contains(uri)) {
				union.add(uri);
			}
		}
		
		//Iterator<?> union = SetUtils.union(s1, s2).iterator();
		
		for (URI individual: union) {
			//URI individual = union.next();

			double frequency1 = 0.0;
			for (URI s : s1)
				frequency1 += s.equals(individual) ? 1 : 0;
			this.service1.add(frequency1);

			double frequency2 = 0.0;
			for (URI s : s2)
				frequency2 += s.equals(individual) ? 1 : 0;
			this.service2.add(frequency2);
		}
	}
}
