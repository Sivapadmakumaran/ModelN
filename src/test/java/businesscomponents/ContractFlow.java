package businesscomponents;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.poi.ss.format.CellFormat;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.cognizant.craft.ScriptHelper;
import com.cognizant.framework.CSVDataAccess;
import com.cognizant.framework.Status;
import com.cognizant.framework.Util;
import com.cognizant.framework.XMLDataAccess;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import config.ServiceRegister;
import pages.Common;
import pcbs.PositiveChargeback;

public class ContractFlow extends CommonActionsAndFunctions{

	public ContractFlow(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}
	public final long lngMinTimeOutInSeconds = Long.parseLong(properties.getProperty("MinObjectSyncTimeout"));
	public final long lngCtrlTimeOutInSeconds = Long.parseLong(properties.getProperty("UploadControlTimeout"));
	public final long jobTimeOut = Long.parseLong(properties.getProperty("JobTimeOut"));
	public final long staleTableTimeOut = Long.parseLong(properties.getProperty("StaleTableTimeOut"));
	private Map<String, String> map = new LinkedHashMap<String, String>();

	/**
	 * Function to call Fill form Utility for Contract creation
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void createContract() {
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "Create Contract";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}

	/**
	 * Function to call Fill form Utility for Contract creation
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void createAndPublishEDI() {
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "CreateAndPublishEDI";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	/**
	 * Function to call Fill form Utility for Positive Chargeback creation
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void createPositiveChargeBack() {
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "Create PositiveChargeBack";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}

	/**
	 * Function to call Fill form Utility to Rebates Claim
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void validateRebatesClaim() {
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "Validate Rebates Claim";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	/**

	/**
	 * Function to add Price program in Contract
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void verifyContractPricing() {

		By webTableHeader = Common.dialogTableHeader;
		String strPageName = driver.getTitle(); 
		String strExpectedValue = dataTable.getData("Parametrized_Checkpoints", "Name:");
		String strColumnName = "Source Name";
		//String strExpectedValue = "";
		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
		driverUtil.waitUntilStalenessOfElement(webTableHeader, lngMinTimeOutInSeconds, strPageName);
		By row = Common.tableRow;
		int intColumnIndex = getColumnIndex(strColumnName, webTableHeader, "LookUp", false, false)+1;
		if (!(intColumnIndex != 0)) {
			ALMFunctions.ThrowException("Get index of column name",
					"Expected column name as " + strColumnName + " shall be displayed",
					"Expected column name as " + strColumnName + " is not displayed", true);
		}
		List<WebElement> listtableRow = driver.getWebDriver().findElements(row);
		boolean blnFound = false;
		if (listtableRow.isEmpty()) {

			ALMFunctions.ThrowException(strExpectedValue, strExpectedValue + " table row should be displayed",
					strExpectedValue + " table row are not displayed", true);

		} else {
			//for (WebElement rows : listtableRow) {
			for(int i=2;i<listtableRow.size();i++) {
				WebElement elmRow = listtableRow.get(i);
				String strActValue = elmRow.findElement(By.xpath("(.//td[" + intColumnIndex + "]//span)|(.//td[" + intColumnIndex + "]//a)")).getText().trim();
				//String strActValue = rows.findElement(By.xpath("(.//td[" + intColumnIndex + "]//select)|(.//td[" + intColumnIndex + "]//span)")).getText().trim();

				if (strActValue.equals(strExpectedValue.trim())) {

					blnFound = true;
					break;

				}
			}
		}
		if (blnFound) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Record should be available in Table",
					strExpectedValue + " value should be displayed",
					"Specified Record " + strExpectedValue + " is found in " + strColumnName + " column in table",
					true);

		} else {
			ALMFunctions.UpdateReportLogAndALMForFailStatus("Record should be available in Table",
					strExpectedValue + " value should be displayed",
					"Specified Record " + strExpectedValue + " is found in " + strColumnName + " column in table",
					true);
		}
	}

	/**
	 * method to create CSV file
	 * 
	 */

