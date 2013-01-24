package br.ufal.ic.grow.grinv.service.parameters;

import java.io.Serializable;
import java.net.URI;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("output")
public class Output implements Serializable{

	@XStreamAlias("id")
	private String id;
	@XStreamAlias("type")
	private URI type;
	public Output(String id, URI type) {
		super();
		this.id = id;
		this.type = type;
	}
	public Output() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public URI getType() {
		return type;
	}
	public void setType(URI type) {
		this.type = type;
	}
	
}
