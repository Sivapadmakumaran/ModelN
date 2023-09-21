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

public class MDMFlow extends CommonFunctions{

	public MDMFlow(ScriptHelper scriptHelper) {
		super(scriptHelper);
		// TODO Auto-generated constructor stub
	}
	public final long lngMinTimeOutInSeconds = Long.parseLong(properties.getProperty("MinObjectSyncTimeout"));
	public final long lngCtrlTimeOutInSeconds = Long.parseLong(properties.getProperty("UploadControlTimeout"));
	public final long jobTimeOut = Long.parseLong(properties.getProperty("JobTimeOut"));
	public final long staleTableTimeOut = Long.parseLong(properties.getProperty("StaleTableTimeOut")); 

	/**
	 * Function to call Fill form Utility to create Customer
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void createCustomer() {
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "createCustomer";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	/**
	 * Function to call Fill form Utility to create Product
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void createProduct() {
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "createProduct";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	
	/**
	 * Function to call Fill form Utility to create Price List
	 * 
	 * @param No parameter
	 * @return No return value
	 */
	public void createPriceList() {
		CommonFunctions fillFormExcelFunctions = new CommonFunctions(scriptHelper);
		String strInputParameter = "createPriceList";
		fillFormExcelFunctions.FillInputForm(strInputParameter);

	}
	/**
	 * Generate New price List name value and update it, retrieve NDC value from Data sheet and update it in Price List CSV file
	 * 
	 * 
	 * @throws IOException
	 */
	public void updatepricelistCSV() throws IOException {

		String strCodeBasePath = System.getProperty("user.dir");
		String strCsvFileName = "pricelist.csv";
		String strPcb = strCodeBasePath + Util.getFileSeparator() + "testDataFiles";
		String strCsvFilePath=strPcb + Util.getFileSeparator() + strCsvFileName;
		int min = 1000; 
		int max = 9999;  
		int random_int = (int)Math.floor(Math.random() * (max - min + 1) + min);
		String strFinal4PriceListChar = String.valueOf(random_int);
		String strNewVal = "PriceList"+strFinal4PriceListChar;
		dataTable.putData("Parametrized_Checkpoints", "PriceListName", strNewVal);
		String strNDC11 = dataTable.getData("Parametrized_Checkpoints", "NDC11");
		int intRowNo = 10;
		int intSecRowNo = 10;
		int intNDCRowNo = 16;
		int intColNo = 0;
		int intSecColNo = 1;

		File inputFile = new File(strCsvFilePath);
		boolean blnFileExist;

		try {
			blnFileExist = FileExists(strCsvFilePath);
			if(blnFileExist)
			{
				CSVReader reader = new CSVReader(new FileReader(inputFile), ',');
				List<String[]> csvBody = reader.readAll();
				csvBody.get(intRowNo)[intColNo] = strNewVal;
				csvBody.get(intSecRowNo)[intSecColNo] = strNewVal;
				csvBody.get(intNDCRowNo)[intColNo] = strNDC11;
				reader.close();

				CSVWriter writer = new CSVWriter(new FileWriter(inputFile), ',');
				writer.writeAll(csvBody);
				writer.flush();
				writer.close();
				report.updateTestLog("Update Price List CSV File", "Price List Name, NDC11 Column values should be updated in CSV",
						 "Price List Name: " +strNewVal+", NDC11: "+strNDC11 +" Column values are updated in CSV", Status.DONE);
			}
			else
			{
				ALMFunctions.ThrowException("Verify File Exist","Expected File " +strCsvFileName+" should be available to edit in this path: "+strPcb, 
						"Expected File " +strCsvFileName+" is not available to edit in this path: "+strPcb,true);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



	}
	/**
	 * Generate NDC9, NDC11 values every time newly and update it in Product XML file
	 * 
	 * 
	 * @throws IOException
	 */
	public void updateProductXML() throws IOException, InterruptedException {
		String strFirst5NDCChar = "61958";
		int min = 1000; 
		int max = 9999;  
		int random_int = (int)Math.floor(Math.random() * (max - min + 1) + min);
		String strFinal4NDCChar = String.valueOf(random_int);
		String strNDC9 = strFirst5NDCChar+strFinal4NDCChar;
		int min1 = 10; 
		int max1 = 99;  
		int random_int1 = (int)Math.floor(Math.random() * (max1 - min1 + 1) + min1);
		String strFinal2NDCChar = String.valueOf(random_int1);
		String strNDC11 = strNDC9+strFinal2NDCChar;
		dataTable.putData("Parametrized_Checkpoints", "NDC9", strNDC9);
		dataTable.putData("Parametrized_Checkpoints", "NDC11", strNDC11);
		String strCodeBasePath = System.getProperty("user.dir");
		String strCsvFileName = "GdCategoryFromXml.xml";
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
			NodeList nodeList = doc.getElementsByTagName("Category");
			int tLength = nodeList.getLength();
			Node node = nodeList.item(2);

			if(node.getNodeType()==Node.ELEMENT_NODE){
				Element element = (Element)node;
				NamedNodeMap elmList = element.getAttributes();
				for(int i=0;i<elmList.getLength();i++)
				{


					if(elmList.item(i).getNodeName().equalsIgnoreCase("NDC")) {
						elmList.item(i).setTextContent(strNDC9);
					}
					if(elmList.item(i).getNodeName().equalsIgnoreCase("ProductNum")) {
						elmList.item(i).setTextContent(strNDC9);
					}
				}
			}


			
			NodeList fieldNodeList = doc.getElementsByTagName("Field");
			Node node10 = fieldNodeList.item(10);
			if(node10.getNodeType()==Node.ELEMENT_NODE){
				Element element = (Element)node10;
				element.setAttribute("Value", strNDC9);
			}

			NodeList mapItemNodeList = doc.getElementsByTagName("MapItem");
			Node ndcNode = mapItemNodeList.item(0);
			if(ndcNode.getNodeType()==Node.ELEMENT_NODE){
				Element element = (Element)ndcNode;
				element.setAttribute("NDC", strNDC11);
				element.setAttribute("ProductNum", strNDC11);
			}

			Node node15 = fieldNodeList.item(15);
			if(node15.getNodeType()==Node.ELEMENT_NODE){
				Element element = (Element)node15;
				element.setAttribute("Value", strNDC11);
			}

			Node node20 = fieldNodeList.item(20);
			if(node20.getNodeType()==Node.ELEMENT_NODE){
				Element element = (Element)node20;
				element.setAttribute("Value", strNDC9);
			}

			Node node21 = fieldNodeList.item(21);
			if(node21.getNodeType()==Node.ELEMENT_NODE){
				Element element = (Element)node21;
				element.setAttribute("Value", strNDC11);
			}

			Node node22 = fieldNodeList.item(22);
			if(node22.getNodeType()==Node.ELEMENT_NODE){
				Element element = (Element)node22;
				String strCurrentNDC = element.getAttribute("Value");
				element.setAttribute("Value", strNDC11);
			}



			try{
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File(strCsvFilePath));
				transformer.transform(source, result);
				report.updateTestLog("Update Product XML File", "NDC9, NDC11 values should be updated in XML",
						 "NDC9: " +strNDC9+", NDC11: "+strNDC11 +" values are updated in XML", Status.DONE);
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

	/**
	 * Generate Customer Full Name, Customer ID,Identifier values every time newly and update it in Customer XML file
	 * 
	 * 
	 * @throws IOException
	 */
	public void updateCustomerXML() throws IOException, InterruptedException {
		
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("hhmmssM");
		String strMemberName = ft.format(dNow);

		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 5;
		int targetStringLength1 = 2;
		int targetStringLength2 = 3;
		Random random = new Random();
		String strFullName = "McKesson Plasma and Biologics Sheperdsville KY";
		String LastName = random.ints(leftLimit, rightLimit + 1).limit(targetStringLength).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
		String strId1 = random.ints(leftLimit, rightLimit + 1).limit(targetStringLength1).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
		String strId2 = random.ints(leftLimit, rightLimit + 1).limit(targetStringLength2).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
		
		strFullName = strFullName+" "+LastName;
		int min1 = 10; 
		int max1 = 99;  
		int random_int1 = (int)Math.floor(Math.random() * (max1 - min1 + 1) + min1);
		String strFirst2Nos = String.valueOf(random_int1);
		int min = 10; 
		int max = 99;  
		int random_int = (int)Math.floor(Math.random() * (max - min + 1) + min);
		String strFinal2Nos = String.valueOf(random_int);
		String strIdentifier = strId1+strFirst2Nos+strId2+strFinal2Nos;
		dataTable.putData("Parametrized_Checkpoints", "MemberName", strMemberName);
		dataTable.putData("Parametrized_Checkpoints", "FullName", strFullName);
		dataTable.putData("Parametrized_Checkpoints", "Identifier", strIdentifier);

		String strCodeBasePath = System.getProperty("user.dir");
		String strCsvFileName = "GdCommunityWithIncrCotFromXml.xml";
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
			NodeList nodeList = doc.getElementsByTagName("imp1:Organization");
			Node node = nodeList.item(0);

			if(node.getNodeType()==Node.ELEMENT_NODE){
				Element element = (Element)node;
				NamedNodeMap elmList = element.getAttributes();
				for(int i=0;i<elmList.getLength();i++)
				{


					if(elmList.item(i).getNodeName().equalsIgnoreCase("MemberName")) {
						elmList.item(i).setTextContent(strMemberName);
					}
					if(elmList.item(i).getNodeName().equalsIgnoreCase("FullName")) {
						elmList.item(i).setTextContent(strFullName);
					}
				}
			}

			NodeList nodeOrgIDList = doc.getElementsByTagName("imp1:OrganizationId");
			Node nodeOrgID = nodeOrgIDList.item(0);

			if(nodeOrgID.getNodeType()==nodeOrgID.ELEMENT_NODE){
				Element element = (Element)nodeOrgID;
				NamedNodeMap elmList = element.getAttributes();
				for(int i=0;i<elmList.getLength();i++)
				{


					if(elmList.item(i).getNodeName().equalsIgnoreCase("Identifier")) {
						elmList.item(i).setTextContent(strIdentifier);
						break;
					}
					
				}
			}
			try{
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(new File(strCsvFilePath));
				transformer.transform(source, result);
				report.updateTestLog("Update Customer XML File", "Member Name, Member Full Name, Idetifier values should be updated in XML",
						 "Member Name: " +strMemberName+", Member Full Name: "+strFullName +", Idetifier: "+strIdentifier +" values are updated in XML", Status.DONE);
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
