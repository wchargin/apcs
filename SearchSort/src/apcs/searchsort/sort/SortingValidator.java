package apcs.searchsort.sort;

import java.util.Collections;
import java.util.List;

/**
 * A class to
 * 
 * @author William
 * 
 */
public final class SortingValidator {

	/**
	 * @deprecated There is no need to instantiate this class. Use the static
	 *             method {@link #isValid(SortingAlgorithm, List)} instead.
	 */
	private SortingValidator() {
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
			SortingAlgorithm sa, List<T> list) {
		Collections.shuffle(list);
		sa.sort(list);
		for (int i = 1; i < list.size(); i++) {
			if (list.get(i).compareTo(list.get(i - 1)) < 0) {
				return false;
			}
		}
		return true;
	}

}
