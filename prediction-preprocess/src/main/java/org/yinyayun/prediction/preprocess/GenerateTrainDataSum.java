/**
 * 
 */
package org.yinyayun.prediction.preprocess;

import java.util.ArrayList;
import java.util.List;

import org.yinyayun.prediction.preprocess.common.DataMapper;

/**
 * @author yinyayun 根据历史N期的和值预测下一期的和值
 */
public class GenerateTrainDataSum {

	private String dataPath;

	public static void main(String[] args) throws Exception {
		new GenerateTrainDataSum("D:\\GithubRepository\\prediction\\prediction-k3\\data\\train-cp-20.txt").generate(5);
	}

	public GenerateTrainDataSum(String dataPath) {
		this.dataPath = dataPath;
	}

	/**
	 * 是根据历史多少生成
	 * 
	 * @param pastNumber
	 * @throws Exception
	 */
	public List<DataMapper> generate(final int pastNumber) throws Exception {
		List<DataMapper> datas = new ArrayList<DataMapper>();
		try (DataReader<int[]> dataReader = new DataReader<>(dataPath)) {
			List<int[]> trainDatas = dataReader.readAllDatas();
			for (int i = pastNumber; i < trainDatas.size(); i++) {
				int output = sum(trainDatas.get(i));
				int[] inputs = new int[pastNumber];
				for (int j = i - pastNumber; j < i; j++) {
					int[] numbers = trainDatas.get(j);
					int sum = sum(numbers);
					inputs[j - i + pastNumber] = sum;
				}
				datas.add(new DataMapper(inputs, output));
			}
		}
		return datas;
	}

	private int sum(int[] array) {
		int sum = 0;
		for (int i : array) {
			sum += i;
		}
		return sum;
	}
}
