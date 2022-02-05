package com.jremotefriendship.util;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class Robot {
	
	private static java.awt.Robot robot;
	private static Rectangle screenRect;

	static {
		try {
			robot = new java.awt.Robot();
			screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	public static void getInstance() {
		
	}

	public synchronized static BufferedImage takeScreenShot() {
		BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
		return screenFullImage;
	}

	public synchronized static void moveMouseCursor(int x, int y) {
		Point from = MouseInfo.getPointerInfo().getLocation();
		mouseGlide(from.x, from.y, x, y, 1, Utility.generateStrongRandomNumber(400, 600));
	}
	
	public synchronized static void moveMouseCursoUltraFast(Point to) {
		Point from = MouseInfo.getPointerInfo().getLocation();
		mouseGlide(from.x, from.y, to.x, to.y, 1, 200);
	}
	
	public synchronized static void mouseGlide(int x1, int y1, int x2, int y2, int t, int n) {
		double dx = (x2 - x1) / ((double) n);
		double dy = (y2 - y1) / ((double) n);
		double dt = t / ((double) n);
		for (int step = 1; step <= n; step++) {
			Utility.sleep((int) dt);
			robot.mouseMove((int) (x1 + dx * step), (int) (y1 + dy * step));
		}
	}
	
	public synchronized static void mouseMove(int x, int y) {
		robot.mouseMove(x, y);
	}
	
	public synchronized static void mousePress(int buttons) {
		robot.mousePress(buttons);
	}
	
	public synchronized static void mouseRelease(int buttons) {
		robot.mouseRelease(buttons);
	}
	
	public static void doMouseClick(int mask) {
		robot.mousePress(mask);
		Utility.sleep(90);
		robot.mouseRelease(mask);
	}
	
}
