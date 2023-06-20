package br.com.alexdev.junit.servicos;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;

public class CalculadoraMockTest {
	
	@Mock
	private Calculadora calcMock;
	
	@Spy
	private Calculadora calcSpy;
	
	@Before
	public void setup() {
		initMocks(this);
	}
	
	@Test
	public void mockESpy() {
		
		when(calcMock.soma(1, 2)).thenReturn(4);
		System.out.println("Mock: " + calcMock.soma(1, 2));
		System.out.println("Mock: " + calcMock.soma(1, 5));
		
		when(calcMock.soma(1, 5)).thenCallRealMethod();
		System.out.println("Mock: " + calcMock.soma(1, 5));
		
		
//		when(calcSpy.soma(1, 2)).thenReturn(3);
		doReturn(4).when(calcSpy).soma(1, 2);
		System.out.println("Spy: " + calcSpy.soma(1, 2));
		System.out.println("Spy: " + calcSpy.soma(1, 5));
		
	}
	
	@Test
	public void test() {
		Calculadora calc = mock(Calculadora.class);
		
		ArgumentCaptor<Integer> argCapt = forClass(Integer.class);
		when(calc.soma(argCapt.capture(), argCapt.capture())).thenReturn(11);
		
		assertEquals(11, calc.soma(1234, 5678));
//		System.out.println(argCapt.getAllValues());
	}
}
