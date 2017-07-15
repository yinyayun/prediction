/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.prediction.k3.hmm.state;

import java.util.List;

/**
 * StateDefineStrategyByGap.java 和上一个样本的差值作为状态码
 * 
 * @author yinyayun
 */
public class StateDefineStrategyByGap implements StateDefineStrategy {

    @Override
    public String buildState(int[] lastNumbers, int[] currentNumbers) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < lastNumbers.length; i++) {
            builder.append(currentNumbers[i] - lastNumbers[i]);
        }
        return builder.toString();
    }

    @Override
    public String startState() {
        return "0";
    }

    @Override
    public List<String> allState() {
        return null;
    }

}
