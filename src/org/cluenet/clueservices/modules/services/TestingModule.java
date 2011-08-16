package org.cluenet.clueservices.modules.services;

import java.io.ByteArrayOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.cluenet.clueservices.core.Core;
import org.cluenet.clueservices.core.Event;
import org.cluenet.clueservices.core.Module;

import org.cluenet.clueservices.ircEvents.PrivmsgEvent;
import org.cluenet.clueservices.ircEvents.ProtocolRequestEvent;
import org.cluenet.clueservices.ircEvents.ServerSyncEvent;
import org.cluenet.clueservices.ircEvents.UserJoinEvent;
import org.cluenet.clueservices.ircEvents.UserSignonEvent;

import org.cluenet.clueservices.ircObjects.ChannelFactory;
import org.cluenet.clueservices.ircObjects.UserFactory;
import org.cluenet.clueservices.ircObjects.ChannelFactory.Channel;
import org.cluenet.clueservices.ircObjects.UserFactory.User;

import org.cluenet.clueservices.misc.Config;
import org.cluenet.clueservices.misc.PasteBin;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class TestingModule extends Module {
	
	@Override
	protected void event( Event take ) {
		if( take instanceof ServerSyncEvent ) {
			if( ( (ServerSyncEvent) take ).getParameters().toString().equals( Config.get("ip") ) ) {
				Core.fireEvent( new ProtocolRequestEvent( new UserSignonEvent( UserFactory.fake( "FooBar", "FooBar", "Foo.Bar", "Foo Bar", "+", "0.0.0.0", null ) ) ) );
			}
		} else if( take instanceof UserSignonEvent ) {
			User u = ( (UserSignonEvent) take ).getParameters();
			Channel c = ChannelFactory.find( "#clueirc" );
			if( u.getNick().equals( "FooBar" ) ) {
				Core.fireEvent( new ProtocolRequestEvent( new UserJoinEvent( u, c ) ) );
				Core.fireEvent( new ProtocolRequestEvent( new PrivmsgEvent( u, c, "Hello, world!" ) ) );
			}
		} else if( take instanceof PrivmsgEvent ) {
			PrivmsgEvent evt = (PrivmsgEvent) take;
			if( evt.getSource() instanceof User && evt.getTarget() instanceof Channel ) {
				User u = (User) evt.getSource();
				Channel c = (Channel) evt.getTarget();
				String data = evt.getParameters();
				if( data.equals( "!testing" ) )
					Core.fireEvent( new ProtocolRequestEvent( new PrivmsgEvent( UserFactory.find( "FooBar" ), c, "Testing." ) ) );
				else if( data.equals( "!force" ) )
					Core.fireEvent( new ProtocolRequestEvent( new UserJoinEvent( u, ChannelFactory.find( "#cluebotng" ) ) ) );
				else if( data.equals( "!xml" ) ) {
					try {
						DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
						DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
						Document doc = docBuilder.newDocument();

						Element root = doc.createElement( "XML" );
						doc.appendChild( root );
						root.appendChild( take.toXML( doc ) );

						TransformerFactory transformerFactory = TransformerFactory.newInstance();
						Transformer transformer = transformerFactory.newTransformer();
						DOMSource source = new DOMSource( doc );
						ByteArrayOutputStream os = new ByteArrayOutputStream();
						StreamResult result =  new StreamResult( os );
						transformer.transform( source, result );

						String pasteURL = PasteBin.upload( os.toString() );
						Core.fireEvent( new ProtocolRequestEvent( new PrivmsgEvent( UserFactory.find( "FooBar" ), c, "XML: " + pasteURL ) ) );
					} catch( Exception e ) {
						Core.fireEvent( new ProtocolRequestEvent( new PrivmsgEvent( UserFactory.find( "FooBar" ), c, "Error: " + e.getMessage() ) ) );
						throw new RuntimeException( e );
					}
				}
			}
		}
	}
	
	@Override
	protected Boolean init() {
		return true;
	}
	
}
