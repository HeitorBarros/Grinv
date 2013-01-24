package br.ufal.ic.grow.grinv.discovery.similarity;

import java.net.URI;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.mindswap.owl.OWLIndividual;

import br.ufal.ic.tcc.utils.MathUtils;

/**
 * 
 * @author <a href="mailto:marlos.tacio@gmail.com">marlos</a>
 * 
 */

public class ExtendedJacquardSimilarity extends SimilarityMeasure {

	// Static -----------------------------------------------------------
	
	private static Logger logger;

	static {
		logger = Logger.getLogger(ExtendedJacquardSimilarity.class.getName());
	}

	// Constructor -----------------------------------------------------

	/**
	 * 
	 * @param s1
	 * @param s2
	 * @throws Exception
	 */
	public ExtendedJacquardSimilarity(Vector<URI> s1, Vector<URI> s2)
			throws Exception {
		super(s1, s2);
	}

	// Public ----------------------------------------------------------

	/**
	 * 
	 */
	@Override
	public double calculateSimilarity() {
		double prod = MathUtils.produtoInterno(super.service1, super.service2);
		double quo = Math.pow(MathUtils.norma(super.service1), 2)
				+ Math.pow(MathUtils.norma(super.service2), 2) - prod;
		
		if(quo == 0.0)
			return 1.0;
		return quo == 0 ? 1 : 1 - (prod / quo);
	}

}
