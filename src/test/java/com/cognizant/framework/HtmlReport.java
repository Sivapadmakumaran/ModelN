package com.cognizant.framework;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.commons.codec.binary.Base64;




/**
 * Class to encapsulate the HTML report generation functions of the framework
 * 
 * @author Cognizant
 */
class HtmlReport implements ReportType {
	private String testLogPath, resultSummaryPath;
	private ReportSettings reportSettings;
	private ReportTheme reportTheme;

	private boolean isTestLogHeaderTableCreated = false;
	private boolean isTestLogMainTableCreated = false;
	private boolean isResultSummaryHeaderTableCreated = false;
	private boolean isResultSummaryMainTableCreated = false;

	private String currentSection = "";
	private String currentSubSection = "";
	private int currentContentNumber = 1;

	/**
	 * Constructor to initialize the HTML report
	 * 
	 * @param reportSettings
	 *            The {@link ReportSettings} object
	 * @param reportTheme
	 *            The {@link ReportTheme} object
	 */
	public HtmlReport(ReportSettings reportSettings, ReportTheme reportTheme) {
		this.reportSettings = reportSettings;
		this.reportTheme = reportTheme;

		testLogPath = reportSettings.getReportPath() + Util.getFileSeparator()
				+ "HTML Results" + Util.getFileSeparator()
				+ reportSettings.getReportName() + ".html";

		resultSummaryPath = reportSettings.getReportPath()
				+ Util.getFileSeparator() + "HTML Results"
				+ Util.getFileSeparator() + "Summary" + ".html";
	}

	private String getThemeCss() {
		return  "\t\t <style type='text/css'> \n" +
					"\t\t\t body { \n" +
						"\t\t\t\t background-color: " + reportTheme.getContentForeColor() +"; \n" +
						"\t\t\t\t font-family: Verdana, Geneva, sans-serif; \n" +
						"\t\t\t\t text-align: center; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t small { \n" +
						"\t\t\t\t font-size: 0.7em; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t table { \n" +
//						"\t\t\t\t border: 1px solid #4D7C7B; \n" +
//						"\t\t\t\t border-collapse: collapse; \n" +
//						"\t\t\t\t border-spacing: 0px; \n" +
						"\t\t\t\t width: 100%; \n" + //95%
						"\t\t\t\t margin-left: auto; \n" + // auto
						"\t\t\t\t margin-right: auto; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t tr.heading { \n" +
						"\t\t\t\t background-color: " + reportTheme.getHeadingBackColor() + "; \n" +
						"\t\t\t\t color: " + reportTheme.getHeadingForeColor() + "; \n" +
						"\t\t\t\t font-size: 0.9em; \n" +
						"\t\t\t\t font-weight: bold; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t tr.subheading { \n" +
						"\t\t\t\t background-color: " + reportTheme.getsubHeadingBackColor() + "; \n" +
						"\t\t\t\t color: " + reportTheme.getsubHeadingForeColor() + "; \n" +
						"\t\t\t\t font-weight: bold; \n" +
						"\t\t\t\t font-size: 0.9em; \n" +
						"\t\t\t\t text-align: justify; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t tr.section { \n" +
						"\t\t\t\t background-color: " + reportTheme.getSectionBackColor() + "; \n" +
						"\t\t\t\t color: " + reportTheme.getSectionForeColor() + "; \n" +
						"\t\t\t\t cursor: pointer; \n" +
						"\t\t\t\t font-weight: bold; \n" +
						"\t\t\t\t font-size: 0.9em; \n" +
						"\t\t\t\t text-align: justify; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t tr.subsection { \n" +
					"\t\t\t\t background-color: " + reportTheme.getsubSectionBackColor() + "; \n" +
						"\t\t\t\t cursor: pointer; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t tr.content { \n" +
					"\t\t\t\t background-color: " + reportTheme.getsubSectionBackColor() + "; \n" +
						"\t\t\t\t color: " + reportTheme.getContentBackColor()+ "; \n" +
						"\t\t\t\t font-size: 0.9em; \n" +
						"\t\t\t\t display: table-row; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t td { \n" +
						"\t\t\t\t padding: 4px; \n" +
						"\t\t\t\t text-align: inherit\\0/; \n" +
						"\t\t\t\t word-wrap: break-word; \n" +
						"\t\t\t\t max-width: 450px; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t th { \n" +
					"\t\t\t\t padding: 4px; \n" +
					"\t\t\t\t text-align: inherit\\0/; \n" +
					"\t\t\t\t word-break: break-all; \n" +
					"\t\t\t\t max-width: 450px; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t td.justified { \n" +
						"\t\t\t\t text-align: justify; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t td.pass { \n" +
						"\t\t\t\t font-weight: bold; \n" +
						"\t\t\t\t color: green; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t td.fail { \n" +
						"\t\t\t\t font-weight: bold; \n" +
						"\t\t\t\t color: red; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t td.done, td.screenshot { \n" +
						"\t\t\t\t font-weight: bold; \n" +
						"\t\t\t\t color: black; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t td.debug { \n" +
						"\t\t\t\t font-weight: bold; \n" +
						"\t\t\t\t color: blue; \n" +
					"\t\t\t } \n\n" +
					
					"\t\t\t td.warning { \n" +
						"\t\t\t\t font-weight: bold; \n" +
						"\t\t\t\t color: orange; \n" +
					"\t\t\t } \n" +					
					"\t\t\t img { \n" +
						"\t\t\t\t width:" + reportSettings.getWidth()  + "; \n" +
						"\t\t\t\t height:"+ reportSettings.getHeight()  + "; \n" +
					"\t\t\t } \n" +
				 "\t\t </style> \n\n";
	}

