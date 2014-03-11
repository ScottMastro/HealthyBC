package ca.ubc.cs310.gwt.healthybc.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SuppressWarnings("serial")
public class UploadedCSVParser extends HttpServlet
{   

	private BufferedReader br = null;
	private String line = "";
	private String cvsSplitBy = ";";
	private ClinicManagerImpl clinicManager = new ClinicManagerImpl();
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		try{
			ServletInputStream sis = request.getInputStream();
			br = new BufferedReader(new InputStreamReader(sis));

			while ((line = br.readLine()) != null) {

				// use semicolon as separator
				String[] cells = line.split(cvsSplitBy);

				String name = cells[0];
				String refID = cells[1];
				String phone = cells[2];
				String website = cells[3];
				String email = cells[4];
				String wc_acess = cells[5];
				String languages = cells[6];
				String street_no = cells[7];
				String street_name = cells[8];
				String street_type = cells[9];
				String city = cells[10];
				String pcode = cells[11];
				String latitude = cells[12];
				String longitude = cells[13]; 
				String desc = cells[14];
				String hours = cells[15];

				String address = street_no + " " + street_name + " " + street_type + " " + city;

				clinicManager.addNewClinic(refID, name, Double.valueOf(latitude), Double.valueOf(longitude),
						hours, address, pcode, email, phone, languages);

			}
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,
					"Request contents type is not supported by the servlet.");
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_CREATED);
	}
}