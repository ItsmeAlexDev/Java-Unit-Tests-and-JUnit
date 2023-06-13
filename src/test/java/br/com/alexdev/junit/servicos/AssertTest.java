package br.com.alexdev.junit.servicos;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.alexdev.junit.entidades.Usuario;

public class AssertTest {
	
	@Test
	public void test() {
		assertTrue(true);
		assertFalse(false);
		
		assertEquals(1, 1);
		assertEquals(0.12345, 0.12, 0.1);
		assertEquals(Math.PI, 3.14, 0.1);
		
		int i = 5;
		Integer i2 = 5;
		assertEquals(Integer.valueOf(i), i2);
		assertEquals(i, i2.intValue());
		
		String str = "bola";
		String str2 = "bola";
		String str3 = "Bola";
		assertEquals(str, str2);
		assertTrue(str.equalsIgnoreCase(str3));
		assertTrue(str.startsWith("bo"));
		
		Usuario u1 = new Usuario("user_1");
		Usuario u2 = new Usuario("user_1");
		Usuario u3 = u2;
		Usuario u4 = null;
		assertEquals(u1, u2);
		assertSame(u2, u3);
		assertNull(u4);
		
		i = 10;
		assertThat(i, is(equalTo(10)));
		assertThat(i, is(not(6)));
	}
}
