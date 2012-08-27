package org.tailfeather.acorn;

import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.sourceforge.qrcode.data.QRCodeImage;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class ZXingCodeScanner {
	private static final Logger LOGGER = Logger.getLogger(ZXingCodeScanner.class.getName());

	private final OpenCVFrameGrabber grabber;

	public ZXingCodeScanner(int camera) {
		try {
			this.grabber = new OpenCVFrameGrabber(camera);
			this.grabber.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String scanCode() {
		try {
			IplImage image = grabber.grab();
			LuminanceSource source = new BufferedImageLuminanceSource(image.getBufferedImage());
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

			try {
				Result result = new MultiFormatReader().decode(bitmap);
				return result.getText();
			} catch (NotFoundException e) {
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Error grabbing frame", e);
		}
		return null;
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
