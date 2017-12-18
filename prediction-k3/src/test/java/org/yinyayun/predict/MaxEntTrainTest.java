/**
 * 
 */
package org.yinyayun.predict;

import java.util.Arrays;

import org.yinyayun.prediction.k3.maxent.MaxEntModel;
import org.yinyayun.prediction.preprocess.GenerateTrainDataSum;
import org.yinyayun.prediction.preprocess.common.DataSample;
import org.yinyayun.prediction.preprocess.common.DataSet;

/**
 * @author yinyayun
 *
 */
public class MaxEntTrainTest {
	public static void main(String[] args) throws Exception {
		int pastnumber = 8;
		DataSet datas = new GenerateTrainDataSum("data/train-cp-20.txt").generate(pastnumber);
		MaxEntModel model = new MaxEntModel();
		model.train(datas, 10);
		int[] randoms = { 10, 500, 600, 200, 700, 800, 10000, 10001, 10002 };
		for (int random : randoms) {
			DataSample dataMapper = datas.getDataSample(random);
			int[] inputs = dataMapper.getInput();
			int out = dataMapper.getLabel();
			int[] ret = model.predict(inputs);
			System.out.println("===========");
			System.out.println("输入：" + Arrays.toString(inputs));
			System.out.println("理论输出：" + out + " 实际输出：" + Arrays.toString(ret));
		}
	}
}
