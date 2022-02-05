package com.jremotefriendship.util;

import java.rmi.RemoteException;

public interface IJRemote extends java.rmi.Remote {

	public void mouseMove(int x, int y) throws RemoteException;
	
	public void mouseClick(int buttonMask) throws RemoteException;
	
	public void keyPress(int keycode) throws RemoteException;
	
	public void keyRelease(int keycode) throws RemoteException;
	
	public byte[] getScreenShot() throws RemoteException;
	
}
