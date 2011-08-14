package com.giago.clag.model;

/**
 * In my view I will like to add all the limitation that the provider need
 * to enforce on queries, for example : 
 * - users
 * - location
 * - time
 * - and pagination of course
 * 
 * @author luigi.agosti
 *
 */
public class Options {
	
	public static final int DEFAULT_LIMIT = 100;
	
	public static final int DEFAULT_OFFSET = 0;
	
	public static final boolean DEFAULT_SUB_OBJECTS_FETCH = true;

	public static final long DEFAULT_ETAG = -1l;
	
	private int limit = DEFAULT_LIMIT;

	private int offset = DEFAULT_OFFSET;
	
	private long etag = DEFAULT_ETAG;
	
	private boolean subObjectFetch = DEFAULT_SUB_OBJECTS_FETCH;
	
	public static final Options getDefault() {
		return new Options();
	}
	
	public Options() {}
	
	public Options(int limit) {
		this.setLimit(limit);
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getLimit() {
		return limit;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
	
	public int getOffset() {
		return offset;
	}

	public void setSubObjectFetch(boolean subObjectFetch) {
		this.subObjectFetch = subObjectFetch;
	}

	public boolean isSubObjectFetch() {
		return subObjectFetch;
	}

	public void setEtag(long etag) {
		this.etag = etag;
	}

	public long getEtag() {
		return etag;
	}
	
}
