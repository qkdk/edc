import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {
	public static int DEFAULT_PORT = 45653;

	public static void main(String[] args) {

		int port;
		port = DEFAULT_PORT;

		ExecutorService pool = Executors.newFixedThreadPool(50);

		try (ServerSocket server = new ServerSocket(port)) {
			while (true) {
				Socket connection = server.accept();
				Callable<Void> task = new EchoTask(connection);
				pool.submit(task);
			}
		} catch (IOException ex) {
		}

	}

	private static class EchoTask implements Callable<Void> {

		private Socket connection;

		EchoTask(Socket connection) {
			this.connection = connection;
		}

		@Override
		public Void call() throws Exception {
			// TODO Auto-generated method stub

			try {

				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));

				String readData = "";
				
				while (true) {
					readData = in.readLine();
					out.write(readData + "\r\n");
					out.flush();
				}

			} finally {
				connection.close();
			}
		}
	}
}
