package businesscomponents;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.cognizant.craft.ScriptHelper;
import com.cognizant.framework.FileLockMechanism;
import com.cognizant.framework.FrameworkException;
import com.cognizant.framework.Util;

public class FillFormExcelFunctions extends CommonFunctions {

	public FillFormExcelFunctions(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
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
	
	
	
}
