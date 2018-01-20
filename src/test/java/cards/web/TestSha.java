package cards.web;

import java.io.FileReader;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.junit.Test;

import junit.framework.Assert;

public class TestSha {
	@Test
	public void testJsSha() throws Exception {
		String msg = "Peter Parker";

		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		engine.eval(new FileReader(getClass().getResource("/html/sha.js").getFile()));
		String res = (String) ((Invocable) engine).invokeFunction("sha1", msg);
		byte[] bytes = new byte[20];
		for (int i = 0; i < res.length(); i++)
			bytes[i] = (byte) res.charAt(i);
		String result = Base64.getEncoder().encodeToString(bytes);

		MessageDigest mDigest = MessageDigest.getInstance("SHA1");
		String should = Base64.getEncoder().encodeToString(mDigest.digest(msg.getBytes()));
		Assert.assertEquals(should, result);
	}
}
