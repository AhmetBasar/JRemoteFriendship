package com.jremotefriendship.server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.jremotefriendship.util.Robot;
import com.jremotefriendship.util.ThreadPool;
import com.jremotefriendship.util.Utility;

public class ScreenShotGenerator {
	
	private Queue<byte[]> queue = new LinkedList<byte[]>();
	
	private final Object mutex = new Object();
	
	private static volatile ScreenShotGenerator instance;
	
	public static ScreenShotGenerator getInstance() {
		if (instance == null) {
			synchronized (ScreenShotGenerator.class) {
				if (instance == null) {
					instance = new ScreenShotGenerator();
				}
			}
		}
		return instance;
	}
	
	private ScreenShotGenerator() {
		List<Runnable> runnables = new ArrayList<Runnable>();
		for (int i = 0; i < ThreadPool.POOL_SIZE; i++) {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					for (;;) {
						synchronized (mutex) {
							try {
								mutex.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						queue.add(getScreenShot0());
					}
				}
			};
			runnables.add(runnable);
		}
		ThreadPool.getInstance().execute(runnables, false);
	}
	
	public byte[] getScreenShot() {
//		System.out.println("queue.size() = " + queue.size());
		byte[] ss = queue.poll();
		synchronized (mutex) {
			mutex.notify();
		}
		return ss;
	}
	
	public byte[] getScreenShot0() {
		ByteArrayOutputStream baos = null;
		BufferedImage img = null;
//		long t1 = System.currentTimeMillis();
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
//			long t2 = System.currentTimeMillis();
//			System.out.println("fark = " + (t2 - t1));
		}
	}

}
