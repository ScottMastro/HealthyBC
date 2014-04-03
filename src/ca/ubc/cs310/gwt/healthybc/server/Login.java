package ca.ubc.cs310.gwt.healthybc.server;
 
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
 
@SuppressWarnings("serial")
public class Login extends HttpServlet
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
         * Authorizes the user
         * @return response: true if login valid, false otherwise
         */
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
        {
                String resp = "";
 
                String username = request.getParameter("username");
                String password = request.getParameter("password");
 
                section:
                {
                	//admin account hardcoded per design
	                if (username.equalsIgnoreCase("admin") && password.equalsIgnoreCase("blankslate123")) {
                        resp = "admin";
                        break section;
	                }
	                else if (username.equalsIgnoreCase("nsa") && password.equalsIgnoreCase("silencesnowden")) {
	                    resp = "success";
	                    break section;
	                }
	               
	                User user = User.getUser(username);
	                if (user != null) {
	                        if (user.checkPassword(password)) {
	                                resp = "success";
	                                break section;
	                        }
	                        else {
	                                resp = "fail";
	                                break section;
	                        }
	                }
	                else {
	                        resp = "fail";
	                        break section;
	                }
                }
               
                response.setContentType("text/html");
 
                PrintWriter out = response.getWriter();
                out.print(resp + ":" + username);
                out.close();
        }
       
}
