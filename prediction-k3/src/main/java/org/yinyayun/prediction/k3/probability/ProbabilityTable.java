/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.prediction.k3.probability;

import java.util.HashMap;
import java.util.Map;

/**
 * ProbabilityTable.java 概率表：（1）某组合在某个状态下的概率（2）某个状态到某个状态的概率
 * 
 * @author yinyayun
 */
public class ProbabilityTable {
    // 状态转移概率
    private Map<String, Double> stateJumpProbability = new HashMap<String, Double>();
}
