package cards.web;

import java.io.FileReader;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Random;
import java.util.stream.LongStream;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import junit.framework.Assert;

public class TestSession {
	private static final LongStream random = new Random(new Date().getTime()).longs();

	@Test
	public void testJsSha1() throws Exception {
		String msg = "Peter Parker";
		String should = Hex.encodeHexString(MessageDigest.getInstance("SHA1").digest(msg.getBytes()));
		String was = hexString((String) callJs("session.js", "sha1str", msg));
		Assert.assertEquals(should, was);
	}

	@Test
	public void testSession() throws Exception {
		// start WebSocket
		String secret = Long.toString(random.findFirst().getAsLong());
		callJs("session.js", "session", secret, "admin");
	}

	/* Call the specified function in the specified javascript file */
	private Object callJs(String file, String function, Object... parameters) throws Exception {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		engine.eval(new FileReader(getClass().getResource("/html/" + file).getFile()));
		return ((Invocable) engine).invokeFunction(function, parameters);
	}

	/* Return a hex dump of each individual 8 byte character on the given binary string */
	private String hexString(String str) {
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) < 16)
				res.append("0");
			res.append(Integer.toHexString(str.charAt(i)));
		}
		return res.toString();
	}
}
