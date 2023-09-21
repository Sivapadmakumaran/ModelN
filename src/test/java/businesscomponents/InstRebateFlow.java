package businesscomponents;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.cognizant.craft.ScriptHelper;
import com.cognizant.framework.Status;
import com.cognizant.framework.Util;
import com.cognizant.framework.XMLDataAccess;

import config.FrameworkAssertion;
import models.EntriesMediAck;
import models.ImportInvoice;
import models.McdCheckAck;
import pages.Common;


public class InstRebateFlow extends CommonFunctions{

	public InstRebateFlow(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}
	public final long lngMinTimeOutInSeconds = Long.parseLong(properties.getProperty("MinObjectSyncTimeout"));
	public final long lngCtrlTimeOutInSeconds = Long.parseLong(properties.getProperty("UploadControlTimeout"));
	public final long jobTimeOut = Long.parseLong(properties.getProperty("JobTimeOut"));
	public final long staleTableTimeOut = Long.parseLong(properties.getProperty("StaleTableTimeOut"));
	public int lastRows;
	public String ndcProductForFirstRow="";
	public String ndcProductForSecondRow="";

	
	/**
	 * Function to call Fill form Utility to Rebates Calculation
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void validateRebatesCalculation() {
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "Validate Rebates Calculation";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	/**
	 * method to Process Rebate Calculation Status
	 
	 * @param No parameter
	 * @return No return value

	 */
	public void processofRebateStatus() throws Exception {
		
			
			moveToProcessRebate();
			
			moveToReviewedRebate();
			
					

	}
	
	/**
	 * Function to call Fill form Utility to Rebates Calculation
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void validateFinalPayment() {
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "validateFinalPayment";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	
	
	/**
	 * method to check Rebate to Move Reviewed

	 * @param No parameter
	 * @return No return value

	 */
	public void moveToReviewedRebate() throws Exception {


		selectDropdownByvalue(Common.rebateTableStatus,"Status", "In Process", driver.getTitle(), true);
		clickByJs(Common.filterButton, lngMinTimeOutInSeconds, "Filter", "Button", driver.getTitle(), true);
		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loader", "Loader", driver.getTitle());
		String strPaymentLink=dataTable.getData("Parametrized_Checkpoints","RebateProgram#");
		Common tableCheckbox=new Common(strPaymentLink);
		driverUtil.waitUntilElementVisible(Common.rebateSelectAllCheckbox, lngMinTimeOutInSeconds);
		List<WebElement> selectAll = driver.findElements(Common.rebateSelectAllCheckbox);

		if (selectAll.size() > 0) {
			click(selectAll.get(0), lngMinTimeOutInSeconds, "Select All", "CheckBox", driver.getTitle(), true);
		} else {
			selectCheckBoxForStatus();
		}

		Common processButton=new Common("Review");
		clickByJs(processButton.button, lngMinTimeOutInSeconds, "Review", "Button", driver.getTitle(), true);
		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loader", "Loader", driver.getTitle());


		handleAlertYes("ConfirmReview", "Yes", "alert");
		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loader", "Loader", driver.getTitle());


		waitForRebateBackground();

		selectDropdownByvalue(Common.rebateTableStatus,"Status", "Reviewed", driver.getTitle(), true);
		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loader", "Loader", driver.getTitle());

		clickByJs(Common.filterButton, lngMinTimeOutInSeconds, "Filter", "Button", driver.getTitle(), true);
		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loader", "Loader", driver.getTitle());

		boolean isReviewed = validateTotalRebateAmount();
		if (isReviewed) {

			ALMFunctions.UpdateReportLogAndALMForPassStatus("Reviewed validation",
					"Reviewed row should be available successfully",
					"Reviewed row is available successfully", true);

		} else {

			ALMFunctions.UpdateReportLogAndALMForFailStatus("Reviewed validation",
					"Reviewed row should be available successfully",
					"Reviewed row is not available", true);
			throw new FrameworkAssertion("Reviewed amount not found");
		}

		String strProgramType=dataTable.getData("Data","Rebate Program Type");
		if(strProgramType.equalsIgnoreCase("Wholesaler Service Fee")) {
			selectDropdownByvalue(Common.rebateTableStatus,"Status", "Due", driver.getTitle(), true);
			clickByJs(Common.filterButton, lngMinTimeOutInSeconds, "Filter", "Button", driver.getTitle(), true);
			driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loader", "Loader", driver.getTitle());
			selectCheckBoxForStatus();
			Common Button=new Common("Remove");
			clickByJs(Button.button, lngMinTimeOutInSeconds, "Remove", "Button", driver.getTitle(), true);
			driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loader", "Loader", driver.getTitle());
			handleAlertOk("ConfirmReview", "OK", "alert");

		}

	}
	/**
	 * method to check Rebate to Move In Process
	 
	 * @param No parameter
	 * @return No return value

	 */
	public void moveToProcessRebate() throws Exception {

		String strPaymentLink=dataTable.getData("Parametrized_Checkpoints","RebateProgram#");
		String strProgramType=dataTable.getData("Data","Rebate Program Type");
		List<WebElement> selectAll = null;
		if(strProgramType.equalsIgnoreCase("Wholesaler Service Fee")) {
			Common	tableCheckbox=new Common(strPaymentLink+"-01");
			WebElement elm = driver.findElement(tableCheckbox.tableCheckBox);
			click(elm, lngMinTimeOutInSeconds, strPaymentLink+"-01", "Checkbox", driver.getTitle(), true);

		}
		else
		{
			
			driverUtil.waitUntilElementVisible(Common.rebateSelectAllCheckbox, lngMinTimeOutInSeconds);
			selectAll =	driver.findElements(Common.rebateSelectAllCheckbox);
			if (selectAll.size() > 0) {
				click(selectAll.get(0), lngMinTimeOutInSeconds, "Select All", "CheckBox", driver.getTitle(), true);
			} else {
				selectCheckBoxForStatus();
			}
		}
		
		
		Common processButton=new Common("Process");
		clickByJs(processButton.button, lngMinTimeOutInSeconds, "Process", "Button", driver.getTitle(), true);
		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loader", "Loader", driver.getTitle());

		handleAlertOk("ProcessRebatePrograms", "Ok", "alert");

		selectDropdownByvalue(Common.rebateTableStatus,"Status", "In Process", driver.getTitle(), true);
		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loader", "Loader", driver.getTitle());


		clickByJs(Common.filterButton, lngMinTimeOutInSeconds, "Filter", "Button", driver.getTitle(), true);
		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loader", "Loader", driver.getTitle());

		waitForRebateBackground();
		boolean isProcessed = validateTotalRebateAmount();
		if (isProcessed) {

			ALMFunctions.UpdateReportLogAndALMForPassStatus("In Process validation",
					"In Process row should be available successfully",
					"In Process row is available successfully", true);

		} else {

			ALMFunctions.UpdateReportLogAndALMForFailStatus("In Process validation",
					"In Process row should be available successfully",
					"In Process row is not available", true);
			throw new FrameworkAssertion("In Process amount not found");
		}
	}
	
