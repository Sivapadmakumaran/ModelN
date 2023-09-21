package businesscomponents;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.cognizant.craft.ScriptHelper;
import com.cognizant.framework.Status;

import pages.Common;

public class GPFlow extends CommonActionsAndFunctions{

	public GPFlow(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}
	public final long lngMinTimeOutInSeconds = Long.parseLong(properties.getProperty("MinObjectSyncTimeout"));
	public final long lngCtrlTimeOutInSeconds = Long.parseLong(properties.getProperty("UploadControlTimeout"));
	public final long jobTimeOut = Long.parseLong(properties.getProperty("JobTimeOut"));
	public final long staleTableTimeOut = Long.parseLong(properties.getProperty("StaleTableTimeOut"));
	
	
	/**
	 * Function to call Fill form Utility for GP workbook creation
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void gpWorkbookCreation() {
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "GP workbook creation";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	/**
	 * Function to call Fill form Utility for Publish the Workbook
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void publishTheWorkbook() {
		
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "Publish the Workbook";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	/**
	 * Function to call Fill form Utility for Publish the Workbook For BP
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void publishTheWorkbookForBP() {
		
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "Publish the Workbook for BP";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	/**
	 * Function to call Fill form Utility for Publish the Workbook For BP Initial
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void publishTheWorkbookForBPInitial() {
		
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "Publish the Workbook for BP Initial";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	/**
	 * Function to call Fill form Utility for Publish the Workbook For ASP
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void publishTheWorkbookForASP() {
		
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "Publish the Workbook for ASP";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	/**
	 * Function to call Fill form Utility for Publish the Workbook For NFAMP
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void publishTheWorkbookForNFAMP() {
		
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "Publish the Workbook for NFAMP";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	/**
	 * Function to call Fill form Utility for Publish the Workbook For FCP
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void publishTheWorkbookForFCP() {
		
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "Publish the Workbook for FCP";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	/**
	 * Function to call Fill form Utility for Validations of 12 Month Managed Care Mail Order Rebates
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void validationsOf12MonthManagedCare() {
		

		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "Validations of 12 Month Managed Care Mail Order Rebates";
		fillFormExcelFunctions.FillInputForm(strInputParameter);
		

	}
	/**
	 * Function to call Fill form Utility for GP workbook creation for ASP
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void gpWorkbookCreationForASP() {
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "GP workbook creation for ASP";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	/**
	 * Function to call Fill form Utility for GP workbook creation for ASP
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void gpWorkbookCreationForNFAMP() {
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "GP workbook creation for NFAMP";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	/**
	 * Function to call Fill form Utility for GP workbook creation for FCP
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void gpWorkbookCreationForFCP() {
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "GP workbook creation for FCP";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	/**
	 * Function to call Fill form Utility for GP workbook creation for Best Price
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void gpWorkbookCreationForBP() {
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "GP workbook creation for BP";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	/**
	 * Function to call Fill form Utility for GP workbook creation for BP Initial
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void gpWorkbookCreationForBPInitial() {
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "GP workbook creation for BP Initial";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	/**
	 * Function to call Fill form Utility for GP workbook creation for Best Price and BP Initial
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void gpWorkbookCreationForBPAndBPInitial() {
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "GP workbook creation For BP and BP Initial";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	/**
	 * Function to call Fill form Utility for Get Values From Direct and Indirect Sales and Rebates
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void getValuesOfDirectIndirectAndRebate() {
		

		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "Get Values From Direct and Indirect Sales and Rebates";
		fillFormExcelFunctions.FillInputForm(strInputParameter);
		

	}
	/**
	 * Function to call Fill form Utility for Validate Direct Sale Lines Details
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void validateDirectSaleLines() {
		

		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "Validate Direct Sale Lines details";
		fillFormExcelFunctions.FillInputForm(strInputParameter);
		

	}
	/**
	 * Function to call Fill form Utility for Validate Direct Sale Lines Details
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void validateDirectSaleLinesForNFAMP() {
		

		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "Validate Direct Sale Lines details For NFAMP";
		fillFormExcelFunctions.FillInputForm(strInputParameter);
		

	}
	/**
	 * Function to call Fill form Utility for Creation of GP Price type Life Cycle Validation
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void creationOfGPPriceTypeLifeCycle() {

		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "GP Price Type Life Cycle";
		fillFormExcelFunctions.FillInputForm(strInputParameter);
		

	}
	/**
	 * Function to call Fill form Utility for Validations of 12 month chargeback
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void validationOf12MonthChargeBack() {

		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "Validation of 12 month chargeback";
		fillFormExcelFunctions.FillInputForm(strInputParameter);
		

	}
	/**
	 * Function to call Fill form Utility for Validations of Chargeback for ASP
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void validationOfChargeBackForASP() {

		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "Validate Chargeback Indirect Lines";
		fillFormExcelFunctions.FillInputForm(strInputParameter);
		

	}
	/**
	 * Method to change contract End date for 12 month Manage care Mail order rebate
	 * 
	 * @param No parameter
	 * @return No return value
	 * @throws Exception 
	 */
	public void changeContractEndDate() throws Exception {
        Common editButton=new Common("Contract End Date");
        clickByJs(editButton.editbutton, lngMinTimeOutInSeconds, "Edit", "Button", driver.getTitle(), true);
		pagescroll(Common.dateInput, driver.getTitle());
		clear(Common.dateInput, lngMinTimeOutInSeconds, "Date Field", driver.getTitle());
		sendkeys(Common.dateInput, lngMinTimeOutInSeconds, "12/31/2023 00:00", "Date Field", driver.getTitle(),true);
		clickByJs(Common.tickButton, lngMinTimeOutInSeconds, "Ok", "Button", driver.getTitle(), true);

	}
	/**
	 * Method to Verify Revision History Table
	 * 
	 * @param No parameter
	 * @return No return value
	 * @throws Exception 
	 */
	public void verifyRevisionHistory() throws Exception {
        
		Common revisionStatus=new Common("Reason","Revised the existing price type");
        String strExpectedStatus=getText(revisionStatus.revisionHistoryTable, lngMinTimeOutInSeconds, "Reason", driver.getTitle());
        String strActualStatus="Revised the existing price type";
        if(strExpectedStatus.equalsIgnoreCase(strActualStatus)) {
        	ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify the Revision History",
					"Reason provided during revision should be displayed",
					"Reason provided during revision is displayed", true);
        }else {
        	ALMFunctions.ThrowException("Verify the Revision History",
					"Reason provided during revision should be displayed",
					"Reason provided during revision is not displayed", true);
        }
	}
	/**
	 * Method to verify the calculation of the Best price discount 
	 *
	 *
	 * @param strPageName   - Page Name in which the control is available
	 * @return No return value
	 */

	public void calculateBestPriceDiscount() {
		
		String strDirectLinesInclusion=dataTable.getData("Parametrized_Checkpoints", "# of Inclusions:");
		strDirectLinesInclusion=strDirectLinesInclusion.replace(",", "");		
		String strDirectLinesExclusion=dataTable.getData("Parametrized_Checkpoints", "# of Exclusions:");
		strDirectLinesExclusion=strDirectLinesExclusion.replace(",", "");
		String strIndirectLinesInclusion=dataTable.getData("Parametrized_Checkpoints", "Indirect Inclusion");
		strIndirectLinesInclusion=strIndirectLinesInclusion.replace(",", "");
		String strIndirectLinesExclusion=dataTable.getData("Parametrized_Checkpoints", "Indirect Exclusion");
		strIndirectLinesExclusion=strIndirectLinesExclusion.replace(",", "");
		String strRebateLinesInclusion=dataTable.getData("Parametrized_Checkpoints", "Rebate Inclusion");
		strRebateLinesInclusion=strRebateLinesInclusion.replace(",", "");
		String strRebateLinesExclusion=dataTable.getData("Parametrized_Checkpoints", "Rebate Exclusion");
		strRebateLinesExclusion=strRebateLinesExclusion.replace(",", "");
		
		double intTotalDirectLines= Double.parseDouble(strDirectLinesInclusion)+Double.parseDouble(strDirectLinesExclusion);
		double intTotalIndirectLines=Double.parseDouble(strIndirectLinesInclusion)+Double.parseDouble(strIndirectLinesExclusion);
		double intTotalRebateLines=Double.parseDouble(strRebateLinesInclusion)+Double.parseDouble(strRebateLinesExclusion);
		double intTotalSales=intTotalDirectLines+intTotalIndirectLines;
		double intDiscountAmount= intTotalRebateLines/intTotalSales;
		
		report.updateTestLog("Calculate of the Best price discount amount","Calculated Discount Amount is "+"'"+intDiscountAmount+"'",
				"Calculated Discount Amount is "+"'"+intDiscountAmount+"'", Status.DONE);
		
	}
	/**
	 * Method to verify the NDC 9 Calculation. This method is used Accept and Override the Exception
	 *
	 *
	 * @param strPageName   - Page Name in which the control is available
	 * @return No return value
	 */

	public void ndc9Calculation() throws Exception {

		String strPageName="Woorkbook Calculation";
		String strPageSize="Page Size:";
		
		Common okButton=new Common("Manually Accept/Override Suggested Values", "Ok");
		Common pageSize=new Common(strPageSize);
		Common updateLines=new Common("Update Lines");
		Common labelPriceType=new Common("Price Type:");
		Common labelUnresolvedExceptions=new Common("Unresolved Exceptions:");
		Common labelAcceptedExceptions=new Common("Accepted Exceptions:");
		Common sortByStatus=new Common("Status");
		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
		driverUtil.waitUntilStalenessOfElement(labelPriceType.labelValue, strPageName);
		String strPricetype=driver.findElement(labelPriceType.labelValue).getText();
		objectExists(labelPriceType.labelValue,"isDisplayed", lngCtrlTimeOutInSeconds, "Price Type", strPricetype, strPageName, true);
		driverUtil.waitUntilStalenessOfElement(labelUnresolvedExceptions.labelValue, strPageName);
		//driverUtil.waitUntilElementVisible(labelUnresolvedExceptions.labelValue, lngCtrlTimeOutInSeconds, "Unresolved Exceptions", "Label Value", strPageName, false);
		String strValuesforException=driver.findElement(labelUnresolvedExceptions.labelValue).getText();
		if(!strValuesforException.equalsIgnoreCase("0"))
		{
			
			click(Common.filterButton, lngCtrlTimeOutInSeconds, "Filter", "Button", strPageName, false);
			selectDropdownByvalue(pageSize.dropdown, "Pagination DropDown", "250", strPageName, false);
			click(sortByStatus.tableStatus, lngCtrlTimeOutInSeconds, "Sort By Status", "Icon", strPageName, false);
			driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
			int intNum=Integer.parseInt(strValuesforException);
			for(int index=1;index<=intNum;index++) {
				String strPatternValue=Integer.toString(index);
				pages.Common tableException=new pages.Common(strPatternValue);
				driverUtil.waitUntilStalenessOfElement(tableException.tableExceptionsCheckbox, staleTableTimeOut, driver.getTitle());
				pagescroll(tableException.tableExceptionsCheckbox,driver.getTitle());
				click(tableException.tableExceptionsCheckbox,lngCtrlTimeOutInSeconds, "TableRow:"+index, "Checkbox", strPageName, false);
			}
			report.updateTestLog("Click "+"'"+strValuesforException+"'"+" Exceptions Checkboxes in a Table","'"+strValuesforException+"'"+" Exceptions Checkboxes should be clicked in the " + strPageName + " Page",
					"'"+strValuesforException+"'"+" Exceptions Checkboxes is clicked in the " + strPageName + " Page", Status.DONE);
			click(updateLines.button, lngCtrlTimeOutInSeconds, "Update Lines", "Button", strPageName, true);
			click(Common.acceptOverrideBtn, lngCtrlTimeOutInSeconds, "Accept Override", "Button", strPageName, true);
			
			By locatorDyn = Common.dynFrame;
			driverUtil.waitUntilStalenessOfElement(locatorDyn, staleTableTimeOut, driver.getTitle());
			WebElement elmFrameDyn = driver.findElement(locatorDyn);
			driver.switchTo().frame(elmFrameDyn);
			for(int index=0;index<intNum;index++) {
				String strPatternValue=Integer.toString(index); 
				pages.Common selectActionDropdown=new pages.Common(strPatternValue);
				pages.Common textArea = new pages.Common(strPatternValue);
				driverUtil.waitUntilStalenessOfElement(selectActionDropdown.tableSelect, staleTableTimeOut, driver.getTitle());
				pagescroll(selectActionDropdown.tableSelect,driver.getTitle());
				driverUtil.waitUntilStalenessOfElement(selectActionDropdown.tableSelect, staleTableTimeOut, driver.getTitle());
				selectDropdownByIndexvalue(selectActionDropdown.tableSelect, "Action Dropdown","Override amount",0, strPageName, false);
				sendkeys(textArea.tableTextArea, lngMinTimeOutInSeconds, "Override Amount", "Text Area", strPageName,false);
				sendkeys(textArea.tableTextArea, lngMinTimeOutInSeconds, Keys.TAB, "Override Amount", strPageName);
			}
			report.updateTestLog("Enter the Override Amount in the "+strValuesforException+" '"+"Text Area"+"'" ,"'"+"Override Amount"+"' should be entered into the "+strValuesforException+" Text Area textboxes",
					"'"+"Override Amount"+"' is entered into the '"+strValuesforException+"' Text Area textboxes",Status.DONE);
			click(okButton.dialogButton,lngCtrlTimeOutInSeconds, "OK", "Button", strPageName, false);
			driver.switchTo().defaultContent();
			By locatorDocument = Common.documentFrame;
			driverUtil.waitUntilStalenessOfElement(locatorDocument, staleTableTimeOut, driver.getTitle());
			WebElement elmFrameDoc = driver.findElement(locatorDocument);
			driver.switchTo().frame(elmFrameDoc);
			driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
			String strValuesforAcceptedException=driver.findElement(labelAcceptedExceptions.labelValue).getText();
			pagescroll(driver.findElement(labelAcceptedExceptions.labelValue), strPageName);
			if(strValuesforException.equalsIgnoreCase(strValuesforAcceptedException)) {
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Validate the NDC 9 Calculations with Unresolved Exception and Accepted Exception",
						"Unresolved Exception of "+strValuesforException+ " should be Overrided and Accepted then Changed to Accepted Exception of "+strValuesforAcceptedException,
						"Unresolved Exception of "+strValuesforException+ " are Overrided and Accepted then Changed to Accepted Exception of "+strValuesforAcceptedException, true);
			
				CommonFunctions commonFunctions = new CommonFunctions(scriptHelper);
				commonFunctions.selectActionsOrNew("Actions", "Save", strPageName);
				String strWorkBookName = dataTable.getData("Parametrized_Checkpoints", "Name:");
				String strWorkBookNo = dataTable.getData("Parametrized_Checkpoints", "Workbook #:");
				String strExpectedVal = "Workbooks - "+strWorkBookName+" - "+strWorkBookNo+" (Draft: Calculation Valid)";
				commonFunctions.verifyObjectState(strExpectedVal, "label!exist", strPageName);

			}
			else {
				ALMFunctions.ThrowException("Validate the NDC 9 Calculations with Unresolved Exception and Accepted Exception",
						"Unresolved Exception of "+strValuesforException+ " should be Overrided and Accepted then Changed to Accepted Exception of "+strValuesforAcceptedException,
						"Unresolved Exception of "+strValuesforException+ " are not Overrided and Accepted then Changed to Accepted Exception of "+strValuesforAcceptedException, true);
			}
		}
		else
		{
			report.updateTestLog("Verify NDC 9 Calculations with Unresolved Exception and Accepted Exception", 
					"Exception should be resolved", "Exception is already resolved without any error", Status.DONE);
		}
	}
	/**
	 * Method to verify the NDC 9 Calculation. This method is used Accept and Override the Exception
	 *
	 *
	 * @param strPageName   - Page Name in which the control is available
	 * @return No return value
	 */

	public void ndc9CalculationWithOneException() throws Exception {

		String strPageName="Woorkbook Calculation";
		String strPageSize="Page Size:";
		
		Common okButton=new Common("Manually Accept/Override Suggested Values", "Ok");
		Common pageSize=new Common(strPageSize);
		Common updateLines=new Common("Update Lines");
		Common labelPriceType=new Common("Price Type:");
		Common labelUnresolvedExceptions=new Common("Unresolved Exceptions:");
		Common labelAcceptedExceptions=new Common("Accepted Exceptions:");
		Common sortByStatus=new Common("Status");
		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
		driverUtil.waitUntilStalenessOfElement(labelPriceType.labelValue, strPageName);
		String strPricetype=driver.findElement(labelPriceType.labelValue).getText();
		objectExists(labelPriceType.labelValue,"isDisplayed", lngCtrlTimeOutInSeconds, "Price Type", strPricetype, strPageName, true);
		driverUtil.waitUntilStalenessOfElement(labelUnresolvedExceptions.labelValue, strPageName);
		//driverUtil.waitUntilElementVisible(labelUnresolvedExceptions.labelValue, lngCtrlTimeOutInSeconds, "Unresolved Exceptions", "Label Value", strPageName, false);
		String strValuesforException=driver.findElement(labelUnresolvedExceptions.labelValue).getText();
		if(!strValuesforException.equalsIgnoreCase("0"))
		{
			
			click(Common.filterButton, lngCtrlTimeOutInSeconds, "Filter", "Button", strPageName, false);
			selectDropdownByvalue(pageSize.dropdown, "Pagination DropDown", "250", strPageName, false);
			click(sortByStatus.tableStatus, lngCtrlTimeOutInSeconds, "Sort By Status", "Icon", strPageName, false);
			driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
			int intNum=Integer.parseInt(strValuesforException);
			for(int index=1;index<=intNum;index++) {
				String strPatternValue=Integer.toString(index);
				pages.Common tableException=new pages.Common(strPatternValue);
				driverUtil.waitUntilStalenessOfElement(tableException.tableExceptionCheckbox, staleTableTimeOut, driver.getTitle());
				pagescroll(tableException.tableExceptionCheckbox,driver.getTitle());
				click(tableException.tableExceptionCheckbox,lngCtrlTimeOutInSeconds, "TableRow:"+index, "Checkbox", strPageName, false);
			}
			report.updateTestLog("Click "+"'"+strValuesforException+"'"+" Exceptions Checkboxes in a Table","'"+strValuesforException+"'"+" Exceptions Checkboxes should be clicked in the " + strPageName + " Page",
					"'"+strValuesforException+"'"+" Exceptions Checkboxes is clicked in the " + strPageName + " Page", Status.DONE);
			click(updateLines.button, lngCtrlTimeOutInSeconds, "Update Lines", "Button", strPageName, true);
			click(Common.acceptOverrideBtn, lngCtrlTimeOutInSeconds, "Accept Override", "Button", strPageName, true);
			
			By locatorDyn = Common.dynFrame;
			driverUtil.waitUntilStalenessOfElement(locatorDyn, staleTableTimeOut, driver.getTitle());
			WebElement elmFrameDyn = driver.findElement(locatorDyn);
			driver.switchTo().frame(elmFrameDyn);
			for(int index=0;index<intNum;index++) {
				String strPatternValue=Integer.toString(index); 
				pages.Common selectActionDropdown=new pages.Common(strPatternValue);
				pages.Common textArea = new pages.Common(strPatternValue);
				driverUtil.waitUntilStalenessOfElement(selectActionDropdown.tableSelect, staleTableTimeOut, driver.getTitle());
				pagescroll(selectActionDropdown.tableSelect,driver.getTitle());
				driverUtil.waitUntilStalenessOfElement(selectActionDropdown.tableSelect, staleTableTimeOut, driver.getTitle());
				selectDropdownByIndexvalue(selectActionDropdown.tableSelect, "Action Dropdown","Override amount",0, strPageName, false);
				sendkeys(textArea.tableTextArea, lngMinTimeOutInSeconds, "Override Amount", "Text Area", strPageName,false);
				sendkeys(textArea.tableTextArea, lngMinTimeOutInSeconds, Keys.TAB, "Override Amount", strPageName);
			}
			report.updateTestLog("Enter the Override Amount in the "+strValuesforException+" '"+"Text Area"+"'" ,"'"+"Override Amount"+"' should be entered into the "+strValuesforException+" Text Area textboxes",
					"'"+"Override Amount"+"' is entered into the '"+strValuesforException+"' Text Area textboxes",Status.DONE);
			click(okButton.dialogButton,lngCtrlTimeOutInSeconds, "OK", "Button", strPageName, false);
			driver.switchTo().defaultContent();
			By locatorDocument = Common.documentFrame;
			driverUtil.waitUntilStalenessOfElement(locatorDocument, staleTableTimeOut, driver.getTitle());
			WebElement elmFrameDoc = driver.findElement(locatorDocument);
			driver.switchTo().frame(elmFrameDoc);
			driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
			String strValuesforAcceptedException=driver.findElement(labelAcceptedExceptions.labelValue).getText();
			pagescroll(driver.findElement(labelAcceptedExceptions.labelValue), strPageName);
			if(strValuesforException.equalsIgnoreCase(strValuesforAcceptedException)) {
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Validate the NDC 9 Calculations with Unresolved Exception and Accepted Exception",
						"Unresolved Exception of "+strValuesforException+ " should be Overrided and Accepted then Changed to Accepted Exception of "+strValuesforAcceptedException,
						"Unresolved Exception of "+strValuesforException+ " are Overrided and Accepted then Changed to Accepted Exception of "+strValuesforAcceptedException, true);
			
				CommonFunctions commonFunctions = new CommonFunctions(scriptHelper);
				commonFunctions.selectActionsOrNew("Actions", "Save", strPageName);
				String strWorkBookName = dataTable.getData("Parametrized_Checkpoints", "Name:");
				String strWorkBookNo = dataTable.getData("Parametrized_Checkpoints", "Workbook #:");
				String strExpectedVal = "Workbooks - "+strWorkBookName+" - "+strWorkBookNo+" (Draft: Calculation Valid)";
				commonFunctions.verifyObjectState(strExpectedVal, "label!exist", strPageName);

			}
			else {
				ALMFunctions.ThrowException("Validate the NDC 9 Calculations with Unresolved Exception and Accepted Exception",
						"Unresolved Exception of "+strValuesforException+ " should be Overrided and Accepted then Changed to Accepted Exception of "+strValuesforAcceptedException,
						"Unresolved Exception of "+strValuesforException+ " are not Overrided and Accepted then Changed to Accepted Exception of "+strValuesforAcceptedException, true);
			}
		}
		else
		{
			report.updateTestLog("Verify NDC 9 Calculations with Unresolved Exception and Accepted Exception", 
					"Exception should be resolved", "Exception is already resolved without any error", Status.DONE);
		}
	}
	/**
	 * Method to verify the NDC 9 Calculation For Best Price Initial price Type. This method is used Accept and Override the Exception
	 *
	 *
	 * @param strPageName   - Page Name in which the control is available
	 * @return No return value
	 */

	public void ndc9CalculationForBPInitial() throws Exception {

		String strPageName="Woorkbook Calculation";
		String strPageSize="Page Size:";
		
		Common okButton=new Common("Manually Accept/Override Suggested Values", "Ok");
		Common pageSize=new Common(strPageSize);
		Common updateLines=new Common("Update Lines");
		Common labelPriceType=new Common("Price Type:");
		Common labelUnresolvedExceptions=new Common("Unresolved Exceptions:");
		Common labelOverriddenExceptions=new Common("Overridden Exceptions:");
		Common sortByStatus=new Common("Status");
		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
		driverUtil.waitUntilStalenessOfElement(labelPriceType.labelValue, strPageName);
		String strPricetype=driver.findElement(labelPriceType.labelValue).getText();
		objectExists(labelPriceType.labelValue,"isDisplayed", lngCtrlTimeOutInSeconds, "Price Type", strPricetype, strPageName, true);
		driverUtil.waitUntilStalenessOfElement(labelUnresolvedExceptions.labelValue, strPageName);
		//driverUtil.waitUntilElementVisible(labelUnresolvedExceptions.labelValue, lngCtrlTimeOutInSeconds, "Unresolved Exceptions", "Label Value", strPageName, false);
		String strValuesforException=driver.findElement(labelUnresolvedExceptions.labelValue).getText();
		if(!strValuesforException.equalsIgnoreCase("0"))
		{
			
			click(Common.filterButton, lngCtrlTimeOutInSeconds, "Filter", "Button", strPageName, false);
			selectDropdownByvalue(pageSize.dropdown, "Pagination DropDown", "250", strPageName, false);
			click(sortByStatus.tableStatus, lngCtrlTimeOutInSeconds, "Sort By Status", "Icon", strPageName, false);
			driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
			int intNum=Integer.parseInt(strValuesforException);
			for(int index=1;index<=intNum;index++) {
				String strPatternValue=Integer.toString(index);
				pages.Common tableException=new pages.Common(strPatternValue);
				driverUtil.waitUntilStalenessOfElement(tableException.tableExceptionCheckbox, staleTableTimeOut, driver.getTitle());
				pagescroll(tableException.tableExceptionCheckbox,driver.getTitle());
				click(tableException.tableExceptionCheckbox,lngCtrlTimeOutInSeconds, "TableRow:"+index, "Checkbox", strPageName, false);
			}
			report.updateTestLog("Click "+"'"+strValuesforException+"'"+" Exceptions Checkboxes in a Table","'"+strValuesforException+"'"+" Exceptions Checkboxes should be clicked in the " + strPageName + " Page",
					"'"+strValuesforException+"'"+" Exceptions Checkboxes is clicked in the " + strPageName + " Page", Status.DONE);
			click(updateLines.button, lngCtrlTimeOutInSeconds, "Update Lines", "Button", strPageName, true);
			click(Common.acceptOverrideBtn, lngCtrlTimeOutInSeconds, "Accept Override", "Button", strPageName, true);
			
			By locatorDyn = Common.dynFrame;
			driverUtil.waitUntilStalenessOfElement(locatorDyn, staleTableTimeOut, driver.getTitle());
			WebElement elmFrameDyn = driver.findElement(locatorDyn);
			driver.switchTo().frame(elmFrameDyn);
			for(int index=0;index<intNum;index++) {
				String strPatternValue=Integer.toString(index); 
				pages.Common selectActionDropdown=new pages.Common(strPatternValue);
				pages.Common textInput=new pages.Common(strPatternValue);
				pages.Common textArea = new pages.Common(strPatternValue);
				driverUtil.waitUntilStalenessOfElement(selectActionDropdown.tableSelect, staleTableTimeOut, driver.getTitle());
				pagescroll(selectActionDropdown.tableSelect,driver.getTitle());
				driverUtil.waitUntilStalenessOfElement(selectActionDropdown.tableSelect, staleTableTimeOut, driver.getTitle());
				selectDropdownByvalue(selectActionDropdown.tableSelect, "Action Dropdown","Override", strPageName, false);
				sendkeys(textInput.tableTextInput, lngMinTimeOutInSeconds, "10", "Text Input", strPageName,false);
				sendkeys(textArea.tableTextArea, lngMinTimeOutInSeconds, "Override Amount", "Text Area", strPageName,false);
				sendkeys(textArea.tableTextArea, lngMinTimeOutInSeconds, Keys.TAB, "Override Amount", strPageName);
			}
			report.updateTestLog("Enter the Override Amount in the "+strValuesforException+" '"+"Text Area"+"'" ,"'"+"Override Amount"+"' should be entered into the "+strValuesforException+" Text Area textboxes",
					"'"+"Override Amount"+"' is entered into the '"+strValuesforException+"' Text Area textboxes",Status.DONE);
			click(okButton.dialogButton,lngCtrlTimeOutInSeconds, "OK", "Button", strPageName, false);
			driver.switchTo().defaultContent();
			By locatorDocument = Common.documentFrame;
			driverUtil.waitUntilStalenessOfElement(locatorDocument, staleTableTimeOut, driver.getTitle());
			WebElement elmFrameDoc = driver.findElement(locatorDocument);
			driver.switchTo().frame(elmFrameDoc);
			driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
			String strValuesforOverriddenException=driver.findElement(labelOverriddenExceptions.labelValue).getText();
			pagescroll(driver.findElement(labelOverriddenExceptions.labelValue), strPageName);
			if(strValuesforException.equalsIgnoreCase(strValuesforOverriddenException)) {
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Validate the NDC 9 Calculations with Unresolved Exception and Overridden Exception",
						"Unresolved Exception of "+strValuesforException+ " should be Overrided and Accepted then Changed to Overridden Exception of "+strValuesforOverriddenException,
						"Unresolved Exception of "+strValuesforException+ " are Overrided and Accepted then Changed to Overridden Exception of "+strValuesforOverriddenException, true);
			
				CommonFunctions commonFunctions = new CommonFunctions(scriptHelper);
				commonFunctions.selectActionsOrNew("Actions", "Save", strPageName);
				String strWorkBookName = dataTable.getData("Parametrized_Checkpoints", "Name:");
				String strWorkBookNo = dataTable.getData("Parametrized_Checkpoints", "Workbook #:");
				String strExpectedVal = "Workbooks - "+strWorkBookName+" - "+strWorkBookNo+" (Draft: Calculation Valid)";
				commonFunctions.verifyObjectState(strExpectedVal, "label!exist", strPageName);

			}
			else {
				ALMFunctions.ThrowException("Validate the NDC 9 Calculations with Unresolved Exception and Overridden Exception",
						"Unresolved Exception of "+strValuesforException+ " should be Overrided and Accepted then Changed to Overridden Exception of "+strValuesforOverriddenException,
						"Unresolved Exception of "+strValuesforException+ " are not Overrided and Accepted then Changed to Overridden Exception of "+strValuesforOverriddenException, true);
			}
		}
		else
		{
			report.updateTestLog("Verify NDC 9 Calculations with Unresolved Exception and Accepted Exception", 
					"Exception should be resolved", "Exception is already resolved without any error", Status.DONE);
		}
	}
	/**
	 * Method to verify the NDC 11 Calculation For ASP price Type. This method is used Accept and Override the Exception
	 *
	 *
	 * @param strPageName   - Page Name in which the control is available
	 * @return No return value
	 */

	public void ndc11CalculationForASP() throws Exception {

		String strPageName="Woorkbook Calculation";
		
		Common okButton=new Common("Manually Accept/Override Suggested Values", "Ok");
		
		Common updateLines=new Common("Update Lines");
		Common labelPriceType=new Common("Price Type:");
		Common labelUnresolvedExceptions=new Common("Unresolved Exceptions:");
		Common labelOverriddenExceptions=new Common("Overridden Exceptions:");
		Common sortByStatus=new Common("Status");
		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
		driverUtil.waitUntilStalenessOfElement(labelPriceType.labelValue, strPageName);
		String strPricetype=driver.findElement(labelPriceType.labelValue).getText();
		objectExists(labelPriceType.labelValue,"isDisplayed", lngCtrlTimeOutInSeconds, "Price Type", strPricetype, strPageName, true);
		driverUtil.waitUntilStalenessOfElement(labelUnresolvedExceptions.labelValue, strPageName);
		//driverUtil.waitUntilElementVisible(labelUnresolvedExceptions.labelValue, lngCtrlTimeOutInSeconds, "Unresolved Exceptions", "Label Value", strPageName, false);
		String strValuesforException=driver.findElement(labelUnresolvedExceptions.labelValue).getText();
		if(!strValuesforException.equalsIgnoreCase("0"))
		{
			
			click(Common.filterButton, lngCtrlTimeOutInSeconds, "Filter", "Button", strPageName, false);
			
			click(sortByStatus.tableStatus, lngCtrlTimeOutInSeconds, "Sort By Status", "Icon", strPageName, false);
			driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
			int intNum=Integer.parseInt(strValuesforException);
			for(int index=1;index<=intNum;index++) {
				String strPatternValue=Integer.toString(index);
				pages.Common tableException=new pages.Common(strPatternValue);
				driverUtil.waitUntilStalenessOfElement(tableException.tableExceptionsCheckbox, staleTableTimeOut, driver.getTitle());
				pagescroll(tableException.tableExceptionsCheckbox,driver.getTitle());
				click(tableException.tableExceptionsCheckbox,lngCtrlTimeOutInSeconds, "TableRow:"+index, "Checkbox", strPageName, false);
			}
			report.updateTestLog("Click "+"'"+strValuesforException+"'"+" Exceptions Checkboxes in a Table","'"+strValuesforException+"'"+" Exceptions Checkboxes should be clicked in the " + strPageName + " Page",
					"'"+strValuesforException+"'"+" Exceptions Checkboxes is clicked in the " + strPageName + " Page", Status.DONE);
			click(updateLines.button, lngCtrlTimeOutInSeconds, "Update Lines", "Button", strPageName, true);
			click(Common.acceptOverrideBtn, lngCtrlTimeOutInSeconds, "Accept Override", "Button", strPageName, true);
			
			By locatorDyn = Common.dynFrame;
			driverUtil.waitUntilStalenessOfElement(locatorDyn, staleTableTimeOut, driver.getTitle());
			WebElement elmFrameDyn = driver.findElement(locatorDyn);
			driver.switchTo().frame(elmFrameDyn);
			for(int index=0;index<intNum;index++) {
				String strPatternValue=Integer.toString(index); 
				pages.Common selectActionDropdown=new pages.Common(strPatternValue);
				pages.Common textInput=new pages.Common(strPatternValue);
				pages.Common textArea = new pages.Common(strPatternValue);
				driverUtil.waitUntilStalenessOfElement(selectActionDropdown.tableSelect, staleTableTimeOut, driver.getTitle());
				pagescroll(selectActionDropdown.tableSelect,driver.getTitle());
				driverUtil.waitUntilStalenessOfElement(selectActionDropdown.tableSelect, staleTableTimeOut, driver.getTitle());
				selectDropdownByvalue(selectActionDropdown.tableSelect, "Action Dropdown","Override", strPageName, false);
				sendkeys(textInput.tableTextInput, lngMinTimeOutInSeconds, "10", "Text Input", strPageName,false);
				sendkeys(textArea.tableTextArea, lngMinTimeOutInSeconds, "Override Amount", "Text Area", strPageName,false);
				sendkeys(textArea.tableTextArea, lngMinTimeOutInSeconds, Keys.TAB, "Override Amount", strPageName);
			}
			report.updateTestLog("Enter the Override Amount in the "+strValuesforException+" '"+"Text Area"+"'" ,"'"+"Override Amount"+"' should be entered into the "+strValuesforException+" Text Area textboxes",
					"'"+"Override Amount"+"' is entered into the '"+strValuesforException+"' Text Area textboxes",Status.DONE);
			click(okButton.dialogButton,lngCtrlTimeOutInSeconds, "OK", "Button", strPageName, false);
			driver.switchTo().defaultContent();
			By locatorDocument = Common.documentFrame;
			driverUtil.waitUntilStalenessOfElement(locatorDocument, staleTableTimeOut, driver.getTitle());
			WebElement elmFrameDoc = driver.findElement(locatorDocument);
			driver.switchTo().frame(elmFrameDoc);
			driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
			String strValuesforOverriddenException=driver.findElement(labelOverriddenExceptions.labelValue).getText();
			pagescroll(driver.findElement(labelOverriddenExceptions.labelValue), strPageName);
			if(strValuesforException.equalsIgnoreCase(strValuesforOverriddenException)) {
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Validate the NDC 11 Calculations with Unresolved Exception and Overridden Exception",
						"Unresolved Exception of "+strValuesforException+ " should be Overrided and Accepted then Changed to Overridden Exception of "+strValuesforOverriddenException,
						"Unresolved Exception of "+strValuesforException+ " are Overrided and Accepted then Changed to Overridden Exception of "+strValuesforOverriddenException, true);
			
				CommonFunctions commonFunctions = new CommonFunctions(scriptHelper);
				commonFunctions.selectActionsOrNew("Actions", "Save", strPageName);
				String strWorkBookName = dataTable.getData("Parametrized_Checkpoints", "Name:");
				String strWorkBookNo = dataTable.getData("Parametrized_Checkpoints", "Workbook #:");
				String strExpectedVal = "Workbooks - "+strWorkBookName+" - "+strWorkBookNo+" (Draft: Calculation Valid)";
				commonFunctions.verifyObjectState(strExpectedVal, "label!exist", strPageName);

			}
			else {
				ALMFunctions.ThrowException("Validate the NDC 11 Calculations with Unresolved Exception and Overridden Exception",
						"Unresolved Exception of "+strValuesforException+ " should be Overrided and Accepted then Changed to Overridden Exception of "+strValuesforOverriddenException,
						"Unresolved Exception of "+strValuesforException+ " are not Overrided and Accepted then Changed to Overridden Exception of "+strValuesforOverriddenException, true);
			}
		}
		else
		{
			report.updateTestLog("Verify NDC 11 Calculations with Unresolved Exception and Accepted Exception", 
					"Exception should be resolved", "Exception is already resolved without any error", Status.DONE);
		}
	}
	/**
	 * Method to verify the NDC 11 Calculation For ASP price Type. This method is used Accept and Override the Exception
	 *
	 *
	 * @param strPageName   - Page Name in which the control is available
	 * @return No return value
	 */

	public void ndc11CalculationForNFAMP() throws Exception {

		String strPageName="Woorkbook Calculation";
		
		Common okButton=new Common("Manually Accept/Override Suggested Values", "Ok");
		
		Common updateLines=new Common("Update Lines");
		Common labelPriceType=new Common("Price Type:");
		Common labelUnresolvedExceptions=new Common("Unresolved Exceptions:");
		Common labelOverriddenExceptions=new Common("Overridden Exceptions:");
		Common sortByStatus=new Common("Status");
		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
		driverUtil.waitUntilStalenessOfElement(labelPriceType.labelValue, strPageName);
		String strPricetype=driver.findElement(labelPriceType.labelValue).getText();
		objectExists(labelPriceType.labelValue,"isDisplayed", lngCtrlTimeOutInSeconds, "Price Type", strPricetype, strPageName, true);
		driverUtil.waitUntilStalenessOfElement(labelUnresolvedExceptions.labelValue, strPageName);
		//driverUtil.waitUntilElementVisible(labelUnresolvedExceptions.labelValue, lngCtrlTimeOutInSeconds, "Unresolved Exceptions", "Label Value", strPageName, false);
		String strValuesforException=driver.findElement(labelUnresolvedExceptions.labelValue).getText();
		if(!strValuesforException.equalsIgnoreCase("0"))
		{
			
			click(Common.filterButton, lngCtrlTimeOutInSeconds, "Filter", "Button", strPageName, false);
			
			click(sortByStatus.tableStatus, lngCtrlTimeOutInSeconds, "Sort By Status", "Icon", strPageName, false);
			driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
			int intNum=Integer.parseInt(strValuesforException);
			for(int index=1;index<=intNum;index++) {
				String strPatternValue=Integer.toString(index);
				pages.Common tableException=new pages.Common(strPatternValue);
				driverUtil.waitUntilStalenessOfElement(tableException.tableExceptionsCheckbox, staleTableTimeOut, driver.getTitle());
				pagescroll(tableException.tableExceptionsCheckbox,driver.getTitle());
				click(tableException.tableExceptionsCheckbox,lngCtrlTimeOutInSeconds, "TableRow:"+index, "Checkbox", strPageName, false);
			}
			report.updateTestLog("Click "+"'"+strValuesforException+"'"+" Exceptions Checkboxes in a Table","'"+strValuesforException+"'"+" Exceptions Checkboxes should be clicked in the " + strPageName + " Page",
					"'"+strValuesforException+"'"+" Exceptions Checkboxes is clicked in the " + strPageName + " Page", Status.DONE);
			click(updateLines.button, lngCtrlTimeOutInSeconds, "Update Lines", "Button", strPageName, true);
			click(Common.acceptOverrideBtn, lngCtrlTimeOutInSeconds, "Accept Override", "Button", strPageName, true);
			
			By locatorDyn = Common.dynFrame;
			driverUtil.waitUntilStalenessOfElement(locatorDyn, staleTableTimeOut, driver.getTitle());
			WebElement elmFrameDyn = driver.findElement(locatorDyn);
			driver.switchTo().frame(elmFrameDyn);
			for(int index=0;index<intNum;index++) {
				String strPatternValue=Integer.toString(index); 
				pages.Common selectActionDropdown=new pages.Common(strPatternValue);
				pages.Common textInput=new pages.Common(strPatternValue);
				pages.Common textArea = new pages.Common(strPatternValue);
				driverUtil.waitUntilStalenessOfElement(selectActionDropdown.tableSelect, staleTableTimeOut, driver.getTitle());
				pagescroll(selectActionDropdown.tableSelect,driver.getTitle());
				driverUtil.waitUntilStalenessOfElement(selectActionDropdown.tableSelect, staleTableTimeOut, driver.getTitle());
				selectDropdownByvalue(selectActionDropdown.tableSelect, "Action Dropdown","Override", strPageName, false);
				sendkeys(textInput.tableTextInput, lngMinTimeOutInSeconds, "10", "Text Input", strPageName,false);
				sendkeys(textArea.tableTextArea, lngMinTimeOutInSeconds, "Override Amount", "Text Area", strPageName,false);
				sendkeys(textArea.tableTextArea, lngMinTimeOutInSeconds, Keys.TAB, "Override Amount", strPageName);
			}
			report.updateTestLog("Enter the Override Amount in the "+strValuesforException+" '"+"Text Area"+"'" ,"'"+"Override Amount"+"' should be entered into the "+strValuesforException+" Text Area textboxes",
					"'"+"Override Amount"+"' is entered into the '"+strValuesforException+"' Text Area textboxes",Status.DONE);
			click(okButton.dialogButton,lngCtrlTimeOutInSeconds, "OK", "Button", strPageName, false);
			driver.switchTo().defaultContent();
			By locatorDocument = Common.documentFrame;
			driverUtil.waitUntilStalenessOfElement(locatorDocument, staleTableTimeOut, driver.getTitle());
			WebElement elmFrameDoc = driver.findElement(locatorDocument);
			driver.switchTo().frame(elmFrameDoc);
			driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
			String strValuesforOverriddenException=driver.findElement(labelOverriddenExceptions.labelValue).getText();
			pagescroll(driver.findElement(labelOverriddenExceptions.labelValue), strPageName);
			if(strValuesforException.equalsIgnoreCase(strValuesforOverriddenException)) {
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Validate the NDC 11 Calculations with Unresolved Exception and Overridden Exception",
						"Unresolved Exception of "+strValuesforException+ " should be Overrided and Accepted then Changed to Overridden Exception of "+strValuesforOverriddenException,
						"Unresolved Exception of "+strValuesforException+ " are Overrided and Accepted then Changed to Overridden Exception of "+strValuesforOverriddenException, true);
			
				CommonFunctions commonFunctions = new CommonFunctions(scriptHelper);
				commonFunctions.selectActionsOrNew("Actions", "Save", strPageName);
				String strWorkBookName = dataTable.getData("Parametrized_Checkpoints", "Name:");
				String strWorkBookNo = dataTable.getData("Parametrized_Checkpoints", "Workbook #:");
				String strExpectedVal = "Workbooks - "+strWorkBookName+" - "+strWorkBookNo+" (Draft: Calculation Valid)";
				commonFunctions.verifyObjectState(strExpectedVal, "label!exist", strPageName);

			}
			else {
				ALMFunctions.ThrowException("Validate the NDC 11 Calculations with Unresolved Exception and Overridden Exception",
						"Unresolved Exception of "+strValuesforException+ " should be Overrided and Accepted then Changed to Overridden Exception of "+strValuesforOverriddenException,
						"Unresolved Exception of "+strValuesforException+ " are not Overrided and Accepted then Changed to Overridden Exception of "+strValuesforOverriddenException, true);
			}
		}
		else
		{
			report.updateTestLog("Verify NDC 11 Calculations with Unresolved Exception and Accepted Exception", 
					"Exception should be resolved", "Exception is already resolved without any error", Status.DONE);
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
	public void salesIndirectLines() {

		String strPageName="Indirect Lines";
		pages.Common updateButton=new pages.Common("Update");
		pages.Common addCriteria=new pages.Common("Add Criteria");
		pages.Common addButton=new pages.Common("Add");
		pages.Common searchTextArea=new pages.Common("Search");
		pages.Common contractID=new pages.Common("Contract ID");
		pages.Common lineID=new pages.Common("Line ID");
		String strContractIDText=dataTable.getData("Data", "ChargeBack Contract ID");
		String strLineIDText=dataTable.getData("Data", "ChargeBack Line ID");
		pages.Common contractIDTypeAhead=new pages.Common(strContractIDText);
		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
		if(objectExists(Common.open,"isDisplayed", lngCtrlTimeOutInSeconds, "Open", "List", strPageName, false)) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify the Indirect Lines Page",
					"Open dropdown in Indirect Lines pages should be displayed",
					"Open dropdown in Indirect Lines pages is displayed", true);
			driverUtil.waitUntilElementDisplayed(Common.open);
			mouseOverandClick(Common.open, lngCtrlTimeOutInSeconds, "Open", "List", strPageName);
			click(Common.close,lngCtrlTimeOutInSeconds, "Close", "List", strPageName, true);
			click(updateButton.button,lngCtrlTimeOutInSeconds, "Update", "Button", strPageName, true);
			click(addCriteria.button,lngCtrlTimeOutInSeconds, "Add Criteria", "Button", strPageName, true);
			sendkeys(searchTextArea.textbox, lngMinTimeOutInSeconds, "Contract ID", "Search Section", strPageName,true);
			click(contractID.label,lngCtrlTimeOutInSeconds, "Contract ID", "List", strPageName, true);
			click(addButton.button,lngCtrlTimeOutInSeconds, "Add", "Button", strPageName, true);
			click(addCriteria.button,lngCtrlTimeOutInSeconds, "Add Criteria", "Button", strPageName, true);
			sendkeys(searchTextArea.textbox, lngMinTimeOutInSeconds, "Line ID", "Search Section", strPageName,true);
			click(lineID.label,lngCtrlTimeOutInSeconds, "Line ID", "List", strPageName, true);
			click(addButton.button,lngCtrlTimeOutInSeconds, "Add", "Button", strPageName, true);
			sendkeys(contractID.textbox, lngMinTimeOutInSeconds, strContractIDText, "Contract ID Section", strPageName,true);
			click(contractIDTypeAhead.typeAheadLabel,lngCtrlTimeOutInSeconds, "Contract ID", "Type Ahead List", strPageName, true);
			click(Common.additem,lngCtrlTimeOutInSeconds, "Add", "Button", strPageName, true);
			sendkeys(lineID.textbox, lngMinTimeOutInSeconds, strLineIDText, "Line ID Section", strPageName,true);
			click(Common.searchitem,lngCtrlTimeOutInSeconds, "Search", "Button", strPageName, true);
			driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
			objectExists(Common.lifeCycleStatus, "isEnabled", lngMinTimeOutInSeconds, "Life Cycle Status", "Life Cycle Status", strPageName,true);    
			click(Common.lineRefNum,lngCtrlTimeOutInSeconds, "Reference Number", "Link", strPageName, true);
		}
		else
		{
			ALMFunctions.ThrowException("Verify the Indirect Lines Page",
					"Open List in Indirect Lines pages should be displayed",
					"Open List in Indirect Lines pages is not displayed", true);
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
	public void salesIndirectLinesForASP() {

		String strPageName="Indirect Lines";
		pages.Common updateButton=new pages.Common("Update");
		pages.Common addCriteria=new pages.Common("Add Criteria");
		pages.Common addButton=new pages.Common("Add");
		pages.Common searchTextArea=new pages.Common("Search");
		
		pages.Common lineID=new pages.Common("Line ID");
		
		String strLineIDText=dataTable.getData("Data", "ChargeBack Line ID");
		
		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
		if(objectExists(Common.open,"isDisplayed", lngCtrlTimeOutInSeconds, "Open", "List", strPageName, false)) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify the Indirect Lines Page",
					"Open dropdown in Indirect Lines pages should be displayed",
					"Open dropdown in Indirect Lines pages is displayed", true);
			driverUtil.waitUntilElementDisplayed(Common.open);
			mouseOverandClick(Common.open, lngCtrlTimeOutInSeconds, "Open", "List", strPageName);
			click(Common.close,lngCtrlTimeOutInSeconds, "Close", "List", strPageName, true);
			click(updateButton.button,lngCtrlTimeOutInSeconds, "Update", "Button", strPageName, true);
			click(addCriteria.button,lngCtrlTimeOutInSeconds, "Add Criteria", "Button", strPageName, true);
			
			sendkeys(searchTextArea.textbox, lngMinTimeOutInSeconds, "Line ID", "Search Section", strPageName,true);
			click(lineID.label,lngCtrlTimeOutInSeconds, "Line ID", "List", strPageName, true);
			click(addButton.button,lngCtrlTimeOutInSeconds, "Add", "Button", strPageName, true);
			
			sendkeys(lineID.textbox, lngMinTimeOutInSeconds, strLineIDText, "Line ID Section", strPageName,true);
			click(Common.searchitem,lngCtrlTimeOutInSeconds, "Search", "Button", strPageName, true);
			driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
			objectExists(Common.lifeCycleStatus, "isEnabled", lngMinTimeOutInSeconds, "Life Cycle Status", "Life Cycle Status", strPageName,true);    
			click(Common.lineRefNum,lngCtrlTimeOutInSeconds, "Reference Number", "Link", strPageName, true);
		}
		else
		{
			ALMFunctions.ThrowException("Verify the Indirect Lines Page",
					"Open List in Indirect Lines pages should be displayed",
					"Open List in Indirect Lines pages is not displayed", true);
		}
	}
	/**
	 * Method to Apply filter in the sales Direct Lines.
	 *
	 * @param locator, locator value of element
	 * @param strButtonName, fieldName of the button
	 * @param strElementState, state of the element
	 * @param strElementType, type of the element
	 * @param strPageName   - Page Name in which the control is available
	 * @return No return value
	 */
	public void salesDirectLines() {

		String strPageName="Direct Lines";
		pages.Common updateButton=new pages.Common("Update");
		pages.Common addCriteria=new pages.Common("Add Criteria");
		pages.Common addButton=new pages.Common("Add");
		pages.Common searchTextArea=new pages.Common("Search");
		pages.Common ExternalLineNumber=new pages.Common("External Line Number");
		
		String strExternalLineNumber=dataTable.getData("Data", "External Line Number");
		
		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
		if(objectExists(Common.directOpen,"isDisplayed", lngCtrlTimeOutInSeconds, "Open", "List", strPageName, false)) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify the Direct Lines Page",
					"Open dropdown in Direct Lines pages should be displayed",
					"Open dropdown in Direct Lines pages is displayed", true);
			driverUtil.waitUntilElementDisplayed(Common.open);
			mouseOverandClick(Common.directOpen, lngCtrlTimeOutInSeconds, "Open", "List", strPageName);
			click(Common.directClose,lngCtrlTimeOutInSeconds, "Close", "List", strPageName, true);
			click(updateButton.button,lngCtrlTimeOutInSeconds, "Update", "Button", strPageName, true);
			click(addCriteria.button,lngCtrlTimeOutInSeconds, "Add Criteria", "Button", strPageName, true);
			sendkeys(searchTextArea.textbox, lngMinTimeOutInSeconds, "External Line Number", "Search Section", strPageName,true);
			click(ExternalLineNumber.label,lngCtrlTimeOutInSeconds, "External Line Number", "List", strPageName, true);
			click(addButton.button,lngCtrlTimeOutInSeconds, "Add", "Button", strPageName, true);
			sendkeys(ExternalLineNumber.textbox, lngMinTimeOutInSeconds, strExternalLineNumber, "Contract ID Section", strPageName,true);
			click(Common.searchitem,lngCtrlTimeOutInSeconds, "Search", "Button", strPageName, true);
			driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
			objectExists(Common.lifeCycleStatus, "isEnabled", lngMinTimeOutInSeconds, "Life Cycle Status", "Life Cycle Status", strPageName,true);    
			click(Common.lineRefNum,lngCtrlTimeOutInSeconds, "Reference Number", "Link", strPageName, true);
			
		}
		else
		{
			ALMFunctions.ThrowException("Verify the Direct Lines Page",
					"Open List in Direct Lines pages should be displayed",
					"Open List in Direct Lines pages is not displayed", true);
		}
	}
	/**
	 * Method to Apply filter in the sales Direct Lines for NFAMP price type.
	 *
	 * @param locator, locator value of element
	 * @param strButtonName, fieldName of the button
	 * @param strElementState, state of the element
	 * @param strElementType, type of the element
	 * @param strPageName   - Page Name in which the control is available
	 * @return No return value
	 */
	public void salesDirectLinesForNFAMP() {

		String strPageName="Direct Lines";
		pages.Common updateButton=new pages.Common("Update");
		pages.Common addCriteria=new pages.Common("Add Criteria");
		pages.Common addButton=new pages.Common("Add");
		pages.Common searchTextArea=new pages.Common("Search");
		pages.Common ExternalLineNumber=new pages.Common("External Line Number");
		pages.Common fromInvoiceDate=new pages.Common("from Invoice Date");
		
		String strExternalLineNumber=dataTable.getData("Data", "External Line Number");
		
		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
		if(objectExists(Common.directOpen,"isDisplayed", lngCtrlTimeOutInSeconds, "Open", "List", strPageName, false)) {
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify the Direct Lines Page",
					"Open dropdown in Direct Lines pages should be displayed",
					"Open dropdown in Direct Lines pages is displayed", true);
			driverUtil.waitUntilElementDisplayed(Common.open);
			mouseOverandClick(Common.directOpen, lngCtrlTimeOutInSeconds, "Open", "List", strPageName);
			click(Common.directClose,lngCtrlTimeOutInSeconds, "Close", "List", strPageName, true);
			click(updateButton.button,lngCtrlTimeOutInSeconds, "Update", "Button", strPageName, true);
			click(addCriteria.button,lngCtrlTimeOutInSeconds, "Add Criteria", "Button", strPageName, true);
			sendkeys(searchTextArea.textbox, lngMinTimeOutInSeconds, "External Line Number", "Search Section", strPageName,true);
			click(ExternalLineNumber.label,lngCtrlTimeOutInSeconds, "External Line Number", "List", strPageName, true);
			click(addButton.button,lngCtrlTimeOutInSeconds, "Add", "Button", strPageName, true);
			sendkeys(ExternalLineNumber.textbox, lngMinTimeOutInSeconds, strExternalLineNumber, "Contract ID Section", strPageName,true);
			clear(fromInvoiceDate.textbox, lngMinTimeOutInSeconds, "Date Field", driver.getTitle());
			click(Common.searchitem,lngCtrlTimeOutInSeconds, "Search", "Button", strPageName, true);
			driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
			objectExists(Common.lifeCycleStatus, "isEnabled", lngMinTimeOutInSeconds, "Life Cycle Status", "Life Cycle Status", strPageName,true);    
			click(Common.lineRefNum,lngCtrlTimeOutInSeconds, "Reference Number", "Link", strPageName, true);
			
		}
		else
		{
			ALMFunctions.ThrowException("Verify the Direct Lines Page",
					"Open List in Direct Lines pages should be displayed",
					"Open List in Direct Lines pages is not displayed", true);
		}
	}

}
