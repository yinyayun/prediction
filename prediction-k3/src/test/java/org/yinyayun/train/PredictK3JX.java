/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.train;

import java.util.Arrays;
import java.util.List;

import org.yinyayun.prediction.k3.hmm.PredictNextNumberAndState;
import org.yinyayun.prediction.k3.hmm.PredictNextNumberAndState.PredictResult;

/**
 * PredictK3JX.java
 *
 * @author yinyayun
 */
public class PredictK3JX {
    public static void main(String[] args) {
        int[][] numbers = {{1, 1, 4}, {1, 2, 6}};
        List<PredictResult> res = new PredictNextNumberAndState(
                "C:/git-rep/prediction/prediction-k3/src/main/resources/cp-jx.txt", numbers.length,
                x -> x.substring(x.indexOf('\t') + 1).replace('\t', ',')).predict(numbers, 5);
        System.out.println(String.format("历史为：%s,下一期可能为：", Arrays.deepToString(numbers)));
        res.forEach(x -> System.out.println(x));
    }

}
