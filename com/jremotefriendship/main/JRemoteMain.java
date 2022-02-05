package com.jremotefriendship.main;

import com.jremotefriendship.server.RMIServerWrapper;

public class JRemoteMain {
	
	public static void main(String[] args) throws Exception {
		RMIServerWrapper.start();
		System.out.println("started");
	}

}
