/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.prediction.k3.spark.data;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.yinyayun.prediction.k3.spark.LabelMapper;

/**
 * LabelPointDataLoader.java
 *
 * @author yinyayun
 */
public class LabelPointDataLoader implements DataLoader<LabeledPoint>, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public List<LabeledPoint> loadData(String dataPath) throws Exception {
        Map<String, Integer> numberToLabelMapper = LabelMapper.numberToLabels;
        List<String> allLines = Files.readAllLines(new File(dataPath).toPath(), Charset.forName("utf-8"));
        return allLines.stream().map(x -> {
            String[] splits = x.split("##");
            return new LabeledPoint(Double.valueOf(numberToLabelMapper.get(splits[0].trim())),
                    stringToDoubleArray(splits[1].trim()));
        }).collect(Collectors.toList());
    }

    private Vector stringToDoubleArray(String feature) {
        String[] featureArray = feature.split(",");
        double[] array = new double[featureArray.length];
        for (int i = 0; i < featureArray.length; i++) {
            array[i] = Double.valueOf(featureArray[i]);
        }
        return Vectors.dense(array);
    }
}
