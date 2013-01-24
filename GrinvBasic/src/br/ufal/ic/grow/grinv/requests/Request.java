package br.ufal.ic.grow.grinv.requests;
/**
 * Abstract Request Class
 * @author Heitor Barros
 * @version 0.1
 *
 */
public abstract class Request {
	/**
	 * Type
	 */
	private String type;
	/**
	 * ID
	 */
	private int id;
	/**
	 * Constructor
	 * @param type
	 */
	public Request(String type) {
		super();
		this.type = type;
	}

	public Request() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	
	
}