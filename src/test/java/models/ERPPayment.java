package models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "ERPPayment", propOrder = {"paymentAmount", "mnPackageId", "erpProcessDate",
		"erpPaymentType", "erpPaymentNumber", "erpPaymentMethod", "erpPaymentDate"  
})
public class ERPPayment {

	private String paymentAmount;
	private String mnPackageId;
	private String erpProcessDate;
	private String erpPaymentType;
	private String erpPaymentNumber;
	private String erpPaymentMethod;
	private String erpPaymentDate;
	
	public ERPPayment() {
		super();
	}
	
	
	
	public ERPPayment(String paymentAmount, String mnPackageId, String erpProcessDate, String erpPaymentType,
			String erpPaymentNumber, String erpPaymentMethod, String erpPaymentDate) {
		super();
		this.paymentAmount = paymentAmount;
		this.mnPackageId = mnPackageId;
		this.erpProcessDate = erpProcessDate;
		this.erpPaymentType = erpPaymentType;
		this.erpPaymentNumber = erpPaymentNumber;
		this.erpPaymentMethod = erpPaymentMethod;
		this.erpPaymentDate = erpPaymentDate;
	}



	@XmlAttribute(name="PaymentAmount")
	public String getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	
	@XmlAttribute(name="MNPackageId")
	public String getMnPackageId() {
		return mnPackageId;
	}
	public void setMnPackageId(String mnPackageId) {
		this.mnPackageId = mnPackageId;
	}
	
	@XmlAttribute(name="ERPProcessDate")
	public String getErpProcessDate() {
		return erpProcessDate;
	}
	public void setErpProcessDate(String erpProcessDate) {
		this.erpProcessDate = erpProcessDate;
	}
	
	@XmlAttribute(name="ERPPaymentType")
	public String getErpPaymentType() {
		return erpPaymentType;
	}
	public void setErpPaymentType(String erpPaymentType) {
		this.erpPaymentType = erpPaymentType;
	}
	
	@XmlAttribute(name="ERPPaymentNumber")
	public String getErpPaymentNumber() {
		return erpPaymentNumber;
	}
	public void setErpPaymentNumber(String erpPaymentNumber) {
		this.erpPaymentNumber = erpPaymentNumber;
	}
	
	@XmlAttribute(name="ERPPaymentMethod")
	public String getErpPaymentMethod() {
		return erpPaymentMethod;
	}
	public void setErpPaymentMethod(String erpPaymentMethod) {
		this.erpPaymentMethod = erpPaymentMethod;
	}
	
	@XmlAttribute(name="ERPPaymentDate")
	public String getErpPaymentDate() {
		return erpPaymentDate;
	}
	public void setErpPaymentDate(String erpPaymentDate) {
		this.erpPaymentDate = erpPaymentDate;
	}
	
	
}
