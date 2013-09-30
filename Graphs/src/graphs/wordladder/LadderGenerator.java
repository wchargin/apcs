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
		/*		String start = pair.s1;
				Queue<Stack<String>> q = new BasicQueue<Stack<String>>();
		
				for (int i = 0; i < start.length(); i++) {
					for (char c = 'a'; c != '{'; c= (char)((int)c + 1)){ 
						String word = start.substring(0, i) + c + start.substring(i + 1);
						if (dictionary.contains(word)) {
							Stack<String> s = new Stack<>();
							s.push(start);
							s.push(word);
							q.add(s);
						}
					}
				}
		*/
		// see http://www.cs.cmu.edu/~adamchik/15-121/labs/HW-4%20Word%20Ladder/lab.html
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	public DictionaryReader getDictionary() {
		return dictionary;
	}

	public void setDictionary(DictionaryReader dictionary) {
		this.dictionary = dictionary;
	}

}
