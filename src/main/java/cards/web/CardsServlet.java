package cards.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rythmengine.Rythm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cards.record.Game;

public class CardsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(CardsServlet.class);
	private final Game game;

	public CardsServlet(Game game) {
		this.game = game;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getRequestURI().equals("/index.html")) {
			logger.debug(request.getRequestURI());
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println("<h1>Hello from HelloServlet</h1>");
		} else if (request.getRequestURI().equals("/tab.html")) {
			logger.debug(request.getRequestURI());
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(Rythm.render(request.getRequestURI(), game));
		} else {
			logger.info("Unknown " + request.getRequestURI());
			response.sendError(404);
		}
	}
}
