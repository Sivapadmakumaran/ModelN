package models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "McdCheckAck", propOrder = {"payNum", "checkGenerated", "checkNum","checkDate"})
public class McdCheckAck {

	private String payNum;
	private String checkGenerated;
	private String checkNum;
	private String checkDate;
		
	public McdCheckAck() {
		super();
	}
	
	
	
	public McdCheckAck(String payNum, String checkGenerated, String checkNum, String checkDate) {
		super();
		this.payNum = payNum;
		this.checkGenerated = checkGenerated;
		this.checkNum = checkNum;
		this.checkDate = checkDate;
		
	}



	@XmlAttribute(name="PayNum")
	public String getPayNum() {
		return payNum;
	}
	public void setPayNum(String payNum) {
		this.payNum = payNum;
	}
	
	@XmlAttribute(name="CheckGenerated")
	public String getCheckGenerated() {
		return checkGenerated;
	}
	public void setCheckGenerated(String checkGenerated) {
		this.checkGenerated = checkGenerated;
	}
	
	@XmlAttribute(name="CheckNum")
	public String getCheckNum() {
		return checkNum;
	}
	public void setCheckNum(String checkNum) {
		this.checkNum = checkNum;
	}
	
	@XmlAttribute(name="CheckDate")
	public String getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}
	
	
	
	
}
