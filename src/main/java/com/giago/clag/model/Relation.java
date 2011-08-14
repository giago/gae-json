package com.giago.clag.model;

public class Relation {
	
	private String from;
	
	private String to;
	
	private String property;
	
	public Relation(String from, String to, String property) {
		this.from = from;
		this.to = to;
		this.property = property;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

}
