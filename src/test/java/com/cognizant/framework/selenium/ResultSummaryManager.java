package com.cognizant.framework.selenium;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.config.Configuration;
import com.applitools.eyes.config.Feature;
import com.applitools.eyes.selenium.StitchMode;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.FrameworkParameters;
import com.cognizant.framework.ReportSettings;
import com.cognizant.framework.ReportTheme;
import com.cognizant.framework.ReportThemeFactory;
import com.cognizant.framework.Settings;
import com.cognizant.framework.TimeStamp;
import com.cognizant.framework.Util;
import com.cognizant.framework.WhitelistingPath;
import com.cognizant.framework.ReportThemeFactory.Theme;

/**
 * Singleton class that manages the result summary creation during a batch
 * execution
 * 
 * @author Cognizant
 */
public class ResultSummaryManager {
	private SeleniumReport summaryReport;

	private ReportSettings reportSettings;
	public String reportPath;

	public Date overallStartTime, overallEndTime;

	private Properties properties;
	private FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();

	private static final ResultSummaryManager RESULT_SUMMARY_MANAGER = new ResultSummaryManager();
	private SeleniumTestParameters testParameters;
	public Configuration config;
	public static BatchInfo batch;

	private ResultSummaryManager() {
		// To prevent external instantiation of this class
	}

	/**
	 * Function to return the singleton instance of the
	 * {@link ResultSummaryManager} object
	 * 
	 * @return Instance of the {@link ResultSummaryManager} object
	 */
	public static ResultSummaryManager getInstance() {
		return RESULT_SUMMARY_MANAGER;
	}

