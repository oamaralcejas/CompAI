package com.snt.mainServlet;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger( MainServlet.class.getName() );
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//HttpSession session = request.getSession();
		//LOGGER.log( Level.INFO, "session id " +session.getId());
		response.setHeader("Cache-Control", "no-store");
		LOGGER.log(Level.INFO, "cache control: ", response.getHeader("Cache-Control"));
		Cookie cookies[]=request.getCookies(); 
		LOGGER.log( Level.INFO, "Cookies: ");
		//c.length gives the cookie count 
		for(int i=0;i<cookies.length;i++){  
		 LOGGER.log(Level.INFO,"Name: "+cookies[i].getName()+" & Value: "+cookies[i].getValue());
		}
		
		
		String username=request.getParameter("uname");  
	    String password=request.getParameter("psw"); 
	    if(username.equalsIgnoreCase("admin") && password.equals("AI$lawyers22")) {
	    	
	    	getServletContext().getRequestDispatcher("/uploadinput.html").forward(request, response);
	    }else {


	    	request.setAttribute("error", "The username or password is incorrect.");
	    	request.setAttribute("goBackPage", "index.jsp");
	    	getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);
	    }
	    
	    
	    
	    
	    
	}

}
