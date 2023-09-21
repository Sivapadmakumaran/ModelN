package com.cognizant.craft;

import java.util.LinkedHashMap;
import java.util.Map;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.cognizant.framework.ALMFunctions;
import com.cognizant.framework.APIReusuableLibrary;
import com.cognizant.framework.CraftDataTable;
import com.cognizant.framework.ExcelDataAccess;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.Report;
import com.cognizant.framework.ReportSettings;
import com.cognizant.framework.selenium.CraftDriver;
import com.cognizant.framework.selenium.ResultSummaryManager;
import com.cognizant.framework.selenium.SeleniumReport;
import com.cognizant.framework.selenium.SeleniumTestParameters;
import com.cognizant.framework.selenium.WebDriverUtil;


/**
 * 
 * Class to store runtime shared objects
 *
 */
public class RunContext {

	private static RunContext runContext = new RunContext();

	private ThreadLocal<CraftDataTable> tlDataTable = new ThreadLocal<>();
	private ThreadLocal<SeleniumReport> tlSeleniumReport = new ThreadLocal<>();
	private ThreadLocal<CraftDriver> tlCraftDriver = new ThreadLocal<>();
	private ThreadLocal<WebDriverUtil> tlWebDriverUtil = new ThreadLocal<>();
	private ThreadLocal<APIReusuableLibrary> tlAPIReusableLibrary = new ThreadLocal<>();
	private ThreadLocal<ExtentTest> tlExtentTest = new ThreadLocal<>();
	private ThreadLocal<ALMFunctions> tlALMFunctions = new ThreadLocal<>();
	private ThreadLocal<ScriptHelper> tlScriptHelper = new ThreadLocal<>();
	private ThreadLocal<DriverScript> tlDriverScript = new ThreadLocal<>();
	private ThreadLocal<SeleniumTestParameters> tlSeleniumTestParameters = new ThreadLocal<>();
	private ThreadLocal<FrameworkParameters> tlFrameworkParameters = new ThreadLocal<>();
	private ThreadLocal<ResultSummaryManager> tlResultSummaryManager = new ThreadLocal<>();
	private ThreadLocal<ReportSettings> tlReportSettings = new ThreadLocal<>();
	private ThreadLocal<Report> tlReport = new ThreadLocal<>();
	private ThreadLocal<ExtentReports> tlExtentReport = new ThreadLocal<>();
	private ThreadLocal<ExtentTest> tlExtendTest = new ThreadLocal<>();
	private ThreadLocal<ExcelDataAccess> tlExcelDataAccess = new ThreadLocal<>();
	private ThreadLocal<String> tlScreenshotPath = new ThreadLocal<>();
	
	private ThreadLocal<Map<String, Map<String, String>>> tlRuntimeMap = new ThreadLocal<Map<String, Map<String,String>>>() {
		
		@Override
		public Map<String, Map<String, String>> initialValue() {
			return new LinkedHashMap<String, Map<String, String>>();
		}
		
	};
	
	//private static AtomicInteger threadNumber = new AtomicInteger();
	
	private RunContext() {}
	
	public static RunContext getRunContext() {
		return runContext;
	}

	public CraftDataTable getDataTable() {
		return tlDataTable.get();
	}

	public void setDataTable(CraftDataTable dataTable) {
		this.tlDataTable.set(dataTable);
	}

	public SeleniumReport getSeleniumReport() {
		return tlSeleniumReport.get();
	}

	public void setSeleniumReport(SeleniumReport seleniumReport) {
		this.tlSeleniumReport.set(seleniumReport);
	}

	public CraftDriver getCraftDriver() {
		return tlCraftDriver.get();
	}

	public void setCraftDriver(CraftDriver craftDriver) {
		this.tlCraftDriver.set(craftDriver);
	}

	public WebDriverUtil getWebDriverUtil() {
		return tlWebDriverUtil.get();
	}

	public void setWebDriverUtil(WebDriverUtil webDriverUtil) {
		this.tlWebDriverUtil.set(webDriverUtil);
	}

