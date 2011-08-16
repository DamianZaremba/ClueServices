package org.cluenet.clueservices.misc;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class Config {
	private Map< String, String > server = new HashMap< String, String >();
	private ArrayList< String > modules = new ArrayList< String >();
	public static String cfg_file = "configuration.xml";

	private Config() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse( new File( this.cfg_file ) );
			doc.normalize();

			Node root = doc.getFirstChild();
			NodeList nodes = root.getChildNodes();
			for ( int i = 0; i < nodes.getLength(); i++ ) {
				Node node = nodes.item(i);
				if (node.getNodeName() == "modules") {
					NodeList subNodes = node.getChildNodes();
	
					for( int j=0; j < subNodes.getLength(); j++) {
						Node subNode = subNodes.item(j);
						if (subNode.getNodeType() == Node.ELEMENT_NODE) {
							String module = subNode.getTextContent();
							modules.add( module.toString() );
						}
					}
				} else if (node.getNodeName() == "server") {
					Map< String, String > config = new HashMap< String, String >();
					NodeList subNodes = node.getChildNodes();

					for ( int j=0; j < subNodes.getLength(); j++) {
						Node subNode = subNodes.item(j);
						if (subNode.getNodeType() == Node.ELEMENT_NODE) {
							String name = subNode.getNodeName();
							String value = subNode.getTextContent();
							server.put( name, value.toString() );
						}
					}
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXParseException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String get(String key) {
		Config cfg = new Config();
		return cfg.server.get( key );
	}

	public static ArrayList< String > getModules() {
		Config cfg = new Config();
		return cfg.modules;
	}
}
