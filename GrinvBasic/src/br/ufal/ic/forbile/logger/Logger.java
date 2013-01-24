package br.ufal.ic.forbile.logger;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class Logger {

	private String filename;
	
	private FileOutputStream logfile;
	
	
	public Logger(String fileUri){
		filename = fileUri;
		try {
			logfile = new FileOutputStream(fileUri);		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void info(String info){
		try {
			logfile.write(info.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("INFO: "+info);
	}
	
	public void close(){
		try {
			logfile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	
}
