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

		// process only multipart requests
		if (ServletFileUpload.isMultipartContent(request)) {

			// Create a factory for disk-based file items
			FileItemFactory factory = new DiskFileItemFactory();

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);

			// Parse the request
			try {
				
				logger.log(Level.WARNING, "enter try");
				List<FileItem> items = upload.parseRequest(request);
				for (FileItem item : items) {
					logger.log(Level.WARNING, "enter iteration");

					// process only file upload - discard other form item types
					if (item.isFormField()) continue;

					String fileName = item.getName();
					// get only the file name not whole path
					if (fileName != null) {
						fileName = FilenameUtils. getName(fileName);
					}
				}
			} catch (Exception e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"An error occurred while parsing the file : " + e.getMessage());
			}

		} else {
			response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,
					"Request contents type is not supported by the servlet.");
		}

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		out.println("<html>");
		out.println("<head>");
		out.println("<title> uploadServlet</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("</body>");

		out.println("Servlet was evoked");

		out.println("</html>");
		out.close();

	}


	private void readFile(FileItem file) throws IOException{

		// Process form file field (input type="file").
		String filename = FilenameUtils.getName(file.getName());
		InputStream filecontent = file.getInputStream();
		RemoteDataManager rdm = new RemoteDataManager();

		BufferedReader br = new BufferedReader(new InputStreamReader(filecontent));

		String line = "";
		String cvsSplitBy = ";";

		while ((line = br.readLine()) != null) {			

			rdm.testPush(line);

			/*

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
			ClinicHours newhrs = new ClinicHours(hours);
			Location newLoc = new Location( Double.parseDouble(latitude), Double.parseDouble(longitude));

			Clinic newClinic = new Clinic(refID, name, newhrs, newLoc, address, pcode, email, phone, languages);
			 */
		}
	}
}