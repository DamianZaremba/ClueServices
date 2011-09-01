package org.cluenet.clueservices.modules.services.WebModuleHandlers;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeHandler extends HttpServlet {
	@Override
	public void doGet( HttpServletRequest request, HttpServletResponse response ) {
		try{
			response.setContentType("text/html;charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println("<h1>Hello world!</h1>");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
