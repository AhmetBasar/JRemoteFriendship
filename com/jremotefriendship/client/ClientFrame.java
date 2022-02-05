package com.jremotefriendship.client;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.rmi.RemoteException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ClientFrame {
	
	public static final int width = 1700;
	public static final int heigth = 1000;
	public static final int[] remoteScreenSize = new int[2];
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new ClientFrame().init();
			}
		});
	}
	
	public void init() {
		JFrame mainFrame = new JFrame();
		mainFrame.setLayout(null);
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mainFrame.setLocation(30, 30);
		mainFrame.setSize(width, heigth);
		mainFrame.setResizable(false);
		
		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getID() == KeyEvent.KEY_PRESSED) {
					try {
						RMIClient.keyPress(e.getKeyCode());
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				} else if (e.getID() == KeyEvent.KEY_RELEASED) {
					try {
						RMIClient.keyRelease(e.getKeyCode());
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
				} else if (e.getID() == KeyEvent.KEY_TYPED) {
				}
				return false;
			}
		});
		
		final JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setSize(width - 50, heigth - 50);
		
		JLabel lblIpAddress = new JLabel("Ip Address");
		lblIpAddress.setSize(150, 25);
		lblIpAddress.setLocation(10, 10);
		panel.add(lblIpAddress);
		
		final JTextField txtIpAddress = new JTextField("localhost");
		txtIpAddress.setLocation(100, 10);
		txtIpAddress.setSize(100, 25);
		panel.add(txtIpAddress);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.setLocation(220, 10);
		btnConnect.setSize(150, 25);
		panel.add(btnConnect);
		btnConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String host = txtIpAddress.getText();
				try {
					if (!RMIClient.isConnected()) {
						RMIClient.connect(host);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					System.exit(-1);
				}
				
				final JLabel lbl = new JLabel();
				lbl.setLocation(10, 50);
				lbl.setSize(width - 50, heigth - 50);
				
				lbl.addMouseListener(new MouseListener() {
					@Override
					public void mouseReleased(MouseEvent e) {
					}
					@Override
					public void mousePressed(MouseEvent e) {
					}
					@Override
					public void mouseExited(MouseEvent e) {
					}
					@Override
					public void mouseEntered(MouseEvent e) {
					}
					@Override
					public void mouseClicked(MouseEvent e) {
						int remoteXpos = (remoteScreenSize[0] * e.getX()) / lbl.getWidth();
						int remoteYpos = (remoteScreenSize[1] * e.getY()) / lbl.getHeight();
						try {
							RMIClient.mouseMove(remoteXpos, remoteYpos);
							int buttonMask = SwingUtilities.isLeftMouseButton(e) ? InputEvent.BUTTON1_DOWN_MASK : InputEvent.BUTTON3_DOWN_MASK;
							RMIClient.mouseClick(buttonMask);
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}
					}
				});
				
				panel.add(lbl);
				
				Thread th = new Thread(new Runnable() {
					
					@Override
					public void run() {
						while (true) {
							try {
								BufferedImage bi = RMIClient.getScreenShot();
								int originalWidth = bi.getWidth();
								int originalHeight = bi.getHeight();
								int labelWidth = lbl.getWidth();
								remoteScreenSize[0] = originalWidth;
								remoteScreenSize[1] = originalHeight;
								java.awt.Image img = new ImageIcon(bi).getImage().getScaledInstance(labelWidth, labelWidth * originalHeight / originalWidth, java.awt.Image.SCALE_SMOOTH);
								lbl.setSize(lbl.getWidth(), labelWidth * originalHeight / originalWidth);
								lbl.setIcon(new ImageIcon(img));
							} catch (Exception e2) {
								e2.printStackTrace();
							}
						}
					}
				});
				th.start();
			}
		});
		
		mainFrame.add(panel);
		
		mainFrame.setVisible(true);
	}

}
