package allocator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import org.openqa.selenium.Platform;

import com.cognizant.framework.selenium.*;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.KlovReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.cognizant.framework.ExcelDataAccessforxlsm;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.IterationOptions;
import com.cognizant.framework.Settings;
import com.cognizant.framework.Util;

/**
 * Class to manage the batch execution of test scripts within the framework
 * 
 * @author Cognizant
 */
public class Allocator {
	private FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
	private Properties properties;
	private Properties mobileProperties;
	private ResultSummaryManager resultSummaryManager = ResultSummaryManager.getInstance();
	public static String strReportPath;
	private static ExtentHtmlReporter htmlReporter;
	private static ExtentReports extentReport;
	private static ExtentTest extentTest;
	private static KlovReporter klovReporter = new KlovReporter();

	public static final ReentrantLock lock = new ReentrantLock();
	public static List<String> lstTestApplications = new ArrayList<String>();
	public static boolean blnUserRolesPrerequisites = true;
	public static boolean blnGeneralProcessPrerequisites = true;
	
	/**
	 * The entry point of the test batch execution <br>
	 * Exits with a value of 0 if the test passes and 1 if the test fails
	 * 
	 * @param args
	 *            Command line arguments to the Allocator (Not applicable)
	 */
	public static void main(String[] args) {
		Allocator allocator = new Allocator();
		allocator.driveBatchExecution();
	}

	private void driveBatchExecution() {
		resultSummaryManager.setRelativePath();
		properties = Settings.getInstance();
		mobileProperties = Settings.getMobilePropertiesInstance();
		String runConfiguration;
		if (System.getProperty("RunConfiguration") != null) {
			runConfiguration = System.getProperty("RunConfiguration");
		} else {
			runConfiguration = properties.getProperty("RunConfiguration");
		}
		resultSummaryManager.initializeTestBatch(runConfiguration);

		int nThreads = Integer.parseInt(properties.getProperty("NumberOfThreads"));
		resultSummaryManager.initializeSummaryReport(nThreads);

		resultSummaryManager.setupErrorLog();

		generateExtentReports();

		strReportPath = resultSummaryManager.reportPath;
		
		int testBatchStatus = executeTestBatch(nThreads);

		resultSummaryManager.wrapUp(false);

		if (Boolean.parseBoolean(properties.getProperty("GenerateKlov"))) {
			extentReport.attachReporter(klovReporter);
		}

		extentReport.flush();

		resultSummaryManager.launchResultSummary();

		System.exit(testBatchStatus);
	}

	private int executeTestBatch(int nThreads) {
		List<SeleniumTestParameters> testInstancesToRun = getRunInfo(frameworkParameters.getRunConfiguration());
		ExecutorService parallelExecutor = Executors.newFixedThreadPool(nThreads);
		ParallelRunner testRunner = null;

		for (int currentTestInstance = 0; currentTestInstance < testInstancesToRun.size(); currentTestInstance++) {
			testRunner = new ParallelRunner(testInstancesToRun.get(currentTestInstance));
			parallelExecutor.execute(testRunner);

			if (frameworkParameters.getStopExecution()) {
				break;
			}
		}

		parallelExecutor.shutdown();
		while (!parallelExecutor.isTerminated()) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (testRunner == null) {
			return 0; // All tests flagged as "No" in the Run Manager
		} else {
			return testRunner.getTestBatchStatus();
		}
	}

