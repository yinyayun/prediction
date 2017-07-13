/**
 * Copyright (c) 2017, yayunyin@126.com All Rights Reserved
 */

package org.yinyayun.prediction.k3.spark;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * LabelMapper 采用SVM训练后，标签定义
 * 
 * @author yinyayun
 */
public class LabelMapper {
    // 标签对应的彩票号码
    public static Map<String, Integer> numberToLabels = new HashMap<String, Integer>();
    // 彩票对应的标签
    public static Map<Integer, String> labelToNumbers = new HashMap<Integer, String>();

    static {
        int label = -1;
        IntStream.range(1, 7).forEach(i -> IntStream.range(1, 7).forEach(j -> IntStream.range(1, 7).forEach(k -> {
            String number = String.format("%d%d%d", i, j, k);
            labelToNumbers.put(label, number);
            numberToLabels.put(number, label);
        })));
    }
}
