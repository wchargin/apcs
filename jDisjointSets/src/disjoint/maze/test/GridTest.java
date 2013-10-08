package disjoint.maze.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import disjoint.maze.Grid;

public class GridTest {

	/**
	 * A test grid.
	 */
	private Grid<Integer> testGrid;

	@Before
	public void setUp() throws Exception {
		testGrid = new Grid<>(10, 5);
	}

	@Test
	public void testAt() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 5; j++) {
				testGrid.set(i, j, (i * 10 + j));
			}
		}

		for (int i = 0; i < 1000; i++) {
			int x = (int) (Math.random() * 10);
			int y = (int) (Math.random() * 5);
			assertEquals("Incorrect value", (Integer) (x * 10 + y),
					testGrid.at(x, y));
		}
	}

	@Test(expected = Exception.class)
	public void testBadAt() {
		testGrid.at(100, 100);
	}

	@Test
	public void testToFormattedString() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 5; j++) {
				testGrid.set(i, j, (i * 10 + j));
			}
		}
		String expected = "+------------------------+\n"
				+ "| 0  | 1  | 2  | 3  | 4  |\n"
				+ "| 10 | 11 | 12 | 13 | 14 |\n"
				+ "| 20 | 21 | 22 | 23 | 24 |\n"
				+ "| 30 | 31 | 32 | 33 | 34 |\n"
				+ "| 40 | 41 | 42 | 43 | 44 |\n"
				+ "| 50 | 51 | 52 | 53 | 54 |\n"
				+ "| 60 | 61 | 62 | 63 | 64 |\n"
				+ "| 70 | 71 | 72 | 73 | 74 |\n"
				+ "| 80 | 81 | 82 | 83 | 84 |\n"
				+ "| 90 | 91 | 92 | 93 | 94 |\n"
				+ "+------------------------+";
		assertEquals("Grid output incorrect", expected,
				testGrid.toFormattedString());
	}
}
