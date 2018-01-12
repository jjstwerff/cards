package cards.web;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;

public class MyEchoSocket implements WebSocketListener {
	private Session outbound;

	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		this.outbound = null;
	}

	@Override
	public void onWebSocketConnect(Session session) {
		this.outbound = session;
	}

	@Override
	public void onWebSocketError(Throwable cause) {
		cause.printStackTrace(System.err);
	}

	@Override
	public void onWebSocketBinary(byte[] payload, int offset, int len) {
		// nothing.. ignore
	}

	@Override
	public void onWebSocketText(String message) {
		if ((outbound != null) && (outbound.isOpen())) {
			outbound.getRemote().sendString("got: " + message, null);
		}
	}

}
