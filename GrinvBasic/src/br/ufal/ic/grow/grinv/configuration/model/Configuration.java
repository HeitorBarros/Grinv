package br.ufal.ic.grow.grinv.configuration.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * Class that represents a Configuration File
 * 
 * @author Heitor Barros
 * @version 0.1
 *
 */
@XStreamAlias("Configuration")
public class Configuration {
	
	@XStreamAlias("Repository-SR")
	private Repository servicesRepository;
	
	@XStreamAlias("Repository-SUO")
	private Repository suoRepository;
	
	@XStreamAlias("Discovery")
	private Discovery discovery;
	@XStreamAlias("Analysis")
	private Analysis analysis;
	@XStreamAlias("Invocation")
	private Invocation invocation;
	
	
	public Configuration(Repository repository, Discovery discovery,
			Analysis analysis, Invocation invocation) {
		super();
		this.servicesRepository = repository;
		this.discovery = discovery; 
		this.analysis = analysis;
		this.invocation = invocation;
	}
	public Configuration() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Repository getRepository() {
		return servicesRepository;
	}
	public void setRepository(Repository repository) {
		this.servicesRepository = repository;
	}
	public Discovery getDiscovery() {
		return discovery;
	}
	public void setDiscovery(Discovery discovery) {
		this.discovery = discovery;
	}
	public Analysis getAnalysis() {
		return analysis;
	}
	public void setAnalysis(Analysis analysis) {
		this.analysis = analysis;
	}
	public Invocation getInvocation() {
		return invocation;
	}
	public void setInvocation(Invocation invocation) {
		this.invocation = invocation;
	}
	
	@Override
	public String toString() {
		String msg="";
		msg+= this.getAnalysis();
		msg+= this.getDiscovery();
		msg+= this.getInvocation();
		msg+= this.getRepository();
		msg+= this.getSuoRepository();
		return msg;
	}
	public void setSuoRepository(Repository suoRepository) {
		this.suoRepository = suoRepository;
	}
	public Repository getSuoRepository() {
		return suoRepository;
	}
	
	
}
