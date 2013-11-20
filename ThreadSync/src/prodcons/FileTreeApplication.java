package prodcons;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileTreeApplication {

	public static void main(String[] args) {
		ApplicationServer<Path> s = new ApplicationServer<>(100);
		s.registerConsumer(new FileReadConsumer());
		s.registerProducer(new FileTreeProducer(Paths
				.get("H:\\git\\blender")));
	}

}
