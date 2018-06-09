package com.services;

import java.util.List;

import org.json.JSONObject;

public interface IInitComponents {
	
	public JSONObject getResourceConfig(List<String> params);
	public JSONObject getAppConfig(String path, String fileConfig, String cloudName, List<String> params);

}
