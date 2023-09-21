package businesscomponents;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.nio.channels.FileLock;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;

import org.openqa.selenium.Keys;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.cognizant.framework.FileLockMechanism;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Settings;
import com.cognizant.framework.Status;
import com.cognizant.framework.Util;
import com.cognizant.framework.XMLDataAccess;

import pages.Common;
import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.selenium.ClassicRunner;
import com.applitools.eyes.selenium.Eyes;
import com.cognizant.craft.ScriptHelper;

public class CommonFunctions extends CommonActionsAndFunctions {
	public CommonFunctions(ScriptHelper scriptHelper) {
		super(scriptHelper);
	}

	public final long lngMinTimeOutInSeconds = Long.parseLong(properties.getProperty("MinObjectSyncTimeout"));
	public final long lngCtrlTimeOutInSeconds = Long.parseLong(properties.getProperty("UploadControlTimeout"));
	public final long jobTimeOut = Long.parseLong(properties.getProperty("JobTimeOut"));
	public final long staleTableTimeOut = Long.parseLong(properties.getProperty("StaleTableTimeOut"));
	public final long staleTimeOut = Long.parseLong(properties.getProperty("StaleTimeOut"));

	//private static HashMap<String, String> windowName = new HashMap<String, String>() ;
	static final HashMap<String, String> windowName = new HashMap<String, String>();




	/**
	 * Function to invoke ModelN Application 
	 * @param No parameters
	 * @return No return value
	 */
	public void invokeApplication() {

		String strAppUrl = dataTable.getData("General_Data", "Application_URL");
		driver.get(strAppUrl);
		List<String> windowToRemove = new ArrayList<String>();
		for (String strWindowName : windowName.values()) {
			windowToRemove.add(strWindowName);
		}
		windowName.values().removeAll(windowToRemove);
		windowName.put("Window1", driver.getWindowHandle());
		//windowName.put("Window1", driver.getWindowHandle());
		manageAndSwitchNewWindow();
		if(objectExists(Common.userName, "isDisplayed", lngMinTimeOutInSeconds, "User Name","Element Existence",driver.getTitle(), false)){

			ALMFunctions.UpdateReportLogAndALMForPassStatus("Home page validation" ,"Home page should be opened." ,"Home page is opened successfully.", true);

		}
		else {

			ALMFunctions.ThrowException("Home page navigation" ,"Home page should be opened." ,"Home page is not opened successfully.", true);

		}

	}





	/*
	 * Declaration of Variable
	 */

	// private final SeleniumTestParameters testParameters;

	/**
	 * Method to fill input form in page
	 * 
	 * @param strParameters - Parameters for the form
	 * @param strPageName   - Name of the page in which form exists
	 */
	@SuppressWarnings("unused")
	public void FillInputForm(String strParameter)
	{
		String strInputParameters = getConcatenatedStringFromExcel("FillForm", "Input_Parameters",
				"Concatenate_Flag_Input", strParameter, "~", true, false);
		String[] arrParameters_Vs_Value = strInputParameters.split("~");
		for (int index = 0; index < arrParameters_Vs_Value.length; index++) {
			strInputParameters = getConcatenatedStringFromExcel("FillForm", "Input_Parameters",
					"Concatenate_Flag_Input", strParameter, "~", true, false);
			arrParameters_Vs_Value = strInputParameters.split("~");
			if (arrParameters_Vs_Value[index].trim().length() > 0) {
				String[] arrParameters = StringUtils.split(arrParameters_Vs_Value[index], ";");

				String strElementType = "";
				String strSection = "";
				String strElementName = "";
				String strValues = "";
				String strWindowName = "";
				String strPageName = "";
				String strStorageValue = "";
				for (int i = 0; i < arrParameters.length; i++) {
					switch (StringUtils.substringBefore(arrParameters[i], "=").toLowerCase()) {
					case "element type":
						strElementType = StringUtils.substringAfter(arrParameters[i], "=");
						break;
					case "element label":
						strElementName = StringUtils.substringAfter(arrParameters[i], "=");
						break;
					case "section name":
						strSection = StringUtils.substringAfter(arrParameters[i], "=");
						break;
					case "element value":
						strValues = StringUtils.substringAfter(arrParameters[i], "=");
						break;
					case "window name":
						strWindowName = StringUtils.substringAfter(arrParameters[i], "=");
						break;
					case "page name":
						strPageName = StringUtils.substringAfter(arrParameters[i], "=");
						break;
					case "storage":
						strStorageValue = StringUtils.substringAfter(arrParameters[i], "=");
						break;

					default:
						ALMFunctions.ThrowException("Test Data",
								"Only Pre-Defined Form Options must be provided in the test data sheet",
								"Error - Unhandled Form Options " + StringUtils.substringBefore(arrParameters[i], "="),
								false);
					}

				}

				if (strValues.trim().length() > 0) {
					switch (strElementType.toLowerCase()) {

					case "verifyobjectstate":
						verifyObjectState(strElementName, strValues, strPageName);
						break;
					case "navigatetomenu":
						selectNavMenu(strElementName, strValues, strPageName);
						break;
					case "navigatetoactionsornew":
						selectActionsOrNew(strElementName, strValues, strPageName);
						break;
					case "navigatetoreportformat":
						selectReportFormat(strElementName, strValues, strPageName);
						break;
					case "findrecordandverify":
						findRecordAndVerify(strElementName, strValues, strPageName);
						break;	
					case "findrecordandverifyndc":
						findRecordAndVerifyNDC(strElementName, strValues, strPageName);
						break;
					case "findrecordandverifyinfilterplaintable":
						findRecordAndVerifyinFilterPlainTable(strElementName, strValues, strPageName);
						break;	
					case "findrecordandverifyinfilterplaintablefirstcolumn":
						findRecordAndVerifyinFilterPlainTableFirstColumn(strElementName, strValues, strPageName);
						break;
						//findRecordAndVerifyinFilterTable
					case "findrecordandverifyinsplittable":
						findRecordAndVerifyInSplitTable(strElementName, strValues, strPageName);
						break;
					case "retrieverecordfromsplittable":
						retrieveRecordFromSplitTable(strElementName, strValues, strPageName);
						break;
					case "retrieverecordfromfiltertable":
						retrieveRecordFromFilterTable(strElementName, strValues, strPageName);
						break;
					case "findrecordandenterintable":
						findRecordAndEnterValueInTable(strElementName, strValues, strPageName);
						break;	
					case "entervalueinsplittable":
						enterValueInSplitTable(strElementName, strValues, strPageName);
						break;
					case "entervalueindialogsplittable":
						enterValueInDialogSplitTable(strElementName, strValues, strPageName);
						break;
					case "entervalueinsplittablewithmultifilter":
						enterValueInSplitTableWithMultipleFilter(strElementName, strValues, strPageName);
						break;

					case "entefilterinsplittable":
						enterFilterInSplitTable(strElementName, strValues, strPageName);
						break;
					case "entefilterintable":
						enterFilterInTable(strElementName, strValues, strPageName);
						break;
					case "selectfirstrowinatableifexist":
                        selectFirstRowinaTableIfExist(strElementName, strValues, strPageName);
                        break;
					case "ifalertexist":
						ifAlertExist(strElementName, strValues, strPageName);
						break;
					case "uploadfile":
						uploadFile(strValues, strPageName);
						break;

					case "footerbutton":
						clickFooterButton(strValues, strPageName);
						break;
					case "parsexmlandvalidate":
						parseXmlAndValidate(strValues, strPageName);
						break;
					case "reschedule":
						reschedule(strValues);
						break;
					case "projectname":
						projectName(strValues);
						break;	
					case "pagerefresh":
						refresh(strValues);
						break;
					case "entertextarea":
						enterTextArea(strElementName, strValues, strPageName);
						break;
					case "clicktableheadericon":
						clickTableHeaderIcon(strValues, strPageName);
						break;
					case "editiconintable":
						clickEditIconinTable(strValues, strPageName);
						break;
					case "imgbutton":
						clickImgButton(strValues, strPageName);
						break;
					case "searchtextboxintable":
						searchTextboxinTable(strValues, strPageName);
						break;
					case "doubleclicklinkinreporttable":
						doubleClickLinkinReportTable(strElementName, strValues, strPageName);
						break;
					case "gettextandverify":
						getTextAndVerify(strElementName, strValues, strPageName);
						break;

					case "getvaluefromapp":
						getValueFromApplication(strElementName, strValues, strPageName);
						break;
					case "navigatetorightmenu":
						selectRightMenu(strElementName, strValues, strPageName);
						break;
					case "navigatetomainmenuinvalidata":
						selectMainMenuinValidata(strElementName, strValues, strPageName);
						break;
					case "selectmultipledropdown":
						selectMultipleDropDown(strElementName, strValues, strPageName);
						break;
					case "selectfirstrowinatable":
						selectFirstRowinaTable(strElementName, strValues, strPageName);
						break;
					case "radiobutton":
						selectRadioButton(strElementName, strValues, strPageName);
						break;
					case "radiobuttonintable":
						selectRadioButtoninTable(strElementName, strValues, strPageName);
						break; 
					case "checkboxintransactionfile":
						selectCheckinTransactionFile(strValues, strPageName);
						break;
					case "link":
						clickLink(strElementName,strValues, strPageName);
						break;
					case "label":
						clickLabel(strValues, strPageName);
						break;	
					case "doubleclicklink":
						doubleClickLink(strValues, strPageName);
						break;
					case "clicklinkintable":
						clickLinkinTable(strElementName,strValues, strPageName);
						break;
					case "button":
						if(strWindowName.length()>0)
						{
							clickDialogButton(strWindowName,strValues, strPageName);
						}
						else 
						{
							clickButton(strValues, strPageName);
						}

						break;
					case "tab":
						clickTab(strValues, strPageName);
						break;
					case "manageandswitchnewwindow":
						manageAndSwitchNewWindow();
						break;
					case "closeandswitchpreviouswindow":
						closeAndSwitchPreviousWindow();
						break;
					case "switchpreviouswindow":
						switchPreviousWindow();
						break;
					case "lookupbtn":
						clickLookUpButton(strValues, strPageName);
						break;
					case "multipletextbox":
						enterMultipleTextbox(strElementName,strValues, strPageName);
						break;
					case "cleartextbox":
						clearTextbox(strElementName,strValues, strPageName);
						break;

					case "textbox":
						if(strWindowName.length()>0)
						{
							enterDialogTextbox(strWindowName,strElementName, strValues, strPageName);
						}
						else 
						{
							enterTextbox(strElementName, strValues, strPageName);
						}
						break;
					case "select":
						if(strWindowName.length()>0)
						{
							selectDialogDropDown(strWindowName,strElementName, strValues, strPageName);
						}
						else 
						{
							selectDropDown(strElementName, strValues, strPageName);
						}
						break;


					case "checkbox":
						selectCheckBox(strElementName, strValues, strPageName);
						break;

					case "deletefile":
						deleteFile(strValues, strPageName);
						break;
					case "frameswitchandswitchback":
						if(strValues.equalsIgnoreCase("documentframe") || strValues.equalsIgnoreCase("default"))
						{
							frameSwitchAndSwitchBack(Common.documentFrame,strValues, strPageName);
						}
						else if(strValues.equalsIgnoreCase("dynframe"))
						{
							frameSwitchAndSwitchBack(Common.dynFrame,strValues, strPageName);
						}
						else if(strValues.equalsIgnoreCase("externalframe"))
						{
							frameSwitchAndSwitchBack(Common.externalFrame,strValues, strPageName);
						}

						break;
					case "tablecheckbox":
						clickTableCheckBox(strElementName, strValues, strPageName);
						break;

					default:

						ALMFunctions.ThrowException("Test Data",
								"Only Pre-Defined Fields Type must be provided in the test data sheet",
								"Error - Unhandled Field Type " + strElementType, false);
					}
					if (strStorageValue.trim().length() > 0) {
						dataTable.putData("Parametrized_Checkpoints", StringUtils.substringAfter(strStorageValue, "!"), strValues);
					}	

				}

			}
		}
	}


	/**
	 * Method to Navigate to menu and submenu
	 * 
	 * @param strMenuName- name of the menu to be clicked
	 * @param strMenuValues - name of the submenu to be clicked
	 * @param strPageName   - Page Name in which the control is available
	 * @return No return value
	 */

	public void selectNavMenu(String strMenuName, String strMenuValues,String strPageName)
	{


		By navMenuBar = new Common(strMenuName).navMenuBar;
		click(navMenuBar, lngMinTimeOutInSeconds,strMenuName, "Navigation Button", strPageName,true);



		for (String strItem : strMenuValues.split("!")) {
			By navMenuBarValues = new Common(strItem).navmenuItem;
			pagescroll(navMenuBarValues,strPageName);
			clickByJs(navMenuBarValues, lngMinTimeOutInSeconds,strItem, "Menu Value", "HomePage",true);
		}

	}

	/**
	 * Method to Navigate to menu and submenu in report format Selection
	 * 
	 * @param strMenuName- name of the menu to be clicked
	 * @param strMenuValues - name of the submenu to be clicked
	 * @param strPageName   - Page Name in which the control is available
	 * @return No return value
	 */

	public void selectReportFormat(String strMenuName, String strMenuValues,String strPageName)
	{

		By toolBtn = new Common(strMenuName).tableMenu;
		click(toolBtn, lngMinTimeOutInSeconds,strMenuName, "Table Menu Button", strPageName,true);

		for (String strItem : strMenuValues.split("!")) {
			By navMenuBarValues = new Common(strItem).tableMenuItem;
			//pagescroll(navMenuBarValues,strPageName);
			click(navMenuBarValues, lngMinTimeOutInSeconds,strItem, "Table Menu Value", "HomePage",true);
		}

	}

	/**
	 * Method to Navigate to menu and submenu in Actions or New buttons
	 * 
	 * @param strMenuName- name of the menu to be clicked
	 * @param strMenuValues - name of the submenu to be clicked
	 * @param strPageName   - Page Name in which the control is available
	 * @return No return value
	 */

	public void selectActionsOrNew(String strMenuName, String strMenuValues,String strPageName)
	{

		By toolBtn = new Common(strMenuName).toolMenuBtn;
		click(toolBtn, lngMinTimeOutInSeconds,strMenuName, "Menu Tool Button", strPageName,true);

		for (String strItem : strMenuValues.split("!")) {
			By navMenuBarValues = new Common(strItem).navmenuItem;
			pagescroll(navMenuBarValues,strPageName);
			clickByJs(navMenuBarValues, lngMinTimeOutInSeconds,strItem, "Menu Value", "HomePage",true);
		}

	}



	/**
	 * Method to verify the state of an object
	 * 
	 * @param strFieldName- name of an object
	 * @param strValueState - Object type and object state should be provided with
	 *                      '!' in data sheet
	 * @param strPageName   - Page Name in which the control is available
	 * @return No return value
	 */
	public void verifyObjectState(String strFieldName, String strValueState, String strPageName) {
		By locator = null;

		String strElementType = "";
		String strElementState = "";
		driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, strPageName);
		String[] strObjectStateValue = strValueState.split("!");
		strElementType = strObjectStateValue[0];
		strElementState = strObjectStateValue[1];

		switch (strElementType.toLowerCase()) {
		case "textbox":
			locator = new Common(strFieldName).textbox;
			break;

		case "link":
			locator = new Common(strFieldName).link;
			break;

		case "label":
			locator = new Common(strFieldName).label;
			break;

		case "radiobutton":
			locator = new Common(strFieldName).radioButton;
			break;

		case "checkbox":
			locator = new Common(strFieldName).checkbox;
			break;





		default:
			ALMFunctions.ThrowException("Verify Object State", "Only pre-defined control must be provided",
					"Unhandled control " + strElementType, false);
			break;
		}

