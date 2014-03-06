package ca.ubc.cs310.gwt.healthybc.client;

import java.io.Serializable;

public class TableInfo implements Serializable {

	/**
	 * for serialization 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String addr;
	private String email;
	
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
}
