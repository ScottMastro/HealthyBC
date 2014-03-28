package ca.ubc.cs310.gwt.healthybc.server;
 
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
 
@SuppressWarnings("serial")
public class Register extends HttpServlet
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
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                String name = request.getParameter("name");
               
                User user = User.createUser(username, email, name, password);
                if (user != null) {
                        resp = "success";
                }
                else {
                        resp = "fail";
                }
               
                response.setContentType("text/html");
 
                PrintWriter out = response.getWriter();
                out.println(resp + ":" + username);
                out.close();
        }
       
}