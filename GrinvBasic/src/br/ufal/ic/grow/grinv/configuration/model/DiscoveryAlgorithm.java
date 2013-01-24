package br.ufal.ic.grow.grinv.configuration.model;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Class that represents a Discovery Configuration
 * 
 * @author Heitor Barros
 * @version 0.1
 *
 */
@XStreamAlias("DiscoveryAlgorithm")
public class DiscoveryAlgorithm {

	@XStreamAlias("priority")
	private int priority;
		
	
	@XStreamAlias("ClassName")
	private String name;
	
	@XStreamImplicit(itemFieldName="Parameter")
	private List<String> parameters;
	
	
	public DiscoveryAlgorithm(String discoveryClass, List<String> parameters) {
		super();
		name = discoveryClass;
		this.parameters = parameters;
	}
	public DiscoveryAlgorithm() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getDiscovery_class() {
		return name;
	}
	public void setDiscovery_class(String discoveryClass) {
		name = discoveryClass;
	}
	public List<String> getParameters() {
		return parameters;
	}
	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	
	
	
}
