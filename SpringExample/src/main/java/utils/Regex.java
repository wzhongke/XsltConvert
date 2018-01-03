package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {

	public static void main(String [] args) {
		Pattern pattern = Pattern.compile("^(http://|https://)[^/]+/$");

		Matcher match = pattern.matcher("http://www.sogou.com/");

		if (match.matches()) {
			System.out.println("match");
		}
	}
}
