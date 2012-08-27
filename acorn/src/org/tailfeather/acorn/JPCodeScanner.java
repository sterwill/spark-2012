package org.tailfeather.acorn;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.data.QRCodeImage;

import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class JPCodeScanner {
	private static final Logger LOGGER = Logger.getLogger(JPCodeScanner.class.getName());

	private final OpenCVFrameGrabber grabber;

	public JPCodeScanner(int camera) {
		try {
			this.grabber = new OpenCVFrameGrabber(camera);
			this.grabber.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] scanCode() {
		try {
			QRCodeDecoder decoder = new QRCodeDecoder();
			IplImage image = grabber.grab();

			try {
				return decoder.decode(new J2SEImage(image.getBufferedImage()));
			} catch (Throwable e) {
				LOGGER.log(Level.WARNING, "error decoding frame", e);
				return null;
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Error grabbing frame", e);
			return null;
		}
	}

	private static class J2SEImage implements QRCodeImage {
		private final BufferedImage image;

		public J2SEImage(BufferedImage image) {
			this.image = image;
		}

		@Override
		public int getHeight() {
			return image.getHeight();
		}

		@Override
		public int getPixel(int x, int y) {
			return image.getRGB(x, y);
		}

		@Override
		public int getWidth() {
			return image.getWidth();
		}
	}
}
