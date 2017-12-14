/**
 * 
 */
package org.yinyayun.prediction.k3.bayes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.yinyayun.prediction.preprocess.common.DataMapper;

/**
 * @author yinyayun 概率分布生成
 */
public class ProbabilityDistribution {
	private List<DataMapper> datas;

	public ProbabilityDistribution(List<DataMapper> datas) {
		this.datas = datas;
	}

	/**
	 * 输出标签的概率分布
	 * 
	 */
	public Map<Integer, Float> outPutProbability() {
		Map<Integer, Integer> counts = new HashMap<Integer, Integer>();
		for (DataMapper dataMapper : datas) {
			Integer count = counts.get(dataMapper.getLabel());
			if (count == null) {
				count = 0;
			}
			counts.put(dataMapper.getLabel(), ++count);
		}
		int dataSize = datas.size();
		Map<Integer, Float> probabilitys = new HashMap<Integer, Float>();
		for (Entry<Integer, Integer> entry : counts.entrySet()) {
			float probability = entry.getValue() / (dataSize * 1.0f);
			probabilitys.put(entry.getKey(), probability);
		}
		return probabilitys;
	}

	/**
	 * 输入分量的概率分布
	 * 
	 * @return
	 */
	public Map<Integer, Float> inputProbability() {
		float dataSize = 0;
		Map<Integer, Integer> counts = new HashMap<Integer, Integer>();
		for (DataMapper dataMapper : datas) {
			int[] inputs = dataMapper.getInput();
			for (int input : inputs) {
				Integer count = counts.get(input);
				if (count == null) {
					count = 0;
				}
				counts.put(input, ++count);
			}
			dataSize += inputs.length;
		}
		Map<Integer, Float> probabilitys = new HashMap<Integer, Float>();
		for (Entry<Integer, Integer> entry : counts.entrySet()) {
			float probability = entry.getValue() / dataSize;
			probabilitys.put(entry.getKey(), probability);
		}
		return probabilitys;
	}

	/**
	 * 在指定输出下，对应特征分量的条件概率
	 * 
	 * @return
	 */
	public Map<Integer, Map<Integer, float[]>> outPutConditionProbabilitys() {
		Map<Integer, Integer> outputCounts = new HashMap<Integer, Integer>();
		Map<Integer, Map<Integer, int[]>> outPutConditionCounts = new HashMap<Integer, Map<Integer, int[]>>();
		for (DataMapper dataMapper : datas) {
			int out = dataMapper.getLabel();
			// 输出标签计数
			outputCounts.compute(out, (k, v) -> v == null ? 0 : ++v);
			// 输出标签对应每个输入分量的计数
			outPutConditionCounts.putIfAbsent(out, new HashMap<Integer, int[]>());
			Map<Integer, int[]> inputsCounts = outPutConditionCounts.get(out);
			int[] inputs = dataMapper.getInput();
			for (int i = 0; i < inputs.length; i++) {
				// 每个特征出现在每个位置上的次数
				inputsCounts.putIfAbsent(inputs[i], new int[inputs.length]);
				int[] inputCount = inputsCounts.get(inputs[i]);
				inputCount[i]++;
			}
		}
		Map<Integer, Map<Integer, float[]>> outPutConditionProbabilitys = new HashMap<Integer, Map<Integer, float[]>>();
		for (Entry<Integer, Map<Integer, int[]>> entry : outPutConditionCounts.entrySet()) {
			int output = entry.getKey();
			int outputCount = outputCounts.get(output);
			Map<Integer, float[]> inputProbabilitys = new HashMap<Integer, float[]>();
			outPutConditionProbabilitys.put(output, inputProbabilitys);
			for (Entry<Integer, int[]> inputsEntry : entry.getValue().entrySet()) {
				int inputi = inputsEntry.getKey();
				int[] inputiCountOnOut = inputsEntry.getValue();
				float[] inputiCountOnOutPro = new float[inputiCountOnOut.length];
				for (int i = 0; i < inputiCountOnOut.length; i++) {
					inputiCountOnOutPro[i] = inputiCountOnOut[i] / (outputCount * 1.0f);
				}
				inputProbabilitys.put(inputi, inputiCountOnOutPro);
			}
		}
		return outPutConditionProbabilitys;
	}
}
