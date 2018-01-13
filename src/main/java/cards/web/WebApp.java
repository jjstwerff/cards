package cards.web;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.rythmengine.Rythm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

public class WebApp {
	private static final Logger logger = LoggerFactory.getLogger(WebApp.class);

	private static final int PORT = 8080;
	private static final String HOSTNAME = "localhost";

	public static void main(String[] args) throws Exception {
		rootLogger(Level.DEBUG);
		logger("org.eclipse.jetty", Level.WARN);

		HandlerList handlers = new HandlerList();

		ResourceHandler resources = new ResourceHandler();
		resources.setDirectoriesListed(false);
		resources.setResourceBase(WebApp.class.getResource("/html/").getFile());
		handlers.addHandler(resources);

		ServletHandler servlet = new ServletHandler();
		servlet.addServletWithMapping(CardsServlet.class, "/*");
		handlers.addHandler(servlet);

		handlers.addHandler(new WebSocketHandler() {
			@Override
			public void configure(WebSocketServletFactory factory) {
				factory.getPolicy().setIdleTimeout(100000);
				factory.register(MyEchoSocket.class);
			}
		});

		Map<String, Object> map = new HashMap<>();
		map.put("home.template", WebApp.class.getResource("/templates").getFile());
		Rythm.init(map);

		Server server = new Server(new InetSocketAddress(HOSTNAME, PORT));
		server.setHandler(handlers);
		server.start();
		logger.info("Url: http://localhost:" + PORT + "/");
		server.join();
	}

	private static void logger(String context, Level level) {
		((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(context)).setLevel(level);;
	}

	private static void rootLogger(Level level) {
		logger(org.slf4j.Logger.ROOT_LOGGER_NAME, level);;
	}
}
