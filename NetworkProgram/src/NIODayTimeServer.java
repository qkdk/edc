import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class NIODayTimeServer {
	public static int DEFAULT_PORT = 40001;

	public static void main(String args[]) {
		int port = DEFAULT_PORT;
		System.out.println("Listening for connections on port " + port);

		ServerSocketChannel serverChannel;
		Selector selector;

		try {
			serverChannel = ServerSocketChannel.open();
			ServerSocket ss = serverChannel.socket();
			InetSocketAddress address = new InetSocketAddress(port);
			ss.bind(address);
			serverChannel.configureBlocking(false);
			selector = Selector.open();
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return;
		}

		while (true) {
			try {
				selector.select();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				break;
			}

			Set<SelectionKey> readKeys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = readKeys.iterator();
			while (iterator.hasNext()) {
				SelectionKey key = iterator.next();
				iterator.remove();
				try {
					if (key.isAcceptable()) {
						ServerSocketChannel server = (ServerSocketChannel) key.channel();
						SocketChannel client = server.accept();
						System.out.println("Accepted connection from " + port);
						client.configureBlocking(false);

						SelectionKey clientKey = client.register(selector, SelectionKey.OP_WRITE);
						ByteBuffer buffer = ByteBuffer.allocate(100);
						Date now = new Date();
						String nowd = now.toString();
						buffer.put(nowd.getBytes(StandardCharsets.US_ASCII));

						clientKey.attach(buffer);
					}

					if (key.isWritable()) {
						SocketChannel client = (SocketChannel) key.channel();
						ByteBuffer output = (ByteBuffer) key.attachment();
						output.flip();
						client.write(output);
						output.compact();
						key.cancel();
						key.channel().close();
					}

				} catch (IOException ex) {
					key.cancel();
					try {
						key.channel().close();
					} catch (IOException cex) {
					}
				}
			}
		}
	}

}
