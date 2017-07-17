/**
 * Copyright (c) 2017, yayunyin@126.com All Rights Reserved
 */

package org.yinyayun.prediction.k3.hmm.state;

import java.util.List;

/**
 * StateParserStragegy
 *
 * @author yinyayun
 */
public class StateParserStragegy {
    private int byHistoryNumber;
    private StateDefineStrategy stateDefineStrategy;

    public StateParserStragegy(StateDefineStrategy stateDefineStrategy, int byHistoryNumber) {
        this.byHistoryNumber = byHistoryNumber;
        this.stateDefineStrategy = stateDefineStrategy;
    }

    /**
     * 假设只关心前一个状态
     * 
     * @param numbers
     */
    public StateStructs parser(List<int[]> numbers) {
        if (numbers.size() < byHistoryNumber) {
            throw new IllegalArgumentException("历史数据太小...");
        }
        StateStructs stateStructs = new StateStructs(numbers.size());
        String lastState = stateDefineStrategy.buildLastState(numbers.subList(0, byHistoryNumber));
        for (int i = byHistoryNumber; i < numbers.size(); i++) {
            // 当前的一个状态
            String currentState = stateDefineStrategy.buildCurrentState(numbers.get(i));
            // 历史状态计数
            stateStructs.addLastStateCounts(lastState);
            // 上一个状态到当前状态的转移计数
            String stateJumpString = lastState.concat("_").concat(currentState);
            stateStructs.addStateJumpCounts(lastState, currentState);
            stateStructs.addJumpStateCount(stateJumpString);
            // 上一个状态转移到该状态并且是号码组合的计数
            stateStructs.addNumberCountForCurrentStates(stateJumpString, numbers.get(i));
            //
            lastState = stateDefineStrategy.buildLastState(numbers.subList(i - byHistoryNumber + 1, i + 1));
        }
        return stateStructs;
    }
}
