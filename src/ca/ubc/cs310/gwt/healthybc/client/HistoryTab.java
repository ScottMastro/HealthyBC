package ca.ubc.cs310.gwt.healthybc.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.ListDataProvider;

public class HistoryTab {
	
	private static HistoryTab singleton = null;
	private String currentUser;
	private ArrayList<Clinic> clinics;
	private ListDataProvider<Clinic> dataProvider; 
	
	public static HistoryTab getInstance() {
		if (singleton == null) {
			singleton = new HistoryTab();
		}
		return singleton;
	}
	
	public HistoryTab() {
	}
	
	public CellTable<Clinic> buildTable(ArrayList<String> result) {
		CellTable<Clinic> table = new CellTable<Clinic>(100);
		ArrayList<Clinic> clinics = parseClinics(result);
		
		if (currentUser != "") {
			TextColumn<Clinic> nameColumn = new TextColumn<Clinic>() {
				@Override
				public String getValue(Clinic c) {
					return c.getName();
				}
			};
			TextColumn<Clinic> addressColumn = new TextColumn<Clinic>() {
				@Override
				public String getValue(Clinic c) {
					return c.getAddress();
				}
			};
			TextColumn<Clinic> dateColumn = new TextColumn<Clinic>() {
				@Override
				public String getValue(Clinic c) {
					return c.getDate();
				}
			};
			
			table.addColumn(nameColumn, "Clinic Name");
			table.addColumn(addressColumn, "Clinic Address");
			table.addColumn(dateColumn, "Access Date");
		}
		
		dataProvider = new ListDataProvider<Clinic>();
		dataProvider.addDataDisplay(table);
		
		List<Clinic> clinicList = dataProvider.getList();
		for (Clinic c: clinics) {
			clinicList.add(c);
		}
		
		table.setWidth("100%", false);
		
		consoleLog("supposedly built the history table.......");
		
		return table;
	}
	
	native void consoleLog( String message) /*-{
    	console.log( "me:" + message );
	}-*/;
	
	private class Clinic {
		private String clinicName;
		private String clinicAddress;
		private String date;
		
		public Clinic(String clinicName, String clinicAddress, String accessDate) {
			this.clinicName = clinicName;
			this.clinicAddress = clinicAddress;
			this.date = accessDate;
		}

		public String getDate() {
			return date;
		}

		public String getAddress() {
			return clinicAddress;
		}

		public String getName() {
			return clinicName;
		}
	}
	
	private ArrayList<Clinic> parseClinics(ArrayList<String> result) {
		ArrayList<Clinic> clinics = new ArrayList<Clinic>();
		
		for(int i = 0; i<= result.size()-1; i+=3){
			String name = result.get(i);
			String addr = result.get(i+1);
			String dateStr = result.get(i+2);
			
			Clinic c = new Clinic(name, addr, dateStr);
			clinics.add(c);
		}
		
		return clinics;
	}

	public void addClinicToTable(String clinicName, String clinicAddress, String dateString) {
		Clinic c = new Clinic(clinicName, clinicAddress, dateString);
		dataProvider.getList().add(0,c);
		dataProvider.refresh();
	}
}
