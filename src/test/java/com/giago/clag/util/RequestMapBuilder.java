package com.giago.clag.util;

import java.util.HashMap;
import java.util.Map;

public class RequestMapBuilder {

	private Map<String, String[]> map  = new HashMap<String, String[]>();
	
	public RequestMapBuilder add(String key) {
		map.put(key, null);
		return this;
	}
	
	public RequestMapBuilder add(String key, String value) {
		if(value == null) {
			map.put(key, null);				
		} else {
			map.put(key, new String[]{value});
		}
		return this;
	}

	public Map<String, String[]> build() {
		return map;
	}
	
}
