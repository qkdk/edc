
import java.net.*;
import java.io.*;

public class Whois {

	public final static int DEFAULT_PORT = 43;
	public final static String DEFAULT_HOST = "whois.internic.net";
	private int port = DEFAULT_PORT;
	private InetAddress host;

	public Whois(InetAddress host, int port) {
		this.host = host;
		this.port = port;
	}

	public Whois(InetAddress host) {
		this(host, DEFAULT_PORT);
	}

	public Whois(String hostname, int port) throws UnknownHostException {
		this(InetAddress.getByName(hostname), port);
	}

	public Whois(String hostname) throws UnknownHostException {
		this(InetAddress.getByName(hostname), DEFAULT_PORT);
	}

	public Whois() throws UnknownHostException {
		this(DEFAULT_HOST, DEFAULT_PORT);
	}

	public String lookUpNames(String target)
			throws IOException {
		
		Socket socket = new Socket();
		try {
			SocketAddress address = new InetSocketAddress(host, port);
			socket.connect(address);
			Writer out = new OutputStreamWriter(socket.getOutputStream(), "ASCII");
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			
			out.write(target + "\r\n");
			out.flush();
			StringBuilder response = new StringBuilder();
			String theLine = null;
			while ((theLine = in.readLine()) != null) {
				response.append(theLine);
				response.append("\r\n");
			}
			return response.toString();
		} finally {
			socket.close();
		}
	}

	public static void main(String args[]) throws IOException {
		Whois whois = new Whois("whois.kr");
		
		String response = whois.lookUpNames("kaist.ac.kr");
		
		System.out.println(response);
	}
}
