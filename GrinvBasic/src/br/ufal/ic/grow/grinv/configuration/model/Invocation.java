package br.ufal.ic.grow.grinv.configuration.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Class that represents a Invocation Configuration
 * 
 * @author Heitor Barros
 * @version 0.1
 * 
 */
public class Invocation {
	@XStreamAlias("ClassName")
	private String name;

	public Invocation(String name) {
		super();
		this.name = name;
	}

	public Invocation() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
