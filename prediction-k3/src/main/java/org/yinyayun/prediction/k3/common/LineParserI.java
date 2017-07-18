/**
 * Copyright (c) 2016, yayunyin@126.com All Rights Reserved
 */
package org.yinyayun.prediction.k3.common;

/**
 * LineParserI.java
 *
 * @author yinyayun
 */
@FunctionalInterface
public interface LineParserI<T> {
    public T apply(String line);
}
