package br.com.alexdev.junit.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.ce.wcaquino.servicos.AssertTest;
import br.com.alexdev.junit.servicos.CalculadoraMockTest;
import br.com.alexdev.junit.servicos.CalculadoraTest;
import br.com.alexdev.junit.servicos.CalculoValorLocacaoTest;
import br.com.alexdev.junit.servicos.LocacaoServiceTest;

@RunWith(Suite.class)
@SuiteClasses({
	AssertTest.class,
	CalculadoraMockTest.class,
	CalculadoraTest.class,
	CalculoValorLocacaoTest.class,
	LocacaoServiceTest.class
})
public class SuiteExecucao {}
