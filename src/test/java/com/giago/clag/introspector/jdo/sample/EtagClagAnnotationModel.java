package com.giago.clag.introspector.jdo.sample;

import com.giago.clag.introspector.annotation.Clag;
import com.giago.clag.model.MetaEntity.OnConflictPolicy;


public class EtagClagAnnotationModel {
	
	@Clag(unique=true,key=true,onConflictPolicy=OnConflictPolicy.REPLACE)
	private Long id;
	
	@Clag(etag=true)
	private Long modified;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setModified(Long modified) {
		this.modified = modified;
	}

	public Long getModified() {
		return modified;
	}
	
}
