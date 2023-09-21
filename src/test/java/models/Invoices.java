package models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"vendorName", "invoiceNotes", "approverNTLogin",
		"invoiceAmount", "source", "invoiceType", "currencyCode", "vendorPayToSiteID", 
		"legalEntityID", "paymentMethod", "paymentHandling", "invoiceDate", "invoiceNumber", "lineItems" })
public class Invoices {
	private String vendorName;
	private String invoiceNotes;
	private String approverNTLogin;
	private String invoiceAmount;
	private String source;
	private String invoiceType;
	private String currencyCode;
	private String vendorPayToSiteID;
	private String legalEntityID;
	private String paymentMethod;
	private String paymentHandling;
	private String invoiceDate;
	private String invoiceNumber;
	private LineItems lineItems;
	
	public Invoices(String vendorName, String invoiceNotes, String approverNTLogin, String invoiceAmount, String source,
			String invoiceType, String currencyCode, String vendorPayToSiteID, String legalEntityID,
			String paymentMethod, String paymentHandling, String invoiceDate, String invoiceNumber,
			LineItems lineItems) {
		super();
		this.vendorName = vendorName;
		this.invoiceNotes = invoiceNotes;
		this.approverNTLogin = approverNTLogin;
		this.invoiceAmount = invoiceAmount;
		this.source = source;
		this.invoiceType = invoiceType;
		this.currencyCode = currencyCode;
		this.vendorPayToSiteID = vendorPayToSiteID;
		this.legalEntityID = legalEntityID;
		this.paymentMethod = paymentMethod;
		this.paymentHandling = paymentHandling;
		this.invoiceDate = invoiceDate;
		this.invoiceNumber = invoiceNumber;
		this.lineItems = lineItems;
	}	
	
	public Invoices() {
		super();
	}

	@XmlElement(name = "VendorName")
	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	@XmlElement(name = "InvoiceNotes")
	public String getInvoiceNotes() {
		return invoiceNotes;
	}

	public void setInvoiceNotes(String invoiceNotes) {
		this.invoiceNotes = invoiceNotes;
	}

	@XmlElement(name = "ApproverNTLogin")
	public String getApproverNTLogin() {
		return approverNTLogin;
	}

	public void setApproverNTLogin(String approverNTLogin) {
		this.approverNTLogin = approverNTLogin;
	}

	@XmlElement(name = "InvoiceAmount")
	public String getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(String invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	@XmlElement(name = "Source")
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@XmlElement(name = "InvoiceType")
	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	@XmlElement(name = "CurrencyCode")
	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	@XmlElement(name = "VendorPayToSiteID")
	public String getVendorPayToSiteID() {
		return vendorPayToSiteID;
	}

	public void setVendorPayToSiteID(String vendorPayToSiteID) {
		this.vendorPayToSiteID = vendorPayToSiteID;
	}

	public String getLegalEntityID() {
		return legalEntityID;
	}

	public void setLegalEntityID(String legalEntityID) {
		this.legalEntityID = legalEntityID;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPaymentHandling() {
		return paymentHandling;
	}

	public void setPaymentHandling(String paymentHandling) {
		this.paymentHandling = paymentHandling;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	@XmlElement(name = "InvoiceNumber")
	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	@XmlElement(name = "LineItems")
	public LineItems getLineItems() {
		return lineItems;
	}

	public void setLineItems(LineItems lineItems) {
		this.lineItems = lineItems;
	}
	
	
	
}
