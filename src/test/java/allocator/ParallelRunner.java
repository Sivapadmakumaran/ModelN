package allocator;

import com.cognizant.framework.selenium.*;

import java.util.HashMap;

import com.cognizant.craft.DriverScript;
import com.cognizant.framework.DataBaseOperation;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.TestCaseBean;

/**
 * Class to facilitate parallel execution of test scripts
 * 
 * @author Cognizant
 */
class ParallelRunner implements Runnable {
	private final SeleniumTestParameters testParameters;
	private int testBatchStatus = 0;
	public HashMap<String,Object> hmTestDetails = new HashMap<String,Object>();

	/**
	 * Constructor to initialize the details of the test case to be executed
	 * 
	 * @param testParameters
	 *            The {@link SeleniumTestParameters} object (passed from the
	 *            {@link Allocator})
	 */
	ParallelRunner(SeleniumTestParameters testParameters) {
		super();

		this.testParameters = testParameters;
	}

	/**
	 * Function to get the overall test batch status
	 * 
	 * @return The test batch status (0 = Success, 1 = Failure)
	 */
	public int getTestBatchStatus() {
		return testBatchStatus;
	}

	public void run() {
		TestCaseBean testCaseBean = new TestCaseBean();
		FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
		String testReportName, executionTime, testStatus;

		if (frameworkParameters.getStopExecution()) {
			testReportName = "N/A";
			executionTime = "N/A";
			testStatus = "Aborted";
			testBatchStatus = 1; // Non-zero outcome indicates failure
		} else {
			DriverScript driverScript = new DriverScript(this.testParameters);
			driverScript.driveTestExecution();

			testReportName = driverScript.getReportName();
			executionTime = driverScript.getExecutionTime();
			testStatus = driverScript.getTestStatus();
			testCaseBean = driverScript.getTestCaseBean();

			if ("failed".equalsIgnoreCase(testStatus)) {
				testBatchStatus = 1; // Non-zero outcome indicates failure
			}
		}

		ResultSummaryManager resultSummaryManager = ResultSummaryManager.getInstance();
		resultSummaryManager.updateResultSummary(testParameters, testReportName, executionTime, testStatus);
		/* DB-Updating reports to database */
		DataBaseOperation dbOperation = new DataBaseOperation();
		dbOperation.initializeTestParameters(testParameters);
		dbOperation.updateMongoDB("Run Manager", testCaseBean, executionTime, testStatus);

	}
}