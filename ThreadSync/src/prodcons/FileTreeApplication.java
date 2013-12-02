package prodcons;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class FileTreeApplication {

	public static void main(String[] args) {
		try (Scanner sc = new Scanner(System.in)) {
			ApplicationServer<Path> s = new ApplicationServer<>(1000);
			System.out.print("Enter root path: ");
			final FileIndex index = new FileIndex(Paths.get(sc.nextLine()));
			s.registerProducer(index.createProducer());
			for (int i = 0; i < 100; i++) {
				s.registerConsumer(index.createConsumer("html", "c", "py",
						"cpp", "java"));
			}
			while (true) {
				System.out.print("Enter search string: ");
				String in = sc.nextLine();
				String[] words = in.split("\\W");
				if (words.length == 0) {
					continue;
				}
				Set<Path> result = index.get(words[0].toLowerCase());
				Set<Path> matches = null;
				if (result != null) {
					matches = new HashSet<>(result);
					for (int i = 1; i < words.length; i++) {
						result = index.get(words[i].toLowerCase());
						if (result == null) {
							matches.clear();
							break;
						}
						matches.retainAll(result);
					}
				}
				System.out.println(matches == null ? "0" : (matches.size()
						+ " : " + matches.toString().substring(0,
						Math.min(matches.toString().length(), 1000))));
			}
		}
	}
}
