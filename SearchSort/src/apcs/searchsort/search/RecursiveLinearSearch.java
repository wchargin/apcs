package apcs.searchsort.search;

import java.util.List;

/**
 * A recursive linear search implementation. Runs in O(<sup>n</sup>) time.
 * However, this is a <em>very silly algorithm</em> because it quickly maxes out
 * the recursion call stack on all but the smallest lists.
 */
public class RecursiveLinearSearch implements SearchAlgorithm {

	@Override
	public <T extends Comparable<? super T>> int indexOf(List<T> list, T target) {
		return indexOf(list, target, 0);
	}

	/**
	 * A helper method for linear search. Tests the {@code index} and recurses
	 * up if no match found.
	 * 
	 * @param list
	 *            the list to search
	 * @param target
	 *            the target object
	 * @param index
	 *            the index to test
	 * @return the final position in the list, or {@code -1} if not in the list
	 */
	private static <T extends Comparable<? super T>> int indexOf(List<T> list,
			T target, int index) {
		return index >= list.size() ? -1
				: matches(target, list.get(index)) ? index : indexOf(list,
						target, index + 1);
	}

	/**
	 * Tests if two things match. This is used to account for the null case.
	 * 
	 * @param o1
	 *            the first thing
	 * @param o2
	 *            the second thing
	 * @return if they match
	 */
	private static boolean matches(Object o1, Object o2) {
		return (o1 == null && o2 == null) || (o1.equals(o2));
	}
}