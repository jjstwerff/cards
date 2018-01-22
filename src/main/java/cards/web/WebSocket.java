package cards.web;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.eclipse.jetty.websocket.api.WriteCallback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cards.record.Area;
import cards.record.Game;

public class WebSocket implements WebSocketListener {
	private Session session;
	private final Game game;
	private Area area;
	private int x;
	private int y;
	private int z;
	private final Map<Integer, List<Pair<WebSocket, Integer>>> listen;
	private final List<Integer> blocks;

	public WebSocket(Game game, Map<Integer, List<Pair<WebSocket, Integer>>> listen) {
		this.game = game;
		this.listen = listen;
		this.blocks = new ArrayList<>();
		area = null;
		x = 0;
		y = 0;
		z = 0;
	}

	public Session getSession() {
		return session;
	}

	@Override
	public void onWebSocketClose(int statusCode, String reason) {
		this.session = null;
		for (int r : blocks) {
			Iterator<Pair<WebSocket, Integer>> it = listen.get(r).iterator();
			while (it.hasNext()) {
				Pair<WebSocket, Integer> next = it.next();
				if (next.getFirst() == this)
					it.remove();
			}
		}
		blocks.clear();
	}

	@Override
	public void onWebSocketConnect(Session session) {
		// TODO flood protection from a single address .. timeout or simple drop too many connections
		// flood protection from random addresses: drop when not known from Servlet login
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
		case "authenticate":
			try {
				MessageDigest mDigest = MessageDigest.getInstance("SHA1");
				mDigest.digest("asdasd".getBytes());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		case "information":
			// register client position to the websocket
			// find block that should be listened to
			JSONObject position = obj.getJSONObject("position");
			//area = game.getAreas().get(position.getIntValue("a"));
			x = position.getIntValue("x");
			y = position.getIntValue("y");
			z = position.getIntValue("z");
		case "request":
			// write game data
		case "edit":
		}
	}
}
