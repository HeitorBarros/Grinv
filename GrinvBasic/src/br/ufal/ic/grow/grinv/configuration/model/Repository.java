package br.ufal.ic.grow.grinv.configuration.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Repository {

	@XStreamAlias("Class")
	private String name;
	
	@XStreamAlias("Location")
	private String location;

	public Repository(String name, String location) {
		super();
		this.name = name;
		this.location = location;
	}

	public Repository() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	@Override
	public String toString() {
		String msg = "Repository\n";
		msg+= "ClassImplementation: "+this.getName()+"\n";
		msg+= "Location :"+ this.getLocation()+"\n";
		 
		return msg;
	}
	
}
