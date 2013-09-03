package apcs.searchsort.search;

import java.util.List;

/**
 * A class to ensure that a search algorithm is valid.
 * 
 * @author William Chargin
 * 
 */
public final class SearchValidator {

	/**
	 * @deprecated There is no need to instantiate this class. Use the static
	 *             method {@link #isValid(SearchAlgorithm, List)} instead.
	 */
	private SearchValidator() {
	}

	/**
	 * Validates the sorting algorithm on the given list by shuffling it,
	 * sorting it with the algorithm, and ensuring the correct order.
	 * 
	 * @param sa
	 *            the algorithm to sort
	 * @param list
	 *            the list to test on (order will be consumed)
	 * @return {@code true} if the algorithm is valid on this list, or
	 *         {@code false} if it fails
	 */
	public static <T extends Comparable<? super T>> boolean isValid(
			SearchAlgorithm sa, List<T> list) {
		int[] indicesToTest = { 0, list.size() / 2,
				(int) (Math.random() * list.size()) /* random item */};
		for (int i : indicesToTest) {
			// get the item, or null if out of bounds
			T t = (i < 0 ? null : list.get(i));
			if (sa.indexOf(list, t) != i) {
				return false;
			}
		}
		return true;
	}
}
