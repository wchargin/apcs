package apcs.searchsort.sort;

import java.util.Comparator;
import java.util.List;

/**
 * A sorting algorithm.
 * 
 * @author William Chargin
 * 
 */
public interface SortingAlgorithm {

	/**
	 * Sorts the given list in ascending order.
	 * 
	 * @param list
	 *            the list to sort
	 * @return the list, for convenience
	 */
	public <T extends Comparator<? super T>> List<T> sort(List<T> list);

}
