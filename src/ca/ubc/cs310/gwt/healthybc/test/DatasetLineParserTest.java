package ca.ubc.cs310.gwt.healthybc.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import ca.ubc.cs310.gwt.healthybc.client.Clinic;
import ca.ubc.cs310.gwt.healthybc.server.DatasetLineParser;

public class DatasetLineParserTest {
	
	String remoteheader = null;
	String dataline1 = null;
	String dataline2 = null;
    	
	String remoteURLString = "http://pub.data.gov.bc.ca/datasets/174267/hlbc_walkinclinics.txt";
	
	@Before
	public void setup(){
		try{
			URL remoteurl = new URL(remoteURLString);
			InputStream is = remoteurl.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			remoteheader = br.readLine();
			dataline1 = br.readLine();
			dataline2 = br.readLine();
			br.close();
		} catch (Exception e){
			System.out.println("Could not open connection to remote URL");
		}
	}
	
	@Test
	public void testNull(){
		String line = null;
		assertTrue(DatasetLineParser.parseInputLine(line) == null);
	}
	
	@Test
	public void testHeader(){
		String header = "SV_TAXONOMY	TAXONOMY_NAME	RG_REFERENCE	RG_NAME	SV_REFERENCE	"
				+ "SV_NAME	SV_DESCRIPTION	SL_REFERENCE	LC_REFERENCE	PHONE_NUMBER	WEBSITE	EMAIL_ADDRESS	"
				+ "WHEELCHAIR_ACCESSIBLE	LANGUAGE	HOURS	STREET_NUMBER	STREET_NAME	STREET_TYPE	STREET_DIRECTION	"
				+ "CITY	PROVINCE	POSTAL_CODE	LATITUDE	LONGITUDE	811_LINK";
		assertTrue(DatasetLineParser.parseInputLine(header) == null);
		assertTrue(DatasetLineParser.parseInputLine(remoteheader) == null);
	}
	
	@Test
	public void testIncompleteData(){
		String incomplete = "LN-9300	Walk In Clinics	RG000012	Vancouver Coastal Health Authority	SV000588	UBC Student Health Services";
		assertTrue(DatasetLineParser.parseInputLine(incomplete) == null);	
	}
	
	@Test
	public void testLines(){

		Clinic myclinic1 = DatasetLineParser.parseInputLine(dataline1);
		Clinic myclinic2 = DatasetLineParser.parseInputLine(dataline2);
		
		assertFalse(myclinic1 == null);
		assertFalse(myclinic2 == null);
		
		assertTrue(myclinic1.getName().equals("Fraser Health Authority"));
		assertTrue(myclinic1.getRefID().equals("SL077114"));
		assertTrue(myclinic1.getLatitude() == 49.2385);
		assertTrue(myclinic1.getLongitude() == -121.7675);
		assertTrue(Long.parseLong(myclinic1.getPhone()) == 6047032030L);
		
		assertTrue(myclinic2.getName().equals("Vancouver Coastal Health Authority"));
		assertTrue(myclinic2.getRefID().equals("SL004037"));
		assertTrue(myclinic2.getLatitude() == 49.265082);
		assertTrue(myclinic2.getLongitude() == -123.244573);
		assertTrue(Long.parseLong(myclinic2.getPhone()) == 6048227011L);
		
	}

}
