package disjoint.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import disjoint.DisjointSet;

/**
 * A test for an arbitrary disjoint set implementation.
 * 
 * @author William Chargin
 * 
 * @param <S>
 *            the disjoint set implementation tested
 * @param <T>
 *            the type of objects held in the tested set
 */
public abstract class DisjointSetTest<S extends DisjointSet<T>, T> {

	/**
	 * The disjoint set used in these tests.
	 */
	protected S set;

	/**
	 * A list of items in the set at the start of each test.
	 */
	private List<T> items;

	/**
	 * Creates a set on which to run tests.
	 * 
	 * @return a new disjoint set
	 */
	public abstract S createSet();

	@Before
	public final void setUp() {
		set = createSet();
		items = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			T item = makeItem();
			items.add(item);
			set.makeSet(item);
		}
	}

	/**
	 * Makes an item that can be put in a disjoint set. The items created by
	 * subsequent calls to this method should be unique.
	 * 
	 * @return an item
	 */
	protected abstract T makeItem();

	@Test
	public void testSingletons() {
		for (T item : items) {
			assertEquals("Singleton is not its own representative", item,
					set.findSet(item));
		}
	}

	@Test
	public void testUnion() {
		Collections.shuffle(items);
		for (int i = 0; i < 19; i++) {
			set.union(items.get(i), items.get(i + 1));
		}

		List<T> sub = items.subList(0, 21);
		for (T item : sub) {
			assertTrue("Item's representative not in set",
					sub.contains(set.findSet(item)));
		}

	}
}
