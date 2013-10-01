package graphs.wordladder;

import graphs.wordladder.dictionary.DictionaryReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

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
	/**
	 * Get the starting word and search through the dictionary to find all words
	 * that are one letter different. Create stacks for each of these words,
	 * containing the starting word (pushed first) and the word that is one
	 * letter different. Enqueue each of these stacks into a queue. This will
	 * create a queue of stacks! Then dequeue the first item (which is a stack)
	 * from the queue, look at its top word and compare it with the ending word.
	 * If they equal, you are done - this stack contains the ladder. Otherwise,
	 * you find all the words one letter different from it. For each of these
	 * new words create a deep copy of the stack and push each word onto the
	 * stack. Then enqueue those stacks to the queue. And so on. You terminate
	 * the process when you reach the ending word or the queue is empty.
	 * <p>
	 * You have to keep the track of used words! <strong>Otherwise an infinite
	 * loop occurs.</strong>
	 */
	public List<String> generateLadder(StringPair pair) {
		if (pair.s1.equals(pair.s2)) {
			return Arrays.<String>asList(pair.s1);
		}

		String start = pair.s1;
		Set<String> alreadyUsedWords = new HashSet<>();
		Queue<Stack<String>> q = new BasicQueue<Stack<String>>();

		for (String word : findOffByOneWords(start, alreadyUsedWords)) {
			Stack<String> s = new Stack<>();
			s.push(start);
			s.push(word);
			q.add(s);
		}

		while (!q.isEmpty()) {
			Stack<String> s = q.poll(); // dequeue();
			String top = s.peek();
			if (top.equals(pair.s2)) {
				// found it
				return new ArrayList<String>(s);
			}
			for (String word : findOffByOneWords(top, alreadyUsedWords)) {
				Stack<String> s2 = new Stack<>();
				for (String old : s) {
					s2.push(old);
				}
				s2.push(word);
				q.add(s2);
			}
		}

		// if we get here there's no answer
		return null;
	}

	private Set<String> findOffByOneWords(String word,
			Collection<? super String> alreadyUsed) {
		Set<String> words = new HashSet<>();
		for (int i = 0; i < word.length(); i++) {
			for (char c = 'a'; c != '{'; c = (char) ((int) c + 1)) {
				String thisWord = word.substring(0, i) + c
						+ word.substring(i + 1);
				if (dictionary.contains(thisWord) && alreadyUsed.add(thisWord)) {
					words.add(thisWord);
				}
			}
		}
		return words;
	}

	public DictionaryReader getDictionary() {
		return dictionary;
	}

	public void setDictionary(DictionaryReader dictionary) {
		this.dictionary = dictionary;
	}

}
