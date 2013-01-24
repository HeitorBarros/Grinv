package br.ufal.ic.grow.grinv.service.parameters;

public class ParametersMapping {

	private Parameter input;
	private Parameter output;
	
	public ParametersMapping(Parameter input, Parameter output) {
		super();
		this.input = input;
		this.output = output;
	}
	
	public ParametersMapping() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Parameter getInput() {
		return input;
	}
	public void setInput(Parameter input) {
		this.input = input;
	}
	public Parameter getOutput() {
		return output;
	}
	public void setOutput(Parameter output) {
		this.output = output;
	}
		
}
