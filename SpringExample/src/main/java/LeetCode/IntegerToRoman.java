package leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 *  Given an integer, convert it to a roman numeral.
 *  Input is guaranteed to be within the range from 1 to 3999.
 */

public class IntegerToRoman {

	private RomanMap [] map = {new RomanMap(1, "I"), new RomanMap(5, "V"),
		new RomanMap(10, "X"), new RomanMap(50, "L"),
		new RomanMap(100, "C"), new RomanMap(500, "D"),
		new RomanMap(1000, "M")};

	private Map<Character, Integer> romanMap = new HashMap<Character, Integer>();

	{
		romanMap.put('M', 6);
		romanMap.put('D', 5);
		romanMap.put('C', 4);
		romanMap.put('L', 3);
		romanMap.put('X', 2);
		romanMap.put('V', 1);
		romanMap.put('I', 0);

	}

	public String intToRomanMethod(int num) {
		String result = "";
		while (num > 0) {
			for (int j=map.length-1; j>=0; j--) {
				while (true) {
					int sub = num - map[j].getNumber();
					if (sub >= 0 ){
						result += map[j].getRoman();
						num = sub;
					} else {
						break;
					}
				}
			}
		}

		char [] charArr = result.toCharArray();
		result = "" + charArr[0];
		int dump = 1;
		char pre = charArr[0];
		for (int i=1; i<charArr.length; i++) {
			result += charArr[i];
			if (charArr[i] == pre) {
				dump += 1;
				if (dump == 4){
					result = result.substring(0, result.length() - dump);
					if (romanMap.get(charArr[i-dump]) - romanMap.get(charArr[i])  == 1) {
						result = result.substring(0, result.length() -1);
						result += charArr[i] +  map[romanMap.get(charArr[i-dump]) + 1].getRoman();
					} else {
						result += charArr[i] + map[romanMap.get(charArr[i]) + 1].getRoman();
					}

				}
			} else  {
				pre = charArr[i];
				dump = 1;
			}
		}
		return result;
	}

	private String [][] roman = {{"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"},
		{"X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"},
		{"C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"},
		{"M", "MM", "MMM", "", "", "", "", "", ""}};

	public String intToRoman(int num) {
		if (num <=0 || num > 3999) {
			return "";
		}

		String result = "";
		for (int toDiv = 1000, i=3; toDiv > 0 && num > 0; toDiv /= 10, i-- ) {
			int div = num / toDiv;
			if (div > 0) {
				result += roman[i][div-1];
			}
			num %= toDiv;
		}
		return  result;
	}

	private Map<Character, Integer> intMap = new HashMap<>();
	{
		intMap.put('M', 1000);
		intMap.put('D', 500);
		intMap.put('C', 100);
		intMap.put('L', 50);
		intMap.put('X', 10);
		intMap.put('V', 5);
		intMap.put('I', 1);
	}

	public int romanToInt (String roman) {
		char [] chars = roman.toCharArray();
		int result = 0;
		int len = chars.length;
		for (int i=0; i<len; i++) {
			if (i<len-1) {
				if (intMap.get(chars[i]) < intMap.get(chars[i+1])) {
					result += intMap.get(chars[i + 1]) - intMap.get(chars[i]);
					i++;
					continue;
				}
			}
			result += intMap.get(chars[i]);
		}
		return result;
	}

	public static void main(String [] args) {
		System.out.println(new IntegerToRoman().intToRoman(1952));
		System.out.println(new IntegerToRoman().romanToInt("MCMLII"));
	}

}

class RomanMap {

	public RomanMap(int number, String roman) {
		this.number = number;
		this.roman = roman;
	}

	private int  number;
	private String roman;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getRoman() {
		return roman;
	}

	public void setRoman(String roman) {
		this.roman = roman;
	}
}
