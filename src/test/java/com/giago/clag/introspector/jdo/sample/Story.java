package com.giago.clag.introspector.jdo.sample;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.giago.clag.introspector.annotation.Clag;


/**
 * @author Luigi Agosti <luigi.agosti@gmail.com>
 */
@PersistenceCapable
public class Story extends FacebookModel {
	
	private static final long serialVersionUID = 1L;
	
	@Persistent private String mediaHref;
	@Persistent private String mediaImageHref;
	@Persistent private String title;
	@Persistent private String caption;
	@Persistent private String copy;
	
	@Clag(hidden=true) 
	@Persistent 
	private String groupId;
	
	public String getMediaHref() {return mediaHref; }
	public void setMediaHref(String mediaHref) { this.mediaHref = mediaHref;}

	public String getMediaImageHref() { return mediaImageHref; }
	public void setMediaImageHref(String mediaImageHref) { this.mediaImageHref = mediaImageHref; }

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

	public String getCaption() { return caption; }
	public void setCaption(String caption) { this.caption = caption; }

	public String getCopy() { return copy; }
	public void setCopy(String copy) {this.copy = copy; }
	
	public void setGroupId(String groupId) { this.groupId = groupId; }
	public String getGroupId() { return groupId; }

	public boolean isStoryComplete() {
		return isNotBlank(getFacebookId()) && isNotBlank(title) && isNotBlank(copy);
	}
	
	private boolean isNotBlank(String property) {
		if(property == null || property.length() == 0){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Story title : " + getFacebookId());
		return sb.toString();
	}

}
