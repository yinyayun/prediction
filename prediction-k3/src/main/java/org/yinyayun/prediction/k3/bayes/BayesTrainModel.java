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

import org.json.JSONObject;
import org.yinyayun.prediction.preprocess.common.DataMapper;

/**
 * @author yinyayun 贝叶斯模型
 */
public class BayesTrainModel {
	private Map<Integer, Float> outPutProbabilitys;
	private Map<Integer, Map<Integer, Float>> outPutConditionProbability;

	public static void main(String[] args) throws Exception {
		// List<DataMapper> datas = new
		// GenerateTrainDataSum("data/train-cp-20.txt").generate(5);
		BayesTrainModel model = new BayesTrainModel();
		// model.train(datas);
		// model.saveMode("data/bayes.model");
		model.loadmodel("data/bayes.model");
		model.predict(new int[] { 5, 4, 5, 6, 3 });
	}

	public int predict(int[] inputs) {
		List<OutPutProbability> outPutProbabilities = new ArrayList<OutPutProbability>();
		for (int i = 3; i <= 18; i++) {
			// 输出为i的概率
			float outi = outPutProbabilitys.get(i);
			Map<Integer, Float> inputProbabilitys = outPutConditionProbability.get(i);
			// 输出是i的条件下出险inputs的概率
			float condiction = 1f;
			for (int input : inputs) {
				condiction *= inputProbabilitys.get(input);
			}
			outPutProbabilities.add(new OutPutProbability(i, condiction * outi));
		}
		Collections.sort(outPutProbabilities);
		return outPutProbabilities.get(0).getOut();
	}

	public void train(List<DataMapper> datas) {
		ProbabilityDistribution probabilityDistribution = new ProbabilityDistribution(datas);
		// 输出标签对应的概率分布
		this.outPutProbabilitys = probabilityDistribution.outPutProbability();
		// 输出标签在每个分量上的条件概率分布
		this.outPutConditionProbability = probabilityDistribution.outPutConditionProbability();
	}

	public void saveMode(String savePath) throws IOException {
		Map<String, Map> map = new HashMap<String, Map>();
		map.put("outPutProbabilitys", outPutProbabilitys);
		map.put("outPutConditionProbability", outPutConditionProbability);
		JSONObject jsonObject = new JSONObject(map);
		Files.write(new File(savePath).toPath(), jsonObject.toString().getBytes(), StandardOpenOption.CREATE);
	}

	public void loadmodel(String savePath) throws IOException {
		List<String> lines = Files.readAllLines(new File(savePath).toPath());
		if (lines != null && lines.size() > 0) {
			JSONObject json = new JSONObject(lines.get(0));
			JSONObject outPutProbabilitysJson = json.getJSONObject("outPutProbabilitys");
			JSONObject outPutConditionProbabilityJson = json.getJSONObject("outPutConditionProbability");
			//
			this.outPutProbabilitys = new HashMap<Integer, Float>();
			for (String key : outPutProbabilitysJson.keySet()) {
				outPutProbabilitys.put(Integer.valueOf(key), (float) outPutProbabilitysJson.getDouble(key));
			}
			//
			this.outPutConditionProbability = new HashMap<Integer, Map<Integer, Float>>();
			for (String key : outPutConditionProbabilityJson.keySet()) {
				Map<Integer, Float> map = new HashMap<Integer, Float>();
				JSONObject jsonObject = outPutConditionProbabilityJson.getJSONObject(key);
				for (String innerKey : jsonObject.keySet()) {
					map.put(Integer.valueOf(innerKey), (float) jsonObject.getDouble(innerKey));
				}
				outPutConditionProbability.put(Integer.valueOf(key), map);
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
