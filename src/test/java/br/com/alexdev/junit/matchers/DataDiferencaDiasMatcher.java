package br.com.alexdev.junit.matchers;

import static br.com.alexdev.junit.utils.DataUtils.isMesmaData;
import static br.com.alexdev.junit.utils.DataUtils.obterDataComDiferencaDias;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class DataDiferencaDiasMatcher extends TypeSafeMatcher<Date> {

	private Integer dias;
	
	public DataDiferencaDiasMatcher(Integer dias) {
		this.dias = dias;
	}
	
	public void describeTo(Description desc) {
		Date dataEsperada = obterDataComDiferencaDias(dias);
		DateFormat format = new SimpleDateFormat("dd/MM/YYYY");
		
		desc.appendText(format.format(dataEsperada));
	}

	@Override
	protected boolean matchesSafely(Date data) {
		return isMesmaData(data, obterDataComDiferencaDias(dias));
	}
	

}
