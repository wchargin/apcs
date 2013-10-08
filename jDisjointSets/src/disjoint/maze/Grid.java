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

}