	private List<SeleniumTestParameters> getRunInfo(String sheetName) {

		ExcelDataAccessforxlsm runManagerAccess = new ExcelDataAccessforxlsm(
				frameworkParameters.getRelativePath() + Util.getFileSeparator() + "src" + Util.getFileSeparator()
						+ "test" + Util.getFileSeparator() + "resources",
				"Run Manager");
		ExcelDataAccessforxlsm runConfigurationAccess = new ExcelDataAccessforxlsm(
				frameworkParameters.getRelativePath() + Util.getFileSeparator() + "src" + Util.getFileSeparator()
				+ "test" + Util.getFileSeparator() + "resources", "Run Manager");
		runConfigurationAccess.setDatasheetName("HostName_Config");
		runManagerAccess.setDatasheetName(sheetName);

		runManagerAccess.setDatasheetName(sheetName);
		List<SeleniumTestParameters> testInstancesToRun = new ArrayList<SeleniumTestParameters>();
		String[] keys = { "Execute", "TestScenario", "TestCase", "TestInstance", "Description", "IterationMode",
				"StartIteration", "EndIteration", "TestConfigurationID"  };
		List<Map<String, String>> values = runManagerAccess.getValues(keys);

		for (int currentTestInstance = 0; currentTestInstance < values.size(); currentTestInstance++) {

			Map<String, String> row = values.get(currentTestInstance);
			String executeFlag = row.get("Execute");

			if (executeFlag.equalsIgnoreCase("Yes")) {
				String currentScenario = row.get("TestScenario");
				String currentTestcase = row.get("TestCase");
				SeleniumTestParameters testParameters = new SeleniumTestParameters(currentScenario, currentTestcase);
				testParameters.setCurrentTestDescription(row.get("Description"));
				testParameters.setCurrentTestInstance("Instance" + row.get("TestInstance"));
				testParameters.setExtentReport(extentReport);
				testParameters.setExtentTest(extentTest);

				String iterationMode = row.get("IterationMode");
				if (!iterationMode.equals("")) {
					testParameters.setIterationMode(IterationOptions.valueOf(iterationMode));
				} else {
					testParameters.setIterationMode(IterationOptions.RUN_ALL_ITERATIONS);
				}

				String startIteration = row.get("StartIteration");
				if (!startIteration.equals("")) {
					testParameters.setStartIteration(Integer.parseInt(startIteration));
				}
				String endIteration = row.get("EndIteration");
				if (!endIteration.equals("")) {
					testParameters.setEndIteration(Integer.parseInt(endIteration));
				}
				String appIdentifier = row.get("AppIdentifier");
				if (!"".equals(appIdentifier)) {
					testParameters.setAppIdentifier(appIdentifier);
				} 				
				String remoteHostName = row.get("RemoteIP");
				if (!"".equals(remoteHostName)) {
					testParameters.setRemoteHostName(remoteHostName);
				} 
				String remoteHostIP = "";
				for(int intHostConfigRow=1;intHostConfigRow<=runConfigurationAccess.getLastRowNum();intHostConfigRow++) {
					if(runConfigurationAccess.getValue(intHostConfigRow, "HostName").equals(remoteHostName)){
						remoteHostIP = runConfigurationAccess.getValue(intHostConfigRow, "HostIP");
						break;
					}
				}
				if (!"".equals(remoteHostIP)) {
					testParameters.setRemoteHostIP(remoteHostIP);
				} 
				
				String testConfig = row.get("TestConfigurationID");
				if (!"".equals(testConfig)) {
					getTestConfigValues(runManagerAccess, "TestConfigurations", testConfig, testParameters);
				}
				
				testInstancesToRun.add(testParameters);
				runManagerAccess.setDatasheetName(sheetName);
			}
		}
		return testInstancesToRun;
	}

