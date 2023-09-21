package com.cognizant.framework;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import models.Entries;
import models.EntriesMediAck;
import models.ImportInvoice;
import models.MyNamespaceMapper;

public class XMLDataAccess {

	public boolean validateXmlDocument(String strDocument, String claim) {
		String claimNumber = null;
		String contractPrice = null;
		String currency = null;
		String lineApprovedRebate = null;
		String preferredEndCustomerId = null;
		String productNum = null;

		// Document document = convertStringToXMLDocument(strDocument);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(strDocument));

			Document document = db.parse(is);
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "/Entries/DistRebatesClaimLine";
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
			// NodeList nodeList = document.getElementsByTagName("Entries");

			System.out.println("Root Node : " + document.getFirstChild().getNodeName());

			// NodeList nodeList = document.getElementsByTagName("Entries");

			for (int index = 0; index < nodeList.getLength(); index++) {

				Node node = nodeList.item(index);
				System.out.println("\nNode Name : " + node.getNodeName());
				if (node.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) node;
					NamedNodeMap map = eElement.getAttributes();

					if (map != null) {
						claimNumber = map.getNamedItem("ClaimNum").getNodeValue();
						if (claimNumber.equalsIgnoreCase(claim)) {
							return true;
						}
//						contractPrice = map.getNamedItem("ContractPrice").getNodeValue();
//						currency = map.getNamedItem("Currency").getNodeValue();
//						lineApprovedRebate = map.getNamedItem("LineApprovedRebate").getNodeValue();
//						preferredEndCustomerId = map.getNamedItem("PreferredEndCustomerId").getNodeValue();
//						productNum = map.getNamedItem("ProductNum").getNodeValue();
//						String attrValues = String.format("%s  %s %s %s %s %s", claimNumber, contractPrice, currency,
//								lineApprovedRebate, preferredEndCustomerId, productNum);
//						System.out.println(attrValues);
					}

				}
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public Map<String, String> validateChargebackClaimDocument(String strDocument, String claimNumber,
			String xpathExpression) {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(strDocument));

			Document document = db.parse(is);
			XPath xPath = XPathFactory.newInstance().newXPath();
			// String expression = "/Entries/ChargebackClaim";
			NodeList nodeList = (NodeList) xPath.compile(xpathExpression).evaluate(document, XPathConstants.NODESET);

			System.out.println("Root Node : " + document.getFirstChild().getNodeName());

			Map<String, String> fieldValues = new LinkedHashMap<String, String>();

			for (int index = 0; index < nodeList.getLength(); index++) {

				Node node = nodeList.item(index);
				System.out.println("\nNode Name : " + node.getNodeName());
				if (node.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) node;
					NamedNodeMap map = eElement.getAttributes();

					if (map != null) {

						String value = map.getNamedItem("ClaimRefNum").getNodeValue();

						if (value.equalsIgnoreCase(claimNumber)) {
							fieldValues.put("ClaimRefNum", value);

							return fieldValues;
						}
					}

				}
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	public String validateInvoiceNumber(String strDocument, String claimNumber,
			String xpathExpression) {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		String invoice="";
		try {
			db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(strDocument));

			Document document = db.parse(is);
			XPath xPath = XPathFactory.newInstance().newXPath();
			// String expression = "/Entries/ChargebackClaim";
			NodeList nodeList = (NodeList) xPath.compile(xpathExpression).evaluate(document, XPathConstants.NODESET);

			System.out.println("Root Node : " + document.getFirstChild().getNodeName());

			Map<String, String> fieldValues = new LinkedHashMap<String, String>();

			for (int index = 0; index < nodeList.getLength(); index++) {

				Node node = nodeList.item(index);
				System.out.println("\nNode Name : " + node.getNodeName());
				if (node.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) node;
					invoice=eElement.getTextContent();

					System.out.println(invoice);

				}
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return invoice;
	}
	public String validateInvoiceAmount(String strDocument, String claimNumber,
			String xpathExpression) {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		String invoice="";
		try {
			db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(strDocument));

			Document document = db.parse(is);
			XPath xPath = XPathFactory.newInstance().newXPath();
			// String expression = "/Entries/ChargebackClaim";
			NodeList nodeList = (NodeList) xPath.compile(xpathExpression).evaluate(document, XPathConstants.NODESET);

			System.out.println("Root Node : " + document.getFirstChild().getNodeName());

			Map<String, String> fieldValues = new LinkedHashMap<String, String>();

			for (int index = 0; index < nodeList.getLength(); index++) {

				Node node = nodeList.item(index);
				System.out.println("\nNode Name : " + node.getNodeName());
				if (node.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) node;
					invoice=eElement.getTextContent();

					System.out.println(invoice);

				}
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return invoice;
	}
//	public Map<String, String> validateXMLHeaders(List<String> headers, String strXmlDocument, String xpathExpression) {
//
//		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//		DocumentBuilder db;
//		try {
//			db = dbf.newDocumentBuilder();
//
//			InputSource is = new InputSource();
//			is.setCharacterStream(new StringReader(strXmlDocument));
//
//			Document document = db.parse(is);
//			XPath xPath = XPathFactory.newInstance().newXPath();
//			NodeList nodeList = (NodeList) xPath.compile(xpathExpression).evaluate(document, XPathConstants.NODESET);
//
//			System.out.println("Root Node : " + document.getFirstChild().getNodeName());
//
//			Map<String, String> fieldValues = new LinkedHashMap<String, String>();
//
//			for (int index = 0; index < nodeList.getLength(); index++) {
//
//				Node node = nodeList.item(index);
//				System.out.println("\nNode Name : " + node.getNodeName());
//				if (node.getNodeType() == Node.ELEMENT_NODE) {
//
//					Element eElement = (Element) node;
//					NamedNodeMap map = eElement.getAttributes();
//
//					if (map != null) {
//
//						String value = map.getNamedItem("ClaimRefNum").getNodeValue();
//
//						if (value.equalsIgnoreCase(claimNumber)) {
//							fieldValues.put("ClaimRefNum", value);
//
//							return fieldValues;
//						}
//					}
//
//				}
//			}
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (XPathExpressionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return null;
//	}
	
	public Map<String, String> validateXMLHeaders(List<String> headers, File filePath, String xpathExpression) {

		Map<String, String> resultMap = new LinkedHashMap<String, String>();
		File xmlFile = new File(filePath.getAbsolutePath());
		DocumentBuilderFactory dbFactory = null;
		DocumentBuilder builder = null;
		try {
			if (xmlFile.exists()) {
				dbFactory = DocumentBuilderFactory.newInstance();

				builder = dbFactory.newDocumentBuilder();
				Document xmlDocument = builder.parse(xmlFile);
				xmlDocument.getDocumentElement().normalize();
				XPath xpath = XPathFactory.newInstance().newXPath();
				NodeList nodeList = (NodeList) xpath.compile(xpathExpression).evaluate(xmlDocument,
						XPathConstants.NODESET);
				for (int index = 0; index < nodeList.getLength(); index++) {
					Node node = nodeList.item(index);

					if (node.getNodeType() == Node.ELEMENT_NODE) {

						Element eElement = (Element) node;
						NamedNodeMap map = eElement.getAttributes();

						if (map != null) {

							for (String attributeHeader : headers) {
								String value = map.getNamedItem(attributeHeader).getNodeValue();
								value = (value == null) ? "" : value;
								resultMap.put(attributeHeader, value);
							}

						} 
					}
				}
			} else {
				return null;
			}

		} catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public ImportInvoice unMarshallImportInvoice(String xmlString) {
		JAXBContext context;
		ImportInvoice invoice = null;
		try {
			context = JAXBContext.newInstance(ImportInvoice.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(xmlString));
			invoice = (ImportInvoice) unmarshaller.unmarshal(reader);
			System.out.println(invoice.getInvoices().getInvoiceNotes());
			
		} catch (JAXBException | XMLStreamException | FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
			
		}
		
		return invoice;
	}
	
	
	public Entries unMarshallEntries(String xmlString) {
		JAXBContext context;
		Entries entries = null;
		try {
			context = JAXBContext.newInstance(Entries.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(xmlString));
			entries = (Entries) unmarshaller.unmarshal(reader);
			System.out.println(entries.getErpPayment().getPaymentAmount());
			
		} catch (JAXBException | XMLStreamException | FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
			
		}
		
		return entries;
	}
	
	
	public boolean marshallGdERPPmtStatusFromXml(String erpFilePath, Entries entries) {
		JAXBContext context;
		Marshaller marshaller;
		try {
			context = JAXBContext.newInstance(Entries.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			try {
	            marshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", new MyNamespaceMapper());
	        } catch(PropertyException e) {
	            // In case another JAXB implementation is used
	        }
			FileOutputStream fileOutputStream;
			fileOutputStream = new FileOutputStream(new File(erpFilePath));
			marshaller.marshal(entries, fileOutputStream);
			fileOutputStream.close();
			return true;
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public boolean mediAckXml(String mediAckFile, EntriesMediAck entries) {
		JAXBContext context;
		Marshaller marshaller;
		try {
			context = JAXBContext.newInstance(EntriesMediAck.class);
			marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			try {
	            marshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", new MyNamespaceMapper());
	        } catch(PropertyException e) {
	            // In case another JAXB implementation is used
	        }
			FileOutputStream fileOutputStream;
			fileOutputStream = new FileOutputStream(new File(mediAckFile));
			marshaller.marshal(entries, fileOutputStream);
			fileOutputStream.close();
			return true;
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	

}
