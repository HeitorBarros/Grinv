package br.ufal.ic.grow.grinv.invocation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.mindswap.exceptions.ExecutionException;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLValue;
import org.mindswap.owls.OWLSFactory;
import org.mindswap.owls.process.execution.ProcessExecutionEngine;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.service.Service;
import org.mindswap.query.ValueMap;

import br.ufal.ic.grow.grinv.repository.GrinvOntManager;
import br.ufal.ic.grow.grinv.service.parameters.Parameter;
import br.ufal.ic.grow.grinv.utils.logger.GrinvLogger;

public class OWLSInvocationEngine extends InvocationEngine {

		@Override
	public Map<Parameter, OWLIndividual> invokeService(int id, br.ufal.ic.grow.grinv.service.Service service, Map<Parameter, OWLIndividual> inputs) {
 
//		System.out.println("Parsing Inputs");
//		for (Parameter p : inputs.keySet()) {
//			System.out.println(p);
//		}
//		OwlsIOParser parser = new OwlsIOParser();
//		List<URI> inpURI = new ArrayList<URI>();
//		for (Parameter p : inputs.keySet()) {
//			OWLIndividual i = (OWLIndividual) inputs.get(p);
//
//			//inpURI.add(i.getURI());
//		}
		//Map parsedInputs = parser.parseInputs(inpURI, service.getUri());
		
		
		
		String log = "";
		log+= "\nStarting service invocation:\n";
		log+= "service: "+service.getURI()+"\n";
		
		GrinvOntManager OM = new GrinvOntManager();
		OWLKnowledgeBase kb;
		try {
			kb = OM.createBasicKB();
			
			kb = OM.addToKB(kb, service.getURI());			
		} catch (IOException e1) {
			kb = OWLFactory.createKB();
		}
		ProcessExecutionEngine exec = OWLSFactory.createExecutionEngine();
		Service s;
		
		try {
//			kb.setReasoner("Pellet");
			s = kb.readService(service.getURI());
			//OM.addOntology(kb);
		} catch (IOException e) {
			log+="Service could not be read\n";
			GrinvLogger.info(1, log);
			
			e.printStackTrace();
			return null;
		}
		
		ValueMap<Input, OWLValue> inputMap = new ValueMap<Input, OWLValue>();
//		for (Object input : parsedInputs.keySet()) {
//			Input i = (Input)input;
//			inputMap.setValue(i, (OWLIndividual)parsedInputs.get(input));
//		}
		
		for (Input i : s.getProfile().getInputs()) {
			
			for (Parameter p : inputs.keySet()) {
				if (p.getId().equals(i.getURI().toString())) {
					OWLIndividual ind = (OWLIndividual)inputs.get(p);
					if (ind.getURI()!=null) {
						ind = kb.createInput(ind.getURI());
					}
					
					inputMap.setValue(i, ind);

				}
			}
		}
		
		try {
			
			ValueMap<Output, OWLValue> outputMap = exec.execute(s.getProcess(), inputMap, kb);
			
			Map<Parameter, OWLIndividual> results = new HashMap<Parameter, OWLIndividual>();
			for (Output output : s.getProcess().getOutputs()) {
				
				
				OWLIndividual ind = (OWLIndividual) outputMap.getValue(output);
				log += "output: "+ ind.toRDF(false, false);
				results.put(new Parameter(service, output.getURI().toString()), ind);
			}
			
			log+="Invocation done!\n\n";
			GrinvLogger.info(id, log);
			
			return results;
		} catch (ExecutionException e) {
			e.printStackTrace();
			return null;
		}
	}

		
}
