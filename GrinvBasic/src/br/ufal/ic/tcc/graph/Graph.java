package br.ufal.ic.tcc.graph;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("graph")
public class Graph implements Serializable{

	// Atributes ------------------------------------------------------
//	@XStreamImplicit(itemFieldName="edges")
//	private Vector<Edge> arestas;
	@XStreamImplicit(itemFieldName="vertex")
	private List<Vertex> vertices;

	// Static ---------------------------------------------------------

	private static Logger logger;

	static {
		logger = Logger.getLogger(Graph.class.getName());
	}

	// Constructor ----------------------------------------------------

	/**
	 * 
	 */
	public Graph() {
		//this.arestas = new Vector<Edge>();
		this.vertices = new Vector<Vertex>();
	}

	// Public ---------------------------------------------------------

	/**
	 * 
	 */
//	public Vector<Edge> getArestas() {
//		return this.arestas;
//	}

	/**
	 * 
	 * @param arestas
	 */
//	public void setArestas(final Vector<Edge> arestas) {
//		this.arestas = arestas;
//	}

	/**
	 * 
	 * @return
	 */
	public List<Vertex> getVertices() {
		return this.vertices;
	}

	/**
	 * 
	 * @param vertices
	 */
	public void setVertices(final List<Vertex> vertices) {
		this.vertices = vertices;
	}

	/**
	 * 
	 * @param collection
	 * @param anItem
	 * @return
	 */
	public boolean exists(final List collection, final Object anItem) {
		return collection.indexOf(anItem) != -1;
	}

	/**
	 */
	public boolean addVertex(final Vertex vertex) {
		if (!exists(this.vertices, vertex))
			return this.vertices.add(vertex);
		return false;
	}

	/**
	 * 
	 * @param edge
	 * @return
	 */
//	public boolean addEdge(final Edge edge) {
//		if (!exists(this.arestas, edge))
//			return this.arestas.add(edge);
//		return false;
//	}

	/**
	 * Retorna um Map contendo a lista dos vertices do caminho e um float
	 * contendo o tamanho total. O motivo de se utilizar um map e a necessidade
	 * de ser juntar o caminho com o valor total. No entanto este map contem
	 * apenas um unico par. A lista com os caminho e o pesototal
	 * 
	 * @param origem
	 * @param destino
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Path getPath(final Vertex origem, final Vertex destino) {
		Vertex[] array = new Vertex[this.vertices.size()];
		this.vertices.toArray(array);
		Dijkstra caminho = new Dijkstra(array);

		Vertex[] menor = caminho.doDijkstra(origem, destino);
		List<Vertex> retorno = new LinkedList<Vertex>();
		float pesoTotal = 0f;
		if (menor != null) {
			int count = 0;
			for (int i = 0; i < menor.length; i++) {
				if (menor[i] != null) {
					count++;
					retorno.add(menor[i]);
					if (i > 0)
						pesoTotal += weight(menor[i - 1], menor[i]);

				}
			}
			if (count == 1 && menor[0].getD() == Dijkstra.INFINITY)
				return null;
			else {
				/*
				 * Map<List<Vertex<T>>, Float> m = new HashMap<List<Vertex<T>>,
				 * Float>(); m.put(retorno, pesoTotal);
				 */
				return new Path(retorno, pesoTotal);
			}
		} else
			return null;
	}

	/*
	 * public boolean removeVertex(Vector<Vertex<T>> collection, Vertex<T>
	 * obj) { boolean result = collection.remove(obj); }
	 */

	//não funciona
//	public void removeEdge(Vertex vOrigem, Vertex vDestino){
//		Vector<Edge> t = this.arestas;
//		for (Edge e : t) {
//			if ((e.getOrigem().getContent() == vOrigem.getContent()) && (e.getDestino().getContent()==vDestino.getContent())) {
//				t.remove(e);
//				break;
//			}
//		}
//		this.arestas.clear();
//		this.arestas = null;
//		this.arestas =t;
//		this.updateEdges();
//	}
	
	/**
	 * 
	 */
//	@SuppressWarnings("unchecked")
//	public void updateEdges() {
//		this.arestas.clear();
//
//		for (int i = 0; i < this.vertices.size(); i++) {
//			Vertex no = (Vertex) this.vertices.elementAt(i);
//			this.arestas.addAll(no.getEdges());
//		}
//	}

	// Private -------------------------------------------------------

	@SuppressWarnings("unchecked")
	private double weight(final Vertex a, final Vertex b) {
		Edge c = new Edge(a, b, 0f);
		for (int i = 0; i < a.getEdges().size(); i++)
			if (a.getEdges().get(i).equals(c))
				return ((Edge) a.getEdges().get(i)).getPeso();

		return Dijkstra.INFINITY;
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.vertices.hashCode();
	}
	
}
