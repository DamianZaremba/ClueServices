package org.cluenet.clueservices.misc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class PasteBin {
	private static String PasteBin(String pasteContent) {
		try {
			String postData = URLEncoder.encode( "paste_format", "UTF-8" ) + "=" + URLEncoder.encode( "XML", "UTF-8" );
			postData += "&" + URLEncoder.encode( "paste_subdomain", "UTF-8" ) + "=" + URLEncoder.encode( "cluenetpastes", "UTF-8" );
			postData += "&" + URLEncoder.encode( "paste_name", "UTF-8" ) + "=" + URLEncoder.encode( "ClueServices", "UTF-8" );
			postData += "&" + URLEncoder.encode( "paste_code", "UTF-8" ) + "=" + URLEncoder.encode( pasteContent, "UTF-8" );

			// Send data
			URL url = new URL( "http://pastebin.com/api_public.php" );
			URLConnection conn = url.openConnection();
			conn.setDoOutput( true );
			OutputStreamWriter wr = new OutputStreamWriter( conn.getOutputStream() );
			wr.write( postData );
			wr.flush();

			// Get the response
			BufferedReader rd = new BufferedReader( new InputStreamReader( conn.getInputStream() ) );
			String apiURL = "Unknown";
			String line;
			while( ( line = rd.readLine() ) != null )
				apiURL = line;
			wr.close();
			rd.close();

			return apiURL;
		} catch( Exception e ) {
			return e.getMessage();
		}
	}

	public static String upload(String pasteContent) {
		String pasteURL = PasteBin(pasteContent);
		return pasteURL;
	}
}
