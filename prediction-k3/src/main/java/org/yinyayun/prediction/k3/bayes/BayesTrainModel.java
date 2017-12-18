/**
 * 
 */
package org.yinyayun.prediction.k3.bayes;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.yinyayun.prediction.k3.Model;
import org.yinyayun.prediction.preprocess.common.DataSet;

/**
 * @author yinyayun 贝叶斯模型
 */
public class BayesTrainModel implements Model {
	private Map<Integer, Float> outPutProbabilitys;
	private Map<Integer, Map<Integer, float[]>> outPutConditionProbabilitys;

	public int[] predict1(int[] inputs) {
		List<OutPutProbability> outPutProbabilities = new ArrayList<OutPutProbability>();
		for (int i = 3; i <= 18; i++) {
			// 输出为i的概率
			float outi = outPutProbabilitys.get(i);
			// outi = 1;
			Map<Integer, float[]> inputProbabilitys = outPutConditionProbabilitys.get(i);
			// 输出是i的条件下出险inputs的概率
			float condiction = 1f;
			for (int j = 0; j < inputs.length; j++) {
				condiction *= inputProbabilitys.get(inputs[j])[j];
			}
			outPutProbabilities.add(new OutPutProbability(i, condiction * outi));
		}
		Collections.sort(outPutProbabilities);
		int[] ret = new int[Math.min(3, outPutProbabilities.size())];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = outPutProbabilities.get(i).getOut();
		}
		return ret;
	}

	@Override
	public void train(DataSet datas) {
		ProbabilityDistribution probabilityDistribution = new ProbabilityDistribution(datas);
		// 输出标签对应的概率分布
		this.outPutProbabilitys = probabilityDistribution.outPutProbability();
		// 输出标签在每个分量上的条件概率分布
		this.outPutConditionProbabilitys = probabilityDistribution.outPutConditionProbabilitys();
	}

	public void saveMode(String savePath) throws IOException {
		Map<String, Map> map = new HashMap<String, Map>();
		map.put("outPutProbabilitys", outPutProbabilitys);
		map.put("outPutConditionProbabilitys", outPutConditionProbabilitys);
		JSONObject jsonObject = new JSONObject(map);
		Files.write(new File(savePath).toPath(), jsonObject.toString().getBytes(), StandardOpenOption.CREATE);
	}

	public void loadModel(String savePath) throws IOException {
		List<String> lines = Files.readAllLines(new File(savePath).toPath());
		if (lines != null && lines.size() > 0) {
			JSONObject json = new JSONObject(lines.get(0));
			JSONObject outPutProbabilitysJson = json.getJSONObject("outPutProbabilitys");
			JSONObject outPutConditionProbabilitysJson = json.getJSONObject("outPutConditionProbabilitys");
			//
			this.outPutProbabilitys = new HashMap<Integer, Float>();
			for (String key : outPutProbabilitysJson.keySet()) {
				this.outPutProbabilitys.put(Integer.valueOf(key), (float) outPutProbabilitysJson.getDouble(key));
			}
			//
			this.outPutConditionProbabilitys = new HashMap<Integer, Map<Integer, float[]>>();
			for (String key : outPutConditionProbabilitysJson.keySet()) {
				Map<Integer, float[]> map = new HashMap<Integer, float[]>();
				JSONObject jsonObject = outPutConditionProbabilitysJson.getJSONObject(key);
				for (String innerKey : jsonObject.keySet()) {
					JSONArray array = jsonObject.getJSONArray(innerKey);
					float[] probabilitys = new float[array.length()];
					for (int i = 0; i < array.length(); i++) {
						probabilitys[i] = (float) array.getDouble(i);
					}
					map.put(Integer.valueOf(innerKey), probabilitys);
				}
				this.outPutConditionProbabilitys.put(Integer.valueOf(key), map);
			}
		}
	}

	class Probabilitys implements Serializable {
		private static final long serialVersionUID = 1L;
		private Map<Integer, Float> outPutProbabilitys;
		private Map<Integer, Map<Integer, Float>> outPutConditionProbability;

		public Probabilitys(Map<Integer, Float> outPutProbabilitys,
				Map<Integer, Map<Integer, Float>> outPutConditionProbability) {
			super();
			this.outPutProbabilitys = outPutProbabilitys;
			this.outPutConditionProbability = outPutConditionProbability;
		}

		public Map<Integer, Float> getOutPutProbabilitys() {
			return outPutProbabilitys;
		}

		public void setOutPutProbabilitys(Map<Integer, Float> outPutProbabilitys) {
			this.outPutProbabilitys = outPutProbabilitys;
		}

		public Map<Integer, Map<Integer, Float>> getOutPutConditionProbability() {
			return outPutConditionProbability;
		}

		public void setOutPutConditionProbability(Map<Integer, Map<Integer, Float>> outPutConditionProbability) {
			this.outPutConditionProbability = outPutConditionProbability;
		}

	}
}
