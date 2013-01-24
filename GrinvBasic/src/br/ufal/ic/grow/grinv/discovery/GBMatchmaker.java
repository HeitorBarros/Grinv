package br.ufal.ic.grow.grinv.discovery;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import br.ufal.ic.grow.grinv.discovery.similarity.CossineSimilarity;
import br.ufal.ic.grow.grinv.service.OWLSService;
import br.ufal.ic.grow.grinv.service.Service;
import br.ufal.ic.grow.grinv.service.ServiceList;
import br.ufal.ic.grow.grinv.service.composite.SequenceComposition;
import br.ufal.ic.grow.grinv.service.parameters.Input;
import br.ufal.ic.grow.grinv.service.parameters.Output;
import br.ufal.ic.grow.grinv.service.parameters.Parameter;
import br.ufal.ic.grow.grinv.service.parameters.ParametersMapping;
import br.ufal.ic.grow.grinv.utils.logger.GrinvLogger;
import br.ufal.ic.tcc.graph.Dijkstra;
import br.ufal.ic.tcc.graph.Edge;
import br.ufal.ic.tcc.graph.Graph;
import br.ufal.ic.tcc.graph.Path;
import br.ufal.ic.tcc.graph.Vertex;



/**
 * 
 * @author Heitor Barros
 * @author Ivo Augusto
 * 
 */

public class GBMatchmaker extends AbstractMatchmaker{

	// Constants ------------------------------------------------------

	public static final int EXACT = 0;

	public static final double FAIL = 1.0;

	public final double DESVIO = 0.05;

	// Attributes ------------------------------------------------------

	// private Graph<Service> graph;

	// Static ----------------------------------------------------------

	private static URI baseURI;

//	private GrinvLogger logger;
	
	private int id;
	
	
	
	private final static String USER_PATH = "user.home";
	
	private static final String REPOSITORY_PATH = "/Grinv/repository/";

	private Graph graph;

	static {
		
	}

	// Constructor -----------------------------------------------------

	public GBMatchmaker(String bURI) throws URISyntaxException {
		GBMatchmaker.baseURI = new URI(bURI);
	}

	// Public ----------------------------------------------------------
		
	/**
	 * 
	 */
	public GBMatchmaker() throws URISyntaxException {
		this("http://localhost:8080/sws/composite.owl");
	}

	
	
	/**
	 * Adiciona um servico ao grafo de servicos disponiveis.
	 * 
	 * @param graph
	 * @param service
	 * @throws Exception
	 */
	public void addToAdvertisementGraph(Graph graph, Service service)
			throws Exception {
		
		List<Vertex> v = graph.getVertices();
		Vertex vser = new Vertex(service);

		for (Vertex vertex : v) {
			 
			List<URI> in2 = OWLSService.class.cast(vser.getContent()).getInputsClasses();
			List<URI> out1 = OWLSService.class.cast(vertex.getContent()).getOutputsClasses();
			
			
			double value = canFollow(out1, in2);
			
//			for (URI uri : out1) {
//				System.out.println(uri);
//			}
//			for (URI uri : in2) {
//				System.out.println(uri);
//			}
//			System.out.println(value);
			
			if (value < 1.0){
				vser.adicionarAresta(new Edge(vser, vertex, value));
				//graph.addEdge(new Edge(vser, vertex, value));
			}
				


			List<URI> in1 = OWLSService.class.cast(vertex.getContent()).getInputsClasses();
			List<URI> out2 = OWLSService.class.cast(vser.getContent()).getOutputsClasses();
			
			double value2 = canFollow(out2, in1);
			
			Edge ed = new Edge(vertex, vser, value2);

//			System.out.println("-"+vertex.getContent().getURI());
//			System.out.println("-"+vser.getContent().getURI());
//			
//			System.out.println(ed.getDestino());
//			
			if (value2 < 1.0){
				vertex.adicionarAresta(new Edge(vertex, vser, value2));
			}
				//graph.addEdge(new Edge(vertex, vser, value2));

		}

		graph.addVertex(vser);
	}

