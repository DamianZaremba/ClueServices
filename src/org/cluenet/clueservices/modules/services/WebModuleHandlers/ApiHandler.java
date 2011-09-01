package org.cluenet.clueservices.modules.services.WebModuleHandlers;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.util.Map;
import java.io.ByteArrayOutputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.cluenet.clueservices.ircObjects.ChannelFactory;
import org.cluenet.clueservices.ircObjects.ChannelFactory.Channel;
import org.cluenet.clueservices.ircObjects.UserFactory;
import org.cluenet.clueservices.ircObjects.UserFactory.User;

public class ApiHandler extends HttpServlet {
	@Override
	public void doGet( HttpServletRequest request, HttpServletResponse response ) {
		response.setContentType( "text/xml;charset=utf-8" );

		try{
			response.setStatus( HttpServletResponse.SC_OK );

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			Element root = doc.createElement( "API" );
			doc.appendChild( root );

			String action = request.getParameter("action");
			if( action == null ) {
				action = "home";
			}

			if( action.equals( "users.list" ) ) {
				root.appendChild( doUsersList( doc, request ) );
			} else if( action.equals( "channels.list" ) ) {
				root.appendChild( doChannelsList( doc, request ) );
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			DOMSource source = new DOMSource( doc );
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			StreamResult responseult =  new StreamResult( response.getOutputStream() );
			transformer.transform(source, responseult);
		} catch(Exception e) {
			response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
			try {
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.newDocument();

				Element root = doc.createElement( "API" );
				doc.appendChild( root );

				Element error = doc.createElement( "Error" );
				Element message = doc.createElement( "Message" );
				message.appendChild( doc.createTextNode( e.getMessage() ) );
				error.appendChild( message );
				Element stackTrace = doc.createElement( "StackTrace" );
				error.appendChild( stackTrace );

				for( StackTraceElement ste : e.getStackTrace() ) {
					Element steDom = doc.createElement( "StackTraceElement" );
					steDom.appendChild( doc.createTextNode( ste.toString() ) );
					stackTrace.appendChild( steDom );
				}
				root.appendChild( error );

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource( doc );
				StreamResult responseult =  new StreamResult( response.getOutputStream() );
				transformer.transform(source, responseult);
			} catch( Exception ex ) {
				ex.printStackTrace();
				e.printStackTrace();
			}
		}
	}

	private Element doUsersList( Document doc, HttpServletRequest req ) {
		Element element = doc.createElement( "Users" );
		for( User user : UserFactory.getUsers().values() ) {
			element.appendChild( user.toXML( doc ) );
		}
		return element;
	}

	private Element doChannelsList( Document doc, HttpServletRequest req ) {
		Element element = doc.createElement( "Channels" );
		for( Channel channel : ChannelFactory.getChannels().values() ) {
			element.appendChild( channel.toXML( doc ) );
		}
		return element;
	}
}
