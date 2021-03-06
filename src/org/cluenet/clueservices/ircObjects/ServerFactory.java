package org.cluenet.clueservices.ircObjects;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class ServerFactory {
	private static ServerFactory factory = new ServerFactory();
	private static Map< String, Server > map = Collections.synchronizedMap( new HashMap< String, Server >() );

	public class Server extends IrcObject implements IrcSource {
		private String name;
		private Server parent;
		private String description;
		private Boolean isSynchronized;
		private Server( String name, Server parent, String description ) {
			this.name = name;
			this.parent = parent;
			this.description = description;
			this.isSynchronized = false;
		}

		public void setSynchronized() {
			isSynchronized = true;
		}

		public Boolean isSynchronized() {
			return isSynchronized;
		}

		public String toString() {
			return name;
		}

		public Server getParent() {
			return parent;
		}

		public String getDescription() {
			return description;
		}

		public Element toXML( Document doc ) {
			Element e = doc.createElement( "Server" );
			e.appendChild( doc.createElement( "Name" ) ).appendChild( doc.createTextNode( name ) );
			if( parent != null )
				e.appendChild( doc.createElement( "Parent" ) ).appendChild( parent.toXML( doc ) );
			e.appendChild( doc.createElement( "Description" ) ).appendChild( doc.createTextNode( description ) );
			e.appendChild( doc.createElement( "IsSynchronized" ) ).appendChild( doc.createTextNode( isSynchronized ? "True" : "False" ) );
			return e;
		}

		public String getName() {
			return name;
		}
	}

	private ServerFactory() {

	}

	public static Server find( String name ) {
		return map.get( name );
	}

	public static Map< String, Server> getServers() {
		return map;
	}

	public static Server create( String name, Server parent, String description ) {
		Server srv = factory.new Server( name, parent, description );
		map.put( name, srv );
		return srv;
	}

}
