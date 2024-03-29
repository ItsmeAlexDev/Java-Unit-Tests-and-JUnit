package br.com.alexdev.junit.matchers;

import static br.com.alexdev.junit.utils.DataUtils.verificarDiaSemana;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.LONG;
import static java.util.Calendar.getInstance;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;


public class DiaSemanaMatcher extends TypeSafeMatcher<Date> {

	private Integer diaSemana;
	
	public DiaSemanaMatcher(Integer diaSemana) {
		this.diaSemana = diaSemana;
	}
	
	public void describeTo(Description desc) {
		Calendar data = getInstance();
		data.set(DAY_OF_WEEK, diaSemana);
		String dataExtenso = data.getDisplayName(DAY_OF_WEEK, LONG, new Locale("pt", "BR"));
		desc.appendText(dataExtenso);
	}

	@Override
	protected boolean matchesSafely(Date data) {
		return verificarDiaSemana(data, diaSemana);
	}


}
