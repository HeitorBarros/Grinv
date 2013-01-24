package br.ufal.ic.grow.grinv.service.composite;

import java.util.ArrayList;
import java.util.List;

import br.ufal.ic.grow.grinv.service.Service;
import br.ufal.ic.grow.grinv.service.parameters.ParametersMapping;

public abstract class CompositeService extends Service{

	protected List<Service> services;
	
	protected List<ParametersMapping> mapping;

	public CompositeService(List<Service> services) {
		super();
		this.services = services;
	}

	public CompositeService() {
		super();
		this.services = new ArrayList<Service>();
		this.mapping = new ArrayList<ParametersMapping>();
	}

	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}

	public List<ParametersMapping> getMapping() {
		return mapping;
	}

	public void setMapping(List<ParametersMapping> mapping) {
		this.mapping = mapping;
	}
	
	public void addService(Service s){
		services.add(s);
	}
	
	public void addMapping(ParametersMapping p){
		mapping.add(p);	
	}
}