	public String getReportPath() {
		return this.reportPath;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	/**
	 * Function to set the absolute path of the framework (to be used as a
	 * relative path)
	 */
	public void setRelativePath() {
		String encryptedPath = WhitelistingPath.cleanStringForFilePath(System.getProperty("user.dir"));
		String relativePath = new File(encryptedPath).getAbsolutePath();
		if (relativePath.contains("supportlibraries")) {
			relativePath = new File(encryptedPath).getParent();
		}
		frameworkParameters.setRelativePath(relativePath);
		configureApplitoolsDetails();
	}

	/**
	 * Function to initialize the test batch execution
	 * 
	 * @param runConfiguration
	 *            The run configuration to be executed
	 */
	public void initializeTestBatch(String runConfiguration) {
		overallStartTime = Util.getCurrentTime();

		properties = Settings.getInstance();

		frameworkParameters.setRunConfiguration(runConfiguration);

		frameworkParameters
				.setStartCapturingObjects(Boolean.parseBoolean(properties.getProperty("StartCapturingObjects")));

		frameworkParameters.setHealObject(Boolean.parseBoolean(properties.getProperty("HealObjects")));

		frameworkParameters.setForceHeal(Boolean.parseBoolean(properties.getProperty("ForceHeal")));
	}

	/**
	 * Function to initialize the summary report
	 * 
	 * @param nThreads
	 *            The number of parallel threads configured for the test batch
	 *            execution
	 */
	public void initializeSummaryReport(int nThreads) {
		initializeReportSettings();
		ReportTheme reportTheme = ReportThemeFactory
				.getReportsTheme(Theme.valueOf(properties.getProperty("ReportsTheme")));

		summaryReport = new SeleniumReport(reportSettings, reportTheme, testParameters);

		summaryReport.initialize();
		summaryReport.initializeResultSummary();

		createResultSummaryHeader(nThreads);
	}

	private void initializeReportSettings() {
		if (System.getProperty("ReportPath") != null) {
			reportPath = System.getProperty("ReportPath");
		} else {
			reportPath = TimeStamp.getInstance();
		}

		reportSettings = new ReportSettings(reportPath, "");

		
		reportSettings.setDateFormatString(properties.getProperty("DateFormatString"));
		reportSettings.setProjectName(properties.getProperty("ProjectName"));
		reportSettings.setGenerateExcelReports(Boolean.parseBoolean(properties.getProperty("ExcelReport")));
		reportSettings.setGenerateHtmlReports(Boolean.parseBoolean(properties.getProperty("HtmlReport")));
		reportSettings.setLinkTestLogsToSummary(true);
	}

	private void createResultSummaryHeader(int nThreads) {
		summaryReport
				.addResultSummaryHeading(reportSettings.getProjectName() + " - Automation Execution Results Summary");
		summaryReport.addResultSummarySubHeading("Date & Time",
				": " + Util.getFormattedTime(overallStartTime, properties.getProperty("DateFormatString")), "OnError",
				": " + properties.getProperty("OnError"));
		summaryReport.addResultSummarySubHeading("Run Configuration", ": " + frameworkParameters.getRunConfiguration(),
				"No. of threads", ": " + nThreads);

		summaryReport.addResultSummaryTableHeadings();
	}

	/**
	 * Function to set up the error log file within the test report
	 */
	public void setupErrorLog() {
		String errorLogFile = reportPath + Util.getFileSeparator() + "ErrorLog.txt";
		String encryptedPath = WhitelistingPath.cleanStringForFilePath(errorLogFile);
		try {
			System.setErr(new PrintStream(new FileOutputStream(encryptedPath)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while setting up the Error log!");
		}
	}

	/**
	 * Function to update the results summary with the status of the test
	 * instance which was executed
	 * 
	 * @param testParameters
	 *            The {@link SeleniumTestParameters} object containing the
	 *            details of the test instance which was executed
	 * @param testReportName
	 *            The name of the test report file corresponding to the test
	 *            instance
	 * @param executionTime
	 *            The time taken to execute the test instance
	 * @param testStatus
	 *            The Pass/Fail status of the test instance
	 */
	public void updateResultSummary(SeleniumTestParameters testParameters, String testReportName, String executionTime,
			String testStatus) {
		summaryReport.updateResultSummary(testParameters, testReportName, executionTime, testStatus);
	}

	/**
	 * Function to do the required wrap-up activities after completing the test
	 * batch execution
	 * 
	 * @param testExecutedInUnitTestFramework
	 *            Boolean variable indicating whether the test is executed in
	 *            JUnit/TestNG
	 */
	public void wrapUp(Boolean testExecutedInUnitTestFramework) {
		overallEndTime = Util.getCurrentTime();
		String totalExecutionTime = Util.getTimeDifference(overallStartTime, overallEndTime);
		summaryReport.addResultSummaryFooter(totalExecutionTime);

		String encrpytedResultSrc = WhitelistingPath.cleanStringForFilePath(frameworkParameters.getRelativePath()
				+ Util.getFileSeparator() + properties.getProperty("TestNgReportPath") + Util.getFileSeparator()
				+ frameworkParameters.getRunConfiguration());

		String encryptedCss = WhitelistingPath
				.cleanStringForFilePath(frameworkParameters.getRelativePath() + Util.getFileSeparator()
						+ properties.getProperty("TestNgReportPath") + Util.getFileSeparator() + "testng.css");

		if (testExecutedInUnitTestFramework && System.getProperty("ReportPath") == null) {
			File testNgResultSrc = new File(encrpytedResultSrc);
			File testNgResultCssFile = new File(encryptedCss);
			File testNgResultDest = summaryReport.createResultsSubFolder("TestNG Results");

			try {
				FileUtils.copyDirectoryToDirectory(testNgResultSrc, testNgResultDest);
				FileUtils.copyFileToDirectory(testNgResultCssFile, testNgResultDest);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Function to launch the summary report at the end of the test batch
	 * execution
	 */
	public void launchResultSummary() {
		if (reportSettings.shouldGenerateHtmlReports()) {
			try {
				/**
				 * Use this Area for Sending any Mails through framework
				 */
				String encryptedPath = WhitelistingPath
						.cleanStringForFilePath(reportPath + Util.getFileSeparator() + "ErrorLog.txt");
				if (Boolean.parseBoolean(properties.getProperty("LaunchCRAFTCentral"))) {
					URI url = null;
					try {
						url = new URI(properties.getProperty("CRAFTCentralURL"));
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
					java.awt.Desktop.getDesktop().browse(url);
				} else {
					if (checkExceptionInErrorLogTxt()) {
						File f = new File(encryptedPath);
						java.awt.Desktop.getDesktop().edit(f);
					} else {
						String encryptedHtml = WhitelistingPath.cleanStringForFilePath(reportPath
								+ Util.getFileSeparator() + "HTML Results" + Util.getFileSeparator() + "Summary.Html");
						File htmlFile = new File(encryptedHtml);
						java.awt.Desktop.getDesktop().browse(htmlFile.toURI());
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Function to return the total batch execution time
	 * 
	 * 
	 * @return Execution time in hours and min
	 */
	public String overAllExecutionTime() {
		
		long timeDifferenceSeconds = (overallEndTime.getTime() - overallStartTime.getTime()) / 1000;	// to convert from milliseconds to seconds
		long timeDifferenceMinutes = timeDifferenceSeconds / 60;
		
		String timeDifferenceDetailed;
		if (timeDifferenceMinutes >= 60) {
			long timeDifferenceHours = timeDifferenceMinutes / 60;
			
			timeDifferenceDetailed = Long.toString(timeDifferenceHours) + ":"
									+ Long.toString(timeDifferenceMinutes % 60) + ":"
									+ Long.toString(timeDifferenceSeconds % 60);
		} else {
			timeDifferenceDetailed = "0:"+Long.toString(timeDifferenceMinutes) + ":"
									+ Long.toString(timeDifferenceSeconds % 60);
		}
		
		return timeDifferenceDetailed;
	}
	
	@SuppressWarnings("resource")
	private boolean checkExceptionInErrorLogTxt() throws IOException {
		boolean isException = false;
		String encryptedPath = WhitelistingPath
				.cleanStringForFilePath(reportPath + Util.getFileSeparator() + "ErrorLog.txt");
		File file = new File(encryptedPath);

		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.contains("Exception")) {
					isException = true;
					break;
				} else {
					isException = false;
				}
			}
		} catch (FileNotFoundException e) {
		}
		return isException;

	}
	
	/**
     * Function to create the configuration details for Applitools 
     */
    public void configureApplitoolsDetails() {

        properties =Settings.getInstance();        
        config = new Configuration();
        config.setApiKey(properties.getProperty("ApiKey"));
        config.setServerUrl(properties.getProperty("ServerUrl"));
        String strBatchName = properties.getProperty("ProjectName")+"_"+properties.getProperty("RunConfiguration");
        batch = new BatchInfo(strBatchName);    
        config.setStitchMode(StitchMode.CSS);
        config.setForceFullPageScreenshot(true);
		config.setBatch(batch); 
        config.setHideCaret(true);
        config.setIgnoreDisplacements(true);
        config.setFeatures(Feature.NO_SWITCH_WITHOUT_FRAME_CHAIN);
    }


}