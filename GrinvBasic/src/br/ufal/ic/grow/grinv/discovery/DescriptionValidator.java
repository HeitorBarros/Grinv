package br.ufal.ic.grow.grinv.discovery;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Class that validates descriptions sent by user
 * 
 * @author Heitor Barros
 * @version 0.1
 *
 */
public class DescriptionValidator {


  public Map<String, List<URI>> validateDescriptions(Map<String, List<URI>> map){
//	  Map<String, List<URI>> selectedDesc = new HashMap<String, List<URI>>();
//		List<String> algorithmParameters = SystemConfiguration.getAlgorithmParameters();
//		
//		for (String string : map.keySet()) {
//			//System.out.println(string);
//			if(algorithmParameters.contains(string)){
//				selectedDesc.put(string, map.get(string));
//			}
//		}
//		
//		if (selectedDesc.isEmpty()) {
//			try {
//				throw new InvalidParametersException();
//			} catch (InvalidParametersException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				return null;
//			}
//		}
		return map;
//		return selectedDesc;
  }
  
}