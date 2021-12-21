

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Hw2_2 {
   public static void main(String[] args) {
      try {
         Socket socket = new Socket("www.cafeaulait.org", 80);
         OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
         
         String body = "&name=Hwang+Sungyu&email=hsk980911%40hanbat.ac.kr&phone+number=010-5586-6937";
         
         out.write("POST /books/jnp4/postquery.phtml HTTP/1.1\r\n"
               + "COOKIE: cookie1=aaa; cookie2=2222\r\n"
               + "USER_AGENT: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.118 Whale/2.11.126.23 Safari/537.36\r\n"
               + "HOST: www.cafeaulait.org\r\n"
               + "ACCEPT: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2\r\n"
               + "X_FORWARDED_PROTO: http\r\n"
               + "X_FORWARDED_FOR: 203.230.104.207\r\n"
               + "CONNECTION: close\r\n"
               + "Content-Length: " + body.length() + "\r\n"
               + "Content-Type: text/html; cjarset=UTF-8\r\n"
               + "\r\n");
         out.flush();
         out.write(body);
         out.flush();
         
         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
         OutputStream outFile = new FileOutputStream("return2.html");
         
         int c;
         while((c = in.read()) != -1) {
            System.out.print((char)c);
            outFile.write((byte)c);
         }
         outFile.flush();
         socket.close();
      } catch (UnknownHostException ex) {
         System.err.println(ex);
      } catch (IOException er) {
         System.out.println("IO exception error = " + er);
      }
   }
}