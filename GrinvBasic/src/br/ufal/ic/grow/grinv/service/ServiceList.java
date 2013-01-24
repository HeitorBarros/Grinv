package br.ufal.ic.grow.grinv.service;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * A set of Services
 * @author Heitor Barros
 * @version 0.1
 *
 */
@XStreamAlias("ServiceList")
public class ServiceList {

	@XStreamImplicit(itemFieldName="service")
	private List<Service> services;

	public ServiceList(List<Service> services) {
		super();
		this.services = services;
	}

	public ServiceList() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}
	
	
	
}