		switch (strElementState.toLowerCase()) {


		case "notexist":
			strElementState = "not exist";
			verifyObjectExistance(locator, strFieldName, strElementState, strElementType, strPageName);
			break;
		case "exist":
			strElementState = "exist";
			verifyObjectExistance(locator, strFieldName, strElementState, strElementType, strPageName);
			break;
		case "mandatory":
			strElementState = "mandatory";
			verifyObjectExistance(locator, strFieldName, strElementState, strElementType, strPageName);
			break;
		case "nonmandatory":
			strElementState = "non mandatory";
			verifyObjectExistance(locator, strFieldName, strElementState, strElementType, strPageName);
			break;
		case "radiobtnchecked":
			strElementState = "checked";
			verifyradioButtonStatus(locator, strFieldName, strElementState, strElementType, strPageName);
			break;
		case "checkboxchecked":
			strElementState = "checked";
			verifyCheckboxStatus(locator, strFieldName, strElementState, strElementType, strPageName);
			break;

		default:
			ALMFunctions.ThrowException("Verify Object State", "Only pre-defined control must be provided",
					"Unhandled control " + strElementState, false);
			break;
		}
	}

	/**
	 * Method to verify the existence of an object. This method is part of
	 * verifyobjectstate method
	 * 
	 * @param locator, locator value of element
	 * @param strButtonName, fieldName of the button
	 * @param strElementState, state of the element
	 * @param strElementType, type of the element
	 * @param strPageName   - Page Name in which the control is available
	 * @return No return value
	 */

	public void verifyObjectExistance(By locator, String strButtonName, String strElementState, String strElementType,
			String strPageName) {

		switch (strElementState) {
		case "exist":
			if (objectExists(locator, "isDisplayed", lngMinTimeOutInSeconds, strButtonName, "Element Existence",strPageName, false)) {
				pagescroll(locator, strPageName);
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify the Label is displayed",
						"'" +strButtonName + "' " + strElementType + " should be displayed in the " + strPageName,
						"'" +strButtonName + "' " + strElementType + " is displayed in the " + strPageName, true);

			} else {
				ALMFunctions.UpdateReportLogAndALMForFailStatus("Verify the Label is displayed",
						"'" +strButtonName + "' " + strElementType + " should be displayed in the " + strPageName,
						"'" +strButtonName + "' " + strElementType + " is not displayed in the " + strPageName, true);

			}

			break;

		case "not exist":
			if (objectExists(locator, "isDisplayed", lngMinTimeOutInSeconds, strButtonName, "Element Existence",
					strPageName, false)) {
				pagescroll(locator, strPageName);
				ALMFunctions.UpdateReportLogAndALMForFailStatus("Verify the Label is not displayed",
						"'" +strButtonName + "' " + strElementType + " should not displayed in the " + strPageName,
						"'" +strButtonName + "' " + strElementType + " is displayed in the " + strPageName, true);

			} else {

				ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify the Label is not displayed",
						"'" +strButtonName + "' " + strElementType + " should not displayed in the " + strPageName,
						"'" +strButtonName + "' " + strElementType + " is not displayed in the " + strPageName, true);

			}

			break;
		case "mandatory":
			if (objectExists(locator, "isPresent", lngMinTimeOutInSeconds, strButtonName, "Mandatory check",strPageName, false)) {
				pagescroll(locator, strPageName);
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify Object is Mandatory",
						strButtonName + " " + strElementType + " should be displayed as mandatory in the " + strPageName,
						strButtonName + " " + strElementType + " is displayed as mandatory in the " + strPageName, true);

			} else {
				ALMFunctions.UpdateReportLogAndALMForFailStatus("Verify Object is Mandatory",
						strButtonName + " " + strElementType + " should be displayed as mandatory in the " + strPageName,
						strButtonName + " " + strElementType + " is not displayed as mandatory in the " + strPageName, true);

			}

			break;
		case "non mandatory":
			if (objectExists(locator, "isDisplayed", lngMinTimeOutInSeconds, strButtonName, "Non Mandatory check",
					strPageName, false)) {
				pagescroll(locator, strPageName);
				ALMFunctions.UpdateReportLogAndALMForFailStatus("Verify Object is not Mandatory",
						strButtonName + " " + strElementType + " should not displayed as mandatory in the " + strPageName,
						strButtonName + " " + strElementType + " is displayed as mandatory in the " + strPageName, true);

			} else {

				ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify Object is not Mandatory",
						strButtonName + " " + strElementType + " should not displayed as mandatory in the " + strPageName,
						strButtonName + " " + strElementType + " is not displayed as mandatory in the " + strPageName, true);

			}

			break;

		default:
			ALMFunctions.ThrowException("Verify object", "Only pre-defined control must be provided",
					"Unhandled control " + strElementState, false);
			break;
		}

	}


	/**
	 * Method to verify the checkbox status. This method is part of
	 * verifyobjectstate method
	 * 
	 * @param locator, locator value of element
	 * @param strButtonName, fieldName of the element
	 * @param strElementState, state of the element
	 * @param strElementType, type of the element
	 * @param strPageName   - Page Name in which the control is available
	 * @return No return value
	 */
	public void verifyCheckboxStatus(By locator, String strButtonName, String strElementState, String strElementType,
			String strPageName) {
		boolean blnChecked = driver.findElement(locator).isSelected();
		if (objectExists(locator, "isDisplayed", lngMinTimeOutInSeconds, strButtonName, "Element Existence",
				strPageName, false)) {

			switch (strElementState) {

			case "checked":

				if (blnChecked) {
					pagescroll(locator, strPageName);
					ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify CheckBox Status",
							"'"+strButtonName + "' " + strElementType + " should be checked in the " + strPageName,
							"'"+strButtonName + "' " + strElementType + " is checked in the " + strPageName, true);

				} else {
					pagescroll(locator, strPageName);
					ALMFunctions.UpdateReportLogAndALMForFailStatus("Verify CheckBox Status",
							"'"+strButtonName + "' " + strElementType + " should be checked in the " + strPageName,
							"'"+strButtonName + "' " + strElementType + " is not checked in the " + strPageName, true);

				}

				break;

			case "not checked":

				if (blnChecked) {
					pagescroll(locator, strPageName);
					ALMFunctions.UpdateReportLogAndALMForFailStatus("Verify CheckBox Status",
							"'"+strButtonName + "' " + strElementType + " should not checked in the " + strPageName,
							"'"+strButtonName + "' " + strElementType + " is checked in the " + strPageName, true);

				} else {
					pagescroll(locator, strPageName);
					ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify CheckBox Status",
							"'"+strButtonName + "' " + strElementType + " should not checked in the " + strPageName,
							"'"+strButtonName + "' " + strElementType + " is not checked in the " + strPageName, true);

				}

				break;

			default:
				ALMFunctions.ThrowException("Verify object", "Only pre-defined control must be provided",
						"Unhandled control " + strElementState, false);
				break;
			}
		} else {
			ALMFunctions.ThrowException("Verify CheckBox Status",
					strButtonName + " Checkbox should be available in " + strPageName,
					strButtonName + " Checkbox is not available in " + strPageName, false);
		}
	}
	/**
	 * Method to click Link
	 * 
	 * @param strMenuName, Menu name of the link which comes in left menu
	 * @param strLink, value to click link
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */

	public void clickLink(String strMenuName,String strLink, String strPageName) {
		By link = null;
		Common objLink ;
		if(strMenuName.isEmpty()) {
			objLink = new Common(strLink);
			link = objLink.link;
		}
		else
		{
			objLink = new Common(strMenuName,strLink);
			link = objLink.leftMenuLink;
		}


		if (objectExists(link, "isDisplayed", lngMinTimeOutInSeconds, strLink, "Link", strPageName, false)) {
			driverUtil.waitUntilStalenessOfElement(link, strPageName);
			pagescroll(link, strPageName);
			clickByJs(link, lngMinTimeOutInSeconds, strLink, "Link", strPageName, true);
			driverUtil.waitUntilStalenessOfElement(link, staleTableTimeOut, strPageName);
		}  

		else {
			ALMFunctions.ThrowException(strLink, "Link - " + strLink + " should be displayed in " + strPageName,
					"Error - Link - " + strLink + " is not available in " + strPageName, true);
		}
	}

	/**
	 * Method to click Link
	 * 
	 * @param strMenuName, Menu name of the link which comes in left menu
	 * @param strLink, value to click link
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */

	public void doubleClickLink(String strLink, String strPageName) {
		By link = null;
		Common objLink ;

		objLink = new Common(strLink);
		link = objLink.link;



		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
		if (objectExists(link, "isDisplayed", lngMinTimeOutInSeconds, strLink, "Link", strPageName, false)) {
			driverUtil.waitUntilStalenessOfElement(link, strPageName);
			pagescroll(link, strPageName);
			doubleClick(link, lngMinTimeOutInSeconds, strLink, "Link", strPageName, true);
			driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, strPageName);
		}  

		else {
			ALMFunctions.ThrowException(strLink, "Link - " + strLink + " should be displayed in " + strPageName,
					"Error - Link - " + strLink + " is not available in " + strPageName, true);
		}
	}

	/**
	 * Method to click Link
	 * 
	 * @param strMenuName, Menu name of the link which comes in left menu
	 * @param strLink, value to click link
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */

	public void doubleClickLinkinReportTable(String strElementType,String strLink, String strPageName) {
		Common tableReportLink=new Common(strLink);
		switch (strElementType.toLowerCase()) {
		case "rosi":

			driverUtil.waitUntilStalenessOfElement(tableReportLink.tableReportLink, strPageName);
			pagescroll(tableReportLink.tableReportLink, strPageName);
			driverUtil.waitUntilElementEnabled(tableReportLink.tableReportLink, lngMinTimeOutInSeconds);
			doubleClick(tableReportLink.tableReportLink, lngMinTimeOutInSeconds, strLink, "Link", strPageName, true);
			break;

		case "pqas":

			pagescroll(Common.reportSection, strPageName);
			objectExists(Common.reportSection, "isDisplayed", lngMinTimeOutInSeconds, "Report", "Section", strPageName, false);
			Common tableCheckbox=new Common("PQAS");
			driverUtil.waitUntilStalenessOfElement(tableCheckbox.tableCheckBox, strPageName);
			pagescroll(tableCheckbox.tableCheckBox, strPageName);

			clickByJs(tableCheckbox.tableCheckBox, lngMinTimeOutInSeconds, "PQAS", "Checkbox", strPageName, true);
			clickByJs(Common.reportView, lngMinTimeOutInSeconds, "View", "Button", strPageName, true);
			break;
		default:
			ALMFunctions.ThrowException("Verify object", "Only pre-defined control must be provided",
					"Unhandled control " + strLink, false);
			break;
		}

	}
	/**
	 * Method to click Tab
	 * 
	 * @param strValue, value to click link
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */

	public void clickTab(String strTab, String strPageName) {
		Common objTab = new Common(strTab);
		if (objectExists(objTab.tab, "isDisplayed", lngMinTimeOutInSeconds, strTab, "Tab", strPageName, false)) {
			driverUtil.waitUntilStalenessOfElement(objTab.button, strPageName);
			pagescroll(objTab.tab, strPageName);
			clickByJs(objTab.tab, lngMinTimeOutInSeconds, strTab, "Tab", strPageName, true);
		} 
		else {
			ALMFunctions.ThrowException(strTab, "Tab - " + strTab + " should be displayed in " + strPageName,
					"Error - Tab - " + strTab + " is not available in " + strPageName, true);
		}
	}
	
	/**
	 * Method to click Label
	 * 
	 * @param strValue, value to click link
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */

	public void clickLabel(String strTab, String strPageName) {
		Common objTab = new Common(strTab);
		if (objectExists(objTab.label, "isDisplayed", lngMinTimeOutInSeconds, strTab, "Label", strPageName, false)) {
			driverUtil.waitUntilStalenessOfElement(objTab.label, strPageName);
			pagescroll(objTab.label, strPageName);
			clickByJs(objTab.label, lngMinTimeOutInSeconds, strTab, "Label", strPageName, true);
		} 
		else {
			ALMFunctions.ThrowException(strTab, "Label - " + strTab + " should be displayed in " + strPageName,
					"Error - Label - " + strTab + " is not available in " + strPageName, true);
		}
	}

	/**
	 * Method to click button
	 * 
	 * @param strValue, value to click button
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */

	public void clickButton(String strBtn, String strPageName) {
		Common objBtn = new Common(strBtn);
		if(strBtn.equalsIgnoreCase("Filter"))
		{
			clickByJs(Common.filterButton, lngMinTimeOutInSeconds, strBtn, "Button", strPageName, true);
		}
		else if(strBtn.equalsIgnoreCase("Clear Filter"))
		{
			clickByJs(Common.clearFilterButton, lngMinTimeOutInSeconds, strBtn, "Button", strPageName, true);
		}else if(strBtn.equalsIgnoreCase("Validate")) {
			clickByJs(Common.validateButton, lngMinTimeOutInSeconds,strBtn, "Button",strPageName,true);
		}else if(strBtn.equalsIgnoreCase("Analyst:")) {
			pagescroll(Common.analystLookup, strPageName);
			clickByJs(Common.analystLookup, lngMinTimeOutInSeconds,strBtn, "Lookup button",strPageName,true);
		}
		else if (objectExists(objBtn.button, "isDisplayed", lngMinTimeOutInSeconds, strBtn, "Button", strPageName, false)) {
			driverUtil.waitUntilStalenessOfElement(objBtn.button, strPageName);
			pagescroll(objBtn.button, strPageName);
			clickByJs(objBtn.button, lngMinTimeOutInSeconds, strBtn, "Button", strPageName, true);
		}  else {
			ALMFunctions.ThrowException(strBtn, "Button - " + strBtn + " should be displayed in " + strPageName,
					"Error - Button - " + strBtn + " is not available in " + strPageName, true);
		}
	}

	/**
	 * Method to click button
	 * 
	 * @param strValue, value to click button
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */

	public void clickFooterButton(String strBtn, String strPageName) {
		Common objBtn = new Common(strBtn);
		if (objectExists(objBtn.footerButton, "isDisplayed", lngMinTimeOutInSeconds, strBtn, "Button", strPageName, false)) {
			driverUtil.waitUntilStalenessOfElement(objBtn.footerButton, strPageName);
			pagescroll(objBtn.footerButton, strPageName);
			clickByJs(objBtn.footerButton, lngMinTimeOutInSeconds, strBtn, "Button", strPageName, true);
		}  else {
			ALMFunctions.ThrowException(strBtn, "Button - " + strBtn + " should be displayed in " + strPageName,
					"Error - Button - " + strBtn + " is not available in " + strPageName, true);
		}
	}

	/**
	 * Method to click button which appears an image
	 * 
	 * @param strValue, value to click button
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */

	public void clickImgButton(String strBtn, String strPageName) {
		Common objBtn = new Common(strBtn);
		if (objectExists(objBtn.imgButton, "isDisplayed", lngMinTimeOutInSeconds, strBtn, "Button", strPageName, false)) {
			driverUtil.waitUntilStalenessOfElement(objBtn.imgButton, strPageName);
			pagescroll(objBtn.imgButton, strPageName);
			clickByJs(objBtn.imgButton, lngMinTimeOutInSeconds, strBtn, "Button", strPageName, true);
		}  else {
			ALMFunctions.ThrowException(strBtn, "Button - " + strBtn + " should be displayed in " + strPageName,
					"Error - Button - " + strBtn + " is not available in " + strPageName, true);
		}
	}

	/**
	 * Method to click button
	 * 
	 * @param strValue, value to click button
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */

	public void clickLookUpButton(String strBtn, String strPageName) {
		Common objBtn = new Common(strBtn);
		if(strBtn.contains("TableLookUpBtn"))
		{

			clickByJs(Common.tableLookUpBtn, lngMinTimeOutInSeconds, strBtn, "Table Look Up Button", strPageName, true);
		}
		else if (objectExists(objBtn.lookupBtn, "isDisplayed", lngMinTimeOutInSeconds, strBtn, "Button", strPageName, false)) {
			driverUtil.waitUntilStalenessOfElement(objBtn.lookupBtn, strPageName);
			pagescroll(objBtn.lookupBtn, strPageName);
			clickByJs(objBtn.lookupBtn, lngMinTimeOutInSeconds, strBtn, "Look Up Button", strPageName, true);
		}  else {
			ALMFunctions.ThrowException(strBtn, "Look Up Button - " + strBtn + " should be displayed in " + strPageName,
					"Error - LookupButton - " + strBtn + " is not available in " + strPageName, true);
		}
	}


	/**
	 * Function to manage and switch to new window which opened recently
	 * 
	 * @param No parameters
	 * @return No return value
	 */

	public void manageAndSwitchNewWindow() {
		if (!windowName.isEmpty()) {
			if (driverUtil.waitUntilWindowCountAvailable(windowName.size() + 1, "New Window", lngCtrlTimeOutInSeconds,
					false)) {
				updateWindowHandle();
			} else if (driverUtil.waitUntilWindowCountAvailable(windowName.size() - 2, "New Window", lngCtrlTimeOutInSeconds,
					false)) {
				updateWindowHandle();
			} else if (driverUtil.waitUntilWindowCountAvailable(windowName.size() - 1, "New Window", lngCtrlTimeOutInSeconds,
					false)) {
				updateWindowHandle();
			}
		} else {
			ALMFunctions.ThrowException("Verify New Window Available",
					"New window title as " + driver.getTitle() + " should be displayed",
					"New window " + driver.getTitle() + " is NOT displayed", true);
		}
		String strPage =null;
		if(driver.getTitle().equalsIgnoreCase(""))
		{
			strPage = driver.getTitle();
			driver.switchTo().window(windowName.get("Window" + windowName.size()));
			report.updateTestLog("Switch Window", "Switched to new window title as " + strPage, Status.DONE);
			driver.manage().window().maximize();
		}
		else
		{
			strPage= driver.getCurrentUrl();
			driver.switchTo().window(windowName.get("Window" + windowName.size()));
			report.updateTestLog("Switch Window", "Switched to new window URL as " + strPage, Status.DONE);
			driver.manage().window().maximize();
		}

	}

	/**
	 * Function to manage and switch to previous window after closing the active
	 * window
	 * 
	 * @param No parameters
	 * @return No return value
	 */
	public void closeAndSwitchPreviousWindow() {
		driver.close();
		windowName.remove("Window" + windowName.size());
		driver.switchTo().window(windowName.get("Window" + windowName.size()));
		report.updateTestLog("Switch Window", "Switched to new window title as " + driver.getTitle(), Status.DONE);
		driver.manage().window().maximize();
		driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, "Window Switch");

	}
	/**
	 * Function to manage and switch to previous window after closing the active
	 * window
	 * 
	 * @param No parameters
	 * @return No return value
	 */
	public void switchPreviousWindow() {

		windowName.remove("Window" + windowName.size());
		driver.switchTo().window(windowName.get("Window" + windowName.size()));
		report.updateTestLog("Switch Window", "Switched to new window title as " + driver.getTitle(), Status.DONE);
		driver.manage().window().maximize();
		driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds, "Window Switch");

	}
	/**
	 * Function to handle window values
	 * 
	 * @param No parameters
	 * @return No return value
	 */
	public void updateWindowHandle() {
		if (windowName.size() < driver.getWindowHandles().size()) {
			for (String windowHand : driver.getWindowHandles()) {
				if (!windowName.containsValue(windowHand)) {
					windowName.put("Window" + (windowName.size() + 1), windowHand);
				}
			}
		}
		if (windowName.size() > driver.getWindowHandles().size()) {
			List<String> windowToRemove = new ArrayList<String>();
			for (String strWindowName : windowName.values()) {
				if (!driver.getWindowHandles().contains(strWindowName)) {
					windowToRemove.add(strWindowName);

				}
			}
			windowName.values().removeAll(windowToRemove);
		}

	}

	/**
	 * Method to verify the expected text is available in different element types
	 * 
	 * @param strElmType, Element type to be verified
	 * @param strValue, ELement name and expected value to be verified should be provided with "!" in sheet
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */
	public void getTextAndVerify(String strElmType,String strValue, String strPageName) {

		WebElement elm;
		String strGetText = null ;
		String[] strObjectStateValue = strValue.split("!");
		String strElementName = strObjectStateValue[0];
		String strExpectedValue = strObjectStateValue[1];
		Common locator = new Common(strElementName);
		driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds);
		driverUtil.waitUntilAjaxLoadingComplete(lngMinTimeOutInSeconds, strPageName);
		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
		switch (strElmType.toLowerCase()) {
		case "labelvalue":
			driverUtil.waitUntilStalenessOfElement(locator.labelValue, strPageName);
			elm  = driver.findElement(locator.labelValue);
			//driverUtil.waitUntilStalenessOfElement(elm, strPageName);
			strGetText = elm.getText().trim();
			if(strGetText.contains("\n"))
			{
				strGetText = strGetText.replace("\n", "");
			}
			pagescroll(elm, strPageName);
			break;

		case "label":
			elm  = driver.findElement(By.xpath(".//div[contains(@class,'detailTitleBar')]//span[@class='title']"));
			//driverUtil.waitUntilStalenessOfElement(elm, strPageName);
			strGetText = elm.getText().trim();
			if(strGetText.contains("\n"))
			{
				strGetText = strGetText.replace("\n", "");
			}
			pagescroll(elm, strPageName);
			break;
		case "labeltitle":
			elm  = driver.findElement(By.xpath(".//span[@class='title']"));
			//driverUtil.waitUntilStalenessOfElement(elm, strPageName);
			strGetText = elm.getText().trim();
			pagescroll(elm, strPageName);
			break;
		case "dropdown":
			elm  = driver.findElement(locator.dropdown);
			driverUtil.waitUntilStalenessOfElement(elm, strPageName);
			strGetText = elm.getAttribute("value");
			pagescroll(elm, strPageName);
			break;

		case "textbox":
			elm  = driver.findElement(locator.textbox);
			//driverUtil.waitUntilStalenessOfElement(elm, strPageName);
			strGetText = elm.getAttribute("value");
			pagescroll(elm, strPageName);
			break;

		default:

			ALMFunctions.ThrowException("Test Data",
					"Only Pre-Defined Fields Type must be provided in the test data sheet",
					"Error - Unhandled Field Type " + strElmType, false);
		}

		if(strExpectedValue.equalsIgnoreCase(strGetText))
		{
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify Label Value Exist", 
					"Expected Label Value:'" + strExpectedValue + "' should be displayed in '"+strElementName +"' <br>", 
					"Actual Label Value: '" + strGetText + "' is displayed in '"+strElementName +"' <br>", true);

		}
		else
		{
			ALMFunctions.UpdateReportLogAndALMForFailStatus("Verify Label Value Exist", 
					"Expected Label Value: '" + strExpectedValue + "' should be displayed  <br>", 
					"Expected Label Value: '" + strExpectedValue + "' is not displayed in '"+strElementName +"'. Actual message displayed is: '" + strGetText + "'<br>", true);
		}
	}

	/**
	 * Method to verify the expected text is available in different element types
	 * 
	 * @param strElmType, Element type to be verified
	 * @param strValue, ELement name and expected value to be verified should be provided with "!" in sheet
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */
	public void getValueFromApplication(String strElmType,String strValue, String strPageName) {

		WebElement elm;
		String strGetText = null ;
		Common locator = new Common(strValue);
		driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds);
		driverUtil.waitUntilAjaxLoadingComplete(lngMinTimeOutInSeconds, strPageName);

		switch (strElmType.toLowerCase()) {
		case "labelvalue":
			elm  = driver.findElement(locator.labelValue);
			//driverUtil.waitUntilStalenessOfElement(elm, strPageName);
			strGetText = elm.getText().trim();
			break;
		case "textboxvalue":
			elm  = driver.findElement(locator.textbox);
			//driverUtil.waitUntilStalenessOfElement(elm, strPageName);
			strGetText = elm.getAttribute("value");
			break;
		case "tablelink":
			elm  = driver.findElement(Common.selectFirstRowInTable);
			//driverUtil.waitUntilStalenessOfElement(elm, strPageName);
			strGetText = elm.getText().trim();
			break;


		default:

			ALMFunctions.ThrowException("Test Data",
					"Only Pre-Defined Fields Type must be provided in the test data sheet",
					"Error - Unhandled Field Type " + strElmType, false);
		}


		dataTable.putData("Parametrized_Checkpoints", strValue, strGetText);
		report.updateTestLog("Fetch value from "+strElmType, "User should be able to fetch value from '"+strValue+"'", 
				"Fetched value from application is '"+strGetText+"'", Status.DONE);




	}


	/**
	 * Method to enter a value in a text box
	 * 
	 * @param strFieldName, The name of the text box
	 * @param strValue, value to be entered in a text box
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */

	public void enterTextbox(String strFieldName, String strValue, String strPageName)

	{
		@SuppressWarnings("unused")
		By textBox;
		String strLatestValue="";
		if(strValue.contains("|Enter"))
		{
			strLatestValue = strValue.replace("|Enter", "");
		}
		/*Common objLink = new Common(strFieldName);
		textBox = objLink.textbox;*/
		Common textbox = new Common(strFieldName);
		//driverUtil.waitUntilStalenessOfElement(textbox.textbox, strPageName);
		pagescroll(textbox.textbox, strPageName);
		clear(textbox.textbox, lngMinTimeOutInSeconds, strFieldName, strPageName);
		click(textbox.textbox, lngMinTimeOutInSeconds, strValue, strFieldName, strPageName, false);
		if(strValue.contains("|Enter"))
		{
			sendkeys(textbox.textbox, lngMinTimeOutInSeconds, strLatestValue, strFieldName, strPageName,true);
			sendkeys(textbox.textbox, lngMinTimeOutInSeconds, Keys.ENTER, strFieldName, strPageName);
			driverUtil.waitUntilStalenessOfElement(textbox.textbox, strPageName);

		}
		else
		{
			sendkeys(textbox.textbox, lngMinTimeOutInSeconds, strValue, strFieldName, strPageName,true);
			sendkeys(textbox.textbox, lngMinTimeOutInSeconds, Keys.TAB, strFieldName, strPageName);
		}

	}
	/**
	 * Method to Sort Table Column in Ascending order
	 * 
	 
	 * @param strColumnName, Column name
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */
	public void sortTableColumnAsc(String strColumnName, String strPageName) {
        Common objColumn = new Common(strColumnName);

 

        if (!objectExists(objColumn.tableColumnSortAsc, "isDisplayed", lngMinTimeOutInSeconds, strColumnName, "Column Name", strPageName, false)) {
            if(objectExists(objColumn.tableColumnName, "isDisplayed", lngMinTimeOutInSeconds, strColumnName, "Column Name", strPageName, true)) {
                clickByJs(objColumn.tableColumnName, lngMinTimeOutInSeconds, strColumnName, "Column Name", strPageName, true);
            }

 

        } 
        else if(objectExists(objColumn.tableColumnSortAsc, "isDisplayed", lngMinTimeOutInSeconds, strColumnName, "Column Name", strPageName, false)){
            report.updateTestLog("Sort Column", strColumnName+" column should be sorted in Ascending", strColumnName+" column is already sorted in Ascending", null);
        }
        else if(objectExists(objColumn.tableColumnSortDesc, "isDisplayed", lngMinTimeOutInSeconds, strColumnName, "Column Name", strPageName, false)){
            clickByJs(objColumn.tableColumnName, lngMinTimeOutInSeconds, strColumnName, "Column Name", strPageName, true);
        }


        else {
            ALMFunctions.ThrowException(strColumnName, "Table Column Name - " + strColumnName + " should be displayed in " + strPageName,
                    "Error - Table Column Name - " + strColumnName + " is not available in " + strPageName, true);
        }
    }
	/**
	 * Method to enter a value in a text Area
	 * 
	 * @param strFieldName, The name of the text box
	 * @param strValue, value to be entered in a text box
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */

	public void enterTextArea(String strFieldName, String strValue, String strPageName)

	{
		@SuppressWarnings("unused")

		Common textArea = new Common(strFieldName);
		driverUtil.waitUntilStalenessOfElement(textArea.textbox, strPageName);
		pagescroll(textArea.textArea, strPageName);
		clear(textArea.textArea, lngMinTimeOutInSeconds, strFieldName, strPageName);
		click(textArea.textArea, lngMinTimeOutInSeconds, strValue, strFieldName, strPageName, false);

		sendkeys(textArea.textArea, lngMinTimeOutInSeconds, strValue, strFieldName, strPageName,true);
		sendkeys(textArea.textArea, lngMinTimeOutInSeconds, Keys.TAB, strFieldName, strPageName);


	}

	/**
	 * Method to clear a value in a text box
	 * 
	 * @param strFieldName, The name of the text box
	 * @param strValue, value to be entered in a text box
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */

	public void clearTextbox(String strFieldName, String strValue, String strPageName)

	{
		@SuppressWarnings("unused")
		By textBox;
		Common objTextBox;
		if(strFieldName.contains("!"))
		{
			String[] strLabels = strFieldName.split("!");
			String strLabelName = strLabels[0];
			String strTextBoxName = strLabels[1];
			objTextBox = new Common(strLabelName,strTextBoxName);
			pagescroll(objTextBox.multipleTextBox, strPageName);
			clear(objTextBox.multipleTextBox, lngMinTimeOutInSeconds, strFieldName, strPageName);
		}
		else
		{
			objTextBox = new Common(strFieldName);
			pagescroll(objTextBox.textbox, strPageName);
			clear(objTextBox.textbox, lngMinTimeOutInSeconds, strFieldName, strPageName);
		}

	}

	/**
	 * Method to enter a value in a text box which Label have Two text boxes
	 * 
	 * @param strFieldName, The name of the text box
	 * @param strValue, value to be entered in a text box
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */

	public void enterMultipleTextbox(String strFieldName, String strValue, String strPageName)

	{
		@SuppressWarnings("unused")
		String[] strLabels = strFieldName.split("!");
		String strLabelName = strLabels[0];
		String strTextBoxName = strLabels[1];
		Common textBox = new Common(strLabelName,strTextBoxName);
		pagescroll(textBox.multipleTextBox, strPageName);
		clear(textBox.multipleTextBox, lngMinTimeOutInSeconds, strLabelName, strPageName);
		click(textBox.multipleTextBox, lngMinTimeOutInSeconds, strValue, strLabelName, strPageName, false);
		sendkeys(textBox.multipleTextBox, lngMinTimeOutInSeconds, strValue, strLabelName, strPageName,true);
		sendkeys(textBox.multipleTextBox, lngMinTimeOutInSeconds, Keys.TAB, strLabelName, strPageName);
	}
	/**
	 * Method to enter a value in a Dialog text box
	 * 
	 * @param WindowName, name of the dialog
	 * @param strLabel, The name of the text box
	 * @param strValue, value to be entered in a text box
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */

	public void enterDialogTextbox(String strWindowName, String strLabel, String strValue, String strPageName)

	{
		@SuppressWarnings("unused")
		By textBox;
		String strLatestValue="";
		if(strValue.contains("|Enter"))
		{
			strLatestValue = strValue.replace("|Enter", "");
		}
		/*Common objButton = new Common(strWindowName, strLabel);
		textBox = objButton.dialogTextBox;
		Common textbox = new Common(strLabel);*/
		Common textbox = new Common(strWindowName,strLabel);
		pagescroll(textbox.dialogTextBox, strPageName);
		clear(textbox.dialogTextBox, lngMinTimeOutInSeconds, strLabel, strPageName);
		click(textbox.dialogTextBox, lngMinTimeOutInSeconds, strValue, strLabel, strPageName, false);

		if(strValue.contains("|Enter"))
		{
			sendkeys(textbox.dialogTextBox, lngMinTimeOutInSeconds, strLatestValue, strLabel, strPageName,true);
			sendkeys(textbox.dialogTextBox, lngMinTimeOutInSeconds, Keys.ENTER, strLabel, strPageName);
			driverUtil.waitUntilStalenessOfElement(textbox.dialogTextBox, strPageName);

		}
		else
		{
			sendkeys(textbox.dialogTextBox, lngMinTimeOutInSeconds, strValue, strLabel, strPageName,true);
		}
		//driverUtil.waitUntilStalenessOfElement(textbox.dialogTextBox, strPageName);
		sendkeys(textbox.dialogTextBox, lngMinTimeOutInSeconds, Keys.TAB, strLabel, strPageName);
	}

	/**
	 * Method to click a Button in a Dialog
	 * 
	 * @param WindowName, name of the dialog
	 * @param strLabel, The name of the Button
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */

	public void clickDialogButton(String strWindowName, String strLabel, String strPageName)

	{
		@SuppressWarnings("unused")
		By textBox;

		Common objButton = new Common(strWindowName, strLabel);
		By button = objButton.dialogButton;
		if (objectExists(button, "isDisplayed", lngMinTimeOutInSeconds, strLabel, "Button", strPageName, false)) {
			driverUtil.waitUntilStalenessOfElement(button, strPageName);
			pagescroll(button, strPageName);
			click(button, lngMinTimeOutInSeconds, strLabel, "Dialog Button", strPageName, true);
		}

		else {
			ALMFunctions.ThrowException(strLabel, "Button - " + strLabel + " should be displayed in " + strPageName,
					"Error - Button - " + strLabel + " is not available in " + strPageName, true);
		}
	}
	/**
	 * Method to Click Check box
	 * 
	 * @param strFieldName, Name of the check box
	 * @param strValueToSelect, "Check" or "Yes" or "Select" to be provided from the sheet
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */

	public void selectCheckBox(String strFieldName, String strValueToSelect, String strPageName) {

		try {
			Common checkBox = new Common(strFieldName,strValueToSelect);
			driverUtil.waitUntilPageReadyStateComplete(lngMinTimeOutInSeconds);
			driverUtil.waitUntilAjaxLoadingComplete(lngMinTimeOutInSeconds, strPageName);
			driverUtil.waitUntilStalenessOfElement(checkBox.checkbox, strPageName);
			boolean blnisSelected = driver.findElement(checkBox.checkbox).isSelected();
			if (blnisSelected==false && (strValueToSelect.equalsIgnoreCase("check") || strValueToSelect.equalsIgnoreCase("yes") || strValueToSelect.equalsIgnoreCase("select"))) {
				clickByJs(checkBox.checkbox, lngMinTimeOutInSeconds, strValueToSelect + " in " + strFieldName, "Check Box", strPageName, true);
			} else if(blnisSelected==true && (strValueToSelect.equalsIgnoreCase("uncheck") || strValueToSelect.equalsIgnoreCase("no"))) {
				clickByJs(checkBox.checkbox, lngMinTimeOutInSeconds, strValueToSelect + " in " + strFieldName, "Check Box", strPageName, true);
			}

			if(blnisSelected == true)
			{
				report.updateTestLog("Select Checkbox", "Checkbox should be selected in" + driver.getTitle(), "Checkbox is already selected in" + driver.getTitle(), Status.DONE);
			}
			else if(blnisSelected == false)
			{
				report.updateTestLog("Unselect Checkbox", "Checkbox should be unselected in" + driver.getTitle(), "Checkbox is already unselected in" + driver.getTitle(), Status.DONE);
			}
		}
		catch(Exception e) {
			ALMFunctions.ThrowException(strFieldName, "Checkbox - " + strFieldName + " should be displayed in " + strPageName,
					"Error - Checkbox - " + strFieldName + " is not available in " + strPageName, true);
		}

	}




	/**
	 * Method to find record in Table
	 * 
	 * @param strColumnName, Primary column name and secondary column name to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strValues,     Primary column value and secondary column value to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strPageName,   Page Name in which the control is available
	 * @return No return value
	 */
	public void findRecordAndVerify(String strColumnName, String strValue, String strPageName) {
		String strFirstCol = "";
		String strSecondCol = "";
		String strRefernceColVal = "";
		String strExpectedColVal = "";
		String strTableLocation = "";
		String strActValue = "";

		By row = Common.tableRow;
		By webTableHeader = null;
		int intColumnIndex = 0;
		String strTableName = "LookUp Table";


		String[] strColNames = strColumnName.split(":");
		String[] strValues = strValue.split("!");
		strFirstCol = strColNames[0];
		strSecondCol = strColNames[1];
		strRefernceColVal = strValues[0].trim();
		strExpectedColVal = strValues[1].trim();
		webTableHeader = Common.tableHeader;

		driverUtil.waitUntilStalenessOfElement(webTableHeader, lngMinTimeOutInSeconds, strPageName);

		intColumnIndex = getColumnIndex(strFirstCol, webTableHeader, strTableName, false, false) + 1;

		if (!(intColumnIndex != 0)) {
			ALMFunctions.ThrowException("Get index of column name",
					"Expected column name as " + strFirstCol + " shall be displayed",
					"Expected column name as " + strFirstCol + " is not displayed", true);
		}


		boolean blnFound = false;
		boolean blnClick = false;
		int intCurrentPage = 1;
		List<WebElement> listtableRow = driver.getWebDriver().findElements(row);
		if (listtableRow.isEmpty()) {

			ALMFunctions.ThrowException(strRefernceColVal, strRefernceColVal + " table row should be displayed",
					strRefernceColVal + " table row are not displayed", true);
			ALMFunctions.ThrowException(strRefernceColVal, "" + strTableName + " Table row should be displayed",
					"" + strTableName + " Table row is NOT displayed", true);

		} else {

			boolean blnRecordNotFound = false;
			do {

				if (intCurrentPage != 1) {

					if (objectExists(Common.nextPage, "isDisplayed", lngMinTimeOutInSeconds, "Next", "Button",
							strTableName, false)) {
						click(Common.nextPage, lngMinTimeOutInSeconds, "Next", "Button", strTableName, false);
						driverUtil.waitUntilStalenessOfElement(Common.nextPage, strTableName);

					} else {
						blnRecordNotFound = true;
					}

				}

				listtableRow = driver.getWebDriver().findElements(row);
				WebElement eleRows = null;

				for (WebElement rows : listtableRow) {

					strActValue = rows.findElement(By.xpath("(.//td[" + intColumnIndex + "]//select)|(.//td[" + intColumnIndex + "]//span)")).getText().trim();

					if (strRefernceColVal.contains("contains")) {
						strRefernceColVal = strRefernceColVal.replaceAll("contains", "");
						if (strActValue.contains(strRefernceColVal)) {
							eleRows = rows;
							blnFound = true;

						}

					} else if (strActValue.equals(strRefernceColVal.trim())) {
						eleRows = rows;
						blnFound = true;

					}


					if (blnFound) {
						webTableHeader = null;
						int intColumnIndex1 = 0;
						webTableHeader = Common.tableHeader;

						intColumnIndex1 = getColumnIndex(strSecondCol, webTableHeader, strTableName, false, false)+ 1;

						if (!(intColumnIndex1 != 0)) {
							ALMFunctions.ThrowException("Get index of column name",
									"Expected column name as " + strSecondCol + " shall be displayed",
									"Expected column name as " + strSecondCol + " is not displayed", true);
						}

						WebElement eleClick = eleRows.findElement(By.xpath("(.//td[" + intColumnIndex1 + "]//select)|(.//td[" + intColumnIndex1 + "]//span)"));


						String actualVal = eleClick.getText().trim();
						if (strExpectedColVal.contains("contains")) {
							strExpectedColVal = strExpectedColVal.replaceAll("contains", "");
							if (actualVal.contains(strExpectedColVal)) {
								blnClick = true;
								break;
							}

						} else if (actualVal.equals(strExpectedColVal.trim())) {
							blnClick = true;
							break;
						}

					}

				}

				intCurrentPage++;
			} while (!(blnClick || blnRecordNotFound));

			if (blnRecordNotFound) {
				ALMFunctions.ThrowException(strExpectedColVal, strExpectedColVal + " value should be display in table row",
						"Error - Specified Record " + strExpectedColVal + " is not found in the " + strSecondCol
						+ " column in table",
						true);

			}

			if (blnClick) {
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Record should be available in Table",
						"'"+strExpectedColVal + "' value should be displayed",
						"Specified Record '" + strExpectedColVal + "' is found in '" + strSecondCol + "' column in table",
						true);

			} else {
				ALMFunctions.ThrowException("Click on " + strRefernceColVal + " Table row",
						"Created From Trigger : " + strActValue + " in " + strTableName
						+ " Table row should be clicked on " + strTableName + "",
						"Error - Specified file in table row is NOT clicked on " + strTableName + " Page", true);
			}

		}

	}
	/**
	 * Method to verify ndc value
	 * 
	 *
	 * @param strPageName,   Page Name in which the control is available
	 * @return No return value
	 */
	public void findRecordAndVerifyNDC(String strColumnName, String strValue, String strPageName) {
		String ndcProductFirstRow = dataTable.getData("Parametrized_Checkpoints", "Tablestring For First Row");

		pagescroll(new Common(ndcProductFirstRow).label,driver.getTitle());
		String ndcProductLineForFirstRow=getText(new Common(ndcProductFirstRow).label, lngMinTimeOutInSeconds,"NDC-11",strPageName);

		if(ndcProductLineForFirstRow.equalsIgnoreCase(ndcProductFirstRow)) {


			ALMFunctions.UpdateReportLogAndALMForPassStatus("Record should be available in Table",
					"'"+ndcProductFirstRow +"'"+" value should be displayed in 'NDC-11' Column.",
					"Specified Record"+"'"+ndcProductFirstRow +"'"+" is found in 'NDC-11' column in table", true);
		} else {

			ALMFunctions.UpdateReportLogAndALMForFailStatus("Record should be available in Table",
					"'"+ndcProductFirstRow +"'"+" value should be displayed in 'NDC-11' Column.",
					"Specified Record"+"'"+ndcProductFirstRow +"'"+" is not found in 'NDC-11' column in table", true);

		}

	}
	/**
	 * Method to find record in Table
	 * 
	 * @param strColumnName, Primary column name and secondary column name to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strValues,     Primary column value and secondary column value to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strPageName,   Page Name in which the control is available
	 * @return No return value
	 */
	public void findRecordAndEnterValueInTable(String strColumnName, String strValue, String strPageName) {
		String strFirstCol = "";
		String strSecondCol = "";
		String strRefernceColVal = "";
		String strValueToBeEntered = "";
		String strObjectType = "";
		String strActValue = "";

		By row = Common.tableRow;
		By webTableHeader = null;
		int intColumnIndex = 0;
		String strTableName = "LookUp Table";


		String[] strColNames = strColumnName.split(":");
		String[] strValues = strValue.split("!");
		strFirstCol = strColNames[0];
		strSecondCol = strColNames[1];
		strRefernceColVal = strValues[0].trim();
		strValueToBeEntered = strValues[1].trim();
		strObjectType= strValues[2].trim();
		webTableHeader = Common.tableHeader;

		driverUtil.waitUntilStalenessOfElement(webTableHeader, lngMinTimeOutInSeconds, strPageName);

		intColumnIndex = getColumnIndex(strFirstCol, webTableHeader, strTableName, false, false) + 1;

		if (!(intColumnIndex != 0)) {
			ALMFunctions.ThrowException("Get index of column name",
					"Expected column name as " + strFirstCol + " shall be displayed",
					"Expected column name as " + strFirstCol + " is not displayed", true);
		}


		boolean blnFound = false;
		boolean blnClick = false;
		int intCurrentPage = 1;
		List<WebElement> listtableRow = driver.getWebDriver().findElements(row);
		if (listtableRow.isEmpty()) {

			ALMFunctions.ThrowException(strRefernceColVal, strRefernceColVal + " table row should be displayed",
					strRefernceColVal + " table row are not displayed", true);
			ALMFunctions.ThrowException(strRefernceColVal, "" + strTableName + " Table row should be displayed",
					"" + strTableName + " Table row is NOT displayed", true);

		} else {

			boolean blnRecordNotFound = false;
			do {

				if (intCurrentPage != 1) {

					if (objectExists(Common.nextPage, "isDisplayed", lngMinTimeOutInSeconds, "Next", "Button",
							strTableName, false)) {
						click(Common.nextPage, lngMinTimeOutInSeconds, "Next", "Button", strTableName, false);
						driverUtil.waitUntilStalenessOfElement(Common.nextPage, strTableName);

					} else {
						blnRecordNotFound = true;
					}

				}

				listtableRow = driver.getWebDriver().findElements(row);
				WebElement eleRows = null;

				for (WebElement rows : listtableRow) {

					if(strObjectType.equalsIgnoreCase("dropdown"))
					{
						WebElement eleClick = rows.findElement(By.xpath(".//select"));
						Select dropdownval = new Select(eleClick);
						dropdownval.selectByVisibleText(strValueToBeEntered);
						blnClick = true;
						break;
					}
					else if(strObjectType.contains("Dropdown2"))
					{
						WebElement eleClick = rows.findElement(By.xpath(".//select[2]"));
						Select dropdownval = new Select(eleClick);
						dropdownval.selectByVisibleText(strValueToBeEntered);
						blnClick = true;
						break;
					}
					else if(strObjectType.equalsIgnoreCase("textbox"))
					{
						WebElement eleClick = rows.findElement(By.xpath(".//input[@type='text']"));
						clear(eleClick, lngMinTimeOutInSeconds, strColumnName, strPageName);
						click(eleClick, lngMinTimeOutInSeconds, strValueToBeEntered, strColumnName, strPageName, false);
						sendkeys(eleClick, lngMinTimeOutInSeconds, strValueToBeEntered, strColumnName, strPageName,true);
						sendkeys(eleClick, lngMinTimeOutInSeconds, Keys.TAB, strColumnName, strPageName,false);
						blnClick = true;
						break;
					}



				}

				intCurrentPage++;
			} while (!(blnClick || blnRecordNotFound));

			if (blnClick) {

				report.updateTestLog("Enter the '" + strSecondCol + "' value from table","'"+
						strSecondCol + "' value should be entered in table",
						"Entered Value from table for " + strSecondCol + " is : " + strValueToBeEntered 
						+" in the row of:'"+strRefernceColVal+"'" , Status.DONE);

			} else {
				ALMFunctions.ThrowException(strSecondCol, strSecondCol + " value should be display in table row",
						"Error - Specified Record " + strSecondCol + " is not found in the " + strTableName + " table",
						true);
			}

		}

	}
	/**
	 * Method to find record in Table
	 * 
	 * @param strColumnName, Primary column name and secondary column name to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strValues,     Primary column value and secondary column value to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strPageName,   Page Name in which the control is available
	 * @return No return value
	 */
	public void findRecordAndVerifyinFilterPlainTable(String strColumnName, String strValue, String strPageName) {
		String strFirstCol = "";
		String strSecondCol = "";
		String strRefernceColVal = "";
		String strExpectedColVal = "";
		String strTableLocation = "";
		String strActValue = "";

		By row = Common.tableRow;
		By webTableHeader = null;
		int intColumnIndex = 0;
		String strTableName = "LookUp Table";


		String[] strColNames = strColumnName.split(":");
		String[] strValues = strValue.split("!");
		strFirstCol = strColNames[0];
		strSecondCol = strColNames[1];
		strRefernceColVal = strValues[0].trim();
		strExpectedColVal = strValues[1].trim();
		webTableHeader = Common.tableHeader;

		driverUtil.waitUntilStalenessOfElement(webTableHeader, lngMinTimeOutInSeconds, strPageName);

		intColumnIndex = getColumnIndex(strFirstCol, webTableHeader, strTableName, false, false);

		if (!(intColumnIndex != 0)) {
			ALMFunctions.ThrowException("Get index of column name",
					"Expected column name as " + strFirstCol + " shall be displayed",
					"Expected column name as " + strFirstCol + " is not displayed", true);
		}


		boolean blnFound = false;
		boolean blnClick = false;
		int intCurrentPage = 1;
		List<WebElement> listtableRow = driver.getWebDriver().findElements(row);
		if (listtableRow.isEmpty()) {

			ALMFunctions.ThrowException(strRefernceColVal, strRefernceColVal + " table row should be displayed",
					strRefernceColVal + " table row are not displayed", true);
			ALMFunctions.ThrowException(strRefernceColVal, "" + strTableName + " Table row should be displayed",
					"" + strTableName + " Table row is NOT displayed", true);

		} else {

			boolean blnRecordNotFound = false;
			do {

				if (intCurrentPage != 1) {

					if (objectExists(Common.nextPage, "isDisplayed", lngMinTimeOutInSeconds, "Next", "Button",
							strTableName, false)) {
						click(Common.nextPage, lngMinTimeOutInSeconds, "Next", "Button", strTableName, false);
						driverUtil.waitUntilStalenessOfElement(Common.nextPage, strTableName);

					} else {
						blnRecordNotFound = true;
					}

				}

				listtableRow = driver.getWebDriver().findElements(row);
				WebElement eleRows = null;

				for (WebElement rows : listtableRow) {

					strActValue = rows.findElement(By.xpath("(.//td[" + intColumnIndex + "]//select)|(.//td[" + intColumnIndex + "]//span)")).getText().trim();

					if (strRefernceColVal.contains("contains")) {
						strRefernceColVal = strRefernceColVal.replaceAll("contains", "");
						if (strActValue.contains(strRefernceColVal)) {
							eleRows = rows;
							blnFound = true;

						}

					} else if (strActValue.equals(strRefernceColVal.trim())) {
						eleRows = rows;
						blnFound = true;

					}


					if (blnFound) {
						webTableHeader = null;
						int intColumnIndex1 = 0;
						webTableHeader = Common.tableHeader;

						intColumnIndex1 = getColumnIndex(strSecondCol, webTableHeader, strTableName, false, false);

						if (!(intColumnIndex1 != 0)) {
							ALMFunctions.ThrowException("Get index of column name",
									"Expected column name as " + strSecondCol + " shall be displayed",
									"Expected column name as " + strSecondCol + " is not displayed", true);
						}

						WebElement eleClick = eleRows.findElement(By.xpath("(.//td[" + intColumnIndex1 + "]//select)|(.//td[" + intColumnIndex1 + "]//span)"));


						String actualVal = eleClick.getText().trim();
						if (strExpectedColVal.contains("contains")) {
							strExpectedColVal = strExpectedColVal.replaceAll("contains", "");
							if (actualVal.contains(strExpectedColVal)) {
								blnClick = true;
								break;
							}

						} else if (actualVal.equals(strExpectedColVal.trim())) {
							blnClick = true;
							break;
						}

					}

				}

				intCurrentPage++;
			} while (!(blnClick || blnRecordNotFound));

			if (blnRecordNotFound) {
				ALMFunctions.ThrowException(strExpectedColVal, strExpectedColVal + " value should be display in table row",
						"Error - Specified Record " + strExpectedColVal + " is not found in the " + strSecondCol
						+ " column in table",
						true);

			}

			if (blnClick) {
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Record should be available in Table",
						strExpectedColVal + " value should be displayed",
						"Specified Record " + strExpectedColVal + " is found in " + strSecondCol + " column in table",
						true);

			} else {
				ALMFunctions.ThrowException("Click on " + strRefernceColVal + " Table row",
						"Created From Trigger : " + strActValue + " in " + strTableName
						+ " Table row should be clicked on " + strTableName + "",
						"Error - Specified file in table row is NOT clicked on " + strTableName + " Page", true);
			}

		}

	}
	/**
	 * Method to find record in Table for first column argument
	 * 
	 * @param strColumnName, Primary column name and secondary column name to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strValues,     Primary column value and secondary column value to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strPageName,   Page Name in which the control is available
	 * @return No return value
	 */
	public void findRecordAndVerifyinFilterPlainTableFirstColumn(String strColumnName, String strValue, String strPageName) {
		String strFirstCol = "";
		String strSecondCol = "";
		String strRefernceColVal = "";
		String strExpectedColVal = "";
		String strTableLocation = "";
		String strActValue = "";

		By row = Common.tableRow;
		By webTableHeader = null;
		int intColumnIndex = 0;
		String strTableName = "LookUp Table";


		String[] strColNames = strColumnName.split(":");
		String[] strValues = strValue.split("!");
		strFirstCol = strColNames[0];
		strSecondCol = strColNames[1];
		strRefernceColVal = strValues[0].trim();
		strExpectedColVal = strValues[1].trim();
		webTableHeader = Common.tableHeader;

		driverUtil.waitUntilStalenessOfElement(webTableHeader, lngMinTimeOutInSeconds, strPageName);

		intColumnIndex = getColumnIndex(strFirstCol, webTableHeader, strTableName, false, false) +1;

		if (!(intColumnIndex != 0)) {
			ALMFunctions.ThrowException("Get index of column name",
					"Expected column name as " + strFirstCol + " shall be displayed",
					"Expected column name as " + strFirstCol + " is not displayed", true);
		}


		boolean blnFound = false;
		boolean blnClick = false;
		int intCurrentPage = 1;
		List<WebElement> listtableRow = driver.getWebDriver().findElements(row);
		if (listtableRow.isEmpty()) {

			ALMFunctions.ThrowException(strRefernceColVal, strRefernceColVal + " table row should be displayed",
					strRefernceColVal + " table row are not displayed", true);
			ALMFunctions.ThrowException(strRefernceColVal, "" + strTableName + " Table row should be displayed",
					"" + strTableName + " Table row is NOT displayed", true);

		} else {

			boolean blnRecordNotFound = false;
			do {

				if (intCurrentPage != 1) {

					if (objectExists(Common.nextPage, "isDisplayed", lngMinTimeOutInSeconds, "Next", "Button",
							strTableName, false)) {
						click(Common.nextPage, lngMinTimeOutInSeconds, "Next", "Button", strTableName, false);
						driverUtil.waitUntilStalenessOfElement(Common.nextPage, strTableName);

					} else {
						blnRecordNotFound = true;
					}

				}

				listtableRow = driver.getWebDriver().findElements(row);
				WebElement eleRows = null;

				for (WebElement rows : listtableRow) {

					strActValue = rows.findElement(By.xpath("(.//td[" + intColumnIndex + "]//select)|(.//td[" + intColumnIndex + "]//span)")).getText().trim();

					if (strRefernceColVal.contains("contains")) {
						strRefernceColVal = strRefernceColVal.replaceAll("contains", "");
						if (strActValue.contains(strRefernceColVal)) {
							eleRows = rows;
							blnFound = true;

						}

					} else if (strActValue.equals(strRefernceColVal.trim())) {
						eleRows = rows;
						blnFound = true;

					}


					if (blnFound) {
						webTableHeader = null;
						int intColumnIndex1 = 0;
						webTableHeader = Common.tableHeader;

						intColumnIndex1 = getColumnIndex(strSecondCol, webTableHeader, strTableName, false, false) +1;

						if (!(intColumnIndex1 != 0)) {
							ALMFunctions.ThrowException("Get index of column name",
									"Expected column name as " + strSecondCol + " shall be displayed",
									"Expected column name as " + strSecondCol + " is not displayed", true);
						}

						WebElement eleClick = eleRows.findElement(By.xpath("(.//td[" + intColumnIndex1 + "]//select)|(.//td[" + intColumnIndex1 + "]//span)"));


						String actualVal = eleClick.getText().trim();
						if (strExpectedColVal.contains("contains")) {
							strExpectedColVal = strExpectedColVal.replaceAll("contains", "");
							if (actualVal.contains(strExpectedColVal)) {
								blnClick = true;
								break;
							}

						} else if (actualVal.equals(strExpectedColVal.trim())) {
							blnClick = true;
							break;
						}

					}

				}

				intCurrentPage++;
			} while (!(blnClick || blnRecordNotFound));

			if (blnRecordNotFound) {
				ALMFunctions.ThrowException(strExpectedColVal, strExpectedColVal + " value should be display in table row",
						"Error - Specified Record " + strExpectedColVal + " is not found in the " + strSecondCol
						+ " column in table",
						true);

			}

			if (blnClick) {
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Record should be available in Table",
						strExpectedColVal + " value should be displayed",
						"Specified Record " + strExpectedColVal + " is found in " + strSecondCol + " column in table",
						true);

			} else {
				ALMFunctions.ThrowException("Click on " + strRefernceColVal + " Table row",
						"Created From Trigger : " + strActValue + " in " + strTableName
						+ " Table row should be clicked on " + strTableName + "",
						"Error - Specified file in table row is NOT clicked on " + strTableName + " Page", true);
			}

		}

	}
	/**
	 * Method to find record in Table
	 * 
	 * @param strColumnName, Primary column name and secondary column name to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strValues,     Primary column value and secondary column value to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strPageName,   Page Name in which the control is available
	 * @return No return value
	 */
	public void findRecordAndVerifyinFilterTable(String strColumnName, String strValue, String strPageName) {
		String strFirstCol = "";
		String strSecondCol = "";
		String strRefernceColVal = "";
		String strExpectedColVal = "";
		String strTableLocation = "";
		String strActValue = "";

		By row = Common.tableRow;
		By webTableHeader = null;
		int intColumnIndex = 0;
		String strTableName = "LookUp Table";


		String[] strColNames = strColumnName.split(":");
		String[] strValues = strValue.split("!");
		strFirstCol = strColNames[0];
		strSecondCol = strColNames[1];
		strRefernceColVal = strValues[0].trim();
		strExpectedColVal = strValues[1].trim();
		webTableHeader = Common.tableHeader;

		driverUtil.waitUntilStalenessOfElement(webTableHeader, lngMinTimeOutInSeconds, strPageName);

		intColumnIndex = getColumnIndex(strFirstCol, webTableHeader, strTableName, false, false)+1;

		if (!(intColumnIndex != 0)) {
			ALMFunctions.ThrowException("Get index of column name",
					"Expected column name as " + strFirstCol + " shall be displayed",
					"Expected column name as " + strFirstCol + " is not displayed", true);
		}


		boolean blnFound = false;
		boolean blnClick = false;
		int intCurrentPage = 1;
		List<WebElement> listtableRow = driver.getWebDriver().findElements(row);
		if (listtableRow.isEmpty()) {

			ALMFunctions.ThrowException(strRefernceColVal, strRefernceColVal + " table row should be displayed",
					strRefernceColVal + " table row are not displayed", true);
			ALMFunctions.ThrowException(strRefernceColVal, "" + strTableName + " Table row should be displayed",
					"" + strTableName + " Table row is NOT displayed", true);

		} else {

			boolean blnRecordNotFound = false;
			do {

				if (intCurrentPage != 1) {

					if (objectExists(Common.nextPage, "isDisplayed", lngMinTimeOutInSeconds, "Next", "Button",
							strTableName, false)) {
						click(Common.nextPage, lngMinTimeOutInSeconds, "Next", "Button", strTableName, false);
						driverUtil.waitUntilStalenessOfElement(Common.nextPage, strTableName);

					} else {
						blnRecordNotFound = true;
					}

				}

				listtableRow = driver.getWebDriver().findElements(row);
				WebElement eleRows = null;

				for (WebElement rows : listtableRow) {

					strActValue = rows.findElement(By.xpath("(.//td[" + intColumnIndex + "]//select)|(.//td[" + intColumnIndex + "]//span)")).getText().trim();

					if (strRefernceColVal.contains("contains")) {
						strRefernceColVal = strRefernceColVal.replaceAll("contains", "");
						if (strActValue.contains(strRefernceColVal)) {
							eleRows = rows;
							blnFound = true;

						}

					} else if (strActValue.equals(strRefernceColVal.trim())) {
						eleRows = rows;
						blnFound = true;

					}


					if (blnFound) {
						webTableHeader = null;
						int intColumnIndex1 = 0;
						webTableHeader = Common.tableHeader;

						intColumnIndex1 = getColumnIndex(strSecondCol, webTableHeader, strTableName, false, false)+1;

						if (!(intColumnIndex1 != 0)) {
							ALMFunctions.ThrowException("Get index of column name",
									"Expected column name as " + strSecondCol + " shall be displayed",
									"Expected column name as " + strSecondCol + " is not displayed", true);
						}

						WebElement eleClick = eleRows.findElement(By.xpath("(.//td[" + intColumnIndex1 + "]//select)|(.//td[" + intColumnIndex1 + "]//span)"));


						String actualVal = eleClick.getText().trim();
						if (strExpectedColVal.contains("contains")) {
							strExpectedColVal = strExpectedColVal.replaceAll("contains", "");
							if (actualVal.contains(strExpectedColVal)) {
								blnClick = true;
								break;
							}

						} else if (actualVal.equals(strExpectedColVal.trim())) {
							blnClick = true;
							break;
						}

					}

				}

				intCurrentPage++;
			} while (!(blnClick || blnRecordNotFound));

			if (blnRecordNotFound) {
				ALMFunctions.ThrowException(strExpectedColVal, strExpectedColVal + " value should be display in table row",
						"Error - Specified Record " + strExpectedColVal + " is not found in the " + strSecondCol
						+ " column in table",
						true);

			}

			if (blnClick) {
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Record should be available in Table",
						strExpectedColVal + " value should be displayed",
						"Specified Record " + strExpectedColVal + " is found in " + strSecondCol + " column in table",
						true);

			} else {
				ALMFunctions.ThrowException("Click on " + strRefernceColVal + " Table row",
						"Created From Trigger : " + strActValue + " in " + strTableName
						+ " Table row should be clicked on " + strTableName + "",
						"Error - Specified file in table row is NOT clicked on " + strTableName + " Page", true);
			}

		}

	}

	/**
	 * Method to find record and verify in Table which are split in to two tables
	 * 
	 * @param strColumnName, Primary column name and secondary column name to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strValues,     Primary column value and secondary column value to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strPageName,   Page Name in which the control is available
	 * @return No return value
	 */
	@SuppressWarnings("unused")
	public void findRecordAndVerifyInSplitTable(String strColumnName, String strValue, String strPageName) {
		String strFirstCol = "";
		String strSecondCol = "";
		String strRefernceColVal = "";
		String strExpectedColVal = "";
		String strTableLocation = "";
		String strActValue = "";
		String strTableCondition = "First";
		WebElement eleRows = null;

		By webTableHeader = null;
		int intColumnIndex = 0;
		String strTableName = "LookUp Table";

		String[] strColNames = strColumnName.split(":");
		String[] strValues = strValue.split("!");
		strFirstCol = strColNames[0];
		strSecondCol = strColNames[1];

		strRefernceColVal = strValues[0].trim();
		strExpectedColVal = strValues[1].trim();

		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
		try {
			webTableHeader = Common.splitFirstTableHeader;
			//driverUtil.waitUntilStalenessOfElement(webTableHeader, lngMinTimeOutInSeconds, strPageName);
			intColumnIndex = getColumnIndex(strFirstCol, webTableHeader, strTableName, false, false) + 1;
		}
		catch(Exception e)
		{
			webTableHeader = Common.plainSplitFirstTableHeader;
			intColumnIndex = getColumnIndex(strFirstCol, webTableHeader, strTableName, false, false) + 1;
		}

		if(!(intColumnIndex != 0)) 
		{
			try{
				webTableHeader = Common.splitSecTableHeader;
				//driverUtil.waitUntilStalenessOfElement(webTableHeader, lngMinTimeOutInSeconds, strPageName);
				intColumnIndex = getColumnIndex(strFirstCol, webTableHeader, strTableName, false, false) + 1;
				strTableCondition = "Second";
			}
			catch(Exception e)
			{
				webTableHeader = Common.plainSplitSecTableHeader;
				//driverUtil.waitUntilStalenessOfElement(webTableHeader, lngMinTimeOutInSeconds, strPageName);
				intColumnIndex = getColumnIndex(strFirstCol, webTableHeader, strTableName, false, false) + 1;
				strTableCondition = "Second";	
			}
		}


		if (!(intColumnIndex != 0)) {
			ALMFunctions.ThrowException("Get index of column name",
					"Expected column name as " + strFirstCol + " shall be displayed",
					"Expected column name as " + strFirstCol + " is not displayed", true);
		}


		boolean blnFound = false;
		boolean blnClick = false;
		int intCurrentPage = 1;
		List<WebElement> listtableRow = null;
		if(strTableCondition.equalsIgnoreCase("First")) {
			By row = Common.splitFirstTableRow;
			listtableRow = driver.getWebDriver().findElements(row);
		}
		else
		{
			By row = Common.splitSecTableRow;
			listtableRow = driver.getWebDriver().findElements(row);
		}
		if (listtableRow.isEmpty()) {

			ALMFunctions.ThrowException(strRefernceColVal, strRefernceColVal + " table row should be displayed",
					strRefernceColVal + " table row are not displayed", true);
			ALMFunctions.ThrowException(strRefernceColVal, "" + strTableName + " Table row should be displayed",
					"" + strTableName + " Table row is NOT displayed", true);

		} else {

			boolean blnRecordNotFound = false;
			do {

				if (intCurrentPage != 1) {

					if (objectExists(Common.nextPage, "isDisplayed", lngMinTimeOutInSeconds, "Next", "Button",
							strTableName, false)) {
						click(Common.nextPage, lngMinTimeOutInSeconds, "Next", "Button", strTableName, false);
						driverUtil.waitUntilStalenessOfElement(Common.nextPage, strTableName);

					} else {
						blnRecordNotFound = true;
					}

				}
				//Identifying primary key value
				if(strTableCondition.equalsIgnoreCase("First")) {
					By row = Common.splitFirstTableRow;
					listtableRow = driver.getWebDriver().findElements(row);
				}
				else
				{
					By row = Common.splitSecTableRow;
					listtableRow = driver.getWebDriver().findElements(row);
				}
				//listtableRow = driver.getWebDriver().findElements(row);
				int Counter = 0;
				for (WebElement rows : listtableRow) {

					strActValue = rows.findElement(By.xpath("(.//td[" + intColumnIndex + "]//a)|(.//td[" + intColumnIndex + "]//span)")).getText().trim();
					Counter++;

					if (strActValue.equals(strRefernceColVal.trim()) || strRefernceColVal.contains(strActValue.trim())) {
						eleRows = rows;
						blnFound = true;

					}

					if (blnFound) {

						//second column details
						webTableHeader = null;
						int intColumnIndex1 = 0;

						webTableHeader = Common.splitFirstTableHeader;
						//driverUtil.waitUntilStalenessOfElement(webTableHeader, lngMinTimeOutInSeconds, strPageName);
						intColumnIndex1 = getColumnIndex(strSecondCol, webTableHeader, strTableName, false, false) + 1;

						if (!(intColumnIndex1 != 0)) 
						{
							webTableHeader = Common.splitSecTableHeader;
							//driverUtil.waitUntilStalenessOfElement(webTableHeader, lngMinTimeOutInSeconds, strPageName);
							intColumnIndex1 = getColumnIndex(strSecondCol, webTableHeader, strTableName, false, false) + 1;
							strTableCondition = "Second";
						}

						/*	intColumnIndex1 = getColumnIndex(strSecondCol, webTableHeader, strTableName, false, false)
								+ 1;*/

						if (!(intColumnIndex1 != 0)) {
							ALMFunctions.ThrowException("Get index of column name",
									"Expected column name as " + strSecondCol + " shall be displayed",
									"Expected column name as " + strSecondCol + " is not displayed", true);
						}
						WebElement eleClick = null;
						WebElement eleRow = null;
						if(strTableCondition.equalsIgnoreCase("First"))
						{
							eleClick = eleRows.findElement(By.xpath("(.//td[" + intColumnIndex1 + "]//a)|(.//td[" + intColumnIndex1 + "]//span)"));

						}
						else
						{
							String strIndex = String.valueOf(Counter);
							eleRow = driver.findElement(new Common(strIndex).splitSecTableRowByIndex);
							eleClick = eleRow.findElement(By.xpath("(.//td[" + intColumnIndex1 + "]//a)|(.//td[" + intColumnIndex1 + "]//span)"));
						}
						String actualVal = eleClick.getText().trim();
						if (actualVal.equals(strExpectedColVal.trim()) || strExpectedColVal.trim().contains(actualVal)) {
							pagescroll(eleClick, strPageName);
							driverUtil.waitUntilStalenessOfTableElement(eleClick, strPageName);
							//pagescroll(eleClick, strPageName);
							blnClick = true;
							break;
						}

					}

				}

				intCurrentPage++;
			} while (!(blnClick || blnRecordNotFound));

			if (blnRecordNotFound) {
				ALMFunctions.ThrowException(strExpectedColVal, strExpectedColVal + " value should be display in table row",
						"Error - Specified Record " + strExpectedColVal + " is not found in the " + strSecondCol
						+ " column in table",
						true);

			}

			if (blnClick) {
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Record should be available in Table",
						"'"+strExpectedColVal + "' value should be displayed in '" +strSecondCol +"' Column",
						"Specified Record '" + strExpectedColVal + "' is found in '" + strSecondCol + "' column in table",
						true);

			} else {
				ALMFunctions.ThrowException("Record should be available in Table",
						"'"+strExpectedColVal + "' value should be displayed in '" +strSecondCol +"' Column",
						"Specified Record '" + strExpectedColVal + "' is not found in '" + strSecondCol + "' column in table",
						true);
			}

		}

	}


	/**
	 * Method to retrieve record from Table which is split in to two tables and Store it in Excel 
	 * 
	 * @param strColumnName, Primary column name and secondary column name to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strValues,     Primary column value and secondary column value to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strPageName,   Page Name in which the control is available
	 * @return No return value
	 */
	@SuppressWarnings("unused")
	public void retrieveRecordFromSplitTable(String strColumnName, String strValue, String strPageName) {
		String strFirstCol = "";
		String strSecondCol = "";
		String strRefernceColVal = "";
		String strExpectedColVal = "";
		String strTableLocation = "";
		String strActValue = "";
		String strExcelColumnName="";
		String strRetrievedVal="";
		String strTableCondition = "First";
		WebElement eleRows = null;

		By webTableHeader = null;
		int intColumnIndex = 0;
		String strTableName = "LookUp Table";

		String[] strColNames = strColumnName.split(":");
		String[] strValues = strValue.split("!");
		strFirstCol = strColNames[0];
		strSecondCol = strColNames[1];
		strRefernceColVal = strValues[0].trim();
		strExcelColumnName = strValues[1].trim();

		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);

		webTableHeader = Common.splitFirstTableHeader;
		driverUtil.waitUntilStalenessOfElement(webTableHeader, staleTableTimeOut, strPageName);
		intColumnIndex = getColumnIndex(strFirstCol, webTableHeader, strTableName, false, false) + 1;

		if (!(intColumnIndex != 0)) 
		{
			webTableHeader = Common.splitSecTableHeader;
			driverUtil.waitUntilStalenessOfElement(webTableHeader, staleTableTimeOut, strPageName);
			intColumnIndex = getColumnIndex(strFirstCol, webTableHeader, strTableName, false, false) + 1;
			strTableCondition = "Second";
		}

		if (!(intColumnIndex != 0)) {
			ALMFunctions.ThrowException("Get index of column name",
					"Expected column name as " + strFirstCol + " shall be displayed",
					"Expected column name as " + strFirstCol + " is not displayed", true);
		}

		boolean blnFound = false;
		boolean blnClick = false;
		int intCurrentPage = 1;
		List<WebElement> listtableRow = null;
		if(strTableCondition.equalsIgnoreCase("First")) {
			By row = Common.splitFirstTableRow;
			listtableRow = driver.getWebDriver().findElements(row);
		}
		else
		{
			By row = Common.splitSecTableRow;
			listtableRow = driver.getWebDriver().findElements(row);
		}
		if (listtableRow.isEmpty()) {

			ALMFunctions.ThrowException(strRefernceColVal, strRefernceColVal + " table row should be displayed",
					strRefernceColVal + " table row are not displayed", true);
			ALMFunctions.ThrowException(strRefernceColVal, "" + strTableName + " Table row should be displayed",
					"" + strTableName + " Table row is NOT displayed", true);

		} else {

			boolean blnRecordNotFound = false;
			do {

				if (intCurrentPage != 1) {

					if (objectExists(Common.nextPage, "isDisplayed", lngMinTimeOutInSeconds, "Next", "Button",
							strTableName, false)) {
						click(Common.nextPage, lngMinTimeOutInSeconds, "Next", "Button", strTableName, false);
						driverUtil.waitUntilStalenessOfElement(Common.nextPage, strTableName);

					} else {
						blnRecordNotFound = true;
					}

				}
				//Identifying primary key value
				if(strTableCondition.equalsIgnoreCase("First")) {
					By row = Common.splitFirstTableRow;
					listtableRow = driver.getWebDriver().findElements(row);
				}
				else
				{
					By row = Common.splitSecTableRow;
					listtableRow = driver.getWebDriver().findElements(row);
				}
				int Counter = 0;
				for (WebElement rows : listtableRow) {

					strActValue = rows.findElement(By.xpath("(.//td[" + intColumnIndex + "]//a)|(.//td[" + intColumnIndex + "]//span)")).getText().trim();
					Counter++;

					if (strActValue.equals(strRefernceColVal.trim())) {

						eleRows = rows;
						blnFound = true;

					}

					if (blnFound) {

						//second column details
						webTableHeader = null;
						int intColumnIndex1 = 0;

						webTableHeader = Common.splitFirstTableHeader;
						driverUtil.waitUntilStalenessOfElement(webTableHeader, staleTableTimeOut, strPageName);
						intColumnIndex1 = getColumnIndex(strSecondCol, webTableHeader, strTableName, false, false) + 1;

						if (!(intColumnIndex1 != 0)) 
						{
							webTableHeader = Common.splitSecTableHeader;
							driverUtil.waitUntilStalenessOfElement(webTableHeader, staleTableTimeOut, strPageName);
							intColumnIndex1 = getColumnIndex(strSecondCol, webTableHeader, strTableName, false, false) + 1;
							strTableCondition="Second";
						}

						if (!(intColumnIndex1 != 0)) {
							ALMFunctions.ThrowException("Get index of column name",
									"Expected column name as " + strSecondCol + " shall be displayed",
									"Expected column name as " + strSecondCol + " is not displayed", true);
						}
						WebElement eleClick = null;
						WebElement eleRow = null;
						if(strTableCondition.equalsIgnoreCase("First"))
						{
							eleClick = eleRows.findElement(By.xpath("(.//td[" + intColumnIndex1 + "]//a)|(.//td[" + intColumnIndex1 + "]//span)"));

						}
						else
						{
							String strIndex = String.valueOf(Counter);
							eleRow = driver.findElement(new Common(strIndex).splitSecTableRowByIndex);
							eleClick = eleRow.findElement(By.xpath("(.//td[" + intColumnIndex1 + "]//a)|(.//td[" + intColumnIndex1 + "]//span)"));
						}
						strRetrievedVal = eleClick.getText().trim();
						if (!strRetrievedVal.isEmpty()) {
							dataTable.putData("Parametrized_Checkpoints", strExcelColumnName, strRetrievedVal);
							blnClick = true;
						}


					}

				}

				intCurrentPage++;
			} while (!(blnClick || blnRecordNotFound));


			if (blnClick) {

				report.updateTestLog("Capture the " + strSecondCol + " value from table",
						"'"+strSecondCol + "' value should be captured from table",
						"captured Value from table for '" + strSecondCol + "' : '" + strRetrievedVal+"'", Status.DONE);

			} else {
				ALMFunctions.ThrowException(strSecondCol, strSecondCol + " value should be display in table row",
						"Error - Specified Record " + strSecondCol + " is not found in the " + strTableName + " table",
						true);
			}

		}

	}

	/**
	 * Method to select a value in dialog dropdown
	 * 
	 * @param windowName, value of the window name
	 * @param strLabel,    Label of the Drop down
	 * @param strValue,    value to be selected in Drop down
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */
	public void selectDialogDropDown(String strWindowName, String strLabel, String strValue, String strPageName) {
		try {

			Common button = new Common(strWindowName, strLabel);
			if (objectExists(button.dialogDropDown, "isDisplayed", lngMinTimeOutInSeconds, strValue, "Popup Button",
					strPageName, false)) {
				driverUtil.waitUntilStalenessOfElement(button.dialogDropDown, strPageName);
				selectListItem(button.dialogDropDown, lngMinTimeOutInSeconds, new String[] { strValue }, strLabel,
						strPageName, "Value");
			}


			else {
				ALMFunctions.ThrowException("Dialog DropDown", strLabel + " should be displayed in " + strPageName,
						"Error - " + strLabel + " is not available in " + strPageName, true);
			}
		}

		catch (Exception e) {
			ALMFunctions.ThrowException("Dialog DropDown", strLabel + " should be displayed in " + strPageName,
					"Error - " + strLabel + " is not available in " + strPageName, true);
		}
	}

	/**
	 * Method to select a value in dropdown
	 * 
	 * @param windowName, value of the window name
	 * @param strLabel,    Label of the Drop down
	 * @param strValue,    value to be selected in Drop down
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */
	public void selectDropDown(String strLabel, String strValue, String strPageName) {
		try {

			Common dropdown = new Common(strLabel);
			if (objectExists(dropdown.dropdown, "isDisplayed", lngMinTimeOutInSeconds, strValue, "Popup Button",
					strPageName, false)) {
				driverUtil.waitUntilStalenessOfElement(dropdown.dropdown, strPageName);
				selectListItem(dropdown.dropdown, lngMinTimeOutInSeconds, new String[] { strValue }, strLabel,
						strPageName, "Value");
			}


			else {
				ALMFunctions.ThrowException("DropDown", strLabel + " should be displayed in " + strPageName,
						"Error - " + strLabel + " is not available in " + strPageName, true);
			}
		}

		catch (Exception e) {
			ALMFunctions.ThrowException("DropDown", strLabel + " should be displayed in " + strPageName,
					"Error - " + strLabel + " is not available in " + strPageName, true);
		}
	}

	/**
	 * Method to enter record in Table which is split in to two tables 
	 * 
	 * @param strColumnName, Primary column name and secondary column name to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strValues,     Primary column value and secondary column value to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strPageName,   Page Name in which the control is available
	 * @return No return value
	 *//*
	@SuppressWarnings("unused")
	public void enterValueInSplitTable(String strColumnName, String strValue, String strPageName) {
		String strFirstCol = "";
		String strSecondCol = "";
		String strFirstColCondition = "";
		String strSecondColCondition = "";
		String strRefernceColVal = "";
		String strExpectedColVal = "";
		String strTableLocation = "";
		String strActValue = "";
		String strObjectType="";
		String strValueToBeEntered="";
		String strRetrievedVal="";
		WebElement eleRows = null;

		By webTableHeader = null;
		int intColumnIndex = 0;
		String strTableName = "LookUp Table";

		String[] strColNames = strColumnName.split(":");
		String[] strPrimaryColNameCondition = strColNames[0].split("-");
		String[] strSecondaryColNameCondition = strColNames[1].split("-");
		String[] strValues = strValue.split("!");
		strFirstCol = strPrimaryColNameCondition[0];
		strFirstColCondition = strPrimaryColNameCondition[1];
		strSecondCol = strSecondaryColNameCondition[0];
		strSecondColCondition = strSecondaryColNameCondition[1];
		strRefernceColVal = strValues[0].trim();
		strValueToBeEntered = strValues[1].trim();
		strObjectType = strValues[2].trim();


		if(strFirstColCondition.equalsIgnoreCase("First"))
		{
			webTableHeader = Common.splitFirstTableHeader;
			//driverUtil.waitUntilStalenessOfElement(webTableHeader, staleTableTimeOut, strPageName);
			intColumnIndex = getColumnIndex(strFirstCol, webTableHeader, strTableName, false, false) + 1;
		}
		else
		{
			webTableHeader = Common.splitSecTableHeader;
			//driverUtil.waitUntilStalenessOfElement(webTableHeader, staleTableTimeOut, strPageName);
			intColumnIndex = getColumnIndex(strFirstCol, webTableHeader, strTableName, false, false) + 1;
		}

		if (!(intColumnIndex != 0)) {
			ALMFunctions.ThrowException("Get index of column name",
					"Expected column name as " + strFirstCol + " shall be displayed",
					"Expected column name as " + strFirstCol + " is not displayed", true);
		}

		boolean blnFound = false;
		boolean blnClick = false;
		int intCurrentPage = 1;
		List<WebElement> listtableRow = null;
		if(strFirstColCondition.equalsIgnoreCase("First")) {
			By row = Common.splitFirstTableRow;
			listtableRow = driver.getWebDriver().findElements(row);
		}
		else
		{
			By row = Common.splitSecTableRow;
			listtableRow = driver.getWebDriver().findElements(row);
		}
		if (listtableRow.isEmpty()) {

			ALMFunctions.ThrowException(strRefernceColVal, strRefernceColVal + " table row should be displayed",
					strRefernceColVal + " table row are not displayed", true);
			ALMFunctions.ThrowException(strRefernceColVal, "" + strTableName + " Table row should be displayed",
					"" + strTableName + " Table row is NOT displayed", true);

		} else {

			boolean blnRecordNotFound = false;
			do {

				if (intCurrentPage != 1) {

					if (objectExists(Common.nextPage, "isDisplayed", lngMinTimeOutInSeconds, "Next", "Button",
							strTableName, false)) {
						click(Common.nextPage, lngMinTimeOutInSeconds, "Next", "Button", strTableName, false);
						driverUtil.waitUntilStalenessOfElement(Common.nextPage, strTableName);

					} else {
						blnRecordNotFound = true;
					}

				}
				//Identifying primary key value
				if(strFirstColCondition.equalsIgnoreCase("First")) {
					By row = Common.splitFirstTableRow;
					listtableRow = driver.getWebDriver().findElements(row);
				}
				else
				{
					By row = Common.splitSecTableRow;
					listtableRow = driver.getWebDriver().findElements(row);
				}

				int Counter = 0;
				for (WebElement rows : listtableRow) {

					WebElement elmRowValue = rows.findElement(By.xpath("(.//td[" + intColumnIndex + "]//a)|(.//td[" + intColumnIndex + "]//span)"));
					pagescroll(elmRowValue, driver.getTitle());
					driverUtil.waitUntilStalenessOfTableElement(elmRowValue, strPageName);
					strActValue = rows.findElement(By.xpath("(.//td[" + intColumnIndex + "]//a)|(.//td[" + intColumnIndex + "]//span)")).getText().trim();
					Counter++;

					if (strActValue.equals(strRefernceColVal.trim())) {

						eleRows = rows;
						blnFound = true;

					}

					if (blnFound) {

						//second column details
						webTableHeader = null;
						int intColumnIndex1 = 0;
						if(strSecondColCondition.equalsIgnoreCase("First"))
						{
							webTableHeader = Common.splitFirstTableHeader;
							intColumnIndex1 = getColumnIndex(strSecondCol, webTableHeader, strTableName, false, false) + 1;
						}
						else
						{
							webTableHeader = Common.splitSecTableHeader;
							intColumnIndex1 = getColumnIndex(strSecondCol, webTableHeader, strTableName, false, false) + 1;
						}

							intColumnIndex1 = getColumnIndex(strSecondCol, webTableHeader, strTableName, false, false)
								+ 1;

						if (!(intColumnIndex1 != 0)) {
							ALMFunctions.ThrowException("Get index of column name",
									"Expected column name as " + strSecondCol + " shall be displayed",
									"Expected column name as " + strSecondCol + " is not displayed", true);
						}
						WebElement eleClick = null;
						WebElement eleRow = null;
						if(strSecondColCondition.equalsIgnoreCase("First"))
						{
							if(strObjectType.equalsIgnoreCase("dropdown"))
							{
								eleClick = eleRows.findElement(By.xpath(".//td[" + intColumnIndex1 + "]//select"));
								Select dropdownval = new Select(eleClick);
								dropdownval.selectByVisibleText(strValueToBeEntered);
								blnClick = true;
								break;	
							}
							else if(strObjectType.equalsIgnoreCase("date"))
							{
								eleClick = eleRows.findElement(By.xpath(".//td[" + intColumnIndex1 + "]//input")); 
								clear(eleClick, lngMinTimeOutInSeconds, strSecondCol, strPageName);
								click(eleClick, lngMinTimeOutInSeconds, strValueToBeEntered, strSecondCol, strPageName, false);
								sendkeys(eleClick, lngMinTimeOutInSeconds, strValueToBeEntered, strSecondCol, strPageName,false);
								sendkeys(eleClick, lngMinTimeOutInSeconds, Keys.TAB, strSecondCol, strPageName,false);
								blnClick = true;
								break;
							}
							else
							{
								ALMFunctions.ThrowException("Test Data",
										"Only Pre-Defined Fields Type must be provided in the test data sheet",
										"Error - Unhandled Field Type " + strObjectType, false);
							}


						}
						else
						{
							String strIndex = String.valueOf(Counter);
							eleRow = driver.findElement(new Common(strIndex).splitSecTableRowByIndex);
							//eleClick = eleRow.findElement(By.xpath("(.//td[" + intColumnIndex1 + "]//a)|(.//td[" + intColumnIndex1 + "]//span)"));
							if(strObjectType.equalsIgnoreCase("dropdown"))
							{
								eleClick = eleRow.findElement(By.xpath(".//td[" + intColumnIndex1 + "]//select"));
								Select dropdownval = new Select(eleClick);
								dropdownval.selectByVisibleText(strValueToBeEntered);
								blnClick = true;
								break;	
							}
							else if(strObjectType.equalsIgnoreCase("date"))
							{
								eleClick = eleRow.findElement(By.xpath(".//td[" + intColumnIndex1 + "]//input"));
								clear(eleClick, lngMinTimeOutInSeconds, strSecondCol, strPageName);
								click(eleClick, lngMinTimeOutInSeconds, strValueToBeEntered, strSecondCol, strPageName, false);
								sendkeys(eleClick, lngMinTimeOutInSeconds, strValueToBeEntered, strSecondCol, strPageName,false);
								sendkeys(eleClick, lngMinTimeOutInSeconds, Keys.TAB, strSecondCol, strPageName,false);
								blnClick = true;
								break;
							}
							else
							{
								ALMFunctions.ThrowException("Test Data",
										"Only Pre-Defined Fields Type must be provided in the test data sheet",
										"Error - Unhandled Field Type " + strObjectType, false);
							}


						}



					}

				}

				intCurrentPage++;
			} while (!(blnClick || blnRecordNotFound));


			if (blnClick) {

				report.updateTestLog("Enter the '" + strSecondCol + "' value from table","'"+
						strSecondCol + "' value should be entered in table",
						"Entered Value from table for " + strSecondCol + " is : " + strValueToBeEntered 
						+" in the row of:'"+strRefernceColVal+"'" , Status.DONE);

			} else {
				ALMFunctions.ThrowException(strSecondCol, strSecondCol + " value should be display in table row",
						"Error - Specified Record " + strSecondCol + " is not found in the " + strTableName + " table",
						true);
			}

		}

	}*/


	/**
	 * Method to enter record in Table which is split in to two tables 
	 * 
	 * @param strColumnName, Primary column name and secondary column name to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strValues,     Primary column value and secondary column value to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strPageName,   Page Name in which the control is available
	 * @return No return value
	 */
	@SuppressWarnings("unused")
	public void enterValueInSplitTable(String strColumnName, String strValue, String strPageName) {
		String strFirstCol = "";
		String strSecondCol = "";
		String strFirstColCondition = "";
		String strSecondColCondition = "";
		String strRefernceColVal = "";
		String strExpectedColVal = "";
		String strTableLocation = "";
		String strActValue = "";
		String strObjectType="";
		String strValueToBeEntered="";
		String strRetrievedVal="";
		WebElement eleRows = null;
		String strTableLoc="First";

		By webTableHeader = null;
		int intColumnIndex = 0;
		String strTableName = "LookUp Table";

		String[] strColNames = strColumnName.split(":");
		String[] strValues = strValue.split("!");
		strFirstCol = strColNames[0];
		strSecondCol = strColNames[1];
		/*
	//	strFirstColCondition = strPrimaryColNameCondition[1];

	//	strSecondColCondition = strSecondaryColNameCondition[1];
		 */		strRefernceColVal = strValues[0].trim();
		 strValueToBeEntered = strValues[1].trim();
		 strObjectType = strValues[2].trim();



		 webTableHeader = Common.splitFirstTableHeader;
		 driverUtil.waitUntilStalenessOfElement(webTableHeader, staleTableTimeOut, strPageName);
		 intColumnIndex = getColumnIndex(strFirstCol, webTableHeader, strTableName, false, false) + 1;
		 if (!(intColumnIndex != 0)) {
			 webTableHeader = Common.splitSecTableHeader;
			 //driverUtil.waitUntilStalenessOfElement(webTableHeader, staleTableTimeOut, strPageName);
			 intColumnIndex = getColumnIndex(strFirstCol, webTableHeader, strTableName, false, false) + 1;
			 strTableLoc = "Second";
		 }


		 if (!(intColumnIndex != 0)) {
			 ALMFunctions.ThrowException("Get index of column name",
					 "Expected column name as " + strFirstCol + " shall be displayed",
					 "Expected column name as " + strFirstCol + " is not displayed", true);
		 }

		 boolean blnFound = false;
		 boolean blnClick = false;
		 int intCurrentPage = 1;
		 List<WebElement> listtableRow = null;
		 if(strTableLoc.equalsIgnoreCase("First")) {
			 By row = Common.splitFirstTableRow;
			 listtableRow = driver.getWebDriver().findElements(row);
		 }
		 else
		 {
			 By row = Common.splitSecTableRow;
			 listtableRow = driver.getWebDriver().findElements(row);
		 }
		 if (listtableRow.isEmpty()) {

			 ALMFunctions.ThrowException(strRefernceColVal, strRefernceColVal + " table row should be displayed",
					 strRefernceColVal + " table row are not displayed", true);
			 ALMFunctions.ThrowException(strRefernceColVal, "" + strTableName + " Table row should be displayed",
					 "" + strTableName + " Table row is NOT displayed", true);

		 } else {

			 boolean blnRecordNotFound = false;
			 do {

				 if (intCurrentPage != 1) {

					 if (objectExists(Common.nextPage, "isDisplayed", lngMinTimeOutInSeconds, "Next", "Button",
							 strTableName, false)) {
						 click(Common.nextPage, lngMinTimeOutInSeconds, "Next", "Button", strTableName, false);
						 driverUtil.waitUntilStalenessOfElement(Common.nextPage, strTableName);

					 } else {
						 blnRecordNotFound = true;
					 }

				 }
				 //Identifying primary key value
				 if(strTableLoc.equalsIgnoreCase("First")) {
					 By row = Common.splitFirstTableRow;
					 listtableRow = driver.getWebDriver().findElements(row);
				 }
				 else
				 {
					 By row = Common.splitSecTableRow;
					 listtableRow = driver.getWebDriver().findElements(row);
				 }

				 int Counter = 0;
				 for (WebElement rows : listtableRow) {

					 WebElement elmRowValue = rows.findElement(By.xpath("(.//td[" + intColumnIndex + "]//a)|(.//td[" + intColumnIndex + "]//span)"));
					 pagescroll(elmRowValue, driver.getTitle());
					 driverUtil.waitUntilStalenessOfTableElement(elmRowValue, strPageName);
					 strActValue = rows.findElement(By.xpath("(.//td[" + intColumnIndex + "]//a)|(.//td[" + intColumnIndex + "]//span)")).getText().trim();
					 Counter++;

					 if (strActValue.equals(strRefernceColVal.trim())) {

						 eleRows = rows;
						 blnFound = true;

					 }

					 if (blnFound) {

						 //second column details
						 webTableHeader = null;
						 int intColumnIndex1 = 0;

						 webTableHeader = Common.splitFirstTableHeader;
						 driverUtil.waitUntilStalenessOfElement(webTableHeader, staleTableTimeOut, strPageName);
						 intColumnIndex1 = getColumnIndex(strSecondCol, webTableHeader, strTableName, false, false) + 1;
						 if(!(intColumnIndex1 != 0))
						 {
							 webTableHeader = Common.splitSecTableHeader;
							 intColumnIndex1 = getColumnIndex(strSecondCol, webTableHeader, strTableName, false, false) + 1;
							 strTableLoc="Second";
						 }


						 if (!(intColumnIndex1 != 0)) {
							 ALMFunctions.ThrowException("Get index of column name",
									 "Expected column name as " + strSecondCol + " shall be displayed",
									 "Expected column name as " + strSecondCol + " is not displayed", true);
						 }
						 WebElement eleClick = null;
						 WebElement eleRow = null;
						 if(strTableLoc.equalsIgnoreCase("First"))
						 {
							 if(strObjectType.equalsIgnoreCase("dropdown"))
							 {
								 eleClick = eleRows.findElement(By.xpath(".//td[" + intColumnIndex1 + "]//select"));
								 Select dropdownval = new Select(eleClick);
								 dropdownval.selectByVisibleText(strValueToBeEntered);
								 blnClick = true;
								 break;	
							 }
							 else if(strObjectType.equalsIgnoreCase("date"))
							 {
								 eleClick = eleRows.findElement(By.xpath(".//td[" + intColumnIndex1 + "]//input")); 
								 clear(eleClick, lngMinTimeOutInSeconds, strSecondCol, strPageName);
								 click(eleClick, lngMinTimeOutInSeconds, strValueToBeEntered, strSecondCol, strPageName, false);
								 sendkeys(eleClick, lngMinTimeOutInSeconds, strValueToBeEntered, strSecondCol, strPageName,false);
								 sendkeys(eleClick, lngMinTimeOutInSeconds, Keys.TAB, strSecondCol, strPageName,false);
								 blnClick = true;
								 break;
							 }
							 else
							 {
								 ALMFunctions.ThrowException("Test Data",
										 "Only Pre-Defined Fields Type must be provided in the test data sheet",
										 "Error - Unhandled Field Type " + strObjectType, false);
							 }


						 }
						 else
						 {
							 String strIndex = String.valueOf(Counter);
							 eleRow = driver.findElement(new Common(strIndex).splitSecTableRowByIndex);
							 //eleClick = eleRow.findElement(By.xpath("(.//td[" + intColumnIndex1 + "]//a)|(.//td[" + intColumnIndex1 + "]//span)"));
							 if(strObjectType.equalsIgnoreCase("dropdown"))
							 {
								 eleClick = eleRow.findElement(By.xpath(".//td[" + intColumnIndex1 + "]//select"));
								 Select dropdownval = new Select(eleClick);
								 dropdownval.selectByVisibleText(strValueToBeEntered);
								 blnClick = true;
								 break;	
							 }
							 else if(strObjectType.equalsIgnoreCase("date"))
							 {
								 eleClick = eleRow.findElement(By.xpath(".//td[" + intColumnIndex1 + "]//input"));
								 clear(eleClick, lngMinTimeOutInSeconds, strSecondCol, strPageName);
								 click(eleClick, lngMinTimeOutInSeconds, strValueToBeEntered, strSecondCol, strPageName, false);
								 sendkeys(eleClick, lngMinTimeOutInSeconds, strValueToBeEntered, strSecondCol, strPageName,false);
								 sendkeys(eleClick, lngMinTimeOutInSeconds, Keys.TAB, strSecondCol, strPageName,false);
								 blnClick = true;
								 break;
							 }
							 else
							 {
								 ALMFunctions.ThrowException("Test Data",
										 "Only Pre-Defined Fields Type must be provided in the test data sheet",
										 "Error - Unhandled Field Type " + strObjectType, false);
							 }


						 }



					 }

				 }

				 intCurrentPage++;
			 } while (!(blnClick || blnRecordNotFound));


			 if (blnClick) {

				 report.updateTestLog("Enter the '" + strSecondCol + "' value from table","'"+
						 strSecondCol + "' value should be entered in table",
						 "Entered Value from table for " + strSecondCol + " is : " + strValueToBeEntered 
						 +" in the row of:'"+strRefernceColVal+"'" , Status.DONE);

			 } else {
				 ALMFunctions.ThrowException(strSecondCol, strSecondCol + " value should be display in table row",
						 "Error - Specified Record " + strSecondCol + " is not found in the " + strTableName + " table",
						 true);
			 }

		 }

	}

	/**
	 * Method to enter record in Table which is split in to two tables 
	 * 
	 * @param strColumnName, Primary column name and secondary column name to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strValues,     Primary column value and secondary column value to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strPageName,   Page Name in which the control is available
	 * @return No return value
	 */
	@SuppressWarnings("unused")
	public void enterValueInSplitTableWithMultipleFilter(String strColumnName, String strValue, String strPageName) {
		String strFirstCol = "";
		String strSecondCol = "";
		String strFirstColCondition = "";
		String strSecondColCondition = "";
		String strRefernceColVal = "";
		String strExpectedColVal = "";
		String strTableLocation = "";
		String strActValue = "";
		String strObjectType="";
		String strValueToBeEntered="";
		String strRetrievedVal="";
		WebElement eleRows = null;
		String strTableLoc="First";

		By webTableHeader = null;
		int intColumnIndex = 0;
		String strTableName = "LookUp Table";

		String[] strColNames = strColumnName.split("!");
		String[] strValues = strValue.split("!");
		strFirstCol = strColNames[0];
		strSecondCol = strColNames[1];
		/*
	//	strFirstColCondition = strPrimaryColNameCondition[1];

	//	strSecondColCondition = strSecondaryColNameCondition[1];
		 */		strRefernceColVal = strValues[0].trim();
		 strValueToBeEntered = strValues[1].trim();
		 strObjectType = strValues[2].trim();

		 driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
		 webTableHeader = Common.plainSplitFirstTableHeader;
		 driverUtil.waitUntilStalenessOfElement(webTableHeader, lngMinTimeOutInSeconds, strPageName);
		 intColumnIndex = getColumnIndex(strFirstCol, webTableHeader, strTableName, false, false) + 1;

		 if (!(intColumnIndex != 0)) {

			 webTableHeader = Common.plainSplitSecTableHeader;
			 //driverUtil.waitUntilStalenessOfElement(webTableHeader, lngMinTimeOutInSeconds, strPageName);
			 intColumnIndex = getColumnIndex(strFirstCol, webTableHeader, strTableName, false, false) + 1;
			 strTableLoc = "Second";	

		 }


		 if (!(intColumnIndex != 0)) {
			 ALMFunctions.ThrowException("Get index of column name",
					 "Expected column name as " + strFirstCol + " shall be displayed",
					 "Expected column name as " + strFirstCol + " is not displayed", true);
		 }

		 boolean blnFound = false;
		 boolean blnClick = false;
		 int intCurrentPage = 1;
		 List<WebElement> listtableRow = null;
		 if(strTableLoc.equalsIgnoreCase("First")) {
			 By row = Common.splitFirstTableRowWithMultiFilter;
			 listtableRow = driver.getWebDriver().findElements(row);
		 }
		 else
		 {
			 By row = Common.splitSecTableRowWithMultiFilter;
			 listtableRow = driver.getWebDriver().findElements(row);
		 }
		 if (listtableRow.isEmpty()) {

			 ALMFunctions.ThrowException(strRefernceColVal, strRefernceColVal + " table row should be displayed",
					 strRefernceColVal + " table row are not displayed", true);
			 ALMFunctions.ThrowException(strRefernceColVal, "" + strTableName + " Table row should be displayed",
					 "" + strTableName + " Table row is NOT displayed", true);

		 } else {

			 boolean blnRecordNotFound = false;
			 do {

				 if (intCurrentPage != 1) {

					 if (objectExists(Common.nextPage, "isDisplayed", lngMinTimeOutInSeconds, "Next", "Button",
							 strTableName, false)) {
						 click(Common.nextPage, lngMinTimeOutInSeconds, "Next", "Button", strTableName, false);
						 driverUtil.waitUntilStalenessOfElement(Common.nextPage, strTableName);

					 } else {
						 blnRecordNotFound = true;
					 }

				 }
				 //Identifying primary key value
				 if(strTableLoc.equalsIgnoreCase("First")) {
					 By row = Common.splitFirstTableRowWithMultiFilter;
					 listtableRow = driver.getWebDriver().findElements(row);
				 }
				 else
				 {
					 By row = Common.splitSecTableRowWithMultiFilter;
					 listtableRow = driver.getWebDriver().findElements(row);
				 }

				 int Counter = 0;
				 for (WebElement rows : listtableRow) {

					 WebElement elmRowValue = rows.findElement(By.xpath("(.//td[" + intColumnIndex + "]//a)|(.//td[" + intColumnIndex + "]//span)"));
					 pagescroll(elmRowValue, driver.getTitle());
					 driverUtil.waitUntilStalenessOfTableElement(elmRowValue, strPageName);
					 strActValue = rows.findElement(By.xpath("(.//td[" + intColumnIndex + "]//a)|(.//td[" + intColumnIndex + "]//span)")).getText().trim();
					 Counter++;

					 if (strActValue.equals(strRefernceColVal.trim())) {

						 eleRows = rows;
						 blnFound = true;

					 }

					 if (blnFound) {

						 //second column details
						 webTableHeader = null;
						 int intColumnIndex1 = 0;

						 webTableHeader = Common.plainSplitFirstTableHeader;
						 driverUtil.waitUntilStalenessOfElement(webTableHeader, staleTableTimeOut, strPageName);
						 intColumnIndex1 = getColumnIndex(strSecondCol, webTableHeader, strTableName, false, false) + 1;
						 if(!(intColumnIndex1 != 0))
						 {
							 webTableHeader = Common.plainSplitSecTableHeader;
							 intColumnIndex1 = getColumnIndex(strSecondCol, webTableHeader, strTableName, false, false) + 1;
							 strTableLoc="Second";
						 }


						 if (!(intColumnIndex1 != 0)) {
							 ALMFunctions.ThrowException("Get index of column name",
									 "Expected column name as " + strSecondCol + " shall be displayed",
									 "Expected column name as " + strSecondCol + " is not displayed", true);
						 }
						 WebElement eleClick = null;
						 WebElement eleRow = null;
						 if(strTableLoc.equalsIgnoreCase("First"))
						 {
							 if(strObjectType.equalsIgnoreCase("dropdown"))
							 {
								 eleClick = eleRows.findElement(By.xpath(".//td[" + intColumnIndex1 + "]//select"));
								 Select dropdownval = new Select(eleClick);
								 dropdownval.selectByVisibleText(strValueToBeEntered);
								 blnClick = true;
								 break;	
							 }
							 else if(strObjectType.equalsIgnoreCase("date"))
							 {
								 eleClick = eleRows.findElement(By.xpath(".//td[" + intColumnIndex1 + "]//input")); 
								 clear(eleClick, lngMinTimeOutInSeconds, strSecondCol, strPageName);
								 click(eleClick, lngMinTimeOutInSeconds, strValueToBeEntered, strSecondCol, strPageName, false);
								 sendkeys(eleClick, lngMinTimeOutInSeconds, strValueToBeEntered, strSecondCol, strPageName,false);
								 sendkeys(eleClick, lngMinTimeOutInSeconds, Keys.TAB, strSecondCol, strPageName,false);
								 blnClick = true;
								 break;
							 }
							 else
							 {
								 ALMFunctions.ThrowException("Test Data",
										 "Only Pre-Defined Fields Type must be provided in the test data sheet",
										 "Error - Unhandled Field Type " + strObjectType, false);
							 }


						 }
						 else
						 {
							 String strIndex = String.valueOf(Counter);
							 eleRow = driver.findElement(new Common(strIndex).splitSecTableWithMultiFilterRowByIndex);
							 //eleClick = eleRow.findElement(By.xpath("(.//td[" + intColumnIndex1 + "]//a)|(.//td[" + intColumnIndex1 + "]//span)"));
							 if(strObjectType.equalsIgnoreCase("dropdown"))
							 {
								 eleClick = eleRow.findElement(By.xpath(".//td[" + intColumnIndex1 + "]//select"));
								 Select dropdownval = new Select(eleClick);
								 dropdownval.selectByVisibleText(strValueToBeEntered);
								 blnClick = true;
								 break;	
							 }
							 else if(strObjectType.equalsIgnoreCase("date") || strObjectType.equalsIgnoreCase("textbox"))
							 {
								 eleClick = eleRow.findElement(By.xpath(".//td[" + intColumnIndex1 + "]//input"));
								 clear(eleClick, lngMinTimeOutInSeconds, strSecondCol, strPageName);
								 click(eleClick, lngMinTimeOutInSeconds, strValueToBeEntered, strSecondCol, strPageName, false);
								 sendkeys(eleClick, lngMinTimeOutInSeconds, strValueToBeEntered, strSecondCol, strPageName,false);
								 sendkeys(eleClick, lngMinTimeOutInSeconds, Keys.TAB, strSecondCol, strPageName,false);
								 blnClick = true;
								 break;
							 }
							 else
							 {
								 ALMFunctions.ThrowException("Test Data",
										 "Only Pre-Defined Fields Type must be provided in the test data sheet",
										 "Error - Unhandled Field Type " + strObjectType, false);
							 }


						 }



					 }

				 }

				 intCurrentPage++;
			 } while (!(blnClick || blnRecordNotFound));


			 if (blnClick) {

				 report.updateTestLog("Enter the '" + strSecondCol + "' value from table","'"+
						 strSecondCol + "' value should be entered in table",
						 "Entered Value from table for " + strSecondCol + " is : " + strValueToBeEntered 
						 +" in the row of:'"+strRefernceColVal+"'" , Status.DONE);

			 } else {
				 ALMFunctions.ThrowException(strSecondCol, strSecondCol + " value should be display in table row",
						 "Error - Specified Record " + strSecondCol + " is not found in the " + strTableName + " table",
						 true);
			 }

		 }

	}
	/**
	 * Method to enter record in Table which is split in to two tables 
	 * 
	 * @param strColumnName, Primary column name and secondary column name to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strValues,     Primary column value and secondary column value to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strPageName,   Page Name in which the control is available
	 * @return No return value
	 */
	//@SuppressWarnings("unused")
	public void enterValueInDialogSplitTable(String strColumnName, String strValue, String strPageName) {

		WebElement eleRows = null;

		By webTableHeader = null;
		int intColumnIndex = 0;
		String strTableName = "LookUp Table";
		String[] strValues = strValue.split("!");
		String strValueToBeEntered = strValues[0].trim();
		String strObjectType = strValues[1].trim();


		webTableHeader = Common.dialogTableHeader;
		intColumnIndex = getColumnIndex(strColumnName, webTableHeader, strTableName, false, false) + 1;
		if (!(intColumnIndex != 0)) {
			ALMFunctions.ThrowException("Get index of column name",
					"Expected column name as " + strColumnName + " shall be displayed",
					"Expected column name as " + strColumnName + " is not displayed", true);
		}

		boolean blnFound = false;
		boolean blnClick = false;
		int intCurrentPage = 1;
		List<WebElement> listtableRow = null;

		By row = Common.dialogTableRow;
		listtableRow = driver.getWebDriver().findElements(row);


		if (listtableRow.isEmpty()) {

			ALMFunctions.ThrowException(strValueToBeEntered, strValueToBeEntered + " table row should be displayed",
					strValueToBeEntered + " table row are not displayed", true);

		} else {

			boolean blnRecordNotFound = false;
			do {

				if (intCurrentPage != 1) {

					if (objectExists(Common.nextPage, "isDisplayed", lngMinTimeOutInSeconds, "Next", "Button",
							strTableName, false)) {
						click(Common.nextPage, lngMinTimeOutInSeconds, "Next", "Button", strTableName, false);
						driverUtil.waitUntilStalenessOfElement(Common.nextPage, strTableName);

					} else {
						blnRecordNotFound = true;
					}

				}
				//Identifying primary key value
				row = Common.dialogTableRow;
				listtableRow = driver.getWebDriver().findElements(row);
				WebElement elmRowValue= null;
				int Counter = 0;
				for (WebElement rows : listtableRow) {

					if(strObjectType.equalsIgnoreCase("dropdown"))
					{
						elmRowValue = rows.findElement(By.xpath(".//td[" + intColumnIndex + "]//select"));
						Select dropdownval = new Select(elmRowValue);
						dropdownval.selectByVisibleText(strValueToBeEntered);
						blnClick = true;
						break;	
					}
					else if(strObjectType.equalsIgnoreCase("textbox"))
					{
						elmRowValue = rows.findElement(By.xpath(".//td[" + intColumnIndex + "]//input"));
						clear(elmRowValue, lngMinTimeOutInSeconds, strValueToBeEntered, strPageName);
						click(elmRowValue, lngMinTimeOutInSeconds, strValueToBeEntered, strColumnName, strPageName, false);
						sendkeys(elmRowValue, lngMinTimeOutInSeconds, strValueToBeEntered, strColumnName, strPageName,false);
						sendkeys(elmRowValue, lngMinTimeOutInSeconds, Keys.ENTER, strColumnName, strPageName,false);
						sendkeys(elmRowValue, lngMinTimeOutInSeconds, Keys.TAB, strColumnName, strPageName,false);
						blnClick = true;
						break;
					}
					else
					{
						ALMFunctions.ThrowException("Test Data",
								"Only Pre-Defined Fields Type must be provided in the test data sheet",
								"Error - Unhandled Field Type " + strObjectType, false);
					}

				}

				intCurrentPage++;
			} while (!(blnClick || blnRecordNotFound));


			if (blnClick) {

				report.updateTestLog("Enter the '" + strColumnName + "' value from table","'"+
						strColumnName + "' value should be entered in table",
						"Entered Value from table for " + strColumnName + " is : " + strValueToBeEntered  , Status.DONE);

			} else {
				ALMFunctions.ThrowException(strColumnName, strColumnName + " value should be display in table row",
						"Error - Specified Record " + strValueToBeEntered + " is not found in the " + strTableName + " table",
						true);
			}

		}

	}
	/**
	 * Function to wait until the Background Job is completed
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void waitForBackgroundJobValidation() throws InterruptedException {
		long start_time=System.currentTimeMillis();
		long wait_time=jobTimeOut;
		long end_time=start_time + wait_time;
		boolean flag=false;
		String strJobAlertMsg = dataTable.getData("Parametrized_Checkpoints", "BackGroundJob");
		Common objJobAlert = new Common(strJobAlertMsg);
		driverUtil.waitUntilStalenessOfElement(objJobAlert.label, staleTableTimeOut, driver.getTitle());
		while(flag==false) {
			if(!objectExists(objJobAlert.label, "isDisplayed", lngMinTimeOutInSeconds, strJobAlertMsg, "Label", driver.getTitle(), false)) {
				flag=true;
			}else if(System.currentTimeMillis() == end_time){
				ALMFunctions.UpdateReportLogAndALMForFailStatus("Validate job alert is visible",
						"Job Alert should be Completed.",
						"Job Alert is not Completed in the expected time period.", true);
				flag=true;
			}else {
				driver.switchTo().defaultContent();
				driver.findElement(Common.refresh).click();
				driverUtil.waitFor(3000);
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
			report.updateTestLog("Validate job alert is visible","Job Alert should be Completed in an expected time period."
					,"Job Alert is Completed in an expected time period. Time taken to complete the action is: "+totalDuration +" "+strDurationType, 
					Status.DONE);

		}
		else
		{
			report.updateTestLog("Validate job alert is visible","Job Alert should be Completed in an expected time period."
					,"Job Alert is not Completed in an expected time period.", Status.FAIL);
		}
	}

	/**
	 * Function to wait until the Background Job is completed
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void waitForContractBackgroundJobValidation() throws InterruptedException {
		long start_time=System.currentTimeMillis();
		long wait_time=jobTimeOut;
		long end_time=start_time + wait_time;
		boolean flag=false;
		String strJobAlertMsg = dataTable.getData("Parametrized_Checkpoints", "BackGroundJob");
		Common objJobAlert = new Common(strJobAlertMsg);
		driverUtil.waitUntilStalenessOfElement(objJobAlert.partialLabel, staleTableTimeOut, driver.getTitle());
		while(flag==false) {
			if(!objectExists(objJobAlert.partialLabel, "isDisplayed", lngMinTimeOutInSeconds, strJobAlertMsg, "Label", driver.getTitle(), false)) {
				flag=true;
				break;
			}else if(System.currentTimeMillis() == end_time){
				ALMFunctions.UpdateReportLogAndALMForFailStatus("Validate job alert is visible",
						"Job Alert should be Completed.",
						"Job Alert is not Completed in the expected time period.", true);
				flag=true;
				break;
			}else {
				driver.switchTo().defaultContent();
				driver.findElement(Common.refresh).click();
				driverUtil.waitFor(3000);
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
			report.updateTestLog("Validate job alert is visible","Job Alert should be Completed in an expected time period."
					,"Job Alert is Completed in an expected time period. Time taken to complete the action is: "+totalDuration +" "+strDurationType, 
					Status.DONE);

		}
		else
		{
			report.updateTestLog("Validate job alert is visible","Job Alert should be Completed in an expected time period."
					,"Job Alert is not Completed in an expected time period.", Status.FAIL);
		}
	}

	/**
	 * Method to apply filter in Table which is split in to two tables 
	 * 
	 * @param strColumnName, Primary column name and secondary column name to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strValues,     Primary column value and secondary column value to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strPageName,   Page Name in which the control is available
	 * @return No return value
	 */
	@SuppressWarnings("unused")

	public void enterFilterInSplitTable(String strColumnName, String strValue, String strPageName)
	{


		String strValToBeEntered = "";
		String strObjectType="";
		WebElement eleRow = null;
		WebElement eleValue = null;
		By webTableHeader = null;
		int intColumnIndex = 0;
		String strTableName = "LookUp Table";
		Boolean eleCondition= false;
		String[] strValues = strValue.split("!");
		strValToBeEntered = strValues[0].trim();
		strObjectType = strValues[1].trim();
		String strTableCondition   = "First";

		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
		/*webTableHeader = Common.splitFirstTableHeader;
		//driverUtil.waitUntilStalenessOfElement(webTableHeader, staleTableTimeOut, strPageName);
		intColumnIndex = getColumnIndex(strColumnName, webTableHeader, strTableName, false, false) + 1;
		if (!(intColumnIndex != 0)) {
			webTableHeader = Common.splitSecTableHeader;
			//driverUtil.waitUntilStalenessOfElement(webTableHeader, staleTableTimeOut, strPageName);
			intColumnIndex = getColumnIndex(strColumnName, webTableHeader, strTableName, false, false) + 1;
			strTableCondition = "Second";
		}*/



		try {
			webTableHeader = Common.splitFirstTableHeader;
			//driverUtil.waitUntilStalenessOfElement(webTableHeader, lngMinTimeOutInSeconds, strPageName);
			intColumnIndex = getColumnIndex(strColumnName, webTableHeader, strTableName, false, false) + 1;
		}
		catch(Exception e)
		{
			webTableHeader = Common.plainSplitFirstTableHeader;
			intColumnIndex = getColumnIndex(strColumnName, webTableHeader, strTableName, false, false) + 1;
		}

		if(!(intColumnIndex != 0)) 
		{
			try{
				webTableHeader = Common.splitSecTableHeader;
				//driverUtil.waitUntilStalenessOfElement(webTableHeader, lngMinTimeOutInSeconds, strPageName);
				intColumnIndex = getColumnIndex(strColumnName, webTableHeader, strTableName, false, false) + 1;
				strTableCondition = "Second";
			}
			catch(Exception e)
			{
				webTableHeader = Common.plainSplitSecTableHeader;
				//driverUtil.waitUntilStalenessOfElement(webTableHeader, lngMinTimeOutInSeconds, strPageName);
				intColumnIndex = getColumnIndex(strColumnName, webTableHeader, strTableName, false, false) + 1;
				strTableCondition = "Second";	
			}
		}



		if (!(intColumnIndex != 0)) {
			ALMFunctions.ThrowException("Get index of column name",
					"Expected column name as " + strColumnName + " shall be displayed",
					"Expected column name as " + strColumnName + " is not displayed", true);
		}

		if(strTableCondition.equalsIgnoreCase("First"))
		{
			String strIndex = String.valueOf(intColumnIndex);
			eleRow = driver.findElement(new Common(strIndex).splitFirstTableHeaderFilter);

			if(strObjectType.equalsIgnoreCase("dropdown"))
			{
				eleValue = eleRow.findElement(By.xpath(".//select"));
				Select dropdownval = new Select(eleValue);
				dropdownval.selectByVisibleText(strValToBeEntered);
				eleCondition = true;
			}

			else if(strObjectType.contains("Dropdown2"))
			{
				eleValue = eleRow.findElement(By.xpath(".//select[2]"));
				Select dropdownval = new Select(eleValue);
				dropdownval.selectByVisibleText(strValToBeEntered);
				eleCondition = true;
			}
			else if(strObjectType.equalsIgnoreCase("textbox"))
			{
				eleValue = eleRow.findElement(By.xpath(".//input[@type='text']"));
				clear(eleValue, lngMinTimeOutInSeconds, strColumnName, strPageName);
				click(eleValue, lngMinTimeOutInSeconds, strValToBeEntered, strColumnName, strPageName, false);
				sendkeys(eleValue, lngMinTimeOutInSeconds, strValToBeEntered, strColumnName, strPageName,true);
				sendkeys(eleValue, lngMinTimeOutInSeconds, Keys.TAB, strColumnName, strPageName,false);
				eleCondition = true;
			}
			else
			{
				ALMFunctions.ThrowException("Test Data",
						"Only Pre-Defined Fields Type must be provided in the test data sheet",
						"Error - Unhandled Field Type " + strObjectType, false);
			}
		}
		else
		{
			String strIndex = String.valueOf(intColumnIndex);
			eleRow = driver.findElement(new Common(strIndex).splitSecTableHeaderFilter);

			if(strObjectType.equalsIgnoreCase("dropdown"))
			{
				eleValue = eleRow.findElement(By.xpath(".//select"));
				Select dropdownval = new Select(eleValue);
				dropdownval.selectByVisibleText(strValToBeEntered);
				eleCondition = true;
			}
			else if(strObjectType.contains("Dropdown2"))
			{
				eleValue = eleRow.findElement(By.xpath(".//select[2]"));
				Select dropdownval = new Select(eleValue);
				dropdownval.selectByVisibleText(strValToBeEntered);
				eleCondition = true;
			}
			else if(strObjectType.equalsIgnoreCase("textbox"))
			{
				eleValue = eleRow.findElement(By.xpath(".//input[@type='text']"));
				clear(eleValue, lngMinTimeOutInSeconds, strColumnName, strPageName);
				click(eleValue, lngMinTimeOutInSeconds, strValToBeEntered, strColumnName, strPageName, false);
				sendkeys(eleValue, lngMinTimeOutInSeconds, strValToBeEntered, strColumnName, strPageName,true);
				sendkeys(eleValue, lngMinTimeOutInSeconds, Keys.TAB, strColumnName, strPageName,false);
				eleCondition = true;
			}
			else
			{
				ALMFunctions.ThrowException("Test Data",
						"Only Pre-Defined Fields Type must be provided in the test data sheet",
						"Error - Unhandled Field Type " + strObjectType, false);
			}
		}

		if(eleCondition)
		{
			report.updateTestLog("Enter the " + strColumnName + " value from table",
					"'"+strColumnName + "' value should be entered in table",
					"Entered Value from table for '" + strColumnName + "' : '" + strValToBeEntered+"'", Status.DONE);

		} 
		else {
			ALMFunctions.ThrowException("Enter the " + strColumnName + " value from table", strColumnName + " value should be display in table row",
					"Error - Specified Record " + strColumnName + " is not found in the " + strTableName + " table",
					true);

		}

	}

	/**
	 * Method to apply filter in Table
	 * 
	 * @param strColumnName, Primary column name and secondary column name to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strValues,     Primary column value and secondary column value to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strPageName,   Page Name in which the control is available
	 * @return No return value
	 */
	@SuppressWarnings("unused")

	public void enterFilterInTable(String strColumnName, String strValue, String strPageName)
	{


		String strValToBeEntered = "";
		String strObjectType="";
		WebElement eleRow = null;
		WebElement eleValue = null;
		By webTableHeader = null;
		int intColumnIndex = 0;
		String strTableName = "LookUp Table";
		Boolean eleCondition= false;
		String[] strValues = strValue.split("!");
		strValToBeEntered = strValues[0].trim();
		strObjectType = strValues[1].trim();

		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
		/*if(objectExists(new Common("Edit Override","Yes").dialogButton, "isDisplayed", lngMinTimeOutInSeconds, "Yes", "Dialog Button",strPageName, false)) {
			clickByJs(new Common("Edit Override","Yes").dialogButton, lngMinTimeOutInSeconds,"Yes", "Dialog Button",strPageName,true);
		}*/
		webTableHeader = Common.tableHeader;
		//driverUtil.waitUntilStalenessOfElement(webTableHeader, staleTableTimeOut, strPageName);
		intColumnIndex = getColumnIndex(strColumnName, webTableHeader, strTableName, false, false) + 1;



		if (!(intColumnIndex != 0)) {
			ALMFunctions.ThrowException("Get index of column name",
					"Expected column name as " + strColumnName + " shall be displayed",
					"Expected column name as " + strColumnName + " is not displayed", true);
		}

		String strIndex = String.valueOf(intColumnIndex);
		eleRow = driver.findElement(new Common(strIndex).tableFilterRow);

		if(strObjectType.equalsIgnoreCase("dropdown"))
		{
			eleValue = eleRow.findElement(By.xpath(".//select"));
			Select dropdownval = new Select(eleValue);
			dropdownval.selectByVisibleText(strValToBeEntered);
			eleCondition = true;
		}

		else if(strObjectType.contains("Dropdown2"))
		{

			List<WebElement> eleValues = eleRow.findElements(By.xpath(".//select"));
			eleValue =	eleValues.get(1);
			Select dropdownval = new Select(eleValue);
			dropdownval.selectByVisibleText(strValToBeEntered);
			eleCondition = true;
		}
		else if(strObjectType.equalsIgnoreCase("textbox"))
		{
			eleValue = eleRow.findElement(By.xpath(".//input[@type='text']"));
			clear(eleValue, lngMinTimeOutInSeconds, strColumnName, strPageName);
			click(eleValue, lngMinTimeOutInSeconds, strValToBeEntered, strColumnName, strPageName, false);
			sendkeys(eleValue, lngMinTimeOutInSeconds, strValToBeEntered, strColumnName, strPageName,true);
			sendkeys(eleValue, lngMinTimeOutInSeconds, Keys.TAB, strColumnName, strPageName,false);
			eleCondition = true;
		}
		else
		{
			ALMFunctions.ThrowException("Test Data",
					"Only Pre-Defined Fields Type must be provided in the test data sheet",
					"Error - Unhandled Field Type " + strObjectType, false);
		}




		if(eleCondition)
		{
			report.updateTestLog("Enter the " + strColumnName + " value from table",
					"'"+strColumnName + "' value should be entered in table",
					"Entered Value from table for '" + strColumnName + "' : '" + strValToBeEntered+"'", Status.DONE);

		} 
		else {
			ALMFunctions.ThrowException("Enter the " + strColumnName + " value from table", strColumnName + " value should be display in table row",
					"Error - Specified Record " + strColumnName + " is not found in the " + strTableName + " table",
					true);

		}

	}

	/**
	 * Method to apply filter in Table
	 * 
	 * @param strColumnName, Primary column name and secondary column name to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strValues,     Primary column value and secondary column value to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strPageName,   Page Name in which the control is available
	 * @return No return value
	 */
	@SuppressWarnings("unused")

	public void verifyTableRowCount(String strColumnName, String strValue, String strPageName)
	{


		String strValToBeEntered = "";
		String strObjectType="";
		WebElement eleRow = null;
		WebElement eleValue = null;
		By webTableHeader = null;
		int intColumnIndex = 0;
		String strTableName = "LookUp Table";
		Boolean eleCondition= false;
		String[] strValues = strValue.split("!");
		strValToBeEntered = strValues[0].trim();
		strObjectType = strValues[1].trim();

		driverUtil.waitUntilElementInVisible(Common.loadingMsg, "Loading Message", "Loading", strPageName);
		if(objectExists(new Common("Edit Override","Yes").dialogButton, "isDisplayed", lngMinTimeOutInSeconds, "Yes", "Dialog Button",strPageName, false)) {
			clickByJs(new Common("Edit Override","Yes").dialogButton, lngMinTimeOutInSeconds,"Yes", "Dialog Button",strPageName,true);
		}
		webTableHeader = Common.tableHeader;
		//driverUtil.waitUntilStalenessOfElement(webTableHeader, staleTableTimeOut, strPageName);
		intColumnIndex = getColumnIndex(strColumnName, webTableHeader, strTableName, false, false) + 1;



		if (!(intColumnIndex != 0)) {
			ALMFunctions.ThrowException("Get index of column name",
					"Expected column name as " + strColumnName + " shall be displayed",
					"Expected column name as " + strColumnName + " is not displayed", true);
		}

		String strIndex = String.valueOf(intColumnIndex);
		eleRow = driver.findElement(new Common(strIndex).tableFilterRow);

		if(strObjectType.equalsIgnoreCase("dropdown"))
		{
			eleValue = eleRow.findElement(By.xpath(".//select"));
			Select dropdownval = new Select(eleValue);
			dropdownval.selectByVisibleText(strValToBeEntered);
			eleCondition = true;
		}

		else if(strObjectType.contains("Dropdown2"))
		{

			List<WebElement> eleValues = eleRow.findElements(By.xpath(".//select"));
			eleValue =	eleValues.get(1);
			Select dropdownval = new Select(eleValue);
			dropdownval.selectByVisibleText(strValToBeEntered);
			eleCondition = true;
		}
		else if(strObjectType.equalsIgnoreCase("textbox"))
		{
			eleValue = eleRow.findElement(By.xpath(".//input[@type='text']"));
			clear(eleValue, lngMinTimeOutInSeconds, strColumnName, strPageName);
			click(eleValue, lngMinTimeOutInSeconds, strValToBeEntered, strColumnName, strPageName, false);
			sendkeys(eleValue, lngMinTimeOutInSeconds, strValToBeEntered, strColumnName, strPageName,true);
			sendkeys(eleValue, lngMinTimeOutInSeconds, Keys.TAB, strColumnName, strPageName,false);
			eleCondition = true;
		}
		else
		{
			ALMFunctions.ThrowException("Test Data",
					"Only Pre-Defined Fields Type must be provided in the test data sheet",
					"Error - Unhandled Field Type " + strObjectType, false);
		}




		if(eleCondition)
		{
			report.updateTestLog("Enter the " + strColumnName + " value from table",
					"'"+strColumnName + "' value should be entered in table",
					"Entered Value from table for '" + strColumnName + "' : '" + strValToBeEntered+"'", Status.DONE);

		} 
		else {
			ALMFunctions.ThrowException("Enter the " + strColumnName + " value from table", strColumnName + " value should be display in table row",
					"Error - Specified Record " + strColumnName + " is not found in the " + strTableName + " table",
					true);

		}

	}



	/**
	 * Method to verify the status of the RadioButton. This method is part of
	 * verifyobjectstate method
	 * 
	 * @param locator, locator value of element
	 * @param strButtonName, fieldName of the button
	 * @param strElementState, state of the element
	 * @param strElementType, type of the element
	 * @param strPageName   - Page Name in which the control is available
	 * @return No return value
	 */

	public void verifyradioButtonStatus(By locator, String strRadioBtnName, String strElementState, String strElementType,
			String strPageName) {

		switch (strElementState) {
		case "checked":

			if (objectExists(locator, "isDisplayed", lngMinTimeOutInSeconds, strRadioBtnName, "RadioButton Checked",strPageName, false)) {
				pagescroll(locator, strPageName);
				locator = new Common(strRadioBtnName).radioButton;
				String strToggleCheckboxstatus = driver.findElement(locator).getAttribute("checked");
				if(strToggleCheckboxstatus.equalsIgnoreCase("true"))
				{
					ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify RadioButton is Checked",
							"'" +strRadioBtnName + "' " + strElementType + " should be checked in the " + strPageName,
							"'" +strRadioBtnName + "' " + strElementType + " is checked in the " + strPageName, true);
				}
				else
				{
					ALMFunctions.UpdateReportLogAndALMForFailStatus("Verify RadioButton is Checked",
							"'" +strRadioBtnName + "' " + strElementType + " should be checked in the " + strPageName,
							"'" +strRadioBtnName + "' " + strElementType + " is not checked in the " + strPageName, true);
				}
			}

			else {
				ALMFunctions.UpdateReportLogAndALMForFailStatus("Verify RadioButton is Checked",
						"'" +strRadioBtnName + "' " + strElementType + " should be display in the " + strPageName,
						"'" +strRadioBtnName + "' " + strElementType + " is not displayed in the " + strPageName, true);

			}

			break;

		case "unchecked":
			if (objectExists(locator, "isDisplayed", lngMinTimeOutInSeconds, strRadioBtnName, "RadioButton UnChecked",strPageName, false)) {
				pagescroll(locator, strPageName);
				locator = new Common(strRadioBtnName).radioButton;
				String strToggleCheckboxstatus = driver.findElement(locator).getAttribute("checked");
				if(strToggleCheckboxstatus.equalsIgnoreCase("false"))
				{
					ALMFunctions.UpdateReportLogAndALMForPassStatus("Verify RadioButton is not Checked",
							"'" +strRadioBtnName + "' " + strElementType + " should be unchecked in the " + strPageName,
							"'" +strRadioBtnName + "' " + strElementType + " is unchecked in the " + strPageName, true);
				}
				else
				{
					ALMFunctions.UpdateReportLogAndALMForFailStatus("Verify RadioButton is not Checked",
							"'" +strRadioBtnName + "' " + strElementType + " should be unchecked in the " + strPageName,
							"'" +strRadioBtnName + "' " + strElementType + " is not unchecked in the " + strPageName, true);
				}
			}

			else {
				ALMFunctions.UpdateReportLogAndALMForFailStatus("Verify RadioButton is not Checked",
						"'" +strRadioBtnName + "' " + strElementType + " should be display in the " + strPageName,
						"'" +strRadioBtnName + "' " + strElementType + " is not displayed in the " + strPageName, true);

			}

			break;


		default:
			ALMFunctions.ThrowException("Verify object", "Only pre-defined control must be provided",
					"Unhandled control " + strElementState, false);
			break;
		}

	}

	/**
	 * Method to switch to different frames in application
	 * 
	 * @param locator,     Locator of the Frame
	 * @param strFrameValue,  input frame values to switch different frames
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */

	public void frameSwitchAndSwitchBack(By locator, String StrFrameValue, String strPageName) {
		switch (StrFrameValue.toLowerCase()) {
		case "documentframe":
			try {
				driver.switchTo().defaultContent();

				if (objectExists(locator, "isEnabled", lngMinTimeOutInSeconds, StrFrameValue, "Frame", strPageName,false)) {
					driverUtil.waitUntilStalenessOfElement(locator, strPageName);
					WebElement elmFrame = driver.findElement(locator);
					driver.switchTo().frame(elmFrame);
					//report.updateTestLog("Switch to Frame", "Should be switched to Document Frame", "Switched to Document Frame", Status.DONE);

				}
				//driverUtil.waitUntilFrameAvailableAndSwitch(locator, lngMinTimeOutInSeconds, strPageName);

			} catch (Exception e) {
				ALMFunctions.ThrowException("Error - Frame", "Iframe should be available in the " + strPageName,
						"Iframe is not available in the " + strPageName, true);
			}
			break;
		case "dynframe":
			try {

				driverUtil.waitUntilFrameAvailableAndSwitch(locator, lngMinTimeOutInSeconds, strPageName);
				//report.updateTestLog("Switch to Frame", "Should be switched to Dynamic Frame", "Switched to Dynamic Frame", Status.DONE);

			} catch (Exception e) {
				ALMFunctions.ThrowException("Error - Frame", "Iframe should be available in the " + strPageName,
						"Iframe is not available in the " + strPageName, true);
			}
			break;
		case "externalframe":
			try {

				driverUtil.waitUntilFrameAvailableAndSwitch(locator, lngMinTimeOutInSeconds, strPageName);
				//report.updateTestLog("Switch to Frame", "Should be switched to External Frame", "Switched to External Frame", Status.DONE);
			} catch (Exception e) {
				ALMFunctions.ThrowException("Error - Frame", "Iframe should be available in the " + strPageName,
						"Iframe is not available in the " + strPageName, true);
			}
			break;
		case "default":
			driver.switchTo().defaultContent();
			//report.updateTestLog("Switch to Frame", "Should be switched to Default Frame", "Switched to Default Frame", Status.DONE);
			break;

		default:
			ALMFunctions.ThrowException("Test Data",
					"Only Pre-Defined Fields Type must be provided in the test data sheet",
					"Error - Unhandled Field Type " + StrFrameValue, false);
			break;
		}

	}


	/**
	 * @param strSheetName     - Name of the Sheet in which data to be concatenated
	 *                         is present
	 * @param strColumnName    - Column Name in which data to be concatenated is
	 *                         present
	 * @param strScenario      - Scenario for which test data is to be user
	 * @param strDelimiter     - Delimiter to be used during concatenation
	 * @param includeDelimiter - Boolean to include delimiter or not
	 * @return - Concatenated String
	 */
	public String getConcatenatedStringFromExcel(String strSheetName, String strColumnName,
			String strConcatenationFlagColumn, String strScenario, String strDelimiter, boolean blnIncludeDelimiter,
			boolean blnInput) {
		String strValue = "";
		String strLockFile = "Excel_Data";
		FileLockMechanism objFileLockMechanism = new FileLockMechanism(
				Long.valueOf(properties.getProperty("FileLockTimeOut")));
		FileLock objFileLock = objFileLockMechanism.SetLockOnFile(strLockFile);
		if (objFileLock != null) {
			String strFilePath = dataTable.datatablePath + Util.getFileSeparator() + dataTable.datatableName + ".xls";
			HSSFWorkbook wb = openExcelFile(strFilePath);
			wb.setForceFormulaRecalculation(true);
			HSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
			Sheet sheet = getSheetFromXLSWorkbook(wb, strSheetName, strFilePath);
			int intColumnIndexConcatFlag = getColumnIndex(strFilePath, strSheetName, strConcatenationFlagColumn);
			int intColumnIndex = getColumnIndex(strFilePath, strSheetName, strColumnName);
			int intStartRow = getStartRow(wb, strFilePath, strSheetName, sheet, strScenario);
			int intEndRow = getEndRow(wb, sheet, intStartRow, strSheetName);
			int intFlagWriteDataInFormSheet = getColumnIndex(strFilePath, strSheetName,
					"Flag_Write_Data_In_This_Sheet");
			int intStorageIndex = getColumnIndex(strFilePath, strSheetName, "Stored_Value");
			for (int i = intStartRow; i < intEndRow; i++) {
				if (sheet.getRow(i) != null) {
					if (getCellValueAsString(wb, sheet.getRow(i).getCell(intColumnIndexConcatFlag))
							.equalsIgnoreCase("yes")) {
						if (blnInput) {
							String strTemp = getCellValueAsString(wb, sheet.getRow(i).getCell(intColumnIndex));
							String strStorage = "";
							strStorage = StringUtils.substringAfter(strTemp, "Storage=");
							String strValueToBeWritten = StringUtils.substringBetween(strTemp, "Element Value=",
									";Storage=");
							if (strStorage != null && strStorage.trim().length() > 0) {
								for (String strWriteParameter : StringUtils.split(strStorage, ";")) {
									dataTable.putData(StringUtils.substringBefore(strWriteParameter, "!"),
											StringUtils.substringAfter(strWriteParameter, "!"), strValueToBeWritten);
								}
							}
							if (getCellValueAsString(wb, sheet.getRow(i).getCell(intFlagWriteDataInFormSheet))
									.equalsIgnoreCase("yes")) {
								writeData(strFilePath, strSheetName, i, intStorageIndex, strValueToBeWritten);
							}
						}
						if (blnIncludeDelimiter) {
							if (i == intStartRow) {
								strValue = getCellValueAsString(wb, sheet.getRow(i).getCell(intColumnIndex))
										+ strDelimiter;
							} else if (i != intEndRow) {
								strValue = strValue + getCellValueAsString(wb, sheet.getRow(i).getCell(intColumnIndex))
								+ strDelimiter;
							} else {
								strValue = strValue + getCellValueAsString(wb, sheet.getRow(i).getCell(intColumnIndex));
							}
						} else {
							if (i == intStartRow) {
								strValue = getCellValueAsString(wb, sheet.getRow(i).getCell(intColumnIndex));
							} else {
								strValue = strValue + getCellValueAsString(wb, sheet.getRow(i).getCell(intColumnIndex));
							}
						}
					}
				}
			}
			try {
				wb.close();
			} catch (IOException e) {
				ALMFunctions.ThrowException("Excel Close", "Should be able to close excel file",
						"Below Exception is thrown when trying to " + "close excel file found in the path "
								+ strFilePath + "<br><br>" + e.getLocalizedMessage(),
								false);
			}
			objFileLockMechanism.ReleaseLockOnFile(objFileLock, strLockFile);
		} else {
			throw new FrameworkException("Error", "Error in getting data from excel due to file lock exception");
		}
		return strValue;
	}

	/**
	 * @param wb           - HSSFWorkbook Object
	 * @param strFilePath  - File Path of the Excel File
	 * @param strSheetName - Sheet Name in which data to be concatenated is present
	 * @param sheet        - Sheet object
	 * @param strScenario  - Scenario for which this test data is going to be used
	 * @return - Index of Start Row
	 */
	public int getStartRow(HSSFWorkbook wb, String strFilePath, String strSheetName, Sheet sheet, String strScenario) {
		boolean blnRowFound = false;
		int intTCIDColumnIndex = getColumnIndex(strFilePath, strSheetName, "TC_ID");
		int intScenarioColumnIndex = getColumnIndex(strFilePath, strSheetName, "TC_Scenario");
		int intIterationColumnIndex = getColumnIndex(strFilePath, strSheetName, "Iteration");
		int intSubIterationColumnIndex = getColumnIndex(strFilePath, strSheetName, "SubIteration");
		for (int i = 1; i < sheet.getLastRowNum(); i++) {
			if (sheet.getRow(i) != null) {
				if (getCellValueAsString(wb, sheet.getRow(i).getCell(intTCIDColumnIndex))
						.equalsIgnoreCase(testparameters.getCurrentTestcase())
						&& getCellValueAsString(wb, sheet.getRow(i).getCell(intScenarioColumnIndex))
						.equalsIgnoreCase(strScenario)
						&& Integer.valueOf(getCellValueAsString(wb,
								sheet.getRow(i).getCell(intIterationColumnIndex))) == (dataTable.currentIteration)
								&& Integer.valueOf(getCellValueAsString(wb, sheet.getRow(i)
										.getCell(intSubIterationColumnIndex))) == (dataTable.currentSubIteration)) {
					blnRowFound = true;
					return i + 2;
				}
			}
		}
		if (!blnRowFound) {
			ALMFunctions.ThrowException("Test Data", "Test Data should be found in the sheet " + sheet,
					"Error - Test Data with " + "TC_ID as " + testparameters.getCurrentTestcase() + " , TC_Scenario as "
							+ strScenario + " , Iteration as " + dataTable.currentIteration + " , SubIteration as "
							+ dataTable.currentSubIteration + " does not exists in the sheet " + strSheetName,
							false);
		}
		return 0;
	}

	/**
	 * @param wb           - HSSFWorkbook Object
	 * @param sheet        - Sheet object
	 * @param intStartRow  - Index of Start Row
	 * @param strSheetName - Sheet Name in which data to be concatenated is present
	 * @return - Index of End Row
	 */
	public int getEndRow(HSSFWorkbook wb, Sheet sheet, int intStartRow, String strSheetName) {
		boolean blnEnd = false;
		for (int i = intStartRow; i <= sheet.getLastRowNum(); i++) {
			if (sheet.getRow(i) != null) {
				if (getCellValueAsString(wb, sheet.getRow(i).getCell(0)).equalsIgnoreCase("end")) {
					blnEnd = true;
					return i;
				}
			}
		}
		if (!blnEnd) {
			ALMFunctions.ThrowException("Test Data", "Test Data with End Tag should be found in the sheet " + sheet,
					"Error - Test Data with " + "End Tag does not exists in the sheet " + strSheetName, false);
		}
		return 0;
	}

	/**
	 * @param strFilePath     - File Path of the Excel File
	 * @param strSheetName    - Sheet Name in which data to be concatenated is
	 *                        present
	 * @param intRowNumber    - Index of the Row
	 * @param intColumnNumber - Index of the Column
	 * @param strValue        - Value to be written
	 */
	public void writeData(String strFilePath, String strSheetName, int intRowNumber, int intColumnNumber,
			String strValue) {
		String strLockFile = "PutData_Lock.xls";
		FileLockMechanism objFileLockMechanism = new FileLockMechanism(
				Long.valueOf(properties.getProperty("FileLockTimeOut")));
		FileLock objFileLock = objFileLockMechanism.SetLockOnFile(strLockFile);
		if (objFileLock != null) {
			synchronized (CommonFunctions.class) {
				HSSFWorkbook wb = openExcelFile(strFilePath);
				Sheet sheet = getSheetFromXLSWorkbook(wb, strSheetName, strFilePath);
				Row row = sheet.getRow(intRowNumber);
				Cell cell = row.createCell(intColumnNumber);
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(strValue);
				FileOutputStream fileOutputStream;
				try {
					fileOutputStream = new FileOutputStream(strFilePath);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					throw new FrameworkException("The specified file \"" + strFilePath + "\" does not exist!");
				}
				try {
					wb.write(fileOutputStream);
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw new FrameworkException(
							"Error while writing into the specified Excel workbook \"" + strFilePath + "\"");
				}
			}
			objFileLockMechanism.ReleaseLockOnFile(objFileLock, strLockFile);
		}
	}

	/**
	 * Function to Logout the ModelN Application 
	 * @param No parameters
	 * @return No return value
	 */
	public void logout() {
		driver.switchTo().defaultContent();
		driverUtil.waitUntilStalenessOfElement(Common.userName, driver.getTitle());
		click(Common.userName, lngMinTimeOutInSeconds, "User Name","DropDown", driver.getTitle(), true);
		click(new Common("Logout").button, lngMinTimeOutInSeconds, "Logout","Button", driver.getTitle(), true);
		String strLogoutMsg = "You have logged out of the Model N application. Please close this window.";
		if(objectExists(new Common(strLogoutMsg).label, "isDisplayed", lngMinTimeOutInSeconds, "Logout","Message",driver.getTitle(), false)){



			report.updateTestLog("Logout from Application" ,"Application should be Logged out Successfully." 
					,"Application is Logged out successfully.", Status.DONE);



		}
		else {



			ALMFunctions.ThrowException("Logout from Application" ,"Application should be Logged out Successfully." 
					,"Application is not Logged out successfully.", true);
		}
	}
	/**
	 * Method to Navigate to Right menu and submenu
	 * 
	 * @param strMenuName- name of the menu to be clicked
	 * @param strMenuValues - name of the submenu to be clicked
	 * @param strPageName   - Page Name in which the control is available
	 * @return No return value
	 */

	public void selectRightMenu(String strMenuName, String strMenuValues,String strPageName)
	{

		click(Common.rightMenu, lngMinTimeOutInSeconds,strMenuName, "Link", strPageName,true);

		for (String strItem : strMenuValues.split("!")) {
			By navMenuBarValues = new Common(strItem).navmenuItem;
			pagescroll(navMenuBarValues,strPageName);
			clickByJs(navMenuBarValues, lngCtrlTimeOutInSeconds, strItem, "Menu Value", strPageName,true);

		}

	}
	/**
	 * Method to Navigate to Right menu and submenu
	 * 
	 * @param strMenuName- name of the menu to be clicked
	 * @param strMenuValues - name of the submenu to be clicked
	 * @param strPageName   - Page Name in which the control is available
	 * @return No return value
	 */

	public void selectMainMenuinValidata(String strMenuName, String strMenuValues,String strPageName)
	{

		click(Common.rightMenu, lngMinTimeOutInSeconds,strMenuName, "Link", strPageName,true);

		for (String strItem : strMenuValues.split("!")) {
			By navMenuBarValues = new Common(strItem).navmenuItem;
			mouseOver(navMenuBarValues, lngCtrlTimeOutInSeconds, strItem, "Menu Value", strPageName);

		}
		By navMenuBarValuesFinal = new Common(strMenuName).navmenuItem;
		mouseOverandClick(navMenuBarValuesFinal, lngCtrlTimeOutInSeconds, strMenuName, "Menu Value", strPageName);
	}
	/**

	 * Method to select a value in Multiple dropdown

	 * 

	 * @param windowName, value of the window name

	 * @param strLabel,    Label of the Drop down

	 * @param strValue,    value to be selected in Drop down

	 * @param strPageName, Page Name in which the control is available

	 * @return No return value

	 */

	public void selectMultipleDropDown(String strLabel, String strValue, String strPageName) {

		String[] strLabels = strLabel.split("!");

		String strLabelName = strLabels[0];

		String strDropDownName = strLabels[1];

		try {

			Common button = new Common(strLabelName, strDropDownName);

			if (objectExists(button.multipleDropdown, "isDisplayed", lngMinTimeOutInSeconds, strDropDownName, "DropDown",strPageName, false)) {

				selectListItem(button.multipleDropdown, lngMinTimeOutInSeconds, new String[] { strValue }, strLabelName,

						strPageName, "Value");

			}

			else {

				ALMFunctions.ThrowException("Multiple DropDown", strLabelName + " should be displayed in " + strPageName,

						"Error - " + strLabelName + " is not available in " + strPageName, true);

			}

		}

		catch (Exception e) {

			ALMFunctions.ThrowException("Multiple DropDown", strLabelName + " should be displayed in " + strPageName,

					"Error - " + strLabelName + " is not available in " + strPageName, true);

		}

	}


	/**

	 * Method to select a radio button

	 * 

	 * @param strFieldName, The name of the field

	 * @param strValue,     value to be selected in the field

	 * @param strPageName,  Page Name in which the control is available

	 * @return No return value

	 */

	public void selectRadioButton(String strFieldName, String strValueToSelect, String strPageName) {

		try {
			if(!strFieldName.isEmpty()) {
				Common radio = new Common(strFieldName, strValueToSelect);
				driverUtil.waitUntilStalenessOfElement(radio.multiRadioButton, strPageName);
				clickByJs(radio.multiRadioButton, lngMinTimeOutInSeconds, strValueToSelect + " in " + strFieldName, "Radio Button", strPageName, true);
			}
			else
			{
				Common radio = new Common(strValueToSelect);
				driverUtil.waitUntilStalenessOfElement(radio.radioButton, strPageName);
				clickByJs(radio.radioButton, lngMinTimeOutInSeconds, strValueToSelect + " in " + strFieldName, "Radio Button", strPageName, true);
			}
		}

		catch(Exception e) {

			throw new FrameworkException(strFieldName + " is not available in " + strPageName);

		}

	}
	/**

	 * Method to select a radio button in table

	 * 

	 * @param strFieldName, The name of the field

	 * @param strValue,     value to be selected in the field

	 * @param strPageName,  Page Name in which the control is available

	 * @return No return value

	 */

	public void selectRadioButtoninTable(String strElementName,String strValues,String strPageName) {

		Common radio = new Common(strValues);

		clickByJs(radio.radioButton, lngMinTimeOutInSeconds, strValues , "Radio Button", strPageName, false);

	}
	/**

	 * Method to select a Checkbox field

	 * 

	 * @param strFieldName, The name of the field

	 * @param strValue,     value to be selected in the field

	 * @param strPageName,  Page Name in which the control is available

	 * @return No return value

	 */

	public void selectCheckinTransactionFile(String strValues,String strPageName) {

		Common checkbox = new Common(strValues);

		clickByJs(checkbox.checkbox, lngMinTimeOutInSeconds, strValues , "Check Box", strPageName, false);

	}
	/**

	 * Method to select a first row in a table

	 * 

	 * @param strElementName, The name of the field

	 * @param strValue,     value to be selected in the field

	 * @param strPageName,  Page Name in which the control is available

	 * @return No return value

	 */

	public void selectFirstRowinaTable(String strElementName, String strValues, String strPageName) {

		String strLinkName = getText(Common.selectFirstRowInTable, lngMinTimeOutInSeconds, strElementName, strPageName);
		driverUtil.waitUntilStalenessOfElement(Common.selectFirstRowInTable, strPageName);
		doubleClick(Common.selectFirstRowInTable,lngMinTimeOutInSeconds,  "Link",  strLinkName,  strPageName,true);


	}

	/**

	 * Method to Click alert button if alert is exist

	 * 

	 * @param strWindowName, The name of the alert window

	 * @param strValue,     value to be selected in the field

	 * @param strPageName,  Page Name in which the control is available

	 * @return No return value

	 */

	public void ifAlertExist(String strWindowName, String strButtonName, String strPageName)
	{
		if(objectExists(new Common(strWindowName,strButtonName).dialogButton, "isDisplayed", staleTableTimeOut, strButtonName, "Dialog Button",strPageName, false)) {
			clickByJs(new Common(strWindowName,strButtonName).dialogButton, lngMinTimeOutInSeconds,strButtonName, "Dialog Button",strPageName,true);
		}
		else if(objectExists(new Common(strButtonName).button, "isDisplayed", staleTableTimeOut, strButtonName, "Dialog Button",strPageName, false)) 

		{
			if(objectExists(new Common(strButtonName).button, "isEnabled", staleTableTimeOut, strButtonName, "Dialog Button",strPageName, false))
			{
				clickByJs(new Common(strButtonName).button, lngMinTimeOutInSeconds,strButtonName, "Dialog Button",strPageName,true);	
			}
		}
	}
	/**

	 * Method to delete a file from testDataFiles location

	 * 

	 * @param strFilePath, The name of the file path

	 * 

	 * @param strPageName,  Page Name in which the control is available

	 * @return No return value

	 */

	public void deleteFile(String strFilePath,String strPageName) {
		String filename=dataTable.getData("Parametrized_Checkpoints", strFilePath);
		File f= new File(filename);
		f.delete();
	}
	/**
	 * Method to Click link in a table

	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */
	public void clickLinkinTable(String strElementName, String strValues, String strPageName) {

		Common linkinTable=new Common(strElementName,strValues);
		pagescroll(linkinTable.clickLinkinTable, strPageName);
		clickByJs(linkinTable.clickLinkinTable, lngMinTimeOutInSeconds,strValues, "Button",strPageName,true);

	}
	/**
	 * Method to Click Check box in a table
	 * 
	 * @param locator, locator of checkbox
	 * @param strFieldName,    value to click check box which is available in Table
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */

	public void clickTableCheckBox(String strCheckBoxName, String strFieldName, String strPageName) {


		if (objectExists(new Common(strFieldName).tableCheckBox, "isDisplayed", lngMinTimeOutInSeconds, strFieldName, "check box", strPageName, false)) {
			driverUtil.waitUntilStalenessOfElement(new Common(strFieldName).tableCheckBox, strPageName);
			boolean blnChecked = driver.findElement(new Common(strFieldName).tableCheckBox).isSelected();
			if (blnChecked) {
				report.updateTestLog("Click the Checkbox", "Checkbox should be able to check",
						strFieldName + " checkbox is already checked", Status.DONE);
			} else {

				clickByJs(new Common(strFieldName).tableCheckBox, lngMinTimeOutInSeconds, strFieldName, "check box", strPageName, true);

			}
		} else {
			ALMFunctions.ThrowException("Checkbox", strFieldName + " should be displayed in " + strPageName,
					"Error - " + strFieldName + " is not available for editing in " + strPageName, true);
		}

	}

	/**
	 * Method to Click Icons in the Table Header
	 * 
	 * @param locator, locator of checkbox
	 * @param strFieldName,    value to click check box which is available in Table
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */
	public void clickTableHeaderIcon(String strValues,String strPageName){

		Common iconinTable=new Common(strValues);
		pagescroll(iconinTable.iconinTableHeader,strPageName);
		clickByJs(iconinTable.iconinTableHeader, lngMinTimeOutInSeconds,strValues, "Button",strPageName,true);
	}
	/**
	 * Method to Click Edit Icons in the Table Header
	 * 
	 * @param locator, locator of checkbox
	 * @param strFieldName,    value to click check box which is available in Table
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */
	public void clickEditIconinTable(String strValues,String strPageName){

		Common editIconinTable=new Common(strValues);
		pagescroll(editIconinTable.editIconinTable,strPageName);
		clickByJs(editIconinTable.editIconinTable, lngMinTimeOutInSeconds,strValues, "Button",strPageName,true);
	}

	/**
	 * Method to Search text in the Table 
	 * 
	 * @param locator, locator of checkbox
	 * @param strFieldName,    value to click check box which is available in Table
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */
	public void searchTextboxinTable(String strValue, String strPageName){


		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driverUtil.waitUntilStalenessOfElement(Common.searchValueTextbox, strPageName);
		clear(Common.searchValueTextbox, lngMinTimeOutInSeconds, strValue, strPageName);
		sendkeys(Common.searchValueTextbox, lngMinTimeOutInSeconds, strValue, "Textbox", strPageName,true);
	}
	/**
	 * Method to upload file
	 * 
	 * @param locator, locator of checkbox
	 * @param strFileName, File which needs to be uploaded
	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */
	public void uploadFile(String strFileName,String strPageName) {
		String strBaseDir = System.getProperty("user.dir");
		mouseOverandClick(Common.fileUpload, lngMinTimeOutInSeconds,"Choose File", "Button", driver.getTitle());
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String strFilePath = "\"" + strBaseDir + Util.getFileSeparator() + "testDataFiles" + Util.getFileSeparator()
		+ strFileName + "\"";
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		executeScript(true, strBaseDir + "\\autoIT" + Util.getFileSeparator() + "File Upload.exe", "Open",
				strFilePath);
		report.updateTestLog("Upload the files", "File should be uploaded successfully", "File is uploaded sucessfully", Status.DONE);
	}
	/**
	 * Method to get Download File path

	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */
	public void getDownloadFilePath() {
		String strFileDownloadPath = null;
		/*String strFileName = dataTable.getExpectedResult("Report_Name")+".xlsx";*/
		String strFileName = "RowCount.csv";
		String strRelativeDirectory = System.getProperty("user.dir") + Util.getFileSeparator();
		String strUserProfilePath = System.getenv("USERPROFILE");
		strFileDownloadPath = strUserProfilePath + "\\Downloads\\";
		//strFileDownloadPath = strRelativeDirectory+Util.getFileSeparator()+"downloads";
		String strParentFolder = "testDataFiles";
		File strDestinationFilePath = null;

		boolean blnFileExist;
		try {
			blnFileExist = FileExists(strFileDownloadPath + strFileName);
			if (blnFileExist) {
				File parentFolder = new File(strRelativeDirectory + strParentFolder);
				if (!parentFolder.exists()) {
					parentFolder.mkdir();
				}

				String strDestinationFolderPath = strRelativeDirectory + Util.getFileSeparator() + strParentFolder;
				strDestinationFilePath = new File(strRelativeDirectory + Util.getFileSeparator() + strParentFolder + Util.getFileSeparator() + strFileName);


				boolean blnDirFileExist = FileExists(strDestinationFilePath.getPath());
				if (blnDirFileExist) {
					new File(strDestinationFilePath.getPath()).delete();
					FileUtils.copyFile(new File(strFileDownloadPath + strFileName), strDestinationFilePath);
					new File(strFileDownloadPath + strFileName).delete();
					report.updateTestLog("Files to be moved to the required folder",strFileName+ " File should be moved to the following Path: "+strDestinationFolderPath, 
							strFileName+ "File is moved to the following Path: "+strDestinationFolderPath, Status.DONE);

				}else {
					FileUtils.copyFile(new File(strFileDownloadPath + strFileName), strDestinationFilePath);
					new File(strFileDownloadPath + strFileName).delete();
					report.updateTestLog("Files to be moved to the required folder",strFileName+ " File should be moved to the following Path: "+strDestinationFolderPath, 
							strFileName+ "File is moved to the following Path: "+strDestinationFolderPath, Status.DONE);
				}


			} else {
				ALMFunctions.ThrowException("Verify Downloaded File Exist in Userprofile Path", strFileName + " - should exist in Userprofile Path", "Error - " + strFileName + " is not exist in Userprofile Path", false);
			}
		} catch(Exception ex) {
			ALMFunctions.ThrowException("Verify Downloaded File Exist in Userprofile Path", strFileName + " - should exist in Userprofile Path", "Error - " + strFileName + " is not exist in Userprofile Path", false);
		}

	}
	/**
	 * Method to get MCO Name and store the Package Id

	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */
	public void projectName(String strValue) {
		if(strValue.equalsIgnoreCase("PackageID")) {
			String value = dataTable.getData("Parametrized_Checkpoints", "Payment Package ID:");
			value = "PKG_"+ value + String.format("%s",System.currentTimeMillis()).substring(7);
			dataTable.putData("Parametrized_Checkpoints", "PackageID ProjectName", value);
		}
	}
	/**
	 * Method to get EDI Time from Approved Time for Reschedule the time

	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */
	public void reschedule(String strValue) {
		String StrValues = dataTable.getData("Parametrized_Checkpoints", "ApprovedTime");

		String[] a = StrValues.split(" ");
		String time = a[1];

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
		cal.add(Calendar.MINUTE, 16);
		String strNewTime = df.format(cal.getTime());
		//String strUpdatedTime = String.valueOf(i);
		String strUpdatedDateAndTime = a[0]+" "+strNewTime;
		dataTable.putData("Parametrized_Checkpoints", "EDITime", strUpdatedDateAndTime);

	}
	/**
	 * Method to Refresh the page

	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */
	public void refresh(String strValue) {
		driver.switchTo().defaultContent();
		driver.findElement(Common.refresh).click();
		driverUtil.waitFor(3000);
		By Locator = Common.documentFrame;
		driverUtil.waitUntilStalenessOfElement(Locator, staleTableTimeOut, driver.getTitle());
		WebElement elmFrame = driver.findElement(Locator);
		driver.switchTo().frame(elmFrame);
	}
	/**
	 * Method to check the File Exists

	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */

	public static boolean FileExists(String strFilePath) throws InterruptedException {
		File file = new File(strFilePath);
		boolean blnFound = false;
		int counter = 0;
		if (!file.exists()) {
			while (counter <= 5) {

				if (file.exists()) {
					blnFound = true;
					break;
				}
				counter++;
			}
		}
		else {
			blnFound = true;
		}
		return blnFound;
	}

	@SuppressWarnings("unused")
	public void csvRowCount() throws IOException
	{
		System.out.println("Present Project Directory : "+ System.getProperty("user.dir"));
		String strFolderName ="testDataFiles";
		String strFileName = "RowCount.csv";
		String strCodeBasePath = System.getProperty("user.dir");
		String strFilePath = strCodeBasePath+"\\"+strFolderName+"\\"+strFileName;
		//String strFilePath = "P:\\ModelN_TestNG\\testDataFiles\\RowCount.csv";
		@SuppressWarnings("resource")
		BufferedReader bufferedReader = new BufferedReader(new FileReader(strFilePath));
		String strInput;
		int count = 0;
		while((strInput = bufferedReader.readLine()) != null)
		{
			count++;
		}
		if(count>5000) {
			report.updateTestLog("Verify file Row count",strFileName+ " File should contain more than 5000 rows", 
					strFileName+ "File contains more than 5000 rows and available row count in the sheet is: "+count, Status.PASS);
		}
		else
		{
			report.updateTestLog("Verify file Row count",strFileName+ " File should contain more than 5000 rows", 
					strFileName+ "File does not contains more than 5000 rows and available row count in the sheet is: "+count, Status.FAIL);
		}

	}
	/**
	 * Method to parse Xml and validate the values

	 * @param strPageName, Page Name in which the control is available
	 * @return No return value
	 */
	public void parseXmlAndValidate(String strValueToBeVerified , String strPageName) {

		By xmlContents = By.xpath(".//textarea[contains(@name,'document')]");
		String strXmlContent = driver.findElement(xmlContents).getText();
		if(strXmlContent.contains(strValueToBeVerified))
		{
			ALMFunctions.UpdateReportLogAndALMForPassStatus("Validate XML file", "Expected Value: '" +strValueToBeVerified +"' should be available in the XML",
					"Expected Value: '" +strValueToBeVerified +"' is available in the XML", true);
		}
		else
		{
			ALMFunctions.ThrowException("Validate XML file", "Expected Value: '" +strValueToBeVerified +"' should be available in the XML",
					"Expected Value: '" +strValueToBeVerified +"' is not available in the XML", true);	
		}



	}
	/**

	 

     * Method to select a first row in a table if row exists

 

     *

 

     * @param strElementName, The name of the field

 

     * @param strValue,     value to be selected in the field

 

     * @param strPageName,  Page Name in which the control is available

 

     * @return No return value

 

     */

 

    public void selectFirstRowinaTableIfExist(String strElementName, String strValues, String strPageName) {

 

        
        driverUtil.waitUntilStalenessOfElement(Common.selectFirstRowInTable, strPageName);
        if (objectExists(Common.selectFirstRowInTable, "isDisplayed", lngMinTimeOutInSeconds, "Link",  "First Link in Table",strPageName, false)) {
        	String strLinkName = getText(Common.selectFirstRowInTable, lngMinTimeOutInSeconds, strElementName, strPageName);
        	doubleClick(Common.selectFirstRowInTable,lngMinTimeOutInSeconds,  "Link",  strLinkName,  strPageName,true);
        }

 

 

    }
	/**

	 * Function to check whether the file got downloaded in the given path

	 * @param downloadedFile File to check

	 * @return boolean value of whether the file got downloaded or not

	 */

	public boolean checkFileDownloaded(File downloadedFile, String strFilePath, String strFileName) {

		for (long stop=System.nanoTime()+TimeUnit.SECONDS.toNanos(lngMinTimeOutInSeconds);stop>System.nanoTime();) {

			if(downloadedFile.exists()) {

				try {

					ALMFunctions.UpdateReportLogAndALMForPassStatus("Download File", "'"+strFileName+"' should be downloaded in file path: "+strFilePath, 

							"'"+"<a href="+new File(strFilePath).toURI().toURL()+">"+strFileName+"</a>' is downloaded in the file path: "+strFilePath, true);


				} catch (MalformedURLException e) {

					e.printStackTrace();

				}

				return true;    

			}

		}

		ALMFunctions.ThrowException("Download document", "'"+strFileName+"' should be downloaded in file path: "+strFilePath, 

				"'"+strFileName+"' is not downloaded in the file path: "+strFilePath+" waited for "+lngMinTimeOutInSeconds+" seconds", true);

		return false;

	}




	/**
	 * Method to retrieve record in Table
	 * 
	 * @param strColumnName, Primary column name and secondary column name to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strValues,     Primary column value and secondary column value to be
	 *                       verified should be provided with '!' in data sheet
	 * @param strPageName,   Page Name in which the control is available
	 * @return No return value
	 */
	public void retrieveRecordFromFilterTable(String strColumnName, String strValue, String strPageName) {
		String strFirstCol = "";
		String strSecondCol = "";
		String strRefernceColVal = "";
		String strExcelColumnName = "";
		String strActValue = "";
		String strRetrievedVal="";
		By row = Common.tableRow;
		By webTableHeader = null;
		int intColumnIndex = 0;
		String strTableName = "LookUp Table";


		String[] strColNames = strColumnName.split("!");
		String[] strValues = strValue.split("!");
		strFirstCol = strColNames[0];
		strSecondCol = strColNames[1];
		strRefernceColVal = strValues[0].trim();
		strExcelColumnName = strValues[1].trim();
		webTableHeader = Common.tableHeader;

		driverUtil.waitUntilStalenessOfElement(webTableHeader, lngMinTimeOutInSeconds, strPageName);

		intColumnIndex = getColumnIndex(strFirstCol, webTableHeader, strTableName, false, false)+1;

		if (!(intColumnIndex != 0)) {
			ALMFunctions.ThrowException("Get index of column name",
					"Expected column name as " + strFirstCol + " shall be displayed",
					"Expected column name as " + strFirstCol + " is not displayed", true);
		}


		boolean blnFound = false;
		boolean blnClick = false;
		int intCurrentPage = 1;
		List<WebElement> listtableRow = driver.getWebDriver().findElements(row);
		if (listtableRow.isEmpty()) {

			ALMFunctions.ThrowException(strRefernceColVal, strRefernceColVal + " table row should be displayed",
					strRefernceColVal + " table row are not displayed", true);
			ALMFunctions.ThrowException(strRefernceColVal, "" + strTableName + " Table row should be displayed",
					"" + strTableName + " Table row is NOT displayed", true);

		} else {

			boolean blnRecordNotFound = false;
			do {

				if (intCurrentPage != 1) {

					if (objectExists(Common.nextPage, "isDisplayed", lngMinTimeOutInSeconds, "Next", "Button",
							strTableName, false)) {
						click(Common.nextPage, lngMinTimeOutInSeconds, "Next", "Button", strTableName, false);
						driverUtil.waitUntilStalenessOfElement(Common.nextPage, strTableName);

					} else {
						blnRecordNotFound = true;
					}

				}

				listtableRow = driver.getWebDriver().findElements(row);
				WebElement eleRows = null;

				for (WebElement rows : listtableRow) {

					strActValue = rows.findElement(By.xpath("(.//td[" + intColumnIndex + "]//select)|(.//td[" + intColumnIndex + "]//span)|(.//td[" + intColumnIndex + "]//a)")).getText().trim();

					if (strRefernceColVal.contains("contains")) {
						strRefernceColVal = strRefernceColVal.replaceAll("contains", "");
						if (strActValue.contains(strRefernceColVal)) {
							eleRows = rows;
							blnFound = true;

						}

					} else if (strActValue.equals(strRefernceColVal.trim())) {
						eleRows = rows;
						blnFound = true;

					}


					if (blnFound) {
						webTableHeader = null;
						int intColumnIndex1 = 0;
						webTableHeader = Common.tableHeader;

						intColumnIndex1 = getColumnIndex(strSecondCol, webTableHeader, strTableName, false, false)+1;

						if (!(intColumnIndex1 != 0)) {
							ALMFunctions.ThrowException("Get index of column name",
									"Expected column name as " + strSecondCol + " shall be displayed",
									"Expected column name as " + strSecondCol + " is not displayed", true);
						}

						WebElement eleClick = eleRows.findElement(By.xpath("(.//td[" + intColumnIndex1 + "]//select)|(.//td[" + intColumnIndex1 + "]//a)|(.//td[" + intColumnIndex1 + "]//span)"));
						strRetrievedVal = eleClick.getText().trim();
						dataTable.putData("Parametrized_Checkpoints", strExcelColumnName, strRetrievedVal);
						blnClick = true;
						break;


					}

				}

				intCurrentPage++;
			} while (!(blnClick || blnRecordNotFound));


			if (blnClick) {
				report.updateTestLog("Capture the " + strSecondCol + " value from table",
						"'"+strSecondCol + "' value should be captured from table",
						"captured Value from table for '" + strSecondCol + "' : '" + strRetrievedVal+"'", Status.DONE);

			} else {
				ALMFunctions.ThrowException("Click on " + strRefernceColVal + " Table row",
						"Created From Trigger : " + strActValue + " in " + strTableName
						+ " Table row should be clicked on " + strTableName + "",
						"Error - Specified file in table row is NOT clicked on " + strTableName + " Page", true);
			}

		}

	}


}