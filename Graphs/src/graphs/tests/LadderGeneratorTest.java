package graphs.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import graphs.wordladder.LadderGenerator;
import graphs.wordladder.LadderGenerator.StringPair;
import graphs.wordladder.dictionary.DictionaryReader;
import graphs.wordladder.dictionary.InputStreamDictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class LadderGeneratorTest {

	private DictionaryReader dict;

	@Before
	public void setUp() throws Exception {
		dict = new InputStreamDictionary(
				LadderGeneratorTest.class
						.getResourceAsStream("/graphs/wordladder/dictionary/dictionary.txt"));
	}

	@Test
	public final void testGenerateLadder() {
		LadderGenerator gen = new LadderGenerator();
		gen.setDictionary(dict);

		try (BufferedReader in = readerFor("/graphs/tests/laddergenerator_input.txt");
				BufferedReader expected = readerFor("/graphs/tests/laddergenerator_expected.txt")) {
			String inline = null, expectline = null;
			while ((inline = in.readLine()) != null
					&& (expectline = expected.readLine()) != null) {
				String[] split = inline.split(" ");
				StringPair p = new StringPair(split[0], split[1]);
				List<String> trace = gen.generateLadder(p);
				String[] expectedSplit = expectline.split(", ");
				if (expectedSplit[0].equals("")) {
					// don't know why this happens
					expectedSplit = new String[] {};
				}
				if (expectedSplit.length == 0) {
					// should be no path
					assertEquals("Should be \"no path\" but path found", null, trace);
				} else {
					assertEquals("Length is incorrect for " + p, expectedSplit.length, trace.size());
					assertEquals("Contents incorrect for " + p, Arrays.<String>asList(expectedSplit), trace);
				}
			}
		} catch (IOException ie) {
			fail("Required files not found.");
		}
	}

	private static final BufferedReader readerFor(String filename) {
		return new BufferedReader(new InputStreamReader(LadderGeneratorTest.class.getResourceAsStream(filename)));
	}
}
