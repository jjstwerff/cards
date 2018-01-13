package cards.web;

import java.io.BufferedWriter;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.rythmengine.Rythm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.betterbe.memorydb.file.DBParser;
import com.betterbe.memorydb.file.Write;
import com.betterbe.memorydb.structure.Store;

import cards.record.Game;
import ch.qos.logback.classic.Level;

public class WebApp {
	private static final Logger logger = LoggerFactory.getLogger(WebApp.class);

	private static final int PORT = 8080;
	private static final String HOSTNAME = "localhost";

	public static void main(String[] args) throws Exception {
		rootLogger(Level.DEBUG);
		logger("org.eclipse.jetty", Level.WARN);
		logger("org.rythmengine.RythmEngine", Level.INFO);

		Store store = new Store(20);
		Game imp = new Game(store);
		String file = WebApp.class.getResource("/data/db.txt").getFile();
		try (DBParser parser = new DBParser(file)) {
			imp.parse(parser);
		}
		Game game = imp.new IndexGameName().iterator().next();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try (BufferedWriter newBufferedWriter = Files.newBufferedWriter(Paths.get("/tmp/db.txt"))) {
					game.output(new Write(newBufferedWriter), 99);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		HandlerList handlers = new HandlerList();

		ResourceHandler resources = new ResourceHandler();
		resources.setDirectoriesListed(false);
		resources.setResourceBase(WebApp.class.getResource("/html/").getFile());
		handlers.addHandler(resources);

		ServletHandler servlet = new ServletHandler();
		servlet.addServletWithMapping(new ServletHolder(new CardsServlet(game)), "/*");
		handlers.addHandler(servlet);

		handlers.addHandler(new WebSocketHandler() {
			@Override
			public void configure(WebSocketServletFactory factory) {
				factory.getPolicy().setIdleTimeout(100000);
				factory.register(MyEchoSocket.class);
			}
		});

		Map<String, Object> map = new HashMap<>();
		map.put("rythm.engine.mode", "dev");
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
