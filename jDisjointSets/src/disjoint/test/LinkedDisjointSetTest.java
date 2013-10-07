package disjoint.test;

import disjoint.LinkedDisjointSet;

/**
 * A test case for a linked disjoint set.
 * 
 * @author William Chargin
 * 
 */
public class LinkedDisjointSetTest
		extends
		DisjointSetTest<LinkedDisjointSet<Integer>, LinkedDisjointSet<Integer>.Node> {

	/**
	 * The item used and incremented in {@link #makeItem()}.
	 */
	private int item = 0;

	@Override
	public LinkedDisjointSet<Integer> createSet() {
		return new LinkedDisjointSet<>();
	}

	@Override
	protected LinkedDisjointSet<Integer>.Node makeItem() {
		return set.makeNode(item++);
	}

}
