package br.ufal.ic.tcc.graph;

import java.util.Stack;

import org.apache.log4j.Logger;

/**
 * 
 * @author Ivo Augusto
 * 
 * @param <T>
 */

public class Dijkstra{

	// Atributes -----------------------------------------------------

	private Vertex[] nos;

	private Vertex[] N;

	// Static --------------------------------------------------------

	public static float INFINITY = Float.MAX_VALUE;
	
	private static Logger logger;

	static {
		logger = Logger.getLogger(Dijkstra.class.getName());
	}

	// Constructor ---------------------------------------------------

	/**
	 * @param nos
	 */
	public Dijkstra(final Vertex[] nos) {
		this.setNos(nos);
	}

	// Public --------------------------------------------------------

	/**
	 * 
	 */
	public Vertex[] getNos() {
		return this.nos;
	}

	/**
	 * 
	 * @param nos
	 */
	public void setNos(final Vertex[] nos) {
		this.nos = nos;
	}

	/**
	 * 
	 * @param no
	 * @return
	 */
	// Verifica se um determinado n� est� em N
	public boolean emN(final Vertex no) {
		for (int i = 0; i < N.length; i++) {
			if (no.compareTo(N[i]) == 0)
				return true;
		}

		return false;
	}

	/**
	 * 
	 * @param origem
	 * @param destino
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Vertex[] doDijkstra(final Vertex origem,
			final Vertex destino) {
		int i = 0, j = 0;
		Vertex menor = null, atual = null; // �ltimo n� adicionado a N

		// Inicializa��o
		N = new Vertex[nos.length];
		N[j++] = origem;

		for (i = 0; i < nos.length; i++) {
			nos[i].setD(INFINITY);
			nos[i].setAnterior(null);
		}

		atual = origem;

		// Dist�ncia de um n� para ele mesmo � inexistente
		atual.setD(0);

		Vertex[] vizinhos = atual.getVizinhos();

		if (vizinhos == null)
			return null;

		for (i = 0; i < vizinhos.length; i++)
			vizinhos[i].setAnterior(atual);

		while (atual.compareTo(destino) != 0) {
			menor = null;

			for (i = 0; i < nos.length; i++) {
				if (!emN(nos[i])) {
					if (menor == null)
						menor = nos[i];

					if (atual.getD() + atual.getWeight(nos[i]) < nos[i].getD()) {
						if (atual.getWeight(nos[i]) < INFINITY) {
							nos[i].setD(atual.getD() + atual.getWeight(nos[i]));

							if (atual.getWeight(nos[i]) < INFINITY)
								nos[i].setAnterior(atual);
						}
					}

					if (nos[i].getD() < menor.getD()) {
						menor = nos[i];
					}
				}
			}

			atual = menor;
			N[j++] = atual;
		}

		Stack<Vertex> pilha = new Stack<Vertex>();
		Vertex[] retorno = new Vertex[N.length];
		atual = N[j - 1];
		i = 0;

		while (atual != null) {
			pilha.push(atual);
			atual = atual.getAnterior();
		}

		j = pilha.size();
		for (i = 0; i < j; i++)
			retorno[i] = (Vertex) pilha.pop();

		return retorno;
	}
}
