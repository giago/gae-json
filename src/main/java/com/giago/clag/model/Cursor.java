package com.giago.clag.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luigi.agosti
 */
public class Cursor {

	private List<Map<String,Object>> rows;
	
	private Map<String, Object> currentRow;
	
	private String name;
	
	public Cursor(String name) {
		this.name = name;
		rows = new ArrayList<Map<String,Object>>();
		currentRow = new LinkedHashMap<String, Object>();
	}

	public Cursor(String name, Map<String, Object> row) {
		this(name);
		addRow(row);
	}

	public Cursor() {
		this(null);
	}
	
	public void add(String key, Object value) {
		currentRow.put(key, value);
	}
	
	public void next() {
		rows.add(currentRow);
		currentRow = new HashMap<String, Object>();
	}
	
	public List<Map<String, Object>> getRows() {
		return rows;
	}
	
	public Object getValueOfCurrentRow(String property) {
		return currentRow.get(property);
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void addRow(Map<String, Object> row) {
		currentRow = row;
		next();
	}
}
