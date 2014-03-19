package ca.ubc.cs310.gwt.healthybc.client;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ClinicTabInfo implements Serializable {

	private Clinic clinic;

	/**
	 * Don't delete this; GWT needs this constructor to be here.
	 */
	protected ClinicTabInfo() {}

	public ClinicTabInfo(Clinic clinic){
		this.clinic = clinic;
	}

	public Clinic getClinic() { return clinic; }


	public boolean equals(Clinic c){
		return c.getRefID().equals(clinic.getRefID());
	}
}



