package br.ufal.ic.grow.grinv.discovery;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.profile.Profile;

/**
 * Class that creates a owl-s description based in the descriptions sent by Grinv user
 * 
 * @author Heitor Barros
 * @version 0.1
 *
 */
public class OwlsDescriptionGenerator {
	
	/**
	 * Method that creates a service description
	 * @param descriptions
	 * @return
	 * @throws IOException
	 */
	public static Profile createService(Map<String,List<URI>> descriptions) throws IOException{
		//creating kb
		OWLKnowledgeBase kb = OWLFactory.createKB();
		//creating an example URI
		URI uri= URI.create("www.example.org");
		List<URI> inputs =  extractIntputs(descriptions);
		List<URI> outputs =  extractOutputs(descriptions);
		
		Profile profile = kb.createProfile(uri);
		
		//creating inputs description
		if (inputs!=null) {
			for (URI inputUri : inputs) {
				kb.read(inputUri);
				OWLIndividual instance = kb.createInstance(kb.createClass(inputUri), URI.create(inputUri+"Temp"));
				Input input = kb.createInput(instance.getURI());
				input.setParamType(inputUri);
				profile.addInput(input);
			}
		}
		
		// creating outputs description
		if (outputs!=null) {
			for (URI outputUri : outputs) {
				kb.read(outputUri);
				OWLIndividual instance = kb.createInstance(kb.createClass(outputUri), URI.create(outputUri+"Temp"));
				Output output = kb.createOutput(instance.getURI());
				output.setParamType(outputUri);
				profile.addOutput(output);
			}
		}
		
		
		
		return profile;
		
	}

	private static List<URI> extractOutputs(Map<String, List<URI>> descriptions) {
		return descriptions.get("outputs");
	}

	private static List<URI> extractIntputs(Map<String, List<URI>> descriptions) {
		return descriptions.get("inputs");
	}

}
