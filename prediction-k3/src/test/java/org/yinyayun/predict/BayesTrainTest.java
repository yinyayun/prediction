/**
 * 
 */
package org.yinyayun.predict;

import java.util.Arrays;
import java.util.List;

import org.yinyayun.prediction.k3.bayes.BayesTrainModel;
import org.yinyayun.prediction.preprocess.GenerateTrainDataSum;
import org.yinyayun.prediction.preprocess.common.DataMapper;

/**
 * @author yinyayun
 *
 */
public class BayesTrainTest {
	public static void main(String[] args) throws Exception {
		int pastnumber = 8;
		List<DataMapper> datas = new GenerateTrainDataSum("data/train-cp-20.txt").generate(pastnumber);
		BayesTrainModel model = new BayesTrainModel();
		model.train(datas);
		model.saveMode("data/bayes.model");
		model.loadModel("data/bayes.model");
		int[] randoms = { 10, 500, 600, 200, 700, 800 };
		for (int random : randoms) {
			DataMapper dataMapper = datas.get(random);
			int[] inputs = dataMapper.getInput();
			int out = dataMapper.getLabel();
			int[] y = model.predict1(inputs);
			System.out.println("===========");
			System.out.println("输入：" + Arrays.toString(inputs));
			System.out.println("理论输出：" + out + " 实际输出：" + Arrays.toString(y));
		}
	}
}
