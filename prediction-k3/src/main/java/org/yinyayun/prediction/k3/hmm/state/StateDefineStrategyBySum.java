/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.prediction.k3.hmm.state;

import java.util.Arrays;
import java.util.List;

/**
 * StateDefineStrategyBySum.java 采用求和方式作为状态
 * 
 * @author yinyayun
 */
public class StateDefineStrategyBySum implements StateDefineStrategy {

    @Override
    public String startState() {
        return "3";
    }

    @Override
    public String buildCurrentState(int[] currentNumber) {
        return String.valueOf(Arrays.stream(currentNumber).sum());
    }

    @Override
    public String buildLastState(List<int[]> lastNumbers) {
        StringBuilder builder = new StringBuilder();
        lastNumbers.stream().map(x -> Arrays.stream(x).sum()).forEach(x -> {
            if (builder.length() > 0) {
                builder.append("_");
            }
            builder.append(x);
        });
        return builder.toString();
    }

}
