/**
 * Copyright (c) 2017, yayunyin@126.com All Rights Reserved
 */

package org.yinyayun.prediction.k3.spark;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * DataTransformer 数据转为训练数据
 * 
 * @author yinyayun
 */
public class DataTransformer {

    public void transformer(String dataPath, int windowSize) {
        try {
            List<String> allLines = Files.readAllLines(new File(dataPath).toPath(), Charset.forName("utf-8"));
            List<String> lines = new ArrayList<String>();
            for (int i = windowSize; i < allLines.size(); i++) {
                String output = allLines.get(i);
                List<String> preNumbers= lines.subList(i - windowSize, i);
                preNumbers.stream()
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 定义文本的处理函数接口
     * 
     * @author yinyayun
     */
    @FunctionalInterface
    interface LineProcess {
        public String map(String line);
    }
}
