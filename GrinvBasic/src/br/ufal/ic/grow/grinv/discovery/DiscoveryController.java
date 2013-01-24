package br.ufal.ic.grow.grinv.discovery;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import br.ufal.ic.grow.grinv.repository.RepositoryFacade;
import br.ufal.ic.grow.grinv.service.Service;
import br.ufal.ic.grow.grinv.utils.logger.GrinvLogger;


/**
 * Class that controls the discovery process
 * 
 * @author Heitor Barros
 * @version 0.1
 *
 */
public class DiscoveryController {

	/**
	 * description validator
	 */
	protected DescriptionValidator descriptionValidator;
	/**
	 * discovery algorithm
	 */
	protected Map<Integer, AbstractMatchmaker> algorithms;
	/**
	 * repository manager
	 */
	protected RepositoryFacade repositoryManager;
	
	/**
	 * Constructor	
	 */
	public DiscoveryController() {
		super();
		this.descriptionValidator = new DescriptionValidator();
		this.algorithms = MatchmakerFactory.createMatchmaker();
		this.repositoryManager = new RepositoryFacade();
	}



	/**
	 * Method that controls the discovery process
	 * @param id Request ID
	 * @param map Map with descriptions
	 * @return
	 */
	public Service discover(int id, Map<String, List<URI>> map){
		GrinvLogger.info(id, "\n------------------------------\n- Starting Discovery Process -\n------------------------------\n\n");
		
		//validate descriptions
		Map<String, List<URI>> selectDesc = descriptionValidator.validateDescriptions(map);
		// load services
		List<Service> services = repositoryManager.loadServices();
		
		
		//Sort priorities
		Vector<Integer> prioridades = new Vector<Integer>();
		for (Integer p : this.algorithms.keySet()) {
			 prioridades.add(p);
		}
		java.util.Arrays.sort(prioridades.toArray());

		//select matchmaker
		AbstractMatchmaker matchmaker;
		Service serviceFound = null;
		for (Integer p :prioridades ) {
			System.out.println(p);
			matchmaker = this.algorithms.get(p);
			System.out.println(matchmaker.getClass().toString());
			GrinvLogger.info(id, "Searching service with: "+matchmaker.getClass()+"\n\n");
			serviceFound = matchmaker.discover(id, selectDesc, services);
			if(serviceFound != null){
				return serviceFound;
			}else{
				GrinvLogger.info(id, "No Service Found\n\n\n");
			}
		}
		
		return serviceFound;
	}
}