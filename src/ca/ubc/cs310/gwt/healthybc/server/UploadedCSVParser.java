package ca.ubc.cs310.gwt.healthybc.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;


@SuppressWarnings("serial")

public class UploadedCSVParser extends HttpServlet
{   

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Logger logger = Logger.getLogger("uploadServletLogger");
		String resp = "";

		ServletFileUpload upload = new ServletFileUpload();

		try {
		    FileItemIterator iterator = upload.getItemIterator(request);
		    
		    while (iterator.hasNext()) {
		    	FileItemStream item = iterator.next();
		    	InputStream stream = item.openStream();
		    	if (item.isFormField()) {
		    		logger.warning("Got a form field: " + item.getFieldName());
		    		resp += "Error: Got a form field "+item.getFieldName()+" <br/>";
		    	} else {
		    		
		    		if(item.getName().endsWith(".csv")){
		    			// TODO: Parse file here
		    			resp += "File "+item.getName()+" successfully uploaded<br/>";
		    			//logger.warning("File: "+item.getName());
		    		} else {
		    			logger.warning("File: " + item.getName());
		    			resp += "Error: File "+item.getName()+" is not a CSV file<br/>";
		    		}
		    	}
	
		    }
		} catch (Exception e){
			logger.warning("Exception occurred");
			resp += "Error: Exception occurred<br/>";
		} finally {
		    
	    response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();

		out.println("<html>");
		out.println("<head>");
		out.println("<title> uploadServlet</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("</body>");

		out.println(resp);

		out.println("</html>");
		out.close();
		}
		
	} 
	
}