	/**
	 * 
	 * @param services
	 * @return
	 * @throws Exception
	 */
	public Graph constructAdvertisementGraph(List<Service> services)
			throws Exception {
		Graph graph = new Graph();

		GrinvLogger.info(id, "Building Graph\n");
		int i = 1;
		for (Service s : services) {
			try {
				//System.out.println("."+s.getURI());

				//System.out.println(service);
				
				GrinvLogger.info(id,"Adding Service "+i+++": " + s.getURI() + "\n");
				GrinvLogger.info(id, "> Inputs: \n");
				for (Input input : s.getInputs()) {
					GrinvLogger.info(id, input.getType()+"\n");
				}
				GrinvLogger.info(id, "> Outputs: \n");
				for (Output output : s.getOutputs()) {
					GrinvLogger.info(id, output.getType()+"\n");
				}
				//logger.info(id, "> Classification: \n"+service.getProfile().getServiceClassification()+"\n\n");
				
				addToAdvertisementGraph(graph, s);

				// graph.addVertex(new Vertex<Service>(service));
			} catch (FileNotFoundException e) {
				GrinvLogger.info(id, "Nao foi possivel ler o servico: " + s);
				e.printStackTrace();
			}
		}

		this.persistGraph(graph);
		
		return graph;
	}


	/**
	 * Efetua o match de servicos
	 * 
	 * @param graph
	 * @param requestService
	 * @param filter
	 * @return
	 * @throws Exception
	 */
	public Service match(final Graph graph, final Service requestService) throws Exception {
		
		System.out.println("Starting discovery process");
		System.out.println("Graph-based Composition Approach");
		
		// Sending informations to log file
		GrinvLogger.info(id, " - - - Executing Direct Matching - - -\n\n REQUEST: \n> Inputs: \n");
		for (Input input : requestService.getInputs()) {
			GrinvLogger.info(id, input.getType()+"\n");
		}
		GrinvLogger.info(id, "> Outputs:\n");
		for (Output output : requestService.getOutputs()) {
			GrinvLogger.info(id, output.getType()+"\n");
		}
		
		// Creating maps to store the similarity between the inputs and output
		// of each service and the request.
		Map<Vertex, Double> inputs = new HashMap<Vertex, Double>();
		Map<Vertex, Double> outputs = new HashMap<Vertex, Double>();

		// comparing the input and output values of all services
		// with the request and put the result in the map
		
		double value_direct = GBMatchmaker.FAIL, 
				similarityValue;
		Service result_direto = null;
		
		for (Vertex vt : graph.getVertices()) {
			// Initializing value
			similarityValue = 0;
			//getting service from graph
			Service currentServiceFromGraph = vt.getContent();
			
			GrinvLogger.info(id,"Comparing Request Service and Service: "
					+ currentServiceFromGraph.getURI() + "\n");
			
			// comparing outputs of a Service and the request
			double valueOuts, valueIns = 0.0;
			valueOuts = outputMatch(currentServiceFromGraph, requestService);
			outputs.put(vt, valueOuts);
			similarityValue += valueOuts;
			GrinvLogger.info(id, "Outputs Similarity: "+ valueOuts+"\n");

				
			// comparing inputs of a Service and the request
			valueIns = inputMatch(currentServiceFromGraph, requestService);
			inputs.put(vt, valueIns);
			similarityValue += valueIns;
			GrinvLogger.info(id, "Inputs Similarity: "+ valueIns+"\n");					
			
			// calculating the mean of inputs and outputs similarities
			similarityValue = similarityValue/2;
			GrinvLogger.info(id, "Final Services Similarity: "+ similarityValue +"\n\n");
			
			// verifying if the current service is most similar 
			// than the previous analysed services
			if(similarityValue  < value_direct){
				result_direto = currentServiceFromGraph;
				value_direct = similarityValue;
			}
		}

		// finding the shortest path for indirect matching
		Path foundPath = findIndirectMatching(graph, requestService);
		
		// verifying if there is a found path, if it does not exists the direct matching result will be choosen
		if(foundPath == null){
			if (result_direto != null) {
				GrinvLogger.info(id, "\n - - - SERVICE DISCOVERED - - -\nDirect Matching:" + result_direto.getURI() + "\nSimilarity: "
						+ value_direct);
				return result_direto;
			}
			// if there is not a path (indirect matching) or a result direct (direct matching)
			// there are no services in repository that fits with the user's request
			GrinvLogger.info(id,"Servico não encontrado!!!");
			return null;
		}
		
		
		double v_indireto = foundPath.getWeight();
		if (value_direct <= v_indireto) {
			GrinvLogger.info(id, "\n - - - SERVICE DISCOVERED - - -\nDirect Matching:" + result_direto.getURI() + "\nSimilarity: "
					+ value_direct);
			return result_direto;
		}

		//List<Vertex<Service>> finalPath = paths.get(flag).getVt();
		List<Vertex> finalPath = foundPath.getVt();
		List<Service> chainOfServices = new LinkedList<Service>();
		String services = "";
		for (Vertex vertex : finalPath) {
			chainOfServices.add(vertex.getContent());
			services += vertex.getContent().getURI() + "\n";
		}

		Service result_indireto = createSequenceService(chainOfServices);
		
		GrinvLogger.info(id,"\n - - - SERVICE DISCOVERED - - -\nIndirect Matching:" + services + "\nSimilarity: "
				+ v_indireto);
//		logger.info("Matching Indireto:\nServiceRequest: " + request.getURI()
//				+ "\nServiceResult:\n" + services + "Similaridade: "
//				+ v_indireto);
		return result_indireto;

	}

