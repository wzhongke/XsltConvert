package com.sohu.secadmin.blackList;


public interface Blacklist {

	public void insert(String[] words, String rule_id, int level);
	public void insert(String[] words, String rule_id, int level, int warnLevel);
	public void insert(String[] words, String rule_id, int level, int warnLevel, long startTime, long endTime);
	public BlacklistEntry check(String[] strs);
	public BlacklistEntry check(String strs);
	public void empty();

}
