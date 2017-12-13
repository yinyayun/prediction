/**
 * 
 */
package org.yinyayun.prediction.k3.bayes;

/**
 * @author yinyayun
 *
 */
public class OutPutProbability implements Comparable<OutPutProbability> {
	private int out;
	private float probability;

	public OutPutProbability(int out, float probability) {
		super();
		this.out = out;
		this.probability = probability;
	}

	/**
	 * @return the out
	 */
	public int getOut() {
		return out;
	}

	/**
	 * @return the probability
	 */
	public float getProbability() {
		return probability;
	}

	@Override
	public int compareTo(OutPutProbability o) {
		if (o.probability > probability) {
			return 1;
		} else if (o.probability < probability) {
			return -1;
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OutPutProbability [out=" + out + ", probability=" + probability + "]";
	}

}
