package models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;


public class MarshallEntries {

	public static void main(String[] args) {
		
		MarshallEntries mEntries = new MarshallEntries();
		ERPPayment payment = new ERPPayment("90392.54|USD",
				"0134970-001",
				"2023-01-01",
				"issued",
				"2023040203",
				"Check",
				"2023-01-01");
		Entries entries = new Entries(payment);
		
		try {
			JAXBContext context = JAXBContext.newInstance(Entries.class);
			mEntries.marshall(context, entries);

			//mInvoices.unMarshall(context);
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void marshall(JAXBContext context, Entries entries) {
		Marshaller marshaller;
		try {
			marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			try {
	            marshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", new MyNamespaceMapper());
	            //m.setProperty("com.sun.xml.bind.namespacePrefixMapper", new MyNamespaceMapper());
	        } catch(PropertyException e) {
	            // In case another JAXB implementation is used
	        }
			FileOutputStream fileOutputStream;
			fileOutputStream = new FileOutputStream(new File("GdERPPmtStatus.xml"));
			marshaller.marshal(entries, fileOutputStream);
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
	}
	
	private Entries unMarshallEntries(JAXBContext context) {

		Entries entries = null;
		try {
			String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\r\n"
					+ "<Entries xmlns=\"http://xmlns.modeln.com/ApplicationObjects/ManagedCareEstimatedFFSIFFAck/V1\">\r\n"
					+ " � �<ERPPayment PaymentAmount=\"6298100.97|USD\" MNPackageId=\"0134980-001\" ERPProcessDate=\"2023-05-25\" ERPPaymentType=\"issued\" ERPPaymentNumber=\"9.267246187E9\" ERPPaymentMethod=\"Check\" ERPPaymentDate=\"2023-05-25\"/>\r\n"
					+ "</Entries>";
			
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
}
