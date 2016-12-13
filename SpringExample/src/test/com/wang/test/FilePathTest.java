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
        // 获取项目路径
        File directory = new File("");
        String courseFile = directory.getCanonicalPath();
        System.out.println(courseFile);
    }
}
