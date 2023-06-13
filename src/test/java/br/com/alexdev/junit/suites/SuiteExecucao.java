package br.com.alexdev.junit.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.com.alexdev.junit.servicos.CalculadoraTest;
import br.com.alexdev.junit.servicos.CalculoValorLocacaoTest;
import br.com.alexdev.junit.servicos.LocacaoServiceTest;

@RunWith(Suite.class)
@SuiteClasses({
	CalculadoraTest.class,
	CalculoValorLocacaoTest.class,
	LocacaoServiceTest.class
})
public class SuiteExecucao {}
