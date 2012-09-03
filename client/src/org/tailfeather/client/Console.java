package org.tailfeather.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.tailfeather.client.model.idle.DefaultPromptIdleHandler;
import org.tailfeather.client.model.idle.PromptIdleHandler;
import org.tailfeather.client.model.idle.PromptIdleHandlerException;

public abstract class Console {
	private static final Logger LOGGER = Logger.getLogger(Console.class.getName());

	private static final String CLEAR = "\033[2J";
	private static final String HOME = "\033[H";
	private static final String CLEAR_LINE = "\033[2K";
	private static final String ATTR_RESET = "\033[m";
	private static final String ATTR_RED = "\033[31m";

	private static volatile InputStream in;
	private static volatile PrintStream out;
	private static volatile PrintStream err;

	private static volatile int columns = -1;
	private static volatile int lines = -1;

	static {
		in = System.in;
		out = System.out;
		err = System.err;
	}

	public static void setOutRate(int cps) {
		out = new PrintStream(new SlowOutputStream(System.out, cps));
	}

	public static void setErrRate(int cps) {
		err = new PrintStream(new SlowOutputStream(System.err, cps));
	}

	public static int getColumns() {
		if (columns == -1) {
			String columnsString = System.getenv("COLUMNS");
			if (columnsString == null) {
				throw new RuntimeException("Must export COLUMNS to define terminal width");
			}
			columns = Integer.parseInt(columnsString);
		}

		return columns;
	}

	public static int getLines() {
		if (lines == -1) {
			String linesString = System.getenv("LINES");
			if (linesString == null) {
				throw new RuntimeException("Must export LINES to define terminal width");
			}
			lines = Integer.parseInt(linesString);
		}

		return lines;
	}

	public static void flush() {
		synchronized (out) {
			out.flush();
		}
	}

	public static void flushError() {
		synchronized (out) {
			err.flush();
		}
	}

	public static void clear() {
		synchronized (out) {
			out.print(HOME);
			out.print(CLEAR);
			out.flush();
		}
	}

	public static void eraseLine() {
		synchronized (out) {
			out.print(CLEAR_LINE);
			out.flush();
		}
	}

	public static void printRed(char c) {
		synchronized (out) {
			out.print(ATTR_RED);
			print(c);
			out.print(ATTR_RESET);
		}
	}

	public static void printRed(String s) {
		synchronized (out) {
			out.print(ATTR_RED);
			print(s);
			out.print(ATTR_RESET);
		}
	}

	public static void printRedLine(String s) {
		synchronized (out) {
			out.print(ATTR_RED);
			printLine(s);
			out.print(ATTR_RESET);
		}
	}

	public static void print(char c) {
		synchronized (out) {
			out.print(c);
		}
	}

	public static void print(String s) {
		synchronized (out) {
			out.print(s);
		}
	}

	public static void printLine() {
		synchronized (out) {
			out.println();
		}
	}

	public static void printLine(String s) {
		synchronized (out) {
			out.println(s);
		}
	}

	public static String readLine(String prompt, int timeout, TimeUnit unit) throws TimeoutException,
			InterruptedException {
		try {
			return readLine(prompt, timeout, unit, new DefaultPromptIdleHandler());
		} catch (PromptIdleHandlerException e) {
			throw new RuntimeException("Should never happen");
		}
	}

	public static String readLine(String prompt, int timeout, TimeUnit unit, PromptIdleHandler idleHandler)
			throws TimeoutException, InterruptedException, PromptIdleHandlerException {
		ExecutorService executor = Executors.newSingleThreadExecutor();
		try {
			final Future<String> result = executor.submit(new PromptTask(prompt, idleHandler));
			try {
				return result.get(timeout, unit);
			} catch (ExecutionException e) {
				if (e.getCause() instanceof PromptIdleHandlerException) {
					throw (PromptIdleHandlerException) e.getCause();
				}

				LOGGER.log(Level.INFO, "Execution exception", e);
				return null;
			} catch (TimeoutException e) {
				result.cancel(true);
				throw e;
			}
		} finally {
			executor.shutdownNow();
		}
	}

	private static class PromptTask implements Callable<String> {
		private final String prompt;
		private final PromptIdleHandler idleHandler;

		private PromptTask(String prompt, PromptIdleHandler idleHandler) {
			this.prompt = prompt;
			this.idleHandler = idleHandler;
		}

		public String call() throws Exception {
			Console.print(prompt);
			Console.flush();

			final BufferedReader br = new BufferedReader(new InputStreamReader(Console.in));
			while (!br.ready()) {
				idleHandler.idle();
			}
			return br.readLine();
		}
	}
}