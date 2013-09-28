package graphs.core;

import java.util.HashMap;
import java.util.Map;

public class DisjointSet<T> {
	
	/**
	 * The map of each object to its parent.
	 */
	private Map<T, T> parents = new HashMap<T, T>();
	
	public void makeSet(T t) {
		if (!parents.containsKey(t)) {
			parents.put(t, t);
		}
	}
	
	public T findSet(T t) {
		T parent = parents.get(t);
		return (parent == t) ? t : findSet(parent);
	}
	
	public void union(T a, T b) {
		a = findSet(a);
		b = findSet(b);
		if (a == b) {
			return;
		}
		parents.put(a, b);
	}

}
