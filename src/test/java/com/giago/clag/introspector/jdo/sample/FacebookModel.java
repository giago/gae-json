package com.giago.clag.introspector.jdo.sample;

import java.util.Date;

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.giago.clag.introspector.annotation.Clag;


@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class FacebookModel extends Model {
	
	private static final long serialVersionUID = 1L;
	
	@Persistent private String facebookId;
	@Persistent private Date date;
	
	@Clag(from="Page",include=true)
	@Persistent 
	private Long pageId;
	
	public String getFacebookId() { return facebookId; }
	public void setFacebookId(String facebookId) { this.facebookId = facebookId;}
	
	public void setPageId(Long pageId) { this.pageId = pageId; }
	public Long getPageId() { return pageId; }
	
	public void setDate(Date date) { this.date = date; }
	public Date getDate() { return date; }

}
