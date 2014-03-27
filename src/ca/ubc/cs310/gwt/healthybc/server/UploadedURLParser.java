package ca.ubc.cs310.gwt.healthybc.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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