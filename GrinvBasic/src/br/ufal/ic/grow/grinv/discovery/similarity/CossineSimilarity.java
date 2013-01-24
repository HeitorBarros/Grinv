package br.ufal.ic.grow.grinv.discovery.similarity;

import java.net.URI;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;


import org.mindswap.owl.OWLIndividual;

import br.ufal.ic.tcc.utils.MathUtils;

/**
 * 
 * @author <a href="mailto:marlos.tacio@gmail.com">marlos</a>
 * 
 */

public class CossineSimilarity extends SimilarityMeasure {

	// Static -----------------------------------------------------------


	static {
	}

	// Constructor ------------------------------------------------------

	public CossineSimilarity(List<URI> s1, List<URI> s2) throws Exception {
		super(s1, s2);
	}

	
//	/**
//	 * 
//	 * @param s1
//	 * @param s2
//	 * @throws Exception
//	 */
//	public CossineSimilarity(Vector<OWLIndividual> s1, Vector<OWLIndividual> s2)
//			throws Exception {
//		super(s1, s2);
//	}

	// Public ----------------------------------------------------------


	/**
	 * 
	 */
	public double calculateSimilarity() {
		double num = MathUtils.produtoInterno(service1, service2);
		double den = MathUtils.norma(service1) * MathUtils.norma(service2);
		
		if(den == 0.0){
			return 1.0;
		}
		
		double result = num / den;
		Double result_ = new Double(result);
		
		double r = 1 - result_.floatValue();
		
		
		return r < 0.0 ? 0.0 : r > 1.0 ? 1.0 : r;
	}
}
