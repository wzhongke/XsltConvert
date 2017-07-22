package com.sohu.secadmin.blackList.word;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Pattern;

import com.sohu.secadmin.blackList.Blacklist;
import com.sohu.secadmin.blackList.BlacklistEntry;

public class SubWordList implements Blacklist{

	private ArrayList<BlacklistEntry<String[]>> wordList;
	private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

	public SubWordList(){
		this(10);
	}
	public SubWordList(int n){
		wordList = new ArrayList<>(n);
	}
	public void insert(String[] words, String rule_id, int level){
		insert(words, rule_id, level, BlacklistEntry.WARN_LEVEL_INVALID);
	}
	public void insert(String[] words, String rule_id, int level, int warnLevel){
		insert(words, rule_id, level, warnLevel, 0, 0);
	}
	public void insert(String[] words, String rule_id, int level, int warnLevel, long startTime, long endTime){
		if( words == null || words.length == 0 ) return;
		BlacklistEntry<String[]> entry = new BlacklistEntry<>();
		entry.key = words;
		entry.id = rule_id;
		entry.value = level;
		entry.warnLevel = warnLevel;
		entry.startTime = startTime;
		entry.endTime = endTime;
		rwLock.writeLock().lock();
		wordList.add( entry );
		rwLock.writeLock().unlock();
	}
	public BlacklistEntry check( String[] words ){
		if( words == null || words.length == 0 ) return null;
		rwLock.readLock().lock();
		BlacklistEntry<String[]> ent = null;
		boolean allMatched = false;
		for( int i = 0; i < wordList.size(); i++ ){
			ent = wordList.get(i);
			String[] terms = ent.key;
			int ti = 0;
			// 轮询每一个关键词
			for( ; ti < terms.length; ti ++ ){
				int wi = 0;
				for( ; wi < words.length; wi ++ ){
					// 如果待匹配的词的长度比黑名单该项的词的长度小，那么继续轮询
					if( words[wi].length() < terms[ti].length() ){
						continue;
					}
					// 如果待匹配词中包含黑名单词
					if( words[wi].indexOf( terms[ti] ) >= 0 ){
						break;
					}
				}
				if( wi == words.length ){ // 没找到
					break;
				}
			}

			// thisMathed说明本条规则，对每条查询词都试用
			if( ti == terms.length ){
				allMatched = true;
				break;
			}
		}
		rwLock.readLock().unlock();

		return allMatched ? ent : null;
	}

	public static final void quickSortByLength(String[] pData, int left, int right) {

		int i = left;
		int j = right;
		String middle = pData[(left + right) / 2];
		do {
			while ((pData[i].length() < (middle).length()) && (i < right))
				i++;
			while ((pData[j].length() > (middle).length()) && (j > left))
				j--;
			if (i <= j) {
				String strTemp = pData[i];
				pData[i] = pData[j];
				pData[j] = strTemp;

				i++;
				j--;
			}
		} while (i <= j);// 如果两边扫描的下标交错，就停止（完成一次）

		if (left < j)
			quickSortByLength(pData, left, j);

		if (right > i)
			quickSortByLength(pData, i, right);
	}
	public void empty() {
		rwLock.writeLock().lock();
		this.wordList.clear();
		rwLock.writeLock().unlock();
	}
	private static final Pattern patt = Pattern.compile(" +");
	public BlacklistEntry check(String strs) {
		return check(patt.split(strs) );
	}


}
