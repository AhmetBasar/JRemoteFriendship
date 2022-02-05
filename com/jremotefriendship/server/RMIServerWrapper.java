package com.jremotefriendship.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServerWrapper {

	private static volatile RMIServerWrapper instance;
	private static RMIServer server;
	private static Registry registry;
	
	public static RMIServerWrapper start() throws Exception {
		if (instance == null) {
			synchronized (RMIServerWrapper.class) {
				if (instance == null) {
					instance = new RMIServerWrapper();
					
					server = new RMIServer();
					registry = LocateRegistry.createRegistry(65500);
					registry.rebind("JRemoteFriendship", server);
				}		
			}
		}
		return instance;
	}
	
}
