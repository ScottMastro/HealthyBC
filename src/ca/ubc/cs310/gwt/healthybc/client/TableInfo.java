package ca.ubc.cs310.gwt.healthybc.client;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TableInfo implements Serializable {

	private String refID;
	private String name;
	private String addr;
	private String email;
	private String[] languages;

	
	/**
	 * Don't delete this; GWT needs this constructor to be here.
	 */
	protected TableInfo() {
	}
	
	public TableInfo(String refID, String name, String address, String email, String[] languages) {
		this.refID = refID;
		this.name = name;
		this.addr = address;
		this.email = email;
		this.languages = languages;
	}
	
	public String getRefID() { return refID; }
	public String getName() { return name; }
	public String getAddress() { return addr; }
	public String getEmail() { return email; }
	public String[] getLanguages() { return this.languages; }

	
	
	public boolean equals(String clinicName, String clinicAddress, String clinicEmail){
		boolean match1 = clinicName.equals(name);
		boolean match2 = clinicAddress.equals(addr);
		boolean match3 = clinicEmail.equals(email);
		
		return match1 && match2 && match3;
	}
}
