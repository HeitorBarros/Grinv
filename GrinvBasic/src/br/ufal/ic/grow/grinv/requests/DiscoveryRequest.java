package br.ufal.ic.grow.grinv.requests;

import java.net.URI;
import java.util.List;
import java.util.Map;


/**
 * Discovery Request Class
 * 
 * @author Heitor Barros
 * @version 0.1
 *
 */
public class DiscoveryRequest extends Request {

	/**
	 * Services Descriptions
	 */
	private Map<String, List<URI>> serviceDescriptions;
	/**
	 * Request type
	 */
	private static final String TYPE = "DiscoveryRequest";
	/**
	 * Constructor
	 * @param serviceDescriptions
	 */
	public DiscoveryRequest(Map<String, List<URI>> serviceDescriptions) {
		super();
		super.setType(TYPE);
		this.serviceDescriptions = serviceDescriptions;
	}
	/**
	 * Constructor
	 */
	public DiscoveryRequest() {
		super();
		super.setType(TYPE);
	}
	
	public Map<String, List<URI>> getServiceDescriptions() {
		return serviceDescriptions;
	}
	
	public void setServiceDescriptions(Map<String, List<URI>> serviceDescriptions) {
		this.serviceDescriptions = serviceDescriptions;
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
		
		return str;
	}
  
  
}