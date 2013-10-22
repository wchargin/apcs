package hash;

/**
 * A test for the {@link ChainedHashMap} class.
 * 
 * @author William Chargin
 * 
 */
public class ChainedHashMapTest extends
		SimpleMapTest<ChainedHashMap<Integer, Integer>> {

	@Override
	protected ChainedHashMap<Integer, Integer> createMap() {
		return new ChainedHashMap<>(new HashCode());
	}

}
