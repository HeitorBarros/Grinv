package br.ufal.ic.grow.grinv.discovery;

import java.net.URI;
import java.util.List;
import java.util.Map;

import br.ufal.ic.grow.grinv.service.Service;

/**
 * Class that represents an abstract matchmaker, discovery extensions should extends this class 
 * 
 * @author Heitor Barros
 * @version 0.1
 * 
 */
public abstract class AbstractMatchmaker {

	/**
	 * Abstract method that represents a discovery process
	 * @param id Request ID
	 * @param selectDesc Descriptions sent by Grinv user
	 * @param services set of Services
	 * @return
	 */
    public abstract Service discover(int id, Map<String, List<URI>> selectDesc, List<Service> services);

}