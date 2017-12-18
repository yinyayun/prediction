/**
 * 
 */
package org.yinyayun.prediction.k3.maxent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yinyayun.prediction.k3.Model;
import org.yinyayun.prediction.k3.bayes.OutPutProbability;
import org.yinyayun.prediction.preprocess.common.DataSample;
import org.yinyayun.prediction.preprocess.common.DataSet;

/**
 * @author yinyayun
 *
 */
public class MaxEntModel implements Model {
	private List<DataSample> samples;
	private Map<Key, Integer> keyPos = new HashMap<Key, Integer>();
	private List<Key> featureList = new ArrayList<Key>();
	private List<Integer> featureCount = new ArrayList<Integer>();
	private List<Integer> labels = new ArrayList<Integer>();
	double[] weight;
	int maxFeatureSize;

	public void train(DataSet dataSet, int maxIt) {
		samples = dataSet.getSamples();
		for (DataSample sample : samples) {
			int label = sample.getLabel();
			List<Integer> featurs = new ArrayList<Integer>();
			for (int input : sample.getInput()) {
				featurs.add(input);
				Key key = new Key(input, label);
				Integer pos = keyPos.get(key);
				if (pos == null) {
					featureList.add(key);
					featureCount.add(1);
					keyPos.put(key, featureList.size() - 1);
				} else {
					featureCount.set(pos, featureCount.get(pos) + 1);
				}
			}
			if (sample.getInput().length > maxFeatureSize)
				maxFeatureSize = sample.getInput().length;
			if (labels.indexOf(label) == -1)
				labels.add(label);
		}
		int size = featureList.size();
		weight = new double[size]; // 特征权重
		double[] empiricalE = new double[size]; // 经验期望
		double[] modelE = new double[size]; // 模型期望
		for (int i = 0; i < size; ++i) {
			empiricalE[i] = (double) featureCount.get(i) / samples.size();
		}

		double[] lastWeight = new double[weight.length]; // 上次迭代的权重
		for (int i = 0; i < maxIt; ++i) {
			System.out.println("iterator " + i);
			modelExpect(modelE);
			for (int w = 0; w < weight.length; w++) {
				lastWeight[w] = weight[w];
				weight[w] += 1.0 / maxFeatureSize * Math.log(empiricalE[w] / modelE[w]);
			}
			if (checkConvergence(lastWeight, weight))
				break;
		}
	}

	/**
	 * 预测类别
	 * 
	 * @param fieldList
	 * @return
	 */
	public int[] predict(int[] inputs) {
		double[] prob = prob(inputs);
		List<OutPutProbability> outPutProbabilities = new ArrayList<OutPutProbability>();
		for (int i = 0; i < prob.length; ++i) {
			outPutProbabilities.add(new OutPutProbability(labels.get(i), (float) prob[i]));
		}
		Collections.sort(outPutProbabilities);
		int[] ret = new int[Math.min(3, outPutProbabilities.size())];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = outPutProbabilities.get(i).getOut();
		}
		return ret;
	}

	/**
	 * 检查是否收敛
	 * 
	 * @param w1
	 * @param w2
	 * @return 是否收敛
	 */
	public boolean checkConvergence(double[] w1, double[] w2) {
		for (int i = 0; i < w1.length; ++i) {
			if (Math.abs(w1[i] - w2[i]) >= 0.01) // 收敛阀值0.01可自行调整
				return false;
		}
		return true;
	}

	/**
	 * 模型期望
	 * 
	 * @param modelE
	 */
	public void modelExpect(double[] modelE) {
		Arrays.fill(modelE, 0.0f);
		for (DataSample sample : samples) {
			int[] inputs = sample.getInput();
			// 计算当前样本X对应所有类别的概率
			double[] pro = prob(inputs);
			for (int input : inputs) {
				for (int i = 0; i < labels.size(); i++) {
					Key key = new Key(input, labels.get(i));
					Integer pos = keyPos.get(key);
					if (pos != null)
						modelE[pos] += pro[i] * (1.0 / samples.size());
				}
			}
		}
	}

	/**
	 * 计算p(y|x)
	 * 
	 */
	public double[] prob(int[] inputs) {
		double[] p = new double[labels.size()];
		double sum = 0; // 正则化因子，保证概率和为1
		for (int i = 0; i < labels.size(); ++i) {
			double weightSum = 0;
			for (int input : inputs) {
				Key key = new Key(input, labels.get(i));
				Integer pos = keyPos.get(key);
				if (pos != null)
					weightSum += weight[pos];
			}
			p[i] = Math.exp(weightSum);
			sum += p[i];
		}
		for (int i = 0; i < p.length; ++i) {
			p[i] /= sum;
		}
		return p;
	}

	@Override
	public void train(DataSet datas) {
		train(datas, 100);
	}

	public class Key {
		Integer k;
		Integer v;

		public Key(Integer k, Integer v) {
			super();
			this.k = k;
			this.v = v;
		}

		@Override
		public String toString() {
			return super.toString();
		}
	}
}
