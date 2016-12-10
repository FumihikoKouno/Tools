package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
	private Map<String, Map<String, List<List<String>>>> config;

	public Config(File dir) {
		readConfig(dir);
	}

	public Config(String dir) {
		readConfig(new File(dir));
	}

	public void readConfig(File dir) {
		config = new HashMap<String, Map<String, List<List<String>>>>();
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File file : files) {
				List<String> lines = new ArrayList<String>();
				try(BufferedReader reader = 
						new BufferedReader(
								new InputStreamReader(
										new FileInputStream(file), "UTF-8"))) {
String line;
					while ((line = reader.readLine()) != null) {
						lines.add(line);
					}
				}catch(UnsupportedEncodingException e){
					e.printStackTrace();
					continue;
				}catch(FileNotFoundException e){
					e.printStackTrace();
					continue;
				}catch(IOException e){
					e.printStackTrace();
					continue;
				}
				Map<String, List<List<String>>> oneFileConfig = new HashMap<String, List<List<String>>>();
				for (String line : lines) {
					String[] keyAndValue = line.split(" ");
					List<String> values = new ArrayList<String>();
					for (int i = 1; i < keyAndValue.length; i++) {
						values.add(keyAndValue[i]);
					}
					if (oneFileConfig.containsKey(keyAndValue[0])) {
						oneFileConfig.get(keyAndValue[0]).add(values);
					} else {
						List<List<String>> list = new ArrayList<List<String>>();
						list.add(values);
						oneFileConfig.put(keyAndValue[0], list);
					}
				}
				config.put(file.getName(), oneFileConfig);
			}
		}
	}

	public Map<String, Map<String, List<List<String>>>> getConfig() {
		return config;
	}

	public Map<String, List<List<String>>> getConfig(String fileName) {
		return config.get(fileName);
	}

	public List<List<String>> getConfig(String fileName, String key) {
		return config.get(fileName).get(key);
	}
}
