package businesscomponents;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import com.cognizant.craft.ScriptHelper;
import com.cognizant.framework.Status;
import com.cognizant.framework.Util;
import com.cognizant.framework.XMLDataAccess;


import models.EntriesMediAck;
import models.McdCheckAck;
import pages.Common;


public class MedicaidFlow extends CommonFunctions{

	public MedicaidFlow(ScriptHelper scriptHelper) {
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
	 * Function to call Fill form Utility for Medicaid Program creation
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void medicaidProgramCreation() {
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "Medicaid Program Creation";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	/**
	 * Function to call Fill form Utility for URA Pricing Status to Publish
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void verifyURAPricingStatus() {
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "URA Pricing Status";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	/**
	 * Function to call Fill form Utility for Adjustment Claim creation
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void createAdjustmentClaim()
	{
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "Create Adjustment Claim";
		fillFormExcelFunctions.FillInputForm(strInputParameter);
		/*adjClaimTextFileUpdate();
		uploadAdjustmentFile();*/

	}
	/**
	 * Function to call Fill form Utility for Original Claim Creation
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void originalClaimCreation() {
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "Claim Creation";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	/**
	 * Function to call Fill form Utility for Custom URA Formula Creation
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void createCustomURAFormula() {
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "Create Custom URA Formula";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	/*Method Name : addProducts
	Description : Fill form utility to perform activities 
	 @throws InterruptedException
	Return Type : void
	 */
	public void addProducts() throws Exception {
		lastRows=4;
		for(int index=1;index<=lastRows;index++) {
			String strPatternValue=Integer.toString(index);
			Common selectNextRowInProductsTable =new Common(strPatternValue);
			Thread.sleep(3000);
			WebElement element=driver.findElement(selectNextRowInProductsTable.selectNextRowInProductsTable);
			String strValue=element.getText();
			doubleClick(selectNextRowInProductsTable.selectNextRowInProductsTable, lngMinTimeOutInSeconds,
					"NDC Product "  , strValue, driver.getTitle(), true);
		}
		driverUtil.waitUntilElementClickable(Common.okButton,lngMinTimeOutInSeconds);
		clickByJS(Common.okButton,lngMinTimeOutInSeconds, "Ok", "Button", driver.getTitle(), true);
		frameSwitchAndSwitchBack(Common.documentFrame,"documentframe", driver.getTitle());
		Thread.sleep(3000);

		List<WebElement> row = driver.findElements(Common.ndcTableRow);
		for (int index=1;index<=row.size();index++) {
			String strPatternValues=Integer.toString(index);
			Common ndcExpiryDateField=new Common(strPatternValues);

			String strExpiryDate=getText(ndcExpiryDateField.ndcExpiryDateField, lngMinTimeOutInSeconds,"Expiry Date",driver.getTitle());

			if(!strExpiryDate.isEmpty()) {
				Common ndcExpiryDateFieldCheckbox=new Common(strPatternValues);
				pagescroll(ndcExpiryDateFieldCheckbox.ndcExpiryDateFieldCheckbox, driver.getTitle());
				clickByJs(ndcExpiryDateFieldCheckbox.ndcExpiryDateFieldCheckbox,lngMinTimeOutInSeconds, "Expiration NDC Product ", "Checkbox", driver.getTitle(), true);

			}

		}
		Common removeButton=new Common("Remove");
		clickByJs(removeButton.button, lngMinTimeOutInSeconds, "Remove","Button", driver.getTitle(), true);
		Common yesAlertButton=new Common("Confirm Delete","Yes");
		driverUtil.waitUntilElementVisible(yesAlertButton.dialogButton, lngMinTimeOutInSeconds);
		click(yesAlertButton.dialogButton, lngMinTimeOutInSeconds, "Yes","Alert Button", driver.getTitle(), true);
		Common addButton=new Common("Add");
		click(addButton.button, lngMinTimeOutInSeconds, "Add","Button", driver.getTitle(), true);
		frameSwitchAndSwitchBack(Common.documentFrame,"documentframe", driver.getTitle());
		frameSwitchAndSwitchBack(Common.dynFrame,"dynframe", driver.getTitle());
		Common ndcTextbox=new Common("Add Product","NDC #");
		Common searchButton=new Common("Add Product","search");
		sendkeys(ndcTextbox.dialogTextBox,lngMinTimeOutInSeconds, "61958", "Text", driver.getTitle(), true);
		click(searchButton.dialogButton,lngMinTimeOutInSeconds, "Search", "Button", driver.getTitle(), true);

		for(int index=lastRows+1;index<=lastRows+2;index++) {
			String strPatternValue=Integer.toString(index);
			Common selectNextRowInProductsTable =new Common(strPatternValue);
			Thread.sleep(3000);
			WebElement element=driver.findElement(selectNextRowInProductsTable.selectNextRowInProductsTable);
			String strValue=element.getText();
			doubleClick(selectNextRowInProductsTable.selectNextRowInProductsTable, lngMinTimeOutInSeconds,
					"NDC Product "  , strValue, driver.getTitle(), true);
		}
		driverUtil.waitUntilElementClickable(Common.okButton,lngMinTimeOutInSeconds);
		clickByJS(Common.okButton,lngMinTimeOutInSeconds, "Ok", "Button", driver.getTitle(), true);
		frameSwitchAndSwitchBack(Common.documentFrame,"documentframe", driver.getTitle());
		Thread.sleep(3000);

		String ndcProductHifenFirstrow=getText(Common.ndcProductinFirstRow, lngMinTimeOutInSeconds,"NDC Product in First Row",driver.getTitle());
		String ndcProductReplacedForFirstRow=ndcProductHifenFirstrow.replaceAll("-","");
		ndcProductForFirstRow=ndcProductReplacedForFirstRow;
		dataTable.putData("Parametrized_Checkpoints", "NDC Product in First Row", ndcProductForFirstRow);
		String[] str =ndcProductHifenFirstrow.split("-");
		String strLabelerForFirstRow=str[0];

		String strMiddleStringForFirstRow=str[1];

		String strEndStringForFirstRow=str[2];

		String strTableStringForFirstRow=strMiddleStringForFirstRow+"-"+strEndStringForFirstRow;
		dataTable.putData("Parametrized_Checkpoints", "Labeler For First Row", strLabelerForFirstRow);
		dataTable.putData("Parametrized_Checkpoints", "Tablestring For First Row", strTableStringForFirstRow);

		String strNdcProductHifenSecondrow=getText(Common.ndcProductinSecondRow, lngMinTimeOutInSeconds,"NDC Product in Second Row",driver.getTitle());
		String strNdcProductReplacedForSecondRow=strNdcProductHifenSecondrow.replaceAll("-","");
		ndcProductForSecondRow=strNdcProductReplacedForSecondRow;
		dataTable.putData("Parametrized_Checkpoints", "NDC Product in Second Row", ndcProductForSecondRow);
		String[] string =strNdcProductHifenSecondrow.split("-");
		String strLabelerForSecondRow=string[0];

		String strMiddleStringForSecondRow=string[1];

		String strEndStringForSecondRow=string[2];

		String strTableStringForSecondRow=strMiddleStringForSecondRow+"-"+strEndStringForSecondRow;

		dataTable.putData("Parametrized_Checkpoints", "Tablestring For Second Row", strTableStringForSecondRow);
		dataTable.putData("Parametrized_Checkpoints", "Labeler For Second Row", strLabelerForSecondRow);


	}
	/*Method Name : Verify URA Jobs Status 
		Description : Fill form utility to perform activities 
		 @throws InterruptedException
		Return Type : void
	 */
	public void verifyURAJobStatus() throws InterruptedException {

		long start_time=System.currentTimeMillis();
		long wait_time=60000;
		long end_time=start_time + wait_time;


		String strEntries = "Completed";
		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", "Home Page");
		driverUtil.waitUntilElementVisible(Common.uraCalculationJobStatus,lngMinTimeOutInSeconds, "Loading Message", "Loading","Home Page",false);
		
		boolean flag=false;
		while(flag==false) {
			driverUtil.waitUntilStalenessOfElement(Common.uraCalculationJobStatus, "Home Page");
			if(strEntries.equalsIgnoreCase(getText(Common.uraCalculationJobStatus, lngMinTimeOutInSeconds,"Status Section",driver.getTitle()))) {
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Validate URA status",
						"Status should be Completed successfully.",
						"Status is Completed successfully.", true);
				flag=true;
			}else if(System.currentTimeMillis() == end_time){
				ALMFunctions.UpdateReportLogAndALMForFailStatus("Validate URA status",
						"Status should be Completed successfully.",
						"Status is not Completed successfully.", true);
				flag=true;
			}else{
				Thread.sleep(2000);
				Common searchButton=new Common("Search");
				click(searchButton.button,lngMinTimeOutInSeconds, "Search", "Button", driver.getTitle(),false);
			}
		}

	}
	/*Method Name : Wait for value appear in table 
		Description : Fill form utility to perform activities 
		 @throws InterruptedException
		Return Type : void
	 */
	public void waitForValueAppearinTable() {


		boolean flag=false;
		while(flag==false) {
			By firstrow=Common.selectFirstRowInTable;
			if(driverUtil.waitUntilElementVisible(firstrow, 3, "Value Appear", "Table", driver.getTitle(),false)==true) {
				flag=true;
			}else {
				Common searchButton=new Common("Search");
				click(searchButton.button,lngMinTimeOutInSeconds, "Search", "Button", driver.getTitle(),false);
			}
		}

		doubleClick(Common.selectFirstRowInTable, lngMinTimeOutInSeconds,"Table row item ", "Link", driver.getTitle(), true);

	}
	/*Method Name : Original Claims File Update 
		Description : Fill form utility to perform activities 
		 @throws InterruptedException
		Return Type : void
	 */
	public void textFileUpdate() {
		String strFileName=dataTable.getData("Data", "Path");
		String strFirstHalf=dataTable.getData("Data", "Firsthalf");
		String strSecondHalf=dataTable.getData("Data", "Secondhalf");
		String strProgramName=dataTable.getData("Parametrized_Checkpoints", "Program Name:");
		String strOriginalFileName = "NM_" + strProgramName + "_Original" + ".txt";

		String strOutputFile = System.getProperty("user.dir") + Util.getFileSeparator() + "testDataFiles" + Util.getFileSeparator() + strOriginalFileName;
		dataTable.putData("Parametrized_Checkpoints", "uploadfilename", strOriginalFileName);
		String strFilePath = System.getProperty("user.dir")+strFileName;
		dataTable.putData("Parametrized_Checkpoints", "originalfilepath", strOutputFile);
		try(BufferedReader br = new BufferedReader(new FileReader(strFilePath))){
			FileWriter writer = null;
			String strBetween="";
			String string="";
			List<String> strFirstText=new ArrayList<String>();
			List<String> strInbetween=new ArrayList<String>();
			List<String> strFullwordText=new ArrayList<String>();
			List<String> strFullword=new ArrayList<String>();
			String strLine="";
			String strSingleLine="";
			String strNewStringSecond="";
			String strNewString="";
			while ((strLine = br.readLine()) != null){


				String[] str =strLine.split(" ");
				String[] strEachLine = strLine.split("\n");
				strSingleLine =  strEachLine[0];
				strFullwordText.add(strSingleLine);

				String strReqText = str[0];

				String strPt=strReqText.substring(0, 4);
				String strState=strReqText.substring(4, 6);
				String strDefaultRecordId=dataTable.getData("Data", "Default Record ID");
				String strStateShortcut=dataTable.getData("Data", "State shortcut");
				String strWordReplaceFirst=strPt.replace(strPt, strDefaultRecordId);
				String strWordReplaceSecond=strState.replace(strState, strStateShortcut);
				String strConcatWord=strWordReplaceFirst+strWordReplaceSecond;
				String strFirstWord=strReqText.substring(0, 6);
				String strNewWord=strFirstWord.replace(strFirstWord,strConcatWord);

				strNewString=strReqText.replace(strFirstWord,strConcatWord);
				strFirstText.add(strNewString);
				strBetween = StringUtils.substringBetween(strNewString, strNewWord,strSecondHalf);
				strInbetween.add(strBetween);
				strNewStringSecond=strSingleLine.replace(strReqText,strNewString);
				strFullword.add(strNewStringSecond);

			}

			String strReplace="";
			String[] strArrndc=new String[2];
			strArrndc[0]=dataTable.getData("Parametrized_Checkpoints", "NDC Product in First Row");
			strArrndc[1]=dataTable.getData("Parametrized_Checkpoints", "NDC Product in Second Row");
			String strReplaceSecond="";
			List<String> strNewContent=new ArrayList<String>();
			for(int i=0;i<2;i++) {
				if(strFirstText.get(i).contains(strInbetween.get(i))) {
					strReplace = strFirstText.get(i).replace(strInbetween.get(i), strArrndc[i]);

				}
				if(strFullword.get(i).contains(strInbetween.get(i))) {
					strReplaceSecond = strFullword.get(i).replace(strFirstText.get(i), strReplace);

					strNewContent.add(strReplaceSecond);

				}
			}

			writer = new FileWriter(strFilePath);
			writer.write(strNewContent.get(0)+"\n");
			writer.write(strNewContent.get(1));
			writer.close();


		} catch(Exception e){
			e.printStackTrace();
		}

		copyfile(strFilePath, strOutputFile);

	}
	/**
	 * method to copy file and create new file
	 
	 * @param No parameter
	 * @return No return value

	 */
	public static void copyfile(String srcFilePath, String desFilePath) {

		try(BufferedReader reader = new BufferedReader(new FileReader(srcFilePath));
				BufferedWriter writer = new BufferedWriter(new FileWriter(desFilePath))){
			String strLine;
			while((strLine = reader.readLine())!= null) {
				writer.write(strLine);
				writer.newLine();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	/*Method Name : Upload a Claim File 
		Description : Fill form utility to perform activities 
		 @throws InterruptedException
		Return Type : void
	 */
	public void uploadFile() {
		String strBaseDir = System.getProperty("user.dir");
		String strClaimFile = dataTable.getData("Parametrized_Checkpoints", "uploadfilename");
		dataTable.putData("Parametrized_Checkpoints", "ClaimFile", strClaimFile);
		mouseOverandClick(Common.fileUpload, lngMinTimeOutInSeconds,"Choose File", "Button", driver.getTitle());
		String strFilePath = "\"" + strBaseDir + Util.getFileSeparator() + "testDataFiles" + Util.getFileSeparator()
		+ strClaimFile + "\"";
		executeScript(true, strBaseDir + "\\autoIT" + Util.getFileSeparator() + "File Upload.exe", "Open",
				strFilePath);
	}
	

	/**
	 * Function for creating Medicaid Report
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void medicaidReporting() {
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "Medicaid report";
		fillFormExcelFunctions.FillInputForm(strInputParameter);
		String strPageName = driver.getTitle();
		objectExists(Common.dynFrame, "isEnabled", lngMinTimeOutInSeconds, "External Frame", "Frame", strPageName,false);
		WebElement elmDynFrame = driver.findElement(Common.dynFrame);
		driver.switchTo().frame(elmDynFrame);
		fillFormExcelFunctions.selectDropDown("Quarter:", "2022", strPageName);
		clickByJs(new Common("Innovator Multiple Source (I)").label, lngMinTimeOutInSeconds, "Innovator Multiple Source (I)", "DropDown Value", strPageName, true);
		clickByJs(new Common("Add >>").label, lngMinTimeOutInSeconds, "Add >>", "Button", strPageName, true);
		fillFormExcelFunctions.clickDialogButton("Set Parameters", "Ok", strPageName);
		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
		driver.switchTo().defaultContent();
		System.out.println("Switched 0");
		objectExists(Common.documentFrame, "isEnabled", lngMinTimeOutInSeconds, "External Frame", "Frame", strPageName,false);
		WebElement elmDocFrame = driver.findElement(Common.documentFrame);
		driver.switchTo().frame(elmDocFrame);
		System.out.println("Switched 1");
		objectExists(Common.dynFrame, "isEnabled", lngMinTimeOutInSeconds, "External Frame", "Frame", strPageName,false);
		WebElement elmDynamicFrame = driver.findElement(Common.dynFrame);
		driver.switchTo().frame(elmDynamicFrame);
		System.out.println("Switched 2");
		objectExists(Common.externalFrame, "isEnabled", lngMinTimeOutInSeconds, "External Frame", "Frame", strPageName,false);
		WebElement elmExternalFrame = driver.findElement(Common.externalFrame);
		driver.switchTo().frame(elmExternalFrame);
		int intColumnIndex;
		By webTableHeader = null;
		By tableRow = null;
		String strColumnName = "Medicaid Penalty URA!Medicaid Basic URA as % of AMP";
		String strExpectedValues = "$!%";
		String[] strColumnNames = strColumnName.split("!");
		String[] strExpectedValue = strExpectedValues.split("!");
		webTableHeader= Common.externalTableHeader;
		tableRow= Common.externalTableRow;
		for(int columnCount =0;columnCount<strColumnNames.length;columnCount++)
		{
			intColumnIndex = getColumnIndex(strColumnNames[columnCount], webTableHeader, "External Table", false, false) + 1;

			if (!(intColumnIndex != 0)) {
				ALMFunctions.ThrowException("Get index of column name",
						"Expected column name as " + strColumnNames[columnCount] + " shall be displayed",
						"Expected column name as " + strColumnNames[columnCount] + " is not displayed", true);
			}


			boolean blnFound = false;
			boolean blnClick = false;
			int intCurrentPage = 1;
			int counter = 0;
			List<WebElement> listtableRow = driver.getWebDriver().findElements(tableRow);
			if (listtableRow.isEmpty()) {

				ALMFunctions.ThrowException(strExpectedValue[columnCount], strExpectedValue[columnCount] + " table row should be displayed",
						strExpectedValue[columnCount] + " table row are not displayed", true);

			} 
			else
			{


				for (WebElement rows : listtableRow) {

					String strActValue = rows.findElement(By.xpath(".//td[" + intColumnIndex + "]//span")).getText().trim();
					if(strActValue.contains(strExpectedValue[columnCount]))
					{
						counter++;
						if(counter>3)
						{
							blnFound = true;
							break;
						}
					}
				}

				if(blnFound)
				{
					ALMFunctions.UpdateReportLogAndALMForPassStatus("Record should be available in Table",
							"'"+strExpectedValue[columnCount] + "' value should be displayed in '" +strColumnNames[columnCount] +"' Column",
							"Specified Record '" + strExpectedValue[columnCount] + "' is found in '" + strColumnNames[columnCount] + "' column in table",
							true);
				}
				else
				{
					ALMFunctions.UpdateReportLogAndALMForFailStatus("Record should be available in Table",
							"'"+strExpectedValue[columnCount] + "' value should be displayed in '" +strColumnNames[columnCount] +"' Column",
							"Specified Record '" + strExpectedValue[columnCount] + "' is not found in '" + strColumnNames[columnCount] + "' column in table",
							true);
				}
			}

		}
		driver.switchTo().defaultContent();
		driver.switchTo().frame(elmDocFrame);
		driver.switchTo().frame(elmDynamicFrame);
		fillFormExcelFunctions.clickDialogButton("Report Viewer", "Close", strPageName);
		driver.switchTo().defaultContent();

	}

	/**
	 * Function for verifying Medicaid Rate
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void medicaidRate() {
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "Medicaid rate";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	/**
	 * Function to Calculate URA Job
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void medicaidCalculateURAJob() {
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "CalculateURA";
		fillFormExcelFunctions.FillInputForm(strInputParameter);
		String strPageName = driver.getTitle();

		String strColumnName = "Business Object";
		String strExpectedValue = "";

		By webTableHeader = Common.tableHeader;
		By tableRow = Common.tableRow;
		int intColumnIndex = getColumnIndex(strColumnName, webTableHeader, "Table", false, false) + 1;
		pagescroll(new Common("Business Object").label, strPageName);
		clickByJs(new Common("Business Object").label, lngMinTimeOutInSeconds, "Business Object", "Table Column", strPageName, true);
		if (!(intColumnIndex != 0)) {
			ALMFunctions.ThrowException("Get index of column name",
					"Expected column name as " + strColumnName + " shall be displayed",
					"Expected column name as " + strColumnName + " is not displayed", true);
		}


		boolean blnFound = false;
		boolean blnClick = false;
		driverUtil.waitUntilStalenessOfElement(Common.tableRow, strPageName);
		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
		objectExists(new Common("Business Object").label, "isDisplayed", lngMinTimeOutInSeconds, "Business Object", "Table Column", strPageName, false);
		List<WebElement> listtableRow = driver.getWebDriver().findElements(tableRow);
		if (listtableRow.isEmpty()) {

			ALMFunctions.ThrowException(strExpectedValue, strExpectedValue + " table row should be displayed",
					strExpectedValue + " table row are not displayed", true);

		} 
		else
		{


			for (WebElement rows : listtableRow) {

				WebElement elmRow = rows.findElement(By.xpath(".//td[" + intColumnIndex + "]//a"));
				String strActValue = rows.findElement(By.xpath(".//td[" + intColumnIndex + "]//a")).getText().trim();

				String[] strSplitActValue = strActValue.split(" ");
				String strWorkBookName = strSplitActValue[0];
				String[] strSplitFirstValues = strWorkBookName.split("-");
				String strSplitFirstValue = strSplitFirstValues[0];
				if(!strWorkBookName.isEmpty())
				{
					pagescroll(elmRow, strPageName);
					ALMFunctions.UpdateReportLogAndALMForPassStatus("Validation of WorkBook",
							strColumnName+" Column value should start with a value and ends with (workbook) ",
							strColumnName+" Column value starts with a value and ends with (workbook).Actual Value available is: "+strActValue
							,true);

				}
				else
				{
					ALMFunctions.UpdateReportLogAndALMForFailStatus("Validation of WorkBook",
							strColumnName+" Column value should start with a value and ends with (workbook) ",
							strColumnName+" Column value not started with a value and ends with (workbook).Actual Value available is: "+strActValue
							,true);
				}

				if (!strSplitFirstValue.isEmpty()) {
					String regex = "^[A-Za-z]\\w{5,29}$";

					// Compile the ReGex
					Pattern p = Pattern.compile(regex); 
					Matcher m = p.matcher(strSplitFirstValue);
					if(m.matches())
					{
						ALMFunctions.UpdateReportLogAndALMForPassStatus("Validation of WorkBook",
								" Before Quarter and year in the "+strColumnName +" value, Alphabet should be available",
								" Before Quarter and year in the "+strColumnName +" value, Alphabet is available. Actual Value availale is: "+strActValue
								,true);
						break;
					}
					else
					{
						ALMFunctions.UpdateReportLogAndALMForFailStatus("Validation of WorkBook",
								" Before Quarter and year in the "+strColumnName +" value, Alphabet should be available",
								" Before Quarter and year in the "+strColumnName +" value, Alphabet is not available. Actual Value availale is: "+strActValue
								,true);
						break;
					}


				}

			}


		}


	}

	/*Method Name : Adjustment Claims File Update 
		Description : Fill form utility to perform activities 
		 @throws InterruptedException
		Return Type : void
		 */
		public void adjClaimTextFileUpdate() {

			String strFileName=dataTable.getData("Data", "Adj Path");
			String strFirstHalf=dataTable.getData("Data", "Firsthalf");
			String strSecondHalf=dataTable.getData("Data", "Secondhalf");
			String strProgramName=dataTable.getData("Parametrized_Checkpoints", "Program Name:");
			
			String strNewstring="";
			List<String> strFirstText=new ArrayList<String>();
			String strOutputFileName = "NM_" +strProgramName+ "_adj" + ".txt";

			String strOutputfile = System.getProperty("user.dir") + Util.getFileSeparator() + "testDataFiles" + Util.getFileSeparator() + strOutputFileName;
			dataTable.putData("Data", "adjustmentFileName", strOutputFileName);

			//String filePath = System.getProperty("user.dir") + "//PCB//NM_MediPOC_Adj.txt";
			String strFilePath = System.getProperty("user.dir") +strFileName;
			dataTable.putData("Data", "adjustmentFilePath", strOutputfile);

			try (BufferedReader br = new BufferedReader(new FileReader(strFilePath))) {

				FileWriter writer = null;

				String strLine = br.readLine();

				String[] strEachline = strLine.split(" ");

				String strSingleline = strEachline[0];

				String strRemline = strEachline[2];

				String strPt=strSingleline.substring(0, 4);
				String strState=strSingleline.substring(4, 6);
				String strDefaultRecordId=dataTable.getData("Data", "Default Record ID");
				String strStateShortCut=dataTable.getData("Data", "State shortcut");
				String strWordReplaceFirst=strPt.replace(strPt, strDefaultRecordId);
				String strWordReplaceSecond=strState.replace(strState, strStateShortCut);
				String strConcatWord=strWordReplaceFirst+strWordReplaceSecond;
				String strFirstword=strSingleline.substring(0, 6);
				String strNewword=strFirstword.replace(strFirstword,strConcatWord);

				strNewstring=strSingleline.replace(strFirstword,strConcatWord);
				strFirstText.add(strNewstring);

				String strBetween = StringUtils.substringBetween(strNewstring, strNewword, strSecondHalf);

				// String replace="";

				String strArrndc = dataTable.getData("Parametrized_Checkpoints", "NDC Product in First Row");

				String strReplace = strNewstring.replace(strBetween, strArrndc);


				String strReplaceSecond = strLine.replace(strSingleline, strReplace);
				writer = new FileWriter(strFilePath);

				writer.write(strReplaceSecond);

				writer.close();

			} catch (Exception e) {

				e.printStackTrace();

			}
			copyfile(strFilePath, strOutputfile);

		}
/*Method Name : Upload a Medi Acknowledment File 
		Description : Fill form utility to perform activities 
		 @throws InterruptedException
		Return Type : void
		 */
		public void uploadMediackFile()
		{
			String strBaseDir = System.getProperty("user.dir");
			mouseOverandClick(Common.fileUpload, lngMinTimeOutInSeconds,"Choose File", "Button", driver.getTitle());
			String strMediAckFile = dataTable.getData("Parametrized_Checkpoints", "MediAck filename");
			String strFilePath = "\"" + strBaseDir + Util.getFileSeparator() + "testDataFiles" + Util.getFileSeparator()
					+ strMediAckFile + "\"";
			executeScript(true, strBaseDir + "\\autoIT" + Util.getFileSeparator() + "File Upload.exe", "Open",
					strFilePath);
			
		}
		/*Method Name : Wait for status changes to Complete
		Description : Fill form utility to perform activities 
		 @throws InterruptedException
		Return Type : void
		 */
		public void waitForStatusChangetoComplete() throws InterruptedException {
			
			String strEntries = "Completed";
			Common status=new Common("Status");
			boolean flag=false;
			do{
				Thread.sleep(1000);
				click(Common.dialogRefreshBtn,lngMinTimeOutInSeconds, "Refresh", "Button", driver.getTitle(),false);
				
				}while(!strEntries.equalsIgnoreCase(getText(status.labelValue, lngMinTimeOutInSeconds,"Status Field",driver.getTitle()))); 
			By totalErrorsBy = new Common("Errors").labelValue;
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Validate Claim Import status",
					"Status Field should be displayed as Completed successfully.",
					"Status Field is displayed as Completed successfully.", true);
			String totalErrors = driver.findElement(totalErrorsBy).getText();
			if (Integer.parseInt(totalErrors) == 0) {
						
				flag= true;
			}
			if(flag) {
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Validate Errors in uploaded Claim file",
						"Uploaded Claim file should be validated without any errors",
						"Uploaded Claim file without any errors - " + totalErrors
								+ " successfully.", true);
			}else {
				ALMFunctions.UpdateReportLogAndALMForFailStatus("Validate Errors in uploaded Claim file",
						"Uploaded Claim file should be validated without any errors",
						"Uploaded Claim file with errors - " + totalErrors
								+ " not successfully.", true);
				
			}
			}
	/*Method Name : Upload a Adjustment File 
		Description : Fill form utility to perform activities 
		 @throws InterruptedException
		Return Type : void
		 */
		public void uploadAdjustmentFile() {
			String strBaseDir = System.getProperty("user.dir");
			String strClaimFile = dataTable.getData("Data", "adjustmentFileName");
			//dataTable.putData("Parametrized_Checkpoints", "ClaimFile", ClaimFile);
			mouseOverandClick(Common.fileUpload, lngMinTimeOutInSeconds,"Choose File", "Button", driver.getTitle());
			String strFilePath = "\"" + strBaseDir + Util.getFileSeparator() + "testDataFiles" + Util.getFileSeparator()+ strClaimFile + "\"";
			executeScript(true, strBaseDir + "\\autoIT" + Util.getFileSeparator() + "File Upload.exe", "Open",
					strFilePath);
		}
		/*Method Name : Create Mediack Xml file
		Description : Fill form utility to perform activities 
		 @throws InterruptedException
		Return Type : void
		 */
		public void createMediAckXml() {
			String strPayNum = dataTable.getData("Parametrized_Checkpoints", "Payment #:");
			String strCheckNum = dataTable.getData("Data", "CheckNumber");
			
			String strFileName = "MediAck" + 
					String.format("%s",System.currentTimeMillis()).substring(9) + "_" +
					strPayNum + ".xml";
		            dataTable.putData("Parametrized_Checkpoints", "MediAck filename", strFileName);
	        System.out.println(strFileName);
			String strMediAckFile = System.getProperty("user.dir") + Util.getFileSeparator() + "testDataFiles" + Util.getFileSeparator()
					+ strFileName;
			dataTable.putData("Parametrized_Checkpoints", "mediackfilepath", strMediAckFile);
			String strCheckGenerated = "true";
			
			LocalDate dt = LocalDate.now();
			dt.minusMonths(4);
			String strCheckDt = DateTimeFormatter.ofPattern("YYYY-MM-dd 00:00:00").format(dt);

			McdCheckAck payment = new McdCheckAck(strPayNum, strCheckGenerated, strCheckNum, strCheckDt);
			EntriesMediAck entries = new EntriesMediAck(payment);

			XMLDataAccess xmlAccess = new XMLDataAccess();
			boolean isCreated = xmlAccess.mediAckXml(strMediAckFile, entries);

			if (isCreated) {
				
				report.updateTestLog("Creating 'mediAckStatusFromXml'",
						"'mediAckXml' file should be created successfully",
						"'mediAckXml' file is created successfully",Status.DONE);
			} else {
				
				report.updateTestLog("Creating 'mediAckStatusFromXml'",
						"'mediAckXml' file should be created successfully",
						"'mediAckXml' file is creation failed",Status.FAIL);

			}

		}
		/**
		 * method to validate upload status after uploading files
		 * 
		 * @param strElementName
		 * @param strValues
		 * @param strElementType
		 * @param strPageName
		 * @return
		 */
		public boolean validateTotalErrorsMediAck() {

			try {
				
				By refreshBy = Common.refresh;
				By endDateBy = new Common("End Date:").labelValue;
				By totalErrorsBy = new Common("Total Errors:").labelValue;
				By totalProcessedBy = new Common("Total Entries Processed:").labelValue;
				String strEndDate = null, totalErrors = null, totalProcessed = null;

				int intItrCount = 0;
				boolean isVisible = false;
				while (!isVisible) {
					try {
						driver.switchTo().defaultContent();
						Thread.sleep(1000);
						driver.findElement(refreshBy).click();

						Thread.sleep(3000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
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
										
										ALMFunctions.UpdateReportLogAndALMForPassStatus("Validate Errors in MediAck file",
												"Uploaded MediAck file should be validated without any errors",
												"Uploaded MediAck file without any errors - " + totalErrors
														+ " successfully.", true);
										return true;
									} else {
										isVisible = true;
										
										ALMFunctions.UpdateReportLogAndALMForFailStatus("Validate Errors in uploaded MediAck file",
												"Uploaded MediAck file should be validated without any errors",
												"Uploaded MediAck file with errors - " + totalErrors
														+ " not successfully.", true);
										

									}
								} else {
									isVisible = false;
									if (intItrCount == 5) {
										isVisible = true;
										
										ALMFunctions.UpdateReportLogAndALMForFailStatus("Validate Errors in uploaded MediAck file",
												"Uploaded MediAck file should be validated without any errors",
												"Upload MediAck file validation failed, taking more than 20 seconds", true);
										
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
		 * method to validate reconciliation status
		 * 
		 * @param strElementName
		 * @param strValues
		 * @param strElementType
		 * @param strPageName
		 */
		public void validatePayNumber() {
			
			driverUtil.waitUntilElementVisible(Common.xmlTextArea,
					lngMinTimeOutInSeconds);
			WebElement element = driver.findElement(Common.xmlTextArea);
			String strXmlContent = element.getText();
			XMLDataAccess xmlAccess = new XMLDataAccess();
			String strPayNum = dataTable.getData("Parametrized_Checkpoints", "Payment #:");
			String strXpathExpression = "/ImportInvoice/Invoices/InvoiceNumber";
			String strInvoiceNumber = xmlAccess.validateInvoiceNumber(strXmlContent, strPayNum,
					strXpathExpression);
			if (strInvoiceNumber.equalsIgnoreCase(strPayNum)) {
				
					
					ALMFunctions.UpdateReportLogAndALMForPassStatus("Validate the Payment Number",
							"Payment Number is Equal to Invoice Number successfully",
							"Payment Number is Equal to Invoice Number successfully with " + strPayNum, true);
				}
			else {

				
				ALMFunctions.UpdateReportLogAndALMForFailStatus("Validate the Payment Number",
						"Payment Number not validated successfully",
						"Payment Number is not validated successfully", true);
			}

		}
		
		
		
}
