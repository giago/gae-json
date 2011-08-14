package com.giago.clag.introspector.jdo.sample;

import com.giago.clag.introspector.annotation.Clag;
import com.giago.clag.model.MetaEntity.OnConflictPolicy;


public class PersistUserIdClagAnnotationModel {
	
	@Clag(unique=true,key=true,onConflictPolicy=OnConflictPolicy.REPLACE)
	private Long id;
	
	@Clag
	private String field;
	
	@Clag(persistUserId=true,hidden=true)
	private String userId;
	
	@Clag
	private Boolean enabled;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getField() {
		return field;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getEnabled() {
		return enabled;
	}
	
}
