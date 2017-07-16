/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.prediction.k3.hmm.state;

import java.util.HashMap;
import java.util.Map;

/**
 * StateStructs.java 状态对象
 * 
 * @author yinyayun
 */
public class StateStructs {
    // 样本数
    private int samples;
    /**
     * 状态转移情况计数
     */
    private Map<String, Map<String, Integer>> stateJumpCounts = new HashMap<String, Map<String, Integer>>();
    /**
     * 转移状态频率
     */
    private Map<String, Integer> jumpStateCount = new HashMap<String, Integer>();
    /**
     * 历史上某个状态出现的次数
     */
    private Map<String, Integer> lastStateCounts = new HashMap<String, Integer>();
    /**
     * 某一状态下出现一个号码的次数
     */
    private Map<String, Map<String, Integer>> stateForNumberCounts = new HashMap<String, Map<String, Integer>>();

    public StateStructs(int samples) {
        this.samples = samples;
    }

    public void addStateJumpCounts(String lastState, String currentState) {
        Map<String, Integer> stateCounts = stateJumpCounts.get(lastState);
        if (stateCounts == null) {
            stateCounts = new HashMap<String, Integer>();
            stateJumpCounts.put(lastState, stateCounts);
        }
        stateCounts.putIfAbsent(currentState, 0);
        stateCounts.compute(currentState, (k, v) -> ++v);
    }

    public void addNumberCountForCurrentStates(String state, int[] number) {
        Map<String, Integer> numberCounts = stateForNumberCounts.get(state);
        if (numberCounts == null) {
            numberCounts = new HashMap<String, Integer>();
            stateForNumberCounts.put(state, numberCounts);
        }
        String numberStr = intArrayToString(number);
        numberCounts.putIfAbsent(numberStr, 0);
        numberCounts.compute(numberStr, (k, v) -> ++v);
    }

    public void addLastStateCounts(String state) {
        lastStateCounts.putIfAbsent(state, 0);
        lastStateCounts.compute(state, (k, v) -> ++v);
    }

    /**
     * 根据历史状态获取后面可能的所有状态
     * 
     * @param historyState
     * @return
     */
    public Map<String, Integer> getJumpStates(String historyState) {
        return stateJumpCounts.get(historyState);
    }

    /**
     * 获取某个状态下出现某组号码的次数
     * 
     * @param numerForstateCount
     * @return
     */
    public Map<String, Integer> getNumberCountForState(String state) {
        return stateForNumberCounts.get(state);
    }

    public void addJumpStateCount(String jumpState) {
        jumpStateCount.putIfAbsent(jumpState, 0);
        jumpStateCount.compute(jumpState, (k, v) -> ++v);
    }

    /**
     * 该转移状态出现的次数
     * 
     * @return
     */
    public int getCountForJumpState(String jumpState) {
        return jumpStateCount.get(jumpState);
    }

    public Integer getlastStateCount(String state) {
        return lastStateCounts.get(state);
    }

    public int getSamples() {
        return samples;
    }

    private String intArrayToString(int[] number) {
        StringBuilder builder = new StringBuilder();
        for (int n : number) {
            builder.append(n);
        }
        return builder.toString();
    }
}
