package cards.web;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.junit.Test;

import junit.framework.Assert;

public class TestJs {
	private static Function<Object, String> onMessage = null;
	private static List<String> toSend = new ArrayList<>();
	private static String socketUrl = null;
	private static ClientSocket clientSocket = null;
	private static WebSocketClient client = null;

	@Test
	public void testJsCallbacks() throws Exception {
		Assert.assertEquals("Cool", (String) TestSession.callJs("callback.js", "test1"));
	}

	public static int toCall(Function<String, String> fn) {
		return fn.apply("Hi").equals("Bye") ? 1 : 0;
	}

	public static void onWebMessage(Function<Object, String> fn) {
		onMessage = fn;
	}

	public static void connectWeb(String url) {
		socketUrl = url;
		client = new WebSocketClient();
		clientSocket = new ClientSocket();
		connectSocket();
	}

	private static void connectSocket() {
		try {
			if (!client.isStarted())
				client.start();
			client.connect(clientSocket, new URI(socketUrl), new ClientUpgradeRequest());
		} catch (Exception e) {
			new RuntimeException(e);
		}
	}

	public static void sendWebMessage(String msg) {
		if (clientSocket.session == null) {
			toSend.add(msg);
			connectSocket();
		} else
			clientSocket.session.getRemote().sendStringByFuture(msg);
	}

	public static boolean connectedWeb() {
		return clientSocket != null && clientSocket.session != null;
	}

	@WebSocket(maxTextMessageSize = 512 * 1024)
	private static class ClientSocket {
		private Session session;

		@OnWebSocketClose
		public void onClose(int statusCode, String reason) {
			this.session = null;
		}

		@OnWebSocketConnect
		public void onConnect(Session session) {
			this.session = session;
			for (String msg : toSend) {
				try {
					session.getRemote().sendString(msg);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			toSend.clear();
		}

		@OnWebSocketMessage
		public void receive(String msg) {
			if (onMessage != null)
				onMessage.apply(msg);
		}

		@OnWebSocketMessage
		public void receive(byte buf[], int offset, int length) {
			if (onMessage != null)
				onMessage.apply(ByteBuffer.wrap(buf, offset, length));
		}
	}
}