	public void createPCBCSV() {
		try {

			String pcbPath = System.getProperty("user.dir");
			Map<String, String> results = dataTable.getData("PositiveChargeback",PositiveChargeback.positiveChargebackHeaders);

			List<String> resultsList = results.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
			List<String[]> lines = new ArrayList<String[]>();
			String[] dataArray = resultsList.toArray(new String[0]);
			lines.add(PositiveChargeback.positiveChargebackHeaders);
			lines.add(dataArray);

			String strPcb = pcbPath + Util.getFileSeparator() + "testDataFiles";
			File pcbDir = new File(strPcb);
			if (!(pcbDir.exists())) {
				pcbDir.mkdir();
			}
			String csvFileName = "CB_Positive_" + results.get("ExternalOriginalContractId") + ".csv";
			File csv = new File(strPcb + Util.getFileSeparator() + csvFileName);

			String csvpath=strPcb + Util.getFileSeparator() + csvFileName;
			if(csv.exists()) {
				csv.delete();
			}
			csv.createNewFile();
			CSVWriter writer = new CSVWriter(new FileWriter(csv, true));
			writer.writeAll(lines);
			writer.close();
			ServiceRegister register = ServiceRegister.getInstance();
			register.putService(Thread.currentThread().getName() + "CSV", csvFileName);


			report.updateTestLog("Chargeback CSV creation", "Chargeback CSV file should be created",
					"Chargeback file created successfully at " + csv.getAbsolutePath(), Status.DONE);
			dataTable.putData("Parametrized_Checkpoints", "PcbFile",
					"CB_Positive_" + results.get("ExternalOriginalContractId") + ".csv");
			dataTable.putData("Parametrized_Checkpoints", "PcbFilepath",csvpath);
		} catch (IOException e1) {
			// TODO Auto-generated catch block

			ALMFunctions.UpdateReportLogAndALMForFailStatus("Chargeback CSV creation", "Chargeback CSV file should be created",
					"Chargeback file not created successfully", true);
			e1.printStackTrace();
		} catch (Exception e1) {

			ALMFunctions.UpdateReportLogAndALMForFailStatus("Chargeback CSV creation", "Chargeback CSV file should be created",
					"Chargeback file not created successfully", true);
			e1.printStackTrace();
		}

	}
	/*public void csv()
{
	String pcbPath = System.getProperty("user.dir");
	String csvFileName = "CB_Positive.csv";
	String strPcb = pcbPath + Util.getFileSeparator() + "testDataFiles";
	String csvpath=strPcb + Util.getFileSeparator() + csvFileName;
	Map<String, String> results = dataTable.getData("PositiveChargeback",PositiveChargeback.positiveChargebackHeaders);
	try (BufferedWriter writer = Files.newBufferedWriter(
		     Paths.get(csvpath));)
		{
		File csv = new File(strPcb + Util.getFileSeparator() + csvFileName); 
		CSVWriter writer1 = new CSVWriter(new FileWriter(csv, true));
		    // This assumes that the rowCollection will never be empty.
		    // An anonymous scope block just to limit the scope of the variable names.
		    {
		        HashMap<String, String> firstRow = rowCollection.get(0);
		        int valueIndex = 0;
		        String[] valueArray = new String[firstRow.size()];

		        for (String currentValue : firstRow.keySet())
		        {
		            valueArray[valueIndex++] = currentValue;
		        }
		        writer1.wr
		        csvPrinter.printrecord(valueArray);
		    }

		    for (HashMap<String, String> row : rowCollection)
		    {
		        int valueIndex = 0;
		        String[] valueArray = new String[row.size()];

		        for (String currentValue : row.values())
		        {
		            valueArray[valueIndex++] = currentValue;
		        }

		        csvPrinter.printrecord(valueArray);
		    }

		    csvPrinter.flush();
		}
}
	 */
	/*Method Name : Wait for status changes to Complete
	Description : Fill form utility to perform activities 
	 @throws InterruptedException
	Return Type : void
	 */

