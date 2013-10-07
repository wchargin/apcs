package disjoint;

/**
 * A data structure with a collection of disjoint dynamic sets, each identified
 * by some member of the set called the "representative." This data structure
 * supports the fundamental operations {@link #makeSet(Object) makeSet},
 * {@link #union(Object, Object) union}, and {@link #findSet(Object) findSet}.
 * 
 * @author William Chargin
 * 
 * @param <T>
 *            the type of objects held in the sets
 */
public interface DisjointSet<T> {

	/**
	 * Creates a new set whose only member (and thus representative) is
	 * {@code t}.
	 * 
	 * @param t
	 *            the member and representative of the new set
	 */
	public void makeSet(T t);

	/**
	 * Unites the dynamic sets that contain {@code x} and {@code y} into a new
	 * set that is the union of these two sets. The original sets of which
	 * {@code x} and {@code y} are members will be destroyed.
	 * 
	 * @param x
	 *            an element of the first set to unite
	 * @param y
	 *            an element of the second set to unite
	 */
	public void union(T x, T y);

	/**
	 * Returns the representative of the set containing {@code t}.
	 * 
	 * @param t
	 *            an element of some set
	 * @return the representative of the set of which {@code t} is an element,
	 *         or {@code null} if {@code t} is not in any set
	 */
	public T findSet(T t);

}
