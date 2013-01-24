package br.ufal.ic.grow.grinv.service.composite;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mindswap.owl.OWLClass;
import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLIndividual;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owls.process.variable.Input;

import br.ufal.ic.grow.grinv.configuration.SystemConfiguration;
import br.ufal.ic.grow.grinv.exception.ServiceInvocationException;
import br.ufal.ic.grow.grinv.invocation.InvocationEngine;
import br.ufal.ic.grow.grinv.invocation.InvocationFactory;
import br.ufal.ic.grow.grinv.invocation.OWLSInvocationFactory;
import br.ufal.ic.grow.grinv.invocation.OwlsIOParser;
import br.ufal.ic.grow.grinv.repository.AbstractRepositoryManager;
import br.ufal.ic.grow.grinv.service.Service;
import br.ufal.ic.grow.grinv.service.OWLSService;
import br.ufal.ic.grow.grinv.service.parameters.Output;
import br.ufal.ic.grow.grinv.service.parameters.Parameter;
import br.ufal.ic.grow.grinv.service.parameters.ParametersMapping;
import br.ufal.ic.grow.grinv.utils.logger.GrinvLogger;

public class SequenceComposition extends CompositeService {

	private Map<Integer, Service> serviceSequence;
	
	
	
	public SequenceComposition() {
		super();
		serviceSequence = new HashMap<Integer, Service>();
	}


	public SequenceComposition(List<Service> services) {
		super(services);
		serviceSequence = new HashMap<Integer, Service>();
		int i = 1;
		for (Service service : services) {
			serviceSequence.put(i, service);
			i++;
		}
	}
	
	public void addService(Service s){
		this.appendService(s);
	}
	
	public void appendService(Service s){
		this.services.add(s);
		int size = serviceSequence.keySet().size();
		serviceSequence.put(size+1, s);
		
	}
	

	@Override
	public Map<Parameter, OWLIndividual> invoke(int id, Map<Parameter, OWLIndividual> map) throws ServiceInvocationException {
		
		GrinvLogger.info(id, "\n\n-------------------------------\n- Starting Service Invocation -\n-     Composite Service       -\n-------------------------------\n");
		
		GrinvLogger.info(id, "\n"+this.getCompleteDescription()+"\n");
		
		Map<Parameter, OWLIndividual> allParameters = new HashMap<Parameter, OWLIndividual>();
		allParameters.putAll(map);
		
		Map<Parameter, OWLIndividual> serviceParameters = null;
		
		for (int i = 1; i <= this.serviceSequence.size(); i++) {
			
			serviceParameters = new HashMap<Parameter, OWLIndividual>();
			
			Service s = serviceSequence.get(i);
			
			
			// Procurando os Mapeamentos necessários para invocação do serviço s
			for (br.ufal.ic.grow.grinv.service.parameters.Input input : s.getInputs()) {
				for (ParametersMapping m : this.mapping) {
					if (m.getInput().getService().getURI().equals(s.getURI()) && m.getInput().getId().equals(input.getId())) {
						Parameter p = m.getOutput();
						serviceParameters.put(m.getInput(), allParameters.get(p));
					}
				}
			}
			
			Map<Parameter, OWLIndividual> result = s.invoke(id, serviceParameters);
			//System.out.println("service invoked");
			
			allParameters.putAll(result);

			
		}
		
//		System.out.println("oi");
//		
//		for (Parameter p : allParameters.keySet()) {
//			System.out.println(".."+p.getId());
//			System.out.println("..."+((OWLIndividual)allParameters.get(p)).toRDF(true, true));
//		}
		Map<Parameter, OWLIndividual> ret = new HashMap<Parameter, OWLIndividual>();
		for (Output out : this.getOutputs()) {
			Parameter p = new Parameter(this, out.getId());
			for (ParametersMapping m : this.mapping) {
				if (m.getInput().equals(p)) {
					ret.put(p, allParameters.get(m.getOutput()));
				}
			}
			
//			ret.put(p, allParameters.get(p));
		}
		
		return ret;
		
	}
	
	
	
	@Override
	public String toString() {
		String ret = "";
		for (Integer i : serviceSequence.keySet()) {
			ret+="Service "+i+": "+serviceSequence.get(i).getURI()+"\n";
		}
		return ret;
	}
	
	public String getCompleteDescription() {
		String ret = ">SERVICES: \n";
		for (Integer i : serviceSequence.keySet()) {
			ret+="Service "+i+": "+serviceSequence.get(i).getURI()+"\n";
		}
		ret+="\n>MAPPINGS: \n";
		
		for (ParametersMapping m : this.mapping) {
			ret+= m.getOutput().getService().getURI()+"("+m.getOutput().getId()+") -> "+m.getInput().getService().getURI()+"("+m.getInput().getId()+")\n"; 
		}
		
		
		return ret;
	}
	
	
	public static void main(String[] args) throws URISyntaxException, ServiceInvocationException {
		ParametersMapping map = new ParametersMapping();
		
		AbstractRepositoryManager repo = SystemConfiguration.getRepository();
		
		List<Service> services = repo.loadAllServices();
		
		int i = 0;
		for (Service service : services) {
			System.out.println(i);
			i++;
			System.out.println(service.getURI());
		}
		
		OWLSService explanation = (OWLSService) services.get(2);
		OWLSService concepts = (OWLSService) services.get(1);
		
		map.setInput(new Parameter(explanation, explanation.getInputs().get(0).getId()));
		map.setOutput(new Parameter(concepts, concepts.getOutputs().get(0).getId()));
		
		SequenceComposition comp = new SequenceComposition();
		
		br.ufal.ic.grow.grinv.service.parameters.Input inp = new br.ufal.ic.grow.grinv.service.parameters.Input("InpComposite", new URI("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Problem"));
		comp.addInput(inp);
		Output out = new Output("OutComposite", new URI("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Exaplanation"));
		comp.addOutput(out);
		
		
		comp.appendService(concepts);
		comp.appendService(explanation);
		comp.addMapping(map);
		ParametersMapping m1 = new ParametersMapping();
		m1.setInput(new Parameter(concepts, concepts.getInputs().get(0).getId()));
		m1.setOutput(new Parameter(comp, comp.getInputs().get(0).getId()));
		
		ParametersMapping m2 = new ParametersMapping();
		m2.setInput(new Parameter(comp, comp.getOutputs().get(0).getId()));
		m2.setOutput(new Parameter(explanation, explanation.getOutputs().get(0).getId()));
		
		comp.addMapping(m1);
		comp.addMapping(m2);
		
		
		OWLKnowledgeBase kb = OWLFactory.createKB();
		
		URI inputURI = URI.create("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Problem");
		Map<Parameter, OWLIndividual> map2 = new HashMap<Parameter, OWLIndividual>();
		
		OWLClass c = kb.createClass(new URI("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Problem"));
		OWLIndividual estudante = kb.createInstance(c, URI.create("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl#Problem_2"));
		
		Parameter p = new Parameter(comp, comp.getInputs().get(0).getId());
		map2.put(p, estudante);
		
		System.out.println(comp.getCompleteDescription());
		
		Map<Parameter, OWLIndividual> oMap = comp.invoke(-1,map2);
		
		
		for (Object o : oMap.keySet()) {
			System.out.println(((OWLIndividual)oMap.get(o)).toRDF(true, true));
		}
		
	}

	
	
	
}
