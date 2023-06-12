package matchers;

import static java.util.Calendar.MONDAY;

public class MatchersLocais {
	
	public static DiaSemanaMatcher caiEm(Integer diaSemana) {
		return new DiaSemanaMatcher(diaSemana);
	}
	
	public static DiaSemanaMatcher caiNumaSegunda() {
		return new DiaSemanaMatcher(MONDAY);
	}
}
