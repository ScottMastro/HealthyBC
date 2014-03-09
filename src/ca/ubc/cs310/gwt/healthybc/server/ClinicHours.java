package ca.ubc.cs310.gwt.healthybc.server;

public class ClinicHours {
	private String hours;
	
	/**
	 * Don't delete this; GWT needs this constructor to be here.
	 */
	protected ClinicHours() {
	}
	
	public ClinicHours(String hours) {
		this.hours = hours;
	}
	
	public String getHours() { return hours; }
	
	@Override
	public String toString() { return getHours(); }
}
