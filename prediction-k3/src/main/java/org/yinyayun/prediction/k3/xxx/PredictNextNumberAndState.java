/**
 * Copyright (c) 2017, yayunyin@126.com All Rights Reserved
 */

package org.yinyayun.prediction.k3.xxx;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.yinyayun.prediction.k3.xxx.state.StateDefineStrategy;
import org.yinyayun.prediction.k3.xxx.state.StateDefineStrategyBySum;
import org.yinyayun.prediction.k3.xxx.state.StateParserStragegy;
import org.yinyayun.prediction.k3.xxx.state.StateStructs;

/**
 * PredictNextNumberAndState 基于隐马进行预测，默认采用号码组的和值表示该组号码的状态
 * 
 * @author yinyayun
 */
public class PredictNextNumberAndState {
    private int byHistorySize;
    private StateStructs stateStructs;
    private StateDefineStrategy stateDefineStrategy = new StateDefineStrategyBySum();

    public static void main(String[] args) {
        int[][] history = new int[][]{{1, 6, 6}, {2, 3, 4}};
        List<PredictResult> res = new PredictNextNumberAndState(history.length).predict(history, 5);
        System.out.println(String.format("历史为：%s,下一期可能为：", Arrays.deepToString(history)));
        for (PredictResult predictResult : res) {
            System.out.println(predictResult);
        }
    }

    /**
     * @param byHistorySize 根据历史多少期推算下一期
     */
    public PredictNextNumberAndState(int byHistorySize) {
        this.byHistorySize = byHistorySize;
        StateParserStragegy stateParser = new StateParserStragegy(stateDefineStrategy, byHistorySize);
        try (InputStream inputStream = PredictNextNumberAndState.class.getClassLoader().getResourceAsStream("cp.txt")) {
            File tmpFile = File.createTempFile("cp-k3", "txt");
            Files.copy(inputStream, tmpFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            List<String> lines = Files.readAllLines(tmpFile.toPath(), Charset.forName("utf-8"));
            List<int[]> numbers = new ArrayList<int[]>();
            for (String line : lines) {
                int[] number = strToArrayAndSort(line.split("\t")[1]);
                numbers.add(number);
            }
            this.stateStructs = stateParser.parser(numbers);
            tmpFile.delete();
        }
        catch (Exception e) {
            throw new RuntimeException("load data error!", e);
        }
    }

    /**
     * @param dataPath 彩票数据地址
     * @param byHistorySize 根据历史多少期推算下一期
     */
    public PredictNextNumberAndState(String dataPath, int byHistorySize) {
        this.byHistorySize = byHistorySize;
        StateParserStragegy stateParser = new StateParserStragegy(stateDefineStrategy, byHistorySize);
        try {
            List<String> lines = Files.readAllLines(new File(dataPath).toPath(), Charset.forName("utf-8"));
            List<int[]> numbers = new ArrayList<int[]>();
            for (String line : lines) {
                int[] number = strToArrayAndSort(line.split("\t")[1]);
                numbers.add(number);
            }
            this.stateStructs = stateParser.parser(numbers);
        }
        catch (Exception e) {
            throw new RuntimeException("load data error!", e);
        }
    }

    /**
     * 给定历史记录
     * 
     * @param historyNumbers 该数据的长度必须和byHistorySize大小一致
     * @param topN
     * @return
     */
    public List<PredictResult> predict(int[][] historyNumbers, int topN) {
        List<PredictResult> res = predict(historyNumbers);
        Collections.sort(res, new Comparator<PredictResult>() {

            @Override
            public int compare(PredictResult o1, PredictResult o2) {
                if (o1.probability == o2.probability) {
                    return 0;
                }
                else {
                    return o1.probability > o2.probability ? -1 : 1;
                }
            }
        });
        return res.subList(0, Math.min(topN, res.size()));
    }

    public List<PredictResult> predict(int[][] historyNumbers) {
        if (historyNumbers.length != byHistorySize) {
            throw new IllegalArgumentException(
                    String.format("model train by historysize:%s,you give the numbers to predict is:%s", byHistorySize,
                            historyNumbers.length));
        }
        for (int i = 0; i < historyNumbers.length; i++) {
            Arrays.sort(historyNumbers[i]);
        }
        List<PredictResult> res = new ArrayList<PredictResult>();
        List<int[]> historyNumberList = new ArrayList<int[]>();
        for (int[] historyNumber : historyNumbers) {
            historyNumberList.add(historyNumber);
        }
        String historyState = stateDefineStrategy.buildLastState(historyNumberList);
        // 历史状态转移状态可能性组合
        Integer historyStateCount = stateStructs.getlastStateCount(historyState);
        // 历史上未有该状态
        if (historyStateCount == null) {
            return res;
        }
        Map<String, Integer> stateCounts = stateStructs.getJumpStates(historyState);
        // 后续状态的概率计算
        for (Entry<String, Integer> entry : stateCounts.entrySet()) {
            String predictState = entry.getKey();
            Integer predictStateCount = entry.getValue();
            float predictStateProbaility = predictStateCount / (float) historyStateCount;
            String stateJumpString = historyState.concat("_").concat(predictState);
            // 该状态下出现某个彩票组合的概率计算
            int jumpstateCount = stateStructs.getCountForJumpState(stateJumpString);
            Map<String, Integer> numberCounts = stateStructs.getNumberCountForState(stateJumpString);
            for (Entry<String, Integer> e : numberCounts.entrySet()) {
                String predictNumber = e.getKey();
                Integer predictNumberCount = e.getValue();
                float predictNumberProbability = predictNumberCount / (float) jumpstateCount;
                res.add(new PredictResult(predictState, predictNumber, predictStateProbaility, predictNumberProbability,
                        predictNumberProbability * predictStateProbaility));
            }
        }
        return res;
    }

    private int[] strToArrayAndSort(String str) {
        String[] splits = str.split(",");
        int[] array = new int[splits.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = Integer.valueOf(splits[i]);
        }
        Arrays.sort(array);
        return array;
    }

    public class PredictResult {
        public final String state;
        public final String number;
        public final float stateProbability;
        public final float numberProbability;
        public final float probability;

        public PredictResult(String state, String number, float stateProbability, float numberProbability,
                float probability) {
            this.state = state;
            this.number = number;
            this.stateProbability = stateProbability;
            this.numberProbability = numberProbability;
            this.probability = probability;
        }

        @Override
        public String toString() {
            return String.format("state:%s,number:%s,probability:%f*%f=%s", state, number, stateProbability,
                    numberProbability, probability);
        }
    }
}
