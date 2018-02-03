package cards.web;

import java.util.function.Function;

import org.junit.Test;

import junit.framework.Assert;

public class TestJs {
	@Test
	public void testJsSha1() throws Exception {
		Assert.assertEquals("Cool", (String) TestSession.callJs("callback.js", "test1"));
	}

	public static int toCall(Function<String, String> fn) {
		return fn.apply("Hi").equals("Bye") ? 1 : 0;
	}
}
