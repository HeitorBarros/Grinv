package br.ufal.ic.grow.grinv.requests;

import java.net.URI;

import br.ufal.ic.grow.grinv.service.Service;

/**
 * Discovery Response Class
 * 
 * @author Heitor Barros
 * @version 0.1
 *
 */
public class DiscoveryResponse extends Response {
	/**
	 * Service
	 */
	private Service service;
	/**
	 * Constructor
	 * @param service
	 */
	public DiscoveryResponse(Service service) {
		super();
		this.service = service;
		super.setType("DiscoveryResponse");
	}
	
	public DiscoveryResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Service getService() {
		return service;
	}
	
	public void setService(Service service) {
		this.service = service;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if(this.getService()!=null){
			return "Discovery Response\n Service: "+ this.getService().toString();
		}
		
		return "No Service Found";
		
	}
  
  
}