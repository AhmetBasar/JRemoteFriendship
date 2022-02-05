package com.jremotefriendship.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.jremotefriendship.util.IJRemote;
import com.jremotefriendship.util.Robot;

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
		return ScreenShotGenerator.getInstance().getScreenShot();
	}

	@Override
	public void mouseClick(int buttonMask) throws RemoteException {
		Robot.doMouseClick(buttonMask);
	}

}
