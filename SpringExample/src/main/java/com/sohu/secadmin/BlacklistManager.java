package com.sohu.secadmin;

import com.sohu.secadmin.blackList.Blacklist;
import com.sohu.secadmin.blackList.BlacklistEntry;
import com.sohu.secadmin.blackList.word.SubWordList;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class BlacklistManager {
	private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final String DISCARD_STRING = "!@#$%^&*()_+|\\=-`~/?.,><\"':;，、～！＠·＃￥％……＆×（）——＋《》｜＼＝－‘“”’；：？．，＞＜ 　";
	public static final char WORD_SEPERATOR = '\ue40a';

	private volatile String[] fileNames = null;
	private volatile Blacklist nonExactBl = new SubWordList();

	public static final BlacklistManager instance = new BlacklistManager();
	public static final BlacklistManager getInstance(){
		return instance;
	}
	private BlacklistManager(){
		// dummy
	}
	public void setFiles(String[]files){
		if( files == null ){
			throw new IllegalArgumentException("Parameter is null!");
		}
		fileNames = files;
	}
	/**
	 * 关键词类匹配的入口函数。根据关键词分段进行匹配，将匹配到的黑白名单数据项返回。
	 * @param str 关键词按照一定规则切分后的数组
	 * @return 如果命中了某条规则，返回对应的数据项；
	 *         否则返回null
	 */
	public BlacklistEntry checkNonExact( String str ){
		if (str == null) return null;
		int length = str.length();
		StringBuilder sb = new StringBuilder(300);
		for(int li=0; li<length; li++){
			char ch = str.charAt(li);
			if( DISCARD_STRING.indexOf(ch) >= 0 )
				continue;
			sb.append(ch);
		}
		String[] strs = {sb.toString()};
		return checkBlacklist( this.nonExactBl, strs );
	}

	private BlacklistEntry checkBlacklist( Blacklist blacklist, String[] strs ){
		BlacklistEntry ent = null;
		if( blacklist != null ){
			ent = blacklist.check(strs);
		}
		return ent;
	}

	/**
	 * 初始化函数。
	 * 将黑白名单从数据文件中加载进来
	 * @throws IOException
	 */
	public void init(){
		if( fileNames == null ) return;
		for (String filename : fileNames) {
			init(filename);
		}
	}

	/**
	 * 加载对应的黑名单文件
	 * @param filename
	 */
	public void init(String filename) {
		if (filename == null || "".equals(filename)) {
			return;
		}
		int i = 0;

		if (i < fileNames.length && filename.equals(fileNames[i++])) {
			try {
				Blacklist bl = loadSubWordFile(filename);
				if (bl != null) this.nonExactBl = bl;
			} catch (IOException e) {
				System.err.println("[WARN]Failed to Load nonExactBl blacklist from :" + filename + " for:" + e.getMessage());
			}
		}
	}

	private Blacklist loadSubWordFile( String filename ) throws IOException {
		Blacklist ret = null;
		try (BufferedReader reader = new BufferedReader( new InputStreamReader(new FileInputStream(filename), "GBK"));) {
			SubWordList sub = new SubWordList();
			String[] data = new String[6];
			for(;;){
				String line = reader.readLine();
				if( line == null ) break;
				preserveLine( line.toLowerCase(), sub, data);
			}
			ret = sub;
		}
		return ret;
	}

	private Pattern patt = Pattern.compile("" + WORD_SEPERATOR + "+");

	private int parseLine( String line, String[]data){
		int left = 0;
		int fi = 0;
		while( left < line.length() ){
			int right = line.indexOf('\t', left);

			//连续\t表示中间有空列
			if( right < 0 ) right = line.length();
			String val = line.substring(left, right );
			if( fi < data.length ){
				data[fi] = val;
			}
			left = right + 1;
			fi ++;
		}
		//如果列数不足，清空剩下的data列数据，避免影响后面的行
		for (int i = fi; i < data.length; i++) {
			data[i] = "";
		}
		return fi;
	}

	private void preserveLine( String line, SubWordList bl, String[]data ){
		parseLine( line, data);

		String rule_id = data[0];
//		String warnLevel = data[1];
		String list = data[1];
		String[]words = null;
		if( list != null ){
			words = patt.split(list);
		}
		if( words == null || words.length == 0 ) {
			System.err.println("[WARN]Invalid Line in SubWordBlacklist:" + line);
			return;
		}

		int warnLevel = -2;

		if (data.length >= 6) {
			try {
				warnLevel = Integer.parseInt(data[5]);
			} catch (Exception e) {
				warnLevel = -2;
			}
		}

		bl.insert(words, rule_id, -1, warnLevel);
	}
}