	public void waitForEndDateValueToAppear() throws InterruptedException {
		long start_time=System.currentTimeMillis();
		long wait_time=jobTimeOut;
		long end_time=start_time + wait_time;
		boolean flag=false;
		Common objJobAlert = new Common("End Date");
		Common objTotalErrors = new Common("Total Errors:");
		driverUtil.waitUntilStalenessOfElement(objJobAlert.labelValue, staleTableTimeOut, driver.getTitle());
		while(flag==false) {
			if(!driver.findElement(objJobAlert.labelValue).getText().isEmpty()) {
				flag=true;
				break;
			}else if(System.currentTimeMillis() == end_time){
				ALMFunctions.UpdateReportLogAndALMForFailStatus("Validate End Date",
						"End date value should be populated.",
						"End date value is not populated", true);
				break;
			}else {
				driver.switchTo().defaultContent();
				driver.findElement(Common.refresh).click();
				By Locator = Common.documentFrame;
				driverUtil.waitUntilStalenessOfElement(Locator, staleTableTimeOut, driver.getTitle());
				WebElement elmFrame = driver.findElement(Locator);
				driver.switchTo().frame(elmFrame);
			}

		}
		long endtime=System.currentTimeMillis();
		long totalDuration = (endtime - start_time)/60000;
		String strDurationType = "Minutes";
		if(totalDuration==0)
		{
			totalDuration = (endtime - start_time)/1000;
			strDurationType = "Seconds";
		}

		if(flag) {
			report.updateTestLog("Validate End Date","End Date Value should be Populated in an expected time period."
					,"End Date Value is Populated in an expected time period. Time taken to complete the action is: "+totalDuration +" "+strDurationType, 
					Status.DONE);
			if(!driver.findElement(objTotalErrors.labelValue).getText().equalsIgnoreCase("0"))
			{
				ALMFunctions.ThrowException("Validate Error Count","Error Count should be Zero."
						,"Errors available in the file. Error count is." +driver.findElement(objTotalErrors.labelValue).getText(), true);
			}
			

		}
		else
		{
			ALMFunctions.ThrowException("Validate End Date","End Date Value should be Populated in an expected time period."
					,"End Date Value is not Populated in an expected time period.", true);
		}
	}


