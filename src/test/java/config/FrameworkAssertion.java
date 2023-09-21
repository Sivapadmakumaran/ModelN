package config;

public class FrameworkAssertion extends AssertionError {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errorMessage;
	
	public FrameworkAssertion(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
	
	@Override
	public String getMessage() {
		return errorMessage;
	}

}
