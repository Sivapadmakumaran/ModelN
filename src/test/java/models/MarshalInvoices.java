package models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class MarshalInvoices {

	public static void main(String[] args) {
		MarshalInvoices mInvoices = new MarshalInvoices();
		Invoices invoices = mInvoices.getInvoices();
		ImportInvoice importInvoice = new ImportInvoice(invoices);

		
		try {
			JAXBContext context = JAXBContext.newInstance(ImportInvoice.class);
			mInvoices.marshall(context, importInvoice);

			//mInvoices.unMarshall(context);
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}
	
	private void marshall(JAXBContext context, ImportInvoice importInvoice) {
		Marshaller marshaller;
		try {
			marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			FileOutputStream fileOutputStream;
			fileOutputStream = new FileOutputStream(new File("ImportInvoice.xml"));
			marshaller.marshal(importInvoice, fileOutputStream);
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void unMarshall(JAXBContext context) {
		
		String strDocument = null;
		String pth = System.getProperty("user.dir");
		try {
		
		String xmlString = new String(Files.readAllBytes(Paths.get(pth, "ImportInvoice.xml")), StandardCharsets.UTF_8);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(xmlString));
		ImportInvoice invoice = (ImportInvoice) unmarshaller.unmarshal(reader);
		System.out.println(invoice.getInvoices().getInvoiceNotes());
		
//		try {
//			strDocument = new String(Files.readAllBytes(Paths.get(pth, "ImportInvoice.xml")), StandardCharsets.UTF_8);
//			Unmarshaller unmarshaller = context.createUnmarshaller();
//			XMLStreamReader reader;
//			try {
//				reader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(strDocument));
//				ImportInvoice invoice;
//				invoice = (ImportInvoice) unmarshaller.unmarshal(reader);
//				System.out.println(invoice.getInvoices().getInvoiceNotes());
//			} catch (XMLStreamException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (FactoryConfigurationError e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	}

	private LineItems getLineItems() {

		return new LineItems("PP for HOS160645679 05/18/23 06:55 AM", "1034", "2821930.27", "US999", 1, 1, "0000",
				"23725", "ITEM");
	}

	private Invoices getInvoices() {
		LineItems lineItems = getLineItems();
		return new Invoices("Ascent Health Services LLC", "Invoice:0134973-001", "skesavan", "2821930.27", "MODELN-MC",
				"STANDARD", "USD", "181699", "001", "Wire", "", "05/19/2023", "0134973-001", lineItems);
	}

}
