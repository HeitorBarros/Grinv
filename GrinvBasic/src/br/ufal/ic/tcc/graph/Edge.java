package br.ufal.ic.tcc.graph;

import java.io.Serializable;

import br.ufal.ic.grow.grinv.service.Service;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @author Ivo Augusto
 * 
 * @param <Service>
 */
@XStreamAlias("edge")
public class Edge implements Serializable{

	// Atributes ------------------------------------------------------
	@XStreamAlias("destination")
	private Vertex destino;
	
	@XStreamAlias("origin")
	private Vertex origem;

	@XStreamAlias("weight")
	private double peso;
	
	// Static ---------------------------------------------------------
	
	private static Logger logger;

	static {
		logger = Logger.getLogger(Edge.class.getName());
	}

	// Constructor ----------------------------------------------------

	/**
	 * @param origem
	 * @param destino
	 * @param peso
	 */
	public Edge(final Vertex origem, final Vertex destino, double peso) {
		this.setNos(origem, destino);
		this.setPeso(peso);
	}

	// Public --------------------------------------------------------

	/**
	 * 
	 */
	public Vertex getOrigem() {
		return this.origem;
	}

	/**
	 * 
	 * @param origem
	 * @param destino
	 */
	public void setNos(final Vertex origem, final Vertex destino) {
		//System.out.println("...");
		if (origem == null || destino == null){
			return;
		}

		// S� altera se a origem for diferente do destino
		if (origem.compareTo(destino) == 0)
			return;
		
		this.origem = origem;
		this.destino = destino;

		this.origem.adicionarAresta(this);
		this.destino.adicionarAresta(this);
	}

	/**
	 * 
	 * @return
	 */
	public int removerAresta() {
		if (this.getOrigem() == null || this.getDestino() == null)
			return 0;

		int res = 0;

		if (this.getOrigem().removerAresta(this))
			++res;

		if (this.getDestino().removerAresta(this))
			++res;

		return res;
	}

	/**
	 * 
	 * @return
	 */
	public Vertex getDestino() {
		return this.destino;
	}

	/**
	 * 
	 * @return
	 */
	public double getPeso() {
		return this.peso;
	}

	/**
	 * 
	 * @param peso
	 */
	public void setPeso(double peso) {
		this.peso = peso;
	}

	/**
	 * 
	 */
	public String toString() {
		return this.getOrigem() + " ---- (" + this.getPeso() + ") ---- "
				+ this.getDestino();
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int compareTo(Object obj) {
		if (obj == null)
			return -1;

		if (obj instanceof Edge) {
			Edge other = (Edge) obj;
			/*
			 * if ((this.origem.compareTo(other.getOrigem()) == 0 &&
			 * this.destino.compareTo(other.getDestino()) == 0) ||
			 * (this.origem.compareTo(other.getDestino()) == 0 &&
			 * this.destino.compareTo(other.getOrigem()) == 0))
			 */
			if (this.origem.compareTo(other.getOrigem()) == 0
					&& this.destino.compareTo(other.getDestino()) == 0)
				return 0;

			return -1;
		} else
			return -1;
	}

	/**
	 * 
	 */
	public boolean equals(Object obj) {
		return this.compareTo(obj) == 0;
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.destino.hashCode()+this.origem.hashCode();
	}
}
