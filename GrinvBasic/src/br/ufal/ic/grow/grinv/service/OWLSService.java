package br.ufal.ic.grow.grinv.service;

import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.mindswap.owl.OWLIndividual;

import br.ufal.ic.grow.grinv.exception.ServiceInvocationException;
import br.ufal.ic.grow.grinv.invocation.InvocationFactory;
import br.ufal.ic.grow.grinv.invocation.OWLSInvocationFactory;
import br.ufal.ic.grow.grinv.service.parameters.Parameter;
import br.ufal.ic.grow.grinv.utils.logger.GrinvLogger;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * Grinv Service Class
 * @author Heitor Barros
 * @version 0.1
 *
 */
@XStreamAlias("service")
public class OWLSService extends Service implements Serializable {
	/**
	 * Inputs Classes
	 */
	@XStreamImplicit(itemFieldName="inputsParents")
	private List<URI> inputsParents;
	/**
	 * Outputs Classes
	 */
	@XStreamImplicit(itemFieldName="outputsParents")
	private List<URI> outputsParents;
	/**
	 * Classification Classes
	 */
	@XStreamImplicit(itemFieldName="ClassificationClasses")
	private List<URI> classificationClasses;
	/**
	 * Constructor
	 * @param uri
	 * @param inputsClasses
	 * @param outputsClasses
	 * @param classificationClasses
	 */
	public OWLSService(URI uri, List<URI> inputsClasses,
			List<URI> outputsClasses, List<URI> classificationClasses) {
		super(uri);
		this.inputsParents = inputsClasses;
		this.outputsParents = outputsClasses;
		this.classificationClasses = classificationClasses;
	}
	/**
	 * Constructor
	 */
	public OWLSService() {
		super();
	}
	/**
	 * Constructor
	 * @param uri
	 */
	public OWLSService(URI uri) {
		super(uri);
	}
	public List<URI> getInputsClasses() {
		return inputsParents;
	}
	public void setInputsClasses(List<URI> inputClasses) {
		this.inputsParents = inputClasses;
	}
	public List<URI> getOutputsClasses() {
		return outputsParents;
	}
	public void setOutputsClasses(List<URI> outputClasses) {
		this.outputsParents = outputClasses;
	}
	public List<URI> getClassificationClasses() {
		return classificationClasses;
	}
	public void setClassificationClasses(List<URI> classificationClasses) {
		this.classificationClasses = classificationClasses;
	}
	
	
	
	@Override
	public Map<Parameter, OWLIndividual> invoke(int id, Map<Parameter, OWLIndividual> inputs) throws ServiceInvocationException {
		
		GrinvLogger.info(id, "\n\n-------------------------------\n- Starting Service Invocation -\n-------------------------------\n");
		GrinvLogger.info(id, "Service: "+this.uri+"\n");
		
		for (Parameter p : inputs.keySet()) {
			GrinvLogger.info(id, "Parameter: "+inputs.get(p).toRDF(false, false)+"\n");
		}
		InvocationFactory factory = new OWLSInvocationFactory();
		return factory.getInvocationEngine().invokeService(id, this, inputs);
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode()+this.getInputsClasses().hashCode()+this.getOutputsClasses().hashCode();
	}
	
}
