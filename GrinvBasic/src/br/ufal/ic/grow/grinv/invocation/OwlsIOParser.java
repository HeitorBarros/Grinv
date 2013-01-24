package br.ufal.ic.grow.grinv.invocation;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLType;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.profile.Profile;

import br.ufal.ic.grow.grinv.service.Service;
import br.ufal.ic.grow.grinv.service.parameters.Parameter;

/**
 * Class responsible for parsing inputs sent by user and the inputs from service that will be invoked
 * 
 * @author Heitor Barros
 * @version 0.1
 *
 */
public class OwlsIOParser{
	
	/**
	 * Method that parse the inputs from user
	 * @param inputs
	 * @param service
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map parseInputs(List<URI> inputs, URI service){
		OWLKnowledgeBase kb = OWLFactory.createKB();
		Map<Input, Input> map = new HashMap<Input, Input>();
		
		List<Input> inputsList = new ArrayList<Input>();
		for (URI input : inputs) {
			
			try {
				StringTokenizer token = new StringTokenizer(input.toString());
				String s = token.nextToken("#");
				kb.read(new URI(s));
				System.out.println(s);
				OWLIndividual individual = kb.getIndividual(input);
				System.out.println(individual.getType());
				OWLType type = kb.getType(new URI(individual.getType().toString()));

				kb.read(type.getURI());
				Input n = kb.createInput(individual.getURI());
				n.setParamType(type.getURI());
				inputsList.add(n);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		Profile profile;
		try {
			profile = kb.readService(service).getProfile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} 
		
		for (Input inputService : profile.getInputs()) {
			for (Input inputRequisition : inputsList) {
				if (inputRequisition.getParamType().equals(inputService.getParamType())) {
					map.put(inputService, inputRequisition);
					inputsList.remove(inputRequisition);
					break;
				}
			}
		}
		
		return map;
	}
	
	
	public static Map<Parameter, Object> castToInputs(Map<Parameter, Object> m) throws URISyntaxException{
	
		Map<Parameter, Object> parsedMap = new HashMap<Parameter, Object>();
		OWLKnowledgeBase kb = OWLFactory.createKB();
		for (Parameter p : m.keySet()) {
			OWLIndividual o = (OWLIndividual) m.get(p);
			
			
			
			kb = o.getKB();
			
			parsedMap.put(p, o);
			
			//kb.createInput();
			
		}
		
		return parsedMap;
	}
}
