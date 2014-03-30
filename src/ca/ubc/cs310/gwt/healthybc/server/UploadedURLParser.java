package ca.ubc.cs310.gwt.healthybc.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.ubc.cs310.gwt.healthybc.client.Clinic;
import ca.ubc.cs310.gwt.healthybc.client.ClinicHours;


@SuppressWarnings("serial")
public class UploadedURLParser extends HttpServlet
{   
	Logger logger = Logger.getLogger("uploadServletLogger");

	
	/**
	 * Pushes the request to POST
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doPost(request, response);
	}

	/**
	 * Receives form and CSV file as a request
	 * @return response: HTML listing parse result
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String resp = "";
		String urlstring = request.getParameter("urlstring");
		
		try {
			URL url = new URL(urlstring);
			InputStream is = url.openStream();
			
			RemoteDataManager rdm = new RemoteDataManager();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			String line = "";
			String cvsSplitBy = "\\t";
			
			while ((line = br.readLine()) != null) {
				
				String[] cells = line.split(cvsSplitBy);
				
				// check length and ignore file header
				if (cells.length != 25 || cells[0].equals("SV_TAXONOMY")){	
					continue;
				}
				
				String name = cells[3];
				String refID = cells[7];
				String phone = cells[9];
				String email = cells[11];
				String languages = cells[13];	/* no necessarily comma separated */
				String hours = cells[14];
				String street_no = cells[15];
				String street_name = cells[16];
				String street_type = cells[17];
				String city = cells[19];
				String pcode = cells[21];
				Double lat = Double.parseDouble(cells[22]);
				Double lon = Double.parseDouble(cells[23]); 
				
				String address = street_no + " " + street_name + " " + street_type + " " + city;
				address = address.replaceAll("\\s+", " ");	/* remove additional spaces */
				
				ClinicHours newhrs = new ClinicHours(hours);
				
				Clinic newClinic = new Clinic(refID, name, newhrs, lat, lon, address, pcode, email, phone, languages);
				rdm.addAndUploadClinicEntity(newClinic);
				
				resp += "Added new Clinic: " + newClinic.getName() + "<br/>";	
			}
		
			br.close();
			
		} catch (Exception e){
			
			resp = "Error: "+ e.getMessage();
			logger.warning("Error: "+e.getMessage());
			
		} finally {
		
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
		
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Server Response</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("</body>");
		
			out.println(resp);
		
			out.println("</html>");
			out.close();
		}

	} 

}