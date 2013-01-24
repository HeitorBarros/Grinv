package br.ufal.ic.grow.grinv.configuration.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Class that represents an Analysis Configuration
 * @author Heitor Barros
 * @version 0.1
 *
 */
public class Analysis {
	@XStreamAlias("ClassName")
	private String name;

	public Analysis(String name) {
		super();
		this.name = name;
	}

	public Analysis() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
