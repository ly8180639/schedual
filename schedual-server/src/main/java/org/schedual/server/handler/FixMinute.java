package org.schedual.server.handler;

public enum FixMinute {
	/**
	 * 特地为5整除的分钟数定义调度器，因为需求会比较多
	 */
	FIXIN0("fixin0"),FIXIN5("fixin5"),FIXIN10("fixin10"),FIXIN15("fixin15"),
	
	FIXIN20("fixin20"),FIXIN25("fixin25"),FIXIN30("fixin30"),FIXIN35("fixin35"),
	
	FIXIN40("fixin40"),FIXIN45("fixin45"),FIXIN50("fixin50"),FIXIN55("fixin55"),
	
	/**
	 * 固定的不能整除5
	 */
	FIXIN10_1("fixin10_1"),FIXIN10_2("fixin10_2"),FIXIN10_3("fixin10_3"),
	FIXIN10_4("fixin10_4"),FIXIN10_6("fixin10_6"),FIXIN10_7("fixin10_7"),
	FIXIN10_8("fixin10_8"),FIXIN10_9("fixin10_9");
	
	private String FixMinute;

	private FixMinute(String FixMinute) {
		this.FixMinute = FixMinute;
	}
	
	@Override
	public String toString() {
		return FixMinute;
	}
}
