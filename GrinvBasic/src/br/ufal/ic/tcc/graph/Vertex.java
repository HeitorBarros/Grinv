package br.ufal.ic.tcc.graph;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import br.ufal.ic.grow.grinv.service.Service;

/**
 * 
 * @author Ivo Augusto
 * 
 * @param <Service>
 */
@XStreamAlias("vertex")
public class Vertex implements Comparable<Vertex>, Serializable{

	// Attributes ------------------------------------------------------
	@XStreamAlias("service")
	private Service content;
	@XStreamAlias("anterior")
	private Vertex anterior;
	@XStreamAlias("D")
	private double D = Dijkstra.INFINITY;

	@XStreamImplicit(itemFieldName="edges")
	private Vector<Edge> edges = new Vector<Edge>();

	// Static ----------------------------------------------------------

	private static Logger logger;

	static {
		logger = Logger.getLogger(Vertex.class.getName());
	}

	// Constructor -----------------------------------------------------

	/**
	 * 
	 * @param content
	 */
	public Vertex(Service content) {
		this.content = content;
	}

	/**
	 * 
	 */
	public Vertex() {
		this.content = null;
	}


	// Public ----------------------------------------------------------


	/**
	 * 
	 * @return
	 */
	public Vector<Edge> getEdges() {
		return this.edges;
	}

	/**
	 * 
	 * @param edges
	 */
	public void setEdges(Vector<Edge> edges) {
		this.edges = edges;
	}

	/**
	 * 
	 * @param destino
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public double getWeight(Vertex destino) {
		Iterator<?> i = this.getEdges().iterator();

		while (i.hasNext()) {
			Edge aresta = (Edge) i.next();

			if ((aresta.getOrigem().compareTo(this) == 0 && aresta.getDestino()
					.compareTo(destino) == 0)
					|| (aresta.getOrigem().compareTo(destino) == 0 && aresta
							.getDestino().compareTo(this) == 0))
				return aresta.getPeso();
		}

		return Dijkstra.INFINITY;
	}

	/**
	 * 
	 * @param edge
	 */
	public void adicionarAresta(Edge edge) {
		//System.out.println(edge.getPeso()+" "+edge.getOrigem().getContent().getURI()+" "+edge.getOrigem().getContent().getURI());
		if (edge == null)
			throw new IllegalArgumentException(
					"Parametro invalido - aresta nula");

		if (this.getEdges().indexOf(edge) == -1)
			this.getEdges().add(edge);
	}

	/**
	 * 
	 * @param edge
	 * @return
	 */
	public boolean removerAresta(Edge edge) {
		return this.getEdges().remove(edge);
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void removerNo() {
		for (int i = 0; i < this.getEdges().size(); i++) {
			Edge a = (Edge) this.getEdges().elementAt(i);

			if (a.removerAresta() > 0)
				--i;
		}
	}

	/**
	 * 
	 * @return
	 */
	public Vertex getAnterior() {
		return this.anterior;
	}

	/**
	 * 
	 * @param anterior
	 */
	public void setAnterior(Vertex anterior) {
		this.anterior = anterior;
	}

	/**
	 * 
	 * @return
	 */
	public double getD() {
		return this.D;
	}

	/**
	 * 
	 * @param D
	 */
	public void setD(double D) {
		this.D = D;
	}

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Vertex[] getVizinhos() {
		Vertex[] nos = new Vertex[this.getEdges().size()];
		Iterator<?> i = this.getEdges().iterator();

		if (this.getEdges().size() == 0)
			return null; // Sem n�s vizinhos

		int count = 0;
		while (i.hasNext()) {
			Edge aresta = (Edge) i.next();

			if (aresta.getOrigem().compareTo(this) == 0)
				nos[count] = aresta.getDestino();
			else
				nos[count] = aresta.getOrigem();

			count++;
		}

		return nos;
	}

	/**
	 * 
	 */
	public int compareTo(Vertex obj) {
		if (obj == null)
			return -1;
		
		if (obj instanceof Vertex) {
			Vertex other = (Vertex) obj;
			if (content instanceof Service) {
				if (content.equals(other.content)) {
					return 0;
				}
				return -1;

			}
			// // PONTO DE ALTERACAO PARA ADICIONAR CODIGO DE COMPARACAO ENTRE
			// TERMOS
			return -1;
		} else
			return -1;
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		if (obj instanceof Vertex) {
			Vertex new_name = (Vertex) obj;

			// return content.equals(new_name.content); //ALTERADO
			if (content instanceof Service) {
				return content.equals(new_name.content);
			}
			return compareTo(new_name) == 0;
		}
		return false;

	}

	/**
	 * 
	 */
	public String toString() {
		return this.getContent().getURI().toString();
	}

	/**
	 * @return the content
	 */
	public Service getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(Service content) {
		this.content = content;
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.content.hashCode();
	}
}
