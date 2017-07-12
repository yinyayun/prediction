/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.prediction.k3;

import org.yinyayun.prediction.k3.state.StateDefineStrategy;
import org.yinyayun.prediction.k3.state.StateDefineStrategyBySum;
import org.yinyayun.prediction.k3.state.StateStructs;

/**
 * StateParser.java
 *
 * @author yinyayun
 */
public class StateParser {
    private StateDefineStrategy stateDefineStrategy = new StateDefineStrategyBySum();

    /**
     * 假设只关心前一个状态
     * 
     * @param numbers
     */
    public StateStructs parser(int[][] numbers) {
        StateStructs stateStructs = new StateStructs(numbers.length);
        int[] lastNumber = numbers[0];
        String lastState = stateDefineStrategy.startState();
        for (int i = 0; i < numbers.length; i++) {
            // 当前的一个状态
            String state = stateDefineStrategy.buildState(lastNumber, numbers[i]);
            stateStructs.addStateCounts(state);
            // 上一个状态到当前状态的转移
            String stateJumpString = lastState.concat("_").concat(state);
            stateStructs.addStateJumpCounts(stateJumpString);
            // 某个状态下是该组合
            String stateAndNumber = buildStatAndString(stateJumpString, numbers[i]);
            stateStructs.addNumberCountForCurrentStates(stateAndNumber);
            //
            lastNumber = numbers[i];
            lastState = state;
        }
        return stateStructs;
    }

    private String buildStatAndString(String state, int[] number) {
        StringBuilder builder = new StringBuilder();
        builder.append(state).append("_");
        for (int i : number) {
            builder.append(i);
        }
        return builder.toString();
    }

}
