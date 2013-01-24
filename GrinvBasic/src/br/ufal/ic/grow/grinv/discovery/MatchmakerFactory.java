package br.ufal.ic.grow.grinv.discovery;

import java.util.Map;

import br.ufal.ic.grow.grinv.configuration.SystemConfiguration;

/**
 * @author Heitor Barros
 * @version 0.1
 * 
 * Factory responsible to instantiate the Discovery Algorithm found in the configuration file
 * created by the Grinv Manager
 * 
 */
public class MatchmakerFactory {
	
	public static Map<Integer, AbstractMatchmaker> createMatchmaker(){
		return getInstance();
	}
	
	private static Map<Integer,AbstractMatchmaker> matchmaker;
	
	private static Map<Integer,AbstractMatchmaker> getInstance(){
		if (matchmaker==null) {
			matchmaker = SystemConfiguration.getMatchmaker();
		}
		return matchmaker;

	}

}
