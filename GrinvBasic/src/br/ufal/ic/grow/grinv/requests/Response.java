package br.ufal.ic.grow.grinv.requests;
/**
 * Abstract Response Class
 * @author Heitor Barros
 * @version 0.1
 *
 */
public class Response {
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
	 */
	public Response() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * Constructor
	 * @param type
	 * @param id
	 */
	public Response(String type, int id) {
		super();
		this.type = type;
		this.id = id;
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