	/**
	 * Method to Apply filter in the sales Indirect Lines.
	 *
	 * @param locator, locator value of element
	 * @param strButtonName, fieldName of the button
	 * @param strElementState, state of the element
	 * @param strElementType, type of the element
	 * @param strPageName   - Page Name in which the control is available
	 * @return No return value
	 */
	public void salesIndirectLinesForChargeBack() {

		String strPageName="Indirect Lines";
		pages.Common updateButton=new pages.Common("Update");
		pages.Common addCriteria=new pages.Common("Add Criteria");
		pages.Common addButton=new pages.Common("Add");
		pages.Common searchTextArea=new pages.Common("Search");
		pages.Common debitMemo=new pages.Common("Debit Memo");
		String strDebitMemoID=dataTable.getData("PositiveChargeback", "ExternalParentDistrDebitMemo");
		By debitMemoSearchBox = By.xpath(".//input[@placeholder='Debit Memo']");
		By lifeCycleStatus = By.xpath(".//div[contains(@id,'LifecycleStatus') and text()='Closed']");
		By claimIDValue = By.xpath(".//div[text()='Claim ID']//parent::li//a");
		By submissionIDValue = By.xpath(".//div[text()='Submission ID']//parent::li//a");
		By status = By.xpath(".//div[text()='Status']//parent::li//div[contains(@id,'item')]");
		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
		if(objectExists(Common.open,"isDisplayed", lngCtrlTimeOutInSeconds, "Open", "List", strPageName, false)) {
			driverUtil.waitUntilElementDisplayed(Common.open);
			mouseOverandClick(Common.open, lngCtrlTimeOutInSeconds, "Open", "List", strPageName);
			click(Common.close,lngCtrlTimeOutInSeconds, "Close", "List", strPageName, true);
			click(updateButton.button,lngCtrlTimeOutInSeconds, "Update", "Button", strPageName, true);
			if(!objectExists(debitMemoSearchBox,"isDisplayed", lngCtrlTimeOutInSeconds, "Open", "List", strPageName, false)) {
				click(addCriteria.button,lngCtrlTimeOutInSeconds, "Add Criteria", "Button", strPageName, true);
				sendkeys(searchTextArea.textbox, lngMinTimeOutInSeconds, "Debit Memo", "Search Section", strPageName,true);
				click(debitMemo.label,lngCtrlTimeOutInSeconds, "Debit Memo", "List", strPageName, true);
				click(addButton.button,lngCtrlTimeOutInSeconds, "Add", "Button", strPageName, true);
			}
			sendkeys(debitMemo.textbox, lngMinTimeOutInSeconds, strDebitMemoID, "Debit Memo Section", strPageName,true);
			click(Common.searchitem,lngCtrlTimeOutInSeconds, "Search", "Button", strPageName, true);
			driverUtil.waitUntilStalenessOfElement(Common.dialogRefreshBtn, strPageName);
			//driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
			click(Common.dialogRefreshBtn,lngMinTimeOutInSeconds, "Refresh", "Button", driver.getTitle(),true);
			objectExists(Common.lifeCycleStatus, "isEnabled", lngMinTimeOutInSeconds, "Life Cycle Status", "Life Cycle Status", strPageName,false);
			click(Common.dialogRefreshBtn,lngMinTimeOutInSeconds, "Refresh", "Button", driver.getTitle(),false);
			objectExists(Common.lifeCycleStatus, "isEnabled", lngMinTimeOutInSeconds, "Life Cycle Status", "Life Cycle Status", strPageName,false);    
			click(Common.dialogRefreshBtn,lngMinTimeOutInSeconds, "Refresh", "Button", driver.getTitle(),true);
			driverUtil.waitFor(1000);
			click(Common.dialogRefreshBtn,lngMinTimeOutInSeconds, "Refresh", "Button", driver.getTitle(),true);
			driverUtil.waitFor(1000);
			click(Common.dialogRefreshBtn,lngMinTimeOutInSeconds, "Refresh", "Button", driver.getTitle(),false);
			driverUtil.waitFor(1000);
			click(Common.dialogRefreshBtn,lngMinTimeOutInSeconds, "Refresh", "Button", driver.getTitle(),false);
			click(Common.dialogRefreshBtn,lngMinTimeOutInSeconds, "Refresh", "Button", driver.getTitle(),false);
			click(Common.dialogRefreshBtn,lngMinTimeOutInSeconds, "Refresh", "Button", driver.getTitle(),false);
			click(Common.dialogRefreshBtn,lngMinTimeOutInSeconds, "Refresh", "Button", driver.getTitle(),false);
			driverUtil.waitFor(1000);
			click(Common.dialogRefreshBtn,lngMinTimeOutInSeconds, "Refresh", "Button", driver.getTitle(),false);
			driverUtil.waitFor(3000);
			click(Common.lineRefNum,lngCtrlTimeOutInSeconds, "Reference Number", "Link", strPageName, true);
			driverUtil.waitFor(3000);
			for(int i=0;i<2;i++)
			{
				if(!objectExists(lifeCycleStatus,"isDisplayed", lngCtrlTimeOutInSeconds, "Life Cycle Status", "Label", strPageName, false)) {
					click(Common.dialogRefreshBtn,lngMinTimeOutInSeconds, "Refresh", "Button", driver.getTitle(),true);
				}
			}
			String strErrorMessage = "The action could not be performed because the selected object could not be found. Please refresh to make sure you are viewing the latest data, then try your action again.";
			if(objectExists(new Common(strErrorMessage).label,"isDisplayed", lngCtrlTimeOutInSeconds, "Error Status Change", "Label", strPageName, false)) {
				click(new Common("Previous Search").button,lngMinTimeOutInSeconds, "Previous Search", "Button", driver.getTitle(),false);
				click(Common.searchitem,lngCtrlTimeOutInSeconds, "Search", "Button", strPageName, false);
				click(Common.lineRefNum,lngCtrlTimeOutInSeconds, "Reference Number", "Link", strPageName, false);
			}
			
			if(objectExists(lifeCycleStatus,"isDisplayed", lngCtrlTimeOutInSeconds, "Life Cycle Status", "Label", strPageName, false)) {
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify the Life Cycle Status",
						"Life Cycle Status should be closed",
						"Life Cycle Status is closed", true);
			}
			else
			{
				ALMFunctions.UpdateReportLogAndALMForFailStatus("Verify the Life Cycle Status",
						"Life Cycle Status should be closed",
						"Life Cycle Status is not closed", true);
			}
			driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
			String strClaimID = driver.findElement(claimIDValue).getText();
			String strSubmissionID = driver.findElement(submissionIDValue).getText();
			String strStatus= driver.findElement(status).getText();
			if(strStatus.equalsIgnoreCase("Valid"))
			{
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify the Claim Status",
						"Claim Status should be Valid",
						"Claim Status is Valid", true);
			}
			else
			{
				ALMFunctions.UpdateReportLogAndALMForFailStatus("Verify the Claim Status",
						"Claim Status should be Valid",
						"Claim Status is not Valid", true);
			}
			dataTable.putData("Parametrized_Checkpoints", "Claim ID",strClaimID);
			dataTable.putData("Parametrized_Checkpoints", "Submission ID",strSubmissionID);
		}
		else
		{
			ALMFunctions.ThrowException("Verify the Indirect Lines Page",
					"Open List in Indirect Lines pages should be displayed",
					"Open List in Indirect Lines pages is not displayed", true);
		}
	}


	
	/**
	 * Function for Apply Debit Memo Filter in Claims
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void applyDebitMemoFilterInClaims()
	{
		String strPageName="Claims";
		pages.Common addCriteria=new pages.Common("Add Criteria");
		pages.Common addButton=new pages.Common("Add");
		pages.Common searchTextArea=new pages.Common("Search");
		pages.Common debitMemo=new pages.Common("Debit Memo");
		String strDebitMemoID=dataTable.getData("Parametrized_Checkpoints", "ExternalParentDistrDebitMemo");
		String strClaimID=dataTable.getData("Parametrized_Checkpoints", "Claim ID");
		By debitMemoSearchBox = By.xpath(".//input[@placeholder='Debit Memo']");
		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
		if(objectExists(Common.searchitem,"isDisplayed", lngCtrlTimeOutInSeconds, "Search", "Button", strPageName, false)) {
			driverUtil.waitUntilElementDisplayed(Common.searchitem);
			if(!objectExists(debitMemoSearchBox,"isDisplayed", lngCtrlTimeOutInSeconds, "Open", "List", strPageName, false)) {
				click(addCriteria.button,lngCtrlTimeOutInSeconds, "Add Criteria", "Button", strPageName, true);
				sendkeys(searchTextArea.textbox, lngMinTimeOutInSeconds, "Debit Memo", "Search Section", strPageName,true);
				click(debitMemo.label,lngCtrlTimeOutInSeconds, "Debit Memo", "List", strPageName, true);
				click(addButton.button,lngCtrlTimeOutInSeconds, "Add", "Button", strPageName, true);
			}
			sendkeys(debitMemo.textbox, lngMinTimeOutInSeconds, strDebitMemoID, "Debit Memo Section", strPageName,true);
			click(Common.searchitem,lngCtrlTimeOutInSeconds, "Search", "Button", strPageName, true);
			driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);

			if(objectExists(new Common(strClaimID).button,"isDisplayed", lngCtrlTimeOutInSeconds, "Life Cycle Status", "Label", strPageName, false)) {
				click(new Common(strClaimID).button,lngCtrlTimeOutInSeconds, strClaimID, "link", strPageName, true);

			}
			else
			{
				ALMFunctions.UpdateReportLogAndALMForFailStatus("Verify the Claim ID",
						"Claim ID should be Displayed",
						"Claim ID Status is not Displayed", true);
			}

		}
		else
		{
			ALMFunctions.ThrowException("Verify the Indirect Lines Page",
					"Open List in Indirect Lines pages should be displayed",
					"Open List in Indirect Lines pages is not displayed", true);
		}
	}
	/**
	 * Function for Parse xml
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void parseXml() {

		String strPageName = driver.getTitle();
		By showContent = By.xpath("(.//a[text()='Show Content'])[2]");
		By xmlContents = By.xpath(".//textarea[contains(@name,'document')]");
		/*By Locator1 = Common.documentFrame;
	driverUtil.waitUntilStalenessOfElement(Locator1, staleTableTimeOut, driver.getTitle());
	WebElement elmFrame1 = driver.findElement(Locator1);
	driver.switchTo().frame(elmFrame1);*/
		click(showContent, lngCtrlTimeOutInSeconds, "Show Content", "link", strPageName, true);
		By Locator = Common.dynFrame;
		driverUtil.waitUntilStalenessOfElement(Locator, staleTableTimeOut, driver.getTitle());
		WebElement elmFrame = driver.findElement(Locator);
		driver.switchTo().frame(elmFrame);
		String strXmlContent = driver.findElement(xmlContents).getText();
		XMLDataAccess xmlAccess = new XMLDataAccess();
		String claim = dataTable.getData("Parametrized_Checkpoints", "Claim ID");
		boolean isValidated = xmlAccess.validateXmlDocument(strXmlContent, claim);
		if (!isValidated) {

			ALMFunctions.UpdateReportLogAndALMForFailStatus("GdNIghtly run and validation", "GdNightly job run and validation should be done",
					"GdNightly job run and failed", true);
			ALMFunctions.ThrowException("XML Validation", "SAP Claim validation should be successful",
					"SAP Claim validation is not successful", true);
			//throw new FrameworkAssertion("SAP Claim validation failed in parseXml");
		} else {

			ALMFunctions.UpdateReportLogAndALMForPassStatus("GdNIghtly run and validation", "GdNightly job run and validation should be done",
					"GdNightly job run and validated successfully", true);
			click(new Common("Message Content","Close").dialogButton, lngCtrlTimeOutInSeconds, "Show Content", "Button", strPageName, true);
		}


	}

	/**
	 * method to parseClaim xml in show content dialog
	 * 
	 * @param strElementName
	 * @param strValues
	 * @param strElementType
	 * @param strPageName
	 */
	public void parseClaim() throws CsvException {
		By xmlContents = By.xpath(".//textarea[contains(@name,'document')]");
		String csvContent = driver.findElement(xmlContents).getText();
		try {
			CSVReader reader = new CSVReader(new StringReader(csvContent));

			List<String[]> lines = reader.readAll();
			for (String[] line : lines) {
				Map<String, String> map = new LinkedHashMap<>();
				map.put("ClaimPaidOn", line[1]);
				map.put("ClaimAprAmt", line[2]);
				dataTable.putData("Parametrized_Checkpoints", map);
			}


			ALMFunctions.UpdateReportLogAndALMForPassStatus("Claim data Parsing", "Claim data should be parsed successfully.",
					"Claim data is parsed successfully", true);
			click(new Common("Message Content","Close").dialogButton, lngCtrlTimeOutInSeconds, "Show Content", "Button", "Claim", true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * Function for Claim CSV Creation
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void claimCsvCreation() {
		ServiceRegister register = ServiceRegister.getInstance();

		String[] headers = { "[ DistRebatesClaim ]", "", "", "", "", "" };

		//String claimNumber = (String) register.getService(Thread.currentThread().getName(), "ClaimNumber");
		String strClaimNumber = dataTable.getData("Parametrized_Checkpoints", "Claim ID");
		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", driver.getTitle());
		driverUtil.waitUntilStalenessOfElement(new Common("Payment Requested On").labelValue, lngMinTimeOutInSeconds, strClaimNumber);
		String strReqDate = driver.findElement(new Common("Payment Requested On").labelValue).getText();
		strReqDate = strReqDate.split(" ")[0];
		String strApprovedAmount = driver.findElement(new Common("Approved Amount").labelValue).getText();
		strApprovedAmount = strApprovedAmount.replaceAll("\\$", "");
		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mmssSSS");
		String cc = formatter.format(dateTime);

		String[] item = { strClaimNumber, strReqDate, strApprovedAmount, "USD", "Credit Memo", "USC" + cc };
		List<String[]> lines = new ArrayList<String[]>();

		lines.add(headers);
		lines.add(item);

		String pcbPath = System.getProperty("user.dir");
		String strBid = dataTable.getData("PositiveChargeback", "ExternalOriginalContractId");
		String strPcb = null;
		String claimFile = "SAP_Ack_" + strBid + ".csv";
		strPcb = pcbPath + Util.getFileSeparator() + "testDataFiles" + Util.getFileSeparator() + claimFile;

		CSVDataAccess csvAccess = new CSVDataAccess(strPcb);

		File csv = new File(strPcb);
		try {
			csv.createNewFile();
			csvAccess.writeToCSV(lines);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		register.putService(Thread.currentThread().getName() + "Claim", claimFile);

		report.updateTestLog("SAP ACK CSV creation", "SAP ACK CSV file should be created",
				"SAP ACK file created successfully at " + csv.getAbsolutePath(), Status.DONE);
		dataTable.putData("Parametrized_Checkpoints", "RebateClaimFileName", claimFile);
		dataTable.putData("Parametrized_Checkpoints", "RebateClaimFilePath", strPcb);


	}
	/**
	 * Function for Get Edit Time
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void getEdiTime() {
		By Locator = Common.documentFrame;
		driverUtil.waitUntilStalenessOfElement(Locator, staleTableTimeOut, driver.getTitle());
		WebElement elmFrame = driver.findElement(Locator);
		driver.switchTo().frame(elmFrame);
		driverUtil.waitUntilElementVisible(By.xpath(
				"//span[text()='Recently Viewed Documents']//following::table[contains(@class,'splitControlBodyTable tableElement')]"),
				lngMinTimeOutInSeconds);
		WebElement e = driver.findElement(Common.getEdiJobTime);
		String strDate = e.getText();
		String[] a = strDate.split(" ");
		String time = a[1];
		/*String[] b = time.split(":");
		String c = b[1];
		int i=Integer.parseInt(c)+3;  */
		//	i = i+3;

		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		Date d = null;
		try {
			d = df.parse(time);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.MINUTE, 2);
		String newTime = df.format(cal.getTime());
		//String strUpdatedTime = String.valueOf(i);
		String strUpdatedDateAndTime = a[0]+" "+newTime;
		dataTable.putData("Parametrized_Checkpoints", "EDITime", strUpdatedDateAndTime);



	}
	
	/**
	 * Function for Get Edit Time
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void getEdiTimeAndNaviagteToJob() {
		By Locator = Common.documentFrame;
		driverUtil.waitUntilStalenessOfElement(Locator, staleTableTimeOut, driver.getTitle());
		WebElement elmFrame = driver.findElement(Locator);
		driver.switchTo().frame(elmFrame);
		driverUtil.waitUntilElementVisible(By.xpath(
				"//span[text()='Recently Viewed Documents']//following::table[contains(@class,'splitControlBodyTable tableElement')]"),
				lngMinTimeOutInSeconds);
		WebElement e = driver.findElement(Common.getEdiJobTime);
		String strDate = e.getText();
		String[] a = strDate.split(" ");
		String time = a[1];
		/*String[] b = time.split(":");
		String c = b[1];
		int i=Integer.parseInt(c)+3;  */
		//	i = i+3;

		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		Date d = null;
		try {
			d = df.parse(time);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.MINUTE, 3);
		String newTime = df.format(cal.getTime());
		//String strUpdatedTime = String.valueOf(i);
		String strUpdatedDateAndTime = a[0]+" "+newTime;
		dataTable.putData("Parametrized_Checkpoints", "EDITimeAndNavigate", strUpdatedDateAndTime);
		click(Common.navigateToJobFromEdiJobTime, lngCtrlTimeOutInSeconds, "Job", "Link", driver.getTitle(), true);



	}
	

	/*Method Name : Wait for status changes to Complete
	Description : Fill form utility to perform activities 
	 @throws InterruptedException
	Return Type : void
	 */

	public void waitForStatusToChangeToCompleteInEDI() throws InterruptedException {
		Common objJobAlert = new Common("State:");
		
		int intWaitTime = 30;
		for(int intTimer=0; intTimer<intWaitTime;intTimer++)
		{
			driver.switchTo().defaultContent();
			driverUtil.waitFor(2000);
			click(Common.refresh,lngCtrlTimeOutInSeconds, "Refresh", "Button", driver.getTitle(), false);
			By Locator = Common.documentFrame;
			driverUtil.waitUntilStalenessOfElement(Locator, staleTableTimeOut, driver.getTitle());
			WebElement elmFrame = driver.findElement(Locator);
			driver.switchTo().frame(elmFrame);
			if(driver.findElement(objJobAlert.labelValue).getText().equalsIgnoreCase("Completed")) {
				break;
			}
		}
	}
	
	/**
	 * method to parse chargeback claim file in show content dialog
	 * 
	 * @param strElementName
	 * @param strValues
	 * @param strElementType
	 * @param strPageName
	 */
	public void parseChargebackClaim() {
		String strPageName = driver.getTitle();
		click(new Common("Edi849ToXml").partialLabel,lngCtrlTimeOutInSeconds, "Edi849ToXml", "Link", strPageName, true);
		click(new Common("Channel Messages").partialLabel,lngCtrlTimeOutInSeconds, "Channel Messages", "Link", strPageName, true);
		By showContent = By.xpath("(.//a[text()='Show Content'])[2]");
		click(showContent,lngCtrlTimeOutInSeconds, "Show Content", "Link", strPageName, true);
		By Locator = Common.dynFrame;
		driverUtil.waitUntilStalenessOfElement(Locator, staleTableTimeOut, driver.getTitle());
		WebElement elmFrame = driver.findElement(Locator);
		driver.switchTo().frame(elmFrame);
		By xmlContents = By.xpath(".//textarea[contains(@name,'document')]");
		driverUtil.waitUntilElementVisible(xmlContents,
				lngMinTimeOutInSeconds);
		WebElement element = driver.findElement(xmlContents);
		String xmlContent = element.getText();
		XMLDataAccess xmlAccess = new XMLDataAccess();
		String claimNumber = dataTable.getData("Parametrized_Checkpoints", "Claim ID");
		String xpathExpression = "/Entries/ChargebackClaim";
		Map<String, String> fieldValues = xmlAccess.validateChargebackClaimDocument(xmlContent, claimNumber,
				xpathExpression);
		if (fieldValues != null) {
			if (fieldValues.get("ClaimRefNum").equalsIgnoreCase(claimNumber)) {
				
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Chargeback 'ClaimRefNum' validation",
						"Chargeback 'ClaimRefNum' should be validated successfully",
						"Chargeback 'ClaimRefNum' is validated successfully as " + claimNumber, true);

				
				ALMFunctions.UpdateReportLogAndALMForPassStatus("EDI 849TOXml validation", "EDI 849TOXml should be validated successfully",
						"EDI 849TOXml is created and validated successfully.", true);
				click(new Common("Message Content","Close").dialogButton, lngCtrlTimeOutInSeconds, "Close", "Button", strPageName, true);
			}
		} else {
			
			ALMFunctions.UpdateReportLogAndALMForFailStatus("Chargeback 'ClaimRefNum' validation",
					"Chargeback 'ClaimRefNum' should be validated successfully",
					"Chargeback 'ClaimRefNum' is not validated successfully", true);

			
			ALMFunctions.UpdateReportLogAndALMForFailStatus("EDI 849TOXml validation", "EDI 849TOXml should be validated successfully",
					"EDI 849TOXml is created and not validated successfully.", true);
		}

	}
}


