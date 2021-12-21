import java.io.*;
import java.net.*;

public class AllHeaders {
	public static void main(String[] args) {
		String site = "http://localhost:8888/";
		try {
			URL u = new URL(site);
			URLConnection uc = u.openConnection();
			for (int j = 1;; j++) {
				String header = uc.getHeaderField(j);
				if (header == null)
					break;
				System.out.println(uc.getHeaderFieldKey(j) + ": " + header);
			}
		} catch (MalformedURLException ex) {
			System.err.println(site + " is not a URL I understand.");
		} catch (IOException ex) {
			System.err.println(ex);
		}
		System.out.println();

	}
}