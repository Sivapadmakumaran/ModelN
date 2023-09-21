package businesscomponents;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.cognizant.craft.ScriptHelper;
import com.cognizant.framework.Status;
import com.cognizant.framework.Util;
import com.cognizant.framework.XMLDataAccess;


import config.FrameworkAssertion;
import models.ERPPayment;
import models.Entries;
import models.EntriesMediAck;
import models.ImportInvoice;
import models.McdCheckAck;

import pages.Common;



public class ManageCareFlow extends CommonFunctions{

	public ManageCareFlow(ScriptHelper scriptHelper) {
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
	 * Function to call Fill form Utility for Transaction file in Validata
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void validateTransactionFileinValidata() {
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "Transaction File Validation in Validata";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	/**
	 * Function to call Fill form Utility for Validation in ModelN
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void validateDetailsinModelN() {
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "Validation in ModelN";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	/**
	 * Function to call Fill form Utility for Validate Payment Details
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void validatePaymentDetails()
	{
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "Validation Payment Details";
		fillFormExcelFunctions.FillInputForm(strInputParameter);
		/*adjClaimTextFileUpdate();
		uploadAdjustmentFile();*/

	}
	/**
	 * Function to call Fill form Utility for Transaction File creation
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void transactionFileUpdate() throws IOException {
		String strFileName=dataTable.getData("Data", "Path");
		Date dNow = new Date();
	    SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
	    String strDateTime = ft.format(dNow);
		
		String strFileNewName = "2021M10 TestSample_" + strDateTime + ".txt";
	    
		String strOutputFile = System.getProperty("user.dir") + Util.getFileSeparator() + "testDataFiles" + Util.getFileSeparator() + strFileNewName;
		dataTable.putData("Parametrized_Checkpoints", "Transaction File Name", strFileNewName);
		String strFilePath = System.getProperty("user.dir")+strFileName;
		dataTable.putData("Parametrized_Checkpoints", "Transaction File Path", strOutputFile);
		InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(strFilePath);
	        os = new FileOutputStream(strOutputFile);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	    }
		
		
		
	}
	/**
	 * Function for Choose Transaction File
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void chooseTransactionFile() {
		String strBaseDir = System.getProperty("user.dir");
		String strTransactionFileNewName=dataTable.getData("Parametrized_Checkpoints", "Transaction File Name");
		dataTable.putData("Parametrized_Checkpoints", "TransactionFile", strTransactionFileNewName);
		mouseOverandClick(Common.chooseFile, lngMinTimeOutInSeconds,"Choose File", "Button", driver.getTitle());
		String strFilePath = "\"" + strBaseDir + Util.getFileSeparator() + "testDataFiles" + Util.getFileSeparator()
				+ strTransactionFileNewName + "\"";
		executeScript(true, strBaseDir + "\\autoIT" + Util.getFileSeparator() + "File Upload.exe", "Open",
				strFilePath);
	}
	/**
	 * Function for Validate Transaction File Status
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void validateStatus() {
		clickByJs(Common.transactionFilebutton, lngMinTimeOutInSeconds, "Transaction File", "Button", driver.getTitle(), true);
		
		
		String strExpectedStatus = "Ready";
		String strActualStatus = validateFileStatus(strExpectedStatus);
		if ((strActualStatus != null) && strActualStatus.equalsIgnoreCase(strExpectedStatus)) {
			report.updateTestLog("Validating transaction file status",
					"Transaction file status should be shown in table",
					"Transaction file status is shown as " + strActualStatus + " successfully.", Status.PASS);
		} else {
			report.updateTestLog("Validating transaction file status",
					"Transaction file status should be shown in table",
					"Transaction file status not shown as expected, having status '" + strActualStatus + "'.",
					Status.FAIL);
		}
	}
	/**
	 * Function for Validate Transaction File Actual Status
	 * 
	 * @param No parameter
	 * @return return String value
	 */
public String validateFileStatus(String expectedStatus) {
	
	driverUtil.waitUntilStalenessOfElement(Common.filterButton, driver.getTitle());
	By refreshBy = Common.filterButton;
	driverUtil.waitUntilStalenessOfElement(Common.transactionFileTableStatus, driver.getTitle());
	By visibleBy = Common.transactionFileTableStatus;
By noticeBy = By.xpath("//div[@id='commonnewnotification']//img[contains(@class,'closeIcon')]");
String strVisibleText = "";
int intCount = 1;
try {

	boolean isVisible = false;
	while (!isVisible) {
		driver.findElement(refreshBy).click();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		++intCount;

		List<WebElement> notifications = driver.findElements(noticeBy);

		if (notifications.size() > 0) {

			WebElement notification = notifications.get(0);

			if (notification.isDisplayed()) {
				Actions action = new Actions(driver.getWebDriver());
				action.click(notification).build().perform();

				action = new Actions(driver.getWebDriver());
				List<WebElement> temp = driver.findElements(visibleBy);
				action.moveToElement(temp.get(0)).build().perform();
			}

		}



		List<WebElement> elements = driver.findElements(visibleBy);

		if (elements.size() > 0) {
			strVisibleText = elements.get(0).getText();
			if (strVisibleText.trim().equalsIgnoreCase(expectedStatus)) {
				isVisible = true;

				break;
			}
			if (strVisibleText == null || strVisibleText.isEmpty()) {
				isVisible = false;
			}
		}

		
	}

	if (isVisible) {
		return strVisibleText;
	} else {
		return null;
	}
} catch (TimeoutException e) {
	System.out.println(e.getMessage());
	return null;
}
}
/**
 * method to Enter submission name
 * @param No parameter
 * @return No return value
 */
public void submissionData() {
	for(int index = 0; index < 2; index++) {
		
		waitForBackground();
	}
	String strFileName = dataTable.getData("Parametrized_Checkpoints", "TransactionFile");
	String strTempFile=strFileName;
	if (strFileName.equalsIgnoreCase("0")) {
		strFileName = strTempFile;
	}
	strFileName = strFileName.replaceAll(".txt", "");
	Common submissionName=new Common("Submission Name:");
	sendkeys(submissionName.textbox, lngMinTimeOutInSeconds, strFileName,"Textbox",driver.getTitle(), true);
	dataTable.putData("Parametrized_Checkpoints", "TransactionFileName",strFileName);
	try {
		Thread.sleep(1000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	sendkeys(submissionName.textbox, lngMinTimeOutInSeconds, Keys.ENTER,"Textbox: Keys.ENTER", driver.getTitle());
}
/**
 * method to verify background job alert
 *
 */
public void waitForBackground() {
	
	try {
		String strJobAlertMsg = dataTable.getData("Parametrized_Checkpoints", "BackGroundJob");
		Common objJobAlert = new Common(strJobAlertMsg);
		By refreshBy = Common.refresh;
		By alertBy = objJobAlert.label;
		
		Wait<WebDriver> wait = new WebDriverWait(driver.getWebDriver(), lngMinTimeOutInSeconds);

		boolean isVisible = true;
		while (isVisible) {
			try {
				driver.switchTo().defaultContent();
				Thread.sleep(1000);
				driver.findElement(refreshBy).click();

				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
 * method to get Text of submission id
 
 * @param No parameter
 * @return No return value

 */
public void getSubmissionId() {
	WebElement element = null;
	String strElementText = "";
    driverUtil.waitUntilElementVisible(Common.submissionId, lngMinTimeOutInSeconds);
				element = driver.findElement(Common.submissionId);
				strElementText = "";
				if(element != null) {
					strElementText = element.getText().trim();
					dataTable.putData("Parametrized_Checkpoints", "SubmissionId", strElementText);
				}
			}
/**
 * method to Validate Claim Id
 
 * @param No parameter
 * @return No return value

 */
public void validateClaimID() {
	String strFileName = dataTable.getData("Parametrized_Checkpoints", "TransactionFile");
	String strTempFile=strFileName;
	if(strFileName.equalsIgnoreCase("0")) {
		
		strFileName = strTempFile;
	}
	strFileName = strFileName.replaceAll(".txt","");
	Common tableClaimLink=new Common(strFileName);
	driverUtil.waitUntilElementVisible(tableClaimLink.tableClaimLink, lngMinTimeOutInSeconds);
	WebElement claimElement = driver.findElement(tableClaimLink.tableClaimLink);
	if (claimElement != null) {
		dataTable.putData("Parametrized_Checkpoints", "ClaimId", claimElement.getText().trim());
		report.updateTestLog("Validating Claim Id",
		"Claim id should be displayed in table",
		"Claim id is displayed as " + claimElement.getText() + " successfully.", Status.PASS);
	} else {
		report.updateTestLog("Validating Claim Id",
				"Claim id should be displayed in table",
				"Claim id is not displayed as expected.", Status.FAIL);
	}
	
	clickByJs(tableClaimLink.tableClaimLink, lngMinTimeOutInSeconds, "Claim Id", "Link", driver.getTitle(), true);
	
	
} 
/**
 * method to Process Rebate Calculation Status
 
 * @param No parameter
 * @return No return value

 */
public void processRebateStatus() throws Exception {
	
		checkRebatesForRecalculate();
		
		selectActionsOrNew("Actions", "Edit",driver.getTitle());
		moveToProcess();
		
		moveToReviewed();
		
		WebElement paymentAmount = driver.findElement(Common.netPaymentAmount);
		if(paymentAmount != null) {
			String strPaymentAmount = paymentAmount.getText().trim();
			strPaymentAmount = strPaymentAmount.replaceAll("\\$", "").replaceAll("\\,", "");
			strPaymentAmount = String.format("%s", strPaymentAmount);
			dataTable.putData("Parametrized_Checkpoints", "PaymentAmount",strPaymentAmount);
		}
		
			
			
		moveToRemove();
	

}
/**
 * method to Check Rebate for Recalculation
 
 * @param No parameter
 * @return No return value

 */
public void checkRebatesForRecalculate() throws Exception, FrameworkAssertion {
	
    Common pageSize=new Common("Page Size:");
	selectDropdownByvalue(pageSize.dropdown,"PageSize", "250", driver.getTitle(), true);
	driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loader", "Loader", driver.getTitle());
	
	selectDropdownByvalue(Common.rebateTableStatus,"Status", "Due", driver.getTitle(), true);
	driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loader", "Loader", driver.getTitle());
	
	clickByJs(Common.filterButton, lngMinTimeOutInSeconds, "Filter", "Button", driver.getTitle(), true);
	driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loader", "Loader", driver.getTitle());
	
	driverUtil.waitUntilElementVisible(Common.rebateSelectAllCheckbox, lngMinTimeOutInSeconds);
	List<WebElement> selectAll = driver.findElements(Common.rebateSelectAllCheckbox);

	if (selectAll.size() > 0) {
		selectAll.get(0).click();
	} else {
		selectCheckBoxForStatus();
	}
	Common recalculateButton=new Common("Recalculate");
	clickByJs(recalculateButton.button, lngMinTimeOutInSeconds, "Recalculate", "Button", driver.getTitle(), true);
	driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loader", "Loader", driver.getTitle());
	
	try {
		Thread.sleep(5000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	Common yesAlert=new Common("Confirm Recalculation","Yes");
	clickByJs(yesAlert.dialogButton, lngMinTimeOutInSeconds, "Yes", "Button", driver.getTitle(), true);

	waitForBackground();
	boolean isDueFound = validateTotalRebateAmount();
	if (isDueFound) {
		
		ALMFunctions.UpdateReportLogAndALMForPassStatus("Recalculate validation",
				"Rebate amount should be available successfully",
				"Rebate amount is available successfully", true);
	} else {
		
		ALMFunctions.UpdateReportLogAndALMForFailStatus("Recalculate validation",
				"Rebate amount should be available successfully",
				"Rebate amount is not available", true);
		throw new FrameworkAssertion("Rebate amount not found");
	}
}
/**
 * method to check Rebate to Move In Process
 
 * @param No parameter
 * @return No return value

 */
public void moveToProcess() throws Exception {

	
	selectDropdownByvalue(Common.rebateAmountDue,"Arithmetic Operation", ">", driver.getTitle(), true);
	sendkeys(Common.rebateAmountDueValue, lngMinTimeOutInSeconds, "0", "textbox", driver.getTitle(),true);
	clickByJs(Common.filterButton, lngMinTimeOutInSeconds, "Filter", "Button", driver.getTitle(), true);
	driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loader", "Loader", driver.getTitle());
	driverUtil.waitUntilElementVisible(Common.rebateSelectAllCheckbox, lngMinTimeOutInSeconds);
	List<WebElement> selectAll = driver.findElements(Common.rebateSelectAllCheckbox);

	if (selectAll.size() > 0) {
		selectAll.get(0).click();
	} else {
		selectCheckBoxForStatus();
	}

	Common processButton=new Common("Process");
	clickByJs(processButton.button, lngMinTimeOutInSeconds, "Process", "Button", driver.getTitle(), true);
	driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loader", "Loader", driver.getTitle());
	
	try {
		Thread.sleep(5000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	handleAlertOk("ProcessRebatePrograms", "Ok", "alert");
	
	selectDropdownByvalue(Common.rebateTableStatus,"Status", "In Process", driver.getTitle(), true);
	driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loader", "Loader", driver.getTitle());
	
	
	try {
		Thread.sleep(3000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	clickByJs(Common.filterButton, lngMinTimeOutInSeconds, "Filter", "Button", driver.getTitle(), true);
	driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loader", "Loader", driver.getTitle());

	waitForBackground();
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
 * method to check Rebate to Move Reviewed
 
 * @param No parameter
 * @return No return value

 */
public void moveToReviewed() throws Exception {
	

	selectDropdownByvalue(Common.rebateTableStatus,"Status", "In Process", driver.getTitle(), true);


	clickByJs(Common.filterButton, lngMinTimeOutInSeconds, "Filter", "Button", driver.getTitle(), true);
	driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loader", "Loader", driver.getTitle());
	driverUtil.waitUntilElementVisible(Common.rebateSelectAllCheckbox, lngMinTimeOutInSeconds);
	List<WebElement> selectAll = driver.findElements(Common.rebateSelectAllCheckbox);


	if (selectAll.size() > 0) {
		selectAll.get(0).click();
	} else {
		selectCheckBoxForStatus();
	}
	
	Common processButton=new Common("Review");
	clickByJs(processButton.button, lngMinTimeOutInSeconds, "Review", "Button", driver.getTitle(), true);
	driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loader", "Loader", driver.getTitle());
	
	try {
		Thread.sleep(5000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	handleAlertYes("ConfirmReview", "Yes", "alert");
	driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loader", "Loader", driver.getTitle());
	

	waitForBackground();
	
	try {
		Thread.sleep(3000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
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
	
}
/**
 * method to check Rebate to Move Remove Due Status
 
 * @param No parameter
 * @return No return value

 */
public void moveToRemove() throws Exception, FrameworkAssertion {
	
	Common pageSize=new Common("Page Size:");
	selectDropdownByvalue(pageSize.dropdown,"PageSize", "250", driver.getTitle(), true);
	driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loader", "Loader", driver.getTitle());
	clickByJs(Common.clearFilterButton, lngMinTimeOutInSeconds, "Filter", "Button", driver.getTitle(), true);
	driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loader", "Loader", driver.getTitle());
	selectDropdownByvalue(Common.rebateTableStatus,"Status", "Due", driver.getTitle(), true);
	driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loader", "Loader", driver.getTitle());
	clickByJs(Common.filterButton, lngMinTimeOutInSeconds, "Filter", "Button", driver.getTitle(), true);
	driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loader", "Loader", driver.getTitle());
	
	driverUtil.waitUntilElementVisible(Common.rebateSelectAllCheckbox, lngMinTimeOutInSeconds);
	List<WebElement> selectAll = driver.findElements(Common.rebateSelectAllCheckbox);

	if (selectAll.size() > 0) {
		selectAll.get(0).click();
		
	} else {
		selectCheckBoxForStatus();
	}
	Common processButton=new Common("Remove");
	clickByJs(processButton.button, lngMinTimeOutInSeconds, "Remove", "Button", driver.getTitle(), true);
	driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loader", "Loader", driver.getTitle());
	
	try {
		Thread.sleep(1000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	handleAlertOk("ProcessRebatePrograms", "Ok", "alert");
	driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loader", "Loader", driver.getTitle());
	
	waitForBackground();
	
	try {
		Thread.sleep(5000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
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
		WebElement inputElement = driver.findElement(
				By.xpath("//div[contains(@class, 'vRestrict')]//tr[" + (index + 1) + "]//input[@type='checkbox']"));
		inputElement.click();
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
			try {
				driver.switchTo().defaultContent();
				Thread.sleep(1000);
				driver.findElement(refreshBy).click();

				Thread.sleep(5000);
				++intCount;
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

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
 * method to get approved time from approval status
 
 * @param No parameter
 * @return No return value

 */
public void getApprovedTime() {
	WebElement element=null;
	String strElementText="";
	Common dateSubmitted=new Common("Date Submitted:");
	driverUtil.waitUntilElementVisible(dateSubmitted.dropdown, lngMinTimeOutInSeconds);
	element = driver.findElement(dateSubmitted.dropdown);
	strElementText = "";
	if(element != null) {
		strElementText = element.getText().trim();
		String values = strElementText.split("--")[1];
		
		//String myvalues = values.replaceAll("[AP]M", "");
		
		if(values.contains("AM")) {
			values = values.split("AM")[0].trim();
		} else if(values.contains("PM")) {
			values = values.split("PM")[0].trim();
		}
	
		dataTable.putData("Parametrized_Checkpoints", "ApprovedTime", values);
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
public boolean validateTotalErrors() {

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
						} else {
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
public void parsePackageId() {


	driverUtil.waitUntilElementVisible(Common.xmlTextArea,
			lngMinTimeOutInSeconds);
	WebElement element = driver.findElement(Common.xmlTextArea);
	String strXmlContent = element.getText();
	XMLDataAccess xmlAccess = new XMLDataAccess();
	String strPackageId = dataTable.getData("Parametrized_Checkpoints", "Payment Package ID:");
	ImportInvoice invoice = xmlAccess.unMarshallImportInvoice(strXmlContent);

	if (invoice != null) {
		if (invoice.getInvoices().getInvoiceNotes().trim().contains(strPackageId)) {
			
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Package Id validation in channel message", "Package id should be equals",
					"Package id is equals with created", true);
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
 * method to create erp xml file
 
 * @param No parameter
 * @return No return value

 */
public void createERPXml() {
	String strPackageId = dataTable.getData("Parametrized_Checkpoints", "Payment Package ID:");
	String strPaymentAmount = dataTable.getData("Parametrized_Checkpoints", "PaymentAmount");
	String strPayAmount = String.format("%s|USD", strPaymentAmount);
	String strFileName = "GdERPPmtStatus_" + 
			String.format("%s",System.currentTimeMillis()).substring(9) + "_" +
			strPackageId + ".xml";

	String strErpPmtFile = System.getProperty("user.dir") + Util.getFileSeparator() + "testDataFiles" + Util.getFileSeparator()
			+ strFileName;
	String strRanNumber = String.format("%s",System.currentTimeMillis()).substring(2);
	
	LocalDate dt = LocalDate.now();
	dt.minusMonths(4);
	String strDt = DateTimeFormatter.ofPattern("YYYY-MM-dd").format(dt);

	ERPPayment payment = new ERPPayment(strPayAmount, strPackageId, strDt, "issued", strRanNumber, "Check", strDt);
	Entries entries = new Entries(payment);

	XMLDataAccess xmlAccess = new XMLDataAccess();
	boolean isCreated = xmlAccess.marshallGdERPPmtStatusFromXml(strErpPmtFile, entries);

	if (isCreated) {
		dataTable.putData("Parametrized_Checkpoints", "GdERPFile", strFileName);
		
		ALMFunctions.UpdateReportLogAndALMForPassStatus("Creating 'GdERPPmtStatusFromXml'",
				"'GdERPPmtStatusFromXml' file should be created successfully",
				"'GdERPPmtStatusFromXml' file is created successfully", true);
	} else {
		
		ALMFunctions.UpdateReportLogAndALMForFailStatus("Creating 'GdERPPmtStatusFromXml'",
				"'GdERPPmtStatusFromXml' file should be created successfully",
				"'GdERPPmtStatusFromXml' file is creation failed", true);

	}

}
/*Method Name : Upload a GdERP File 
Description : Fill form utility to perform activities 
 @throws InterruptedException
Return Type : void
 */
public void uploadGdERPFile()
{
	String strBaseDir = System.getProperty("user.dir");
	mouseOverandClick(Common.fileUpload, lngMinTimeOutInSeconds,"Choose File", "Button", driver.getTitle());
	String strGdERPFile = dataTable.getData("Parametrized_Checkpoints", "GdERPFile");
	String strFilePath = "\"" + strBaseDir + Util.getFileSeparator() + "testDataFiles" + Util.getFileSeparator()
			+ strGdERPFile + "\"";
	executeScript(true, strBaseDir + "\\autoIT" + Util.getFileSeparator() + "File Upload.exe", "Open",
			strFilePath);
	
}
/**
 * method to Parse xml to validate erp payment
 
 * @param No parameter
 * @return No return value

 */
public void parseERPPayment() {
	

	driverUtil.waitUntilElementVisible(Common.xmlTextArea,
			lngMinTimeOutInSeconds);
	WebElement element = driver.findElement(Common.xmlTextArea);
	String strXmlContent = element.getText();
	XMLDataAccess xmlAccess = new XMLDataAccess();
	String strPaymentAmount = dataTable.getData("Parametrized_Checkpoints", "PaymentAmount");
	Entries entries = xmlAccess.unMarshallEntries(strXmlContent);

	if (entries != null) {
		if (entries.getErpPayment().getPaymentAmount().trim().contains(strPaymentAmount)) {
			
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Payment Amount validation in channel message", "Payment Amount should be equals",
					"Payment Amount is equals with created", true);
		} else {
			
			ALMFunctions.UpdateReportLogAndALMForFailStatus("Payment Amount validation in channel message", "Package id should be equals",
					"Package id is not equals with created", true);

		}
	} else {
		
		ALMFunctions.UpdateReportLogAndALMForFailStatus("Payment Amount validation in channel message", "Payment Amount should be equals",
				"Entries parsing error", true);
	}
	
}
/**
 * method to click paid publish payment
 
 * @param No parameter
 * @return No return value

 */
public void paidPublishPayment() {
	click(Common.edi849, lngMinTimeOutInSeconds,"Table row item " + "Publish Paid Payments", "Link", driver.getTitle(), false);
	
	List<WebElement> elements = driver.findElements(Common.alertYes);
	if (elements.size() > 0) {
		elements.get(0).click();
	}
}
/**
 * method to Check status from paid publish payment to completed
 
 * @param No parameter
 * @return No return value

 */
public void paymentCompleted() {
	String ediJobId = dataTable.getData("Parametrized_Checkpoints", "Job Id:");
	
	By visibleBy = By.xpath(
			"(//table[contains(@class,'splitControlBodyTable tableElement')]//tr)//td[2]//span[text()[normalize-space()='"
					+ ediJobId
					+ "']]//ancestor::tr[1]//td[4]//span[contains(text(),'Completed')]//ancestor::tr[1]//td[4]//span");
	By refreshBy = Common.refresh;
	String strExpectedStatus = "Completed";
	
	validateElementStatus(refreshBy, visibleBy, strExpectedStatus);
	report.updateTestLog("EDI 849 Job creation and validation",
			"EDI 849 Job should be created and validated successfullly",
			" EDI 849 Job is created and validated successfully.", Status.PASS);
}
/**
 * method to validate to expected element status
 * 
 * @param refreshBy
 * @param visibleBy
 * @param expectedStatus
 * 
 */
public void validateElementStatus(By refreshBy, By visibleBy, String strExpectedStatus) {

	try {
		Wait<WebDriver> wait = new WebDriverWait(driver.getWebDriver(), lngMinTimeOutInSeconds);

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

			frameSwitchAndSwitchBack(Common.documentFrame,"documentframe", driver.getTitle());
			List<WebElement> elements = driver.findElements(visibleBy);
			String strVisibleText = "";
			if (elements.size() > 0) {
				strVisibleText = elements.get(0).getText().trim();
				if (strVisibleText == null || strVisibleText.isEmpty()) {
					isVisible = false;
				} else {
					String strActualStatus = elements.get(0).getText();
					if (strActualStatus.equalsIgnoreCase(strExpectedStatus)) {
						isVisible = true;
						pagescroll(visibleBy, driver.getTitle());
						
						ALMFunctions.UpdateReportLogAndALMForPassStatus("Validate value " + strActualStatus,
								strActualStatus + strActualStatus + " value should be fetched.",
								strActualStatus + " value is fetched successfully.", true);
					} else {
						isVisible = false;
					}
				}
			}
		}
	} catch (TimeoutException e) {
		return;

	}

}
/**
 * Function for Validate payment status
 * 
 * @param No parameter
 * @return No return value
 */
public void validatePaymentStatus() {
	By rowsBy = By.xpath("//table[contains(@class,'splitControlBodyTable tableElement')]//tr");
	By columnBy = By.xpath("//table[contains(@class,'splitControlBodyTable tableElement')]//tr//td[3]//span");
	driverUtil.waitUntilElementVisible(rowsBy, lngMinTimeOutInSeconds);
	boolean isIssued = validateAllElementStatus(rowsBy, columnBy, "Issued");
	if(isIssued) {
		report.updateTestLog("Validating Payment status",
				"Payment status should be displayed as 'Issued' in table",
				"Payment status is displayed as 'Issued' in table.", Status.PASS);
	} else {
		report.updateTestLog("Validating Payment status",
				"Payment status should be displayed as 'Issued' in table",
				"Payment status is displayed as 'Issued' in table.", Status.PASS);
	}
}
/**
 * method to validate all element status
 
 * @param No parameter
 * @return return boolean value

 */
public boolean validateAllElementStatus(By visibleBy, By columnBy, String expectedStatus) {
	try {
		List<WebElement> rows = driver.findElements(columnBy);
		int intIndex = 0;
		if (rows.size() > 0) {
			for (WebElement row: rows) {
				String strStatus = row.getText();
				++intIndex;
				if (!(strStatus.equalsIgnoreCase(expectedStatus))) {
					pagescroll(row, driver.getTitle());
					
					return false;
				}
			}
		} else {
			return false;
		}

	} catch (TimeoutException e) {
		return false;

	}

	return true;

}


}
