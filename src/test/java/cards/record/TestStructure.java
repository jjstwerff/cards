package cards.record;

import org.junit.Test;

import com.betterbe.memorydb.meta.Tests;

import cards.generate.CardsStructure;
import junit.framework.Assert;

public class TestStructure extends Tests {
	@Test
	public void testProjectStructure() {
		Assert.assertEquals(content(this, "testProjectStructure.txt"), CardsStructure.getProject().toString());
	}
}
