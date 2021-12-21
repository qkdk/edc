import java.io.*;
import java.net.*;
import java.util.*;

public class RequestMethod {

	public static void main(String[] args) {

		String site = "https://thiscom.co.kr/";

		try {
			URL u = new URL(site);
			HttpURLConnection uc = (HttpURLConnection) u.openConnection();
			uc.setRequestMethod("TRACE");

			int code = uc.getResponseCode();
			String response = uc.getResponseMessage();

			for (int j = 1;; j++) {
				String header = uc.getHeaderField(j);
				String key = uc.getHeaderFieldKey(j);
				if (header == null || key == null)
					break;
				System.out.println(uc.getHeaderFieldKey(j) + ": " + header);
			}

			System.out.println();
			
			try (InputStream in = new BufferedInputStream(uc.getInputStream())) {
				// chain the InputStream to a Reader
				Reader r = new InputStreamReader(in);
				int c;
				while ((c = r.read()) != -1) {
					System.out.print((char) c);
				}
			}
			

		} catch (MalformedURLException ex) {
			System.err.println(site + " is not a URL I understand");
		} catch (IOException ex) {
			System.err.println(ex);
		}

		System.out.println();
	}

}