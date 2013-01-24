package br.ufal.ic.tcc.graph;

import java.util.List;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * @author iaarc
 * 
 * Classe que armazena uma lista de vertices indicando um caminho e o peso total
 */
@XStreamAlias("Path")
public class Path implements Comparable<Path> {

	// Attributes ------------------------------------------------------
	@XStreamImplicit(itemFieldName="vertex")
	private List<Vertex> vt;
	@XStreamAlias("weight")
	private double weight;

	// Static ----------------------------------------------------------

	private static Logger logger;

	static {
		logger = Logger.getLogger(Path.class.getName());
	}

	// Constructor -----------------------------------------------------

	public Path(final List<Vertex> vt, final double weight) {
		this.vt = vt;
		this.weight = weight;
	}

	// Public ----------------------------------------------------------

	/**
	 * @return the vt
	 */
	public List<Vertex> getVt() {
		return vt;
	}

	/**
	 * @param vt
	 *            the vt to set
	 */
	public void setVt(List<Vertex> vt) {
		this.vt = vt;
	}

	/**
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * @param weight
	 *            the weight to set
	 */
	public void setWeight(final double weight) {
		this.weight = weight;
	}

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Path) {
			Path new_name = (Path) obj;
			return new_name.vt.equals(vt) && new_name.weight == weight;
		}
		return false;
	}

	@Override
	public int compareTo(final Path o) {
		return 0;
	}
}