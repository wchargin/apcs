package hash;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * A test for the {@link BloomFilter} implementation.
 * 
 * @author William Chargin
 * 
 */
public class BloomFilterTest {

	/**
	 * The filter under test.
	 */
	BloomFilter<String> filter;

	@Before
	public void setUp() throws Exception {
		filter = new BloomFilter<>(20, 5);
	}

	@Test
	public final void testContains() {
		filter.put("dog");
		filter.put("cat");
		filter.put("fish");
		assertTrue(filter.contains("dog"));
		assertTrue(filter.contains("fish"));
	}

	@Test
	public final void testPut() {
		filter.put("frog");
		filter.put(null);
	}

}
