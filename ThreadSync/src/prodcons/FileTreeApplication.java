package prodcons;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class FileTreeApplication {

	public static void main(String[] args) {
		try (Scanner sc = new Scanner(System.in)) {
			ApplicationServer<Path> s = new ApplicationServer<>(1000);
			final Index<String, Path> index = new Index<>();
			for (int i = 0; i < 100; i++) {
				s.registerConsumer(new FileIndexingConsumer(index,Charset.forName("UTF-8"),"html"));
			}
			System.out.print("Enter root path: ");
			s.registerProducer(new FileTreeProducer(Paths.get(sc.nextLine())));
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
