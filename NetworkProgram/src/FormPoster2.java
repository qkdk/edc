import java.io.*;
import java.net.*;

public class FormPoster2 {
	public static final String SERVER = "www.cafeaulait.org";
	public static final int PORT = 80;
	public static final int TIMEOUT = 15000;
	
	
	public static void main(String[] args) {
		Socket socket = null;
		try {
			socket = new Socket(SERVER, PORT);
			socket.setSoTimeout(TIMEOUT);
			OutputStream out = socket.getOutputStream();
			Writer writer = new OutputStreamWriter(out, "UTF-8");
			writer = new BufferedWriter(writer);
			
			QueryString query = new QueryString();
			query.add("3", "2");
			query.add("network", "programming");
			String string = query.toString();
			
			writer.write("POST /books/jnp4/postquery.phtml HTTP/1.1\r\n"
					+ "COOKIE: cookie1=111; cookie2=222; cookie3=333\r\n"
					+ "USER_AGENT: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36\r\n"
					+ "HOST: www.cafeaulait.org\r\n"
					+ "ACCEPT: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2\r\n"
					+ "X_FORWARDED_PROTO: http\r\n"
					+ "X_FORWARDED_FOR: 121.183.255.250\r\n"
					+ "CONNECTION: close\r\n"
					+ "Content-Length: " + string.length() + "\r\n"
		            + "Content-Type: text/html; charset=UTF-8\r\n"
					+ "\r\n");
			
			writer.write(string);
			writer.write("quit\r\n");
			writer.flush();
			
			InputStream in = socket.getInputStream();
			InputStreamReader reader = new InputStreamReader(in, "UTF-8");
			FileOutputStream fout = new FileOutputStream("return.html");

			int c;
			while ((c = reader.read()) != -1) {
				System.out.print((char) c);
				fout.write((byte) c);
			}

			fout.close();
			socket.close();

		} catch (IOException ex) {
			System.err.println(ex);
		} finally { // dispose
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException ex) {
					// ignore
				}
			}
		}
	}
}