package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Usuario;

public class AssertTest {
	
	@Test
	public void test() {
		Assert.assertTrue(true);
		Assert.assertFalse(false);
		
		Assert.assertEquals(1, 1);
		Assert.assertEquals(0.12345, 0.12, 0.1);
		Assert.assertEquals(Math.PI, 3.14, 0.1);
		
		int i = 5;
		Integer i2 = 5;
		Assert.assertEquals(Integer.valueOf(i), i2);
		Assert.assertEquals(i, i2.intValue());
		
		String str = "bola";
		String str2 = "bola";
		String str3 = "Bola";
		Assert.assertEquals(str, str2);
		Assert.assertTrue(str.equalsIgnoreCase(str3));
		Assert.assertTrue(str.startsWith("bo"));
		
		Usuario u1 = new Usuario("user_1");
		Usuario u2 = new Usuario("user_1");
		Usuario u3 = u2;
		Usuario u4 = null;
		Assert.assertEquals(u1, u2);
		Assert.assertSame(u2, u3);
		Assert.assertNull(u4);
	}
}
