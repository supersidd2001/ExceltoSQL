package com.my.qfc.service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties {

	@Value("${input.folder}")
	private String inputFolder;

	@Value("${output.folder}")
	private String outputFolder;

	public String getInputFolder() {
		return inputFolder;
	}

	public String getOutputFolder() {
		return outputFolder;
	}
}
