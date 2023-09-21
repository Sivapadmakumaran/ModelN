package models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Entries", 
	namespace = "http://xmlns.modeln.com/ApplicationObjects/ManagedCareEstimatedFFSIFFAck/V1")
public class Entries {


	private ERPPayment erpPayment;

	public Entries() {
		super();
	}

	public Entries(ERPPayment erpPayment) {
		super();
		this.erpPayment = erpPayment;
	}

	@XmlElement(name = "ERPPayment", 
			namespace = "http://xmlns.modeln.com/ApplicationObjects/ManagedCareEstimatedFFSIFFAck/V1" )
	public ERPPayment getErpPayment() {
		return erpPayment;
	}

	public void setErpPayment(ERPPayment erpPayment) {
		this.erpPayment = erpPayment;
	}

	
	
	
}
