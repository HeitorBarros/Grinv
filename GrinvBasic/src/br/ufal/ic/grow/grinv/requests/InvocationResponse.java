package br.ufal.ic.grow.grinv.requests;

import java.util.Iterator;
import java.util.List;

import org.mindswap.owl.OWLIndividual;
/**
 * Invocation Response Class
 * 
 * @author Heitor Barros
 * @version 0.1
 *
 */
public class InvocationResponse extends Response {
	/**
	 * Results
	 */
	private List<OWLIndividual> results;
	/**
	 * Constructor
	 * @param results
	 */
	public InvocationResponse(List<OWLIndividual> results) {
		super();
		this.results = results;
		super.setType("InvocationResponse");
	}
	
	public InvocationResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public List<OWLIndividual> getResults() {
		return results;
	}
	
	public void setResults(List<OWLIndividual> results) {
		this.results = results;
	}
  
	@SuppressWarnings("unchecked")
	@Override
	public String toString() {
		if(results!=null){
			String s = "Invocation Response\nResults:\n";
			for (Iterator iterator = results.iterator(); iterator.hasNext();) {
				s += iterator.next()+"\n";
				
			}
			return s;
		}
		
		return "No Results for this Invocation";
		
		
	}
  

}