	/**
	 * method to Select Checkbox for status
	 
	 * @param No parameter
	 * @return No return value

	 */
	public void selectCheckBoxForStatus() {

		driverUtil.waitUntilElementVisible(By.xpath("//div[contains(@class, 'vScroll')]//tr"), lngMinTimeOutInSeconds);
		List<WebElement> dueRows = driver.findElements(By.xpath("//div[contains(@class, 'vScroll')]//tr"));

		for (int index = 0; index < dueRows.size(); index++) {
			WebElement inputElement = driver.findElement(By.xpath("//div[contains(@class, 'vRestrict')]//tr[" + (index + 1) + "]//input[@type='checkbox']"));
			driverUtil.waitUntilStalenessOfTableElement(inputElement, driver.getTitle());
			pagescroll(inputElement, driver.getTitle());
			click(inputElement, lngMinTimeOutInSeconds, "Table CheckBox"+index, "CheckBox",  driver.getTitle(), false);
			
		}
	}
	/**
	 * method to Validate total rebate amount
	 
	 * @param No parameter
	 * @return return boolean value

	 */
	public boolean validateTotalRebateAmount() {
		By refreshBy = Common.refresh;
		By visibleBy= Common.dueAmountField;
		String strExpectedStatus="$0.00";
		try {
			int intCount = 0;
			boolean isVisible = false;
			boolean isFound = false;
			while (!isVisible) {
				driver.switchTo().defaultContent();
				
				click(refreshBy, lngMinTimeOutInSeconds, "Refresh", "Button", driver.getTitle(), true);
				
				++intCount;

				frameSwitchAndSwitchBack(Common.documentFrame,"documentframe", driver.getTitle());
				List<WebElement> elements = driver.findElements(visibleBy);
				String visibleText = "";
				if (elements.size() > 0) {
					visibleText = elements.get(0).getText().trim();
					if (visibleText == null || visibleText.isEmpty()) {
						isVisible = false;
					} else {
						String strActualStatus = elements.get(0).getText();
						strActualStatus = strActualStatus.replaceAll("\\$", "").replaceAll("\\,", "");

						if (Double.parseDouble(strActualStatus) == 0D) {

							if (intCount <= 100) {
								isVisible = false;
								isFound = false;
							} else {
								isVisible = true;
								isFound = false;
							}
						} else {
							isVisible = true;
							isFound = true;
						}
					}
				}
			}

			return isFound;
		} catch (TimeoutException e) {
			return false;

		}

	}
	/**
	 * method to verify background job alert
	 *
	 */
	public void waitForRebateBackground() {
		
		try {
			String strJobAlertMsg = dataTable.getData("Parametrized_Checkpoints", "Rebate BackGround Job");
			Common objJobAlert = new Common(strJobAlertMsg);
			By refreshBy = Common.refresh;
			By alertBy = objJobAlert.label;
			
			Wait<WebDriver> wait = new WebDriverWait(driver.getWebDriver(), lngMinTimeOutInSeconds);

			boolean isVisible = true;
			while (isVisible) {
				driver.switchTo().defaultContent();
				click(refreshBy, lngMinTimeOutInSeconds, "Refresh", "Button", driver.getTitle(), true);
				frameSwitchAndSwitchBack(Common.documentFrame,"documentframe", driver.getTitle());
				
				WebElement e = wait.until(ExpectedConditions.visibilityOfElementLocated(alertBy));
				if (e != null) {
					
				}
				isVisible = (e != null);
			}
		} catch (TimeoutException e) {
			return;
		}

	}
	/**
	 * method to Handle alert popup yes button
	 
	 * @param No parameter
	 * @return No return value

	 */
	private void handleAlertYes(String strElementName, String strValues, String strElementType) {
		
		driverUtil.waitUntilElementVisible(Common.alertYes, lngMinTimeOutInSeconds);
		List<WebElement> elements = driver.findElements(Common.alertYes);
		if (elements.size() > 0) {
			click(Common.alertYes, lngMinTimeOutInSeconds, strElementName,strElementType, driver.getTitle(), true);
		}
	}
	/**
	 * method to Handle alert popup ok button
	 
	 * @param No parameter
	 * @return No return value

	 */
	private void handleAlertOk(String strElementName, String strValues, String strElementType) {
		
		driverUtil.waitUntilElementVisible(Common.alertOk, lngMinTimeOutInSeconds);
		List<WebElement> elements = driver.findElements(Common.alertOk);
		if (elements.size() > 0) {
			click(Common.alertOk, lngMinTimeOutInSeconds, strElementName,strElementType, driver.getTitle(), true);
		}
	}
	/**
	 * method to validate total errors
	 
	 * @param No parameter
	 * @return return boolean value

	 */
	public boolean validateRebateTotalErrors() {

		try {
			By refreshBy = Common.refresh;
			By endDateBy = new Common("End Date:").labelValue;
			By totalErrorsBy = new Common("Total Errors:").labelValue;
			By totalProcessedBy = new Common("Total Entries Processed:").labelValue;
			String strEndDate = null, totalErrors = null, totalProcessed = null;

			int intItrCount = 0;
			boolean isVisible = false;
			while (!isVisible) {
				driver.switchTo().defaultContent();
				click(refreshBy, lngMinTimeOutInSeconds, "Refresh", "Button", driver.getTitle(), true);
				++intItrCount;
				frameSwitchAndSwitchBack(Common.documentFrame,"documentframe", driver.getTitle());
				List<WebElement> elements = driver.findElements(endDateBy);
				String strVisibleText = "";
				if (elements.size() > 0) {
					strVisibleText = elements.get(0).getText();
					if (strVisibleText == null || strVisibleText.isEmpty()) {
						isVisible = false;
					} else {
						
						strEndDate = elements.get(0).getText();

						if (strEndDate.length() > 0) {

							totalProcessed = driver.findElements(totalProcessedBy).get(0).getText();
							if (Integer.parseInt(totalProcessed) > 0) {
								totalErrors = driver.findElement(totalErrorsBy).getText();
								if (Integer.parseInt(totalErrors) == 0) {
									isVisible = true;
									
									ALMFunctions.UpdateReportLogAndALMForPassStatus("Validate Errors in uploaded file",
											"Uploaded file should be validated without any errors",
											"Uploaded file without any errors - " + totalErrors
													+ " successfully.", true);
									return true;
								} else {
									isVisible = true;
									
									ALMFunctions.UpdateReportLogAndALMForFailStatus("Validate Errors in uploaded file",
											"Uploaded file should be validated without any errors",
											"Uploaded file with errors - " + totalErrors
													+ " not successfully.", true);
									throw new FrameworkAssertion(
											"Expected: Total Errors should be Zero," + " Actual is " + totalErrors);

								}
							}
							else {
								isVisible = false;
								if (intItrCount == 5) {
									isVisible = true;
									
									ALMFunctions.UpdateReportLogAndALMForFailStatus("Validate Errors in uploaded file",
											"Uploaded file should be validated without any errors",
											"Upload file validation failed, taking more than 20 seconds", true);
									throw new FrameworkAssertion(
											"Upload file validation failed, taking more than 20 seconds");
								}
							}
						} else {
							isVisible = false;
						}
					}
				}
			}
		} catch (TimeoutException e) {
			return false;
		}
		return false;
	}
	/**
	 * method to Parse xml to validate package id
	 
	 * @param No parameter
	 * @return No return value

	 */
	public void parseRebatePackageId() {


		driverUtil.waitUntilElementVisible(Common.xmlTextArea,
				lngMinTimeOutInSeconds);
		WebElement element = driver.findElement(Common.xmlTextArea);
		String strXmlContent = element.getText();
		XMLDataAccess xmlAccess = new XMLDataAccess();
		String strPackageId = dataTable.getData("Parametrized_Checkpoints", "Payment Package ID");
		ImportInvoice invoice = xmlAccess.unMarshallImportInvoice(strXmlContent);

		if (invoice != null) {
			if (invoice.getInvoices().getInvoiceNotes().trim().contains(strPackageId)) {
				
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Package Id validation in channel message", "Package id should be equals created Package ID: "+strPackageId,
						"Package id is equals with created Package ID: "+strPackageId, true);
			} else {
				
				ALMFunctions.UpdateReportLogAndALMForFailStatus("Package Id validation in channel message", "Package id should be equals",
						"Package id is not equals with created", true);

			}
		} else {
			
			ALMFunctions.UpdateReportLogAndALMForFailStatus("Package Id validation in channel message", "Package id should be equals",
					"ImportInvoice parsing error", true);
		}
	}
	/**
	 * method to get approved time from approval status
	 
	 * @param No parameter
	 * @return No return value

	 */
	public void getApprovedTimeforRebate() {
		WebElement element=null;
		String strElementText="";
		Common dateSubmitted=new Common("Date Submitted:");
		driverUtil.waitUntilElementVisible(dateSubmitted.dropdown, lngMinTimeOutInSeconds);
		element = driver.findElement(dateSubmitted.dropdown);
		strElementText = "";
		if(element != null) {
			strElementText = element.getText().trim();
			String values = strElementText.split("--")[1];
			
			if(values.contains("AM")) {
				values = values.split("AM")[0].trim();
			} else if(values.contains("PM")) {
				values = values.split("PM")[0].trim();
			}
		
			dataTable.putData("Parametrized_Checkpoints", "ApprovedTime", values);
		}
		
	}
	
	

	/**
	 * Generate Customer Full Name, Customer ID,Identifier values every time newly and update it in Customer XML file
	 * 
	 * 
	 * @throws IOException
	 */
	public void updatePaymentStatusXML() throws IOException, InterruptedException {
		
		String strPaymentPackageID = dataTable.getData("Parametrized_Checkpoints", "Latest Payment Package ID");
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("hhmmssMMS");
		String strPaymentNo = ft.format(dNow);
		String strCodeBasePath = System.getProperty("user.dir");
		String strCsvFileName = "GdERPPmtStatusFromXml.xml";
		String strPcb = strCodeBasePath + Util.getFileSeparator() + "testDataFiles";
		String strCsvFilePath=strPcb + Util.getFileSeparator() + strCsvFileName;
		boolean blnFileExist;
		File inputFile = new File(strCsvFilePath);
		blnFileExist = FileExists(strCsvFilePath);
		if(blnFileExist)
		{

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = null;
			try {
				dBuilder = dbf.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Document doc = null;
			try {
				doc = dBuilder.parse(inputFile);
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getElementsByTagName("ns3:ERPPayment");
			Node node = nodeList.item(0);

			if(node.getNodeType()==Node.ELEMENT_NODE){
				Element element = (Element)node;
				NamedNodeMap elmList = element.getAttributes();
				for(int i=0;i<elmList.getLength();i++)
				{


					if(elmList.item(i).getNodeName().equalsIgnoreCase("MNPackageId")) {
						elmList.item(i).setTextContent(strPaymentPackageID);
					}
					if(elmList.item(i).getNodeName().equalsIgnoreCase("ERPPaymentNumber")) {
						elmList.item(i).setTextContent(strPaymentNo);
					}
					
				}
			}

			
			try{
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File(strCsvFilePath));
				transformer.transform(source, result);
				report.updateTestLog("Update Customer XML File", "Payment Package ID,Payment Number should be updated in XML",
						 "Payment Package ID: " +strPaymentPackageID  +"Payment Number: " +strPaymentNo  +"value is updated in XML", Status.DONE);
			}
			catch (TransformerException e) {
				e.printStackTrace();
			}
		}
		else
		{
			ALMFunctions.ThrowException("Verify File Exist","Expected File " +strCsvFileName+" should be available to edit in this path: "+strPcb, 
					"Expected File " +strCsvFileName+" is not available to edit in this path: "+strPcb,true);
		}
	}
}