	private String getJavascriptFunctions() {
		return "\t\t <script> \n"
				
				+"var slideIndex = 1;\r\n" + 
				"				showDivs(slideIndex, objID);\r\n" + 
				"\r\n" + 
				"				function plusDivs(n, objID) {\r\n" + 
				"				  showDivs(slideIndex += n, objID);\r\n" + 
				"				}\r\n" + 
				"\r\n" + 
				"				function showDivs(n, objID) {\r\n" + 
				"				  var i;\r\n" + 
				"				  var x = document.getElementsByClassName(objID);\r\n" + 
				"				  if (n > x.length) {slideIndex = 1} \r\n" + 
				"				  if (n < 1) {slideIndex = x.length} ;\r\n" + 
				"				  for (i = 0; i < x.length; i++) {\r\n" + 
				"					x[i].style.display = \"none\"; \r\n" + 
				"				  }\r\n" + 
				"				  x[slideIndex-1].style.display = \"block\"; \r\n" + 
				"				}"
				+ "\t\t\t function toggleMenu(objID) { \n"
				+ "\t\t\t\t if (!document.getElementById) return; \n"
				+ "\t\t\t\t var ob = document.getElementById(objID).style; \n"
				+ "\t\t\t\t if(ob.display === 'none') { \n"
				+ "\t\t\t\t\t try { \n"
				+ "\t\t\t\t\t\t ob.display='table-row-group'; \n"
				+ "\t\t\t\t\t } catch(ex) { \n"
				+ "\t\t\t\t\t\t ob.display='block'; \n"
				+ "\t\t\t\t\t } \n"
				+ "\t\t\t\t } \n"
				+ "\t\t\t\t else { \n"
				+ "\t\t\t\t\t ob.display='none'; \n"
				+ "\t\t\t\t } \n"
				+ "\t\t\t } \n"
				+

				"\t\t\t function toggleSubMenu(objId) { \n"
				+ "\t\t\t\t for(i=1; i<10000; i++) { \n"
				+ "\t\t\t\t\t var ob = document.getElementById(objId.concat(i)); \n"
				+ "\t\t\t\t\t if(ob === null) { \n" + "\t\t\t\t\t\t break; \n"
				+ "\t\t\t\t\t } \n"
				+ "\t\t\t\t\t if(ob.style.display === 'none') { \n"
				+ "\t\t\t\t\t\t try { \n"
				+ "\t\t\t\t\t\t\t ob.style.display='table-row'; \n"
				+ "\t\t\t\t\t\t } catch(ex) { \n"
				+ "\t\t\t\t\t\t\t ob.style.display='block'; \n"
				+ "\t\t\t\t\t\t } \n" + "\t\t\t\t\t } \n"
				+ "\t\t\t\t\t else { \n"
				+ "\t\t\t\t\t\t ob.style.display='none'; \n"
				+ "\t\t\t\t\t } \n" + "\t\t\t\t } \n" + "\t\t\t } \n"
				+ "\t\t </script> \n";
	}

	/* TEST LOG FUNCTIONS */

	public void initializeTestLog() {
		File testLogFile = new File(testLogPath);
		try {
			testLogFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while creating HTML test log file");
		}

		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(testLogFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FrameworkException("Cannot find HTML test log file");
		}
		PrintStream printStream = new PrintStream(outputStream);

		String testLogHeadSection;
		testLogHeadSection = "<!DOCTYPE html> \n" + "<html> \n"
				+ "\t <head> \n" + "\t\t <meta charset='UTF-8'> \n"
				+ "\t\t <title>" + reportSettings.getProjectName() + " - "
				+ reportSettings.getReportName()
				+ " Automation Execution Results" + "</title> \n\n"
				+ getThemeCss() + getJavascriptFunctions() + "\t </head> \n";

		printStream.println(testLogHeadSection);
		printStream.close();
	}

	public void addTestLogHeading(String heading) {
		if (!isTestLogHeaderTableCreated) {
			createTestLogHeaderTable();
			isTestLogHeaderTableCreated = true;
		}

		BufferedWriter bufferedWriter;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(testLogPath,
					true));

			String testLogHeading = "\t\t\t\t <tr class='heading'> \n"
					+ "\t\t\t\t\t <th colspan='4' style='font-family:Copperplate Gothic; font-size:1.4em;'> \n"
					+ "\t\t\t\t\t\t " + heading + " \n" + "\t\t\t\t\t </th> \n"
					+ "\t\t\t\t </tr> \n";
			bufferedWriter.write(testLogHeading);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while adding heading to HTML test log");
		}
	}

	private void createTestLogHeaderTable() {
		BufferedWriter bufferedWriter;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(testLogPath,
					true));

			String testLogHeaderTable = "\t <body> \n"
					+ "\t\t <table id='header'> \n" + "\t\t\t <thead> \n";
			bufferedWriter.write(testLogHeaderTable);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while adding header table to HTML test log");
		}
	}

	public void addTestLogSubHeading(String subHeading1, String subHeading2,
			String subHeading3, String subHeading4) {
		BufferedWriter bufferedWriter;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(testLogPath,
					true));

			String testLogSubHeading = "\t\t\t\t <tr class='subheading'> \n"
					+ "\t\t\t\t\t <th>&nbsp;"
					+ subHeading1.replace(" ", "&nbsp;") + "</th> \n"
					+ "\t\t\t\t\t <th>&nbsp;"
					+ subHeading2.replace(" ", "&nbsp;") + "</th> \n"
					+ "\t\t\t\t\t <th>&nbsp;"
					+ subHeading3.replace(" ", "&nbsp;") + "</th> \n"
					+ "\t\t\t\t\t <th>&nbsp;"
					+ subHeading4.replace(" ", "&nbsp;") + "</th> \n"
					+ "\t\t\t\t </tr> \n";
			bufferedWriter.write(testLogSubHeading);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while adding sub-heading to HTML test log");
		}
	}

	private void createTestLogMainTable() {
		BufferedWriter bufferedWriter;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(testLogPath,
					true));

			String testLogMainTable = "\t\t\t </thead> \n"
					+ "\t\t </table> \n\n" +

					"\t\t <table id='main'> \n";

			bufferedWriter.write(testLogMainTable);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while adding main table to HTML test log");
		}
	}

	public void addTestLogTableHeadings() {
		if (!isTestLogMainTableCreated) {
			createTestLogMainTable();
			isTestLogMainTableCreated = true;
		}

		BufferedWriter bufferedWriter;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(testLogPath,
					true));

