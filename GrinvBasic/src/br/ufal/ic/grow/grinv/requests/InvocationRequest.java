package br.ufal.ic.grow.grinv.requests;

import java.net.URI;
import java.util.List;

import br.ufal.ic.grow.grinv.service.Service;
/**
 * Invocation Request Class
 * 
 * @author Heitor Barros
 * @version 0.1
 *
 */
public class InvocationRequest extends Request {
	/**
	 * Service
	 */
	private Service service;
	/**
	 * Inputs
	 */
	private List<URI> inputs;
	/**
	 * Request Type
	 */
	private static final String TYPE = "InvocationRequest";
	/**
	 * Constructor
	 * @return
	 */
	public Service getService() {
		return service;
	}
	
	public void setService(Service service) {
		this.service = service;
	}
	
	@SuppressWarnings("unchecked")
	public List getInputs() {
		return inputs;
	}
	
	public void setInputs(List<URI> inputs) {
		this.inputs = inputs;
	}
	
	public InvocationRequest(Service service, List<URI> inputs) {
		super();
		super.setType(TYPE);
		this.service = service;
		this.inputs = inputs;
	}
	
	public InvocationRequest() {
		super();
		super.setType(TYPE);
	}

	public String getCompleteDescription() {
		
		String str="";
		str+=this.getType()+"\n";
		
		str+= service+": \n inputs:\n";
		for (URI uri : inputs) {
			str+=">"+ uri+"\n";
		}

		
		return str;
	}

}