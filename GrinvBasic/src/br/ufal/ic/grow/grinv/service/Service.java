package br.ufal.ic.grow.grinv.service;

import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mindswap.owl.OWLIndividual;

import br.ufal.ic.grow.grinv.exception.ServiceInvocationException;
import br.ufal.ic.grow.grinv.service.parameters.Input;
import br.ufal.ic.grow.grinv.service.parameters.Output;
import br.ufal.ic.grow.grinv.service.parameters.Parameter;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * Generic Service Class
 * @author Heitor Barros
 * @version 0.1
 *
 */
@XStreamAlias("service")
public abstract class Service implements Serializable{
	
	@XStreamAlias("Uri")
	protected URI uri;
	@XStreamImplicit(itemFieldName="Inputs")
	protected List<Input> inputs;
	@XStreamImplicit(itemFieldName="Outputs")
	protected List<Output> outputs;
	
	///////////////////////////////////
	/// METHODS
    ///////////////////////////////////
	

	public abstract Map<Parameter, OWLIndividual> invoke(int id, Map<Parameter, OWLIndividual> map) throws ServiceInvocationException;
	
	
	
	///////////////////////////////
	//// CONSTRUCTORS
	///////////////////////////////
	
	public Service(URI uri) {
		super();
		this.uri = uri;
		this.inputs = new ArrayList<Input>();
		this.outputs = new ArrayList<Output>();
	}

	public Service(URI uri, List<Input> inputs, List<Output> outputs) {
		super();
		this.uri = uri;
		this.inputs = inputs;
		this.outputs = outputs;
	}

	public Service() {
		super();
		uri = URI.create("http://localhost:8080/LearningServices/owls/newService.owl");
		this.inputs = new ArrayList<Input>();
		this.outputs = new ArrayList<Output>();
	}	
	
	////////////////////////////////
	/// GETTERS AND SETTERS
	/////////////////////////////////
	
	
	public URI getURI() {
		return uri;
	}

	public void setURI(URI uri) {
		this.uri = uri;
	}

	public List<Input> getInputs() {
		if (inputs==null) {
			return new ArrayList<Input>();
		}
		return inputs;
	}

	public void setInputs(List<Input> inputs) {
		this.inputs = inputs;
	}

	public List<Output> getOutputs() {
		if (outputs == null) {
			return new ArrayList<Output>();
		}
		
		return outputs;
	}

	public void setOutputs(List<Output> outputs) {
		this.outputs = outputs;
	}
	
	@Override
	public boolean equals(Object obj) {
		try{
			Service s = (Service)obj;
			if (this.getURI().equals(s.getURI())) {
				return true;
			}
		}catch (Exception e) {
			return false;
		}
		return false;
	}
	
	public Input getInput(String id){
		
		for (Input input : this.inputs) {
			if (input.getId().equals(id)) {
				return input;
			}
		}
		
		return null;
		
	}
	
	public Output getOutput(String id){
		
		for (Output output : this.outputs) {
			if (output.getId().equals(id)) {
				return output;
			}
		}
		
		return null;
		
	}
	
	public void addInput(Input i){
		this.inputs.add(i);
	}
	
	public void addOutput(Output o){
		this.outputs.add(o);
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.getURI().hashCode()+this.getInputs().hashCode()+this.getOutputs().hashCode();
	}
	
}
