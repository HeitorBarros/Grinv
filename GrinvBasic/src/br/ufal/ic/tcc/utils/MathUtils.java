package br.ufal.ic.tcc.utils;

import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * 
 * @author <a href="mailto:marlos.tacio@gmail.com">marlos</a>
 * 
 */

public class MathUtils {

	// Static --------------------------------------------------------

	private static Logger logger;

	static {
		logger = Logger.getLogger(Math.class.getName());
	}

	// Public --------------------------------------------------------

	/**
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double produtoInterno(final Vector<Double> v1,
			final Vector<Double> v2) {
		if (v1.size() != v2.size())
			return 0.0;

		double result = 0.0;
		for (int i = 0; i < v1.size(); ++i)
			result += v1.get(i) * v2.get(i);

		return result;
	}

	/**
	 * 
	 * @param v
	 * @return
	 */
	public static double norma(final Vector<Double> v) {
		return Math.sqrt(MathUtils.produtoInterno(v, v));
	}
}
