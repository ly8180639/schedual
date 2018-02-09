package org.schedual.server.handler;

public enum RateMinute {
	EVERY5_0("every5_0"),EVERY5_1("every5_1"),EVERY5_2("every5_2"),EVERY5_3("every5_3"),EVERY5_4("every5_4"),
	
	EVERY3_0("every3_0"),EVERY3_1("every3_1"),EVERY3_2("every3_2"),
	
	EVERY2_0("every2_0"),EVERY2_1("every2_1"),
	
	EVERY1_0("every1_0");
	
	private String everyminute;

	private RateMinute(String everyminute) {
		this.everyminute = everyminute;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return everyminute;
	}
}
