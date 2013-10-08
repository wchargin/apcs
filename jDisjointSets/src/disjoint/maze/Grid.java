package disjoint.maze;

import java.util.ArrayList;
import java.util.List;

/**
 * A grid of objects.
 * 
 * @author William Chargin
 * 
 * @param <T>
 *            the type of objects held in this grid
 */
public class Grid<T> {

	/**
	 * The internal grid storage.
	 */
	private List<List<T>> grid;

	/**
	 * Retrieves the value at the given position.
	 * 
	 * @param row
	 *            the row
	 * @param col
	 *            the column
	 * @return the value
	 */
	public T at(int row, int col) {
		return grid.get(row).get(col);
	}

	/**
	 * Sets the value at the given position.
	 * 
	 * @param row
	 *            the row
	 * @param col
	 *            the column
	 * @param t
	 *            the new value
	 * @return the old value
	 */
	public T set(int row, int col, T t) {
		return grid.get(row).set(col, t);
	}

	/**
	 * Creates the grid with the given number of rows and columns.
	 * 
	 * @param rows
	 *            the number of rows
	 * @param columns
	 *            the number of columns
	 */
	public Grid(int rows, int columns) {
		grid = new ArrayList<>();
		for (int i = 0; i < rows; i++) {
			List<T> outer = new ArrayList<>();
			for (int j = 0; j < columns; j++) {
				outer.add(null);
			}
			grid.add(outer);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((grid == null) ? 0 : grid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Grid<?> other = (Grid<?>) obj;
		if (grid == null) {
			if (other.grid != null)
				return false;
		} else if (!grid.equals(other.grid))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Grid [grid=" + grid + "]";
	}

	public String toFormattedString() {
		StringBuilder sb = new StringBuilder();
		if (grid.isEmpty() || grid.get(0).isEmpty()) {
			return new String();
		}
		String[][] strings = new String[grid.size()][grid.get(0).size()];
		int[] maxColumnWidth = new int[grid.get(0).size()];
		for (int i = 0; i < grid.size(); i++) {
			List<T> outer = grid.get(i);
			for (int j = 0; j < outer.size(); j++) {
				strings[i][j] = String.valueOf(outer.get(j));
				int length = strings[i][j].length();
				if (length > maxColumnWidth[j]) {
					maxColumnWidth[j] = length;
				}
			}
		}

		int totalWidth = 0;
		for (int i : maxColumnWidth) {
			totalWidth += i;
		}
		totalWidth += strings[0].length * 3 + 1; // pipes and spaces

		sb.append('+');
		for (int i = 0; i < totalWidth - 2; i++) {
			sb.append('-');
		}
		sb.append('+');
		sb.append('\n');

		for (int i = 0; i < strings.length; i++) {
			sb.append('|');
			String[] thisRow = strings[i];
			for (int j = 0; j < thisRow.length; j++) {
				sb.append(' ');
				sb.append(thisRow[j]);
				for (int k = thisRow[j].length(); k < maxColumnWidth[j]; k++) {
					sb.append(' ');
				}
				sb.append(" |");
			}
			sb.append('\n');
		}

		sb.append('+');
		for (int i = 0; i < totalWidth - 2; i++) {
			sb.append('-');
		}
		sb.append('+');

		return sb.toString();
	}
}
