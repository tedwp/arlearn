package org.celstec.arlearn2.fusion;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class CSV {
	private static final Pattern CSV_VALUE_PATTERN = Pattern
			.compile("([^,\\r\\n\"]*|\"(([^\"]*\"\")*[^\"]*)\")(,|\\r?\\n)");

	Vector<String> header = new Vector<String>();
	Vector<Vector<String>> rows = new Vector<Vector<String>>();

	public CSV(InputStream source, String charsetName) {
		Scanner scanner = new Scanner(source, "UTF-8");
		boolean first = true;
		while (scanner.hasNextLine()) {
			if (first) {
				processFirstLine(scanner);
				first = false;
			} else {
				processOtherLine(scanner);
			}
		}
	}

	private void processFirstLine(Scanner scanner) {
		int i = 0;
		while (scanner.hasNextLine()) {
			scanner.findWithinHorizon(CSV_VALUE_PATTERN, 0);
			MatchResult match = scanner.match();
			String quotedString = match.group(2);
			String decoded = quotedString == null ? match.group(1)
					: quotedString.replaceAll("\"\"", "\"");
			header.add(i++, decoded);
			if (!match.group(4).equals(",")) {
				return;
			}
		}
	}

	private void processOtherLine(Scanner scanner) {
		Vector row = new Vector();
		while (scanner.hasNextLine()) {
			if (!rows.contains(row))
				rows.add(row);
			scanner.findWithinHorizon(CSV_VALUE_PATTERN, 0);
			MatchResult match = scanner.match();
			String quotedString = match.group(2);
			String decoded = quotedString == null ? match.group(1)
					: quotedString.replaceAll("\"\"", "\"");
			// System.out.print("|" + decoded);
			row.add(decoded);
			if (!match.group(4).equals(",")) {
				return;
			}
		}
	}

	public int rowSize() {
		return rows.size();
	}

	public int columnSize() {
		return header.size();
	}

	public String getColumnName(int i) {
		return header.elementAt(i);
	}

	public int getColumnIndex(String value) {
		int i = 0;
		for (String columnValue : header) {
			if (columnValue.equals(value))
				return i;
			i++;
		}
		return -1;
	}

	public String getValue(int row, int column) {
		try {
			return rows.get(row).get(column);
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public String getValue(int row, String columnName) {
		try {
			return rows.get(row).get(getColumnIndex(columnName));
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	public boolean containsValue(int column, String value) {
		for (Vector<String> row : rows) {
			if (row.get(column).equals(value))
				return true;
		}
		return false;
	}
	
	public boolean containsValue(String columnName, String value) {
		return containsValue(getColumnIndex(columnName), value);
	}
	
	public int rowIdForValue(int column, String value) {
		int i = 0;
		for (Vector<String> row : rows) {
			if (row.get(column).equals(value))
				return i;
			i++;
		}
		
		return -1;
	}
	
	public int rowIdForValue(String columnName, String value) {
		return rowIdForValue(getColumnIndex(columnName), value);
	}
	
	public Iterator<HashMap<String, String>> iterator() {
		Iterator<HashMap<String, String>> result = new Iterator<HashMap<String, String>>() {

			private int i = 0;
			
			public boolean hasNext() {
				return i < rowSize();
			}

			
			public HashMap<String, String> next() {
				HashMap<String, String> hm = new HashMap<String, String>();
				for (int j = 0 ; j < columnSize(); j++) {
					hm.put(getColumnName(j), getValue(i, j));
				}
				i++;
				return hm;
			}

			
			public void remove() {
				
			}
			
		};
		
		return result;
	}
	
}
