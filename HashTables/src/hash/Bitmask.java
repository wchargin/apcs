package hash;

import java.util.Arrays;

/**
 * A bitmask of arbitrary length.
 * 
 * @author William Chargin
 * 
 */
public class Bitmask {

	/**
	 * The internal storage of this bitmask.
	 */
	private boolean[] mask;

	/**
	 * Creates a bitmask of the given size. All bits will be initialized to
	 * {@code false}.
	 * 
	 * @param size
	 *            the number of bits to store
	 */
	public Bitmask(int size) {
		this.mask = new boolean[size];
		Arrays.fill(mask, false);
	}

	/**
	 * Performs a bitwise-and operation on this bitmask with the given bitmask.
	 * The other bitmask will not be affected.
	 * 
	 * @param other
	 *            the bitmask to <em>and</em> with
	 */
	public void and(Bitmask other) {
		for (int i = 0; i < mask.length; i++) {
			mask[i] &= other.mask[i];
		}
	}

	/**
	 * Sets this bitmask to its complement. Each bit will be flipped.
	 */
	public void complement() {
		for (int i = 0; i < mask.length; i++) {
			mask[i] = !mask[i];
		}
	}

	/**
	 * Sets each bit in the mask to the given value.
	 * 
	 * @param value
	 *            the new value
	 */
	public void fill(boolean value) {
		Arrays.fill(mask, value);
	}

	/**
	 * Flips the given bit by setting it to {@code true} if it was {@code false}
	 * or {@code false} if it was {@code true}.
	 * 
	 * @param bit
	 *            the index to flip
	 * @return the old value
	 */
	public boolean flip(int bit) {
		return put(bit, !get(bit));
	}

	/**
	 * Gets the value of the given bit.
	 * 
	 * @param bit
	 *            the index to get
	 * @return the old value
	 */
	public boolean get(int bit) {
		return mask[bit];
	}

	/**
	 * Performs a bitwise-or operation on this bitmask with the given bitmask.
	 * The other bitmask will not be affected.
	 * 
	 * @param other
	 *            the bitmask to <em>or</em> with
	 */
	public void or(Bitmask other) {
		for (int i = 0; i < mask.length; i++) {
			mask[i] |= other.mask[i];
		}
	}

	/**
	 * Sets the given bit to the given value.
	 * 
	 * @param bit
	 *            the index to change
	 * @param value
	 *            the new value
	 * @return the old value
	 */
	public boolean put(int bit, boolean value) {
		boolean old = mask[bit];
		mask[bit] = value;
		return old;
	}

	/**
	 * Sets the given bit to {@code true}.
	 * 
	 * @param bit
	 *            the index to set
	 * @return the old value
	 */
	public boolean set(int bit) {
		return put(bit, true);
	}

	/**
	 * Gets the length of this bitmask.
	 * 
	 * @return the length
	 */
	public int size() {
		return mask.length;
	}

	/**
	 * Sets the given bit to {@code false}.
	 * 
	 * @param bit
	 *            the index to unset
	 * @return the old value
	 */
	public boolean unset(int bit) {
		return put(bit, false);
	}

	/**
	 * Performs a bitwise-xor operation on this bitmask with the given bitmask.
	 * The other bitmask will not be affected.
	 * 
	 * @param other
	 *            the bitmask to <em>xor</em> with
	 */
	public void xor(Bitmask other) {
		for (int i = 0; i < mask.length; i++) {
			mask[i] ^= other.mask[i];
		}
	}

}
