package br.ufal.ic.grow.grinv.configuration.model;

import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;


@XStreamAlias("Discovery")
public class Discovery {
	@XStreamImplicit(itemFieldName="Algorithm")
	private List<DiscoveryAlgorithm> algorithms;

	public Discovery(List<DiscoveryAlgorithm> algorithms) {
		super();
		this.algorithms = algorithms;
	}

	public Discovery() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<DiscoveryAlgorithm> getAlgorithms() {
		return algorithms;
	}

	public void setAlgorithms(List<DiscoveryAlgorithm> algorithms) {
		this.algorithms = algorithms;
	}

	
	
}
