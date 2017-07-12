/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.prediction.k3.state;

import java.util.Arrays;

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
        return "000";
    }
}
