package models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Entries", 
	namespace = "http://xmlns.modeln.com/ApplicationObjects/MCDCheckAck/V1")
public class EntriesMediAck {


	private McdCheckAck mcdCheckAck;

	public EntriesMediAck() {
		super();
	}

	public EntriesMediAck(McdCheckAck mcdCheckAck) {
		super();
		this.mcdCheckAck = mcdCheckAck;
	}

	
	public McdCheckAck getMcdCheckAck() {
		return mcdCheckAck;
	}

	public void setMcdCheckAck(McdCheckAck mcdCheckAck) {
		this.mcdCheckAck = mcdCheckAck;
	}

	
	
}
