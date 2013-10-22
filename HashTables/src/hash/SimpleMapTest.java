package hash;

import static org.junit.Assert.*;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * A test for any map type. Any implementation of {@link SimpleMap} can easily
 * create a test by creating a test case that extends this class and implements
 * the {@link #createMap()} method.
 * 
 * @author William Chargin
 * 
 * @param <M>
 *            the type of map tested in this test
 */
public abstract class SimpleMapTest<M extends SimpleMap<Integer, Integer>> {

	/**
	 * The map used in this test.
	 */
	private M map;

	/**
	 * Creates an empty map to use in this test.
	 * 
	 * @return the map to use
	 */
	protected abstract M createMap();

	@Before
	public void setUp() throws Exception {
		map = createMap();
	}

	/**
	 * Adds the first {@code n} powers of two to the map, with entries of the
	 * form <em>i</em> &rarr; 2<sup><em>i</em></sup>.
	 * 
	 * @param n
	 *            the number of powers to add
	 */
	private void addPowers(int n) {
		for (int i = 0; i < n; i++) {
			map.put(i, 1 << i);
		}
	}

	@Test
	public final void testClear() {
		addPowers(10);
		map.clear();
		assertEquals(0, map.size());
	}

	@Test
	public final void testContainsKey() {
		addPowers(10);
		assertTrue(map.containsKey(3));
		assertTrue(map.containsKey(9));
		assertTrue(map.containsKey(0));
		assertFalse(map.containsKey(-1));
		assertFalse(map.containsKey(null));
		assertFalse(map.containsKey(20));
	}

	@Test
	public final void testContainsValue() {
		int toAdd = 10;
		addPowers(toAdd);
		int max = 1 << toAdd;

		for (int i = 0; i < max; i++) {
			boolean isPowerOfTwo = (i & (i - 1)) == 0 && i != 0;
			if (isPowerOfTwo) {
				assertTrue(map.containsValue(i));
			} else {
				assertFalse(map.containsValue(i));
			}
		}
	}

	@Test
	public final void testEntrySet() {
		addPowers(5);
		Set<? extends Map.Entry<Integer, Integer>> entries = map.entrySet();
		assertEquals(5, entries.size());
	}

	@Test
	public final void testGet() {
		addPowers(5);
		for (int i = 0; i < 5; i++) {
			assertEquals(1 << i, map.get(i).intValue());
		}
		assertEquals(null, map.get(null));
		assertEquals(null, map.get(10));
	}

	@Test
	public final void testPut() {
		map.put(3, 7);
		assertEquals(7, map.put(3, 2).intValue());
		assertEquals(2, map.put(3, 5).intValue());
		assertEquals(null, map.put(6, 3));
		map.remove(3);
		assertEquals(null, map.put(3, 8));
	}

	@Test
	public final void testRemove() {
		addPowers(10);
		assertEquals(1 << 5, map.remove(5).intValue());
		assertEquals(9, map.size());
		assertEquals(null, map.remove(600));
		assertEquals(null, map.remove(null));
	}

	@Test
	public final void testSize() {
		map.put(20, 1);
		assertEquals(1, map.size());
		map.put(20, 10);
		assertEquals(1, map.size());
		addPowers(5);
		assertEquals(6, map.size());
		map.remove(3);
		assertEquals(5, map.size());
		map.remove(999);
		assertEquals(5, map.size());
	}

}
