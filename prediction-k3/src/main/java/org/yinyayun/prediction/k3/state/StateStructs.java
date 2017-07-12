/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.prediction.k3.state;

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
    private Map<String, Integer> stateJumpCounts = new HashMap<String, Integer>();
    /**
     * 某一状态下出现一个号码的次数
     */
    private Map<String, Integer> numberCountForCurrentStates = new HashMap<String, Integer>();

    /**
     * 某个状态的次数
     */
    private Map<String, Integer> stateCounts = new HashMap<String, Integer>();

    public StateStructs(int samples) {
        this.samples = samples;
    }

    public void addStateJumpCounts(String state) {
        stateJumpCounts.putIfAbsent(state, 0);
        stateJumpCounts.compute(state, (k, v) -> ++v);
    }

    public void addNumberCountForCurrentStates(String stateAndNumber) {
        numberCountForCurrentStates.putIfAbsent(stateAndNumber, 0);
        numberCountForCurrentStates.compute(stateAndNumber, (k, v) -> ++v);
    }

    public void addStateCounts(String state) {
        stateCounts.putIfAbsent(state, 0);
        stateCounts.compute(state, (k, v) -> ++v);
    }

    public Map<String, Integer> getStateJumpCounts() {
        return stateJumpCounts;
    }

    public Map<String, Integer> getNumberCountForCurrentStates() {
        return numberCountForCurrentStates;
    }

    public Map<String, Integer> getStateCounts() {
        return stateCounts;
    }

    public int getSamples() {
        return samples;
    }
}
