package br.ufal.ic.grow.grinv.discovery.similarity;

import java.net.URI;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.mindswap.pellet.utils.SetUtils;

/**
 * 
 * @author <a href="mailto:marlos.tacio@gmail.com">marlos</a>
 * 
 */

public class LOISimilarity extends SimilarityMeasure {

	// Attributes ------------------------------------------------------

	private double union;

	private double intersection;

	// Static -----------------------------------------------------------

	private static Logger logger;

	static {
		logger = Logger.getLogger(LOISimilarity.class.getName());
	}

	// Constructor ------------------------------------------------------

	/**
	 * 
	 * @param s1
	 * @param s2
	 * @throws Exception
	 */
	public LOISimilarity(final Vector<URI> s1, final Vector<URI> s2)
			throws Exception {
		super(s1, s2);
		this.prepareVectors(s1, s2);
	}

	// Public ------------------------------------------------------------

	/**
	 * 
	 */
	@Override
	public double calculateSimilarity() {
		double r = (this.union - this.intersection)
		/ (super.service1.size() + super.service2.size());
		
		
		if((super.service1.size() + super.service2.size()) == 0)
			return 1.0;
		return r;
	}

	// Private -----------------------------------------------------------

	/**
	 * 
	 * @param s1
	 * @param s2
	 */
	private void prepareVectors(final Vector<URI> s1,
			final Vector<URI> s2) {
		this.union = Math.abs(SetUtils.union(s1, s2).size());
		this.intersection = Math.abs(SetUtils.intersection(s1, s2).size());
	}

}
