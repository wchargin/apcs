package disjoint.test;

import static org.junit.Assert.*;

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

	@Test
	public void testRedundantUnion() {
		T x = items.get(0), y = items.get(1), z = items.get(2);
		set.union(x, y);
		set.union(x, z);
		set.union(y, z);
		set.union(z, y);
		set.union(z, x);
		assertEquals("xy parent different", set.findSet(x), set.findSet(y));
		assertEquals("yz parent different", set.findSet(y), set.findSet(z));
		assertEquals("zx parent different", set.findSet(z), set.findSet(x));
	}

	@Test
	public void testIsConnected() {
		List<T> united = new ArrayList<T>();
		final double max = Math.random() / 2 * items.size() + 1;
		for (int i = 1; i < max; i++) {
			T random = items.get((int) Math.random() * items.size());
			united.add(random);
			items.remove(random);
			set.union(united.get(0), random);
		}
		
		// make sure each AA pair is connected
		for (T t1 : united) for (T t2 : united) {
			assertTrue("False negative", set.isConnected(t1, t2));
		}
		
		items.removeAll(united);
		// make sure AB, BA pairs are nonconnected
		for (T t1 : united) for (T t2 : items){
			assertFalse("False positive", set.isConnected(t1, t2));
			assertFalse("False positive", set.isConnected(t2, t1));
		}
	}
}
