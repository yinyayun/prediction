/**
 * 
 */
package org.yinyayun.prediction.preprocess;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author yinyayun
 *
 */
public class DataReader<R> implements AutoCloseable {
	private BufferedReader reader;
	private String line;

	public DataReader(String path) {
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), Charset.forName("utf-8")),
					1024);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public boolean hasNext() throws IOException {
		this.line = reader.readLine();
		if (this.line != null) {
			return true;
		}
		return false;
	}

	public R readData(Function<String, R> function) {
		return function.apply(line);
	}

	public int[] readData() {
		return readData(this.line);
	}

	private int[] readData(String line) {
		String[] parts = line.split("##");
		if (parts.length == 0) {
			return null;
		}
		String str = parts[0];
		int[] data = new int[3];
		for (int i = 0; i < str.length(); i++) {
			data[i] = str.charAt(i) - '0';
		}
		return data;
	}

	public List<int[]> readAllDatas() throws IOException {
		List<int[]> datas = new ArrayList<int[]>();
		String line = null;
		while ((line = reader.readLine()) != null) {
			int[] data = readData(line);
			if (data != null) {
				datas.add(data);
			}
		}
		return datas;
	}

	@Override
	public void close() throws Exception {
		if (reader != null) {
			try {
				reader.close();
			} catch (Exception e) {
			}
		}
	}
}
