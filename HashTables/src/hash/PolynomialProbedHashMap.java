package hash;

/**
 * A hash map that uses arbitrary polynomial probing to resolve collisions. This
 * may break on certain polynomial configurations due to infinite remap
 * failures. Choose your constants carefully.
 * 
 * @author William Chargin
 * 
 * @param <K>
 *            the type of keys stored in this map
 * @param <V>
 *            the type of values stored in this map
 */
public class PolynomialProbedHashMap<K, V> extends ProbedHashMap<K, V> {

	/**
	 * Checks the coefficients array for validity, and may throw an exception if
	 * validity checks fail. Validity checks include nonzero length and nonzero
	 * leading coefficient.
	 * 
	 * @param coefficients
	 *            the coefficients array
	 * @throws IllegalArgumentException
	 *             if the array is invalid
	 */
	private static void checkCoefficientsArray(int[] coefficients)
			throws IllegalArgumentException {
		if (coefficients.length < 1) {
			throw new IllegalArgumentException(
					"No coefficients provided; at least one required");
		}
		if (coefficients[0] == 0) {
			throw new IllegalArgumentException(
					"Leading coefficient must be nonzero");
		}
		for (int coefficient : coefficients) {
			if (coefficient < 0) {
				throw new IllegalArgumentException(
						"Coefficients must be nonnegative (" + coefficient
								+ ")");
			}
		}
	}

	/**
	 * The coefficients for the polynomial.
	 */
	private final int[] coefficients;

	/**
	 * Creates the map with the given hash function. The provided coefficients
	 * will be used for the polynomial.
	 * 
	 * @param hasher
	 *            the hash function to use
	 * @param loadFactor
	 *            the {@linkplain #loadFactor load factor} for the map
	 * @param coefficients
	 *            the coefficients, in order of decreasing polynomial order
	 */
	public PolynomialProbedHashMap(HashFunction<? super K> hasher,
			float loadFactor, int... coefficients) {
		super(hasher, loadFactor);
		checkCoefficientsArray(coefficients);
		this.coefficients = coefficients;
	}

	/**
	 * Creates the map with the given hash function and default load factor. The
	 * provided coefficients will be used for the polynomial.
	 * 
	 * @param hasher
	 *            the hash function to use
	 * @param coefficients
	 *            the coefficients, in order of decreasing polynomial order
	 */
	public PolynomialProbedHashMap(HashFunction<? super K> hasher,
			int... coefficients) {
		super(hasher);
		checkCoefficientsArray(coefficients);
		this.coefficients = coefficients;
	}

	@Override
	protected int getProbeValue(final int i) {
		int polynomial = 0;
		for (int j = 0; j < coefficients.length; j++) {
			int order = coefficients.length - j - 1;
			polynomial += (Math.pow(i, order)) * coefficients[j];
		}
		return polynomial;
	}

}
