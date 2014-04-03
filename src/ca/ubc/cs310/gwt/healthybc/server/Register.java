package ca.ubc.cs310.gwt.healthybc.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;


import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@SuppressWarnings("serial")
public class Register extends HttpServlet
{  
	Logger logger = Logger.getLogger("uploadServletLogger");
	private static final int MAX_NAME_LENGTH = 20;


	/**
	 * Pushes the request to POST
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}

	/**
	 * Authorizes the user
	 * @return response: true if login valid, false otherwise
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String resp = "";

		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String name = request.getParameter("name");

		if(!isAcceptableName(username)){
			resp = "fail";
		}
		else{

			User user = User.createUser(username, email, name, password);
			if (user != null) {
				resp = "success";
			}
			else {
				resp = "fail";
			}
		}
		
		response.setContentType("text/html");

		PrintWriter out = response.getWriter();
		out.print(resp + ":" + username);
		out.close();

	}

	/**
	 * sanity-check an input string to check if it's an allowable name
	 *
	 * @param input input string
	 * @return true if name is acceptable according to our criteria
	 */
	private static boolean isAcceptableName(String input) {
		if (input == null) {
			return false;
		}

		//"admin" is reserved
		if (input.equalsIgnoreCase("admin")) {
			return false;
		}

		//check if string is too long
		if (input.length() > MAX_NAME_LENGTH) {
			return false;
		}

		//check if string contains white space using regex
		Pattern pattern = Pattern.compile("\\s");
		if (pattern.matcher(input).find()) {
			return false;
		}

		//check if string starts with a number
		if (Character.isDigit(input.charAt(0))) {
			return false;
		}

		//check if string contains non-ASCII characters
		char eachChar;
		for (int i = 0; i < input.length(); ++i) {
			eachChar = input.charAt(i);
			if (Character.UnicodeBlock.of(eachChar) != Character.UnicodeBlock.BASIC_LATIN) {
				return false;
			}
		}

		return true;
	}

}
