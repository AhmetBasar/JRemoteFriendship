package com.jremotefriendship.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.security.SecureRandom;

import javax.imageio.ImageIO;

public class Utility {

	public static final String COMPRESSION_ALGORITHM_BMP = "bmp";

	public static byte[] convertImageToByteArray(BufferedImage image, String algorithm) throws Exception {
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			ImageIO.write(image, algorithm, baos);
			baos.flush();
			byte[] bytes = baos.toByteArray();
			return bytes;
		} finally {
			if (baos != null) {
				baos.close();
			}
		}
	}

	public static void closeQuietly(Closeable... closeables) {
		for (Closeable closeable : closeables) {
			try {
				if (closeable != null) {
					closeable.close();
				}
			} catch (Exception e) {
			}
		}
	}

	public static String prepareGetterMethodName(String fieldName) {
		return "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
	}
	
	public static void sleep(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static int generateStrongRandomNumber(int begin, int end) {
		SecureRandom r = new SecureRandom();
		return r.nextInt(end - begin) + begin;
	}
	
	public static boolean doProbability(int percentage) {
		return generateStrongRandomNumber(0, 100) < percentage;
	}
	
	public static boolean isDebug() {
		return false;
	}
	
	public static String padRightSpaces(String inputString, int length) {
	    if (inputString.length() >= length) {
	        return inputString;
	    }
	    StringBuilder sb = new StringBuilder();
	    sb.append(inputString);
	    while (sb.length() < length - inputString.length()) {
	        sb.append(' ');
	    }
	 
	    return sb.toString();
	}
	
}
