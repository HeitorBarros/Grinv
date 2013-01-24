package br.ufal.ic.grow.grinv.discovery;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividualList;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLType;
import org.mindswap.owls.process.variable.Input;
import org.mindswap.owls.process.variable.Output;
import org.mindswap.owls.profile.Profile;
import org.mindswap.owls.service.Service;

import br.ufal.ic.grow.grinv.service.parameters.Parameter;
import br.ufal.ic.grow.grinv.utils.logger.GrinvLogger;

/**
 * Class responsible for discovery process, this class has a simple matching algorithm
 * 
 * @author Heitor Barros
 * @version 0.1
 *
 */
public class DefaultMatchmaker extends AbstractMatchmaker {

	/**
	 * Method responsible for execute the discovery process
	 */
	public br.ufal.ic.grow.grinv.service.Service discover(int id, Map<String, List<URI>> descriptions, List<br.ufal.ic.grow.grinv.service.Service> services) {
		
		String log = "\n- - - - - -\nStarting Discovery Process\n";
		
		Profile profile;
		OWLKnowledgeBase kb = OWLFactory.createKB();
		
		try {
			//creating a Owl-s description based in descriptions sent by Grinv user
			profile = OwlsDescriptionGenerator.createService(descriptions);
		} catch (IOException e) {
			log+="Descriptions could not be used\n";
			GrinvLogger.info(id, log);
			return null;
		}
		
		//Search the most similar service in the services list
		for (br.ufal.ic.grow.grinv.service.Service s : services) {
			URI uri = s.getURI();
			try {
				Service aService = kb.readService(uri);
				Profile aProfile = aService.getProfile();
				
				//comparing inputs and outputs
				if (this.compareListIn(aProfile.getInputs(), profile.getInputs())&&this.compareListOut(aProfile.getOutputs(), profile.getOutputs())) {
					log+="Service Found: "+ uri+"\n";
					GrinvLogger.info(id, log);
					//returning service URI
					return new br.ufal.ic.grow.grinv.service.OWLSService(uri);
				}
				
				
			} catch (IOException e) {
			}
		}
		
	
		log+="No Services Found!\n";
		return null;
	}
	
	/**
	 * method that compare outputs of two services
	 * @param a outputs of service A
	 * @param b outputs of service B
	 * @return
	 */
	private boolean compareListOut(OWLIndividualList<Output> a,
			OWLIndividualList<Output> b) {
		//verify size of two lists
		if (a.size()!= b.size()) {
			return false;
		}
		
		List<OWLType> typesb= new ArrayList<OWLType>();
		List<OWLType> typesa= new ArrayList<OWLType>();
		
		//extracting outputs types
		for (Output output : b) {
			typesb.add(output.getParamType());
		}

		for (Output output : a) {
			typesa.add(output.getParamType());
		}
		
		//matching parameters
		for (OWLType owlType : typesa) {
			if (!typesb.contains(owlType)) {
				return false;
			}
			typesb.remove(owlType);
		}
		return true;
	}

	/**
	 * method that compare inputs of two services
	 * @param a inputs of service A
	 * @param b inputs of service B
	 * @return
	 */
	private boolean compareListIn(OWLIndividualList<Input> a, OWLIndividualList<Input> b){
		//verify size of two lists
		if (a.size()!= b.size()) {
			return false;
		}
		
		List<OWLType> typesb= new ArrayList<OWLType>();
		List<OWLType> typesa= new ArrayList<OWLType>();
		//extracting inputs types
		for (Input input : b) {
			typesb.add(input.getParamType());
		}

		for (Input input : a) {
			typesa.add(input.getParamType());
		}
		
		//matching parameter
		for (OWLType owlType : typesa) {
			if (!typesb.contains(owlType)) {
				return false;
			}
			typesb.remove(owlType);
		}
		return true;
	}
}