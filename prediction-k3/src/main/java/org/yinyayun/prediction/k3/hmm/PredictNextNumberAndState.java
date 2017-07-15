/**
 * Copyright (c) 2017, yayunyin@126.com All Rights Reserved
 */

package org.yinyayun.prediction.k3.hmm;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.yinyayun.prediction.k3.hmm.probablity.StateParser;
import org.yinyayun.prediction.k3.hmm.state.StateDefineStrategy;
import org.yinyayun.prediction.k3.hmm.state.StateDefineStrategyBySum;
import org.yinyayun.prediction.k3.hmm.state.StateStructs;

/**
 * PredictNextNumberAndState 预测下一个号码
 * 
 * @author yinyayun
 */
public class PredictNextNumberAndState {
    private StateStructs stateStructs;
    private StateDefineStrategy stateDefineStrategy = new StateDefineStrategyBySum();
    private static PredictNextNumberAndState predict;

    public static PredictNextNumberAndState getPredictInstance() {
        if (predict == null) {
            predict = new PredictNextNumberAndState("data/cp.txt");
        }
        return predict;
    }

    private PredictNextNumberAndState(String dataPath) {
        StateParser stateParser = new StateParser();
        try {
            List<String> lines = Files.readAllLines(new File(dataPath).toPath(), Charset.forName("utf-8"));
            List<int[]> numbers = lines.stream().map(x -> strToArray(x.split("\t")[1])).collect(Collectors.toList());
            this.stateStructs = stateParser.parser(numbers);
        }
        catch (Exception e) {
            throw new RuntimeException("load data error!");
        }
    }

    public Map<String, Float> predict(int[][] historyNumbers) {
        Map<String, Float> stateNumberProbability = new HashMap<String, Float>();
        StringBuilder builder = new StringBuilder();
        int[] lastNumber = {1, 1, 1};
        for (int i = 0; i < historyNumbers.length; i++) {
            if (builder.length() > 0) {
                builder.append("_");
            }
            builder.append(stateDefineStrategy.buildState(lastNumber, historyNumbers[i]));
            lastNumber = historyNumbers[i];
        }
        String historyState = builder.toString();
        // 历史状态转移状态可能性组合
        int historyStateCount = stateStructs.getHistoryStateCount(historyState);
        Map<String, Integer> stateCounts = stateStructs.getJumpStates(historyState);
        // 后续状态的概率计算
        stateCounts.forEach((predictState, predictStateCount) -> {
            float predictStateProbaility = predictStateCount / (float) historyStateCount;
            String stateJumpString = historyState.concat("_").concat(predictState);
            // 该状态下出现某个彩票组合的概率计算
            int jumpstateCount = stateStructs.getCountForJumpState(stateJumpString);
            Map<String, Integer> numberCounts = stateStructs.getNumberCountForState(stateJumpString);
            numberCounts.forEach((predictNumber, predictNumberCount) -> {
                float predictNumberProbability = predictNumberCount / (float) jumpstateCount;
                stateNumberProbability.put(predictState.concat("_").concat(predictNumber),
                        predictNumberProbability * predictStateProbaility);
            });
        });
        return stateNumberProbability;
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
