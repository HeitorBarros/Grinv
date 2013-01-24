package br.ufal.ic.grow.grinv.service.parameters;

import br.ufal.ic.grow.grinv.service.Service;

public class Parameter {

	private Service service;
	
	private String id;

	public Parameter(Service service, String id) {
		super();
		this.service = service;
		this.id = id;
	}

	public Parameter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		String ret = "";
		ret+= "service: "+this.getService().getURI()+" input:"+this.id;
		
		
		return ret;
	}
	
	@Override
	public boolean equals(Object obj) {
		Parameter p = Parameter.class.cast(obj);
		if (this.id.equals(p.id)) {
			if (this.service.getURI().equals(p.getService().getURI())) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.id.toString().hashCode() ^ this.service.toString().hashCode();
	}
	
}
