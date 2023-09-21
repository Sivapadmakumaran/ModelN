package testscripts;

import org.testng.annotations.Test;

import com.cognizant.craft.DriverScript;
import com.cognizant.craft.TestConfigurations;
import com.cognizant.framework.selenium.SeleniumTestParameters;

public class Contract extends TestConfigurations {
	/**
	 * @param testParameters
	 */
	
	@Test(dataProvider = "DesktopBrowsers", dataProviderClass = TestConfigurations.class)
	public void tC_Contract_E2E (SeleniumTestParameters testParameters) {

		testParameters.setCurrentTestDescription("To validate the Contract E2E Flow");
		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();
		tearDownTestRunner(testParameters, driverScript);
	}

}
