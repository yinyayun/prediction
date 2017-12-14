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
	 * <p>
	 * 给定输出下对应输入的分向量位置上值对应的概率分布 。如：(x1,x2,x3)->y，计算在y的条件下x(2)=x2的概率
	 * </p>
	 * key:output value:input->int[];(该input出现在每个分量位置上的次数)
	 * 
	 * @return
	 */
	public Map<Integer, Map<Integer, Float>> outPutConditionProbability() {
		Map<Integer, Integer> outputCounts = new HashMap<Integer, Integer>();
		Map<Integer, Map<Integer, Integer>> outPutConditionCounts = new HashMap<Integer, Map<Integer, Integer>>();
		for (DataMapper dataMapper : datas) {
			int out = dataMapper.getLabel();
			// 输出标签计数
			Integer count = outputCounts.get(out);
			if (count == null) {
				count = 0;
			}
			outputCounts.put(out, ++count);
			// 输出标签对应每个输入分量的计数
			Map<Integer, Integer> inputsCounts = outPutConditionCounts.get(out);
			if (inputsCounts == null) {
				inputsCounts = new HashMap<Integer, Integer>();
				outPutConditionCounts.put(out, inputsCounts);
			}
			int[] inputs = dataMapper.getInput();
			for (int i = 0; i < inputs.length; i++) {
				Integer inputCount = inputsCounts.get(inputs[i]);
				if (inputCount == null) {
					inputCount = 0;
				}
				inputsCounts.put(inputs[i], ++inputCount);
			}
		}
		Map<Integer, Map<Integer, Float>> outPutConditionProbabilitys = new HashMap<Integer, Map<Integer, Float>>();
		for (Entry<Integer, Map<Integer, Integer>> entry : outPutConditionCounts.entrySet()) {
			int output = entry.getKey();
			int outputCount = outputCounts.get(output);
			Map<Integer, Float> inputProbabilitys = new HashMap<Integer, Float>();
			outPutConditionProbabilitys.put(output, inputProbabilitys);
			for (Entry<Integer, Integer> inputsEntry : entry.getValue().entrySet()) {
				int inputi = inputsEntry.getKey();
				int inputiCountOnOut = inputsEntry.getValue();
				float probabilitys = inputiCountOnOut / (outputCount * 1.0f);
				inputProbabilitys.put(inputi, probabilitys);
			}
		}
		return outPutConditionProbabilitys;
	}

	/**
	 * <p>
	 * 给定输出下对应输入的分向量位置上值对应的概率分布 。如：(x1,x2,x3)->y，计算在y的条件下x(2)=x2的概率
	 * </p>
	 * key:output value:input->int[];(该input出现在每个分量位置上的次数)
	 * 
	 * @return
	 */
	public Map<Integer, Map<Integer, float[]>> outPutConditionProbabilitys() {
		Map<Integer, Integer> outputCounts = new HashMap<Integer, Integer>();
		Map<Integer, Map<Integer, int[]>> outPutConditionCounts = new HashMap<Integer, Map<Integer, int[]>>();
		for (DataMapper dataMapper : datas) {
			int out = dataMapper.getLabel();
			// 输出标签计数
			Integer count = outputCounts.get(out);
			if (count == null) {
				count = 0;
			}
			outputCounts.put(out, ++count);
			// 输出标签对应每个输入分量的计数
			Map<Integer, int[]> inputsCounts = outPutConditionCounts.get(out);
			if (inputsCounts == null) {
				inputsCounts = new HashMap<Integer, int[]>();
				outPutConditionCounts.put(out, inputsCounts);
			}
			int[] inputs = dataMapper.getInput();
			for (int i = 0; i < inputs.length; i++) {
				int[] inputCount = inputsCounts.get(inputs[i]);
				if (inputCount == null) {
					inputCount = new int[inputs.length];
					inputsCounts.put(inputs[i], inputCount);
				}
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
