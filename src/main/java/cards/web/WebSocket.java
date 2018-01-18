package cards.web;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.eclipse.jetty.websocket.api.WriteCallback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cards.record.Game;

public class WebSocket implements WebSocketListener {
	private Session session;
	private final Game game;
	private final Map<Integer, List<Pair<WebSocket, Integer>>> listen;
	private final List<Integer> registered;

	public WebSocket(Game game, Map<Integer, List<Pair<WebSocket, Integer>>> listen) {
		this.game = game;
		this.listen = listen;
		this.registered = new ArrayList<>();
	}

	public Session getSession() {
		return session;
	}

	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		this.session = null;
		for (int r : registered)
			listen.get(r).remove(this);
		registered.clear();
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
		JSONObject obj = JSON.parseObject(message);
		String request = obj.getString("request");
		switch (request) {
		case "information":
			// register client position to the websocket
			// find block that should be listened to
		case "request":
			// write game data
		case "edit":
		}
	}
}
