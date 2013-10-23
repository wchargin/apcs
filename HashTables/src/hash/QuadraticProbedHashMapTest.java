package hash;

/**
 * 
 * A test for the {@link QuadraticProbedHashMap} class.
 * 
 * @author William Chargin
 * 
 */
public class QuadraticProbedHashMapTest extends
		SimpleMapTest<QuadraticProbedHashMap<Integer, Integer>> {

	@Override
	protected QuadraticProbedHashMap<Integer, Integer> createMap() {
		return new QuadraticProbedHashMap<>(new HashCode());
	}

}
