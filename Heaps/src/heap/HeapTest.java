package heap;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

/**
 * A test framework for the {@link Heap} class.
 * 
 * @author William Chargin
 * 
 */
public class HeapTest {

	/**
	 * The heap used for this test.
	 */
	private Heap<Integer> heap;

	@Before
	public void setUp() throws Exception {
		heap = Heap.fromNaturalOrder();
	}

	@Test
	public final void testAdd() {
		assertTrue(heap.add(6));
		assertTrue(heap.contains(6));
		assertTrue(heap.add(0));
		assertTrue(heap.contains(0));
		assertTrue(heap.add(-1));
		assertTrue(heap.add(-1));
		assertTrue(heap.contains(-1));
	}

	@Test
	public final void testAddAll() {
		List<Integer> toAdd = Arrays.asList(3, 6, 0, -5, 8);
		heap.addAll(toAdd);
		for (int i : toAdd) {
			assertTrue(heap.contains(i));
		}
	}

	@Test
	public final void testClear() {
		List<Integer> toAdd = Arrays.asList(3, 6, 0, -5, 8);
		heap.addAll(toAdd);
		heap.clear();
		assertTrue(heap.isEmpty());
		for (int i : toAdd) {
			assertFalse(heap.contains(i));
		}
	}

	@Test
	public final void testContains() {
		List<Integer> toAdd = Arrays.asList(3, 6, 0, -5, 8);
		heap.addAll(toAdd);
		for (int i = -100; i < 100; i++) {
			assertEquals(toAdd.contains(i), heap.contains(i));
		}
	}

	@Test
	public final void testContainsAll() {
		List<Integer> list = new ArrayList<>(Arrays.asList(3, 6, 0, -5, 8));
		heap.addAll(list);
		// / 8 6 3 0 -5
		// / 8
		// 6 3
		// 0 -5
		assertTrue(heap.containsAll(list));
		list.remove(0);
		assertTrue(heap.containsAll(list));
		list.add(999);
		assertFalse(heap.containsAll(list));
	}

	@Test
	public final void testPoll() {
		List<Integer> toAdd = Arrays.asList(3, 6, 0, -5, 8);
		heap.addAll(toAdd);
		int max = Collections.max(toAdd);
		assertTrue(heap.contains(max));
		heap.poll();
		assertFalse(heap.contains(max));
	}

	@Test
	public final void testPeek() {
		for (int i = 0; i < 10; i++) {
			List<Integer> list = new ArrayList<>();
			for (int j = 0; j < 100; j++) {
				list.add((int) (Math.random() * 50));
			}
			heap.addAll(list);
			assertEquals(Collections.max(list), heap.peek());
			heap.clear();
		}
	}

	@Test
	public final void testFromNaturalOrder() {
		assertNotNull(heap);
		Heap<String> stringheap = Heap.<String> fromNaturalOrder();
		assertNotNull(stringheap);
		// nothing really much to test here
	}

	@Test
	public final void testIsEmpty() {
		assertTrue(heap.isEmpty());
		heap.add(8);
		assertFalse(heap.isEmpty());
		heap.remove(8);
		assertTrue(heap.isEmpty());
		heap.add(8);
		heap.add(9);
		heap.add(2);
		assertFalse(heap.isEmpty());
		heap.clear();
		assertTrue(heap.isEmpty());
	}

	@Test
	public final void testIterator() {
		List<Integer> list = Arrays.asList(6, 2, 1, -6, -1, 0, 10);
		for (int i : list) {
			heap.add(i);
		}

		Set<Integer> shouldBe = new HashSet<>(list);
		Set<Integer> is = new HashSet<>(heap);

		assertEquals(shouldBe, is);
	}

	@Test
	public final void testRemove() {
		List<Integer> toAdd = Arrays.asList(3, 6, 0, -5, 8);
		heap.addAll(toAdd);
		assertTrue(heap.contains(6));
		assertTrue(heap.remove(6));
		assertFalse(heap.contains(6));
		assertFalse(heap.remove(6));
		assertFalse(heap.remove(10));
	}

	@Test
	public final void testRemoveAll() {
		List<Integer> toAdd = Arrays.asList(3, 6, 0, -5, 8);
		heap.addAll(toAdd);
		List<Integer> toRemove = Arrays.asList(3, 2, -5);
		assertTrue(heap.removeAll(toRemove));
		for (int removed : toRemove) {
			assertFalse(heap.contains(removed));
		}
		assertFalse(heap.removeAll(toRemove));
	}

	@Test
	public final void testRetainAll() {
		List<Integer> toAdd = Arrays.asList(3, 6, 0, -5, 8);
		heap.addAll(toAdd);
		List<Integer> toRetain = Arrays.asList(3, 2, -5);
		assertTrue(heap.retainAll(toRetain));
		for (int retained : toRetain) {
			assertTrue(heap.contains(retained) == toAdd.contains(retained));
		}
		for (int added : toAdd) {
			assertTrue(heap.contains(added) == toRetain.contains(added));
		}
		assertFalse(heap.retainAll(toRetain));
	}

	@Test
	public final void testSize() {
		List<Integer> toAdd = Arrays.asList(3, 6, 0, -5, 8);
		heap.addAll(toAdd);
		assertEquals(toAdd.size(), heap.size());
		heap.addAll(toAdd);
		assertEquals(toAdd.size() * 2, heap.size());
		heap.remove(toAdd.get(0));
		assertEquals(toAdd.size() * 2 - 1, heap.size());
	}

	@Test
	public final void testToArray() {
		List<Integer> toAdd = Arrays.asList(3, 6, 0, -5, 8);
		heap.addAll(toAdd);
		Object[] arr = heap.toArray();
		Arrays.sort(arr);
		Collections.sort(toAdd);
		assertArrayEquals(toAdd.toArray(), arr);
	}

	@Test
	public final void testToArrayTArray() {
		List<Integer> toAdd = Arrays.asList(
				3, 6, 0, -5, 8);
		heap.addAll(toAdd);
		Integer[] arr = heap.toArray(new Integer[toAdd.size()]);
		Arrays.sort(arr);
		Collections.sort(toAdd);
		assertEquals(Arrays.<Integer> asList(arr), toAdd);
	}

}