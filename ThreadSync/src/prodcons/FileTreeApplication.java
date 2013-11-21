package prodcons;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class FileTreeApplication {

	public static void main(String[] args) {
		try (Scanner sc = new Scanner(System.in)) {
			ApplicationServer<Path> s = new ApplicationServer<>(1000);
			final Index<String, Path> index = new Index<>();
			s.registerConsumer(new FileIndexingConsumer(index));
			System.out.print("Enter root path: ");
			s.registerProducer(new FileTreeProducer(Paths.get(sc.nextLine())));
			while (true) {
				System.out.print("Enter search string: ");
				String in = sc.nextLine();
				final List<Path> result = index.get(in.toLowerCase());
				System.out.println(result == null ? "0" : (result.size()
						+ " : " + result));
			}
		}
	}

}
