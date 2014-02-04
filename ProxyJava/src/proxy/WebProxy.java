package proxy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Semaphore;

public class WebProxy {

	private static Semaphore logLock = new Semaphore(1);
	private static BufferedWriter logStream;
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");

	private static Semaphore globalDataLock = new Semaphore(0);

	private static void log(String format, Object... args) {
		try {
			logLock.acquire();

			String now = DATE_FORMAT.format(new Date());
			logStream.write(String.format("%s - ", now));

			final String out = String.format(format, args);
			logStream.write(out);
			logStream.write('\n');

			logStream.flush();

			System.out.println(out);

			logLock.release();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println("argument should be a port in [1024, 65536]");
			System.exit(1);
		}
		int port = Integer.parseInt(args[0]);
		if (port < 1024 || port > 65536) {
			System.err.println("port out of range: " + port);
			System.exit(2);
		}

		File f = new File("proxy.log");
		logStream = new BufferedWriter(new FileWriter(f));
		log("Server starting, logging to %s.", f.getAbsolutePath());

		ServerSocket serv = new ServerSocket(port);
		log("Server socket opened on port %s.", serv.getLocalPort());

		while (serv != null && serv.isBound() && !serv.isClosed()) {
			Socket client = serv.accept();

		}

	}
}
