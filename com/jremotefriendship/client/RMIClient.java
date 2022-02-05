package com.jremotefriendship.client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.jremotefriendship.util.IJRemote;
import com.jremotefriendship.util.Utility;

public class RMIClient {

	private static Registry registry;
	private static IJRemote server;

	public static void connect(String host) throws Exception {
		registry = LocateRegistry.getRegistry(host, 65500);
		server = (IJRemote) registry.lookup("JRemoteFriendship");
	}

	public static boolean isConnected() {
		return server != null;
	}

	public static void mouseMove(int x, int y) throws RemoteException {
		server.mouseMove(x, y);
	}
	
	public static void mouseClick(int buttonMask) throws RemoteException {
		server.mouseClick(buttonMask);
	}

	public static BufferedImage getScreenShot() throws RemoteException {
		ByteArrayInputStream is = null;
		try {
			is = new ByteArrayInputStream(server.getScreenShot());
			return javax.imageio.ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			Utility.closeQuietly(is);
		}
	}

}
