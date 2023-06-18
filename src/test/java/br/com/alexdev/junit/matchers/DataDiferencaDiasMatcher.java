package br.com.alexdev.junit.matchers;

import static br.com.alexdev.junit.utils.DataUtils.obterDataComDiferencaDias;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import br.com.alexdev.junit.utils.DataUtils;

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
		return DataUtils.isMesmaData(data, obterDataComDiferencaDias(dias));
	}
	

}
