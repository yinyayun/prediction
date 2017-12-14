/**
 * 
 */
package org.yinyayun.prediction.preprocess.common;

/**
 * @author yinyayun 输入与输出的映射
 */
public class DataSample {
	private int[] input;
	private int label;

	public DataSample(int[] input, int label) {
		this.input = input;
		this.label = label;
	}

	/**
	 * @return the input
	 */
	public int[] getInput() {
		return input;
	}

	/**
	 * @return the label
	 */
	public int getLabel() {
		return label;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i : input) {
			if (builder.length() > 0) {
				builder.append(",");
			}
			builder.append(i);
		}
		builder.append("##").append(label);
		return builder.toString();
	}

}
