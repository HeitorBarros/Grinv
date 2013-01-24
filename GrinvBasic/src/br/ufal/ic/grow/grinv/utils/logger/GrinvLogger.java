package br.ufal.ic.grow.grinv.utils.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import br.ufal.ic.grow.grinv.requests.DaIRequest;
import br.ufal.ic.grow.grinv.requests.DiscoveryRequest;
import br.ufal.ic.grow.grinv.requests.DiscoveryResponse;
import br.ufal.ic.grow.grinv.requests.InvocationRequest;
import br.ufal.ic.grow.grinv.requests.InvocationResponse;
import br.ufal.ic.grow.grinv.requests.Request;
import br.ufal.ic.grow.grinv.requests.Response;

public class GrinvLogger {

	private static final String USER_PATH = "user.home";
	private final static String PATH = "/Grinv/log/";
	
	
	public static void info(int id, String info){
		String home = System.getProperty(USER_PATH);
		String path = home+ PATH+"request"+ id+".log";
		try {
			
			BufferedWriter arq = new BufferedWriter(new FileWriter(path, true));
			arq.append(info);
			arq.close();
		} catch (IOException e) {
			 System.out.println("Log Failed");
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public static void info(int id, Request req){
		String home = System.getProperty(USER_PATH);
		String path = home+ PATH+ id+".log";
		new File(path);
		String msg = "";
		if (req.getType().equals("DiscoveryRequest")) {
			msg+="Receiving Discovery Request\n";
			msg+="Descriptions:\n";
			Map<String, List<URI>> desc = ((DiscoveryRequest)req).getServiceDescriptions();
			for (String s : desc.keySet()) {
				msg+=s+" -> "+ desc.get(s);
			}
		}else if (req.getType().equals("DaIRequest")) {
			msg+="Receiving Discovery and Invocation Request\n";
			msg+="Descriptions:\n";
			Map<String, List<URI>> desc = ((DaIRequest)req).getServiceDescriptions();
			for (String s : desc.keySet()) {
				msg+=s+" -> "+ desc.get(s)+"\n";
			}
			
			List<URI> inputs = ((DaIRequest)req).getInputs();
			msg+="Inputs:\n";
			for (URI in : inputs) {
				msg+=in.toString()+"\n";
			}
			
		}else if (req.getType().equals("InvocationRequest")) {
			msg+="Receiving Invocation Request\n";
			List<URI> inputs = ((InvocationRequest)req).getInputs();
			msg+="Inputs:\n";
			for (URI in : inputs) {
				msg+=in.toString()+"\n";
			}
		}
		GrinvLogger.info(id, msg);
		
		
	}
	
	public static void info(int id, Response resp){
		String msg = "\n- - - - - -\nSending Response\n";
		
		if (resp.getType().equals("DiscoveryResponse")) {
			msg+="Discovery Response\n";
			msg+="service found: "+ ((DiscoveryResponse)resp).getService();
		}else if (resp.getType().equals("InvocationResponse")) {
			msg+="Invocation Response\n";
			msg+="Results: \n";
			for (Object results : ((InvocationResponse)resp).getResults()) {
				msg+=(String)results+"\n";
			}
		}
		
		GrinvLogger.info(id, msg);
		
		
	}
	
	
	
}
