package com.wang.test;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by admin on 2016/12/13.
 */
public class FilePathTest {
    @Test
    public void testProjectPath() throws IOException {
        String tmp = "1fd%kkd_";
        tmp = tmp.replaceAll("([%_])", "\\\\\\\\$1");
        System.out.println(tmp);
    }

    @Test
    public void testPrint() {
        for (int j =1; j<=5; j++) {
            for (String s : new String[]{"djt","1.djt", "1.tc", "gd", "1.gd"}) {
                System.out.println("tupu_tj:tj0"+j+".tupu." + s+ ".ted:3010");
            }

        }
    }
}
