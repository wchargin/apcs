package graphs.tests;

import static org.junit.Assert.assertEquals;
import graphs.wordladder.BasicQueue;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class BasicQueueTest {

	private BasicQueue<String> q;

	@Before
	public void setUp() throws Exception {
		q = new BasicQueue<>();
	}

	@Test
	public final void testSize() {
		q.add("hello");
		q.add("world");
		q.add("moar");
		assertEquals("Wrong: add", 3, q.size());
		q.offer("stuff");
		q.offer("things");
		assertEquals("Wrong: add+offer", 5, q.size());
		q.poll();
		q.peek();
		q.poll();
		assertEquals("Wrong: add+offer+peek+poll", 3, q.size());
		q.offer("stuff");
		q.add("replacement");
		assertEquals("Wrong: add+offer+peek+poll+offer+add", 5, q.size());
	}

	@Test
	public final void testOffer() {
		q.offer("string");
		assertEquals("Wrong: 1/peek", "string", q.peek());
		assertEquals("Wrong: 1/peek+poll", "string", q.poll());
		q.offer("coming");
		q.offer("through");
		assertEquals("Wrong: 3/peek", "coming", q.peek());
		assertEquals("Wrong: 3/peek+poll", "coming", q.poll());
		assertEquals("Wrong: 3/peek+2poll", "through", q.poll());
	}

	@Test
	public final void testPeek() {
		q.offer("bob");
		q.offer("the");
		q.offer("builder");
		for (int i = 0; i < 10; i++) {
			assertEquals("Wrong: " + (i + 1) + "peek", "bob", q.peek());
		}
		q.poll();
		for (int i = 0; i < 10; i++) {
			assertEquals("Wrong: " + (i + 1) + "peek", "the", q.peek());
		}
	}

	@Test
	public final void testPoll() {
		q.addAll(Arrays.asList("a", "b", "c", "d", "e", "f"));
		for (int i = 0; i < "abcdef".length(); i++) {
			assertEquals("Wrong: " + (i + 1) + "poll",
					(char) (((int) 'a') + i), q.poll().charAt(0));
		}
		assertEquals("Wrong: empty queue", null, q.poll());
	}

	@Test
	public final void testIterator() {
		String[] arr = "lorem ipsum dolor sit amet".split(" ");
		q.addAll(Arrays.<String> asList(arr));

		Iterator<String> it = q.iterator();

		for (int i = 0; it.hasNext(); i++) {
			assertEquals("Wrong: null/" + i, arr[i], it.next());
		}

		it = q.iterator();
		it.next();
		it.remove();
		for (int i = 1; it.hasNext(); i++) {
			assertEquals("Wrong: remove/" + i, arr[i], it.next());
		}

		it = q.iterator();
		for (int i = 1; it.hasNext(); i++) {
			assertEquals("Wrong: postremove/" + i, arr[i], it.next());
		}
	}
}
