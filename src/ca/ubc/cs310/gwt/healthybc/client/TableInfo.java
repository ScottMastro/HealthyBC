package ca.ubc.cs310.gwt.healthybc.client;

import java.io.Serializable;

@SuppressWarnings("serial")
public class TableInfo implements Serializable {

	private String name;
	private String addr;
	private String email;
	
	/**
	 * Don't delete this; GWT needs this constructor to be here.
	 */
	protected TableInfo() {
	}
	
	public TableInfo(String name, String address, String email) {
		this.name = name;
		this.addr = address;
		this.email = email;
	}

	public String getName() { return name; }
	public String getAddress() { return addr; }
	public String getEmail() { return email; }
	
	public TableInfo setName(String n) {
		name = n;
		return this;
	}
	
	public TableInfo setAddress(String a) {
		addr = a;
		return this;
	}
	
	public TableInfo setEmail(String e) {
		email = e;
		return this;
	}
	
	public boolean equals(String clinicName, String clinicAddress, String clinicEmail){
		boolean match1 = clinicName.equals(name);
		boolean match2 = clinicAddress.equals(addr);
		boolean match3 = clinicEmail.equals(email);
		
		return match1 && match2 && match3;
	}
}
