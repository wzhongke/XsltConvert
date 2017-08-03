package xml.parse;

import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.util.List;

/**
 * Created by admin on 2017/4/28.
 */
public class XMLParsUtil {

    /**
     * 如果可以转换成数字，返回转换后的数字，否则返回0
     * @param parse ： 待转换的数字
     * @return
     */
    public static int parseInt (String parse) {
        if (parse == null) return 0;
        try {
            return Integer.parseInt(parse);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static String flat(List<String> list, String spliter) {
        if (spliter == null) {
            return list.stream().reduce("", (tmp, x) -> tmp + x);
        }
        String result =  list.stream().reduce("", (tmp, x) -> tmp + spliter + x);
        return result.substring(spliter.length());
    }

    public static String getFromBase64(String s) {
        if ( s == null) return "";
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte [] b = decoder.decodeBuffer(s);
            return new String(b, "utf8");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static void main(String [] args) {
        System.out.println(getFromBase64("54m55pyX5pmu"));

    }
}
