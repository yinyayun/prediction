/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.prediction.k3.hmm.probablity;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.yinyayun.prediction.k3.hmm.state.StateStructs;

/**
 * ProbabilityTable.java 概率表：（1）某组合在某个状态下的概率（2）某个状态到某个状态的概率
 * 
 * @author yinyayun
 */
public class ProbabilityTable {
    // 状态转移概率
    private Map<String, Double> stateJumpProbability = new HashMap<String, Double>();

    public void build(String dataPath) throws Exception {
        StateParser parser = new StateParser();
        List<String> lines = Files.readAllLines(new File(dataPath).toPath(), Charset.forName("utf-8"));
        List<int[]> numbers = lines.stream().map(x -> strToArray(x.split("\t")[1])).collect(Collectors.toList());
        StateStructs stateStructs = parser.parser(numbers);
        // 状态转移概率计算
        
    }

    private int[] strToArray(String str) {
        String[] splits = str.split(",");
        int[] array = new int[splits.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = Integer.valueOf(splits[i]);
        }
        return array;
    }
}
