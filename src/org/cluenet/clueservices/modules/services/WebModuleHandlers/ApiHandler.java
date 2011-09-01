package org.cluenet.clueservices.modules.services.WebModuleHandlers;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiHandler extends HttpServlet {
	@Override
	public void doGet( HttpServletRequest request, HttpServletResponse response ) {
		try{
			response.setContentType("text/xml;charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println("<xml><message>Hello world!</message></xml>");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
