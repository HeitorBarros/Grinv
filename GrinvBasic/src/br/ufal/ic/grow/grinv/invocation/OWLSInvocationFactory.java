package br.ufal.ic.grow.grinv.invocation;

import br.ufal.ic.grow.grinv.configuration.SystemConfiguration;
import br.ufal.ic.grow.grinv.configuration.model.Configuration;

public class OWLSInvocationFactory extends InvocationFactory {

	@Override
	public InvocationEngine getInvocationEngine() {
		return SystemConfiguration.getInvocationEngine();
		
	}

}