	// Private --------------------------------------------------------

	private Path findIndirectMatching(Graph graph,
			Service request) throws Exception {

		List<Vertex> out = new LinkedList<Vertex>();
		List<Vertex> in = new LinkedList<Vertex>();
		for (Vertex v : graph.getVertices()) {
			if (inputMatch(request, v.getContent())<= (this.EXACT+this.DESVIO)) {
				in.add(v);
			}
			if(outputMatch(request, v.getContent())<= (this.EXACT+this.DESVIO)) {
				out.add(v);
			}
		}		
		
		
		List<Path> paths = new LinkedList<Path>();
		//adiciona todos os caminhos a lista de caminhos
		for (Vertex destino : out) {
			for (Vertex origem : in) {
				if (destino.getContent() != origem.getContent()) {
					Path path = graph.getPath(origem, destino);
					if (path != null){
						//System.out.println("_"+path.getWeight());
						paths.add(path);
					}
				}
			}
		}
		
		if(paths.size() == 0){
			return null;
		}
		
		int flag = 0;
		double v_indireto = Dijkstra.INFINITY;
		for (int i = 0; i < paths.size(); i++) {
			if (paths.get(i).getWeight() < v_indireto
					/*&& paths.get(i).getWeight() < filter + DESVIO*/) {
				flag = i;
				v_indireto = paths.get(i).getWeight();
			}
		}
		
		Path foundPath = paths.get(flag);
		GrinvLogger.info(id, " - - - Discovering Indirect Matching - - - \n\n");
		GrinvLogger.info(id, "Recommended Services Composition:\n");
		
		for (int i = 0; i< foundPath.getVt().size(); i++) {
			Vertex aService = foundPath.getVt().get(i);
			
			GrinvLogger.info(id, "Service"+(i+1)+": "+ aService.getContent().getURI()+"\n");
						
		}

		double compSimilarity = 0.0;
		double dInputs = inputMatch(request, foundPath.getVt().get(0).getContent());
		double dOutputs = outputMatch(request, foundPath.getVt().get(foundPath.getVt().size()-1).getContent());
		double[] dMid = new double[foundPath.getVt().size()];
		GrinvLogger.info(id, "InputRequest -("+dInputs + ")-> ");
		for (int i = 0; i < foundPath.getVt().size()-1; i++) {
			
			List<URI> out1 = OWLSService.class.cast(foundPath.getVt().get(i).getContent()).getOutputsClasses();
			List<URI> in2 = OWLSService.class.cast(foundPath.getVt().get(i+1).getContent()).getInputsClasses();
			
			dMid[i] = canFollow(out1, in2);
			compSimilarity+= dMid[i];
			GrinvLogger.info(id, " Service"+(i+1)+" -("+dMid[i]+")->");
		}
		
		
		GrinvLogger.info(id, " Service"+foundPath.getVt().size()+" -("+ dOutputs +")-> OutputRequest\n");
		
		compSimilarity+=dInputs+dOutputs;
		
		// fazendo a media das similaridades envolvidas na composição,
		// dividindo a soma das similaridades pelo tamanho de arestas
		// envolvidas da composição (dMid.lenght) somado por 2 (inputs e outputs)
		compSimilarity = compSimilarity / (2+dMid.length);
		
		GrinvLogger.info(id, "Composition Similarity: "+compSimilarity+"\n - - - - - -\n");
		foundPath.setWeight(compSimilarity);
		return foundPath;
		
	}