	public APIReusuableLibrary getAPIReusableLibrary() {
		return tlAPIReusableLibrary.get();
	}

	public void setAPIReusableLibrary(APIReusuableLibrary apiReusableLibrary) {
		this.tlAPIReusableLibrary.set(apiReusableLibrary);
	}

	public ExtentTest getExtentTest() {
		return tlExtentTest.get();
	}

	public void setExtentTest(ExtentTest extentTest) {
		this.tlExtentTest.set(extentTest);
	}

	public ALMFunctions getALMFunctions() {
		return tlALMFunctions.get();
	}

	public void setALMFunctions(ALMFunctions almFunctions) {
		this.tlALMFunctions.set(almFunctions);
	}

	public ScriptHelper getScriptHelper() {
		return tlScriptHelper.get();
	}

	public void setScriptHelper(ScriptHelper scriptHelper) {
		this.tlScriptHelper.set(scriptHelper);
	}

	public DriverScript getDriverScript() {
		return tlDriverScript.get();
	}

	public void setDriverScript(DriverScript driverScript) {
		this.tlDriverScript.set(driverScript);
	}

	public SeleniumTestParameters getSeleniumTestParameters() {
		return tlSeleniumTestParameters.get();
	}

	public void setSeleniumTestParameters(SeleniumTestParameters seleniumTestParameters) {
		this.tlSeleniumTestParameters.set(seleniumTestParameters);
	}

	public FrameworkParameters getFrameworkParameters() {
		return tlFrameworkParameters.get();
	}

	public void setFrameworkParameters(FrameworkParameters frameworkParameters) {
		this.tlFrameworkParameters.set(frameworkParameters);
	}

	public ResultSummaryManager getResultSummaryManager() {
		return tlResultSummaryManager.get();
	}

	public void setResultSummaryManager(ResultSummaryManager resultSummaryManager) {
		this.tlResultSummaryManager.set(resultSummaryManager);
	}

	public ReportSettings getReportSettings() {
		return tlReportSettings.get();
	}

	public void setReportSettings(ReportSettings reportSettings) {
		this.tlReportSettings.set(reportSettings);
	}

	public Report getReport() {
		return tlReport.get();
	}

	public void setReport(Report report) {
		this.tlReport.set(report);
	}

	public ExtentReports getExtentReport() {
		return tlExtentReport.get();
	}

	public void setExtentReport(ExtentReports extentReport) {
		this.tlExtentReport.set(extentReport);
	}

	public ExtentTest getExtendTest() {
		return tlExtendTest.get();
	}

	public void setExtendTest(ExtentTest extendTest) {
		this.tlExtendTest.set(extendTest);
	}

	public ExcelDataAccess getExcelDataAccess() {
		return tlExcelDataAccess.get();
	}

	public void setExcelDataAccess(ExcelDataAccess excelDataAccess) {
		this.tlExcelDataAccess.set(excelDataAccess);
	}
	
	public String getScreenshotPath() {
		return tlScreenshotPath.get();
	}

	public void setScreenshotPath(String screenshotPath) {
		this.tlScreenshotPath.set(screenshotPath);
	}
	
	public Map<String, Map<String,String>> getRuntimeMap() {
		return this.tlRuntimeMap.get();
	}

	/**
	 * remove all shared objects once execution completed
	 */
	public void removeAll() {
		tlDataTable.remove();
		tlSeleniumReport.remove();
		tlCraftDriver.remove();
		tlWebDriverUtil.remove();
		tlAPIReusableLibrary.remove();
		tlExtentTest.remove();
		tlALMFunctions.remove();
		tlScriptHelper.remove();
		tlDriverScript.remove();
		tlSeleniumTestParameters.remove();
		tlFrameworkParameters.remove();
		tlExtentReport.remove();
		tlExtendTest.remove();
		tlExcelDataAccess.remove();
		tlScreenshotPath.remove();
	}
	
//	public int getThreadCount() {
//		return threadNumber.getAndIncrement();  
//	}
	
	
}
