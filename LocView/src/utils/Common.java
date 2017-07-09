package utils;

import java.awt.Dimension;
import java.util.List;
import java.util.Map;

public class Common {
	public Dimension windowSize;
	public Config config;

	public Common() {
		config = new Config("config");
		setConfig();
	}

	private void setConfig() {
		Map<String, List<List<String>>> commonConfig = config.getConfig("Common");
		List<List<String>> windowSizeList = commonConfig.get("WindowSize");
		int windowX = Integer.parseInt(windowSizeList.get(0).get(0));
		int windowY = Integer.parseInt(windowSizeList.get(0).get(1));
		windowSize = new Dimension(windowX, windowY);
	}
}
