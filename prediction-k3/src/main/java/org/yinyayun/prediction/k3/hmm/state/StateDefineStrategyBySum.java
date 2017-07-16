/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.prediction.k3.hmm.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * StateDefineStrategyBySum.java 采用求和方式作为状态
 * 
 * @author yinyayun
 */
public class StateDefineStrategyBySum implements StateDefineStrategy {

    @Override
    public String buildState(int[] lastNumbers, int[] currentNumbers) {
        return String.valueOf(Arrays.stream(currentNumbers).reduce((x, y) -> x + y));
    }

    @Override
    public String startState() {
        return "3";
    }

    @Override
    public List<String> allState() {
        List<String> states = new ArrayList<String>();
        for (int i = 1; i < 7; i++) {
            for (int j = 1; j < 7; j++) {
                for (int k = 1; k < 7; k++) {
                    states.add(String.valueOf((i + j + k)));
                }
            }
        }
        return states;
    }
}
