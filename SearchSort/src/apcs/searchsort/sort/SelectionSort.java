package apcs.searchsort.sort;

import java.util.List;

/**
 * Basic selection sort. Runs in O(n<sup>2</sup>) time.
 * 
 * @author William Chargin
 * 
 */
public class SelectionSort implements SortingAlgorithm {

	@Override
	public <T extends Comparable<? super T>> List<T> sort(List<T> list) {
		for (int i = 0; i < list.size(); i++) {
			// minimum index of list[i:] defaults to i
			int minimum = i;
			T minValue = list.get(i);

			for (int j = i; j < list.size(); j++) {
				T jValue = list.get(j);
				int compared = minValue.compareTo(jValue);
				// compared <=> 0 as minValue <=> jValue

				if (compared > 0) {
					// "minimum" greater than j
					minValue = jValue;
					minimum = j;
				}
			}

			// do a swap
			if (minimum != i) {
				list.set(minimum, list.get(i));
				list.set(i, minValue);
			}
		}
		return list;
	}

}