	private void getTestConfigValues(ExcelDataAccessforxlsm runManagerAccess, String sheetName, String testConfigName,
			SeleniumTestParameters testParameters) {

		runManagerAccess.setDatasheetName(sheetName);
		int rowNum = runManagerAccess.getRowNum(testConfigName, 0, 1);

		String[] keys = { "TestConfigurationID", "ExecutionMode", "ToolName", "MobileExecutionPlatform",
				"MobileOSVersion", "DeviceName", "Browser", "BrowserVersion", "Platform", "PlatformVersion",
				"SeeTestPort" };
		Map<String, String> values = runManagerAccess.getValuesForSpecificRow(keys, rowNum);

		String executionMode = values.get("ExecutionMode");
		if (!"".equals(executionMode)) {
			testParameters.setExecutionMode(ExecutionMode.valueOf(executionMode));
		} else {
			testParameters.setExecutionMode(ExecutionMode.valueOf(properties.getProperty("DefaultExecutionMode")));
		}

		String toolName = values.get("ToolName");
		if (!"".equals(toolName)) {
			testParameters.setMobileToolName(ToolName.valueOf(toolName));
		} else {
			testParameters.setMobileToolName(ToolName.valueOf(mobileProperties.getProperty("DefaultMobileToolName")));
		}

		String executionPlatform = values.get("MobileExecutionPlatform");
		if (!"".equals(executionPlatform)) {
			testParameters.setMobileExecutionPlatform(MobileExecutionPlatform.valueOf(executionPlatform));
		} else {
			testParameters.setMobileExecutionPlatform(
					MobileExecutionPlatform.valueOf(mobileProperties.getProperty("DefaultMobileExecutionPlatform")));
		}

		String mobileOSVersion = values.get("MobileOSVersion");
		if (!"".equals(mobileOSVersion)) {
			testParameters.setmobileOSVersion(mobileOSVersion);
		}

		String deviceName = values.get("DeviceName");
		if (!"".equals(deviceName)) {
			testParameters.setDeviceName(deviceName);
		}

		String browser = values.get("Browser");
		if (!"".equals(browser)) {
			testParameters.setBrowser(Browser.valueOf(browser));
		} else {
			testParameters.setBrowser(Browser.valueOf(properties.getProperty("DefaultBrowser")));
		}

		String browserVersion = values.get("BrowserVersion");
		if (!"".equals(browserVersion)) {
			testParameters.setBrowserVersion(browserVersion);
		}

		String platform = values.get("Platform");
		if (!"".equals(platform)) {
			testParameters.setPlatform(Platform.valueOf(platform));
		} else {
			testParameters.setPlatform(Platform.valueOf(properties.getProperty("DefaultPlatform")));
		}

		String seeTestPort = values.get("SeeTestPort");
		if (!"".equals(seeTestPort)) {
			testParameters.setSeeTestPort(seeTestPort);
		} else {
			testParameters.setSeeTestPort(mobileProperties.getProperty("SeeTestDefaultPort"));
		}

		String platformVersion = values.get("PlatformVersion");
		if (!"".equals(platformVersion)) {
			testParameters.setPlatformVersion(platformVersion);
		}
	}

	private void generateExtentReports() {
		integrateWithKlov();
		htmlReporter = new ExtentHtmlReporter(resultSummaryManager.getReportPath() + Util.getFileSeparator()
				+ "Extent Result" + Util.getFileSeparator() + "ExtentReport.html");
		extentReport = new ExtentReports();
		extentReport.attachReporter(htmlReporter);
		extentReport.setSystemInfo("Project Name", properties.getProperty("ProjectName"));
		extentReport.setSystemInfo("Framework", "CRAFT Maven");
		extentReport.setSystemInfo("Framework Version", "3.2");
		extentReport.setSystemInfo("Author", "Cognizant");

		htmlReporter.config().setDocumentTitle("CRAFT Extent Report");
		htmlReporter.config().setReportName("Extent Report for CRAFT");
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		htmlReporter.config().setTheme(Theme.STANDARD);
	}

	private void integrateWithKlov() {
		String dbHost = properties.getProperty("DBHost");
		String dbPort = properties.getProperty("DBPort");
		if (Boolean.parseBoolean(properties.getProperty("GenerateKlov"))) {
			klovReporter.initMongoDbConnection(dbHost, Integer.valueOf(dbPort));
			klovReporter.setProjectName(properties.getProperty("GenerateKlov"));
			klovReporter.setReportName("CRAFT Reports");
			klovReporter.setKlovUrl(properties.getProperty("KlovURL"));
		}
	}

}