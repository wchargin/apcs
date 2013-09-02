package apcs.searchsort.sort;

import java.util.List;

/**
 * A quicksort implementation that uses the midpoint as the pivot.
 * 
 * @author William Chargin
 * 
 */
public class MidpointQuickSort extends AbstractQuickSort {

	@Override
	protected <T extends Comparable<? super T>> Comparable<? super T> selectPivot(
			List<T> list, int start, int end) {
		return list.get(start + (end - start) / 2);
	}

}
