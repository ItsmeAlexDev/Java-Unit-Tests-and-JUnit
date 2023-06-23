package br.com.alexdev.junit.servicos;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.ce.wcaquino.runners.ParallelRunner;

@RunWith(ParallelRunner.class)
public class CalculadoraTest{
	
	private int a;
	private int b;
	private Calculadora calc;
	private int resultado;
	
	@Before
	public void setup() {
		a = 0;
		b = 0;
		calc = new Calculadora();
		resultado = 0;
	}
	
	@Test
	public void somaDoisValores() {
		a = 3;
		b = 5;
		
		resultado = calc.soma(a, b);
		
		assertEquals(8, resultado);
	}
	
	@Test
	public void subtraiDoisValores() {
		a = 8;
		b = 5;
		
		resultado = calc.subtrai(a, b);
				
		assertEquals(3, resultado);
	}
	
	@Test
	public void divideDoisValores() {
		a = 6;
		b = 3;
		
		resultado = calc.divide(a, b);
		
		assertEquals(2, resultado);
	}
	
	@Test(expected = ArithmeticException.class)
	public void exceptionDivisaoPorZero() {
		a = 10;
		b = 0;
		
		calc.divide(a, b);
	}
	
	@Test
	public void multiplicaDoisValores() {
		a = 6;
		b = 3;
		
		resultado = calc.multiplica(a, b);
		
		assertEquals(18, resultado);
	}
	
}
