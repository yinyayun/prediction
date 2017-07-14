/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.prediction.k3.spark.data;

import java.io.Serializable;
import java.util.List;

import org.apache.spark.sql.Row;

/**
 * DataLoader.java
 *
 * @author yinyayun
 */
public interface DataLoader<T> {
    public List<T> loadData(String dataPath) throws Exception;
}
