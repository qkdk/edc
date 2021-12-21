import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.util.*;
import java.io.IOException;

public class NIOEchoServer {
	public static int DEFAULT_PORT = 50000;

	public static void main(String[] args) {
		int port;
		try {
			port = Integer.parseInt(args[0]);
		} catch (RuntimeException ex) {
			port = DEFAULT_PORT;
		}

		System.out.println("Listening for connections on port " + port);
		ServerSocketChannel serverChannel;
		Selector selector;
		try {
			serverChannel = ServerSocketChannel.open();
			ServerSocket ss = serverChannel.socket();
			InetSocketAddress address = new InetSocketAddress(port);
			ss.bind(address);
			serverChannel.configureBlocking(false);// 블로킹 방식 설정
			selector = Selector.open();
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}
		while (true) {
			try {
				selector.select();
			} catch (IOException ex) {
				ex.printStackTrace();
				break;
			}
			Set<SelectionKey> readyKeys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = readyKeys.iterator();

			while (iterator.hasNext()) {
				SelectionKey key = iterator.next();
				iterator.remove();
				try {
					if (key.isAcceptable()) {
						ServerSocketChannel server = (ServerSocketChannel) key.channel(); 				// Key값에 있는 채널 뽑아냄
						SocketChannel client = server.accept(); 							// 소켓 채널 리턴
						System.out.println("Accepted connection from " + client);
						client.configureBlocking(false);

						SelectionKey clientKey = client.register(selector,
								SelectionKey.OP_WRITE | SelectionKey.OP_READ);
						ByteBuffer buffer = ByteBuffer.allocate(100);
						clientKey.attach(buffer);
					}
					if (key.isReadable()) {
						SocketChannel client = (SocketChannel) key.channel(); 					// key 에 연결된 채널 인스턴스 리넡
						ByteBuffer output = (ByteBuffer) key.attachment(); 							//
						client.read(output);
					}
					if (key.isWritable()) {
						SocketChannel client = (SocketChannel) key.channel();
						ByteBuffer output = (ByteBuffer) key.attachment();

						if (output.position() > 0 && output.get(output.position() - 1) == '\n') {
							output.flip();
							client.write(output);
							output.compact();
						}
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