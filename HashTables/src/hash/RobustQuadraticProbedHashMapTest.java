package hash;

/**
 * A test for the {@link QuadraticProbedHashMap} class.
 * 
 * @author William Chargin
 * 
 */
public class RobustQuadraticProbedHashMapTest extends
		RobustSimpleMapTest<QuadraticProbedHashMap<Integer, Integer>> {

	@Override
	protected QuadraticProbedHashMap<Integer, Integer> createMap() {
		return new QuadraticProbedHashMap<>(new HashCode());
	}

}
