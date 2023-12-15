package com.my.qfc.service.util;

import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

public class ReadProperties {
	ResourceBundle rd = ResourceBundle.getBundle("path");
	Properties properties = new Properties();

	public ReadProperties() {
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream("path.properties");
		try {
			properties.load(stream);

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private static class LazyHolder {
		private static final ReadProperties INSTANCE = new ReadProperties();
	}

	public static ReadProperties getInstance() {
		return LazyHolder.INSTANCE;
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public Set<String> getAllPropertyNames() {
		return properties.stringPropertyNames();
	}

	public boolean containsKey(String key) {
		return properties.containsKey(key);
	}

	public static String getInputPath() {
		return ReadProperties.getInstance().getProperty("path.readPath");
	}

	public static String getArchivePath() {
		return ReadProperties.getInstance().getProperty("path.archivePath");
	}

	public static String getErrorPath() {
		return null;
	}
}
