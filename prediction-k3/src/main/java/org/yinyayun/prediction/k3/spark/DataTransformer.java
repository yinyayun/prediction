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

import org.apache.commons.io.FileUtils;

/**
 * DataTransformer 数据转为训练数据
 * 
 * @author yinyayun
 */
public class DataTransformer {
    public static void main(String[] args) {
        new DataTransformer().transformer("data/cp.txt", "data/train-cp-20.txt", 20);
    }

    public void transformer(String dataPath, String savePath, int windowSize) {
        try {
            List<String> allLines = Files.readAllLines(new File(dataPath).toPath(), Charset.forName("utf-8"));
            List<String> lines = new ArrayList<String>();
            LineProcess process = x -> x.split("\t")[1];
            for (int i = windowSize; i < allLines.size(); i++) {
                String output = process.map(allLines.get(i)).replace(",", "");
                List<String> preNumbers = allLines.subList(i - windowSize, i);
                String feature = String.join(",", preNumbers.stream().map(x -> process.map(x)).toArray(String[]::new));
                lines.add(output.concat("##").concat(feature));
            }
            System.out.println("保存...");
            FileUtils.writeLines(new File(savePath), "utf-8", lines, "\n", false);
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