/*			String testLogTableHeading = "\t\t\t <thead> \n"
					+ "\t\t\t\t <tr class='heading'> \n"
					+ "\t\t\t\t\t <th>Step No</th> \n"
					+ "\t\t\t\t\t <th>Step Name</th> \n"
					+ "\t\t\t\t\t <th>Description</th> \n"
					+ "\t\t\t\t\t <th>Status</th> \n"
					+ "\t\t\t\t\t <th>Step Time</th> \n"
					+ "\t\t\t\t\t <th>ScreenShot</th> \n" + "\t\t\t\t </tr> \n"
					+ "\t\t\t </thead> \n\n";
*/		
			String testLogTableHeading = "\t\t\t <thead> \n"
					+ "\t\t\t\t <tr class='heading'> \n"
					+ "\t\t\t\t\t <th>Test Step</th> \n"
					+ "\t\t\t\t\t <th>Action</th> \n"
					+ "\t\t\t\t\t <th>Expected Result</th> \n"
					+ "\t\t\t\t\t <th>Actual Result</th> \n"
					+ "\t\t\t\t\t <th>Pass / Fail</th> \n"
					+ "\t\t\t\t\t <th>Initial / Date</th> \n"
					+ "\t\t\t\t\t <th>ScreenShot</th> \n" + "\t\t\t\t </tr> \n"
					+ "\t\t\t </thead> \n\n";
			
			bufferedWriter.write(testLogTableHeading);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while adding main table headings to HTML test log");
		}
	}

	public void addTestLogSection(String section) {
		String testLogSection = "";
		if (!"".equals(currentSection)) {
			testLogSection = "\t\t\t </tbody>";
		}

		currentSection = section.replaceAll("[^a-zA-Z0-9]", "");

		BufferedWriter bufferedWriter;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(testLogPath,
					true));

			testLogSection += "\t\t\t <tbody> \n"
					+ "\t\t\t\t <tr class='section'> \n"
					//+ "\t\t\t\t\t <td colspan='6' onclick=\"toggleMenu('"
					+ "\t\t\t\t\t <td colspan='7' onclick=\"toggleMenu('"
					+ currentSection + "')\">+ " + section + "</td> \n"
					+ "\t\t\t\t </tr> \n" + "\t\t\t </tbody> \n"
					+ "\t\t\t <tbody id='" + currentSection
					+ "' style='display:table-row-group'> \n";
			bufferedWriter.write(testLogSection);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while adding section to HTML test log");
		}
	}

	public void addTestLogSubSection(String subSection) {
		currentSubSection = subSection.replaceAll("[^a-zA-Z0-9]", "");
		currentContentNumber = 1;

		BufferedWriter bufferedWriter;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(testLogPath,
					true));

			String testLogSubSection = "\t\t\t\t <tr class='subheading subsection'> \n"
					//+ "\t\t\t\t\t <td colspan='6' onclick=\"toggleSubMenu('"
					+ "\t\t\t\t\t <td colspan='7' onclick=\"toggleSubMenu('"
					+ currentSection
					+ currentSubSection
					+ "')\">&nbsp;+ "
					+ subSection + "</td> \n" + "\t\t\t\t </tr> \n";
			bufferedWriter.write(testLogSubSection);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while adding sub-section to HTML test log");
		}
	}

	public void updateTestLog(String stepNumber, String stepName,
			String stepDescription, Status stepStatus, String screenShotName) {
		try {
			/*BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
					testLogPath, true));*/
			
			BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(testLogPath),  StandardCharsets.UTF_8, StandardOpenOption.APPEND);
			//BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(testLogPath, true), StandardCharsets.UTF_8));


			String testStepRow = "\t\t\t\t <tr class='content' id='"
					+ currentSection + currentSubSection + currentContentNumber
					+ "'> \n" + "\t\t\t\t\t <td>" + stepNumber + "</td> \n"
					+ "\t\t\t\t\t <td class='justified'>" + stepName
					+ "</td> \n";
			currentContentNumber++;
			
			switch (stepStatus) {
			case FAIL:
				if (reportSettings.shouldTakeScreenshotFailedStep()) {
					testStepRow += getTestStepWithScreenshotBase64(stepDescription,
							stepStatus, screenShotName);
				} else {
					testStepRow += getTestStepWithoutScreenshot(
							stepDescription, stepStatus);
				}
				break;

			case PASS:
				if (reportSettings.shouldTakeScreenshotPassedStep()) {
					testStepRow += getTestStepWithScreenshotBase64(stepDescription,
							stepStatus, screenShotName);
				} else {
					testStepRow += getTestStepWithoutScreenshot(
							stepDescription, stepStatus);
				}
				break;

			case SCREENSHOT:
				testStepRow += getTestStepWithScreenshotBase64(stepDescription,
						stepStatus, screenShotName);
				break;
				
			case DONE:
				testStepRow += 
				"\t\t\t\t\t <td class='justified'>" +
								stepDescription + 
							"</td> \n" +
				"\t\t\t\t\t <td class='justified'>" +
							stepDescription + 
						"</td> \n" +
				"\t\t\t\t\t <td>" +
			 					stepStatus +
			 				"</td> \n"+
			 	"\t\t\t\t\t <td>"
							+ "<small>"
							+ Util.getCurrentFormattedTime(reportSettings
									.getDateFormatString()) + "</small>" + "</td> \n"+
			"\t\t\t\t\t <td>" + " N/A " +
			 				"</td> \n"+
			"\t\t\t\t </tr> \n";
				break;

			default:
				testStepRow += getTestStepWithoutScreenshot(stepDescription,
						stepStatus);
				break;
			}

			bufferedWriter.write(testStepRow);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while updating HTML test log");
		}
	}

	private synchronized String getTestStepWithScreenshotBase64(String stepDescription, Status stepStatus,
            String screenShotName) {
	     String testStepRow;
	     //ServiceRegister register = ServiceRegister.getInstance();
	
	     FileInputStream fTptStrm = null;
	     String encodedBase64 = null;
	     String screenshotPath = reportSettings.getReportPath() + Util.getFileSeparator()+"Screenshots"
	                  +Util.getFileSeparator()+ screenShotName;
	     //String testCase = register.getService(Thread.currentThread().getName(),"CurrentTestCase").toString();
	     //String screenshotPath = register.getService(Thread.currentThread().getName()+ testCase + "ScreenShotPath", screenShotName).toString();
	     File screenShotFile = new File(screenshotPath);
	     try {
	            fTptStrm = new FileInputStream(screenShotFile);
	            byte[] bytes = new byte[(int) screenShotFile.length()];
	            fTptStrm.read(bytes);
	            encodedBase64 = new String(Base64.encodeBase64(bytes));
	            //register.putService(Thread.currentThread().getName() + testCase + screenshotPath + "EncodeBase64", encodedBase64);
	     } catch (FileNotFoundException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	     } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	     }
	     String result = "data:image/png;base64, " + encodedBase64;		     
	
	     if (reportSettings.shouldLinkScreenshotsToTestLog()) {
	            testStepRow = "\t\t\t\t\t <td class='justified'>" + stepDescription + "</td> \n"
	                         //+ "\t\t\t\t\t <td class='justified'>" + stepDescription + "</td> \n" 
	                         + "\t\t\t\t\t <td class='"
	                         + stepStatus.toString().toLowerCase() + "'>" + stepStatus + "</td> \n" + "\t\t\t\t\t <td>"
	                         + "<small>" + Util.getCurrentFormattedTime(reportSettings.getDateFormatString()) + "</small>"
	                         + "</td> \n" + "\t\t\t\t\t <td>" + "<img src='" + result + "'>" + "</img>" + "</td> \n"
	                         + "\t\t\t\t </tr> \n";
	     } else {
	            testStepRow = "\t\t\t\t\t <td class='justified'>" + stepDescription + "</td> \n"
	                         //+ "\t\t\t\t\t <td class='justified'>" + stepDescription + "</td> \n" 
	                         + "\t\t\t\t\t <td class='"
	                         + stepStatus.toString().toLowerCase() + "'>" + stepStatus + "</td> \n" + "\t\t\t\t\t <td>"
	                         + "<small>" + Util.getCurrentFormattedTime(reportSettings.getDateFormatString()) + "</small>"
	                         + "</td> \n" + "\t\t\t\t\t <td>" + " (Refer Screenshot @ " + screenShotName + ")" + "</td> \n"
	                         + "\t\t\t\t </tr> \n";
	     }
	     
	    //register.removeService(Thread.currentThread().getName() + testCase + screenshotPath, "EncodeBase64");
	     
	     return testStepRow;
	}

	
	private String getTestStepWithScreenshot(String stepDescription, Status stepStatus, String screenShotName) {
		String testStepRow;
		
		if (reportSettings.shouldLinkScreenshotsToTestLog()) {
			testStepRow = 
					"\t\t\t\t\t <td class='justified'>" +
									stepDescription +
								"</td> \n" +
					"\t\t\t\t\t <td class='justified'>" +
								stepDescription +
							"</td> \n" +
					"\t\t\t\t\t <td class='" + stepStatus.toString().toLowerCase() + "'>"+
				 						stepStatus +
				 				"</td> \n"+
				 	"\t\t\t\t\t <td>"
								+ "<small>"
								+ Util.getCurrentFormattedTime(reportSettings
										.getDateFormatString()) + "</small>" + "</td> \n"+				
					"\t\t\t\t\t <td>" + "<img src='..\\Screenshots\\" + screenShotName + "'>" +
			 					"</img>" +
			 				"</td> \n" +
			 		"\t\t\t\t </tr> \n";
		} else {
			testStepRow = 
					"\t\t\t\t\t <td class='justified'>" +
									stepDescription + 
								"</td> \n" +
					"\t\t\t\t\t <td class='justified'>" +
								stepDescription +
							"</td> \n" +
					"\t\t\t\t\t <td class='" + stepStatus.toString().toLowerCase() + "'>" +
				 				stepStatus +
				 				"</td> \n"+
				 	"\t\t\t\t\t <td>"
								+ "<small>"
								+ Util.getCurrentFormattedTime(reportSettings
										.getDateFormatString()) + "</small>" + "</td> \n"+
					"\t\t\t\t\t <td>" + " (Refer Screenshot @ " + screenShotName + ")" +
				 				"</td> \n"+	
					"\t\t\t\t </tr> \n";
		}
		
		return testStepRow;
	}
	
	
	private String getTestStepWithScreenshots(String expectedResults,String actualResults, Status stepStatus, String screenShotName) {
		String testStepRow;
		String strImages="";
		for(int intIndex=1;intIndex<=Integer.parseInt(System.getProperty("ScreenshotsCount"));intIndex++) {
			if(intIndex==1) {
				strImages = strImages+"<img class='"+screenShotName.replaceAll(".png", "")+"' style='display:block' src='..\\Screenshots\\" + screenShotName + "'>" +
	 					"</img>";
			}
			else {
				strImages = strImages+"<img class='"+screenShotName.replaceAll(".png", "")+"' style='display:none' src='..\\Screenshots\\" + screenShotName.replaceAll(".png", "") + "_scroll"+(intIndex-1)+".png'>" +
	 					"</img>";
			}			
		}
		if (reportSettings.shouldLinkScreenshotsToTestLog()) {
			testStepRow = 
					"\t\t\t\t\t <td class='justified'>" +
							expectedResults +
					"</td> \n" +
		"\t\t\t\t\t <td class='justified'>" +
							actualResults +
				"</td> \n" +
		"\t\t\t\t\t <td class='" + stepStatus.toString().toLowerCase() + "'>"+
	 						stepStatus +
	 				"</td> \n"+
	 	"\t\t\t\t\t <td>"
					+ "<small>"
								+ Util.getCurrentFormattedTime(reportSettings
										.getDateFormatString()) + "</small>" + "</td> \n"+				
					"\t\t\t\t\t <td><div>"+strImages+
					"<div style=\"width:100%\">\r\n" + 
					"					<div onclick=\"plusDivs(-1, '"+screenShotName.replaceAll(".png", "")+"')\">&#10094;</div>\r\n" + 
					"					<div onclick=\"plusDivs(1, '"+screenShotName.replaceAll(".png", "")+"')\">&#10095;</div>\r\n" + 
					"					<span 	onclick=\"currentDiv(1, '"+screenShotName.replaceAll(".png", "")+"')\"></span>					\r\n" + 
					"  </div>"+
			 				"</td> \n" +
			 		"\t\t\t\t </tr> \n";
		} else {
					testStepRow = 
					"\t\t\t\t\t <td class='justified'>" +
							expectedResults +
							"</td> \n" +
					"\t\t\t\t\t <td class='justified'>" +
							actualResults +
							"</td> \n" +
					"\t\t\t\t\t <td class='" + stepStatus.toString().toLowerCase() + "'>" +
				 				stepStatus +
				 				"</td> \n"+
				 	"\t\t\t\t\t <td>"
								+ "<small>"
								+ Util.getCurrentFormattedTime(reportSettings
										.getDateFormatString()) + "</small>" + "</td> \n"+
					"\t\t\t\t\t <td>" + " (Refer Screenshot @ " + screenShotName + ")" +
				 				"</td> \n"+	
					"\t\t\t\t </tr> \n";
		}
		
		return testStepRow;
	}

	private String getTestStepWithoutScreenshot(String stepDescription,
			Status stepStatus) {
		String testStepRow;

		testStepRow = "\t\t\t\t\t <td class='justified'>"
				+ stepDescription
				+ "</td> \n"
				+ "\t\t\t\t\t <td class='justified'>"
				+ stepDescription
				+ "</td> \n"
				+ "\t\t\t\t\t <td class='"
				+ stepStatus.toString().toLowerCase()
				+ "'>"
				+ stepStatus
				+ "</td> \n"
				+ "\t\t\t\t\t <td>"
				+ "<small>"
				+ Util.getCurrentFormattedTime(reportSettings
						.getDateFormatString()) + "</small>" + "</td> \n"
				+ "\t\t\t\t\t <td>" + " N/A " + "</td> \n"
				+ "\t\t\t\t </tr> \n";

		return testStepRow;
	}


	public void updateTestLog(String stepNumber, String stepDescription,
			String expectedResults,String actualResults, Status stepStatus, String screenShotName) {
		try {
			/*BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
					testLogPath, true));*/
			BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(testLogPath),  StandardCharsets.UTF_8, StandardOpenOption.APPEND);
			//BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(testLogPath, true), StandardCharsets.UTF_8));

			String testStepRow = "\t\t\t\t <tr class='content' id='"
					+ currentSection + currentSubSection + currentContentNumber
					+ "'> \n" + "\t\t\t\t\t <td>" + stepNumber + "</td> \n"
					+ "\t\t\t\t\t <td class='justified'>" + stepDescription
					+ "</td> \n";
			currentContentNumber++;
			
			switch (stepStatus) {
			case FAIL:
				if (reportSettings.shouldTakeScreenshotFailedStep()) {
					testStepRow += getTestStepWithScreenshotBase64(expectedResults,actualResults,
							stepStatus, screenShotName);
				} else {
					testStepRow += getTestStepWithoutScreenshot(
							expectedResults,actualResults, stepStatus);
				}
				break;

			case PASS:
				if (reportSettings.shouldTakeScreenshotPassedStep()) {
					testStepRow += getTestStepWithScreenshotBase64(expectedResults,actualResults,
							stepStatus, screenShotName);
				} else {
					testStepRow += getTestStepWithoutScreenshot(
							stepDescription, stepStatus);
				}
				break;

			case SCREENSHOT:
				testStepRow += getTestStepWithScreenshotBase64(stepDescription,
						stepStatus, screenShotName);
				break;
				
			case DONE:
				testStepRow += 
				"\t\t\t\t\t <td class='justified'>" +
							expectedResults + 
							"</td> \n" +
				"\t\t\t\t\t <td class='justified'>" +
							actualResults + 
						"</td> \n" +
				"\t\t\t\t\t <td>" +
			 					stepStatus +
			 				"</td> \n"+
			 	"\t\t\t\t\t <td>"
							+ "<small>"
							+ Util.getCurrentFormattedTime(reportSettings
									.getDateFormatString()) + "</small>" + "</td> \n"+
			"\t\t\t\t\t <td>" + " N/A " +
			 				"</td> \n"+
			"\t\t\t\t </tr> \n";
				break;

			default:
				testStepRow += getTestStepWithoutScreenshot(stepDescription,
						stepStatus);
				break;
			}

			bufferedWriter.write(testStepRow);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while updating HTML test log");
		}
	}
	
	public void updateTestLogs(String stepNumber, String stepDescription,
			String expectedResults,String actualResults, Status stepStatus, String screenShotName) {
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
					testLogPath, true));

			String testStepRow = "\t\t\t\t <tr class='content' id='"
					+ currentSection + currentSubSection + currentContentNumber
					+ "'> \n" + "\t\t\t\t\t <td>" + stepNumber + "</td> \n"
					+ "\t\t\t\t\t <td class='justified'>" + stepDescription
					+ "</td> \n";
			currentContentNumber++;
			
			switch (stepStatus) {
			case FAIL:
				if (reportSettings.shouldTakeScreenshotFailedStep()) {
					testStepRow += getTestStepWithScreenshots(expectedResults,actualResults,
							stepStatus, screenShotName);
				} else {
					testStepRow += getTestStepWithoutScreenshot(
							expectedResults,actualResults, stepStatus);
				}
				break;

			case PASS:
				if (reportSettings.shouldTakeScreenshotPassedStep()) {
					testStepRow += getTestStepWithScreenshots(expectedResults,actualResults,
							stepStatus, screenShotName);
				} else {
					testStepRow += getTestStepWithoutScreenshot(
							stepDescription, stepStatus);
				}
				break;

			case SCREENSHOT:
				testStepRow += getTestStepWithScreenshot(stepDescription,
						stepStatus, screenShotName);
				break;
				
			case DONE:
				testStepRow += 
				"\t\t\t\t\t <td class='justified'>" +
							expectedResults + 
							"</td> \n" +
				"\t\t\t\t\t <td class='justified'>" +
							actualResults + 
						"</td> \n" +
				"\t\t\t\t\t <td>" +
			 					stepStatus +
			 				"</td> \n"+
			 	"\t\t\t\t\t <td>"
							+ "<small>"
							+ Util.getCurrentFormattedTime(reportSettings
									.getDateFormatString()) + "</small>" + "</td> \n"+
			"\t\t\t\t\t <td>" + " N/A " +
			 				"</td> \n"+
			"\t\t\t\t </tr> \n";
				break;

			default:
				testStepRow += getTestStepWithoutScreenshot(stepDescription,
						stepStatus);
				break;
			}

			bufferedWriter.write(testStepRow);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException("Error while updating HTML test log");
		}
	}
	
	@SuppressWarnings("unused")
	private String getTestStepWithScreenshot(String expectedResults,String actualResults, Status stepStatus, String screenShotName) {
		String testStepRow;
		
		if (reportSettings.shouldLinkScreenshotsToTestLog()) {
			testStepRow = 
					"\t\t\t\t\t <td class='justified'>" +
										expectedResults +
								"</td> \n" +
					"\t\t\t\t\t <td class='justified'>" +
										actualResults +
							"</td> \n" +
					"\t\t\t\t\t <td class='" + stepStatus.toString().toLowerCase() + "'>"+
				 						stepStatus +
				 				"</td> \n"+
				 	"\t\t\t\t\t <td>"
								+ "<small>"
								+ Util.getCurrentFormattedTime(reportSettings
										.getDateFormatString()) + "</small>" + "</td> \n"+				
					"\t\t\t\t\t <td>" + "<img src='..\\Screenshots\\" + screenShotName + "'>" +
			 					"</img>" +
			 				"</td> \n" +
			 		"\t\t\t\t </tr> \n";
		} else {
			testStepRow = 
					"\t\t\t\t\t <td class='justified'>" +
							expectedResults +
							"</td> \n" +
					"\t\t\t\t\t <td class='justified'>" +
							actualResults +
							"</td> \n" +
					"\t\t\t\t\t <td class='" + stepStatus.toString().toLowerCase() + "'>" +
				 				stepStatus +
				 				"</td> \n"+
				 	"\t\t\t\t\t <td>"
								+ "<small>"
								+ Util.getCurrentFormattedTime(reportSettings
										.getDateFormatString()) + "</small>" + "</td> \n"+
					"\t\t\t\t\t <td>" + " (Refer Screenshot @ " + screenShotName + ")" +
				 				"</td> \n"+	
					"\t\t\t\t </tr> \n";
		}
		
		return testStepRow;
	}

	private String getTestStepWithoutScreenshot(String expectedResults,String actualResults,
			Status stepStatus) {
		String testStepRow;

		testStepRow = "\t\t\t\t\t <td class='justified'>"
				+ expectedResults
				+ "</td> \n"
				+ "\t\t\t\t\t <td class='justified'>"
				+ actualResults
				+ "</td> \n"
				+ "\t\t\t\t\t <td class='"
				+ stepStatus.toString().toLowerCase()
				+ "'>"
				+ stepStatus
				+ "</td> \n"
				+ "\t\t\t\t\t <td>"
				+ "<small>"
				+ Util.getCurrentFormattedTime(reportSettings
						.getDateFormatString()) + "</small>" + "</td> \n"
				+ "\t\t\t\t\t <td>" + " N/A " + "</td> \n"
				+ "\t\t\t\t </tr> \n";

		return testStepRow;
	}
	
	
	
	
	
	
	
	public void addTestLogFooter(String executionTime, int nStepsPassed,
			int nStepsFailed) {
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
					testLogPath, true));

			String testLogFooter = "\t\t\t </tbody> \n" + "\t\t </table> \n\n" +

			"\t\t <table id='footer'> \n" + "\t\t\t <colgroup> \n"
					+ "\t\t\t\t <col style='width: 25%' /> \n"
					+ "\t\t\t\t <col style='width: 25%' /> \n"
					+ "\t\t\t\t <col style='width: 25%' /> \n"
					+ "\t\t\t\t <col style='width: 25%' /> \n"
					+ "\t\t\t </colgroup> \n\n" +

					"\t\t\t <tfoot> \n" + "\t\t\t\t <tr class='heading'> \n"
					+ "\t\t\t\t\t <th colspan='4'>Execution Duration: "
					+ executionTime + "</th> \n" + "\t\t\t\t </tr> \n"
					+ "\t\t\t\t <tr class='subheading'> \n"
					+ "\t\t\t\t\t <td class='pass'>&nbsp;Steps passed</td> \n"
					+ "\t\t\t\t\t <td class='pass'>&nbsp;: " + nStepsPassed
					+ "</td> \n"
					+ "\t\t\t\t\t <td class='fail'>&nbsp;Steps failed</td> \n"
					+ "\t\t\t\t\t <td class='fail'>&nbsp;: " + nStepsFailed
					+ "</td> \n" + "\t\t\t\t </tr> \n" + "\t\t\t </tfoot> \n"
					+ "\t\t </table> \n" + "\t </body> \n" + "</html>";

			bufferedWriter.write(testLogFooter);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while adding footer to HTML test log");
		}
	}

	/* RESULT SUMMARY FUNCTIONS */

	public void initializeResultSummary() {
		File resultSummaryFile = new File(resultSummaryPath);

		try {
			resultSummaryFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while creating HTML result summary file");
		}

		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(resultSummaryFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new FrameworkException("Cannot find HTML result summary file");
		}
		PrintStream printStream = new PrintStream(outputStream);

		String resultSummaryHeader;
		resultSummaryHeader = "<!DOCTYPE html> \n" + "<html> \n"
				+ "\t <head> \n" + "\t\t <meta charset='UTF-8'> \n"
				+ "\t\t <title>" + reportSettings.getProjectName()
				+ " - Automation Execution Results Summary" + "</title> \n\n"
				+ getThemeCss() + getJavascriptFunctions() + "\t </head> \n";

		printStream.println(resultSummaryHeader);
		printStream.close();
	}

	public void addResultSummaryHeading(String heading) {
		if (!isResultSummaryHeaderTableCreated) {
			createResultSummaryHeaderTable();
			isResultSummaryHeaderTableCreated = true;
		}

		BufferedWriter bufferedWriter;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(
					resultSummaryPath, true));

			String resultSummaryHeading = "\t\t\t\t <tr class='heading'> \n"
					+ "\t\t\t\t\t <th colspan='4' style='font-family:Copperplate Gothic; font-size:1.4em;'> \n"
					+ "\t\t\t\t\t\t " + heading + " \n" + "\t\t\t\t\t </th> \n"
					+ "\t\t\t\t </tr> \n";
			bufferedWriter.write(resultSummaryHeading);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while adding heading to HTML result summary");
		}
	}

	private void createResultSummaryHeaderTable() {
		BufferedWriter bufferedWriter;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(
					resultSummaryPath, true));

			String resultSummaryHeaderTable = "\t <body> \n"
					+ "\t\t <table id='header'> \n" + "\t\t\t <thead> \n";
			bufferedWriter.write(resultSummaryHeaderTable);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while adding header table to HTML result summary");
		}
	}

	public void addResultSummarySubHeading(String subHeading1,
			String subHeading2, String subHeading3, String subHeading4) {
		BufferedWriter bufferedWriter;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(
					resultSummaryPath, true));

			String resultSummarySubHeading = "\t\t\t\t <tr class='subheading'> \n"
					+ "\t\t\t\t\t <th>&nbsp;"
					+ subHeading1.replace(" ", "&nbsp;")
					+ "</th> \n"
					+ "\t\t\t\t\t <th>&nbsp;"
					+ subHeading2.replace(" ", "&nbsp;")
					+ "</th> \n"
					+ "\t\t\t\t\t <th>&nbsp;"
					+ subHeading3.replace(" ", "&nbsp;")
					+ "</th> \n"
					+ "\t\t\t\t\t <th>&nbsp;"
					+ subHeading4.replace(" ", "&nbsp;")
					+ "</th> \n"
					+ "\t\t\t\t </tr> \n";
			bufferedWriter.write(resultSummarySubHeading);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while adding sub-heading to HTML result summary");
		}
	}

	private void createResultSummaryMainTable() {
		BufferedWriter bufferedWriter;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(
					resultSummaryPath, true));

			String resultSummaryMainTable = "\t\t\t </thead> \n"
					+ "\t\t </table> \n\n" +

					"\t\t <table id='main'> \n" + "\t\t\t <colgroup> \n";

			bufferedWriter.write(resultSummaryMainTable);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while adding main table to HTML result summary");
		}
	}

	public void addResultSummaryTableHeadings() {
		if (!isResultSummaryMainTableCreated) {
			createResultSummaryMainTable();
			isResultSummaryMainTableCreated = true;
		}

		BufferedWriter bufferedWriter;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(
					resultSummaryPath, true));

			String resultSummaryTableHeading = "\t\t\t <thead> \n"
					+ "\t\t\t\t <tr class='heading'> \n"
					+ "\t\t\t\t\t <th>Test Scenario</th> \n"
					+ "\t\t\t\t\t <th>Test Case</th> \n"
					+ "\t\t\t\t\t <th>Test Instance</th> \n"
					+ "\t\t\t\t\t <th>Test Description</th> \n"
					+ "\t\t\t\t\t <th>Additional Details</th> \n"
					+ "\t\t\t\t\t <th>Execution Time</th> \n"
					+ "\t\t\t\t\t <th>Test Status</th> \n"
					+ "\t\t\t\t </tr> \n" + "\t\t\t </thead> \n\n";
			bufferedWriter.write(resultSummaryTableHeading);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while adding main table headings to HTML result summary");
		}
	}

	public void updateResultSummary(TestParameters testParameters,
			String testReportName, String executionTime, String testStatus) {
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
					resultSummaryPath, true));

			String testcaseRow;
			String scenarioName = testParameters.getCurrentScenario();
			String testcaseName = testParameters.getCurrentTestcase();
			String testInstanceName = testParameters.getCurrentTestInstance();
			String testcaseDescription = testParameters
					.getCurrentTestDescription();
			String additionalDetails = testParameters.getAdditionalDetails();

			if (reportSettings.shouldLinkTestLogsToSummary()) {
				testcaseRow = "\t\t\t\t <tr class='content' > \n"
						+ "\t\t\t\t\t <td class='justified'>" + scenarioName
						+ "</td> \n" + "\t\t\t\t\t <td class='justified'>"
						+ testcaseName + "</td> \n"
						+ "\t\t\t\t\t <td class='justified'><a href='"
						+ testReportName + ".html' " + "target='about_blank'>"
						+ testInstanceName + "</a>" + "</td> \n"
						+ "\t\t\t\t\t <td class='justified'>"
						+ testcaseDescription + "</td> \n"
						+ "\t\t\t\t\t <td class='justified'>"
						+ additionalDetails + "</td> \n" + "\t\t\t\t\t <td>"
						+ executionTime + "</td> \n";
			} else {
				testcaseRow = "\t\t\t\t <tr class='content' > \n"
						+ "\t\t\t\t\t <td class='justified'>" + scenarioName
						+ "</td> \n" + "\t\t\t\t\t <td class='justified'>"
						+ testcaseName + "</td> \n"
						+ "\t\t\t\t\t <td class='justified'>"
						+ testInstanceName + "</td> \n"
						+ "\t\t\t\t\t <td class='justified'>"
						+ testcaseDescription + "</td> \n"
						+ "\t\t\t\t\t <td class='justified'>"
						+ additionalDetails + "</td> \n" + "\t\t\t\t\t <td>"
						+ executionTime + "</td> \n";
			}

			if ("passed".equalsIgnoreCase(testStatus)) {
				testcaseRow += "\t\t\t\t\t <td class='pass'>" + testStatus
						+ "</td> \n" + "\t\t\t\t </tr> \n";
			} else {
				testcaseRow += "\t\t\t\t\t <td class='fail'>" + testStatus
						+ "</td> \n" + "\t\t\t\t </tr> \n";
			}

			bufferedWriter.write(testcaseRow);
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while updating HTML result summary");
		}
	}

	public void addResultSummaryFooter(String totalExecutionTime,
			int nTestsPassed, int nTestsFailed) {
		try {
			BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(
					resultSummaryPath, true));

			String resultSummaryFooter = "\t\t\t </tbody> \n"
					+ "\t\t </table> \n\n" +

					"\t\t <table id='footer'> \n" + "\t\t\t <colgroup> \n"
					+ "\t\t\t\t <col style='width: 25%' /> \n"
					+ "\t\t\t\t <col style='width: 25%' /> \n"
					+ "\t\t\t\t <col style='width: 25%' /> \n"
					+ "\t\t\t\t <col style='width: 25%' /> \n"
					+ "\t\t\t </colgroup> \n\n" +

					"\t\t\t <tfoot> \n" + "\t\t\t\t <tr class='heading'> \n"
					+ "\t\t\t\t\t <th colspan='4'>Total Duration: "
					+ totalExecutionTime + "</th> \n" + "\t\t\t\t </tr> \n"
					+ "\t\t\t\t <tr class='subheading'> \n"
					+ "\t\t\t\t\t <td class='pass'>&nbsp;Tests passed</td> \n"
					+ "\t\t\t\t\t <td class='pass'>&nbsp;: " + nTestsPassed
					+ "</td> \n"
					+ "\t\t\t\t\t <td class='fail'>&nbsp;Tests failed</td> \n"
					+ "\t\t\t\t\t <td class='fail'>&nbsp;: " + nTestsFailed
					+ "</td> \n" + "\t\t\t\t </tr> \n" + "\t\t\t </tfoot> \n"
					+ "\t\t </table> \n" + "\t </body> \n" + "</html>";

			bufferWriter.write(resultSummaryFooter);
			bufferWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new FrameworkException(
					"Error while adding footer to HTML result summary");
		}
	}
	
	
	public void updateTestLog(String string, String endPoint, Object expectedValue, Object actualValue,
			Status stepStatus) {
		// TODO Auto-generated method stub
		
	}

	private synchronized String getTestStepWithScreenshotBase64(String expectedResults,String actualResults, Status stepStatus, String screenShotName) {
	     String testStepRow;
	     //ServiceRegister register = ServiceRegister.getInstance();

	     FileInputStream fTptStrm = null;
	     String encodedBase64 = null;
	     String screenshotPath = reportSettings.getReportPath() + Util.getFileSeparator()+"Screenshots"
	                  +Util.getFileSeparator()+ screenShotName;
	     //String testCase = register.getService(Thread.currentThread().getName(),"CurrentTestCase").toString();
	     //String screenshotPath = register.getService(Thread.currentThread().getName()+ testCase + "ScreenShotPath", screenShotName).toString();
	     File screenShotFile = new File(screenshotPath);
	     try {
	            fTptStrm = new FileInputStream(screenShotFile);
	            byte[] bytes = new byte[(int) screenShotFile.length()];
	            fTptStrm.read(bytes);
	            encodedBase64 = new String(Base64.encodeBase64(bytes));
	            //register.putService(Thread.currentThread().getName() + testCase + screenshotPath + "EncodeBase64", encodedBase64);
	     } catch (FileNotFoundException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	     } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	     }
	     String result = "data:image/png;base64, " + encodedBase64;

	    // Reporter.log("<img src='" + result + "' height='300' width='500'/>");

	     if (reportSettings.shouldLinkScreenshotsToTestLog()) {
	                     testStepRow = 
	                                  "\t\t\t\t\t <td class='justified'>" +
	                                                                     expectedResults +
	                                                       "</td> \n" +
	                                  "\t\t\t\t\t <td class='justified'>" +
	                                                                     actualResults +
	                                                "</td> \n" +
	                                  "\t\t\t\t\t <td class='" + stepStatus.toString().toLowerCase() + "'>"+
	                                                                   stepStatus +
	                                                     "</td> \n"+
	                                 "\t\t\t\t\t <td>"
	                                                       + "<small>"
	                                                       + Util.getCurrentFormattedTime(reportSettings
	                                                                     .getDateFormatString()) + "</small>" + "</td> \n"+                       
	                                  "\t\t\t\t\t <td>" + "<img src='" + result + "'>" +
	                                                      "</img>" +
	                                               "</td> \n" +
	                                  "\t\t\t\t </tr> \n";
	              } else {
	                     testStepRow = 
	                                  "\t\t\t\t\t <td class='justified'>" +
	                                                expectedResults +
	                                                "</td> \n" +
	                                  "\t\t\t\t\t <td class='justified'>" +
	                                                actualResults +
	                                                "</td> \n" +
	                                  "\t\t\t\t\t <td class='" + stepStatus.toString().toLowerCase() + "'>" +
	                                                     stepStatus +
	                                                     "</td> \n"+
	                                 "\t\t\t\t\t <td>"
	                                                       + "<small>"
	                                                       + Util.getCurrentFormattedTime(reportSettings
	                                                                     .getDateFormatString()) + "</small>" + "</td> \n"+
	                                  "\t\t\t\t\t <td>" + " (Refer Screenshot @ " + screenShotName + ")" +
	                                                     "</td> \n"+  
	                                  "\t\t\t\t </tr> \n";
	              }
	     
	    //register.removeService(Thread.currentThread().getName() + testCase + screenshotPath, "EncodeBase64");
	     
	     return testStepRow;
	}



}