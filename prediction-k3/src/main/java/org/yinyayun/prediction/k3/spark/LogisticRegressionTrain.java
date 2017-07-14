/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.prediction.k3.spark;

import java.io.Serializable;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.classification.LogisticRegressionModel;
import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.yinyayun.prediction.k3.spark.data.DataLoader;
import org.yinyayun.prediction.k3.spark.data.LabelPointDataLoader;

import scala.Tuple2;

/**
 * SvmTrain.java
 *
 * @author yinyayun
 */
public class LogisticRegressionTrain implements Serializable {
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        LogisticRegressionTrain logisticRegressionTrain = new LogisticRegressionTrain();
        new LogisticRegressionTrain().train(logisticRegressionTrain.createSparkConf());
    }

    public SparkConf createSparkConf() {
        SparkConf sparkConf = new SparkConf();
        sparkConf.setMaster("spark://192.168.50.65:7077");
        sparkConf.setAppName("LogisticRegressionTrain");
        sparkConf.setJars(new String[]{"target/prediction-k3-0.0.1-SNAPSHOT.jar"});
        return sparkConf;
    }

    public void train(SparkConf conf) {
        DataLoader<LabeledPoint> dataLoader = new LabelPointDataLoader();
        try (JavaSparkContext sparkContext = new JavaSparkContext(conf)) {
            JavaRDD<LabeledPoint> labelPointRDD = sparkContext.parallelize(dataLoader.loadData("data/train-cp-20.txt"));
            JavaRDD<LabeledPoint>[] rddSplits = labelPointRDD.randomSplit(new double[]{0.95d, 0.05d});
            // 构建模型训练
            LogisticRegressionWithLBFGS lbfgs = new LogisticRegressionWithLBFGS().setNumClasses(6 * 6 * 6);
            LogisticRegressionModel model = lbfgs.run(rddSplits[0].rdd());
            // 验证
            JavaRDD<Tuple2<Object, Object>> scores = rddSplits[1]
                    .map(x -> new Tuple2<Object, Object>(model.predict(x.features()), x.label()));
            System.out.println(String.format("准确率：%s", new MulticlassMetrics(scores.rdd()).accuracy()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
