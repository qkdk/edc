import java.io.*;
import java.net.*;

public class SourceViewer {
	public static void main(String[] args) {
		InputStream in = null;
		try {
			// Open the URL for reading
			URL u = new URL("https://www.hanbat.ac.kr/images/kor/main/images1.jpg");
			in = u.openStream();
			// buffer the input to increase performance
			in = new BufferedInputStream(in);
			// chain the InputStream to a Reader
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("data.jpg"));
			
			int c;
			while ((c = in.read()) != -1) {
				out.write(c);
			}
			
			
		
			
		} catch (MalformedURLException ex) {
			System.err.println(args[0] + " is not a parseable URL");
		} catch (IOException ex) {
			System.err.println(ex);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}
}