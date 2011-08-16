/**
 * 
 */
package org.cluenet.clueservices.core;

import org.cluenet.clueservices.misc.Config;


/**
 * @author cobi
 *
 */
public class ClueServices {
	
	/**
	 * @param args
	 */
	public static void main( String[] args ) {
		if( args.length == 1 ) {
			Config.cfg_file = args[0];
		}

		ThreadGroup coreGroup = new ThreadGroup( "CoreGroup" );
		Thread core = new Thread( coreGroup, new Core(), "Core" );
		core.start();
	}
	
}
