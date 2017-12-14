/**
 * 
 */
package org.yinyayun.prediction.k3.bayes;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.yinyayun.prediction.preprocess.common.DataSample;
import org.yinyayun.prediction.preprocess.common.DataSet;

/**
 * @author yinyayun 概率分布生成
 */
public class ProbabilityDistribution {
	private DataSet dataSet;

	public ProbabilityDistribution(DataSet dataSet) {
		this.dataSet = dataSet;
	}

	/**
	 * 输出标签的概率分布
	 * 
	 */
	public Map<Integer, Float> outPutProbability() {
		Map<Integer, Integer> counts = new HashMap<Integer, Integer>();
		for (DataSample dataMapper : dataSet.getSamples()) {
			Integer count = counts.get(dataMapper.getLabel());
			if (count == null) {
				count = 0;
			}
			counts.put(dataMapper.getLabel(), ++count);
		}
		int dataSize = dataSet.getDataSize();
		Map<Integer, Float> probabilitys = new HashMap<Integer, Float>();
		for (Entry<Integer, Integer> entry : counts.entrySet()) {
			// 拉普拉斯平滑估计
			float probability = (entry.getValue() + 1.f) / (dataSize * 1.0f + dataSet.getLabels());
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
		for (DataSample dataMapper : dataSet.getSamples()) {
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
		for (DataSample dataMapper : dataSet.getSamples()) {
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
					// 拉普拉斯平滑估计
					inputiCountOnOutPro[i] = (inputiCountOnOut[i] + 1.f) / (outputCount * 1.0f + dataSet.getLabels());
				}
				inputProbabilitys.put(inputi, inputiCountOnOutPro);
			}
		}
		return outPutConditionProbabilitys;
	}

	/**
	 * 在指定输出下，对应特征分量的条件概率
	 * 
	 * @return
	 */
	public Map<Integer, Map<Integer, float[]>> outPutConditionProbabilitys2() {
		Map<Integer, Integer> outputCounts = new HashMap<Integer, Integer>();
		// label->pos->inputs
		Map<Integer, Map<Integer, int[]>> outPutConditionCounts = new HashMap<Integer, Map<Integer, int[]>>();
		for (DataSample dataMapper : dataSet.getSamples()) {
			int out = dataMapper.getLabel();
			// 输出标签计数
			outputCounts.compute(out, (k, v) -> v == null ? 0 : ++v);
			// 输出标签对应每个输入分量的计数
			outPutConditionCounts.putIfAbsent(out, new HashMap<Integer, int[]>());
			Map<Integer, int[]> inputsCounts = outPutConditionCounts.get(out);
			int[] inputs = dataMapper.getInput();
			for (int i = 0; i < inputs.length; i++) {
				// 每个特征出现在每个位置上的次数
				inputsCounts.putIfAbsent(i, new int[dataSet.getLabels()]);
				inputsCounts.get(i)[inputs[i] - dataSet.getMinLabelValue()]++;
			}
		}
		Map<Integer, Map<Integer, float[]>> outPutConditionProbabilitys = new HashMap<Integer, Map<Integer, float[]>>();
		for (Entry<Integer, Map<Integer, int[]>> entry : outPutConditionCounts.entrySet()) {
			int output = entry.getKey();
			int outputCount = outputCounts.get(output);
			// pos->inputcount
			Map<Integer, float[]> posInputsProbabilitys = new HashMap<Integer, float[]>();
			outPutConditionProbabilitys.put(output, posInputsProbabilitys);
			for (Entry<Integer, int[]> inputsEntry : entry.getValue().entrySet()) {
				int pos = inputsEntry.getKey();
				int[] inputCounts = inputsEntry.getValue();
				float[] inputProbabilitysOnCurrentPos = new float[dataSet.getLabels()];
				for (int i = 0; i < inputProbabilitysOnCurrentPos.length; i++) {
					inputProbabilitysOnCurrentPos[i] = inputCounts[i] / (outputCount * 1.0f);
				}
				posInputsProbabilitys.put(pos, inputProbabilitysOnCurrentPos);
			}
			for (Entry<Integer, float[]> inputProbabilityEntry : posInputsProbabilitys.entrySet()) {
				float all = 0f;
				for (float f : inputProbabilityEntry.getValue())
					all += f;
				System.out.println(all);
			}
		}
		return outPutConditionProbabilitys;
	}
}
