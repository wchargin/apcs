package apcs.searchsort.sort;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of merge sort. Runs in O(n log n) time.
 * 
 * @author William Chargin
 * 
 */
public class MergeSort implements SortingAlgorithm {

	/**
	 * The sorting algorithm used for fallback; default is an insertion sort.
	 */
	private static final SortingAlgorithm fallback = new InsertionSort();

	/**
	 * The size at or below which lists will be sorted with the
	 * {@link #fallback} sorting algorithm.
	 */
	private static final int FALLBACK_THRESHOLD = 8;

	@Override
	public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
		mergeSort(list, 0, list.size());
		return list;
	}

	/**
	 * Recursive method for merge sort implementation.
	 * 
	 * @param list
	 *            the list to sort
	 * @param start
	 *            the start index (inclusive)
	 * @param end
	 *            the end index (exclusive)
	 */
	private static <T extends Comparable<? super T>> void mergeSort(
			List<T> list, int start, int end) {
		if (end - start <= FALLBACK_THRESHOLD) {
			fallback.sort(list.subList(start, end));
			return;
		}

		int median = start + (end - start) / 2;
		mergeSort(list, start, median);
		mergeSort(list, median, end);
		merge(list, start, median, end);
	}

	/**
	 * Merges the lists list[start:median] and list[median:end] such that
	 * list[start:end] is properly sorted. It is assumed that list[start:median]
	 * and list[median:end] are already sorted.
	 * 
	 * @param list
	 *            the list to merge
	 * @param start
	 *            the left-list start index (inclusive)
	 * @param median
	 *            the left-list end index (exclusive) and right-list start index
	 *            (inclusive)
	 * @param end
	 *            the right-list end index (exclusive)
	 */
	private static final <T extends Comparable<? super T>> void merge(
			List<T> list, int start, int median, int end) {
		List<T> temp = new ArrayList<T>(end - start);
		int leftIndex = start, rightIndex = median;

		while (true) {
			if (leftIndex == median) {
				// Left list is finished
				// Put the right list in
				temp.addAll(list.subList(rightIndex, end));
				break;
			} else if (rightIndex == end) {
				// Right list is finished
				// Put the left list in
				temp.addAll(list.subList(leftIndex, median));
				break;
			}

			T left = list.get(leftIndex), right = list.get(rightIndex);
			int compared = left.compareTo(right);
			// compared <=> 0 as left <=> right

			if (compared < 0) {
				// left < right
				temp.add(left);
				leftIndex++;
			} else {
				temp.add(right);
				rightIndex++;
			}
		}

		// Transfer temporary array
		// (Collections equivalent of System.arraycopy)
		for (int i = 0; i < temp.size(); i++) {
			list.set(start + i, temp.get(i));
		}
	}

}
