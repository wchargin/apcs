package graphs.wordladder;

import graphs.wordladder.dictionary.DictionaryReader;

import java.util.List;

public class LadderGenerator {

	public static class StringPair {
		private final String s1, s2;

		public StringPair(String s1, String s2) {
			super();
			this.s1 = s1;
			this.s2 = s2;
		}

		@Override
		public String toString() {
			return "StringPair [s1=" + s1 + ", s2=" + s2 + "]";
		}
		
	}

	/**
	 * The dictionary for this reader.
	 */
	private DictionaryReader dictionary;

	/**
	 * Generates a wordladder trace for the given string pair.
	 * 
	 * @param pair
	 *            the pair to trace
	 * @return the trace
	 */
	public List<String> generateLadder(StringPair pair) {
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	public DictionaryReader getDictionary() {
		return dictionary;
	}

	public void setDictionary(DictionaryReader dictionary) {
		this.dictionary = dictionary;
	}

}
