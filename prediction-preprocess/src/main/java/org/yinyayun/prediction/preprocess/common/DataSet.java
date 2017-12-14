/**
 * 
 */
package org.yinyayun.prediction.preprocess.common;

import java.util.List;

/**
 * @author yinyayun
 *
 */
public class DataSet {
	private List<DataSample> samples;
	/**
	 * 输出标签数量
	 */
	private int labels;
	// label的最小值
	private int minLabelValue;

	private int dataSize;

	/**
	 * @param samples
	 * @param labels
	 * @param minLabelValue
	 */
	public DataSet(List<DataSample> samples, int labels, int minLabelValue) {
		super();
		this.samples = samples;
		this.labels = labels;
		this.minLabelValue = minLabelValue;
		this.dataSize = samples.size();
	}

	/**
	 * @return the samples
	 */
	public List<DataSample> getSamples() {
		return samples;
	}

	public DataSample getDataSample(int i) {
		return samples.get(i);
	}

	/**
	 * @return the labels
	 */
	public int getLabels() {
		return labels;
	}

	/**
	 * @return the minLabelValue
	 */
	public int getMinLabelValue() {
		return minLabelValue;
	}

	/**
	 * @return the dataSize
	 */
	public int getDataSize() {
		return dataSize;
	}
}
