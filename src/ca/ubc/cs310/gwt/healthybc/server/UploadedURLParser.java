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
			
			while ((line = br.readLine()) != null) {
				
				Clinic newClinic = DatasetLineParser.parseInputLine(line);
				
				if (newClinic != null){
					rdm.addAndUploadClinicEntity(newClinic);
					resp += "Added new Clinic: " + newClinic.getName() + "<br/>";
				}
					
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
			out.println("<center><b>Success :</b><a href='http://healthy-bc-310.appspot.com/'> Return to application </a></center><br/>");
			out.println(resp);
			out.println("</body>");
			out.println("</html>");
			out.close();
		}

	} 
	
}