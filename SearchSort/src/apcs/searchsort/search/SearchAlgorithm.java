package apcs.searchsort.search;

import java.util.List;

/**
 * A searching algorithm.
 * 
 * @author William Chargin
 * 
 */
public interface SearchAlgorithm {

	/**
	 * Finds the given element in the given list and returns the index at which
	 * it occurs.
	 * 
	 * @param list
	 *            the list to search
	 * @param target the item to search for
	 * @return the index of the item in the list, or {@code -1} if not found
	 */
	public <T extends Comparable<? super T>> int indexOf(List<T> list, T target);

}
