package hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

/**
 * A more robust test (than {@link SimpleMapTest}). for any map type. Any
 * implementation of {@link SimpleMap} can easily create a test by creating a
 * test case that extends this class and implements the {@link #createMap()}
 * method.
 * 
 * @author William Chargin
 * 
 * @param <M>
 *            the type of map tested in this test
 */
public abstract class RobustSimpleMapTest<M extends SimpleMap<Integer, Integer>> {

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

	private enum Operation {
		PUT(0.8), GET(0.1), REMOVE(0.1);
		private static final Operation[] values = values();
		private final double probability;

		private Operation(double probability) {
			this.probability = probability;
		}

		public static Operation select(double random) {
			double total = 0;
			for (Operation value : values) {
				if ((total += value.probability) >= random) {
					return value;
				}
			}
			return null;
		}
	}

	@Test
	public final void testFunctionality() {
		for (int n = 0; n < 1000; n++) {
			// assume standard Java map works
			Map<Integer, Integer> builtin = new HashMap<Integer, Integer>();
			map.clear();
			Random r = new Random(n);
			for (int i = 0; i < (1 << 15); i++) {
				Operation op = Operation.select(r.nextDouble());
				final int k = r.nextInt(), v = r.nextInt();
				switch (op) {
				case PUT:
					builtin.put(k, v);
					map.put(k, v);
					break;
				case GET:
					final Integer built = builtin.get(k);
					final Integer ours = map.get(k);
					assertEquals(built, ours);
					break;
				case REMOVE:
					builtin.remove(k);
					map.remove(k);
					break;
				default:
					fail("Unsupported operation " + op);
				}
			}
		}

	}
}
