package hash;

/**
 * 
 * A test for the {@link LinearProbedHashMap} class.
 * 
 * @author William Chargin
 * 
 */
public class LinearProbedHashMapTest extends
		SimpleMapTest<LinearProbedHashMap<Integer, Integer>> {

	@Override
	protected LinearProbedHashMap<Integer, Integer> createMap() {
		return new LinearProbedHashMap<>(new HashCode());
	}

}
