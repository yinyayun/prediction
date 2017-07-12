/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.prediction.k3.state;

/**
 * StateDefineStrategy.java 状态定义可能存在多种方案
 * 
 * @author yinyayun
 */
public interface StateDefineStrategy {
    /**
     * 生成状态
     * 
     * @param lastNumbers
     * @param currentNumbers
     */
    public String buildState(int[] lastNumbers, int[] currentNumbers);

    public String startState();
}
