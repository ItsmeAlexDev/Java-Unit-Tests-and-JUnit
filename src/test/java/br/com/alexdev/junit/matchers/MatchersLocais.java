package br.com.alexdev.junit.matchers;

import static java.util.Calendar.MONDAY;

public class MatchersLocais {
	
	public static DiaSemanaMatcher caiEm(Integer diaSemana) {
		return new DiaSemanaMatcher(diaSemana);
	}
	
	public static DiaSemanaMatcher caiNumaSegunda() {
		return new DiaSemanaMatcher(MONDAY);
	}
	
	public static DataDiferencaDiasMatcher isHoje() {
		return new DataDiferencaDiasMatcher(0);
	}
	
	public static DataDiferencaDiasMatcher isHojeComMaisXDias(Integer dias) {
		return new DataDiferencaDiasMatcher(dias);
	}
}
