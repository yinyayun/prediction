/**
 * Copyright (c) 2017, yayunyin@126.com All Rights Reserved
 */

package org.yinyayun.prediction.k3.spark;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * LabelMapper 采用训练标签定义
 * 
 * @author yinyayun
 */
public class LabelMapper implements Serializable {
    private static final long serialVersionUID = 1L;
    // 标签对应的彩票号码
    public static Map<String, Integer> numberToLabels = new HashMap<String, Integer>();
    // 彩票对应的标签
    public static Map<Integer, String> labelToNumbers = new HashMap<Integer, String>();

    static {
        int label = -1;
        for (int i = 1; i < 7; i++) {
            for (int j = 1; j < 7; j++) {
                for (int k = 1; k < 7; k++) {
                    label = label + 1;
                    String number = String.format("%d%d%d", i, j, k);
                    labelToNumbers.put(label, number);
                    numberToLabels.put(number, label);
                }
            }
        }
    }
}
