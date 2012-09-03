package org.tailfeather.client;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SlowOutputStream extends FilterOutputStream {

	private final long period;
	private long lastPrinted = -1;

	public SlowOutputStream(OutputStream out, int bytesPerSecond) {
		super(out);
		this.period = (long) (1000f / bytesPerSecond);
	}

	@Override
	public void write(int b) throws IOException {
		slow();
		super.write(b);
		flush();
	}

	@Override
	public void write(byte[] bytes) throws IOException {
		for (byte b : bytes) {
			write(b);
		}
	}

	@Override
	public void write(byte[] bytes, int off, int len) throws IOException {
		for (int i = off; i < off + len; i++) {
			write(bytes[i]);
		}
	}

	private void slow() {
		final long wait = lastPrinted + period - System.currentTimeMillis();
		if (wait <= 0) {
			lastPrinted = System.currentTimeMillis();
			return;
		}

		try {
			Thread.sleep(wait);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
