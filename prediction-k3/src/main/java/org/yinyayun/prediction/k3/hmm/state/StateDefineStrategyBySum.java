/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.prediction.k3.hmm.state;

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
        int sum = 0;
        for (int n : currentNumber) {
            sum += n;
        }
        return String.valueOf(String.format("%d!%d", currentNumber[1], sum));
    }

    @Override
    public String buildLastState(List<int[]> lastNumbers) {
        StringBuilder builder = new StringBuilder();
        for (int[] number : lastNumbers) {
            String state = buildCurrentState(number);
            if (builder.length() > 0) {
                builder.append("_");
            }
            builder.append(state);
        }
        return builder.toString();
    }

}
