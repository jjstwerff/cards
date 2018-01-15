package cards.web;

import java.nio.ByteBuffer;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.eclipse.jetty.websocket.api.WriteCallback;

public class WebSocket implements WebSocketListener {
	private Session session;

	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		this.session = null;
	}

	@Override
	public void onWebSocketConnect(Session session) {
		this.session = session;
	}

	@Override
	public void onWebSocketError(Throwable cause) {
		cause.printStackTrace(System.err);
	}

	@Override
	public void onWebSocketBinary(byte[] payload, int offset, int len) {
		ByteBuffer data = ByteBuffer.allocate(12123);
		session.getRemote().sendBytes(data, new WriteCallback() {
			@Override
			public void writeFailed(Throwable x) {
			}

			@Override
			public void writeSuccess() {
			}
		});
	}

	@Override
	public void onWebSocketText(String message) {
		if ((session != null) && (session.isOpen())) {
			session.getRemote().sendString("got: " + message, null);
		}
	}
}
