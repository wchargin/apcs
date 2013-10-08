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

}
