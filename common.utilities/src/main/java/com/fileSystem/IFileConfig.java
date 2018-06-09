package com.fileSystem;

import java.util.List;

import org.json.JSONObject;

public interface IFileConfig {
	
	public void loadFileConfig(String path, String fileConfig, List<String> parameters) ;
	public boolean loadStatus();
	public JSONObject getValues();
	
}
