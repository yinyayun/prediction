/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.prediction.k3.hmm.state;

import java.util.List;

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
    public StateStructs parser(List<int[]> numbers) {
        StateStructs stateStructs = new StateStructs(numbers.size());
        int[] lastNumber = numbers.get(0);
        String lastState = stateDefineStrategy.startState();
        for (int i = 0; i < numbers.size(); i++) {
            // 当前的一个状态
            String state = stateDefineStrategy.buildState(lastNumber, numbers.get(i));
            stateStructs.addHistoryStateCounts(state);
            // 上一个状态到当前状态的转移
            String stateJumpString = lastState.concat("_").concat(state);
            stateStructs.addStateJumpCounts(lastState, state);
            // 某个状态下是该组合
            stateStructs.addNumberCountForCurrentStates(stateJumpString, numbers.get(i));
            stateStructs.addJumpStateCount(stateJumpString);
            //
            lastNumber = numbers.get(i);
            lastState = state;
        }
        return stateStructs;
    }
}
