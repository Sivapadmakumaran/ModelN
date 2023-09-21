package businesscomponents;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.cognizant.craft.ScriptHelper;
import com.cognizant.framework.Status;
import com.cognizant.framework.Util;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import org.w3c.dom.NamedNodeMap;

public class ReportingFlow extends CommonFunctions{

	public ReportingFlow(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}
	public final long lngMinTimeOutInSeconds = Long.parseLong(properties.getProperty("MinObjectSyncTimeout"));
	public final long lngCtrlTimeOutInSeconds = Long.parseLong(properties.getProperty("UploadControlTimeout"));
	public final long jobTimeOut = Long.parseLong(properties.getProperty("JobTimeOut"));
	public final long staleTableTimeOut = Long.parseLong(properties.getProperty("StaleTableTimeOut")); 

	/**
	 * Function to call Fill form Utility to customize the Report
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void customizeReport() {
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "customizeReport";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	

	/**
	 * Function to parse the Gilead Medicaid Disputes By NDC Matrix Excel Report
	 * @throws IOException
	 */
	public void parseMedicaidDisputesExcel() throws IOException {
		manageAndSwitchNewWindow();
		closeAndSwitchPreviousWindow();
		String strFileName = "Gilead Medicaid Disputes By NDC" + ".xlsx";
		String strFilePath = System.getProperty("user.dir")+"\\externalFiles\\"+strFileName;
		File inputFile = new File(strFilePath);
		boolean blnFileExist;

		try {
			blnFileExist = FileExists(strFilePath);
			if(blnFileExist)
			{
				int intPrimaryRowNo =	getRowNoFromXLSX(strFilePath, "Page1_1", 4, "NDC");
				int intPrimaryRowValNo = intPrimaryRowNo+1;
				int intPrimaryColumnRowIndex =	getColumnIndexFromXlSX(strFilePath,"Page1_1",intPrimaryRowNo,"NDC");
				int intOriginalQuarterColumnRowIndex =	getColumnIndexFromXlSX(strFilePath,"Page1_1",intPrimaryRowNo,"Original Quarter");
				int intProductNameColumnRowIndex =	getColumnIndexFromXlSX(strFilePath,"Page1_1",intPrimaryRowNo,"Product Name");
				int intCumulativePaidAmountColumnRowIndex =	getColumnIndexFromXlSX(strFilePath,"Page1_1",intPrimaryRowNo,"Cumulative Paid Amount");

				String strNDCVal = getColumnValueFromXlSX(strFilePath,"Page1_1", intPrimaryRowValNo, intPrimaryColumnRowIndex);
				String strOriginalQuarterVal = getColumnValueFromXlSX(strFilePath,"Page1_1", intPrimaryRowValNo, intOriginalQuarterColumnRowIndex);
				String strProductName = getColumnValueFromXlSX(strFilePath,"Page1_1", intPrimaryRowValNo, intProductNameColumnRowIndex);
				String strCumulativePaidAmountVal = getColumnValueFromXlSX(strFilePath,"Page1_1", intPrimaryRowValNo, intCumulativePaidAmountColumnRowIndex);
				dataTable.putData("Parametrized_Checkpoints", "NDC", strNDCVal);
				dataTable.putData("Parametrized_Checkpoints", "Original Quarter", strOriginalQuarterVal);
				dataTable.putData("Parametrized_Checkpoints", "Product Name", strProductName);
				dataTable.putData("Parametrized_Checkpoints", "Cumulative Paid Amount", strCumulativePaidAmountVal);
				report.updateTestLog("Retrieve Values from "+strFileName, "NDC, Product Name, Original Quarter,Cumulative Paid Amount values should be retrieved from "+strFileName,
						 "NDC: " +strNDCVal+", Product Name: "+strProductName +", Original Quarter: "+strOriginalQuarterVal+", Cumulative Paid Amount: "+strCumulativePaidAmountVal +" values are retrieved from Excel", Status.DONE);
				inputFile.delete();
			}else
			{
				ALMFunctions.ThrowException("Verify File Exist","Expected File " +strFileName+" should be available to edit in this path: "+strFilePath, 
						"Expected File " +strFileName+" is not available to edit in this path: "+strFilePath,true);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	
	/**
	 * Function to parse the Rebate Summary Report - Rebate Payment Excel Report
	 * @throws IOException
	 */
	public void parseRebateSummaryExcel() throws IOException {
		manageAndSwitchNewWindow();
		closeAndSwitchPreviousWindow();
		String strFileName = "Rebate Summary Report - Rebate Payment" + ".xlsx";
		String strFilePath = System.getProperty("user.dir")+"\\externalFiles\\"+strFileName;
		File inputFile = new File(strFilePath);
		boolean blnFileExist;

		try {
			blnFileExist = FileExists(strFilePath);
			if(blnFileExist)
			{
				
				int intCustomerIDRowNo =	getRowNoFromXLSX(strFilePath, "Page1_1", 2, "Customer ID :");
				int intRebatePaymentIDRowNo =	getRowNoFromXLSX(strFilePath, "Page1_1", 2, "Rebate Payment ID :");
				int intContractRowNo =	getRowNoFromXLSX(strFilePath, "Page1_1", 2, "Contract ID :");
				int intTotalPaymentAmtRowNo =	getRowNoFromXLSX(strFilePath, "Page1_1", 2, "Total Payment Amount :");
				
				String strContractID = getColumnValueFromXlSX(strFilePath,"Page1_1", intContractRowNo, 9);
				String[] strFullContractID = strContractID.split("-");
				strContractID = strFullContractID[0];
				String strCustomerID = getColumnValueFromXlSX(strFilePath,"Page1_1", intCustomerIDRowNo, 9);
				String strTotalPaymentAmt = getColumnValueFromXlSX(strFilePath,"Page1_1", intTotalPaymentAmtRowNo, 9);
				String strRebatePayID = getColumnValueFromXlSX(strFilePath,"Page1_1", intRebatePaymentIDRowNo, 9);
				
				
				dataTable.putData("Parametrized_Checkpoints", "Contract ID", strContractID);
				dataTable.putData("Parametrized_Checkpoints", "Customer ID", strCustomerID);
				dataTable.putData("Parametrized_Checkpoints", "Total Payment Amount", strTotalPaymentAmt);
				dataTable.putData("Parametrized_Checkpoints", "Rebate Payment ID", strRebatePayID);
				
				report.updateTestLog("Retrieve Values from "+strFileName, "Contract ID, Customer ID, Total Payment Amount,Rebate Payment ID values should be retrieved from "+strFileName,
						 "Contract ID: " +strContractID+", Customer ID: "+strCustomerID +", Rebate Payment ID: "+strTotalPaymentAmt+", Rebate Payment ID: "+strRebatePayID +" values are retrieved from Excel", Status.DONE);
				inputFile.delete();
			}else
			{
				ALMFunctions.ThrowException("Verify File Exist","Expected File " +strFileName+" should be available to edit in this path: "+strFilePath, 
						"Expected File " +strFileName+" is not available to edit in this path: "+strFilePath,true);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	
	/**
	 * Function to parse the Product Life Detail Excel Report
	 * @throws IOException
	 */
	public void parseProductLifeDetailExcel() throws IOException {
		manageAndSwitchNewWindow();
		closeAndSwitchPreviousWindow();
		String strFileName = "Product Life Detail" + ".xlsx";
		String strFilePath = System.getProperty("user.dir")+"\\externalFiles\\"+strFileName;
		File inputFile = new File(strFilePath);
		boolean blnFileExist;

		try {
			blnFileExist = FileExists(strFilePath);
			if(blnFileExist)
			{
				int intPrimaryValueRowNo =	getRowNoFromXLSX(strFilePath, "Page1_1", 2, "61958050101");
				int intPrimaryRowNo =	getRowNoFromXLSX(strFilePath, "Page1_1", 2, "Product #");
				
				int intProductNameColumnIndex =	getColumnIndexFromXlSX(strFilePath,"Page1_1",intPrimaryRowNo,"Product Name");
				int intMarketEntryDateColumnIndex =	getColumnIndexFromXlSX(strFilePath,"Page1_1",intPrimaryRowNo,"Market Entry Date");
				int intFDAApprovalDateColumnIndex =	getColumnIndexFromXlSX(strFilePath,"Page1_1",intPrimaryRowNo,"FDA Approval Date");
				int intFirstDateOfSaleColumnIndex =	getColumnIndexFromXlSX(strFilePath,"Page1_1",intPrimaryRowNo,"First Date of Sale");
				int intShelfLifeExpirationDateColumnIndex =	getColumnIndexFromXlSX(strFilePath,"Page1_1",intPrimaryRowNo,"Shelf Life Expiration Date");
				int intManuTermDateColumnIndex =	getColumnIndexFromXlSX(strFilePath,"Page1_1",intPrimaryRowNo,"Manufacturer Termination Date");
				
				String strProductName = getColumnValueFromXlSX(strFilePath,"Page1_1", intPrimaryValueRowNo,intProductNameColumnIndex);
				String strMarketEntryDate = getColumnValueFromXlSX(strFilePath,"Page1_1", intPrimaryValueRowNo, intMarketEntryDateColumnIndex);
				String strFDAApprovalDate = getColumnValueFromXlSX(strFilePath,"Page1_1", intPrimaryValueRowNo, intFDAApprovalDateColumnIndex);
				String strFirstDateOfSale = getColumnValueFromXlSX(strFilePath,"Page1_1", intPrimaryValueRowNo, intFirstDateOfSaleColumnIndex);
				String strShelfLifeExpirationDate = getColumnValueFromXlSX(strFilePath,"Page1_1", intPrimaryValueRowNo, intShelfLifeExpirationDateColumnIndex);
				String strtManuTermDate = getColumnValueFromXlSX(strFilePath,"Page1_1", intPrimaryValueRowNo, intManuTermDateColumnIndex);
				
				
				dataTable.putData("Parametrized_Checkpoints", "Product Name", strProductName);
				dataTable.putData("Parametrized_Checkpoints", "MarketEntryDate", strMarketEntryDate);
				dataTable.putData("Parametrized_Checkpoints", "FDAApprovalDate", strFDAApprovalDate);
				dataTable.putData("Parametrized_Checkpoints", "FirstDateOfSale", strFirstDateOfSale);
				dataTable.putData("Parametrized_Checkpoints", "ShelfLifeExpirationDate", strShelfLifeExpirationDate);
				dataTable.putData("Parametrized_Checkpoints", "ManuTermDate", strtManuTermDate);
				
				report.updateTestLog("Retrieve Values from "+strFileName, "Product Name, MarketEntryDate, FDAApprovalDate,FirstDateOfSale,ShelfLifeExpirationDate,ManuTermDate values should be retrieved from "+strFileName,
						 "Product Name: " +strProductName+", MarketEntryDate: "+strMarketEntryDate +", FDAApprovalDate: "+strFDAApprovalDate+", FirstDateOfSale: "+strFirstDateOfSale +", ShelfLifeExpirationDate: "+strShelfLifeExpirationDate +", ManuTermDate: "+strtManuTermDate +" values are retrieved from Excel", Status.DONE);
				inputFile.delete();
			}else
			{
				ALMFunctions.ThrowException("Verify File Exist","Expected File " +strFileName+" should be available to edit in this path: "+strFilePath, 
						"Expected File " +strFileName+" is not available to edit in this path: "+strFilePath,true);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	

	/**
	 * Function to parse the Reconciliation of State Invoice (ROSI) Excel Report
	 * @throws IOException
	 */
	public void parseROSIExcel() throws IOException {
		
		String strFileName = "Reconciliation of State Invoice (ROSI)" + ".xlsx";
		String strFilePath = System.getProperty("user.dir")+"\\externalFiles\\"+strFileName;
		manageAndSwitchNewWindow();
		closeAndSwitchPreviousWindow();
		File inputFile = new File(strFilePath);
		boolean blnFileExist;

		try {
			blnFileExist = FileExists(strFilePath);
			if(blnFileExist)
			{
				
				int intLabelerNameRowNo =	getRowNoFromXLSX(strFilePath, "Page1_1", 2, "LABELER NAME:");
				int intLabelerCodeRowNo =	getRowNoFromXLSX(strFilePath, "Page1_1", 2, "LABELER CODE:");
				int intPeriodCoveredRowNo =	getRowNoFromXLSX(strFilePath, "Page1_1", 2, "PERIOD COVERED:");
				
				String strLabelerName = getColumnValueFromXlSX(strFilePath,"Page1_1", intLabelerNameRowNo, 2);
				String strLabelerCode = getColumnValueFromXlSX(strFilePath,"Page1_1", intLabelerCodeRowNo, 2);
				String strPeriodCovered = getColumnValueFromXlSX(strFilePath,"Page1_1", intPeriodCoveredRowNo, 2);
				String strLabelerContact = getColumnValueFromXlSX(strFilePath,"Page1_1", 4, 9);
				String[] strLabelerCont = strLabelerContact.split(" ");
				strLabelerContact = strLabelerCont[1] +", "+strLabelerCont[0];
				String strPhone = getColumnValueFromXlSX(strFilePath,"Page1_1", 5, 9);
				String strEmail = getColumnValueFromXlSX(strFilePath,"Page1_1", 6, 9);
				String strState = getColumnValueFromXlSX(strFilePath,"Page1_1", 4, 18);
				String strDate = getColumnValueFromXlSX(strFilePath,"Page1_1", 6, 18);

				
				
				int intPrimaryRowNo =	getRowNoFromXLSX(strFilePath, "Page1_1", 2, "Code");
				
				int intPrimaryColumnIndex =	getColumnIndexFromXlSX(strFilePath,"Page1_1",intPrimaryRowNo,"Code");
				int intNameColumnIndex =	getColumnIndexFromXlSX(strFilePath,"Page1_1",intPrimaryRowNo,"Name");
				int intRecordIDColumnIndex =	getColumnIndexFromXlSX(strFilePath,"Page1_1",intPrimaryRowNo,"Record ID");
				int intInvoicedColumnIndex =	getColumnIndexFromXlSX(strFilePath,"Page1_1",intPrimaryRowNo,"INVOICED");
				
				
				String strProductCode = getColumnValueFromXlSX(strFilePath,"Page1_1", intPrimaryRowNo+1,intPrimaryColumnIndex);
				String[] strHalfProductCode = strProductCode.split("-");
				strProductCode = strHalfProductCode[1]+"-"+strHalfProductCode[2];
				String strProductName = getColumnValueFromXlSX(strFilePath,"Page1_1", intPrimaryRowNo+1, intNameColumnIndex);
				String strRecordID = getColumnValueFromXlSX(strFilePath,"Page1_1", intPrimaryRowNo+1, intRecordIDColumnIndex);
				String strInvoicedAmount = getColumnValueFromXlSX(strFilePath,"Page1_1", intPrimaryRowNo+1, intInvoicedColumnIndex);
				
				
				dataTable.putData("Parametrized_Checkpoints", "ProductCode", strProductCode);
				dataTable.putData("Parametrized_Checkpoints", "ProductName", strProductName);
				dataTable.putData("Parametrized_Checkpoints", "RecordID", strRecordID);
				dataTable.putData("Parametrized_Checkpoints", "InvoicedAmount", strInvoicedAmount);
				
				dataTable.putData("Parametrized_Checkpoints", "LabelerName", strLabelerName);
				dataTable.putData("Parametrized_Checkpoints", "LabelerCode", strLabelerCode);
				dataTable.putData("Parametrized_Checkpoints", "PeriodCovered", strPeriodCovered);
				dataTable.putData("Parametrized_Checkpoints", "LabelerContact", strLabelerContact);
				dataTable.putData("Parametrized_Checkpoints", "Phone", strPhone);
				dataTable.putData("Parametrized_Checkpoints", "Email", strEmail);
				dataTable.putData("Parametrized_Checkpoints", "State", strState);
				dataTable.putData("Parametrized_Checkpoints", "Date", strDate);
				
				report.updateTestLog("Retrieve Values from "+strFileName, "LabelerName, LabelerCode, PeriodCovered,LabelerContact,Phone,Email,State,Date values should be retrieved from "+strFileName,
						 "LabelerName: " +strLabelerName+", LabelerCode: "+strLabelerCode +", PeriodCovered: "+strPeriodCovered+", LabelerContact: "+strLabelerContact +", Phone: "+strPhone +", Email: "+strEmail+", State: "+strState +", Date: "+strDate +" values are retrieved from Excel", Status.DONE);
				inputFile.delete();
			}else
			{
				ALMFunctions.ThrowException("Verify File Exist","Expected File " +strFileName+" should be available to edit in this path: "+strFilePath, 
						"Expected File " +strFileName+" is not available to edit in this path: "+strFilePath,true);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	
}
