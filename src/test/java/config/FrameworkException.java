package config;

import com.cognizant.craft.RunContext;

/**
 * Exception class for the framework
 * @author Cognizant
 */
@SuppressWarnings("serial")
public class FrameworkException extends RuntimeException {
	/**
	 * The step name to be specified for the exception
	 */
	public String errorName = "Error";
	
	public String errorDescription;
	
	
	/**
	 * Constructor to initialize the exception from the framework
	 * @param errorDescription The Exception message to be thrown
	 */
	public FrameworkException(String errorDescription) {
		super(errorDescription);
		this.errorDescription = errorDescription;
		//RunContext.getRunContext().getALMFunctions().ThrowException("Framework exception", "Error occurred", errorDescription, false);
	}
	
	/**
	 * Constructor to initialize the exception from the framework
	 * @param errorName The step name for the error
	 * @param errorDescription The Exception message to be thrown
	 */
	public FrameworkException(String errorName, String errorDescription) {
		super(errorDescription);
		this.errorName = errorName;
		this.errorDescription = errorDescription;
		//RunContext.getRunContext().getALMFunctions().ThrowException("Framework exception", errorName, errorDescription, false);
	}

	public FrameworkException(String errorName, String strExpected, String errorDescription) {
		super(errorDescription);
		this.errorName = errorName;
		this.errorDescription = errorDescription;
		//RunContext.getRunContext().getALMFunctions().ThrowException(errorName, strExpected, errorDescription, false);
	}
	
	public String getErrorName() {
		return errorName;
	}
	
	@Override
	public String getMessage() {
		return errorDescription;
	}
}