	/**
	 * 
	 */
	private double calculateSimilarity(Service request, Service aService) throws Exception{
		GrinvLogger.info(id, "Calculando a Similaridade entre o servico: "+ request.getURI()+" e "+ aService.getURI());
		int numParam = 2;
		double similaridadeParcial = 0, L;

		similaridadeParcial += this.outputMatch(request, aService);
		similaridadeParcial += this.inputMatch(request, aService);
		return similaridadeParcial/numParam;
		
	}
	
	
	
	
	/**
	 * Determina se dois servicos podem ser conectados. A analise eh feita
	 * verificando as entradas de um servico em relacao as saida do outro
	 * 
	 * @param s1
	 *            servico de entrada
	 * @param s2
	 *            servico de saida
	 * @return Peso da aresta
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private double canFollow(List<URI> s1, List<URI> s2)
			throws Exception {

		Vector<URI> inputs = new Vector<URI>();
		Vector<URI> outputs = new Vector<URI>();


		inputs.addAll(s1);
		outputs.addAll(s2);

		if (inputs.isEmpty() && outputs.isEmpty())
			return GBMatchmaker.EXACT;

		double r = new CossineSimilarity(s1, s2).calculateSimilarity(); 

		if (r < 0)
			return 0;
		return r;

	}

	/**
	 * Verifica se duas ontologias estao de alguma forma conectadas
	 * 
	 * @param clz1
	 *            entrada
	 * @param clz2
	 *            saida
	 * @return
	 */ 
	private double matchOntologies(List<URI> individual, List<URI> individual2) {
		try {
			return new CossineSimilarity(individual, individual2).calculateSimilarity();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return FAIL;
	}

	/**
	 * Cria um servico composto a partir de uma lista de servicos
	 * 
	 * @param services
	 * @return
	 */
	private Service createSequenceService(List<Service> services) {
		//TODO
		System.out.println("SEQUENCE COMPOSITION FOUND:");
		for (Service service : services) {
			System.out.println(service.getURI());
		}
		
		SequenceComposition comp = new SequenceComposition();
		
		try {
			comp.setURI(new URI("localhost:8080/Services/compositeService.owl"));
		} catch (URISyntaxException e) {}
		
		Service firstService, lastService;
		
		firstService = services.get(0);
		lastService = services.get(services.size()-1);
		
		comp.setInputs(firstService.getInputs());
		comp.setOutputs(lastService.getOutputs());
		
		List<Output> out;
		List<Input> in;
		
		for (int i = 0; i < services.size(); i++) {
			comp.addService(services.get(i));
			
			if (i<services.size()-1) {
				Service thisService = services.get(i);
				Service nextService = services.get(i+1);
				out = thisService.getOutputs();
				in = nextService.getInputs();
				Map<Input, Output> bindings = this.bindingInputAndOutputs(in, out);
				
				for (Input input : bindings.keySet()) {
					Parameter pin = new Parameter(nextService, input.getId());
					Parameter pout = new Parameter(thisService, bindings.get(input).getId());
					
					ParametersMapping mapping = new ParametersMapping(pin, pout);
					
					comp.addMapping(mapping);
				}
			}
		}
		
		Map<Input, Input> bindInp = this.bindingInputs(firstService.getInputs(), comp.getInputs());
		
		for (Input input : bindInp.keySet()) {
			Parameter pinFirst = new Parameter(firstService, input.getId());
			Parameter pinComp = new Parameter(comp, bindInp.get(input).getId());
			
			ParametersMapping mapping = new ParametersMapping(pinFirst, pinComp);
			
			comp.addMapping(mapping);
		}
		
		Map<Output, Output> bindOut = this.bindingOutputs(lastService.getOutputs(), comp.getOutputs());
		
		for (Output input : bindOut.keySet()) {
			Parameter poutLast = new Parameter(lastService, input.getId());
			Parameter poutComp = new Parameter(comp, bindOut.get(input).getId());
			
			ParametersMapping mapping = new ParametersMapping(poutComp, poutLast);
			
			comp.addMapping(mapping);
		}
		
		System.out.println(comp.getCompleteDescription());
		
 		
		return comp;
	}


	/**
	 * Metodo que efetua ligacao entre entradas e saidas de dois servicos. Faz
	 * uma iteracao nas entradas verificando qual saida melhor pode ser
	 * conectada e coloca o par em um map para retorno
	 * 
	 * @param inputs
	 * @param outputs
	 * @return map contendo os parametros ligados um a um
	 */
	@SuppressWarnings("unchecked")
	private Map<Input, Output> bindingInputAndOutputs(List<Input> inputs,
			List<Output> outputs) {
		
		Map<Input, Output> ret = new HashMap<Input, Output>();

		List<Input> listInputs = new LinkedList<Input>();
		List<Output> listOutputs = new LinkedList<Output>();

		listInputs.addAll(inputs);
		listOutputs.addAll(outputs);

		for (int i = 0; i < listInputs.size(); i++) {
			double tmpResult = FAIL;
			int flag = -1;
			for (int j = 0; j < listOutputs.size(); j++) {
				double rm = matchOntologies(listInputs.get(i).getType(), listOutputs
						.get(j).getType());
				if (rm <= tmpResult) {
					tmpResult = rm;
					flag = j;
				}
			}

			ret.put(listInputs.remove(i), listOutputs.remove(flag));
		}
		return ret;
	}
	
	private Map<Input, Input> bindingInputs(List<Input> in1,
			List<Input> in2) {
		
		Map<Input, Input> ret = new HashMap<Input, Input>();

		List<Input> listInputs = new LinkedList<Input>();
		List<Input> listOutputs = new LinkedList<Input>();

		listInputs.addAll(in1);
		listOutputs.addAll(in2);

		for (int i = 0; i < listInputs.size(); i++) {
			double tmpResult = FAIL;
			int flag = -1;
			for (int j = 0; j < listOutputs.size(); j++) {
				double rm = matchOntologies(listInputs.get(i).getType(), listOutputs
						.get(j).getType());
				if (rm <= tmpResult) {
					tmpResult = rm;
					flag = j;
				}
			}

			ret.put(listInputs.remove(i), listOutputs.remove(flag));
		}
		return ret;
	}
	
	private Map<Output, Output> bindingOutputs(List<Output> outputs,
			List<Output> outputs2) {
		Map<Output, Output> ret = new HashMap<Output, Output>();

		List<Output> listInputs = new LinkedList<Output>();
		List<Output> listOutputs = new LinkedList<Output>();

		listInputs.addAll(outputs);
		listOutputs.addAll(outputs2);

		for (int i = 0; i < listInputs.size(); i++) {
			double tmpResult = FAIL;
			int flag = -1;
			for (int j = 0; j < listOutputs.size(); j++) {
				double rm = matchOntologies(listInputs.get(i).getType(), listOutputs
						.get(j).getType());
				if (rm <= tmpResult) {
					tmpResult = rm;
					flag = j;
				}
			}

			ret.put(listInputs.remove(i), listOutputs.remove(flag));
		}
		return ret;
	}

	private double matchOntologies(URI type, URI type2) {
		List<URI> l1 = new ArrayList<URI>();
		List<URI> l2 = new ArrayList<URI>();
		l1.add(type);
		l2.add(type2);
		return this.matchOntologies(l1, l2);
	}

	/**
	 * 
	 * @param adv
	 * @param rqs
	 * @return
	 * @throws Exception
	 */

	private double inputMatch(Service adv, Service rqs) throws Exception {
		List<URI> in1 = new ArrayList<URI>();
		List<URI> in2 = new ArrayList<URI>();
		
		for (Input in : adv.getInputs()) {
			in1.add(in.getType());
		}
		
		for (Input in : rqs.getInputs()) {
			in2.add(in.getType());
		}
		
		double result = canFollow(in1, in2);

//		logger.info("Comparação de inputs:\nin1: " + in1 + "\nin2: " + in2
//				+ "\nDiferença: " + result + "\n");
		

		return result;
	}

	/**
	 * 
	 * @param adv
	 * @param rqs
	 * @return
	 * @throws Exception
	 */
	private double outputMatch(Service adv, Service rqs) throws Exception {

		List<URI> out1 = new ArrayList<URI>();
		
		for (Output out : adv.getOutputs()) {
			out1.add(out.getType());
		}

		List<URI> out2 = new ArrayList<URI>();
		
		for (Output out : rqs.getOutputs()) {
			out2.add(out.getType());
		}
		
		double result = canFollow(out1, out2);

//		logger.info("outputs:\nout1: " + out1 + "\nout2: " + out2
//				+ "\nDiferença: " + result + "\n");
		

		return result;
	}

	@Override
	public Service discover(int id, Map<String, List<URI>> selectDesc,
			List<Service> services) {
		
		this.id = id;
		
		try {
			//graph = this.constructAdvertisementGraph(services);
			
			List<URI> servicesInGraph = this.getServicesInGraph();
			boolean hasNewService=false; 
			for (Service service : services) {
				if(!servicesInGraph.contains(service.getURI())){
					graph = this.constructAdvertisementGraph(services);
					hasNewService = true;
					break;
				}
			}
			
			if (!hasNewService) {
				graph = this.getGraph();
			}
			
//			System.out.println(graph.getVertices().size());
//			for (Vertex V : graph.getVertices()) {
//				System.out.println(V.getContent().getInputs().get(0));
//			}
			//Graph<Service> graph = this.constructAdvertisementGraph(services);
			
			
			
			Service rqs = this.createService(selectDesc);
			
			return this.match(graph, rqs);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private Service createService(Map<String, List<URI>> selectDesc) {
		OWLSService s = new OWLSService(null);
		List<URI> list;
		for (String key : selectDesc.keySet()) {
		
			if(key.equalsIgnoreCase("inputs")) {
				for (URI uri : selectDesc.get(key)) {
					s.addInput(new Input(uri.toString(), uri));
				}				
			}else if (key.equalsIgnoreCase("outputs")) {
				for (URI uri : selectDesc.get(key)) {
					s.addOutput(new Output(uri.toString(), uri));
				}
			}else if (key.equalsIgnoreCase("outputsparents")) {
				list = new ArrayList<URI>();
				for (URI uri : selectDesc.get(key)) {
					list.add(uri);
				}
				s.setOutputsClasses(list);
			}else if (key.equalsIgnoreCase("inputsparents")) {
				list = new ArrayList<URI>();
				for (URI uri : selectDesc.get(key)) {
					list.add(uri);
				}
				s.setInputsClasses(list);
			}
			
			
		}
		
		return s;
	}
	
	private void persistGraph(Graph v) throws IOException{
		String file_path = System.getProperty(USER_PATH)+REPOSITORY_PATH;
		ObjectOutputStream objectOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file_path+"dicoveryGraph")));              
		objectOut.writeObject(v);  
		objectOut.close();  
		this.persistListOfServicesInGraph(v);
		
//		String file_path = System.getProperty(USER_PATH)+REPOSITORY_PATH;
//		XStream xstream = new XStream(new DomDriver());
//		xstream.processAnnotations(Graph.class);
//		xstream.toXML(v, new FileWriter(file_path+"dicoveryGraph"));
	}
	
