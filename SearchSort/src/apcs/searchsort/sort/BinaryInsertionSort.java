package apcs.searchsort.sort;

import java.util.List;

/**
 * Binary insertion sort. Probably runs in O(n log n) time.
 * 
 * @author William Chargin
 * 
 */
public class BinaryInsertionSort implements SortingAlgorithm {

	@Override
	public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
		for (int i = 1; i < list.size(); i++) {
			T item = list.get(i);

			int indexToInsert = binaryInsertionFinder(list, item, 0, i);

			list.remove(i);
			list.add(indexToInsert, item);
		}
		return list;
	}

	/**
	 * Modified binary search to find the optimal insertion position of an item.
	 * 
	 * @param list
	 *            the list to search
	 * @param item
	 *            the item to insert
	 * @param start
	 *            the starting location
	 * @param end
	 *            the ending location
	 * @return the index at which the item should be inserted
	 */
	private static <T extends Comparable<? super T>> int binaryInsertionFinder(
			List<T> list, T item, int start, int end) {
		if (end <= start) {
			// It's not in the list
			return start;
		}

		// How this method works:
		// Either $item \in list$ or not. If it is, then keep going until you
		// find it (standard binary search). If not, keep going until you get
		// "stuck." Then, just insert it there. Thus, keep track of the middle
		// position and the previous middle position; if they're the same, then
		// stuckness has been achieved (as each iteration of the loop is
		// independent of previous iterations) and you can return the middle
		// index.

		int lastMiddle = -2;
		int middle = -1;
		while (lastMiddle != middle) {
			lastMiddle = middle;
			middle = start + (end - start) / 2;
			T atMiddle = list.get(middle);

			int compared = atMiddle.compareTo(item);
			// compared <=> 0 as atMiddle <=> target

			if (compared > 0) {
				// atMiddle > target; target in the left half
				end = middle - 1;
			} else if (compared < 0) {
				// atMiddle < target; target in the right half
				start = middle + 1;
			} else {
				// All done!
				return middle;
			}
		}
		return middle;

	}

}
