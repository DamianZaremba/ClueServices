package org.cluenet.clueservices.modules.services.WebModuleHandlers;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cluenet.clueservices.core.Core;
import org.cluenet.clueservices.core.Event;
import org.cluenet.clueservices.core.Module;

import org.cluenet.clueservices.ircEvents.PrivmsgEvent;
import org.cluenet.clueservices.ircEvents.ProtocolRequestEvent;
import org.cluenet.clueservices.ircEvents.ServerSyncEvent;
import org.cluenet.clueservices.ircEvents.UserJoinEvent;
import org.cluenet.clueservices.ircEvents.UserSignonEvent;

import org.cluenet.clueservices.ircObjects.ChannelFactory;
import org.cluenet.clueservices.ircObjects.ChannelFactory.Channel;
import org.cluenet.clueservices.ircObjects.UserFactory;
import org.cluenet.clueservices.ircObjects.UserFactory.User;
import org.cluenet.clueservices.ircObjects.ServerFactory;
import org.cluenet.clueservices.ircObjects.ServerFactory.Server;

public class HomeHandler extends HttpServlet {
	@Override
	public void doGet( HttpServletRequest request, HttpServletResponse response ) {
		response.setContentType( "text/html;charset=utf-8" );
		try{
			response.setStatus( HttpServletResponse.SC_OK );

			String action = request.getParameter("action");
			if( action != null ) {
				if( action.equals( "sayhi" ) ) {
					Channel c = ChannelFactory.find("#clueirc");
					User u = UserFactory.find("FooBar");

					if( c != null && u != null ) {
						Core.fireEvent( new ProtocolRequestEvent( new PrivmsgEvent( u, c, "Hi!" ) ) );
						response.getWriter().println("<p>Event fired off to the core</p>");
					} else {
						response.getWriter().println("<p>Yeah, whatever.</p>");
					}
				}
			} else {
				response.getWriter().println("<h1>Hello world!</h1>");
				response.getWriter().println("<p>Users: " + UserFactory.getUsers().values().size() + "</p>");
				response.getWriter().println("<p>Channels: " + ChannelFactory.getChannels().values().size() + "</p>");
				response.getWriter().println("<p>Servers: " + ServerFactory.getServers().values().size() + "</p>");
			}
		} catch( Exception e) {
			response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
			try {
				e.printStackTrace( response.getWriter()  );
			} catch( Exception ex ) {
				ex.printStackTrace();
				e.printStackTrace();
			}
		}
	}
}
