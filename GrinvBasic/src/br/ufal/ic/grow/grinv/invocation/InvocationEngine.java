package br.ufal.ic.grow.grinv.invocation;

import java.util.Map;

import org.mindswap.owl.OWLIndividual;

import br.ufal.ic.grow.grinv.exception.ServiceInvocationException;
import br.ufal.ic.grow.grinv.service.Service;
import br.ufal.ic.grow.grinv.service.parameters.Parameter;

public abstract class InvocationEngine {
	
	public abstract Map<Parameter, OWLIndividual> invokeService(int id, Service service, Map<Parameter, OWLIndividual> inputs) throws ServiceInvocationException;

}
