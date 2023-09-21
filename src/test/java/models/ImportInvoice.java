package models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ImportInvoice")
public class ImportInvoice {

	private Invoices Invoices;

	public ImportInvoice(Invoices invoices) {
		super();
		Invoices = invoices;
	}
	
	public ImportInvoice() {
		super();
	}

	@XmlElement(name = "Invoices")
	public Invoices getInvoices() {
		return Invoices;
	}

	public void setInvoices(Invoices invoices) {
		Invoices = invoices;
	}
	
	
}