	private Graph getGraph() throws IOException, ClassNotFoundException{
//		String file_path = System.getProperty(USER_PATH)+REPOSITORY_PATH;
//		XStream xstream = new XStream(new DomDriver());
//		xstream.processAnnotations(Graph.class);
//		
//		Graph g = null;
//		try {
//			g = (Graph)xstream.fromXML(new FileInputStream(file_path+"dicoveryGraph"));
//		} catch (Exception e1) {
//			e1.printStackTrace();
//			g = new Graph();
//		}
//		
//		return g;
		
		String file_path = System.getProperty(USER_PATH)+REPOSITORY_PATH;
		ObjectInputStream objectIn = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file_path+"dicoveryGraph")));  
		Graph graph = (Graph)objectIn.readObject();  
		objectIn.close();  
		return graph;
	}
	
	private List<URI> getServicesInGraph() throws IOException, ClassNotFoundException{
		String file_path = System.getProperty(USER_PATH)+REPOSITORY_PATH;
		ObjectInputStream objectIn = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file_path+"servicesInGraph")));  
		List<URI> services = (List<URI>)objectIn.readObject();  
		objectIn.close();  
		return services;		
	}
	
	private void persistListOfServicesInGraph(Graph v) throws IOException{
		List<URI> services = new ArrayList<URI>();
		for (Vertex vert : v.getVertices()) {
			services.add(vert.getContent().getURI());
		}
		
		String file_path = System.getProperty(USER_PATH)+REPOSITORY_PATH;
		ObjectOutputStream objectOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file_path+"servicesInGraph")));              
		objectOut.writeObject(services);  
		objectOut.close();  
	}
	
}
