package br.ufal.ic.grow.grinv.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.mindswap.owl.OWLFactory;
import org.mindswap.owl.OWLKnowledgeBase;
import org.mindswap.owl.OWLOntology;

public class GrinvOntManager implements OntologyRepositoryManager{

	private static final String USER_PATH = "user.home";
	 
	private static final String ONTOLOGIES_REPOSITORY_PATH = "/Grinv/repository/onto/";
	
	private static final String FILE_NAME = "GrinvKB";

	private static final String ONTOLOGIES_INDEX = "ontologyBase";
	
	private static final String BASIC_KB = "BasicKB";
	
	@Override
	public void addOntology(URI uri) throws IOException {
		OWLKnowledgeBase kb = this.getKB();
		
		OWLOntology ont = kb.read(uri);
		String file_path = System.getProperty(USER_PATH)+ONTOLOGIES_REPOSITORY_PATH;
		Writer wt = new FileWriter(file_path+FILE_NAME);
		kb.getWriter().write(wt, null);
		for (OWLOntology onto : ont.getImports(true)) {
			this.addOntology(onto.getURI());
		}
	}

	@Override
	public OWLKnowledgeBase getKB() throws IOException {
		
		OWLKnowledgeBase kb = OWLFactory.createKB();
		String file_path = System.getProperty(USER_PATH)+ONTOLOGIES_REPOSITORY_PATH;
		FileReader reader = new FileReader(file_path+FILE_NAME);
		kb.read(reader, null);


		return kb;
	}
	
	public OWLKnowledgeBase createBasicKB() throws IOException{
		OWLKnowledgeBase kb = OWLFactory.createKB();
		
		String file_path = System.getProperty(USER_PATH)+ONTOLOGIES_REPOSITORY_PATH;
		FileReader reader = new FileReader(file_path+BASIC_KB);
		kb.read(reader, null);
		
//		reader = new FileReader(file_path+"ActorDefault.owl");
//		kb.read(reader, null);
//		
//		reader = new FileReader(file_path+"ServiceParameter.owl");
//		kb.read(reader, null);
//		
//		reader = new FileReader(file_path+"ServiceCategory.owl");
//		kb.read(reader, null);
//		
//		reader = new FileReader(file_path+"Expression.owl");
//		kb.read(reader, null);
//		
//		reader = new FileReader(file_path+"MoreGroundings.owl");
//		kb.read(reader, null);
//		
//		reader = new FileReader(file_path+"FLAService.owl");
//		kb.read(reader, null);
//		
//		this.addOntology(kb);
		return kb;
	}
	
	public OWLKnowledgeBase addToKB(OWLKnowledgeBase kb, URI uri) throws IOException{

		OWLKnowledgeBase anotherKB = this.getOntology(uri);
		System.out.println(kb.readService(uri).getURI());
//		for (OWLOntology ont : anotherKB.getOntologies(true)) {
//			System.out.println(ont.getURI());
//		}
		kb.load(anotherKB.readService(uri).getOntology(), true);
		
		
		return kb;
	}
	
	public OWLKnowledgeBase getOntology(URI uri) throws IOException{
		URI cleanURI = URI.create(uri.toString().split("#")[0]);
		Map<URI, String> index = this.readOntologyIndex();
		if(!index.containsKey(cleanURI)){
			this.submitNewOntology(uri);
		}

		OWLKnowledgeBase kb = OWLFactory.createKB();
		String file_path = System.getProperty(USER_PATH)+ONTOLOGIES_REPOSITORY_PATH;
		FileReader reader = new FileReader(file_path+index.get(cleanURI));
		kb.read(reader, null);
		
		return kb;
		
	}
	
	private void submitNewServiceOntology(URI uri) throws IOException {
		OWLKnowledgeBase kb = this.createBasicKB();
		kb.readService(uri);
		String file_path = System.getProperty(USER_PATH)+ONTOLOGIES_REPOSITORY_PATH;
		String tk = uri.getPath();
		if (tk!=null) {
			String[] t  = tk.split("/");
			tk = t[t.length-1];
		}
		Writer wt = new FileWriter(file_path+tk);
		kb.getWriter().write(wt, null);
		
		if(!this.readOntologyIndex().containsKey(uri)){
			this.addOntologyInFile(uri, tk);
		}
	}
	
	public void submitNewOntology(URI uri) throws IOException{
		
		OWLKnowledgeBase kb = OWLFactory.createKB();
		OWLOntology ont = kb.read(uri);
		String file_path = System.getProperty(USER_PATH)+ONTOLOGIES_REPOSITORY_PATH;
		String tk = uri.getPath();
		if (tk!=null) {
			String[] t  = tk.split("/");
			tk = t[t.length-1];
		}
		Writer wt = new FileWriter(file_path+tk);
		kb.getWriter().write(wt, null);
		
		if(!this.readOntologyIndex().containsKey(uri)){
			this.addOntologyInFile(uri, tk);
		}
			
		
		
	}
	
	private void addOntologyInFile(URI uri, String s) throws IOException{
		String home = System.getProperty(USER_PATH);
		String path = home+ONTOLOGIES_REPOSITORY_PATH+ONTOLOGIES_INDEX;
		BufferedWriter out = new BufferedWriter(new FileWriter(path, true)); 
		out.append(uri+"="+s+"\n"); 
		out.close(); 
	}
	
	private Map<URI, String> readOntologyIndex() throws IOException{
		
		Map<URI, String> uri_index = new HashMap<URI, String>();
		
		String home = System.getProperty(USER_PATH);
		String path = home+ONTOLOGIES_REPOSITORY_PATH+ONTOLOGIES_INDEX;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String s = "";
		String[] tks = null;
		while ((s=reader.readLine())!=null) {
			if ((s.isEmpty())||(s.startsWith("#"))) {
				break;
			}else{
				tks = s.split("=");
				try {
					uri_index.put(new URI(tks[0]), tks[1]);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			
		}
		return uri_index;
		
	}
	
	public static void main(String[] args) throws IOException, URISyntaxException {
		GrinvOntManager OM = new GrinvOntManager();
		
		OM.submitNewOntology(new URI("http://localhost:8080/LearningOntologies/onto/OntologyDomainSWS.owl"));
		//OM.submitNewServiceOntology(new URI("http://localhost:8080/LearningServices/owls/getConcept.owl"));
//		OWLKnowledgeBase kb = OWLFactory.createKB();
//		kb = OM.getOntology(new URI("http://www.daml.org/services/owl-s/1.2/Service.owl"));
	}

	public void addOntology(OWLKnowledgeBase kb) throws IOException {
		
		String file_path = System.getProperty(USER_PATH)+ONTOLOGIES_REPOSITORY_PATH;
		Writer wt = new FileWriter(file_path+"BasicKB");
		kb.getWriter().write(wt, null);
		
	}

	

}
