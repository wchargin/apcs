package disjoint.test;

import disjoint.DisjointSetForest;

/**
 * A test case for a linked disjoint set.
 * 
 * @author William Chargin
 * 
 */
public class DisjointSetForestTest
		extends
		DisjointSetTest<DisjointSetForest<Integer>, DisjointSetForest<Integer>.Node> {

	/**
	 * The item used and incremented in {@link #makeItem()}.
	 */
	private int item = 0;

	@Override
	public DisjointSetForest<Integer> createSet() {
		return new DisjointSetForest<>();
	}

	@Override
	protected DisjointSetForest<Integer>.Node makeItem() {
		return set.makeNode(item++);
	}

}
