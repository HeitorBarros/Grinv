package br.ufal.ic.grow.grinv.requests;

import java.net.URI;
import java.util.Map;
import java.util.List;


/**
 * Discovery and Invocation Request Class
 * 
 * @author Heitor Barros
 * @version 0.1
 *
 */
public class DaIRequest extends Request {

	/**
	 * Service Descriptions
	 */
	private Map<String, List<URI>> serviceDescriptions;
	/**
	 * Inputs
	 */
	private List<URI> inputs;
	/**
	 * Type 
	 */
	private static final String TYPE = "DaIRequest";
	  
	/**
	 * Constructor
	 */
	public DaIRequest() {
		super();
		super.setType(TYPE);
	}
	
	/**
	 * Constructor
	 * @param serviceDescriptions
	 * @param inputs
	 */
	public DaIRequest(Map<String, List<URI>> serviceDescriptions, List<URI> inputs) {
		super();
		super.setType(TYPE);
		this.serviceDescriptions = serviceDescriptions;
		this.inputs = inputs;
	}
	
	public Map<String, List<URI>> getServiceDescriptions() {
		return serviceDescriptions;
	}
	
	public void setServiceDescriptions(Map<String, List<URI>> serviceDescriptions) {
		this.serviceDescriptions = serviceDescriptions;
	}
	
	public List<URI> getInputs() {
		return inputs;
	}
	
	public void setInputs(List<URI> inputs) {
		this.inputs = inputs;
	}

	public String getCompleteDescription() {
		
		String str="";
		str+=this.getType()+"\n";
		
		for (String key : this.serviceDescriptions.keySet()) {
			str+= key+": \n";
			for (URI uri : serviceDescriptions.get(key)) {
				str+=">"+ uri+"\n";
			}
		}
		
		str+= "\n inputs:\n";
		for (URI uri : inputs) {
			str+=">"+ uri+"\n";
		}
		
		return str;
	}
	
}