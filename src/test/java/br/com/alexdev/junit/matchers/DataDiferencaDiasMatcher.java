package br.com.alexdev.junit.matchers;

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
		desc.appendText("quem leu eh gay lol");
	}

	@Override
	protected boolean matchesSafely(Date data) {
		return DataUtils.isMesmaData(data, DataUtils.obterDataComDiferencaDias(dias));
	}
	

}
