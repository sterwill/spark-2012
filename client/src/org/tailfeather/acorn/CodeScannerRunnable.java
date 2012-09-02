package org.tailfeather.acorn;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.googlecode.javacv.FrameGrabber.Exception;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class CodeScannerRunnable implements Runnable {
	private static final Logger LOGGER = Logger.getLogger(CodeScannerRunnable.class.getName());

	private final QRCodeReader reader = new QRCodeReader();
	private final OpenCVFrameGrabber grabber;

	private volatile String code;

	public CodeScannerRunnable(int camera) {
		try {
			this.grabber = new OpenCVFrameGrabber(camera);
			this.grabber.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void run() {

		while (true) {
			IplImage image;
			try {
				image = grabber.grab();
			} catch (Exception e) {
				LOGGER.log(Level.WARNING, "Error grabbing frame", e);
				continue;
			}

			Result result;
			try {
				result = reader.decode(new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image
						.getBufferedImage()))));
			} catch (ChecksumException | NotFoundException e) {
				// Normal
				continue;
			} catch (FormatException e) {
				LOGGER.log(Level.INFO, "Error decoding frame", e);
				continue;
			}

			if (result.getText() != null && !result.getText().equals(code)) {
				code = result.getText();
			}
		}
	}

	public String getCode() {
		return code;
	}

	public void clear() {
		this.code = null;
	}
}
