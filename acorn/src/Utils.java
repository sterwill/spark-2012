public class Utils {
	private static int columns = -1;
	private static int lines = -1;

	public static int getColumns() {
		if (columns == -1) {
			String columnsString = System.getenv("COLUMNS");
			if (columnsString == null) {
				throw new RuntimeException(
						"Must export COLUMNS to define terminal width");
			}
			columns = Integer.parseInt(columnsString);
		}

		return columns;
	}

	public static int getLines() {
		if (lines == -1) {
			String linesString = System.getenv("LINES");
			if (linesString == null) {
				throw new RuntimeException(
						"Must export LINES to define terminal width");
			}
			lines = Integer.parseInt(linesString);
		}

		return lines;
	}
}
