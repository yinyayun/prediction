/**
 * 
 */
package org.yinyayun.prediction.k3;

import org.yinyayun.prediction.preprocess.common.DataSet;

/**
 * @author yinyayun
 *
 */
public interface Model {
	public void train(DataSet datas);
}
