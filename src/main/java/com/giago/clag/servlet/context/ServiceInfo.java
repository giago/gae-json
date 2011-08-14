package com.giago.clag.servlet.context;

import java.util.List;

public class ServiceInfo {
	
	private String name;
	
	private String version;
	
	private List<String> gets;

	private List<String> puts;
	
	private List<String> posts;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<String> getGets() {
		return gets;
	}

	public void setGets(List<String> gets) {
		this.gets = gets;
	}

	public List<String> getPuts() {
		return puts;
	}

	public void setPuts(List<String> puts) {
		this.puts = puts;
	}

	public List<String> getPosts() {
		return posts;
	}

	public void setPosts(List<String> posts) {
		this.posts = posts;
	}

	public List<String> getDeletes() {
		return deletes;
	}

	public void setDeletes(List<String> deletes) {
		this.deletes = deletes;
	}

	private List<String> deletes;

}
