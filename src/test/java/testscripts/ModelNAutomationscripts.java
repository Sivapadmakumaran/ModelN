package testscripts;

import org.testng.annotations.Test;

import com.cognizant.craft.DriverScript;
import com.cognizant.craft.TestConfigurations;
import com.cognizant.framework.selenium.SeleniumTestParameters;

public class ModelNAutomationscripts extends TestConfigurations {
	/**
	 * @param testParameters
	 */
	
	/*@Test(dataProvider = "DesktopBrowsers", dataProviderClass = TestConfigurations.class,priority=0)
	public void tC_Medicaid_URM009_URM004 (SeleniumTestParameters testParameters) {

		testParameters.setCurrentTestDescription("To validate the GP Flow");
		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();
		tearDownTestRunner(testParameters, driverScript);
	}*/
	@Test(dataProvider = "DesktopBrowsers", dataProviderClass = TestConfigurations.class,priority=1)
	public void tC_GPETEFlowForAMPPriceType (SeleniumTestParameters testParameters) {

		testParameters.setCurrentTestDescription("To validate the GP Flow");
		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();
		tearDownTestRunner(testParameters, driverScript);
	}
	/*@Test(dataProvider = "DesktopBrowsers", dataProviderClass = TestConfigurations.class,priority=1)
	public void tC_GPForBPAndBPInitialPriceType (SeleniumTestParameters testParameters) {

		testParameters.setCurrentTestDescription("To validate the BP and BP Initial Flow");
		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();
		tearDownTestRunner(testParameters, driverScript);
	}*/
	/*@Test(dataProvider = "DesktopBrowsers", dataProviderClass = TestConfigurations.class,priority=1)
	public void tC_GPForASPPriceType (SeleniumTestParameters testParameters) {

		testParameters.setCurrentTestDescription("To validate the ASP Flow");
		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();
		tearDownTestRunner(testParameters, driverScript);
	}*/
	/*@Test(dataProvider = "DesktopBrowsers", dataProviderClass = TestConfigurations.class,priority=1)
	public void tC_GPForNFAMPPriceType (SeleniumTestParameters testParameters) {

		testParameters.setCurrentTestDescription("To validate the NFAMP Flow");
		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();
		tearDownTestRunner(testParameters, driverScript);
	}*/
	
	/*@Test(dataProvider = "DesktopBrowsers", dataProviderClass = TestConfigurations.class,priority=1)
	public void tC_GPForPHSPriceType (SeleniumTestParameters testParameters) {

		testParameters.setCurrentTestDescription("To validate the PHS Flow");
		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();
		tearDownTestRunner(testParameters, driverScript);
	}*/
	/*@Test(dataProvider = "DesktopBrowsers", dataProviderClass = TestConfigurations.class,priority=1)
	public void tC_GPForFCPPriceType (SeleniumTestParameters testParameters) {

		testParameters.setCurrentTestDescription("To validate the PHS Flow");
		DriverScript driverScript = new DriverScript(testParameters);
		driverScript.driveTestExecution();
		tearDownTestRunner(testParameters, driverScript);
	}*/
}
