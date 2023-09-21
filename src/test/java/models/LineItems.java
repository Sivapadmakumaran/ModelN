package models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"description", "productCode", "amount", "locationCode", 
		"quantity", "lineNumber", "costCenter", "naturalAccountCode","lineTypeLookupCode" })
public class LineItems {

	private String description;
	private String productCode;
	private String amount;
	private String locationCode;
	private int quantity;
	private int lineNumber;
	private String costCenter;
	private String naturalAccountCode;
	private String lineTypeLookupCode;
	
	public LineItems() {
		super();
	}

	public LineItems(String description, String productCode, String amount, String locationCode, int quantity,
			int lineNumber, String costCenter, String naturalAccountCode, String lineTypeLookupCode) {
		super();
		this.description = description;
		this.productCode = productCode;
		this.amount = amount;
		this.locationCode = locationCode;
		this.quantity = quantity;
		this.lineNumber = lineNumber;
		this.costCenter = costCenter;
		this.naturalAccountCode = naturalAccountCode;
		this.lineTypeLookupCode = lineTypeLookupCode;
	}

	@XmlElement(name = "Description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElement(name = "ProductCode")
	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	@XmlElement(name = "Amount")
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@XmlElement(name = "LocationCode")
	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	@XmlElement(name = "Quantity")
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@XmlElement(name = "LineNumber")
	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	@XmlElement(name = "CostCenter")
	public String getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	@XmlElement(name = "NaturalAccountCode")
	public String getNaturalAccountCode() {
		return naturalAccountCode;
	}

	public void setNaturalAccountCode(String naturalAccountCode) {
		this.naturalAccountCode = naturalAccountCode;
	}

	@XmlElement(name = "LineTypeLookupCode")
	public String getLineTypeLookupCode() {
		return lineTypeLookupCode;
	}

	public void setLineTypeLookupCode(String lineTypeLookupCode) {
		this.lineTypeLookupCode = lineTypeLookupCode;
	}
	
}
