package org.cluenet.clueservices.modules.services;

import org.cluenet.clueservices.core.Module;
import org.cluenet.clueservices.core.Event;
import org.cluenet.clueservices.misc.Config;

import java.lang.Exception;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import org.cluenet.clueservices.modules.services.WebModuleHandlers.HomeHandler;
import org.cluenet.clueservices.modules.services.WebModuleHandlers.ApiHandler;

public class WebModule extends Module {

	@Override
	protected void event( Event take ) {

	}

	@Override
	protected Boolean init() {
		try {
			Server server = new Server( Integer.parseInt( Config.get("web_port") ) );
			ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

			context.setContextPath("/");
			server.setHandler(context);

			context.addServlet(new ServletHolder(new HomeHandler()), "/");
			context.addServlet(new ServletHolder(new ApiHandler()), "/api");

			server.start();
			server.join();
		} catch( Exception e ) {
			e.printStackTrace();
		}
		return true;
	}

}

