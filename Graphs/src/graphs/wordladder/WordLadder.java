package graphs.wordladder;

import graphs.wordladder.LadderGenerator.StringPair;
import graphs.wordladder.dictionary.DictionaryReader;
import graphs.wordladder.dictionary.FileDictionary;
import graphs.wordladder.dictionary.WebDictionary;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The WordLadder driver class.
 * 
 * @author William Chargin
 * 
 */
public class WordLadder {

	/**
	 * @param args
	 *            of the form
	 *            {@code fileOrURL, pair1a, pair1b, [..., pairNa, pairNb]}
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err
					.println("WordLadder: first argument should be a URL or file path");
			System.exit(1);
		} else if (args.length == 1 || (args.length - 1) % 2 != 0) {
			System.err
					.println("WordLadder: must have odd arguments; non-first should be pairs of words");
		}

		System.out.println("WordLadder begun.");

		DictionaryReader dict;

		File file = new File(args[0]);

		if (file.exists()) {
			dict = new FileDictionary(file);
			System.out.println("Successfully created file dictionary.");
		} else {
			try {
				URL u = new URL(args[0]);
				dict = new WebDictionary(u);
				System.out.println("Successfully created web dictionary.");
			} catch (MalformedURLException murle) {
				throw new IllegalArgumentException("URL does not exist: "
						+ args[0], murle);
			}
		}

		Set<StringPair> pairs = new HashSet<>();
		for (int i = 1; i < args.length; i += 2) {
			StringPair pair = new StringPair(args[i], args[i + 1]);
			pairs.add(pair);
			System.out.println("Found word pair: " + pair);
		}

		LadderGenerator lg = new LadderGenerator();
		lg.setDictionary(dict);

		System.out
				.println("Created ladder dictionary. Beginning generation...");

		for (StringPair pair : pairs) {
			System.out.println("Generating ladder for: " + pair);
			List<String> ladder = lg.generateLadder(pair);
			if (ladder == null) {
				System.out.println("No successful path was found.");
			} else {
				System.out.println("Ladder found:");
				for (String trace : ladder) {
					System.out.println(" * " + trace);
				}
			}
			System.out.println();
		}

		System.out.println("WordLadder complete.");
		System.out.println("Exiting normally.");
		System.exit(0);
	}

}
