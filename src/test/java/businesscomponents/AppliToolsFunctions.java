/*package businesscomponents;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.TestResults;
import com.applitools.eyes.TestResultsStatus;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.StitchMode;
import com.applitools.eyes.selenium.fluent.Target;
import com.cognizant.craft.ScriptHelper;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Settings;

public class AppliToolsFunctions extends CommonFunctions{

	public AppliToolsFunctions(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}
	*//**
	 * Function to set configuration and initiate Applitools Eyes 
	 * 
	 * @param strTestMethod - name of the test method
	 * @return - no return parameters
	 *//*


	public Eyes eyes;
	public ClassicRunner runner = new ClassicRunner();
	public static String strBatchName = Settings.getInstance().getProperty("ProjectName")+"_"+Settings.getInstance().getProperty("RunConfiguration");
	//public static String strBatchName = "Test";
	public static BatchInfo batch =new BatchInfo(strBatchName);
	private void initiateEyes() throws FrameworkException {


		eyes = new Eyes(runner);
		eyes.setApiKey(properties.getProperty("ApiKey"));
		eyes.setServerUrl(properties.getProperty("ServerUrl"));                             
		eyes.setBatch(batch); 
		eyes.setForceFullPageScreenshot(true);
		eyes.setStitchMode(StitchMode.CSS);
		eyes.setHideCaret(true);
		eyes.setIgnoreDisplacements(true);
		String strProjectName = properties.getProperty("ProjectName");
		eyes.open(driver.getWebDriver(), strProjectName, testparameters.getCurrentTestcase());

	}

	*//**
	 * Function to Validate Frame using Applitools Eyes
	 * 
	 * @param strMethodName - name of the test method
	 * @param strPageName - name of Page to be matched
	 * @return - no return parameters
	 * 
	 *//*         

	public void validateApplitoolsLightning(String strPageName) {

		initiateEyes();
		eyes.check(Target.window().fully());                            
		TestResults testResult= eyes.close(false);
		TestResultsStatus testResultsStatus = testResult.getStatus();
		if(testResultsStatus.toString().equalsIgnoreCase("Passed")) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Applitools Validation", 
					"Baseline and Checkpoint image should match for "+strPageName+ " Page",
					"Baseline and Checkpoint image matched for " 
							+strPageName+ " Page - <a href = " +testResult.getUrl()+ ">View Results</a>", false);


		}else {
			ALMFunctions.ThrowException("Applitools Validation", "Baseline and Checkpoint image should match", 
					"Baseline and Checkpoint image not matched for " +strPageName+ 
					" Page - <a href = " +testResult.getUrl()+ ">View Results</a>", false);
		}             
	}

}
*/