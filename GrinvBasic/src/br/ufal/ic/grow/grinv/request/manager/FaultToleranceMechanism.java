package br.ufal.ic.grow.grinv.request.manager;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mindswap.owl.OWLIndividual;

import br.ufal.ic.grow.grinv.configuration.SystemConfiguration;
import br.ufal.ic.grow.grinv.exception.ServiceInvocationException;
import br.ufal.ic.grow.grinv.repository.AbstractRepositoryManager;
import br.ufal.ic.grow.grinv.requests.DaIRequest;
import br.ufal.ic.grow.grinv.requests.DiscoveryRequest;
import br.ufal.ic.grow.grinv.requests.DiscoveryResponse;
import br.ufal.ic.grow.grinv.requests.InvocationRequest;
import br.ufal.ic.grow.grinv.requests.InvocationResponse;
import br.ufal.ic.grow.grinv.service.Service;
import br.ufal.ic.grow.grinv.service.parameters.Input;
import br.ufal.ic.grow.grinv.service.parameters.Output;
import br.ufal.ic.grow.grinv.service.parameters.Parameter;

public class FaultToleranceMechanism {
	
	public InvocationResponse recoverFromFault(int id, Service faultyService, DaIRequest r){
		AbstractRepositoryManager suo = SystemConfiguration.getSUORepository();
		AbstractRepositoryManager sr = SystemConfiguration.getRepository();
		
		suo.addService(faultyService.getURI());
		sr.remove(faultyService.getURI());
		
		RequestManager rm = new RequestManager();
		
		r.setId(id);
		return rm.manageRequest(r);
	}

	public InvocationResponse recoverFromFault(int id, InvocationRequest originalRequest) {
		AbstractRepositoryManager suo = SystemConfiguration.getSUORepository();
		AbstractRepositoryManager sr = SystemConfiguration.getRepository();
		RequestManager requestManager = new RequestManager();
		
		Service faultyService = originalRequest.getService();
		
		suo.addService(faultyService.getURI());
		sr.remove(faultyService.getURI());
		
		DiscoveryRequest dr = new DiscoveryRequest();
		dr.setId(id);
		
		Map<String,List<URI>> map = new HashMap<String,List<URI>>();
		List<URI> inputs = new ArrayList<URI>();
		for (Input in : faultyService.getInputs()) {
			inputs.add(in.getType());
		}
		map.put("inputs", inputs);
		
		List<URI> outputs = new ArrayList<URI>();
		for (Output out : faultyService.getOutputs()) {
			outputs.add(out.getType());
		}
		map.put("outputs", outputs);
		
		dr.setServiceDescriptions(map);
		
		DiscoveryResponse response = requestManager.manageRequest(dr);
		
		response.getService();
		
		InvocationRequest ir = new InvocationRequest();
		ir.setId(id);
		ir.setInputs(originalRequest.getInputs());
		ir.setService(response.getService());
		
		return requestManager.manageRequest(ir);
		
	}

}
