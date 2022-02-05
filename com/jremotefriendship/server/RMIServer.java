package com.jremotefriendship.server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.jremotefriendship.util.IJRemote;
import com.jremotefriendship.util.Robot;
import com.jremotefriendship.util.Utility;

class RMIServer extends UnicastRemoteObject implements IJRemote {

	private static final long serialVersionUID = 1L;

	protected RMIServer() throws RemoteException {
		super(65500);
	}

	@Override
	public void mouseMove(int x, int y) throws RemoteException {
		com.jremotefriendship.util.Robot.moveMouseCursor(x, y);
	}
	
	public byte[] getScreenShot() throws RemoteException {
		ByteArrayOutputStream baos = null;
		BufferedImage img = null;
		try {
			baos = new ByteArrayOutputStream();
			img = Robot.takeScreenShot();
			javax.imageio.ImageIO.write(img, "jpg", baos);
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			Utility.closeQuietly(baos);
		}
	}

	@Override
	public void mouseClick(int buttonMask) throws RemoteException {
		Robot.doMouseClick(buttonMask);
	}

}
